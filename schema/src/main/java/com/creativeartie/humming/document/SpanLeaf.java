package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public final class SpanLeaf implements Span {
    private final Manuscript spanRoot;
    private final SpanParent parentSpan;
    private final StylesSpans styleClass;
    private final int styleLength;
    private final String referText;

    protected SpanLeaf(SpanParent parent, String text) {
        this(parent, text, StylesSpans.OPERATOR);
    }

    protected SpanLeaf(SpanParent parent, String text, StylesSpans style) {
        spanRoot = parent.getRoot();
        parentSpan = parent;
        referText = text;
        styleLength = text.length();
        styleClass = style;
    }

    @Override
    public Manuscript getRoot() {
        return spanRoot;
    }

    public List<SpanStyle> getClassStyles() {
        return ImmutableList.<SpanStyle>builder().addAll(parentSpan.getInheritedStyles()).add(styleClass).build();
    }

    public List<String> getCssStyles() {
        ImmutableList.Builder<String> styles = ImmutableList.builder();
        for (SpanStyle style : getClassStyles()) {
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
    public Optional<SpanParent> getParent() {
        return Optional.of(parentSpan);
    }

    @Override
    public String toString() {
        return referText;
    }

    public String getRefText() {
        return referText;
    }

    public StylesSpans getStyle() {
        return styleClass;
    }
}
