package com.creativeartie.humming.schema;

import java.util.regex.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ReferencePatternTest extends PatternTestBase<ReferencePattern> {
    @BeforeAll
    static void displayPattern() throws Exception {
        splitPrintPattern(ReferencePattern.matcher("{^ad}"));
    }

    @ParameterizedTest
    @CsvSource({ "^,FOOTREF", "*,ENDREF", ">,CITEREF", "%,METAREF", "+,IMAGE" })
    void testFullRef(String test, String type) {
        final String raw = "{" + test + "cat:id}";
        final ReferencePattern pattern = ReferencePattern.valueOf(type);

        final Matcher match = ReferencePattern.matcher(raw);
        assertGroup("{", match, ReferencePattern.START, 1);
        assertGroup(test, match, pattern, 2);
        assertGroup("cat:id", match, ReferencePattern.ID, 3);
        assertGroup("}", match, ReferencePattern.END, 4);
        assertEnd(match);
    }

    @Test
    void testNoEndRef() {
        final String raw = "{^cat:id";

        final Matcher match = ReferencePattern.matcher(raw);
        assertGroup("{", match, ReferencePattern.START, 1);
        assertGroup("^", match, ReferencePattern.FOOTREF, 2);
        assertGroup("cat:id", match, ReferencePattern.ID, 3);
        assertEnd(match);
    }

    @Test
    void testError() {
        final Matcher match = ReferencePattern.matcher("{addd}");
        assertGroup("{", match, ReferencePattern.START, 1);
        assertGroup("addd", match, ReferencePattern.ERROR, 2);
        assertGroup("}", match, ReferencePattern.END, 3);
        assertEnd(match);
    }

    @ParameterizedTest
    @CsvSource({ "{#avd},#avd,true", "{^^abc},^^abc,true", "{^,^,false", "{^},^,true", "{addd},addd,true" })
    void testError(String raw, String text, boolean hasEnd) {
        final Matcher match = ReferencePattern.matcher(raw);
        assertGroup("{", match, ReferencePattern.START, 1);
        assertGroup(text, match, ReferencePattern.ERROR, 2);
        if (hasEnd) assertGroup("}", match, ReferencePattern.END, 3);
        assertEnd(match);
    }
}
