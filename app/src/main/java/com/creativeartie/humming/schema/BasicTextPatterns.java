package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum BasicTextPatterns implements PatternEnum {
    ID("[\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit} _\t]+"),
    LINK("[^\\|\\}\\n]+"), SPECIAL("[^\\}\\n]+"), TEXT("[^\\n]+"),
    HEADING("[^\\n\\#]+"), CITE("[\\p{IsAlphabetic}_]+");

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

        @Override
        public boolean runFind() {
            return true;
        }

    }

    private final String textPattern;
    private final String basePattern;
    private Pattern compiledPattern;

    private BasicTextPatterns(String pat) {
        textPattern = pat;
        // @formatter:off
        basePattern = "(" +
                BasicTextPart.ESCAPE.getRawPattern() + "|" +
                pat +
            ")+";
        // @formatter:on
    }

    public Matcher matcher(String text) {
        if (compiledPattern == null) {
            compiledPattern = Pattern.compile(
            // @formatter:off
                "(" +
                    BasicTextPart.ESCAPE.getNamedPattern() + "|" +
                    PatternEnum.namePattern(BasicTextPart.TEXT.name(), textPattern) +
                ")"
            // @formatter:on
            );
        }
        return compiledPattern.matcher(text);
    }

    @Override
    public String getRawPattern() {
        return basePattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }

    @Override
    public boolean runFind() {
        return true;
    }
}
