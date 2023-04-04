package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

final class IdentityTodoTest extends SpanBranchTestBase<IdentityTodo> {
    @Test
    void testFull() {
        IdentityTodo span = newSpan("{!todo}");
        addStyleTest("{!", CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        addStyleTest("todo", CssSpanStyles.AGENDA, CssSpanStyles.TEXT);
        addStyleTest("}", CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("todo", span.getAgenda());
    }

    @Test
    void testNoEnd() {
        IdentityTodo span = newSpan("{!todo");
        addStyleTest("{!", CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        addStyleTest("todo", CssSpanStyles.AGENDA, CssSpanStyles.TEXT);
        testStyles(span);
        Assertions.assertEquals("todo", span.getAgenda());
    }

    @Test
    void testNoText() {
        IdentityTodo span = newSpan("{!}");
        addStyleTest("{!", CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        addStyleTest("}", CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("", span.getAgenda());
    }

    @Test
    void testStartOnly() {
        IdentityTodo span = newSpan("{!");
        addStyleTest("{!", CssSpanStyles.AGENDA, CssSpanStyles.OPERATOR);
        testStyles(span);
        Assertions.assertEquals("", span.getAgenda());
    }

    @Override
    protected IdentityTodo initSpan(SpanBranch parent, String input) {
        return IdentityTodo.newSpan(parent, input);
    }
}
