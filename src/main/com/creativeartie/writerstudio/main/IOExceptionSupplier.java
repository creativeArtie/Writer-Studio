package com.creativeartie.writerstudio.main;

import java.io.*;
import java.util.function.*;

public interface IOExceptionSupplier<T> extends Supplier<T> {
    @Override
    default T get() {
        try {
            return getThrows();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    T getThrows() throws IOException;

}