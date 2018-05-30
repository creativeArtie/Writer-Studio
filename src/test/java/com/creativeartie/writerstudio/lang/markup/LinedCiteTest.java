package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchInfoDataAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchLineAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
public class LinedCiteTest {

    private static final SetupParser[] parsers = new SetupParser[]{
            LinedParseRest.CITE};

    @Test
    public void basicInText(){
        String raw = "!>in-text:a";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(0, 3).setNote(1);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.IN_TEXT);
        ContentTextAssert data = new ContentTextAssert(doc)
            .setData(0, 3, 0);

        cite.test(4,           raw,       0);
        doc.assertKey(  0,  2, "!>",      0, 0);
        field.test(1,          "in-text", 0, 1);
        doc.assertField(2,  9, "in-text", 0, 1, 0);
        doc.assertKey(  9, 10, ":",       0, 2);
        data.test(1,           "a",       0, 3);
        doc.assertChild(1,     "a",       0, 3, 0);
        doc.assertData(10, 11, "a",       0, 3, 0, 0);
        doc.assertRest();
    }

    @Test
    public void inTextColonNewline(){
        String raw = "!>in-text:\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.IN_TEXT);

        cite.test(4,           raw,       0);
        doc.assertKey(  0,  2, "!>",      0, 0);
        field.test(1,          "in-text", 0, 1);
        doc.assertField(2,  9, "in-text", 0, 1, 0);
        doc.assertKey(  9, 10, ":",       0, 2);
        doc.assertKey( 10, 11, "\n",       0, 3);
        doc.assertRest();
    }

    @Test
    public void inTextNoColon(){
        String raw = "!>in-text";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.IN_TEXT);

        cite.test(2,          raw,       0);
        doc.assertKey(  0, 2, "!>",      0, 0);
        field.test(1,         "in-text", 0, 1);
        doc.assertField(2, 9, "in-text", 0, 1, 0);
        doc.assertRest();
    }

    @Test
    public void errorNoField(){
        String raw = "!>:a\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR);

        cite.test(4,         raw,  0);
        doc.assertKey( 0, 2, "!>", 0, 0);
        doc.assertKey( 2, 3, ":",  0, 1);
        doc.assertText(3, 4, "a",  0, 2);
        doc.assertKey( 4, 5, "\n", 0, 3);
        doc.assertRest();
    }

    @Test
    public void inTextNoColonData(){
        String raw = "!>in-texta";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(0, 2).setNote(1);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.IN_TEXT);
        ContentTextAssert data = new ContentTextAssert(doc)
            .setData(0, 2, 0);

        cite.test(3,           raw,       0);
        doc.assertKey(  0,  2, "!>",      0, 0);
        field.test(1,          "in-text", 0, 1);
        doc.assertField(2,  9, "in-text", 0, 1, 0);
        data.test(1,           "a",       0, 2);
        doc.assertChild(1,     "a",       0, 2, 0);
        doc.assertData( 9, 10, "a",       0, 2, 0, 0);
        doc.assertRest();
    }

    @Test
    public void errorWithUnparsed(){
        ///           01 23456
        String raw = "!>\nabc";
        DocumentAssert doc = assertDoc(2, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.ERROR);

        cite.test(2,         "!>\n", 0);
        doc.assertKey(0,  2, "!>",   0, 0);
        doc.assertKey(2,  3, "\n",   0, 1);
        doc.assertRest("abc");

    }

    @Test
    public void errorEmptyData(){
        String raw = "!>sdaf";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.ERROR);

        cite.test(2,           raw,    0);
        doc.assertKey(  0,  2, "!>",   0, 0);
        field.test(1,          "sdaf", 0, 1);
        doc.assertField(2,  6, "sdaf", 0, 1, 0);
        doc.assertRest();
    }

    @Test
    public void errorEmptyDataNewLine(){
        String raw = "!>sdaf\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.ERROR);

        doc.assertKey(  0, 2, "!>",   0, 0);
        field.test(1,         "sdaf", 0, 1);
        doc.assertField(2, 6, "sdaf", 0, 1, 0);
        doc.assertKey(  6, 7, "\n",   0, 2);
        doc.assertRest();
    }

    @Test
    public void errorEmptyDataWithColon(){
        String raw = "!>sdaf:\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.ERROR);

        doc.assertKey(  0, 2, "!>",   0, 0);
        field.test(1,         "sdaf", 0, 1);
        doc.assertField(2, 6, "sdaf", 0, 1, 0);
        doc.assertKey(  6, 7, ":",    0, 2);
        doc.assertKey(  7, 8, "\n",   0, 3);
        doc.assertRest();
    }

    @Test
    public void errorEmptyDataWithExraSpaces(){
        String raw = "!>sdaf:  \n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.ERROR);

        doc.assertKey(  0,  2, "!>",   0, 0);
        field.test(1,          "sdaf", 0, 1);
        doc.assertField(2,  6, "sdaf", 0, 1, 0);
        doc.assertKey(  6,  7, ":",    0, 2);
        doc.assertText( 7,  9, "  ",   0, 3);
        doc.assertKey(  9, 10, "\n",  0, 4);
        doc.assertRest();
    }

    @Test
    public void errorUsingError(){
        String raw = "!>error:text\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.ERROR);

        cite.test(5,           raw,     0);
        doc.assertKey(  0,  2, "!>",    0, 0);
        field.test(1,          "error", 0, 1);
        doc.assertField(2,  7, "error", 0, 1, 0);
        doc.assertKey(  7,  8, ":",     0, 2);
        doc.assertText( 8, 12, "text",  0, 3);
        doc.assertKey( 12, 13, "\n",    0, 4);
        doc.assertRest();
    }

    @Test
    public void basicFootnote(){
        String raw = "!>footnote:abc\\\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.FOOTNOTE)
            .setDataSpan(0, 3).setNote(1);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.FOOTNOTE);
        FormatDataAssert data = new FormatDataAssert(doc)
            .setData(0, 3, 0);

        cite.test(4,           raw,        0);
        doc.assertKey(  0,  2, "!>",       0, 0);
        field.test(1,          "footnote", 0, 1);
        doc.assertField(2, 10, "footnote", 0, 1, 0);
        doc.assertKey( 10, 11, ":",        0, 2);
        data.test(1,           "abc\\\n",  0, 3);
        doc.assertChild(1,     "abc\\\n",  0, 3, 0);
        doc.assertChild(2,     "abc\\\n",  0, 3, 0, 0);
        doc.assertData(11, 14, "abc",      0, 3, 0, 0, 0);
        doc.assertChild(2,     "\\\n",     0, 3, 0, 0, 1);
        doc.assertKey( 14, 15, "\\",       0, 3, 0, 0, 1, 0);
        doc.assertData(15, 16, "\n",       0, 3, 0, 0, 1, 1);
        doc.assertRest();
    }

    @Test
    public void basicSource(){
        String find = "Henry** Reads**";
        String raw = "!>source:" + find + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.SOURCE)
            .setDataSpan(0, 3).setNote(2);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.SOURCE);
        FormatDataAssert data = new FormatDataAssert(doc)
            .setData(0, 3, 0);

        cite.test(5,           raw,      0);
        doc.assertKey(  0,  2, "!>",     0, 0);
        field.test(1,          "source", 0, 1);
        doc.assertField(2,  8, "source", 0, 1, 0);
        doc.assertKey(  8,  9, ":",      0, 2);
        data.test(1,           find,     0, 3);
        doc.assertChild(4,     find,     0, 3, 0);
        doc.assertChild(1,     "Henry",  0, 3, 0, 0);
        doc.assertData( 9, 14, "Henry",  0, 3, 0, 0, 0);
        doc.assertKey( 14, 16, "**",     0, 3, 0, 1);
        doc.assertChild(1,     " Reads", 0, 3, 0, 2);
        doc.assertData(16, 22, " Reads", 0, 3, 0, 2, 0);
        doc.assertKey( 22, 24, "**",     0, 3, 0, 3);
        doc.assertKey( 24, 25, "\n",     0, 4);
        doc.assertRest();
    }

    @Test
    public void sourceWithNote(){
        String find = "{@abc}";
        String raw = "!>source:" + find + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.SOURCE)
            .setDataSpan(0, 3).setNote(1);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.SOURCE);
        FormatDataAssert data = new FormatDataAssert(doc)
            .setData(0, 3, 0);

        cite.test(1,           raw,      0);
        doc.assertKey(  0,  2, "!>",     0, 0);
        field.test(1,          "source", 0, 1);
        doc.assertField(2,  8, "source", 0, 1, 0);
        doc.assertKey(  8,  9, ":",      0, 2);
        data.test(1,           find,     0, 3);
        doc.assertChild(1,     find,     0, 3, 0);
        doc.assertData( 9, 15, find,     0, 3, 0, 0, 0);
        doc.assertKey( 15, 16, "\n",     0, 4);
        doc.assertRest();
    }

    @Test
    public void editAddField(){
        String before = "!>:abc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(2, "in-text", 0);
        editCommon(doc);
    }

    @Test
    public void editContent(){
        ///              0123456789012
        String before = "!>in-text:abec";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(12, 13, 0);
        ///             01234567890123
        String after = "!>in-text:abc";
        doc.assertDoc(1, after, parsers);
        editCommon(doc);
    }

    @Test
    public void editRemoveCite(){
        ///              0123456789012
        String before = "!>in-text:abec";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(0, 1);
        ///             01234567890123
        String after = ">in-text:abec";
        doc.assertDoc(1, after, parsers);

        doc.assertRest(after);
    }

    private void editCommon(DocumentAssert doc){
        ///             01234567890123
        String after = "!>in-text:abc";
        doc.assertDoc(1, after);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(0, 3).setNote(1);
        FieldAssert field = new FieldAssert(doc)
            .setType(InfoFieldType.IN_TEXT);
        ContentTextAssert data = new ContentTextAssert(doc)
            .setData(0, 3, 0);

        cite.test(4,           after,     0);
        doc.assertKey(  0,  2, "!>",      0, 0);
        field.test(1,          "in-text", 0, 1);
        doc.assertField(2,  9, "in-text", 0, 1, 0);
        doc.assertKey(  9, 10, ":",       0, 2);
        data.test(1,           "abc",     0, 3);
        doc.assertChild(1,     "abc",     0, 3, 0);
        doc.assertData(10, 13, "abc",     0, 3, 0, 0);
        doc.assertRest();
    }
}
