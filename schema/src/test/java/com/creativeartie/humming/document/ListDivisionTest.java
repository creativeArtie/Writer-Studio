package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

class ListDivisionTest extends DivisionTestBase<ListDivision> {
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

    public ListDivisionTest() {
        super(true, ListDivision.class);
    }

    @Test
    void testSimple() {
        newDoc("#item 1\n#item 2");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item 1", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item 2", LineStyles.NUMBERED, SpanStyles.TEXT);
        testStyles();

        newChildAtIndex("parent", 0).setChildrenSize(1).setClass(SectionDivision.class).newChildAtIndex("list", 0)
                .setChildrenSize(2).setClass(ListDivision.class).setData(false, 1, newPositions(1, 2));
        testChildren();
    }

    @Test
    void testWithSub() {
        newDoc("#item1\n#item2\n##subitem1\n##subitem2\n###sub sub");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item1", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item2", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("##", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("subitem1", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("##", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("subitem2", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("###", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("sub sub", LineStyles.NUMBERED, SpanStyles.TEXT);
        testStyles();
        TestChild heading = newChildAtIndex("base", 0).setClass(SectionDivision.class).setChildrenSize(1);
        TestChild list = heading.newChildAtIndex("list", 0).setClass(ListDivision.class).setChildrenSize(3)
                .setData(false, 1, newPositions(1, 2, 2));
        TestChild sub = list.newChildAtIndex("sub list", 2).setClass(ListDivision.class).setChildrenSize(3)
                .setData(false, 2, newPositions(1, 2, 2));

        sub.newChildAtIndex("sub sub", 2).setClass(ListDivision.class).setChildrenSize(1)
                .setData(false, 3, newPositions(1));
        testChildren();
    }

    @Test
    void testStartChild() {
        newDoc("##sub item");
        addStyleTest("##", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("sub item", LineStyles.NUMBERED, SpanStyles.TEXT);
        testStyles();

        TestChild heading = newChildAtIndex("base", 0).setClass(SectionDivision.class).setChildrenSize(1);
        TestChild list = heading.newChildAtIndex("list", 0).setClass(ListDivision.class).setChildrenSize(1)
                .setData(false, 1, newPositions(0));
        list.newChildAtIndex("sub list", 0).setClass(ListDivision.class).setChildrenSize(1)
                .setData(false, 2, newPositions(1));
        testChildren();
    }

    @Test
    void testSubInMiddle() {
        newDoc("#item 1\n##sub item 1\n#item 2");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item 1", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("##", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("sub item 1", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item 2", LineStyles.NUMBERED, SpanStyles.TEXT);
        testStyles();

        TestChild heading = newChildAtIndex("base", 0).setClass(SectionDivision.class).setChildrenSize(1);
        TestChild list = heading.newChildAtIndex("list", 0).setClass(ListDivision.class).setChildrenSize(3)
                .setData(false, 1, newPositions(1, 1, 2));
        list.newChildAtIndex("sub list", 1).setClass(ListDivision.class).setChildrenSize(1)
                .setData(false, 2, newPositions(1));
        testChildren();
    }

    @Test
    void testTypeChange() {
        newDoc("#item 1\n-item 2");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item 1", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("-", LineStyles.BULLET, SpanStyles.OPERATOR);
        addStyleTest("item 2", LineStyles.BULLET, SpanStyles.TEXT);
        testStyles();

        TestChild heading = newChildAtIndex("base", 0).setClass(SectionDivision.class).setChildrenSize(2);
        heading.newChildAtIndex("number", 0).setClass(ListDivision.class).setChildrenSize(1)
                .setData(false, 1, newPositions(1));
        heading.newChildAtIndex("bullet", 1).setClass(ListDivision.class).setChildrenSize(1)
                .setData(true, 1, newPositions(1));
        testChildren();
    }

    @Test
    void testWithTodos() {
        newDoc("#item 1\n!help\n#item 2");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item 1", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("!", LineStyles.AGENDA, SpanStyles.OPERATOR);
        addStyleTest("help", LineStyles.AGENDA, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.AGENDA, SpanStyles.OPERATOR);

        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item 2", LineStyles.NUMBERED, SpanStyles.TEXT);
        testStyles();

        newChildAtIndex("base", 0).setClass(SectionDivision.class).setChildrenSize(1).newChildAtIndex("list", 0)
                .setChildrenSize(3).setClass(ListDivision.class).setData(false, 1, newPositions(1, -1, 2));
        testChildren();
    }

    @Test
    void testWithHeading() {
        newDoc("#item 1\n=Heading 1");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item 1", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("=", LineStyles.HEADING, SpanStyles.OPERATOR);
        addStyleTest("heading 1", LineStyles.HEADING, SpanStyles.TEXT);
        testStyles();

        newChildAtIndex("Ch 1", 0).setClass(SectionDivision.class).setChildrenSize(1).newChildAtIndex("list", 0)
                .setChildrenSize(1).setClass(ListDivision.class).setData(false, 1, newPositions(1));

        newChildAtIndex("Ch 2", 1).setClass(SectionDivision.class).setChildrenSize(1);
        testChildren();
    }

    @Override
    protected Executable testChild(int index, Object expect, ListDivision test) {

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
                    Assertions.assertInstanceOf(AgendaLine.class, child, message);
                } else {
                    Assertions.assertInstanceOf(ListSpan.class, child, message);
                    Assertions.assertEquals(post, ((ListSpan) child).getPosition(), message);
                }
            };
            i++;
        }
        return () -> Assertions.assertAll(tests);
    }
}
