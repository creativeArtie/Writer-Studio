package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

class IdentityTodoTest extends SpanBranchTestBase<IdentityTodo> {
    @Test
    void testFull() {
        IdentityTodo span = newSpan("{!todo}");
        addStyleTest("{!", StylesSpans.TODO, StylesSpans.OPERATOR);
        addStyleTest("todo", StylesSpans.TODO, StylesSpans.TEXT);
        addStyleTest("}", StylesSpans.TODO, StylesSpans.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("todo", span.getAgenda());
    }

    @Test
    void testNoEnd() {
        IdentityTodo span = newSpan("{!todo");
        addStyleTest("{!", StylesSpans.TODO, StylesSpans.OPERATOR);
        addStyleTest("todo", StylesSpans.TODO, StylesSpans.TEXT);
        testStyles(span);
        Assertions.assertEquals("todo", span.getAgenda());
    }

    @Test
    void testNoText() {
        IdentityTodo span = newSpan("{!}");
        addStyleTest("{!", StylesSpans.TODO, StylesSpans.OPERATOR);
        addStyleTest("}", StylesSpans.TODO, StylesSpans.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("", span.getAgenda());
    }

    @Test
    void testStartOnly() {
        IdentityTodo span = newSpan("{!");
        addStyleTest("{!", StylesSpans.TODO, StylesSpans.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("", span.getAgenda());
    }

    @Override
    protected IdentityTodo initSpan(SpanBranch parent, String input) {
        return IdentityTodo.newSpan(parent, input);
    }
}
