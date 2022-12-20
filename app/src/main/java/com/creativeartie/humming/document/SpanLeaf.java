package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public class SpanLeaf implements Span {
    private final Document spanRoot;
    private final ImmutableList<StyleClasses> styleClasses;
    private final int styleLength;

    protected SpanLeaf(SpanBranch parent, int length, StyleClasses style) {
        spanRoot = parent.getRoot();
        ImmutableList.Builder<StyleClasses> builder = ImmutableList.builder();
        styleClasses = builder.addAll(parent.getInheritedStyles()).add(style).build();
        styleLength = length;
    }

    @Override
    public Document getRoot() {
        return spanRoot;
    }

    public List<StyleClasses> getClassStyles() {
        return styleClasses;
    }

    public int getLength() {
        return styleLength;
    }
}
