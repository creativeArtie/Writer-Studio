package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.api.extension.*;
import static com.creativeartie.writerstudio.lang.CatalogueStatus;

public abstract class IDParameterMethodSource {

    public static Stream<Arguments> provideText(){
        return Stream.of(
            Arguments.of(UNUSED,    getIdText()),
            Arguments.of(MULTIPLE,  getIdText() +  getIdText()),
            Arguments.of(MULTIPLE,  getIdText() +  getIdText() +  getIdText()),
            Arguments.of(MULTIPLE,  getIdText() +  getIdText() +  getIdText() + getIdText()),
            Arguments.of(MULTIPLE,  getIdText() +  getIdText() +  getIdText() + getRefText()),
            Arguments.of(MULTIPLE,  getIdText() +  getIdText() +  getRefText()),
            Arguments.of(MULTIPLE,  getIdText() +  getIdText() +  getRefText() + getIdText()),
            Arguments.of(MULTIPLE,  getIdText() +  getIdText() +  getRefText() + getRefText()),
            Arguments.of(READY,     getIdText() +  getRefText()),
            Arguments.of(MULTIPLE,  getIdText() +  getRefText() + getIdText()),
            Arguments.of(MULTIPLE,  getIdText() +  getRefText() + getIdText() + getIdText()),
            Arguments.of(MULTIPLE,  getIdText() +  getRefText() + getIdText() + getRefText()),
            Arguments.of(READY,     getIdText() +  getRefText() + getRefText()),
            Arguments.of(MULTIPLE,  getIdText() +  getRefText() + getRefText() + getIdText()),
            Arguments.of(READY,     getIdText() +  getRefText() + getRefText() + getRefText()),
            Arguments.of(NOT_FOUND, getRefText()),
            Arguments.of(READY,     getRefText() + getIdText()),
            Arguments.of(MULTIPLE,  getRefText() + getIdText() +  getIdText()),
            Arguments.of(MULTIPLE,  getRefText() + getIdText() +  getIdText() + getIdText()),
            Arguments.of(MULTIPLE,  getRefText() + getIdText() +  getIdText() + getRefText()),
            Arguments.of(READY,     getRefText() + getIdText() +  getRefText()),
            Arguments.of(MULTIPLE,  getRefText() + getIdText() +  getRefText() + getIdText()),
            Arguments.of(READY,     getRefText() + getIdText() +  getRefText() + getRefText()),
            Arguments.of(NOT_FOUND, getRefText() + getRefText()),
            Arguments.of(READY,     getRefText() + getRefText() + getIdText()),
            Arguments.of(MULTIPLE,  getRefText() + getRefText() + getIdText() + getIdText()),
            Arguments.of(READY,     getRefText() + getRefText() + getIdText() + getRefText()),
            Arguments.of(NOT_FOUND, getRefText() + getRefText() + getRefText()),
            Arguments.of(READY,     getRefText() + getRefText() + getRefText() + getIdText()),
            Arguments.of(NOT_FOUND, getRefText() + getRefText() + getRefText() + getRefText())
        ):
    }

    protected IDParameterMethodSource(){}

    protected abstract String getIdText();
    protected abstract String getRefText();
}
