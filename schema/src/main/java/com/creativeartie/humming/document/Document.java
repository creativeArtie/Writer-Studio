package com.creativeartie.humming.document;

import java.util.*;
import java.util.Optional;
import java.util.concurrent.*;

import com.google.common.base.*;
import com.google.common.cache.*;
import com.google.common.collect.*;

/**
 * Stores information about the writing text files. This includes
 * <ul>
 * <li>list of id and their references
 * <li>list of styles and the ability to rebuild them
 * <li>list of children span and how to find them
 * </ul>
 *
 * @author wai
 */
public class Document extends ForwardingList<DivisionSection> implements SpanParent {
    private IdentityStorage idStorage;

    public boolean isIdUnique(IdentitySpan span) {
        return idStorage.isIdUnique(span);
    }

    public int getPointerCount(IdentitySpan span) {
        return idStorage.getPointerCount(span);
    }

    public void addId(IdentitySpan id) {
        idStorage.addId(id);
    }

    public void removeId(IdentitySpan id) {
        idStorage.removeId(id);
    }

    // Methods add more styles for ids
    private final ArrayList<DivisionSection> docChildren;
    private LoadingCache<Span, List<Integer>> findChildCache;
    private LoadingCache<SpanBranch, Integer> lengthsCache;
    private LoadingCache<Span, Integer> startIdxCache, endIdxCache;
    private LoadingCache<Integer, List<Span>> locateChildrenCache;

    public Document() {
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
                return getLocateChildrenCache(key, Document.this);
            }
        });
    }

    public List<Span> locateChildren(int location) {

        try {
            return locateChildrenCache.get(location);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return new ArrayList<>();
        }
    }

    private List<Span> getLocateChildrenCache(int location, List<? extends Span> children) {
        ImmutableList.Builder<Span> answer = ImmutableList.builder();

        for (Span child : children) {

            if (child.getEndIndex() > location) {
                answer.add(child);

                if (child instanceof SpanParent) {
                    answer.addAll(getLocateChildrenCache(location, (SpanBranch) child));
                    return answer.build();
                } else {
                    return answer.build();
                }
            }
        }
        return answer.build();
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

    protected List<Integer> getFindChildCache(Span span, List<Span> children) {
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

    @Override
    public boolean add(DivisionSection child) {
        child.setParent(this);
        return docChildren.add(child);
    }

    @Override
    public List<DivisionSection> delegate() {
        return docChildren;
    }

    @Override
    public boolean addAll(Collection<? extends DivisionSection> c) {
        c.forEach((child) -> child.setParent(this));
        return docChildren.addAll(c);
    }

    @Override
    public Document getRoot() {
        return this;
    }

    @Override
    public Optional<SpanParent> getParent() {
        return Optional.empty();
    }

    @Override
    public boolean cleanUp() {
        boolean changed = false;

        for (SpanBranch child : docChildren) {
            changed = changed || child.cleanUp();
        }
        return changed;
    }

    @Override
    public int getLength() {
        if (isEmpty()) return 0;
        return get(size() - 1).getEndIndex();
    }

    @Override
    public int getStartIndex() {
        return 0;
    }

    @Override
    public int getEndIndex() {
        return getLength();
    }

    private static int getCacheIndex(Span span, boolean isStart) {
        List<Integer> targetIndexes = span.getRoot().findChild(span);
        ForwardingList<? extends Span> parent = span.getRoot();
        int length = 0;

        for (int targetIndex : targetIndexes) {

            for (int childIndex = 0; childIndex < targetIndex; childIndex++) {
                length += parent.get(childIndex).getLength();
            }

            if (parent.get(targetIndex) instanceof SpanLeaf) {
                return length + (isStart ? 0 : parent.get(targetIndex).getLength());
            }
            parent = (SpanBranch) parent.get(targetIndex);
        }
        return length + (isStart ? 0 : ((SpanBranch) parent).getLength());
    }

    protected int getCacheLength(SpanBranch span) {

        try {
            return lengthsCache.get(span);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return 0;
        }
    }

    protected int getCacheStart(Span span) {

        try {
            return startIdxCache.get(span);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return 0;
        }
    }

    protected int getCacheEnd(Span span) {

        try {
            return endIdxCache.get(span);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return 0;
        }
    }

    public void printCacheStats() {
        System.out.printf("findChildCache %s\n", findChildCache.stats());
        System.out.printf("  lengthsCache %s\n", lengthsCache.stats());
        System.out.printf(" startIdxCache %s\n", startIdxCache.stats());
        System.out.printf("   endIdxCache %s\n", endIdxCache.stats());
        System.out.printf("   locateCache %s\n", locateChildrenCache.stats());
    }

    public void updateText(String text) {

        try {
            clear();

            findChildCache.invalidateAll();
            lengthsCache.invalidateAll();
            startIdxCache.invalidateAll();
            endIdxCache.invalidateAll();
            locateChildrenCache.invalidateAll();
            idStorage.clear();

            Division parent = new DivisionSection(this);
            add((DivisionSection) parent);
            List<String> texts = Splitter.on('\n').splitToList(text);
            int line = 1;

            for (String raw : texts) {
                raw += (line == texts.size() ? "" : "\n");
                Para span = Para.newLine(parent, raw);
                Optional<Division> next = parent.addLine(span, span.getLineStyle());

                if (next.isPresent()) {
                    parent = next.get();
                }
                line++;
            }
            cleanUp();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public <T> List<T> convertLeaves(Function<SpanLeaf, T> convert) {
        ImmutableList.Builder<T> leaves = ImmutableList.builder();

        for (SpanBranch child : docChildren) {

            for (SpanLeaf leaf : child.getLeafs()) {
                leaves.add(convert.apply(leaf));
            }
        }
        return leaves.build();
    }

    @Override
    public List<SpanLeaf> getLeafs() {
        return convertLeaves((leaf) -> leaf);
    }

    @Override
    public List<SpanStyle> getInheritedStyles() {
        return ImmutableList.of();
    }

    @Override
    public <T> Optional<T> findParent(Class<T> clazz) {
        return Optional.empty();
    }
}
