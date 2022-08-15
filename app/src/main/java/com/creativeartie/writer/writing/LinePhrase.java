package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

import com.google.common.collect.*;

public class LinePhrase extends Span {

    private enum FormatPatterns {
        // @orderFor Word.Word and TypedStyles
        BOLD("\\*"), ITALICS("`"), UNDERLINE("_"),
        // @endOrder Word.Word

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
            StringBuilder string = new StringBuilder();
            ImmutableList.Builder<TypedStyles> baseStyles = ImmutableList
                .builder();
            baseStyles.addAll(otherStyles);
            hasFormat = formats;
            int i = 0;
            for (boolean format : formats) {
                if (format) {
                    baseStyles.add(
                        TypedStyles.values()[TypedStyles.BOLD.ordinal() + i]
                    );
                }
                i++;
            }

            TypedStyles textStyle[] = ImmutableList.builder().addAll(
                baseStyles.build()
            ).add(TypedStyles.TEXT).build().toArray(new TypedStyles[0]);
            TypedStyles escOptStyle[] = ImmutableList.builder().addAll(
                baseStyles.build()
            ).add(TypedStyles.ESCAPE, TypedStyles.OPERATOR).build().toArray(
                new TypedStyles[0]
            );
            TypedStyles escTxtStyle[] = ImmutableList.builder().addAll(
                baseStyles.build()
            ).add(TypedStyles.ESCAPE, TypedStyles.TEXT).build().toArray(
                new TypedStyles[0]
            );

            Matcher match = lineType.getEnder().wordPattern.matcher(text);
            while (match.find()) {
                String find = match.group(txtName);
                System.out.println(find);
                if (find != null) {
                    string.append(find);
                    docBuilder.addStyle(match, txtName, textStyle);
                    continue;
                }
                find = match.group(escape);
                if (find != null) {
                    string.append(find.charAt(1));
                    docBuilder.addTextStyle("\\", escOptStyle);
                    docBuilder.addTextStyle(find.substring(1), escTxtStyle);
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

    private final ParaTypes lineType;
    private final ImmutableList<TypedStyles> otherStyles;
    private final ImmutableList<Span> childrenSpans;

    LinePhrase(
        String text, DocBuilder docBuilder, ParaTypes type,
        TypedStyles... others
    ) {
        lineType = type;
        otherStyles = new ImmutableList.Builder<TypedStyles>().add(
            type.getStyle()
        ).add(others).build();

        ImmutableList<TypedStyles> optStyle = new ImmutableList.Builder<
            TypedStyles>().addAll(otherStyles).add(TypedStyles.OPERATOR)
                .build();

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
                    docBuilder.addStyle(matcher, pattern, optStyle);
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
