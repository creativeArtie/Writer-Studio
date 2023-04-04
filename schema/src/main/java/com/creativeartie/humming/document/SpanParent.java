package com.creativeartie.humming.document;

import java.util.*;

/**
 * Span with children
 */
public interface SpanParent extends Span {
    /**
     * gets the {@link SpanLeaf}.
     *
     * @return list of span leaf.
     */
    List<SpanLeaf> getLeafs();

    /**
     * Find the position of this span.
     *
     * @param span
     *        the span to find
     *
     * @return the span
     */
    List<Integer> findChild(Span span);

    /**
     * Gets the list of inherited styles
     *
     * @return styles
     */
    List<CssStyle> getInheritedStyles();

    /**
     * Gets the size of the children.
     *
     * @return size
     */
    int size();
}
