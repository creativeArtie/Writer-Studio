package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class TodoSpanTest extends SpanBranchTestBase<TodoSpan> {
    @Test
    void testFull() {
        TodoSpan span = newSpan("{!todo}");
        addStyleTest("{!", SpanStyles.TODO, SpanStyles.OPERATOR);
        addStyleTest("todo", SpanStyles.TODO, SpanStyles.TEXT);
        addStyleTest("}", SpanStyles.TODO, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("todo", span.getAgenda());
    }

    @Test
    void testNoEnd() {
        TodoSpan span = newSpan("{!todo");
        addStyleTest("{!", SpanStyles.TODO, SpanStyles.OPERATOR);
        addStyleTest("todo", SpanStyles.TODO, SpanStyles.TEXT);
        testStyles(span);
        Assertions.assertEquals("todo", span.getAgenda());
    }

    @Test
    void testNoText() {
        TodoSpan span = newSpan("{!}");
        addStyleTest("{!", SpanStyles.TODO, SpanStyles.OPERATOR);
        addStyleTest("}", SpanStyles.TODO, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("", span.getAgenda());
    }

    @Test
    void testStartOnly() {
        TodoSpan span = newSpan("{!");
        addStyleTest("{!", SpanStyles.TODO, SpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("", span.getAgenda());
    }

    @Override
    protected TodoSpan initSpan(SpanBranch parent, String input) {
        return TodoSpan.newSpan(parent, input);
    }
}
