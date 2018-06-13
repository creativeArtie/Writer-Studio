package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchFormatAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchLineAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
public class LinedCiteTest {

    private static IDBuilder buildId(String name){
        return new IDBuilder().addCategory("note").setId(name);
    }

    private static final SetupParser[] parsers = LinedParseCite.values();

    @Test
    public void basicInText(){
        String raw = "!>in-text:a";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(0, 3).setNote(1)
            .setDataClass(ContentSpan.class);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("a").setCount(1);

        cite.test(4,           raw,       0);
        doc.assertKey(  0,  2, "!>",      0, 0);
        doc.assertField(2,  9, "in-text", 0, 1);
        doc.assertKey(  9, 10, ":",       0, 2);
        data.test(1,           "a",       0, 3);
        doc.assertData(10, 11, "a",       0, 3, 0);
        doc.assertRest();
    }

    @Test
    public void inTextColonNewline(){
        String raw = "!>in-text:\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT).setNote(0);

        cite.test(4,           raw,       0);
        doc.assertKey(  0,  2, "!>",      0, 0);
        doc.assertField(2,  9, "in-text", 0, 1);
        doc.assertKey(  9, 10, ":",       0, 2);
        doc.assertKey( 10, 11, "\n",      0, 3);
        doc.assertRest();
    }

    @Test
    public void inTextNoColon(){
        String raw = "!>in-text";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT).setNote(0);

