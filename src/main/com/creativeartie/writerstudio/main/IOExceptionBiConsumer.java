package com.creativeartie.writerstudio.main;

import java.io.*;
import java.util.function.*;

public interface IOExceptionBiConsumer<T, U> extends BiConsumer<T, U> {
    @Override
    default void accept(final T first, final U second) {
        try {
            acceptThrows(first, second);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    void acceptThrows(T first, U second) throws IOException;

}
