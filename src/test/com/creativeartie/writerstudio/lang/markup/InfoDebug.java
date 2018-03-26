package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
import static com.creativeartie.writerstudio.lang.markup.BranchTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchDataTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;

import com.creativeartie.writerstudio.lang.*;

@RunWith(JUnit4.class)
public class InfoDebug {

    /// TODO Test data edited

    @Test
    public void dataFormatted(){
        ///           01234 5678901
        String raw = "abcd*\\*efg*";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            InfoDataParser.FORMATTED);

        FormatDataTest data = new FormatDataTest()
            .setData(doc, 0, 0);
        FormatMainTest format = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        FormatContentTest text1 = new FormatContentTest()
            .setText("abcd")   .setBegin(false)
            .setEnd(false);
        FormatContentTest text2 = new FormatContentTest()
            .setText("*efg")   .setBegin(false)
            .setEnd(false)     .setFormats(FormatType.ITALICS);
        EscapeTest escape = new BranchTest.EscapeTest().setEscape("*");

        data.test(       doc,  1, raw,      0);
        format.test(     doc,  4, raw,      0, 0);
        text1.test(      doc,  1, "abcd",   0, 0, 0);
        doc.assertDataLeaf(0,  4, "abcd",   0, 0, 0, 0);
        doc.assertKeyLeaf( 4,  5, "*",      0, 0, 1);
        text2.test(      doc,  2, "\\*efg", 0, 0, 2);
        escape.test(     doc,  2, "\\*",    0, 0, 2, 0);
        doc.assertKeyLeaf( 5,  6, "\\",     0, 0, 2, 0, 0);
        doc.assertDataLeaf(6,  7, "*",      0, 0, 2, 0, 1);
        doc.assertDataLeaf(7, 10, "efg",    0, 0, 2, 1);
        doc.assertKeyLeaf(10, 11, "*",      0, 0, 3);

        doc.assertIds();
    }

    @Test
    public void dataText(){
        ///           0 1234
        String raw = "*\\*a";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            InfoDataParser.TEXT);
        ContentDataTest data = new ContentDataTest()
            .setData(doc, 0, 0);
        ContentTest text = new ContentTest()
            .setText("**a").setBegin(false)
            .setEnd(false) .setCount(1);
        EscapeTest escape = new EscapeTest()
            .setEscape("*");

        data.test(       doc, 1, raw,      0);
        text.test(       doc, 3, "*\\*a",  0, 0);
        doc.assertDataLeaf(0, 1, "*",  0, 0, 0);
        escape.test(     doc, 2, "\\*",    0, 0, 1);
        doc.assertKeyLeaf( 1, 2, "\\", 0, 0, 1, 0);
        doc.assertDataLeaf(2, 3, "*",  0, 0, 1, 1);
        doc.assertDataLeaf(3, 4, "a",  0, 0, 2);

        doc.assertIds();
    }

    @Test
    public void fieldSource(){
        ///           0123456
        String raw = "source";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            InfoFieldParser.getParsers());

        FieldTest field = new FieldTest().setType(InfoFieldType.SOURCE);

        field.test(       doc, 1, raw, 0);
        doc.assertFieldLeaf(0, 6, raw, 0, 0);

        doc.assertIds();
    }

    @Test
    public void fieldInText(){
        ///           01234567
        String raw = "in-text";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            InfoFieldParser.getParsers());

        FieldTest field = new FieldTest().setType(InfoFieldType.IN_TEXT);

        field.test(       doc, 1, raw, 0);
        doc.assertFieldLeaf(0, 7, raw, 0, 0);

        doc.assertIds();
    }

    @Test
    public void fieldFootnote(){
        ///           012345678
        String raw = "footnote";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            InfoFieldParser.getParsers());
        FieldTest field = new FieldTest()
            .setType(InfoFieldType.FOOTNOTE);

        field.test(       doc, 1, raw, 0);
        doc.assertFieldLeaf(0, 8, raw, 0, 0);

        doc.assertIds();
    }

    @Test
    public void fieldError(){
        ///           012345
        String raw = "error";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            InfoFieldParser.getParsers());

        FieldTest field = new FieldTest().setType(InfoFieldType.ERROR);

        field.test(       doc, 1, raw, 0);
        doc.assertFieldLeaf(0, 5, raw, 0, 0);
    }

    @Test
    public void fieldErrorRandom(){
        ///           012345
        String raw = "p-ges";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            InfoFieldParser.getParsers());

        FieldTest field = new FieldTest().setType(InfoFieldType.ERROR);

        field.test(       doc, 1, raw, 0);
        doc.assertFieldLeaf(0, 5, raw, 0, 0);

        doc.assertIds();
    }
}