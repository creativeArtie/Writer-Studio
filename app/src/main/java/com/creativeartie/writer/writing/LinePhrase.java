package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

import com.google.common.collect.*;

public class LinePhrase extends Span {

    private enum FormatPatterns {
        // @orderFor LinePhrase.TextPhrase and SpanStyles
        BOLD("\\*"), ITALICS("`"), UNDERLINE("_"),
        // @endOrder

        REF_START("\\{"), ESCAPE("\\\\");

        @Override
        public String toString() {
            return rawPat;
        }

        private final String rawPat;

        FormatPatterns(String pat) {
            rawPat = pat;
        }
    }

    public class TextPhrase extends Span {
        static {
            String keys = "";
            for (FormatPatterns pattern : FormatPatterns.values()) {
                keys += pattern.rawPat;
            }
            commonKeys = keys;
        }
        private final String storedText;
        private final boolean hasFormat[];

        private TextPhrase(
            String text, DocBuilder docBuilder, boolean... formats
        ) {
            super(docBuilder);
            StringBuilder string = new StringBuilder();
            ImmutableList.Builder<SpanStyles> baseStyles = ImmutableList
                .builder();
            baseStyles.addAll(otherStyles);
            hasFormat = formats;
            int i = 0;
            for (boolean format : formats) {
                if (format) {
                    baseStyles.add(
                        SpanStyles.values()[SpanStyles.BOLD.ordinal() + i]
                    );
                }
                i++;
            }

            SpanStyles textStyle[] = ImmutableList.builder().addAll(
                baseStyles.build()
            ).add(SpanStyles.TEXT).build().toArray(new SpanStyles[0]);
            SpanStyles escOptStyle[] = ImmutableList.builder().addAll(
                baseStyles.build()
            ).add(SpanStyles.ESCAPE, SpanStyles.OPERATOR).build().toArray(
                new SpanStyles[0]
            );
            SpanStyles escTxtStyle[] = ImmutableList.builder().addAll(
                baseStyles.build()
            ).add(SpanStyles.ESCAPE, SpanStyles.TEXT).build().toArray(
                new SpanStyles[0]
            );

            Matcher match = lineType.getEnder().wordPattern.matcher(text);
            while (match.find()) {
                String find = match.group(txtName);
                System.out.println(find);
                if (find != null) {
                    string.append(find);
                    addStyle(match, txtName, textStyle);
                    continue;
                }
                find = match.group(escape);
                if (find != null) {
                    string.append(find.charAt(1));
                    addTextStyle("\\", escOptStyle);
                    addTextStyle(find.substring(1), escTxtStyle);
                }
            }

            storedText = string.toString();
        }

        public String getText() {
            return storedText;
        }

        public boolean isBold() {
            return hasFormat[FormatPatterns.BOLD.ordinal()];
        }

        public boolean isItalics() {
            return hasFormat[FormatPatterns.ITALICS.ordinal()];
        }

        public boolean isUnderline() {
            return hasFormat[FormatPatterns.UNDERLINE.ordinal()];
        }
    }

    private static String commonKeys;

    static {
        String keys = "";
        for (FormatPatterns pattern : FormatPatterns.values()) {
            keys += pattern.rawPat;
        }
        commonKeys = keys;
    }

    private static String escapePattern = "\\\\.";
    private static final String txtName = "TXT", escape = "ESC";

    public enum LineEnders {
        TABLE("|"), REF("}"), HEADING("#"), LINKS(">"), NONE("");

        private String rawTextPat, rawWordPat;
        private Pattern wordPattern;
        private Pattern textPattern;

        LineEnders(String ender) {
            String wordPat = "[^" + commonKeys + ender + "]+";
            wordPattern = Pattern.compile(
                Span.namePattern(txtName, wordPat) + "|" + Span.namePattern(
                    escape, escapePattern
                )
            );

            rawWordPat = wordPat + "|" + escapePattern;
            rawTextPat = "[^" + ender + "\n]+|" + escapePattern;

            String pattern = "";
            pattern += Span.namePattern(FormatPatterns.BOLD) + "|";
            pattern += Span.namePattern(FormatPatterns.ITALICS) + "|";
            pattern += Span.namePattern(FormatPatterns.UNDERLINE) + "|";
            pattern += Span.namePattern(txtName, rawWordPat);
            textPattern = Pattern.compile(pattern);

        }

        public String getTextName() {
            return "TEXT";
        }

        public String getTextPattern(boolean withName) {
            return withName ? Span.namePattern(getTextName(), rawTextPat) :
                "(" + rawTextPat + ")";
        }

    }

    private final LineTypes lineType;
    private final ImmutableList<SpanStyles> otherStyles;
    private final ImmutableList<Span> childrenSpans;

    LinePhrase(
        String text, DocBuilder docBuilder, LineTypes type, SpanStyles... others
    ) {
        super(docBuilder);
        lineType = type;
        otherStyles = new ImmutableList.Builder<SpanStyles>().add(
            type.getStyle()
        ).add(others).build();

        ImmutableList<SpanStyles> optStyle = new ImmutableList.Builder<
            SpanStyles>().addAll(otherStyles).add(SpanStyles.OPERATOR).build();

        LineEnders ender = type.getEnder();

        Matcher matcher = ender.textPattern.matcher(text);
        boolean formats[] = { false, false, false };

        ImmutableList<FormatPatterns> formatList = new ImmutableList.Builder<
            FormatPatterns>().add(
                FormatPatterns.BOLD, FormatPatterns.ITALICS,
                FormatPatterns.UNDERLINE
            ).build();

        ImmutableList.Builder<Span> spanBuilder = ImmutableList.builder();

        findLoop: while (matcher.find()) {
            for (FormatPatterns pattern : formatList) {
                String find = Span.match(matcher, pattern);
                if (find != null) {
                    int idx = pattern.ordinal();
                    formats[idx] = !formats[idx];
                    addStyle(matcher, pattern, optStyle);
                    continue findLoop;
                }
            }
            String find = matcher.group(txtName);
            if (find != null) {
                spanBuilder.add(new TextPhrase(find, docBuilder, formats));
            }
        }

        childrenSpans = spanBuilder.build();
    }

    public List<Span> getChildren() {
        return childrenSpans;
    }
}
