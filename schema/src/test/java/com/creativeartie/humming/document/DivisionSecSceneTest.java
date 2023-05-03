package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

@SuppressWarnings("nls")
final class DivisionSecSceneTest extends DivisionTestBase<DivisionSecScene> {
    public DivisionSecSceneTest() {
        super(true, DivisionSecScene.class);
    }

    @Test
    void testTwoOutline() {
        newDoc("!=heading\n!=heading 2.");

        addStyleTest("!", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("=", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("heading", CssLineStyles.OUTLINE, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);

        addStyleTest("!", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("=", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("heading 2.", CssLineStyles.OUTLINE, CssSpanStyles.TEXT);
        testStyles();
        Span head = getSpan(0);
        setCounter(0, 3);
        TestChild chapter =
                newChildAtIndex("Chapter", 0).setSize(2).setClass(DivisionSecChapter.class).setCounter(0, 3);
        chapter.newChildAtIndex("Scene 1", 0).setSize(1).setClass(DivisionSecScene.class).setData(1, "1", head)
                .setCounter(0, 1);
        chapter.newChildAtIndex("Scene 2", 1).setSize(1).setClass(DivisionSecScene.class).setData(1, "2", head)
                .setCounter(0, 2);
        testChildren();
    }

    @Test
    void testBasicOutline() {
        newDoc("!=heading\n#content\nabc");

        addStyleTest("!", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("=", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("heading", CssLineStyles.OUTLINE, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);

        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("content", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("abc", CssLineStyles.NORMAL, CssSpanStyles.TEXT);
        testStyles();
        Span head = getSpan(0);

        setCounter(2, 1);
        // @formatter:off
        newChildAtIndex("Chapter", 0).setSize(1).setClass(DivisionSecChapter.class).setCounter(2, 1)
            .newChildAtIndex("Scene", 0).setSize(3).setClass(DivisionSecScene.class).setData(1, "1", head)
                .setCounter(2, 1)
            .newChildAtIndex("List", 1).setSize(1).setClass(DivisionList.class).setCounter(1, 0);
        // @formatter:on
        testChildren();
    }

    @Test
    void testSubOutline() {
        newDoc("!=heading\n!==child 1\n!=child");

        addStyleTest("!", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("=", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("heading", CssLineStyles.OUTLINE, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);

        addStyleTest("!", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("==", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("child 1", CssLineStyles.OUTLINE, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);

        addStyleTest("!", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("=", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("child", CssLineStyles.OUTLINE, CssSpanStyles.TEXT);
        testStyles();
        Span head = getSpan(0);

        setCounter(0, 4);
        TestChild chapter =
                newChildAtIndex("Chapter", 0).setSize(2).setClass(DivisionSecChapter.class).setCounter(0, 4);
        // @formatter:off
        chapter.newChildAtIndex("Scene 1", 0).setSize(2).setClass(DivisionSecScene.class).setData(1, "1", head)
                .setCounter(0, 3)
            .newChildAtIndex("child", 1).setSize(1).setClass(DivisionSecScene.class)
                .setData(2, "1:1", head).setCounter(0, 2);
        // @formatter:on
        chapter.newChildAtIndex("Scene 2", 1).setSize(1).setClass(DivisionSecScene.class).setData(1, "2", head)
                .setCounter(0, 1);

        testChildren();
    }

    @Test
    void testSplitOutline() {
        newDoc("!=heading\n=child 1\n!=child");

        addStyleTest("!", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("=", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("heading", CssLineStyles.OUTLINE, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);

        addStyleTest("=", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);
        addStyleTest("child 1", CssLineStyles.HEADING, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);

        addStyleTest("!", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("=", CssLineStyles.OUTLINE, CssSpanStyles.OPERATOR);
        addStyleTest("child", CssLineStyles.OUTLINE, CssSpanStyles.TEXT);
        testStyles();

        Span head = getSpan(0);
        Span head1 = getSpan(1);

        setCounter(2, 2);
        // @formatter:off
        newChildAtIndex("Chapter 1", 0).setSize(1).setClass(DivisionSecChapter.class).setCounter(0, 1)
            .newChildAtIndex("Scene 1", 0).setSize(1).setClass(DivisionSecScene.class).setData(1, "1", head)
                .setCounter(0, 1);
        newChildAtIndex("Chapter 2", 1).setSize(2).setClass(DivisionSecChapter.class).setCounter(2, 1)
            .newChildAtIndex("Scene 2", 1).setSize(1).setClass(DivisionSecScene.class).setData(1, "1", head1)
                .setCounter(0, 1);
        // @formatter:on
        testChildren();
    }

    @Override
    protected Executable testChild(int index, Object expect, DivisionSecScene child) {
        if (index == 0) return () -> Assertions.assertEquals(expect, child.getLevel(), "getLevel");
        else if (index == 1) return () -> Assertions.assertEquals(expect, child.getLocation(), "getLocation");
        return () -> Assertions.assertSame(expect, child.getChapter());
    }
}
