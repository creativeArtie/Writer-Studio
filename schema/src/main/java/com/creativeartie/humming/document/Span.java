package com.creativeartie.humming.document;

import java.util.*;

/**
 * A span for text, paragraph or division.
 */
public interface Span {
    /**
     * Clean up the text
     *
     * @return {@code true} if something changed
     */
    boolean cleanUp();

    /**
     * Find the parent with this class
     *
     * @param <T>
     *        The class to find
     * @param clazz
     *        the class
     *
     * @return parent or a empty optional.
     */
    default <T> Optional<T> findParent(Class<T> clazz) {
        Optional<SpanParent> parent = getParent();

        while (parent.filter(span -> clazz.isInstance(span)).isEmpty()) {

            if (parent.isEmpty()) return Optional.empty();
            parent = parent.get().getParent();
        }
        return parent.map(span -> clazz.cast(span));
    }

    /**
     * Gets the end index.
     *
     * @return end index
     */
    default int getEndIndex() {
        return getRoot().getCacheEnd(this);
    }

    /**
     * Gets the length.
     *
     * @return the length
     */
    int getLength();

    /**
     * Gets its direct parent span
     *
     * @return the parent
     */
    Optional<SpanParent> getParent();

    /**
     * Gets the root span
     *
     * @return the root
     */
    Manuscript getRoot();

    /**
     * Gets the start index.
     *
     * @return end index
     */
    default int getStartIndex() {
        return getRoot().getCacheStart(this);
    }
}
