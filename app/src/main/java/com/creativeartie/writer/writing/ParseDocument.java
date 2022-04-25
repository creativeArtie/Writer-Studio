package com.creativeartie.writer.writing;

import java.util.regex.*;

public class ParseDocument {

    // ID
    private static String ID_WORD =
        "[\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit}]+";

    private static String ID_TEXT = ID_WORD + "(( |_)*" + ID_WORD + ")*";

    private static Pattern SYNTAX = Pattern.compile("");

    static enum Phrases {
        ID(ID_TEXT + "( *- *" + ID_TEXT + ")*"),
        FOOTNOTE("\\{\\^" + ID.format_syntax + "\\}?"),
        ENDNOTE("\\{\\*" + ID.format_syntax + "\\}?");

        final String format_syntax;

        Phrases(String format) {
            format_syntax = "(?<" + name() + ">" + format + ")";
        }
    }

    private ParseDocument() {
    }

}
