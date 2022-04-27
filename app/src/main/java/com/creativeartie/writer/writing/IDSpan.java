package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * Creates a phrase for identifiers. An identifier can be in any language and
 * digits. It doesn't allow punctuation. An identifier can have categories. The
 * categories are separated by "-" character.
 *
 * @author wai
 */
public class IDSpan extends Span {
    /**
     * Pattern groups use in {@link GROUPS}.
     *
     * @author wai-kin
     */
    private enum Groups {
        NAME(ID_PART), SEP(ID_SPACES + "-" + ID_SPACES);

        private final String PATTERN;

        Groups(String pattern) {
            PATTERN = "(?<" + name() + ">" + pattern + ")";

        }
    }

    /**
     * Check if text won't throw an exception
     *
     * @param  text
     *              the text to text
     * @return      {@code true} if no issue.
     */
    public static boolean check(String text) {
        return CHECK.matcher(text).find();
    }

    private final static String ALLOWED_ID_CHAR =
        "[\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit}]+";

    static final String ID_SPACES = "( |_|\t)*";

    private final static String ID_PART =
        ALLOWED_ID_CHAR + "(" + ID_SPACES + ALLOWED_ID_CHAR + ")*";

    final static String TEXT_ID =
        ID_PART + "(" + ID_SPACES + "-" + ID_SPACES + ID_PART + ")*";
    private final static Pattern CHECK = Pattern.compile("^" + TEXT_ID + "$");

    private final static Pattern GROUPS =
        Pattern.compile(Groups.NAME.PATTERN + "|" + Groups.SEP.PATTERN);

    private static String simplizeName(Matcher match) {
        return Joiner.on(" ").join(
            Splitter.on(CharMatcher.anyOf(" \t_")).omitEmptyStrings()
                .trimResults().split(match.group(Groups.NAME.name()))
        );

    }

    private boolean isReference;

    private final List<String> idCategories;

    private final String idName;

    public IDSpan(String text, StyleBuilder docBuilder) {
        Preconditions.checkArgument(
            CHECK.matcher(text).find(), "Text doesn't fit pattern."
        );
        final TypedStyles[] nameStyle = { TypedStyles.NAME, TypedStyles.IDER };
        final TypedStyles[] sepStyle = { TypedStyles.OPER, TypedStyles.IDER };
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        final Matcher match = GROUPS.matcher(text);
        String name = "";

        while (match.find()) if (match.group(Groups.NAME.name()) != null) {
            docBuilder.addStyle(match, Groups.NAME, nameStyle);
            if (!name.isBlank()) {
                builder.add(name);
            }
            name = simplizeName(match);
        } else if (match.group(Groups.SEP.name()) != null) {
            docBuilder.addStyle(match, sepStyle);
        }

        idName = name;
        idCategories = builder.build();

    }

    public List<String> getCategories() {
        return idCategories;
    }

    public String getId() {
        return Joiner.on("-").join(idCategories) + "-" + idName;
    }

    public String getName() {
        return idName;
    }

    public boolean isRef() {
        return isReference;
    }

}
