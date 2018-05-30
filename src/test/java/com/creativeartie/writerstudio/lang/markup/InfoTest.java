package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchFormatAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchInfoDataAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class InfoTest {

    /// TODO Test data edited
    private final SetupParser[] FIELD_PARSER = InfoFieldParser.values();

    @Test
    public void dataFormatted(){
        ///           01234 5678901
        String raw = "abcd*\\*efg*";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            InfoDataParser.FORMATTED);

        FormatDataAssert data = new FormatDataAssert(doc)
            .setData(0, 0);
        FormattedSpanAssert format = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0);
        FormatContentAssert text1 = new FormatContentAssert(doc)
            .setBoth("abcd")   .setBegin(false)
            .setEnd(false);
        FormatContentAssert text2 = new FormatContentAssert(doc)
            .setBoth("*efg")   .setBegin(false)
            .setEnd(false)     .setFormats(FormatTypeStyle.ITALICS);
        EscapeAssert escape = new EscapeAssert(doc).setEscape("*");

        data.test(1,          raw,      0);
        format.test(4,        raw,      0, 0);
        text1.test(1,         "abcd",   0, 0, 0);
        doc.assertData(0,  4, "abcd",   0, 0, 0, 0);
        doc.assertKey( 4,  5, "*",      0, 0, 1);
        text2.test(2,         "\\*efg", 0, 0, 2);
        escape.test(2,        "\\*",    0, 0, 2, 0);
        doc.assertKey( 5,  6, "\\",     0, 0, 2, 0, 0);
        doc.assertData(6,  7, "*",      0, 0, 2, 0, 1);
        doc.assertData(7, 10, "efg",    0, 0, 2, 1);
        doc.assertKey(10, 11, "*",      0, 0, 3);

        doc.assertRest();
    }

    @Test
    public void dataText(){
        ///           0 1234
        String raw = "*\\*a";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            InfoDataParser.TEXT);
        ContentTextAssert data = new ContentTextAssert(doc)
            .setData(0, 0);
        ContentAssert text = new ContentAssert(doc)
            .setBoth("**a").setBegin(false)
            .setEnd(false) .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc)
            .setEscape("*");

        data.test(1,         raw,     0);
        text.test(3,         "*\\*a", 0, 0);
        doc.assertData(0, 1, "*",     0, 0, 0);
        escape.test(2,       "\\*",   0, 0, 1);
        doc.assertKey( 1, 2, "\\",    0, 0, 1, 0);
        doc.assertData(2, 3, "*",     0, 0, 1, 1);
        doc.assertData(3, 4, "a",     0, 0, 2);

        doc.assertRest();
    }

    @Test
    public void fieldSource(){
        ///           0123456
        String raw = "source";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, FIELD_PARSER);

        FieldAssert field = new FieldAssert(doc).setType(InfoFieldType.SOURCE);

        field.test(1,         raw, 0);
        doc.assertField(0, 6, raw, 0, 0);

        doc.assertRest();
    }

    @Test
    public void fieldInText(){
        ///           01234567
        String raw = "in-text";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, FIELD_PARSER);

        FieldAssert field = new FieldAssert(doc).setType(InfoFieldType.IN_TEXT);

        field.test(1,         raw, 0);
        doc.assertField(0, 7, raw, 0, 0);

        doc.assertRest();
    }

    @Test
    public void fieldFootnote(){
        ///           012345678
        String raw = "footnote";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, FIELD_PARSER);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.FOOTNOTE);

        field.test(1,         raw, 0);
        doc.assertField(0, 8, raw, 0, 0);

        doc.assertRest();
    }

    @Test
    public void fieldError(){
        ///           012345
        String raw = "error";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, FIELD_PARSER);

        FieldAssert field = new FieldAssert(doc).setType(InfoFieldType.ERROR);

        field.test(1,         raw, 0);
        doc.assertField(0, 5, raw, 0, 0);
    }

    @Test
    public void fieldErrorRandom(){
        ///           012345
        String raw = "p-ges";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,FIELD_PARSER);

        FieldAssert field = new FieldAssert(doc).setType(InfoFieldType.ERROR);

        field.test(1,         raw, 0);
        doc.assertField(0, 5, raw, 0, 0);

        doc.assertRest();
    }
}
