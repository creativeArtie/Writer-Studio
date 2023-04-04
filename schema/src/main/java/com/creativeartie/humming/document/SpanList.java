package com.creativeartie.humming.document;

/**
 * Span for lists
 */
public interface SpanList {
    /**
     * Gets the span level
     *
     * @return the level
     */
    int getLevel();

    /**
     * Gets the span position in the list
     *
     * @return list position
     */
    int getPosition();

    /**
     * Check if this is a bullet list
     *
     * @return is bullet
     */
    boolean isBullet();
}
