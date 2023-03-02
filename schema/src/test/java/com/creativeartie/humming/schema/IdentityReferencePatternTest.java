package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class IdentityReferencePatternTest extends PatternTestBase<IdentityReferencePattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(IdentityReferencePattern.matcher("{^ad}"));
    }

    @ParameterizedTest
    @CsvSource({ "^,FOOTREF", "*,ENDREF", ">,CITEREF", "%,METAREF", "+,IMAGE" })
    void testFullRef(String test, String type) {
        final String raw = "{" + test + "cat:id}";
        final IdentityReferencePattern pattern = IdentityReferencePattern.valueOf(type);

        final Matcher match = IdentityReferencePattern.matcher(raw);
        assertGroup("{", match, IdentityReferencePattern.START, 1);
        assertGroup(test, match, pattern, 2);
        assertGroup("cat:id", match, IdentityReferencePattern.ID, 3);
        assertGroup("}", match, IdentityReferencePattern.END, 4);
        assertEnd(match);
    }

    @Test
    void testNoEndRef() {
        final String raw = "{^cat:id";

        final Matcher match = IdentityReferencePattern.matcher(raw);
        assertGroup("{", match, IdentityReferencePattern.START, 1);
        assertGroup("^", match, IdentityReferencePattern.FOOTREF, 2);
        assertGroup("cat:id", match, IdentityReferencePattern.ID, 3);
        assertEnd(match);
    }

    @Test
    void testError() {
        final Matcher match = IdentityReferencePattern.matcher("{addd}");
        assertGroup("{", match, IdentityReferencePattern.START, 1);
        assertGroup("addd", match, IdentityReferencePattern.ERROR, 2);
        assertGroup("}", match, IdentityReferencePattern.END, 3);
        assertEnd(match);
    }

    @ParameterizedTest
    @CsvSource({ "{#avd},#avd,true", "{^^abc},^^abc,true", "{^,^,false", "{^},^,true", "{addd},addd,true" })
    void testError(String raw, String text, boolean hasEnd) {
        final Matcher match = IdentityReferencePattern.matcher(raw);
        assertGroup("{", match, IdentityReferencePattern.START, 1);
        assertGroup(text, match, IdentityReferencePattern.ERROR, 2);
        if (hasEnd) assertGroup("}", match, IdentityReferencePattern.END, 3);
        assertEnd(match);
    }
}
