package com.creativeartie.humming.document;

import java.util.*;

/**
 * An parent that is associated with an identity.
 */
public interface IdentityParent {
    /**
     * gets the id position.
     *
     * @return the position
     */
    public int getIdPosition();

    /**
     * The identity span.
     *
     * @return the span
     */
    Optional<IdentitySpan> getPointer();
}