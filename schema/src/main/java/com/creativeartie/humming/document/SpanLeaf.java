package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public final class SpanLeaf implements Span {
    private final Document spanRoot;
    private final SpanBranch parentSpan;
    private final SpanStyles styleClass;
    private final int styleLength;
    private final String referText;

    protected SpanLeaf(SpanBranch parent, String text) {
        this(parent, text, SpanStyles.OPERATOR);
    }

    protected SpanLeaf(SpanBranch parent, String text, SpanStyles style) {
        spanRoot = parent.getRoot();
        parentSpan = parent;
        referText = text;
        styleLength = text.length();
        styleClass = style;
    }

    @Override
    public Document getRoot() {
        return spanRoot;
    }

    public List<StyleClass> getClassStyles() {
        return ImmutableList.<StyleClass>builder().addAll(parentSpan.getInheritedStyles()).add(styleClass).build();
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

    @Override
    public String toString() {
        return referText;
    }

    public SpanStyles getStyle() {
        return styleClass;
    }
}
