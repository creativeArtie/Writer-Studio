package com.creativeartie.humming.document;

import java.util.*;

public interface Span {
    boolean cleanUp();

    default <T> Optional<T> findParent(Class<T> clazz) {
        Optional<SpanParent> parent = getParent();

        while (parent.filter(span -> clazz.isInstance(span)).isEmpty()) {

            if (parent.isEmpty()) return Optional.empty();
            parent = parent.get().getParent();
        }
        return parent.map(span -> clazz.cast(span));
    }

    default int getEndIndex() {
        return getRoot().getCacheEnd(this);
    }

    int getLength();

    Optional<SpanParent> getParent();

    Manuscript getRoot();

    default int getStartIndex() {
        return getRoot().getCacheStart(this);
    }

    default SpanParent useParent() {
        return getParent().get();
    }

    default <T> T useParent(Class<T> clazz) {
        return getParent().filter(span -> clazz.isInstance(span)).map(span -> clazz.cast(span)).get();
    }
}
