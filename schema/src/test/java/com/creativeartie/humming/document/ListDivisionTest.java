package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

class ListDivisionTest extends DivisionTestBase<ListDivision> {
    private static class ListPositions extends ArrayList<Integer> {}

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

        TestChild section = addChild(0, SectionDivision.class, 1, "parent");
        section.newChild(0, ListDivision.class, 2, "list", false, 1, newPositions(1, 2));
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

        TestChild heading = addChild(0, SectionDivision.class, 1, "base");
        TestChild list = heading.newChild(0, ListDivision.class, 3, "list", false, 1, newPositions(1, 2, 2));
        TestChild sub = list.newChild(2, ListDivision.class, 3, "sub list", false, 2, newPositions(1, 2, 2));
        sub.newChild(2, ListDivision.class, 1, "sub sub", false, 3, newPositions(1));
        testChildren();
    }

    @Test
    void testStartChild() {
        newDoc("##sub item");
        addStyleTest("##", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("sub item", LineStyles.NUMBERED, SpanStyles.TEXT);
        testStyles();

        TestChild heading = addChild(0, SectionDivision.class, 1, "base");
        TestChild list = heading.newChild(0, ListDivision.class, 1, "list", false, 1, newPositions(0));
        list.newChild(0, ListDivision.class, 1, "sub list", false, 2, newPositions(1));
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
        TestChild heading = addChild(0, SectionDivision.class, 1, "base");
        TestChild list = heading.newChild(0, ListDivision.class, 3, "list", false, 1, newPositions(1, 1, 2));
        list.newChild(1, ListDivision.class, 1, "sub list", false, 2, newPositions(1));
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
        TestChild base = addChild(0, SectionDivision.class, 2, "base");
        base.newChild(0, ListDivision.class, 1, "number", false, 1, newPositions(1));
        base.newChild(1, ListDivision.class, 1, "bullet", true, 1, newPositions(1));
    }

    @Test
    void testWithTodos() {
        newDoc("#item 1\n!help");
        addStyleTest("#", LineStyles.NUMBERED, SpanStyles.OPERATOR);
        addStyleTest("item 1", LineStyles.NUMBERED, SpanStyles.TEXT);
        addStyleTest("\n", LineStyles.NUMBERED, SpanStyles.OPERATOR);

        addStyleTest("!", LineStyles.AGENDA, SpanStyles.OPERATOR);
        addStyleTest("help", LineStyles.AGENDA, SpanStyles.TEXT);
        testStyles();
        addChild(0, SectionDivision.class, 2, "base")
                .newChild(0, ListDivision.class, 2, "list", false, newPositions(1));
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
            int pos = i;
            tests[i] = () -> {
                Span child = test.get(pos);
                String message = "Child at " + Integer.toString(pos);
                if (child instanceof ListSpan) Assertions.assertEquals(post, ((ListSpan) child).getPosition(), message);
            };
            i++;
        }
        return () -> Assertions.assertAll(tests);
    }
}
