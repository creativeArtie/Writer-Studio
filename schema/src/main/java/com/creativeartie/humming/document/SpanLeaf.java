package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public final class SpanLeaf implements Span {
    private final Document spanRoot;
    private final SpanBranch parentSpan;
    private final List<StyleClass> styleClass;
    private final int styleLength;

    protected SpanLeaf(SpanBranch parent, String text) {
        this(parent, text.length(), SpanStyles.OPERATOR);
    }

    protected SpanLeaf(SpanBranch parent, String text, StyleClass... styles) {
        this(parent, text.length(), styles);
    }

    protected SpanLeaf(SpanBranch parent, int length) {
        this(parent, length, SpanStyles.OPERATOR);
    }

    protected SpanLeaf(SpanBranch parent, int length, StyleClass... styles) {
        spanRoot = parent.getRoot();
        parentSpan = parent;
        styleLength = length;
        styleClass = Arrays.asList(styles);
    }

    @Override
    public Document getRoot() {
        return spanRoot;
    }

    public List<StyleClass> getClassStyles() {
        return ImmutableList.<StyleClass>builder().addAll(parentSpan.getInheritedStyles()).addAll(styleClass).build();
    }

    public List<String> getCssStyles() {
        ImmutableList.Builder<String> styles = ImmutableList.builder();
        for (StyleClass style : getClassStyles()) {
            styles.add(style.getCssName());
        }
        return styles.build();
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
