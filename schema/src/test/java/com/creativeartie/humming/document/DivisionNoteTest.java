package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

import com.google.common.collect.*;

final class DivisionNoteTest extends DivisionTestBase<DivisionNote> {
    public DivisionNoteTest() {
        super(true, DivisionNote.class);
    }

    @Test
    void testFullNote() {
        newDoc("%=Heading#id\n%text\n%>abc=dec\n%>afadf\ntext");
        addStyleTest("%=", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("Heading", CssLineStyles.HEADER, CssSpanStyles.TEXT);
        addStyleTest("#", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("id", CssLineStyles.HEADER, CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);

        addStyleTest("%", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);
        addStyleTest("text", CssLineStyles.NOTE, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);

        addStyleTest("%>", CssLineStyles.FIELD, CssSpanStyles.OPERATOR);
        addStyleTest("abc", CssLineStyles.FIELD, CssSpanStyles.TEXT);
        addStyleTest("=", CssLineStyles.FIELD, CssSpanStyles.OPERATOR);
        addStyleTest("dec", CssLineStyles.FIELD, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.FIELD, CssSpanStyles.OPERATOR);

        addStyleTest("%>", CssLineStyles.FIELD, CssSpanStyles.ERROR, CssSpanStyles.OPERATOR);
        addStyleTest("afadf", CssLineStyles.FIELD, CssSpanStyles.ERROR, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.FIELD, CssSpanStyles.ERROR, CssSpanStyles.OPERATOR);

        addStyleTest("text", CssLineStyles.NORMAL, CssSpanStyles.TEXT);
        testStyles();

        Span id = getSpan(0, 0, 0, 3);

        ImmutableMap<String, String> expect = ImmutableMap.of("abc", "dec");

        // @formatter:off
        newChildAtIndex("section", 0).setSize(2).setClass(DivisionSecChapter.class)
            .newChildAtIndex("note", 0).setSize(4).setClass(DivisionNote.class).setData(id, expect);
        // @formatter:on
        testChildren();
    }

    @Test
    void testDouble() {
        newDoc("%Note 1\n%=note 2");

        addStyleTest("%", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);
        addStyleTest("Note 1", CssLineStyles.NOTE, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NOTE, CssSpanStyles.OPERATOR);

        addStyleTest("%=", CssLineStyles.HEADER, CssSpanStyles.OPERATOR);
        addStyleTest("note 2", CssLineStyles.HEADER, CssSpanStyles.TEXT);
        testStyles();

        Span id = null;
        ImmutableMap<String, String> expect = ImmutableMap.of();

        // @formatter:off
        TestChild head= newChildAtIndex("section", 0).setSize(2).setClass(DivisionSecChapter.class);
        head.newChildAtIndex("note 1", 0).setSize(1).setClass(DivisionNote.class).setData(id, expect);
        head.newChildAtIndex("note 2", 1).setSize(1).setClass(DivisionNote.class).setData(id, expect);
        // @formatter:on
        testChildren();
    }

    @Override
    protected Executable testChild(int index, Object expect, DivisionNote child) {
        if (index == 0)
            return expect == null ? () -> Assertions.assertTrue(child.getPointer().isEmpty(), "id") : () -> {
                Assertions.assertTrue(child.getPointer().isPresent(), "id");
                Assertions.assertSame(expect, child.getPointer().get(), "id");
            };
        return () -> Assertions.assertEquals(expect, child.getFields());
    }
}
