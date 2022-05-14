package com.creativeartie.writer.writing;

import java.util.regex.*;

import com.google.common.base.*;

public final class RefSpan extends Span {

    private enum Types {
        FOOTNOTE("\\{\\^"), ENDNOTE("\\{\\^"), SOURCE("\\{\\>");

        private final String PATTERN;

        Types(String pattern) {
            PATTERN = pattern;
        }

        static String listPatterns() {
            String patterns = "";
            for (Types type : values()) {
                if (!patterns.isEmpty()) {
                    patterns += "|";
                }
                patterns += "(?<" + type.name() + ">" + type.PATTERN + ")";
            }
            return "(" + patterns + ")";
        }

    }

    private static final String ID_PATTERN = IdSpan.getIdPattern();
    private static final String END_ID = "END";
    private static final String END_PATTERN = "(<" + END_ID + ">\\}?)";

    private static final Pattern PATTERN =
        Pattern.compile(Types.listPatterns() + ID_PATTERN + END_PATTERN);

    private final IdGroups refType;
    private final IdSpan idName;

    public RefSpan(String text, DocBuilder docBuilder) {
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
        docBuilder
            .addStyle(match, found, refType.toTypedStyles(), TypedStyles.OPER);
        idName = new IdSpan(
            refType, match.group(IdSpan.getIdName()), docBuilder, false
        );
        docBuilder
            .addStyle(match, END_ID, refType.toTypedStyles(), TypedStyles.OPER);
    }

}
