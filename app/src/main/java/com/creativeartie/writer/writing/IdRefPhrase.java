package com.creativeartie.writer.writing;

import java.util.regex.*;

import com.google.common.base.*;

public final class IdRefPhrase extends Span {
    
    private static final String endPatName = "END";
    private static final String endPat = "\\}?";

    private static final Pattern PATTERN = Pattern.compile(
        IdTypes.listPatterns(true) + IdMarkerPhrase.getPhrasePattern(true) +
            namePattern(endPatName, endPat)
    );

    static String getPhraseName() {
        return "REF";
    }

    static String getPhrasePattern(boolean withName) {
        String pattern = IdTypes.listPatterns(false);
        pattern += IdMarkerPhrase.getPhrasePattern(false) + endPat;
        return withName ? namePattern(getPhraseName(), pattern) : pattern;
    }

    private final IdTypes refType;
    private final IdMarkerPhrase idName;

    IdRefPhrase(String text, DocBuilder docBuilder) {
        super(docBuilder);
        Matcher match = PATTERN.matcher(text);
        Preconditions.checkArgument(match.find(), "Text pattern not found");
        IdTypes found = null;

        for (IdTypes type : IdTypes.values()) {
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
