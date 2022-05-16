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
public final class Identifier extends Span {
    /**
     * Pattern groups use in {@link Pattern}.
     *
     * @author wai-kin
     */
    private enum Patterns {
        /** List of possible characters in a name */
        CHARS("[\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit}]+"),
        /** List of spaces available. @see #OPTIONAL */
        SPACERS("( |_|\t)+"),
        /** List of spaces available. @see #SPACERS */
        OPTIONAL("( |_|\t)*"),
        /** The category/id name. */
        NAME(CHARS + "(" + OPTIONAL + CHARS + ")*"),
        /** Separator between names */
        SEP(OPTIONAL + "-" + OPTIONAL),
        /** For {@link #getIdPattern()} */
        FULL(OPTIONAL + NAME.toString() + "(" + SEP + NAME + ")*" + OPTIONAL);

        private final String rawPattern;
        private final String namedPattern;

        Patterns(String pattern) {
            rawPattern = pattern;
            namedPattern = namePattern(this);
        }

        @Override
        public String toString() {
            return rawPattern;
        }
    }

    static String getPhraseName() {
        return "ID";
    }

    static String getPhrasePattern(boolean withName) {
        return withName ?
            namePattern(getPhraseName(), Patterns.FULL.rawPattern) :
            Patterns.FULL.rawPattern;
    }

    /** Pre-compiled spaces pattern */
    private static final Pattern spacesPat =
        Pattern.compile(Patterns.SPACERS.rawPattern);
    /** Pre-compiled check pattern */
    private static final Pattern checkPat =
        Pattern.compile("^" + Patterns.FULL.rawPattern + "$");
    /** Pre-compiled breaking id pattern */
    private static final Pattern parsePat = Pattern
        .compile(Patterns.NAME.namedPattern + "|" + Patterns.SEP.namedPattern);

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
                .trimResults().split(match.group(Patterns.NAME.name()))
        );

    }

    private final IdGroups idType;

    private final boolean isIdentitfier;

    private final List<String> idCategories;

    private final ImmutableList<Integer> spanStylePositions;

    private final String idName;

    Identifier(
        IdGroups group, String text, DocBuilder docBuilder, boolean isId
    ) {
        idType = group;
        isIdentitfier = isId;
        ImmutableList.Builder<Integer> positions = ImmutableList.builder();
        TypedStyles idStyle = TypedStyles.ID;
        TypedStyles grpType = group.toTypedStyles();

        // No match
        if (!checkPat.matcher(text).find()) {
            idName = "";
            idCategories = ImmutableList.of();
            docBuilder.addTextStyle(text, grpType, idStyle, TypedStyles.ERROR);
            spanStylePositions = ImmutableList.of();
            return;
        }

        final ImmutableList.Builder<String> catBuilder =
            ImmutableList.builder();

        // Start space padding
        Matcher spaces = spacesPat.matcher(text);
        // ! spaces in the middle isn't the start of the id !
        if (spaces.find() && (spaces.start() == 0)) {
            positions.add(
                docBuilder
                    .addStyle(spaces, grpType, idStyle, TypedStyles.OPERATOR)
            );
        }

        // Parts of an id
        Matcher match = parsePat.matcher(text);
        String name = "";
        int start = 0;
        while (match.find()) if (match.group(Patterns.NAME.name()) != null) {
            // A name is found
            positions.add(
                docBuilder.addStyle(
                    match, Patterns.NAME, grpType, idStyle, TypedStyles.NAME
                )
            );
            // Last name found is a category, not an id
            if (!name.isBlank()) {
                catBuilder.add(name);
            }
            name = simplizeName(match);
            start = match.end();

        } else if (match.group(Patterns.SEP.name()) != null) {
            // A separator is found
            positions.add(
                docBuilder.addStyle(
                    match, Patterns.SEP, grpType, idStyle, TypedStyles.OPERATOR
                )
            );
        }

        // end space padding
        if (spaces.find(start)) {
            positions.add(
                docBuilder
                    .addStyle(spaces, grpType, idStyle, TypedStyles.OPERATOR)
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
        if (idCategories.isEmpty()) return idName;
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
