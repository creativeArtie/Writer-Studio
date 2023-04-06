package com.creativeartie.humming.document;

import java.util.*;
import java.util.Optional;
import java.util.concurrent.*;

import org.checkerframework.checker.nullness.qual.*;

import com.google.common.base.*;
import com.google.common.cache.*;
import com.google.common.collect.*;

/**
 * Stores information about the writing text files. This includes
 * <ul>
 * <li>list of id and their references
 * <li>list of styles and the ability to rebuild them
 * <li>list of children span and how to find them
 * <li>create caches for the spans.
 * </ul>
 *
 * @author wai
 */
public final class Manuscript extends ForwardingList<DivisionSecChapter> implements SpanParent {
    private static int getCacheIndex(Span span, boolean isStart) {
        List<Integer> targetIndexes = span.getRoot().findChild(span);
        ForwardingList<? extends Span> parent = span.getRoot();
        int length = 0;

        for (int targetIndex : targetIndexes) {

            for (int childIndex = 0; childIndex < targetIndex; childIndex++)
                length += parent.get(childIndex).getLength();

            if (parent.get(targetIndex) instanceof SpanLeaf)
                return length + (isStart ? 0 : parent.get(targetIndex).getLength());
            parent = (SpanBranch) parent.get(targetIndex);
        }
        return length + (isStart ? 0 : ((SpanBranch) parent).getLength());
    }

    private IdentityStorage idStorage;

    // Methods add more styles for ids
    private final ArrayList<DivisionSecChapter> docChildren;
    private LoadingCache<Span, List<Integer>> findChildCache;
    private LoadingCache<SpanBranch, Integer> lengthsCache;
    private LoadingCache<Span, Integer> startIdxCache, endIdxCache;
    private LoadingCache<Integer, List<Span>> locateChildrenCache;

    /**
     * Create a new instance of Manuscript
     */
    public Manuscript() {
        idStorage = new IdentityStorage();
        docChildren = new ArrayList<>();
        findChildCache = CacheBuilder.newBuilder().recordStats().build(new CacheLoader<Span, List<Integer>>() {
            @Override
            public List<Integer> load(Span key) {
                return getFindChildCache(key, ImmutableList.copyOf(docChildren));
            }
        });
        lengthsCache = CacheBuilder.newBuilder().recordStats().build(new CacheLoader<SpanBranch, Integer>() {
            @Override
            public Integer load(SpanBranch key) {
                return key.getCacheLength();
            }
        });
        startIdxCache = CacheBuilder.newBuilder().recordStats().build(new CacheLoader<Span, Integer>() {
            @Override
            public Integer load(Span key) {
                return getCacheIndex(key, true);
            }
        });
        endIdxCache = CacheBuilder.newBuilder().recordStats().build(new CacheLoader<Span, Integer>() {
            @Override
            public Integer load(Span key) {
                return getCacheIndex(key, false);
            }
        });
        locateChildrenCache = CacheBuilder.newBuilder().recordStats().build(new CacheLoader<Integer, List<Span>>() {
            @Override
            public List<Span> load(Integer key) {
                return getLocateChildrenCache(key, Manuscript.this);
            }
        });
    }

    @Override
    public boolean add(DivisionSecChapter child) {
        child.setParent(this);
        return docChildren.add(child);
    }

    @Override
    public boolean addAll(Collection<? extends DivisionSecChapter> c) {
        c.forEach(child -> child.setParent(this));
        return docChildren.addAll(c);
    }

    @Override
    public boolean cleanUp() {
        boolean changed = false;

        for (SpanBranch child : docChildren) changed = changed || child.cleanUp();
        return changed;
    }

    /**
     * Changes leaf spans.
     *
     * @param <T>
     *        the type to change to
     * @param convert
     *        conversion function
     *
     * @return the result
     *
     * @see #convertLines(Function)
     */
    public <T> List<T> convertLeaves(Function<SpanLeaf, T> convert) {
        ImmutableList.Builder<T> leaves = ImmutableList.builder();

        for (SpanBranch child : docChildren) for (SpanLeaf leaf : child.getLeafs()) leaves.add(convert.apply(leaf));
        return leaves.build();
    }

    /**
     * Changes lines spans.
     *
     * @param <T>
     *        the type to change to
     * @param convert
     *        conversion function
     *
     * @return the result
     *
     * @see #convertLeaves(Function)
     */
    public <T> List<T> convertLines(Function<Para, T> convert) {
        ImmutableList.Builder<T> lines = ImmutableList.builder();
        for (Division child : this) lines.addAll(convertLines(convert, child));
        return lines.build();
    }

    @Override
    public List<DivisionSecChapter> delegate() {
        return docChildren;
    }

    @Override
    public List<Integer> findChild(Span span) {

        try {
            return findChildCache.get(span);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return new ArrayList<>();
        }
    }

