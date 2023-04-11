package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import com.google.common.base.*;
import com.google.common.collect.*;

@SuppressWarnings("nls")
final class IdentitySpanTest extends SpanBranchTestBase<IdentitySpan> {
    private boolean isAddress;

    public IdentitySpan createIdPointer(String text) {
        isAddress = false;
        return newSpan(text);
    }

    public IdentitySpan createIdAddress(String text) {
        isAddress = true;
        return newSpan(text);
    }

    @Override
    protected IdentitySpan initSpan(SpanBranch parent, String input) {
        if (isAddress) {
            return IdentitySpan.newAddressId(parent, input, IdentityGroup.FOOTNOTE);
        }
        return IdentitySpan.newPointerId(parent, input, IdentityGroup.FOOTNOTE);
    }

    public static void testId(String name, IdentitySpan test, String... categories) {
        String fullName =
                Joiner.on(":").join(ImmutableList.builder().addAll(Arrays.asList(categories)).add(name).build());
        Assertions.assertAll(
                () -> assertArrayEquals(categories, test.getCategories().toArray(new String[0]), "Categories"),
                () -> assertEquals(name, test.getId(), "Id"), () -> assertEquals(fullName, test.getFullId(), "Full id")
        );
    }

    @Test
    void testNoCat() {
        IdentitySpan id = createIdPointer("no cat");
        addStyleTest("no cat", CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(id);
        testId("no cat", id);
    }

    @Test
    void testSpaces() {
        IdentitySpan id = createIdPointer("   no    cat   ");
        addStyleTest("   no    cat   ", CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(id);
        testId("no cat", id);
    }

    @Test
    void testWithEscape() {
        IdentitySpan id = createIdPointer("cat\\:egory:id");
        addStyleTest("cat", CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest("\\:", CssSpanStyles.ID, CssSpanStyles.ESCAPE);
        addStyleTest("egory", CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest(":", CssSpanStyles.ID, CssSpanStyles.OPERATOR);
        addStyleTest("id", CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(id);
        testId("id", id, "cat:egory");
    }

    @Test
    void testMultiCat() {
        IdentitySpan id = createIdPointer("cat1:cat2:cat3:id");
        addStyleTest("cat1", CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest(":", CssSpanStyles.ID, CssSpanStyles.OPERATOR);
        addStyleTest("cat2", CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest(":", CssSpanStyles.ID, CssSpanStyles.OPERATOR);
        addStyleTest("cat3", CssSpanStyles.ID, CssSpanStyles.TEXT);
        addStyleTest(":", CssSpanStyles.ID, CssSpanStyles.OPERATOR);
        addStyleTest("id", CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(id);
        testId("id", id, "cat1", "cat2", "cat3");
    }

    @Test
    void testOnlyPointer() {
        getDocument().updateText("{^error}");
        addStyleTest(
                "error", CssLineStyles.NORMAL, CssSpanStyles.FOOTNOTE, CssSpanStyles.ID, CssSpanStyles.ERROR,
                CssSpanStyles.TEXT
        );
        SpanBranch division = getDocument().get(0);
        SpanBranch normal = (SpanBranch) division.get(0);
        SpanBranch text = (SpanBranch) normal.get(0);
        IdentityReference ref = (IdentityReference) text.get(0);
        testStyles(ref.getPointer().get());
    }

    @Test
    void testLaterPointer() {
        getDocument().updateText("{^later}\n!^later=data");

        SpanBranch division = getDocument().get(0);
        SpanBranch normal = (SpanBranch) division.get(0);
        SpanBranch text = (SpanBranch) normal.get(0);
        IdentityReference ref = (IdentityReference) text.get(0);

        ParaReference footnote = (ParaReference) division.get(1);

        addStyleTest("ptrid", CssLineStyles.NORMAL, CssSpanStyles.FOOTNOTE, CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(ref.getPointer().get());

        refreshLists();
        addStyleTest("refid", CssLineStyles.FOOTNOTE, CssSpanStyles.ID, CssSpanStyles.TEXT);
        testStyles(footnote.getPointer().get());
    }
}
