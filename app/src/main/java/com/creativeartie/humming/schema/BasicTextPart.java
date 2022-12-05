package com.creativeartie.humming.schema;

enum BasicTextPart implements PatternEnum {
    ESCAPE("\\\\."), TEXT("");

    private final String pattern;

    BasicTextPart(String pat) {
        pattern = pat;
    }

    @Override
    public String getRawPattern() {
        return pattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }

}
