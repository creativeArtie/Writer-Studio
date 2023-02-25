package com.creativeartie.humming.document;

import java.util.*;

public interface Span {
    Document getRoot();

    Optional<SpanParent> getParent();

    public boolean cleanUp();

    public default int getStartIndex() {
        return getRoot().getCacheStart(this);
    }

    public int getLength();

    default int getEndIndex() {
        return getRoot().getCacheEnd(this);
    }

    default SpanParent useParent() {
        return getParent().get();
    }

    default <T> T useParent(Class<T> clazz) {
        return getParent().filter((span) -> clazz.isInstance(span)).map((span) -> clazz.cast(span)).get();
    }

    default <T> Optional<T> findParent(Class<T> clazz) {
        Optional<SpanParent> parent = getParent();

        while (parent.filter((span) -> clazz.isInstance(span)).isEmpty()) {

            if (parent.isEmpty()) {
                return Optional.empty();
            }
            parent = parent.get().getParent();
        }
        return parent.map((span) -> clazz.cast(span));
    }
}