    @Override
    public <T> Optional<T> findParent(Class<T> clazz) {
        return Optional.empty();
    }

    @Override
    public int getEndIndex() {
        return getLength();
    }

    @Override
    public List<CssStyle> getInheritedStyles() {
        return ImmutableList.of();
    }

    @Override
    public List<SpanLeaf> getLeafs() {
        return convertLeaves(leaf -> leaf);
    }

    @Override
    public int getLength() {
        if (isEmpty()) return 0;
        return get(size() - 1).getEndIndex();
    }

    @Override
    public Optional<SpanParent> getParent() {
        return Optional.empty();
    }

    /**
     * Get ID's pointer count.
     *
     * @param span
     *        the id
     *
     * @return number of pointers
     */
    public int getPointerCount(IdentitySpan span) {
        return idStorage.getPointerCount(span);
    }

    @Override
    public Manuscript getRoot() {
        return this;
    }

    @Override
    public int getStartIndex() {
        return 0;
    }

    /**
     * Gets the manuscript text
     *
     * @return list of text
     */
    public String getText() {
        return Joiner.on("").join(convertLeaves(@Nullable SpanLeaf::getRefText));
    }

    /**
     * is the id unique?
     *
     * @param span
     *        id to test
     *
     * @return ID has only one pointer
     */
    public boolean isIdUnique(IdentitySpan span) {
        return idStorage.isIdUnique(span);
    }

    /**
     * Locate the children span at location
     *
     * @param location
     *
     * @return the list of children
     */
    public List<Span> locateChildren(int location) {

        try {
            return locateChildrenCache.get(location);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return new ArrayList<>();
        }
    }

    /**
     * Prints the cache stats
     */
    public void printCacheStats() {
        System.out.printf("findChildCache %s\n", findChildCache.stats());
        System.out.printf("  lengthsCache %s\n", lengthsCache.stats());
        System.out.printf(" startIdxCache %s\n", startIdxCache.stats());
        System.out.printf("   endIdxCache %s\n", endIdxCache.stats());
        System.out.printf("   locateCache %s\n", locateChildrenCache.stats());
    }

    /**
     * Update the text
     *
     * @param text
     */
    public void updateText(String text) {
        try {
            clear();

            findChildCache.invalidateAll();
            lengthsCache.invalidateAll();
            startIdxCache.invalidateAll();
            endIdxCache.invalidateAll();
            locateChildrenCache.invalidateAll();
            idStorage.clear();

            Division parent = new DivisionSecChapter(this);
            add((DivisionSecChapter) parent);
            List<String> texts = Splitter.on('\n').splitToList(text);
            int line = 1;

            for (String raw : texts) {
                raw += (line == texts.size() ? "" : "\n");
                Para span = Para.newLine(parent, raw);
                parent = parent.addLine(span, span.getLineStyle());
                line++;
            }
            cleanUp();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    void addId(IdentitySpan span) {
        idStorage.addId(span);
    }

    int getCacheEnd(Span span) {

        try {
            return endIdxCache.get(span);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return 0;
        }
    }

    int getCacheLength(SpanBranch span) {

        try {
            return lengthsCache.get(span);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return 0;
        }
    }

    int getCacheStart(Span span) {

        try {
            return startIdxCache.get(span);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return 0;
        }
    }

    List<Integer> getFindChildCache(Span span, List<Span> children) {
        ArrayList<Integer> answer = new ArrayList<>();
        int i = 0;

        for (Span child : children) {

            if (child == span) {
                answer = new ArrayList<>();
                answer.add(i);
                return answer;
            }

            if (child instanceof SpanParent) {
                List<Integer> addList = getFindChildCache(span, ((SpanBranch) child));

                if (!addList.isEmpty()) {
                    answer.add(i);
                    answer.addAll(addList);
                    return answer;
                }
            }
            i++;
        }
        return answer;
    }

    void removeId(IdentitySpan id) {
        idStorage.removeId(id);
    }

    private <T> List<T> convertLines(Function<Para, T> convert, Division div) {
        ImmutableList.Builder<T> lines = ImmutableList.builder();
        for (Span child : div) if (child instanceof Division) lines.addAll(convertLines(convert, (Division) child));
        else if (child instanceof Para) lines.add(convert.apply((Para) child));
        return lines.build();
    }

    private List<Span> getLocateChildrenCache(int location, List<? extends Span> children) {
        ImmutableList.Builder<Span> answer = ImmutableList.builder();

        for (Span child : children) if (child.getEndIndex() > location) {
            answer.add(child);

            if (child instanceof SpanParent) {
                answer.addAll(getLocateChildrenCache(location, (SpanBranch) child));
            }
            return answer.build();
        }
        return answer.build();
    }
}
