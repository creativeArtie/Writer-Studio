package com.creativeartie.writer.writing;

import java.util.regex.*;

import com.google.common.base.*;

public class TodoPhrase extends Span {

    private final String todoText;

    private enum Patterns {
        START("\\{!"), TEXT("[^\\}]*"), END("\\}"), FULL(
            START.toString() + TEXT + END
        );

        final String rawPattern;

        Patterns(String pattern) {
            rawPattern = pattern;
        }

        @Override
        public String toString() {
            return rawPattern;
        }
    }

    static String getPhraseName() {
        return "Todo";
    }

    static String getPhrasePattern(boolean withName) {
        String pattern = Patterns.START.toString() + Patterns.TEXT +
            Patterns.END;
        return withName ? namePattern(getPhraseName(), pattern) : pattern;
    }

    private static Pattern phrasePattern = Pattern.compile(
        namePattern(Patterns.START) + namePattern(Patterns.TEXT) + namePattern(
            Patterns.END
        ) + "?"
    );

    TodoPhrase(String value, DocBuilder docBuilder) {
        Matcher matched = phrasePattern.matcher(value);
        Preconditions.checkArgument(matched.find(), "Text not found.");
        docBuilder.addStyle(
            matched, Patterns.START, TypedStyles.TODO, TypedStyles.OPERATOR
        );
        String text = matched.group(Patterns.TEXT.name());
        if (text == null) {
            todoText = "";
        } else {
            docBuilder.addStyle(
                matched, Patterns.TEXT, TypedStyles.TODO, TypedStyles.TEXT
            );
            todoText = text;
        }
        if (matched.group(Patterns.END.name()) != null) {
            docBuilder.addStyle(
                matched, Patterns.END, TypedStyles.TODO, TypedStyles.OPERATOR
            );
        }
    }

    public String getTodoText() {
        return todoText;
    }
}
