package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.creativeartie.humming.document.*;
import com.creativeartie.humming.files.*;

/**
 * Patterns for basic text with escape chars.
 */
@SuppressWarnings("nls")
public enum TextSpanPatterns implements PatternEnum {
    /**
     * Text pattern for ID
     *
     * @see IdentityPattern
     */
    ID() {
        @Override
        String getValuePattern() {
            return "[\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit} _\\t]+";
        }
    },
    /**
     * Text pattern for in-line malformed address id error and Todo.
     *
     * @see IdentityReferencePattern
     * @see IdentityTodoPattern
     */
    SPECIAL() {
        @Override
        String getValuePattern() {
            return "[^\\}\\n" + Literals.TEXTPAT_FORMATS.getText() + "]+";
        }
    },
    /**
     * Text pattern for basic cases
     *
     * @see TextFormattedPatterns#BASIC
     * @see TextFormattedPatterns#NOTE
     */
    TEXT() {
        @Override
        String getValuePattern() {
            return "[^\\n" + Literals.TEXTPAT_REF.getText() + "]+";
        }
    },
    /**
     * Text pattern for malformed address line id text
     *
     * @see ParaHeadingPattern
     * @see ParaNotePatterns
     * @see ParaReferencePatterns
     * @see ParaBasicPatterns
     */
    ERROR() {
        @Override
        String getValuePattern() {
            return "[^\\n" + Literals.TEXTPAT_ESCAPE.getText() + "]+";
        }
    },
    /**
     * Text format for heading
     *
     * @see TextFormattedPatterns#HEADING
     */
    HEADING() {
        @Override
        String getValuePattern() {
            return "[^\\n\\#" + Literals.TEXTPAT_REF.getText() + "]+";
        }
    },
    /**
     * Text for for note field's key
     *
     * @see ParaNotePatterns
     */
    KEY() {
        @Override
        String getValuePattern() {
            return "[\\p{IsAlphabetic}_\s]+";
        }
    },
    /**
     * Text format for table cell
     *
     * @see TextFormattedPatterns#CELL
     */
    CELL() {
        @Override
        String getValuePattern() {
            return "[^\\n\\|" + Literals.TEXTPAT_REF.getText() + "]+";
        }
    },
    /**
     * Text format for note. Uses in {@link TextFormatted}.
     */
    NOTE {
        @Override
        String getValuePattern() {
            return "([^\\n" + Literals.TEXTPAT_REF.getText() + "]|(\\{[^!]))+";
        }
    };

    /** Parts of a text */
    public enum TextSpanParts implements PatternEnum {
        /** escape text pattern. This includes escaping no character. */
        ESCAPE("(\\\\.|\\\\$)"),
        /** Placeholder for text pattern. */
        TEXT("");

        private final String pattern;

        TextSpanParts(String pat) {
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
    }

    abstract String getValuePattern();

    private final String basePattern;
    private Pattern compiledPattern;

    TextSpanPatterns() {
        // @formatter:off
        basePattern = "(" +
                TextSpanParts.ESCAPE.getRawPattern() + "|" +
                getValuePattern() +
            ")+";
        // @formatter:on
    }

    /**
     * Match text to this pattern
     *
     * @param text
     *        the text to match
     *
     * @return Matcher of null if not matched
     */
    public Matcher matcher(String text) {
        if (compiledPattern == null) compiledPattern = Pattern.compile(
        // @formatter:off
            "(" +
                TextSpanParts.ESCAPE.getNamedPattern() + "|" +
                PatternEnum.namePattern(TextSpanParts.TEXT.name(),
                    TextSpanParts.ESCAPE.getRawPattern() + "|" +
                    getValuePattern()
                ) +
            ")"
        // @formatter:on
        );
        return compiledPattern.matcher(text);
    }

    @Override
    public String getRawPattern() {
        return basePattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
