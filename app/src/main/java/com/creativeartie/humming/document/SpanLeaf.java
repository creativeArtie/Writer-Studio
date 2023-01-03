package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public final class SpanLeaf implements Span {
    private final Document spanRoot;
    private final SpanBranch parentSpan;
    private final List<StyleClasses> styleClass;
    private final int styleLength;

    protected SpanLeaf(SpanBranch parent, int length) {
        this(parent, length, StyleClasses.OPERATOR);
    }

    protected SpanLeaf(SpanBranch parent, int length, StyleClasses... styles) {
        spanRoot = parent.getRoot();
        parentSpan = parent;
        styleLength = length;
        styleClass = Arrays.asList(styles);
    }

    @Override
    public Document getRoot() {
        return spanRoot;
    }

    public List<StyleClasses> getClassStyles() {
        return ImmutableList.<StyleClasses>builder().addAll(parentSpan.getInheritedStyles()).addAll(styleClass).build();
    }

    @Override
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
}
