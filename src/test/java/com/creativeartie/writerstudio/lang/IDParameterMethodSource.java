package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.params.provider.*;

import java.util.stream.*;

import static com.creativeartie.writerstudio.lang.CatalogueStatus.*;

/// Create document for all different type of {@link CataloguStatus} formation.
public abstract class IDParameterMethodSource {

    public static Document newDocument(String text, SetupParser ... parsers){
        return new Document(text, parsers){};
    }

    public static Stream<Arguments> provideText(IDParameterMethodSource source){
        String id = source.getIdText();
        String ref = source.getRefText();
        return Stream.of(
            source.buildArgument(UNUSED,    true),
            source.buildArgument(MULTIPLE,  true,  true),
            source.buildArgument(MULTIPLE,  true,  true,  true),
            source.buildArgument(MULTIPLE,  true,  true,  true,  true),
            source.buildArgument(MULTIPLE,  true,  true,  true,  false),
            source.buildArgument(MULTIPLE,  true,  true,  false),
            source.buildArgument(MULTIPLE,  true,  true,  false, true),
            source.buildArgument(MULTIPLE,  true,  true,  false, false),
            source.buildArgument(READY,     true,  false),
            source.buildArgument(MULTIPLE,  true,  false, true),
            source.buildArgument(MULTIPLE,  true,  false, true,  true),
            source.buildArgument(MULTIPLE,  true,  false, true,  false),
            source.buildArgument(READY,     true,  false, false),
            source.buildArgument(MULTIPLE,  true,  false, false, true),
            source.buildArgument(READY,     true,  false, false, false),
            source.buildArgument(NOT_FOUND, false),
            source.buildArgument(READY,     false, true),
            source.buildArgument(MULTIPLE,  false, true,  true),
            source.buildArgument(MULTIPLE,  false, true,  true,  true),
            source.buildArgument(MULTIPLE,  false, true,  true,  false),
            source.buildArgument(READY,     false, true,  false),
            source.buildArgument(MULTIPLE,  false, true,  false, true),
            source.buildArgument(READY,     false, true,  false, false),
            source.buildArgument(NOT_FOUND, false, false),
            source.buildArgument(READY,     false, false, true),
            source.buildArgument(MULTIPLE,  false, false, true,  true),
            source.buildArgument(READY,     false, false, true,  false),
            source.buildArgument(NOT_FOUND, false, false, false),
            source.buildArgument(READY,     false, false, false, true),
            source.buildArgument(NOT_FOUND, false, false, false, false)
        );
    }

    private Arguments buildArgument(CatalogueStatus status,
            boolean ... ids){
        StringBuilder builder = new StringBuilder();
        for (boolean id: ids){
            builder.append(id? getIdText(): getRefText());
        }
        return Arguments.of(status, builder.toString(), ids);
    }

    protected IDParameterMethodSource(){}

    protected abstract String getIdText();
    protected abstract String getRefText();
}
