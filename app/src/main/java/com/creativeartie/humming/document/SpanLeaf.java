package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public final class SpanLeaf implements Span {
    private final Document spanRoot;
    private final SpanBranch parentSpan;
    private final StyleClasses styleClass;
    private final int styleLength;
    private final int spanId;
    private static int countId = 0;

    protected SpanLeaf(SpanBranch parent, int length) {
        this(parent, length, StyleClasses.OPERATOR);
    }

    protected SpanLeaf(SpanBranch parent, int length, StyleClasses style) {
        spanRoot = parent.getRoot();
        parentSpan = parent;
        styleLength = length;
        styleClass = style;
        spanId = countId++;
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

    @Override
    public Optional<SpanBranch> getParent() {
        return Optional.of(parentSpan);
    }

    @Override
    public String getId() {
        return "Leaf" + Integer.toString(spanId);
    }
}
