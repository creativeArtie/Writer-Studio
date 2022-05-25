package com.creativeartie.writer.writing;

import java.util.regex.*;

import com.google.common.base.*;

public final class RefPhrase extends Span {

    private enum Types {
        FOOTNOTE("\\{\\^"), ENDNOTE("\\{\\*"), SOURCE("\\{\\>");

        private final String textPattern;

        Types(String pattern) {
            textPattern = pattern;
        }

        static String listPatterns(boolean withName) {
            String patterns = "";
            for (Types type : values()) {
                if (!patterns.isEmpty()) {
                    patterns += "|";
                }
                patterns +=
                    withName ? Span.namePattern(type) : type.textPattern;
            }
            return "(" + patterns + ")";
        }

        @Override
        public String toString() {
            return textPattern;
        }
    }

    private static final String endPatName = "END";
    private static final String endPat = "\\}?";

    private static final Pattern PATTERN = Pattern.compile(
        Types.listPatterns(true) + Identifier.getPhrasePattern(true) +
            namePattern(endPatName, endPat)
    );

    static String getPhraseName() {
        return "REF";
    }

    static String getPhrasePattern(boolean withName) {
        String pattern = Types.listPatterns(false);
        pattern += Identifier.getPhrasePattern(false) + endPat;
        return withName ? namePattern(getPhraseName(), pattern) : pattern;
    }

    private final IdGroups refType;
    private final Identifier idName;

    RefPhrase(String text, DocBuilder docBuilder) {
        Matcher match = PATTERN.matcher(text);
        Preconditions.checkArgument(match.find(), "Text pattern not found");
        Types found = null;

        for (Types type : Types.values()) {
            if (match.group(type.name()) != null) {
                found = type;
                break;
            }

        }
        assert found != null;
        refType = IdGroups.values()[found.ordinal()];
        docBuilder.addStyle(
            match, found, refType.toTypedStyles(), TypedStyles.OPERATOR
        );
        idName = new Identifier(
            refType, match.group(Identifier.getPhraseName()), docBuilder, false
        );
        docBuilder.addStyle(
            match, endPatName, refType.toTypedStyles(), TypedStyles.OPERATOR
        );
    }

    public Identifier getId() {
        return idName;
    }

    public IdGroups getType() {
        return refType;
    }

}
