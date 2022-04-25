package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

public class IDSpan extends Span {
    private static String ALLOWED_ID_CHAR =
        "[\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit}]+";

    private static String ID_PART =
        ALLOWED_ID_CHAR + "(( |_)*" + ALLOWED_ID_CHAR + ")*";

    static String TEXT_ID = ID_PART + "( *- *" + ID_PART + ")*";

    private Pattern PATTERN = Pattern.compile(
        "(?<" + PatternGroups.ID_FIRST.name() + ">" + ID_PART + ")\"( *(?<" +
            PatternGroups.ID_KEY.name() + ">-) *<" +
            PatternGroups.ID_SECOND.name() + ID_PART + "))*"
    );

    private enum PatternGroups {
        ID_FIRST, ID_KEY, ID_SECOND;
    }

    private boolean isReference;

    private List<String> categories;

    private String name;

    public IDSpan(String text) {
        Matcher match = PATTERN.matcher(text);
        while (match.find()) {

        }
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getName() {
        return name;
    }

    public boolean isRef() {
        return isReference;
    }

}
