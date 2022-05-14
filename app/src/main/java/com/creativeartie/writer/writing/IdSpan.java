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
public final class IdSpan extends Span {
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

    static String getIdName() {
        return "ID";
    }

    static String getIdPattern() {
        return "(<" + getIdName() + ">" + TEXT_ID + ")";
    }

    /** characters relevant to the id, not including the collapsed spaces */
    private final static String ALLOWED_ID_CHAR =
        "[\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit}]+";

    /**
     * Spaces in the id, {@link SPACES} needs it not optional
     *
     * @see ID_SPACES
     */
    private static final String RE_SPACES = " |_|\t";

    /**
     * Other optional spaces in the id
     *
     * @see RE_SPACES
     */
    private static final String ID_SPACES = "(" + RE_SPACES + ")*";

    /** The name or category of the id */
    private final static String ID_PART =
        ALLOWED_ID_CHAR + "(" + ID_SPACES + ALLOWED_ID_CHAR + ")*";

    /**
     * The complete ID
     */
    private static final String TEXT_ID = ID_SPACES + "?" + ID_PART + "(" +
        ID_SPACES + "-" + ID_SPACES + ID_PART + ")*" + ID_SPACES + "?";

    /** Pre-compiled spaces pattern */
    private static final Pattern SPACES = Pattern.compile(RE_SPACES);
    /** Pre-compiled check pattern */
    private static final Pattern CHECK = Pattern.compile("^" + TEXT_ID + "$");
    /** Pre-compiled breaking id pattern */
    private static final Pattern GROUPS =
        Pattern.compile(Groups.NAME.PATTERN + "|" + Groups.SEP.PATTERN);

    /**
     * Collapse spaces for an id.
     *
     * @param  match
     *               the matcher to use
     * @return       the id
     */
    private static String simplizeName(Matcher match) {
        return Joiner.on(" ").join(
            Splitter.on(CharMatcher.anyOf(" \t_")).omitEmptyStrings()
                .trimResults().split(match.group(Groups.NAME.name()))
        );

    }

    private final IdGroups idType;

    private final boolean isIdentitfier;

    private final List<String> idCategories;

    private final ImmutableList<Integer> spanStylePositions;

    private final String idName;

    public IdSpan(
        IdGroups group, String text, DocBuilder docBuilder, boolean isId
    ) {
        idType = group;
        isIdentitfier = isId;
        ImmutableList.Builder<Integer> positions = ImmutableList.builder();
        TypedStyles idStyle = TypedStyles.IDER;
        TypedStyles grpType = group.toTypedStyles();

        // No match
        if (!CHECK.matcher(text).find()) {
            idName = "";
            idCategories = ImmutableList.of();
            docBuilder.addTextStyle(text, grpType, idStyle, TypedStyles.ERROR);
            spanStylePositions = ImmutableList.of();
            return;
        }

        final ImmutableList.Builder<String> catBuilder =
            ImmutableList.builder();

        // Start space padding
        Matcher spaces = SPACES.matcher(text);
        // ! spaces in the middle isn't the start of the id !
        if (spaces.find() && (spaces.start() == 0)) {
            positions.add(
                docBuilder.addStyle(spaces, grpType, idStyle, TypedStyles.OPER)
            );
        }

        // Parts of an id
        Matcher match = GROUPS.matcher(text);
        String name = "";
        int start = 0;
        while (match.find()) if (match.group(Groups.NAME.name()) != null) {
            // A name is found
            positions.add(
                docBuilder.addStyle(
                    match, Groups.NAME, grpType, idStyle, TypedStyles.NAME
                )
            );
            // Last name found is a category, not an id
            if (!name.isBlank()) {
                catBuilder.add(name);
            }
            name = simplizeName(match);
            start = match.end();

        } else if (match.group(Groups.SEP.name()) != null) {
            // A separator is found
            positions.add(
                docBuilder.addStyle(
                    match, Groups.SEP, grpType, idStyle, TypedStyles.OPER
                )
            );
        }

        // end space padding
        if (spaces.find(start)) {
            positions.add(
                docBuilder.addStyle(spaces, grpType, idStyle, TypedStyles.OPER)
            );
        }

        idName = name;
        idCategories = catBuilder.build();
        spanStylePositions = positions.build();

        if (isRef()) {
            docBuilder.addCleanup((builder) -> {
                if (!builder.containsId(idType, getId())) {
                    for (int idx : spanStylePositions) {
                        builder.insertStyles(idx, TypedStyles.ERROR);
                    }
                }
            });
        } else {
            if (!docBuilder.putId(idType, getId())) {
                for (int idx : spanStylePositions) {
                    docBuilder.insertStyles(idx, TypedStyles.ERROR);
                }
            }
        }
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
        return !isIdentitfier;
    }

    public boolean isId() {
        return isIdentitfier;
    }

}
