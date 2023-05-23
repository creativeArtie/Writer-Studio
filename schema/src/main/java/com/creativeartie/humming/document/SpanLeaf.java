package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

/**
 * The basic span
 */
public final class SpanLeaf implements Span {
    private final Manuscript spanRoot;
    private final SpanParent parentSpan;
    private final CssSpanStyles styleClass;
    private final int styleLength;
    private final String referText;

    static Optional<String> addLeaf(SpanBranch parent, String text) {
        return addLeaf(parent, text, CssSpanStyles.OPERATOR);
    }

    static Optional<String> addLeaf(SpanBranch parent, String text, CssSpanStyles style) {
        if (text == null || text.isEmpty()) {
            return Optional.empty();
        }
        parent.add(new SpanLeaf(parent, text, style));
        return Optional.of(text);
    }

    private SpanLeaf(SpanParent parent, String text, CssSpanStyles style) {
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

    /**
     * Get the style as {@link CssStyle}
     *
     * @return the list of styles
     *
     * @see #getCssStyles()
     */
    public List<CssStyle> getClassStyles() {
        return ImmutableList.<CssStyle>builder().addAll(parentSpan.getInheritedStyles()).add(styleClass).build();
    }

    /**
     * Get the style as {@linkplain String}
     *
     * @return the list of styles
     *
     * @see #getClassStyles()
     */
    public List<String> getCssStyles() {
        ImmutableList.Builder<String> styles = ImmutableList.builder();
        for (CssStyle style : getClassStyles()) {
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

    /**
     * Gets the text it represent
     *
     * @return the text
     */
    public String getReferText() {
        return referText;
    }

    /**
     * Get the span style
     *
     * @return the span style
     */
    public CssSpanStyles getStyle() {
        return styleClass;
    }
}
