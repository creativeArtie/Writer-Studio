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
        addStyleTest("%=", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("Heading", StyleLines.HEADER, StylesSpans.TEXT);
        addStyleTest("#", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("id", StyleLines.HEADER, StylesSpans.ID, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.HEADER, StylesSpans.OPERATOR);

        addStyleTest("%", StyleLines.NOTE, StylesSpans.OPERATOR);
        addStyleTest("text", StyleLines.NOTE, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NOTE, StylesSpans.OPERATOR);

        addStyleTest("%>", StyleLines.FIELD, StylesSpans.OPERATOR);
        addStyleTest("abc", StyleLines.FIELD, StylesSpans.TEXT);
        addStyleTest("=", StyleLines.FIELD, StylesSpans.OPERATOR);
        addStyleTest("dec", StyleLines.FIELD, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.FIELD, StylesSpans.OPERATOR);

        addStyleTest("%>", StyleLines.FIELD, StylesSpans.ERROR, StylesSpans.OPERATOR);
        addStyleTest("afadf", StyleLines.FIELD, StylesSpans.ERROR, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.FIELD, StylesSpans.ERROR, StylesSpans.OPERATOR);

        addStyleTest("text", StyleLines.NORMAL, StylesSpans.TEXT);
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

        addStyleTest("%", StyleLines.NOTE, StylesSpans.OPERATOR);
        addStyleTest("Note 1", StyleLines.NOTE, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NOTE, StylesSpans.OPERATOR);

        addStyleTest("%=", StyleLines.HEADER, StylesSpans.OPERATOR);
        addStyleTest("note 2", StyleLines.HEADER, StylesSpans.TEXT);
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
