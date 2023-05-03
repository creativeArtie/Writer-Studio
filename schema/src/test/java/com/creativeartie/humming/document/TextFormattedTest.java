package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import com.creativeartie.humming.schema.*;

@SuppressWarnings("nls")
class TextFormattedTest extends SpanBranchTestBase<TextFormatted> {
    private TextFormattedPatterns testPattern;

    @ParameterizedTest
    @EnumSource
    void testBold(TextFormattedPatterns pattern) {
        testPattern = pattern;
        TextFormatted test = newSpan("*bold*");
        addStyleTest("*", CssSpanStyles.OPERATOR);
        addStyleTest("bold", CssSpanStyles.BOLD, CssSpanStyles.TEXT);
        addStyleTest("*", CssSpanStyles.OPERATOR);
        testStyles(test);
        assertEquals(1, test.getWrittenCount());
        assertEquals(0, test.getOutlineCount());
    }

    @ParameterizedTest
    @EnumSource
    void testItalics(TextFormattedPatterns pattern) {
        testPattern = pattern;
        TextFormatted test = newSpan("`italics`");
        addStyleTest("`", CssSpanStyles.OPERATOR);
        addStyleTest("italics", CssSpanStyles.ITALICS, CssSpanStyles.TEXT);
        addStyleTest("`", CssSpanStyles.OPERATOR);
        testStyles(test);
        assertEquals(1, test.getWrittenCount());
        assertEquals(0, test.getOutlineCount());
    }

    @ParameterizedTest
    @EnumSource
    void testUnderline(TextFormattedPatterns pattern) {
        testPattern = pattern;
        TextFormatted test = newSpan("_underline_");
        addStyleTest("_", CssSpanStyles.OPERATOR);
        addStyleTest("underline", CssSpanStyles.UNDERLINE, CssSpanStyles.TEXT);
        addStyleTest("_", CssSpanStyles.OPERATOR);
        testStyles(test);
        assertEquals(1, test.getWrittenCount());
        assertEquals(0, test.getOutlineCount());
    }

    @ParameterizedTest
    @EnumSource
    void testTodo(TextFormattedPatterns pattern) {
        testPattern = pattern;
        TextFormatted test = newSpan("Mix{!to do text}ed");
        addStyleTest("Mix", CssSpanStyles.TEXT);
        addStyleTest("{!", CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        addStyleTest("to do text", CssSpanStyles.AGENDA, CssSpanStyles.TEXT);
        addStyleTest("}", CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        addStyleTest("ed", CssSpanStyles.TEXT);
        testStyles(test);
        assertEquals(1, test.getWrittenCount());
        assertEquals(3, test.getOutlineCount());
    }

    @ParameterizedTest
    @EnumSource
    void testWordCount(TextFormattedPatterns pattern) {
        testPattern = pattern;
        TextFormatted test = newSpan("    some _text:_ un*believe*able. ");
        addStyleTest("    some ", CssSpanStyles.TEXT);

        addStyleTest("_", CssSpanStyles.OPERATOR);
        addStyleTest("text:", CssSpanStyles.UNDERLINE, CssSpanStyles.TEXT);
        addStyleTest("_", CssSpanStyles.OPERATOR);

        addStyleTest(" un", CssSpanStyles.TEXT);

        addStyleTest("*", CssSpanStyles.OPERATOR);
        addStyleTest("believe", CssSpanStyles.BOLD, CssSpanStyles.TEXT);
        addStyleTest("*", CssSpanStyles.OPERATOR);

        addStyleTest("able. ", CssSpanStyles.TEXT);

        testStyles(test);
        assertEquals(3, test.getWrittenCount());
        assertEquals(0, test.getOutlineCount());
    }

    @ParameterizedTest
    @EnumSource
    void testRef(TextFormattedPatterns pattern) {
        testPattern = pattern;
        TextFormatted test = newSpan("{^hello}");
        if (pattern != TextFormattedPatterns.NOTE) {
            addStyleTest("{", CssSpanStyles.FOOTNOTE, CssSpanStyles.OPERATOR);
            addStyleTest("*", CssSpanStyles.FOOTNOTE, CssSpanStyles.OPERATOR);
            addStyleTest("hello", CssSpanStyles.FOOTNOTE, CssSpanStyles.ID, CssSpanStyles.TEXT);
            addStyleTest("}", CssSpanStyles.FOOTNOTE, CssSpanStyles.OPERATOR);
            assertEquals(0, test.getWrittenCount());
        } else {
            addStyleTest("{^hello}", CssSpanStyles.TEXT);
            assertEquals(1, test.getWrittenCount());
        }

        testStyles(test);

        assertEquals(0, test.getOutlineCount());
    }

    @Override
    protected TextFormatted initSpan(SpanBranch parent, String input) {
        switch (testPattern) {
            case BASIC:
                return TextFormatted.newBasicText(parent, input);
            case CELL:
                return TextFormatted.newCellText(parent, input);
            case HEADING:
                return TextFormatted.newHeadingText(parent, input);
            case NOTE:
                return TextFormatted.newNoteText(parent, input);
            default:
                return null;
        }
    }
}
