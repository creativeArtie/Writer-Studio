package com.creativeartie.humming.document;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.*;

import com.google.common.base.*;
import com.google.common.collect.*;

class IdentitySpanTest extends SpanBranchTestBase {
    public static IdentitySpan createIdPointer(String text) {
        return IdentitySpan.newPointerId(newParent(), text, IdentitySpan.IdGroup.FOOTNOTE);
    }

    public static IdentitySpan createIdAddress(String text) {
        return IdentitySpan.newAddressId(newParent(), text, IdentitySpan.IdGroup.FOOTNOTE);
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
        addStyleTest("no cat", StyleClasses.ID, StyleClasses.TEXT);
        testStyles(id);
        testId("no cat", id);
    }

    @Test
    void testWithEscape() {
        IdentitySpan id = createIdPointer("cat\\:egory:id");
        addStyleTest("cat", StyleClasses.ID, StyleClasses.TEXT);
        addStyleTest("\\:", StyleClasses.ID, StyleClasses.ESCAPE);
        addStyleTest("egory", StyleClasses.ID, StyleClasses.TEXT);
        addStyleTest(":", StyleClasses.ID, StyleClasses.OPERATOR);
        addStyleTest("id", StyleClasses.ID, StyleClasses.TEXT);
        testStyles(id);
        testId("id", id, "cat:egory");
    }

    @Test
    void testMultiCat() {
        IdentitySpan id = createIdPointer("cat1:cat2:cat3:id");
        addStyleTest("cat1", StyleClasses.ID, StyleClasses.TEXT);
        addStyleTest(":", StyleClasses.ID, StyleClasses.OPERATOR);
        addStyleTest("cat2", StyleClasses.ID, StyleClasses.TEXT);
        addStyleTest(":", StyleClasses.ID, StyleClasses.OPERATOR);
        addStyleTest("cat3", StyleClasses.ID, StyleClasses.TEXT);
        addStyleTest(":", StyleClasses.ID, StyleClasses.OPERATOR);
        addStyleTest("id", StyleClasses.ID, StyleClasses.TEXT);
        testStyles(id);
        testId("id", id, "cat1", "cat2", "cat3");
    }

    @Test
    void testOnlyPointer() {
        IdentitySpan id = createIdPointer("error");
        getDocument().addChild(id);
        getDocument().runCleanup();
        addStyleTest("error", StyleClasses.ID, StyleClasses.ERROR, StyleClasses.TEXT);
        testStyles(id);
    }

    @Test
    void testLaterPointer() {
        IdentitySpan id = createIdPointer("later");
        getDocument().addChild(id);
        IdentitySpan address = createIdAddress("later");
        getDocument().addChild(address);
        getDocument().runCleanup();
        addStyleTest("later", StyleClasses.ID, StyleClasses.TEXT);

        testStyles(id);
        testStyles(address);
    }
}
