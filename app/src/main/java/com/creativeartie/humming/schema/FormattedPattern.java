package com.creativeartie.humming.schema;

import java.util.*;
import java.util.regex.*;

import com.google.common.base.*;

public enum FormattedPattern implements PatternEnum {
    BOLD("\\*"), UNDERLINE("_"), ITALICS("`"),
    LINK(LinkPattern.getFullPattern()), TODO(TodoPattern.getFullPattern()),
    REFER(ReferencePattern.getFullPattern()),
    ERR(ErrorRefPattern.getFullPattern()), TEXT("");

    private static TreeMap<BasicTextPatterns, String> fullPatterns;
    private static TreeMap<BasicTextPatterns, Pattern> checkPatterns;
    private static TreeMap<BasicTextPatterns, Pattern> matchPatterns;

    private static String
        getFullPattern(boolean withName, BasicTextPatterns subtype) {
        StringBuilder pattern = new StringBuilder();

        for (PatternEnum pat : values()) {
            if (pat == TEXT) break;
            pattern.append(pat.getPattern(withName) + "|");
        }
        pattern.append(
            withName ? PatternEnum
                .namePattern(TEXT.getPatternName(), subtype.getRawPattern()) :
                subtype.getRawPattern()
        );
        return pattern.toString();
    }

    public static String getFullPattern(BasicTextPatterns subtype) {
        if (fullPatterns == null) {
            fullPatterns = new TreeMap<>();
        }
        if (!fullPatterns.containsKey(subtype)) {
            String answer = getFullPattern(false, subtype);
            fullPatterns.put(subtype, answer);
            return answer;

        }
        return fullPatterns.get(subtype);
    }

    public static Matcher matcher(String text, BasicTextPatterns subtype) {
        if (checkPatterns == null) {
            checkPatterns = new TreeMap<>();
            matchPatterns = new TreeMap<>();
        }
        Pattern check, match;
        if (!checkPatterns.containsKey(subtype)) {
            check =
                Pattern.compile("^(" + getFullPattern(true, subtype) + ")*");
            checkPatterns.put(subtype, check);
            match = Pattern.compile(getFullPattern(true, subtype));
            matchPatterns.put(subtype, match);
        } else {
            check = checkPatterns.get(subtype);
            match = matchPatterns.get(subtype);
        }
        Preconditions
            .checkArgument(check.matcher(text).find(), "Pattern not found");
        return match.matcher(text);
    }

    private final String pattern;

    private FormattedPattern(String pat) {
        pattern = pat;
    }

    @Override
    public String getRawPattern() {
        return pattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }

    @Override
    public boolean runFind() {
        return true;
    }

}
