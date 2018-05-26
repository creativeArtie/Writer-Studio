package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.params.provider.*;
import static com.creativeartie.writerstudio.lang.CatalogueStatus.*;

import java.util.stream.*;

public abstract class IDParameterMethodSource {

    public static Stream<Arguments> provideText(IDParameterMethodSource source){
        String id = source.getIdText();
        String ref = source.getRefText();
        return Stream.of(
            Arguments.of(UNUSED,    id),
            Arguments.of(MULTIPLE,  id  + id),
            Arguments.of(MULTIPLE,  id  + id  + id),
            Arguments.of(MULTIPLE,  id  + id  + id + id),
            Arguments.of(MULTIPLE,  id  + id  + id + ref),
            Arguments.of(MULTIPLE,  id  + id  + ref),
            Arguments.of(MULTIPLE,  id  + id  + ref + id),
            Arguments.of(MULTIPLE,  id  + id  + ref + ref),
            Arguments.of(READY,     id  + ref),
            Arguments.of(MULTIPLE,  id  + ref + id),
            Arguments.of(MULTIPLE,  id  + ref + id + id),
            Arguments.of(MULTIPLE,  id  + ref + id + ref),
            Arguments.of(READY,     id  + ref + ref),
            Arguments.of(MULTIPLE,  id  + ref + ref + id),
            Arguments.of(READY,     id  + ref + ref + ref),
            Arguments.of(NOT_FOUND, ref),
            Arguments.of(READY,     ref + id),
            Arguments.of(MULTIPLE,  ref + id  + id),
            Arguments.of(MULTIPLE,  ref + id  + id + id),
            Arguments.of(MULTIPLE,  ref + id  + id + ref),
            Arguments.of(READY,     ref + id  + ref),
            Arguments.of(MULTIPLE,  ref + id  + ref + id),
            Arguments.of(READY,     ref + id  + ref + ref),
            Arguments.of(NOT_FOUND, ref + ref),
            Arguments.of(READY,     ref + ref + id),
            Arguments.of(MULTIPLE,  ref + ref + id + id),
            Arguments.of(READY,     ref + ref + id + ref),
            Arguments.of(NOT_FOUND, ref + ref + ref),
            Arguments.of(READY,     ref + ref + ref + id),
            Arguments.of(NOT_FOUND, ref + ref + ref + ref)
        );
    }

    protected IDParameterMethodSource(){}

    protected abstract String getIdText();
    protected abstract String getRefText();
}
