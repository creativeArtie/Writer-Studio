package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

public class LinePhrase extends Span implements SpanBranch {

    private enum Patterns {
        BOLD("\\*"), ITALICS("\\`"), UNDERLINE("\\_"), ESCAPE("\\\\.");

        private static String enderPattern = "";

        private final String rawPattern;
        private final String namedPattern;

        Patterns(String pattern) {
            rawPattern = pattern;
            namedPattern = namePattern(this);
        }

        private static String getEndPattern() {
            if (enderPattern == "") {
                for (Patterns pat : values()) {
                    enderPattern += pat.toString().substring(0, 2);
                }
            }
            return enderPattern;
        }

        private SpanStyles toTypeStyle() {
            return SpanStyles.valueOf(name());
        }

        @Override
        public String toString() {
            return rawPattern;
        }
    }

    public static enum TextEnders {
        TABLE("\\|"), REF("\\}"), HEADING("#"), LINKS(">"), NONE("");

        private final String partPattern;
        private String rawPattern;
        private String namedPattern;
        private Pattern textPattern;

        static String getPhraseName() {
            return "text";
        }

        String getPhrasePattern(boolean withName) {
            return withName ? namePattern(getPhraseName(), createRegex(false)) :
                createRegex(false);
        }

        TextEnders(String pattern) {
            partPattern = pattern;
            rawPattern = null;
            namedPattern = null;
        }

        String getPattern(boolean withName) {
            if (rawPattern == null) {
                rawPattern = "[^" + partPattern + Patterns.getEndPattern() +
                    ">\\{]+";
                namedPattern = namePattern(getPhraseName(), rawPattern);
            }
            return withName ? namedPattern : rawPattern;
        }

        @Override
        public String toString() {
            return rawPattern;
        }

        private String createRegex(boolean withName) {
            String builder = TodoPhrase.getPhrasePattern(withName) + "|" +
                IdRefPhrase.getPhrasePattern(withName);
            for (Patterns pat : Patterns.values()) {
                builder += "|" + (withName ? pat.namedPattern : pat.rawPattern);
            }
            builder += "|" + getPattern(withName);
            return builder;
        }

        public Pattern getTextPattern() {
            if (textPattern == null) {
                textPattern = Pattern.compile(createRegex(true));
            }
            return textPattern;
        }
    }

    public class TextSpan extends Span {

        private boolean[] textFormats;
        private String textString;

        private TextSpan(String text, DocBuilder doc, boolean... formats) {
            super(doc);
            textString = text;
            textFormats = Arrays.copyOf(formats, 3);
        }

        public boolean isBold() {
            return textFormats[Patterns.BOLD.ordinal()];
        }

        public boolean isItalics() {
            return textFormats[Patterns.ITALICS.ordinal()];
        }

        public boolean isUnderline() {
            return textFormats[Patterns.UNDERLINE.ordinal()];
        }

        public boolean[] getFormats() {
            return textFormats;
        }

        public String getText() {
            return textString;
        }

    }

    static String getPhraseName() {
        return "line";
    }

    private ArrayList<Span> childrenSpans;

    public LinePhrase(String text, DocBuilder docBuilder, TextEnders ender) {
        super(docBuilder);
        childrenSpans = new ArrayList<>();
        boolean formats[] = { false, false, false };
        TreeSet<SpanStyles> styles = new TreeSet<>();
        Matcher matched = ender.getTextPattern().matcher(text);
        while (matched.find()) {
            String value = null;
            for (Patterns pattern : Patterns.values()) {
                if (pattern.ordinal() >= Patterns.ESCAPE.ordinal()) break;
                if ((value = matched.group(pattern.name())) != null) {
                    formats[pattern.ordinal()] = !formats[pattern.ordinal()];

                    if (formats[pattern.ordinal()]) {
                        addStyle(matched, pattern, styles, SpanStyles.OPERATOR);
                        styles.add(pattern.toTypeStyle());

                    } else {
                        styles.remove(pattern.toTypeStyle());
                        addStyle(matched, pattern, styles, SpanStyles.OPERATOR);
                    }
                    break;
                }
            }

            if (value != null) continue;

            if ((value = matched.group(Patterns.ESCAPE.name())) != null) {
                childrenSpans.add(
                    new TextSpan(value.charAt(1) + "", docBuilder, formats)
                );
                addStyle(matched, Patterns.ESCAPE, styles, SpanStyles.ESCAPE);
                continue;
            }

            if ((value = matched.group(TodoPhrase.getPhraseName())) != null) {
                childrenSpans.add(new TodoPhrase(value, docBuilder));
                // matched.find();
                continue;
            }
            value = matched.group(TextEnders.getPhraseName());
            childrenSpans.add(new TextSpan(value, docBuilder, formats));
            addStyle(matched, styles, SpanStyles.TEXT);
        }
    }

    public ArrayList<Span> getChildren() {
        return childrenSpans;
    }

    @Override
    public Iterator<Span> iterator() {
        return childrenSpans.iterator();
    }

    @Override
    public int size() {
        return childrenSpans.size();
    }
}
