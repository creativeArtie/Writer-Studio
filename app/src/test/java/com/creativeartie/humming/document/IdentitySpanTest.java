package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import com.google.common.base.*;
import com.google.common.collect.*;

class IdentitySpanTest extends SpanBranchTestBase<IdentitySpan> {
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
        addStyleTest("no cat", SpanStyles.ID, SpanStyles.TEXT);
        testStyles(id);
        testId("no cat", id);
    }

    @Test
    void testSpaces() {
        IdentitySpan id = createIdPointer("   no    cat   ");
        addStyleTest("   no    cat   ", SpanStyles.ID, SpanStyles.TEXT);
        testStyles(id);
        testId("no cat", id);
    }

    @Test
    void testWithEscape() {
        IdentitySpan id = createIdPointer("cat\\:egory:id");
        addStyleTest("cat", SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest("\\:", SpanStyles.ID, SpanStyles.ESCAPE);
        addStyleTest("egory", SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest(":", SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.ID, SpanStyles.TEXT);
        testStyles(id);
        testId("id", id, "cat:egory");
    }

    @Test
    void testMultiCat() {
        IdentitySpan id = createIdPointer("cat1:cat2:cat3:id");
        addStyleTest("cat1", SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest(":", SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("cat2", SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest(":", SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("cat3", SpanStyles.ID, SpanStyles.TEXT);
        addStyleTest(":", SpanStyles.ID, SpanStyles.OPERATOR);
        addStyleTest("id", SpanStyles.ID, SpanStyles.TEXT);
        testStyles(id);
        testId("id", id, "cat1", "cat2", "cat3");
    }

    @Test
    void testOnlyPointer() {
        IdentitySpan id = createIdPointer("error");
        getDocument().add(id);
        getDocument().cleanUp();
        addStyleTest("error", SpanStyles.ID, SpanStyles.ERROR, SpanStyles.TEXT);
        testStyles(id);
    }

    @Test
    void testLaterPointer() {
        IdentitySpan id = createIdPointer("later");
        getDocument().add(id);
        IdentitySpan address = createIdAddress("later");
        getDocument().add(address);
        getDocument().cleanUp();
        addStyleTest("later", SpanStyles.ID, SpanStyles.TEXT);

        testStyles(id);
        testStyles(address);
    }
}
