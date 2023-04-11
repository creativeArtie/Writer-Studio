package com.creativeartie.humming.document;

import org.junit.jupiter.api.*;

@SuppressWarnings("nls")
final class ParaNoteFieldTest extends SpanBranchTestBase<ParaNoteField> {
    @Test
    void testFull() {
        ParaNoteField span = newSpan("%>first_name=John\n");
        addStyleTest("%>", CssLineStyles.FIELD, CssSpanStyles.OPERATOR);
        addStyleTest("first_name", CssLineStyles.FIELD, CssSpanStyles.TEXT);
        addStyleTest("=", CssLineStyles.FIELD, CssSpanStyles.OPERATOR);
        addStyleTest("John", CssLineStyles.FIELD, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.FIELD, CssSpanStyles.OPERATOR);
        testStyles(span);
    }

    @Test
    void testNoEnd() {
        ParaNoteField span = newSpan("%>first_name=John");
        addStyleTest("%>", CssLineStyles.FIELD, CssSpanStyles.OPERATOR);
        addStyleTest("first_name", CssLineStyles.FIELD, CssSpanStyles.TEXT);
        addStyleTest("=", CssLineStyles.FIELD, CssSpanStyles.OPERATOR);
        addStyleTest("John", CssLineStyles.FIELD, CssSpanStyles.TEXT);
        testStyles(span);
    }

    @Test
    void testErrorFull() {
        ParaNoteField span = newSpan("%>first_name\\=John\n");
        addStyleTest("%>", CssLineStyles.FIELD, CssSpanStyles.ERROR, CssSpanStyles.OPERATOR);
        addStyleTest("first_name", CssLineStyles.FIELD, CssSpanStyles.ERROR, CssSpanStyles.TEXT);
        addStyleTest("\\=", CssLineStyles.FIELD, CssSpanStyles.ERROR, CssSpanStyles.ESCAPE);
        addStyleTest("John", CssLineStyles.FIELD, CssSpanStyles.ERROR, CssSpanStyles.TEXT);
        addStyleTest("\n", CssLineStyles.FIELD, CssSpanStyles.ERROR, CssSpanStyles.OPERATOR);
        testStyles(span);
    }

    @Override
    protected ParaNoteField initSpan(SpanBranch parent, String input) {
        Para span = Para.newLine(parent, input);
        Assertions.assertInstanceOf(ParaNoteField.class, span);
        return (ParaNoteField) span;
    }
}
