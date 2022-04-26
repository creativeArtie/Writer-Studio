package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class IDSpan extends Span {
    private final static String ALLOWED_ID_CHAR =
        "[\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit}]+";

    static final String ID_SPACES = "( |_|\t)*";

    private final static String ID_PART =
        ALLOWED_ID_CHAR + "(" + ID_SPACES + ALLOWED_ID_CHAR + ")*";

    final static String TEXT_ID =
        ID_PART + "(" + ID_SPACES + "-" + ID_SPACES + ID_PART + ")*";

    private final static String LEFTOVER =
        ID_PART + "(" + ID_SPACES + "-" + ID_SPACES + TEXT_ID + ")*";

    private final static Pattern PATTERN =
        Pattern.compile(Groups.FIRST.PATTERN + Groups.REST.PATTERN);

    private enum Groups {
        FIRST(ID_PART), SEP(ID_SPACES + "-" + ID_SPACES),
        REST("(" + Groups.SEP.PATTERN + LEFTOVER + ")?");

        private final String PATTERN;

        Groups(String pattern) {
            PATTERN = "(?<" + name() + ">" + pattern + ")";

        }
    }

    private boolean isReference;

    private List<String> idCategories;

    private String idName;

    private static String simplizeName(String name) {
        return Joiner.on(" ").join(
            Splitter.on(CharMatcher.anyOf(" \t_")).omitEmptyStrings()
                .trimResults().split(name)
        );

    }

    public IDSpan(String text, StyleBuilder docBuilder) {
        Matcher matcher = PATTERN.matcher(text);
        Preconditions
            .checkArgument(matcher.find(), "Text doesn't fit pattern.");
        final TypedStyles[] nameStyle = { TypedStyles.NAME, TypedStyles.IDER };
        final TypedStyles[] sepStyle = { TypedStyles.OPER, TypedStyles.IDER };
        String name = match(matcher, Groups.FIRST).trim();
        String rest = match(matcher, Groups.REST);
        if ((rest == null) || rest.isBlank()) {
            // No category
            docBuilder.addStyle(matcher, nameStyle);
            idCategories = ImmutableList.of();
            idName = simplizeName(name);
            return;
        }

        // Has categories
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        builder.add(simplizeName(name));
        // Add style for next category/name
        docBuilder.addStyleGroup(matcher, Groups.FIRST, nameStyle);
        docBuilder.addStyleGroup(matcher, Groups.SEP, sepStyle);
        matcher = PATTERN.matcher(rest);
        while (matcher.find()) {
            name = match(matcher, Groups.FIRST).trim();
            rest = match(matcher, Groups.REST);
            if ((rest == null) || rest.isBlank()) {
                // Add style for the name
                docBuilder.addStyleGroup(matcher, Groups.FIRST, nameStyle);
                idName = simplizeName(name);
            } else {
                // Add style for the category
                docBuilder.addStyleGroup(matcher, Groups.FIRST, nameStyle);
                docBuilder.addStyleGroup(matcher, Groups.SEP, sepStyle);
                builder.add(simplizeName(name));
                matcher = PATTERN.matcher(rest);
            }
        }

        idCategories = builder.build();
    }

    public List<String> getCategories() {
        return idCategories;
    }

    public String getName() {
        return idName;
    }

    public boolean isRef() {
        return isReference;
    }

}
