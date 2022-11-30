package com.creativeartie.writer.writing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class RefPhraseTest {

    private String optStyles[] = { "", SpanStyles.OPERATOR.getStyle() },
        idNameStyles[] = { "", SpanStyles.ID.getStyle(), SpanStyles.NAME
            .getStyle() }, idOptStyles[] = { "", SpanStyles.OPERATOR
                .getStyle() };

    private DocBuilder builder;

    @BeforeEach
    public void setup() {
        builder = new DocBuilder(true);
        optStyles[0] = "";
        idNameStyles[0] = "";
        idOptStyles[0] = "";
    }

    @ParameterizedTest(name = "{displayName}: \"{1}\" ({0})")
    @CsvSource(
        { "FOOTNOTE, {^hello}", "ENDNOTE, {*hello}", "SOURCE, {>hello}" }
    )
    @DisplayName("Complete Note Test")
    void testNotes(String type, String raw) {
        IdTypes group = IdTypes.valueOf(type);
        editStyle(group);

        final int lengths[] = { 2, 5, 1 };
        final String[][] styles = { optStyles, idNameStyles, idOptStyles };

        IdRefPhrase test = new IdRefPhrase(raw, builder);
        Assertions.assertAll(
            () -> Assertions.assertEquals("hello", test.getId().getId(), "Id"),
            () -> Assertions.assertEquals(group, test.getType(), "Type"),
            () -> new SpanStyleTester(builder, 3).addSpanLength(
                (idx) -> lengths[idx]
            ).addSpanStyle((idx) -> styles[idx]).assertStyles()

        );
    }

    @Test
    @DisplayName("No ending: {^hello")
    void testNoEnd() {
        IdTypes expectGroup = IdTypes.FOOTNOTE;
        editStyle(expectGroup);

        final int lengths[] = { 2, 5 };
        final String[][] styles = { optStyles, idNameStyles };
        IdRefPhrase test = new IdRefPhrase("{^hello", builder);
        Assertions.assertAll(
            () -> Assertions.assertEquals("hello", test.getId().getId(), "Id"),
            () -> Assertions.assertEquals(expectGroup, test.getType(), "Type"),
            () -> new SpanStyleTester(builder, 2).addSpanLength(
                (idx) -> lengths[idx]
            ).addSpanStyle((idx) -> styles[idx]).assertStyles()
        );
    }

    @Test
    @DisplayName("No id: {^}")
    void testNoId() {
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> new IdRefPhrase(
                "{^}", builder
            )
        );
    }

    @Test
    @DisplayName("No note type: {Hello}")
    void testNoStart() {
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> new IdRefPhrase(
                "{Hello}", builder
            )
        );
    }

    protected void editStyle(IdTypes group) {
        optStyles[0] = group.toTypedStyles().getStyle();
        idNameStyles[0] = group.toTypedStyles().getStyle();
        idOptStyles[0] = group.toTypedStyles().getStyle();
    }

}
