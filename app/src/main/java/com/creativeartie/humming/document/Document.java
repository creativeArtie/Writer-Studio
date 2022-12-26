package com.creativeartie.humming.document;

import java.util.*;
import java.util.concurrent.*;

import com.google.common.collect.*;

/**
 * Stores information about the writing text files. This includes
 * <ul>
 * <li>A list of id and their references
 * <li>list of styles and the ability to rebuild them
 * </ul>
 *
 * @author wai
 */
public class Document extends ForwardingList<SpanBranch> {
    // List of ids
    private TreeMap<String, Integer> idList;

    // Methods add more styles for ids
    private final ArrayList<SpanBranch> docChildren;

    public Document() {
        idList = new TreeMap<>();
        docChildren = new ArrayList<>();
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

    int getStartIndex(Span span) throws ExecutionException {
        return getIndex(span, true);
    }

    int getEndIndex(Span span) throws ExecutionException {
        return getIndex(span, false);
    }

    private int getIndex(Span untilSpan, boolean isStart) throws ExecutionException {
        int index = 0;
        for (SpanBranch child : docChildren) {
            int found = child.getLength(isStart, untilSpan);
            if (isStart && untilSpan == child) {
                return index;
            }
            index += Math.abs(found);
            if (found <= 0) { // negative = cut short, 0 = cut before even begin
                return index;
            }
            if (!isStart && untilSpan == child) {
                return index;
            }
        }
        return index;
    }

    /**
     * Style clean up
     */
    public void runCleanup() {
        for (SpanBranch child : docChildren) {
            child.cleanUp();
        }
    }

    @Override
    public List<SpanBranch> delegate() {
        return docChildren;
    }
}
