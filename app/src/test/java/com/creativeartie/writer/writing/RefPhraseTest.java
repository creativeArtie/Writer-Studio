package com.creativeartie.writer.writing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class RefPhraseTest {

    private String optStyles[] = { "", TypedStyles.OPERATOR.getStyle() },
        idNameStyles[] =
            { "", TypedStyles.ID.getStyle(), TypedStyles.NAME.getStyle() },
        idOptStyles[] = { "", TypedStyles.OPERATOR.getStyle() };

    private DocBuilder builder;

    @BeforeEach
    public void setup() {
        builder = new DocBuilder();
        optStyles[0] = "";
        idNameStyles[0] = "";
        idOptStyles[0] = "";
    }

    @ParameterizedTest
    @CsvSource(
        { "FOOTNOTE, {^hello}", "ENDNOTE, {*hello}", "SOURCE, {>hello}" }
    )
    void testNotes(String type, String raw) {
        IdGroups group = IdGroups.valueOf(type);
        editStyle(group);

        final int lengths[] = { 2, 5, 1 };
        final String[][] styles = { optStyles, idNameStyles, idOptStyles };

        RefPhrase test = new RefPhrase(raw, builder);
        Assertions.assertAll(
            () -> Assertions.assertEquals("hello", test.getId().getId(), "Id"),
            () -> Assertions.assertEquals(group, test.getType(), "Type"),
            () -> CommonTests.assertSpanStyles(
                builder, 3, (idx) -> lengths[idx], (idx) -> styles[idx]
            )
        );
    }

    @Test
    void testNoEnd() {
        IdGroups expectGroup = IdGroups.FOOTNOTE;
        editStyle(expectGroup);

        final int lengths[] = { 2, 5 };
        final String[][] styles = { optStyles, idNameStyles };
        RefPhrase test = new RefPhrase("{^hello", builder);
        Assertions.assertAll(
            () -> Assertions.assertEquals("hello", test.getId().getId(), "Id"),
            () -> Assertions.assertEquals(expectGroup, test.getType(), "Type"),
            () -> CommonTests.assertSpanStyles(
                builder, 2, (idx) -> lengths[idx], (idx) -> styles[idx]
            )
        );

    }

    protected void editStyle(IdGroups group) {
        optStyles[0] = group.toTypedStyles().getStyle();
        idNameStyles[0] = group.toTypedStyles().getStyle();
        idOptStyles[0] = group.toTypedStyles().getStyle();
    }

}
