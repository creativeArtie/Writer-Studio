package com.creativeartie.humming.document;

import java.util.*;
import java.util.concurrent.*;

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
    // List of ids
    private TreeMap<String, Integer> idList;

    // Methods add more styles for ids
    private final ArrayList<SpanBranch> docChildren;

    public Document() {
        idList = new TreeMap<>();
        docChildren = new ArrayList<>();
    }

    public List<Integer> findChild(Span span) {
        List<Span> spans = ImmutableList.copyOf(docChildren);
        return findChild(span, spans);
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

    /**
     * Adds a id
     *
     * @param group
     *        the type of id
     * @param name
     *        the name of id
     *
     * @return {@code true} if the id doesn't exist previously
     */
    void putId(Identity id) {
        String name = id.getInternalId();
        if (idList.containsKey(name)) idList.put(name, idList.get(name) + 1);
        else idList.put(name, 1);
    }

    /**
     * Check if the id is found
     *
     * @param group
     *        the type of id
     * @param name
     *        the name of id
     *
     * @return {@code true} if the id exist
     */
    boolean isCorrect(Identity id) {
        String name = id.getInternalId();
        return idList.containsKey(name) && idList.get(name) == 1;
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
    public int getLength() throws ExecutionException {
        if (isEmpty()) return 0;
        return get(size() - 1).getEndIndex();
    }

    @Override
    public int getStartIndex() throws ExecutionException {
        return 0;
    }

    @Override
    public int getEndIndex() throws ExecutionException {
        return getLength();
    }

    @SuppressWarnings("unchecked")
    protected static int getCacheIndex(Span span, boolean isStart) throws ExecutionException {
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
}
