package com.creativeartie.humming.document;

import java.util.*;
import java.util.concurrent.*;

import org.fxmisc.richtext.model.*;

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
public class Document extends ForwardingList<SpanBranch> implements Span {
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
    private final ArrayList<SpanBranch> docChildren;
    private LoadingCache<Span, List<Integer>> findChildCache;
    private LoadingCache<SpanBranch, Integer> lengthsCache;
    private LoadingCache<Span, Integer> startIdxCache, endIdxCache;

    public Document() {
        idStorage = new IdentityStorage();
        docChildren = new ArrayList<>();
        findChildCache = CacheBuilder.newBuilder().recordStats().build(new CacheLoader<Span, List<Integer>>() {
            @Override
            public List<Integer> load(Span key) {
                return findChild(key, ImmutableList.copyOf(docChildren));
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
    }

    public List<Integer> findChild(Span span) {
        try {
            return findChildCache.get(span);
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.exit(-1);
            return new ArrayList<>();
        }
    }

    protected List<Integer> findChild(Span span, List<Span> children) {
        ArrayList<Integer> answer = new ArrayList<>();
        int i = 0;
        for (Span child : children) {
            if (child == span) {
                answer = new ArrayList<>();
                answer.add(i);
                return answer;
            }
            if (child instanceof SpanBranch) {
                List<Integer> addList = findChild(span, ((SpanBranch) child));
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
    public boolean add(SpanBranch child) {
        return docChildren.add(child);
    }

    @Override
    public List<SpanBranch> delegate() {
        return docChildren;
    }

    @Override
    public boolean addAll(Collection<? extends SpanBranch> c) {
        return docChildren.addAll(c);
    }

    @Override
    public Document getRoot() {
        return this;
    }

    @Override
    public Optional<SpanBranch> getParent() {
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

    @SuppressWarnings("unchecked")
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
            parent = (ForwardingList<? extends Span>) parent.get(targetIndex);
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
    }

    public void updateText(String text) {
        clear();
        SpanBranch parent = new SpanBranch(this);
        add(LineSpan.newLine(parent, text));
    }

    public StyleSpans<Collection<String>> getStyles() {
        final StyleSpansBuilder<Collection<String>> styleSpans = new StyleSpansBuilder<>();
        for (SpanBranch branch : docChildren) {
            for (SpanLeaf leaf : branch.getLeafs()) {
                styleSpans.add(leaf.getCssStyles(), leaf.getLength());
            }
        }
        return styleSpans.create();
    }
}
