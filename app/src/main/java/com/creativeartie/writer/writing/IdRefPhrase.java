package com.creativeartie.writer.writing;

import java.util.regex.*;

import com.google.common.base.*;

public final class IdRefPhrase extends Span {

    private enum Types {
        // @orderFor IdTypes
        FOOTNOTE("\\{\\^"), ENDNOTE("\\{\\*"), SOURCE("\\{\\>");
        // @endOrder

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
                patterns += withName ? Span.namePattern(type) :
                    type.textPattern;
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
        Types.listPatterns(true) + IdMarkerPhrase.getPhrasePattern(true) +
            namePattern(endPatName, endPat)
    );

    static String getPhraseName() {
        return "REF";
    }

    static String getPhrasePattern(boolean withName) {
        String pattern = Types.listPatterns(false);
        pattern += IdMarkerPhrase.getPhrasePattern(false) + endPat;
        return withName ? namePattern(getPhraseName(), pattern) : pattern;
    }

    private final IdTypes refType;
    private final IdMarkerPhrase idName;

    IdRefPhrase(String text, DocBuilder docBuilder) {
        super(docBuilder);
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
        refType = IdTypes.values()[found.ordinal()];
        addStyle(match, found, refType.toTypedStyles(), SpanStyles.OPERATOR);
        idName = new IdMarkerPhrase(
            refType, match.group(IdMarkerPhrase.getPhraseName()), docBuilder,
            false
        );
        addStyle(
            match, endPatName, refType.toTypedStyles(), SpanStyles.OPERATOR
        );
    }

    public IdMarkerPhrase getId() {
        return idName;
    }

    public IdTypes getType() {
        return refType;
    }

}
