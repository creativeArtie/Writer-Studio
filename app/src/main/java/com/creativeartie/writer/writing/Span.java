package com.creativeartie.writer.writing;

import java.util.*;

import com.google.common.collect.*;

/**
 * A span of text TODO stub class, not sure if this is needed
 * 
 * @author wai
 */
public abstract class Span {
    private class Node {
        ImmutableList<String> styleClasses;

        final int nodeLength;

        Node(List<String> styles, int length) {
            styleClasses = ImmutableList.copyOf(styles);
            nodeLength = length;
        }
    }

    private ArrayList<Node> styleClasses;

    Span() {
        styleClasses = new ArrayList<>();
    }

    protected void addStyle(List<String> styles, int size) {
        styleClasses.add(new Node(styles, size));
    }
}
