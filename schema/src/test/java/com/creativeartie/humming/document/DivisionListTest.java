package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

class DivisionListTest extends DivisionTestBase<DivisionList> {
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

    public DivisionListTest() {
        super(true, DivisionList.class);
    }

    @Test
    void testSimple() {
        newDoc("#item 1\n#item 2");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item 1", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item 2", StyleLines.NUMBERED, StylesSpans.TEXT);
        testStyles();

        newChildAtIndex("parent", 0).setChildrenSize(1).setClass(DivisionSection.class).newChildAtIndex("list", 0)
                .setChildrenSize(2).setClass(DivisionList.class).setData(false, 1, newPositions(1, 2));
        testChildren();
    }

    @Test
    void testWithSub() {
        newDoc("#item1\n#item2\n##subitem1\n##subitem2\n###sub sub");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item1", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item2", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("##", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("subitem1", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("##", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("subitem2", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("###", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("sub sub", StyleLines.NUMBERED, StylesSpans.TEXT);
        testStyles();
        TestChild heading = newChildAtIndex("base", 0).setClass(DivisionSection.class).setChildrenSize(1);
        TestChild list = heading.newChildAtIndex("list", 0).setClass(DivisionList.class).setChildrenSize(3)
                .setData(false, 1, newPositions(1, 2, 2));
        TestChild sub = list.newChildAtIndex("sub list", 2).setClass(DivisionList.class).setChildrenSize(3)
                .setData(false, 2, newPositions(1, 2, 2));

        sub.newChildAtIndex("sub sub", 2).setClass(DivisionList.class).setChildrenSize(1)
                .setData(false, 3, newPositions(1));
        testChildren();
    }

    @Test
    void testStartChild() {
        newDoc("##sub item");
        addStyleTest("##", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("sub item", StyleLines.NUMBERED, StylesSpans.TEXT);
        testStyles();

        TestChild heading = newChildAtIndex("base", 0).setClass(DivisionSection.class).setChildrenSize(1);
        TestChild list = heading.newChildAtIndex("list", 0).setClass(DivisionList.class).setChildrenSize(1)
                .setData(false, 1, newPositions(0));
        list.newChildAtIndex("sub list", 0).setClass(DivisionList.class).setChildrenSize(1)
                .setData(false, 2, newPositions(1));
        testChildren();
    }

    @Test
    void testSubInMiddle() {
        newDoc("#item 1\n##sub item 1\n#item 2");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item 1", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("##", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("sub item 1", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item 2", StyleLines.NUMBERED, StylesSpans.TEXT);
        testStyles();

        TestChild heading = newChildAtIndex("base", 0).setClass(DivisionSection.class).setChildrenSize(1);
        TestChild list = heading.newChildAtIndex("list", 0).setClass(DivisionList.class).setChildrenSize(3)
                .setData(false, 1, newPositions(1, 1, 2));
        list.newChildAtIndex("sub list", 1).setClass(DivisionList.class).setChildrenSize(1)
                .setData(false, 2, newPositions(1));
        testChildren();
    }

    @Test
    void testTypeChange() {
        newDoc("#item 1\n-item 2");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item 1", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("-", StyleLines.BULLET, StylesSpans.OPERATOR);
        addStyleTest("item 2", StyleLines.BULLET, StylesSpans.TEXT);
        testStyles();

        TestChild heading = newChildAtIndex("base", 0).setClass(DivisionSection.class).setChildrenSize(2);
        heading.newChildAtIndex("number", 0).setClass(DivisionList.class).setChildrenSize(1)
                .setData(false, 1, newPositions(1));
        heading.newChildAtIndex("bullet", 1).setClass(DivisionList.class).setChildrenSize(1)
                .setData(true, 1, newPositions(1));
        testChildren();
    }

    @Test
    void testWithTodos() {
        newDoc("#item 1\n!help\n#item 2");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item 1", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("!", StyleLines.AGENDA, StylesSpans.OPERATOR);
        addStyleTest("help", StyleLines.AGENDA, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.AGENDA, StylesSpans.OPERATOR);

        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item 2", StyleLines.NUMBERED, StylesSpans.TEXT);
        testStyles();

        newChildAtIndex("base", 0).setClass(DivisionSection.class).setChildrenSize(1).newChildAtIndex("list", 0)
                .setChildrenSize(3).setClass(DivisionList.class).setData(false, 1, newPositions(1, -1, 2));
        testChildren();
    }

    @Test
    void testWithHeading() {
        newDoc("#item 1\n=Heading 1");
        addStyleTest("#", StyleLines.NUMBERED, StylesSpans.OPERATOR);
        addStyleTest("item 1", StyleLines.NUMBERED, StylesSpans.TEXT);
        addStyleTest("\n", StyleLines.NUMBERED, StylesSpans.OPERATOR);

        addStyleTest("=", StyleLines.HEADING, StylesSpans.OPERATOR);
        addStyleTest("heading 1", StyleLines.HEADING, StylesSpans.TEXT);
        testStyles();

        newChildAtIndex("Ch 1", 0).setClass(DivisionSection.class).setChildrenSize(1).newChildAtIndex("list", 0)
                .setChildrenSize(1).setClass(DivisionList.class).setData(false, 1, newPositions(1));

        newChildAtIndex("Ch 2", 1).setClass(DivisionSection.class).setChildrenSize(1);
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
