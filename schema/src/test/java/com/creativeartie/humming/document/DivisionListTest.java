package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

@SuppressWarnings("nls")
final class DivisionListTest extends DivisionTestBase<DivisionList> {
    private static class ListPositions extends ArrayList<Integer> {
        private static final long serialVersionUID = -2815430445917427332L;
    }

    private static ListPositions newPositions(int... positions) {
        ListPositions ans = new ListPositions();

        for (int pos : positions) {
            ans.add(pos);
        }
        return ans;
    }

    private DivisionListTest() {
        super(true, DivisionList.class);
    }

    @Test
    void testSimple() {
        newDoc("#item 1\n#item 2");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item 1", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item 2", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        testStyles();

        setCounter(4, 0);
        // @formatter:off
        newChildAtIndex("parent", 0).setSize(1).setClass(DivisionSecChapter.class).setCounter(4, 0)
            .newChildAtIndex("list", 0).setSize(2).setClass(DivisionList.class)
                .setData(false, 1, newPositions(1, 2)).setCounter(4, 0);
        // @formatter:on
        testChildren();
    }

    @Test
    void testWithSub() {
        newDoc("#item1\n#item2\n##subitem1\n##subitem2\n###sub sub");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item1", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item2", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("##", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("subitem1", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("##", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("subitem2", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("###", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("sub sub", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        testStyles();
        setCounter(6, 0);
        TestChild heading = newChildAtIndex("base", 0).setClass(DivisionSecChapter.class).setSize(1).setCounter(6, 0);
        TestChild list = heading.newChildAtIndex("list", 0).setClass(DivisionList.class).setSize(3)
                .setData(false, 1, newPositions(1, 2, 2)).setCounter(6, 0);
        TestChild sub = list.newChildAtIndex("sub list", 2).setClass(DivisionList.class).setSize(3)
                .setData(false, 2, newPositions(1, 2, 2)).setCounter(4, 0);

        sub.newChildAtIndex("sub sub", 2).setClass(DivisionList.class).setSize(1).setData(false, 3, newPositions(1))
                .setCounter(2, 0);
        testChildren();
    }

    @Test
    void testStartChild() {
        newDoc("##sub item");
        addStyleTest("##", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("sub item", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        testStyles();

        setCounter(2, 0);
        TestChild heading = newChildAtIndex("base", 0).setClass(DivisionSecChapter.class).setSize(1).setCounter(2, 0);
        TestChild list = heading.newChildAtIndex("list", 0).setClass(DivisionList.class).setSize(1)
                .setData(false, 1, newPositions(0)).setCounter(2, 0);
        list.newChildAtIndex("sub list", 0).setClass(DivisionList.class).setSize(1).setData(false, 2, newPositions(1))
                .setCounter(2, 0);
        testChildren();
    }

    @Test
    void testSubInMiddle() {
        newDoc("#item 1\n##sub item 1\n#item 2");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item 1", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("##", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("sub item 1", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item 2", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        testStyles();
        setCounter(7, 0);
        TestChild heading = newChildAtIndex("base", 0).setClass(DivisionSecChapter.class).setSize(1).setCounter(7, 0);
        TestChild list = heading.newChildAtIndex("list", 0).setClass(DivisionList.class).setSize(3)
                .setData(false, 1, newPositions(1, 1, 2)).setCounter(7, 0);
        list.newChildAtIndex("sub list", 1).setClass(DivisionList.class).setSize(1).setData(false, 2, newPositions(1))
                .setCounter(3, 0);
        testChildren();
    }

    @Test
    void testTypeChange() {
        newDoc("#item 1\n-item 2");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item 1", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("-", CssLineStyles.BULLET, CssSpanStyles.OPERATOR);
        addStyleTest("item 2", CssLineStyles.BULLET, CssSpanStyles.TEXT);
        testStyles();
        setCounter(4, 0);
        TestChild heading = newChildAtIndex("base", 0).setClass(DivisionSecChapter.class).setSize(2).setCounter(4, 0);
        heading.newChildAtIndex("number", 0).setClass(DivisionList.class).setSize(1).setData(false, 1, newPositions(1))
                .setCounter(2, 0);
        heading.newChildAtIndex("bullet", 1).setClass(DivisionList.class).setSize(1).setData(true, 1, newPositions(1))
                .setCounter(2, 0);
        testChildren();
    }

    private

    @Test void testWithTodos() {
        newDoc("#item 1\n!help\n#item 2");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item 1", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("!", CssLineStyles.AGENDA, CssSpanStyles.OPERATOR);
        addStyleTest("help", CssLineStyles.AGENDA, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.AGENDA, CssSpanStyles.OPERATOR);

        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item 2", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        testStyles();

        newChildAtIndex("base", 0).setClass(DivisionSecChapter.class).setSize(1).newChildAtIndex("list", 0).setSize(3)
                .setClass(DivisionList.class).setData(false, 1, newPositions(1, -1, 2));
        testChildren();
    }

    @Test
    void testWithHeading() {
        newDoc("#item 1\n=Heading 1");
        addStyleTest("#", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);
        addStyleTest("item 1", CssLineStyles.NUMBERED, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.NUMBERED, CssSpanStyles.OPERATOR);

        addStyleTest("=", CssLineStyles.HEADING, CssSpanStyles.OPERATOR);
        addStyleTest("heading 1", CssLineStyles.HEADING, CssSpanStyles.TEXT);
        testStyles();
        setCounter(4, 0);
        // @formatter: off
        newChildAtIndex("Ch 1", 0).setClass(DivisionSecChapter.class).setSize(1).setCounter(2, 0)
                .newChildAtIndex("list", 0).setSize(1).setClass(DivisionList.class).setData(false, 1, newPositions(1))
                .setCounter(2, 0);
        // @formatter: on

        newChildAtIndex("Ch 2", 1).setClass(DivisionSecChapter.class).setSize(1).setCounter(2, 0);
        testChildren();
    }

    @Override
    protected Executable testChild(int index, Object expect, DivisionList test) {

        switch (index) {
            case 0:
                return () -> Assertions.assertEquals(expect, test.isBullet(), "isBullet");
            case 1:
                return () -> Assertions.assertEquals(expect, test.getLevel(), "getListLevel");
        }
        ArrayList<Integer> positions = (ListPositions) expect;
        Executable[] tests = new Executable[positions.size()];

        int i = 0;

        for (int post : positions) {
            int idx = i;
            tests[i] = () -> {
                Span child = test.get(idx);
                String message = "Child at " + Integer.toString(idx);

                if (post == -1) {
                    Assertions.assertInstanceOf(ParaAgenda.class, child, message);
                } else {
                    Assertions.assertInstanceOf(SpanList.class, child, message);
                    Assertions.assertEquals(post, ((SpanList) child).getPosition(), message);
                }
            };
            i++;
        }
        return () -> Assertions.assertAll(tests);
    }
}
