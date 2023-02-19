package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

class ListDivisionTest extends DivisionTestBase<ListDivision> {
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
        section.newChild(0, ListDivision.class, 2, "list", false, 1, 2);
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
        int size = (Integer) expect;
        Executable[] tests = new Executable[size];
        for (int i = 0; i < size; i++) {
            int pos = i + 1;
            tests[i] = () -> {
                Span child = test.get(pos - 1);
                Assertions.assertInstanceOf(ListSpan.class, child);
                Assertions.assertEquals(pos, ((ListSpan) child).getPosition());
            };
        }
        return () -> Assertions.assertAll(tests);
    }
}
