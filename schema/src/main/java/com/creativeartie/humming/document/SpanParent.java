package com.creativeartie.humming.document;

import java.util.*;

public interface SpanParent extends Span {
    List<SpanLeaf> getLeafs();

    List<Integer> findChild(Span span);

    List<StyleClass> getInheritedStyles();

    default void addChild(Span child) {
        useParent().add(child);
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

    boolean add(Span child);

    int size();
}
