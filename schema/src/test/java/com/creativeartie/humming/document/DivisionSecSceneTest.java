package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

class DivisionSecSceneTest extends DivisionTestBase<DivisionSecScene> {
    private static Integer[] head = new Integer[] { 0 };

    public DivisionSecSceneTest() {
        super(true, DivisionSecScene.class);
    }

    @Test
    void testTwoOutline() {
        newDoc("!=heading\n!=heading 2.");

        addStyleTest("!", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("=", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("heading", StyleLines.OUTLINE, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.OUTLINE, StylesSpans.OPERATOR);

        addStyleTest("!", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("=", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("heading 2.", StyleLines.OUTLINE, StylesSpans.TEXT);
        testStyles();

        TestChild chapter = newChildAtIndex("Chapter", 0).setSize(2).setClass(DivisionSecChapter.class);
        chapter.newChildAtIndex("Scene 1", 0).setSize(1).setClass(DivisionSecScene.class).setData(1, "1", head);
        chapter.newChildAtIndex("Scene 2", 1).setSize(1).setClass(DivisionSecScene.class).setData(1, "2", head);
        testChildren();
    }

    @Test
    void testBasicOutline() {
        newDoc("!=heading\n#content\nabc");

        addStyleTest("!", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("=", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("heading", StyleLines.OUTLINE, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.OUTLINE, StylesSpans.OPERATOR);

        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("content", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("abc", StyleLines.NORMAL, StylesSpans.TEXT);
        testStyles();

        // @formatter:off
        newChildAtIndex("Chapter", 0).setSize(2).setClass(DivisionSecChapter.class)
            .newChildAtIndex("Scene", 0).setSize(2).setClass(DivisionSecScene.class).setData(1, "1", head)
            .newChildAtIndex("List", 1).setSize(1).setClass(DivisionList.class);
        // @formatter:on
        testChildren();
    }

    @Test
    void testSubOutline() {
        newDoc("!=heading\n!==child 1\n!=child");

        addStyleTest("!", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("=", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("heading", StyleLines.OUTLINE, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.OUTLINE, StylesSpans.OPERATOR);

        addStyleTest("!", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("==", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("child 1", StyleLines.OUTLINE, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.OUTLINE, StylesSpans.OPERATOR);

        addStyleTest("!", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("=", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("child", StyleLines.OUTLINE, StylesSpans.TEXT);
        testStyles();

        TestChild chapter = newChildAtIndex("Chapter", 0).setSize(2).setClass(DivisionSecChapter.class);
        chapter.newChildAtIndex("Scene 1", 0).setSize(2).setClass(DivisionSecScene.class).setData(1, "1", head)
                .newChildAtIndex("child", 1).setSize(1).setClass(DivisionSecScene.class).setData(2, "1:1", head);
        chapter.newChildAtIndex("Scene 2", 1).setSize(1).setClass(DivisionSecScene.class).setData(1, "2", head);

        testChildren();
    }

    @Test
    void testSplitOutline() {
        newDoc("!=heading\n=child 1\n!=child");

        addStyleTest("!", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("=", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("heading", StyleLines.OUTLINE, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.OUTLINE, StylesSpans.OPERATOR);

        addStyleTest("=", StyleLines.HEADING, StylesSpans.OPERATOR);
        addStyleTest("child 1", StyleLines.HEADING, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.HEADING, StylesSpans.OPERATOR);

        addStyleTest("!", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("=", StyleLines.OUTLINE, StylesSpans.OPERATOR);
        addStyleTest("child", StyleLines.OUTLINE, StylesSpans.TEXT);
        testStyles();

        Integer[] head2 = new Integer[] { 1 };

        // @formatter:off
        newChildAtIndex("Chapter 1", 0).setSize(1).setClass(DivisionSecChapter.class)
            .newChildAtIndex("Scene 1", 0).setSize(1).setClass(DivisionSecScene.class).setData(1, "1", head);
        newChildAtIndex("Chapter 2", 1).setSize(2).setClass(DivisionSecChapter.class)
            .newChildAtIndex("Scene 2", 1).setSize(1).setClass(DivisionSecScene.class).setData(1, "2", head2);
        // @formatter:on
    }

    @Override
    protected Executable testChild(int index, Object expect, DivisionSecScene child) {
        if (index == 0) return () -> Assertions.assertEquals(expect, child.getLevel(), "getLevel");
        else if (index == 1) return () -> Assertions.assertEquals(expect, child.getLocation(), "getLocation");
        Integer[] locate = (Integer[]) expect;
        Span find = getDocument();
        for (int idx : locate) {
            find = (find instanceof Document ? ((Document) find).get(idx) : ((SpanBranch) find).get(index));
        }
        Span chapter = find;
        return () -> Assertions.assertSame(chapter, child.getChapter());
    }
}
