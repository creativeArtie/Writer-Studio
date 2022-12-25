package com.creativeartie.humming.document;

import java.util.*;

/**
 * Stores information about the writing text files. This includes
 * <ul>
 * <li>A list of id and their references
 * <li>list of styles and the ability to rebuild them
 * </ul>
 *
 * @author wai
 */
public class Document {
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

    public void addChild(SpanBranch child) {
        docChildren.add(child);
    }

    int getStartIndex(Span span) {
        return getIndex(span, true);
    }

    int getEndIndex(Span span) {
        return getIndex(span, false);
    }

    private int getIndex(Span untilSpan, boolean isStart) {
        int index = 0;
        for (SpanBranch child : docChildren) {
            int found = child.getLength(isStart, untilSpan);
            index += Math.abs(found);
            if (found <= 0) { // negative = cut short, 0 = cut before even begin
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
}