        cite.test(2,          raw,       0);
        doc.assertKey(  0, 2, "!>",      0, 0);
        doc.assertField(2, 9, "in-text", 0, 1);
        doc.assertRest();
    }

    @Test
    public void errorNoField(){
        String raw = "!>:a\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR).setNote(0);

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
            .setDataSpan(0, 2).setNote(1)
            .setDataClass(ContentSpan.class);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("a").setCount(1);

        cite.test(3,           raw,       0);
        doc.assertKey(  0,  2, "!>",      0, 0);
        doc.assertField(2,  9, "in-text", 0, 1);
        data.test(1,           "a",       0, 2);
        doc.assertData( 9, 10, "a",       0, 2, 0);
        doc.assertRest();
    }

    @Test
    public void errorWithUnparsed(){
        ///           01 23456
        String raw = "!>\nabc";
        DocumentAssert doc = assertDoc(2, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR).setNote(0);

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
            .setInfoType(InfoFieldType.ERROR).setNote(0);

        cite.test(2,           raw,    0);
        doc.assertKey(  0,  2, "!>",   0, 0);
        doc.assertField(2,  6, "sdaf", 0, 1);
        doc.assertRest();
    }

    @Test
    public void errorEmptyDataNewLine(){
        String raw = "!>sdaf\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR);

        doc.assertKey(  0, 2, "!>",   0, 0);
        doc.assertField(2, 6, "sdaf", 0, 1);
        doc.assertKey(  6, 7, "\n",   0, 2);
        doc.assertRest();
    }

    @Test
    public void errorEmptyDataWithColon(){
        String raw = "!>sdaf:\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR);

        doc.assertKey(  0, 2, "!>",   0, 0);
        doc.assertField(2, 6, "sdaf", 0, 1);
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

        doc.assertKey(  0,  2, "!>",   0, 0);
        doc.assertField(2,  6, "sdaf", 0, 1);
        doc.assertKey(  6,  7, ":",    0, 2);
        doc.assertText( 7,  9, "  ",   0, 3);
        doc.assertKey(  9, 10, "\n",   0, 4);
        doc.assertRest();
    }

    @Test
    public void errorUsingError(){
        String raw = "!>error:text\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR).setNote(0);

        cite.test(5,           raw,     0);
        doc.assertKey(  0,  2, "!>",    0, 0);
        doc.assertField(2,  7, "error", 0, 1);
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
            .setDataSpan(0, 3).setNote(1)
            .setDataClass(FormattedSpan.class);
        FormattedSpanAssert data = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0)
            .setParsed("abc");

        cite.test(4,           raw,        0);
        doc.assertKey(  0,  2, "!>",       0, 0);
        doc.assertField(2, 10, "footnote", 0, 1);
        doc.assertKey( 10, 11, ":",        0, 2);
        data.test(1,           "abc\\\n",  0, 3);
        doc.assertChild(2,     "abc\\\n",  0, 3, 0);
        doc.assertData(11, 14, "abc",      0, 3, 0, 0);
        doc.assertChild(2,     "\\\n",     0, 3, 0, 1);
        doc.assertKey( 14, 15, "\\",       0, 3, 0, 1, 0);
        doc.assertData(15, 16, "\n",       0, 3, 0, 1, 1);
        doc.assertRest();
    }

    @Test
    public void basicSource(){
        String find = "Henry** Reads**";
        String raw = "!>source:" + find + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.SOURCE)
            .setDataSpan(0, 3).setNote(2)
            .setDataClass(FormattedSpan.class);
        FormattedSpanAssert data = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(0)
            .setParsed("Henry Reads");

        cite.test(5,           raw,      0);
        doc.assertKey(  0,  2, "!>",     0, 0);
        doc.assertField(2,  8, "source", 0, 1);
        doc.assertKey(  8,  9, ":",      0, 2);
        data.test(4,           find,     0, 3);
        doc.assertChild(1,     "Henry",  0, 3, 0);
        doc.assertData( 9, 14, "Henry",  0, 3, 0, 0);
        doc.assertKey( 14, 16, "**",     0, 3, 1);
        doc.assertChild(1,     " Reads", 0, 3, 2);
        doc.assertData(16, 22, " Reads", 0, 3, 2, 0);
        doc.assertKey( 22, 24, "**",     0, 3, 3);
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
            .setDataSpan(0, 3).setNote(1)
            .setDataClass(FormattedSpan.class);
        FormattedSpanAssert data = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0)
            .setParsed("{@abc}");

        cite.test(5,           raw,      0);
        doc.assertKey(  0,  2, "!>",     0, 0);
        doc.assertField(2,  8, "source", 0, 1);
        doc.assertKey(  8,  9, ":",      0, 2);
        data.test(1,           find,     0, 3); /// FormattedSpan
        doc.assertChild(1,     find,     0, 3, 0); /// FormatSpanContent
        doc.assertData( 9, 15, find,     0, 3, 0, 0);
        doc.assertKey( 15, 16, "\n",     0, 4);
        doc.assertRest();
    }

    @Test
    public void refID(){
        String find = "qwerty";
        String raw = "!> ref  :" + find + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(buildId(find),
            CatalogueStatus.NOT_FOUND, 0);
        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.REF)
            .setDataSpan(0, 3).setNote(0)
            .setDataClass(FormattedSpan.class)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert data = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setLookup(find)
            .setIdentity(builder);

        cite.test(5,           raw,    0);
        doc.assertKey(  0,  2, "!>",   0, 0);
        doc.assertField(2,  6, " ref", 0, 1);
        doc.assertKey(  6,  9, "  :",  0, 2);
        data.test(1,           find,   0, 3);       /// DirectorySpan
        doc.assertChild(1,     find,   0, 3, 0);    /// ContentSpan
        doc.assertId(   9, 15, find,   0, 3, 0, 0);
        doc.assertKey( 15, 16, "\n",   0, 4);
        doc.assertRest();
    }

    @Test
    public void editAddField(){
        String before = "!>:abc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(2, "in-text", 0);
        commonIntext(doc);
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
        commonIntext(doc);
    }

    @Test
    public void editRemoveCite(){
        ///              0123456789012
        String before = "!>in-text:abec";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(0, 1);
        ///             01234567890123
        String after = ">in-text:abec";
        doc.assertDoc(1, after);

        doc.assertRest(after);
    }

    @Test
    public void editInsertField(){
        ///              0123
        String before = "!>";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(2, "f", 0);
        String after = "!>f";

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR)
            .setNote(0);

        doc.assertDoc(1,      after);
        cite.test(2,          after, 0);
        doc.assertKey(  0, 2, "!>", 0, 0);
        doc.assertField(2, 3, "f",  0, 1);
        doc.assertRest();
    }

    @Test
    public void editRemoveField(){
        ///              0123
        String before = "!>f";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(2, 3, 0);
        String after = "!>";

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR)
            .setNote(0);

        doc.assertDoc(1,      after);
        cite.test(1,          after, 0);
        doc.assertKey( 0, 2,  "!>",  0, 0);
        doc.assertRest();
    }

    private void commonIntext(DocumentAssert doc){
        ///             01234567890123
        String after = "!>in-text:abc";
        doc.assertDoc(1, after);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(0, 3).setNote(1)
            .setDataClass(ContentSpan.class);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("abc").setCount(1);

        cite.test(4,           after,     0);
        doc.assertKey(  0,  2, "!>",      0, 0);
        doc.assertField(2,  9, "in-text", 0, 1);
        doc.assertKey(  9, 10, ":",       0, 2);
        data.test(1,           "abc",     0, 3);
        doc.assertData(10, 13, "abc",     0, 3, 0);
        doc.assertRest();
    }
}
