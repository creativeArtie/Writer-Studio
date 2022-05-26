package com.creativeartie.writer.writing;

import java.util.regex.*;

import com.google.common.collect.*;

public class TextPhrase extends Span {

    private enum Patterns {
        // @orderFor Word.Word and TypedStyles
        BOLD("*"), ITALICS("`"), UNDERLINE("_"),
        // @endOrder Word.Word

        REF_START("{"), ESCAPE("\\");

        @Override
        public String toString() {
            return rawPat;
        }

        private final String rawPat;

        Patterns(String pat) {
            rawPat = pat;
        }
    }

    public class Word extends Span {
        static {
            String keys = "";
            for (Patterns pattern : Patterns.values()) {
                keys += pattern.rawPat;
            }
            commonKeys = keys;
        }
        private final String storedText;
        private final boolean hasFormat[];
        private static final String commonKeys, txtName = "TXT", escape = "ESC",
            escapePattern = "\\\\.";
        private static String phraseName = "WORD";

        public static String getPhraseName() {
            return phraseName;
        }

        public static String getPhrasePattern(boolean withName) {
            return getPhrasePattern(withName, TextEnders.NONE);
        }

        public static String getPhrasePattern(
            boolean withName, TextEnders ender
        ) {
            String pat = "[^" + commonKeys + ender.textEnder + "]+|" +
                escapePattern;
            return (withName ? namePattern(phraseName, pat) : "(" + pat + ")") +
                "+";

        }

        private Word(String text, DocBuilder docBuilder, boolean... formats) {
            StringBuilder string = new StringBuilder();
            ImmutableList.Builder<TypedStyles> baseStyles = ImmutableList
                .builder();
            baseStyles.add(lineType.getStyle());
            baseStyles.addAll(otherStyles);
            hasFormat = formats;
            int i = 0;
            for (boolean format : formats) {
                if (format) {
                    baseStyles.add(
                        TypedStyles.values()[TypedStyles.BOLD.ordinal() + i]
                    );
                    i++;
                }
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
            Matcher match = Pattern.compile(
                Span.namePattern(
                    txtName, "[^" + commonKeys + lineType.getEnder().textEnder +
                        "]+"
                ) + "|" + Span.namePattern(escape, escapePattern)
            ).matcher(text);
            while (match.find()) {
                String find = match.group(txtName);
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
            return hasFormat[Patterns.BOLD.ordinal()];
        }

        public boolean isItalics() {
            return hasFormat[Patterns.ITALICS.ordinal()];
        }

        public boolean isUnderline() {
            return hasFormat[Patterns.UNDERLINE.ordinal()];
        }
    }

    public enum TextEnders {
        TABLE("|"), REF("}"), HEADING("#"), LINKS(">"), NONE("");

        private String textEnder;

        TextEnders(String ender) {
            textEnder = ender;
        }
    }

    private final ParaTypes lineType;
    private final ImmutableList<TypedStyles> otherStyles;

    TextPhrase(
        String text, DocBuilder docBuilder, ParaTypes type,
        TypedStyles... others
    ) {
        lineType = type;
        otherStyles = ImmutableList.copyOf(others);
    }
}
