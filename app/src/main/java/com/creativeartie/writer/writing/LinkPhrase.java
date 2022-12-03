package com.creativeartie.writer.writing;

public final class LinkPhrase extends Span {

    private enum Patterns {
        START("{@"), LINK("[^\\|\\}]+"), SPLIT("\\|"), TEXT("[^\\}]+"),
        ENDING("}");

        private final String rawPattern;

        private Patterns(String pattern) {
            rawPattern = pattern;
        }

        @Override
        public String toString() {
            return rawPattern;
        }
    }

    private String pattern = Patterns.START.rawPattern +
        Patterns.LINK.rawPattern + "(" + Patterns.SPLIT.rawPattern +
        Patterns.TEXT.rawPattern + ")?" + Patterns.ENDING.rawPattern + "?";

    public LinkPhrase(String text, DocBuilder docBuilder) {
        super(docBuilder);
        // TODO Auto-generated constructor stub
    }

}
