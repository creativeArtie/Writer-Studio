package com.creativeartie.writer.writing;

import java.util.regex.*;

import com.google.common.base.*;

public class TodoPhrase extends Span {

    private enum Patterns {
        START("\\{"), TEXT("[^\\}]*"), END("\\}"),
        FULL(START.toString() + TEXT + END);

        final String rawPattern;

        Patterns(String pattern) {
            rawPattern = pattern;
        }

        @Override
        public String toString() {
            return rawPattern;
        }
    }

    private static Pattern phrasePattern = Pattern.compile(
        namePattern(Patterns.START) + namePattern(Patterns.TEXT) +
            namePattern(Patterns.END) + "?"
    );

    TodoPhrase(String text, DocBuilder builder) {
        Matcher matched = phrasePattern.matcher(text);
        Preconditions.checkArgument(matched.find(), "Text not found.");

    }
}
