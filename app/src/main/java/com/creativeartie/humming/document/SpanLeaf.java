package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public final class SpanLeaf implements Span {
    private final Document spanRoot;
    private final SpanBranch parentSpan;
    private final StyleClasses styleClass;
    private final int styleLength;

    protected SpanLeaf(SpanBranch parent, int length, StyleClasses style) {
        spanRoot = parent.getRoot();
        parentSpan = parent;
        styleLength = length;
        styleClass = style;
    }

    @Override
    public Document getRoot() {
        return spanRoot;
    }

    public List<StyleClasses> getClassStyles() {
        ImmutableList.Builder<StyleClasses> builder = ImmutableList.builder();
        return builder.addAll(parentSpan.getInheritedStyles()).add(styleClass).build();
    }

    public int getLength() {
        return styleLength;
    }

    @Override
    public boolean cleanUp() {
        return false;
    }
}
