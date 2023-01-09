package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class TodoSpanTest extends SpanBranchTestBase {
    @Test
    void testFull() {
        TodoSpan span = TodoSpan.newSpan(newParent(), "{!todo}");
        addStyleTest("{!", SpanStyles.TODO, SpanStyles.OPERATOR);
        addStyleTest("todo", SpanStyles.TODO, SpanStyles.TEXT);
        addStyleTest("}", SpanStyles.TODO, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("todo", span.getAgenda());
    }

    @Test
    void testNoEnd() {
        TodoSpan span = TodoSpan.newSpan(newParent(), "{!todo");
        addStyleTest("{!", SpanStyles.TODO, SpanStyles.OPERATOR);
        addStyleTest("todo", SpanStyles.TODO, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertEquals("todo", span.getAgenda());
    }

    @Test
    void testNoText() {
        TodoSpan span = TodoSpan.newSpan(newParent(), "{!}");
        addStyleTest("{!", SpanStyles.TODO, SpanStyles.OPERATOR);
        addStyleTest("}", SpanStyles.TODO, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("", span.getAgenda());
    }

    @Test
    void testStartOnly() {
        TodoSpan span = TodoSpan.newSpan(newParent(), "{!");
        addStyleTest("{!", SpanStyles.TODO, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("", span.getAgenda());
    }
}
