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
        String field = "source|in-text";
        String raw = "!>" + field + ":a";
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
        doc.assertField(2, 16, field, 0, 1);
        doc.assertKey( 16, 17, ":",       0, 2);
        data.test(1,           "a",       0, 3);
        doc.assertData(17, 18, "a",       0, 3, 0);
        doc.assertRest();
    }

    @Test
    public void inTextColonNewline(){
        String field = "source|in-text";
        String raw = "!>" + field + ":\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT).setNote(0);

        cite.test(4,           raw,   0);
        doc.assertKey(  0,  2, "!>",  0, 0);
        doc.assertField(2, 16, field, 0, 1);
        doc.assertKey( 16, 17, ":",   0, 2);
        doc.assertKey( 17, 18, "\n",  0, 3);
        doc.assertRest();
    }

    @Test
    public void inTextNoColon(){
        String field = "source|in-text";
        String raw = "!>" + field;
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT).setNote(0);

        cite.test(2,           raw,   0);
        doc.assertKey(  0,  2, "!>",  0, 0);
        doc.assertField(2, 16, field, 0, 1);
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
        String field = "source|in-text";
        String raw = "!>" + field + "a";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(0, 2).setNote(1)
            .setDataClass(ContentSpan.class);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("a").setCount(1);

        cite.test(3,           raw,   0);
        doc.assertKey(  0,  2, "!>",  0, 0);
        doc.assertField(2, 16, field, 0, 1);
        data.test(1,           "a",   0, 2);
        doc.assertData(16, 17, "a",   0, 2, 0);
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
        String field = "source|footnote";
        String raw = "!>" + field + ":abc\\\n";
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
        doc.assertField(2, 17, field, 0, 1);
        doc.assertKey( 17, 18, ":",        0, 2);
        data.test(1,           "abc\\\n",  0, 3);
        doc.assertChild(2,     "abc\\\n",  0, 3, 0);
        doc.assertData(18, 21, "abc",      0, 3, 0, 0);
        doc.assertChild(2,     "\\\n",     0, 3, 0, 1);
        doc.assertKey( 21, 22, "\\",       0, 3, 0, 1, 0);
        doc.assertData(22, 23, "\n",       0, 3, 0, 1, 1);
        doc.assertRest();
    }

    @Test
    public void basicSource(){
        String find = "Henry** Reads**";
        String field = "source|work-cited";
        String raw = "!>" + field + ":" + find + "\n";
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
        doc.assertField(2, 19, field, 0, 1);
        doc.assertKey( 19, 20, ":",      0, 2);
        data.test(4,           find,     0, 3);
        doc.assertChild(1,     "Henry",  0, 3, 0);
        doc.assertData(20, 25, "Henry",  0, 3, 0, 0);
        doc.assertKey( 25, 27, "**",     0, 3, 1);
        doc.assertChild(1,     " Reads", 0, 3, 2);
        doc.assertData(27, 33, " Reads", 0, 3, 2, 0);
        doc.assertKey( 33, 35, "**",     0, 3, 3);
        doc.assertKey( 35, 36, "\n",     0, 4);
        doc.assertRest();
    }

    @Test
    public void sourceWithNote(){
        String find = "{@abc}";
        String field = "source|work-cited";
        String raw = "!>" + field + ":" + find + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.SOURCE)
            .setDataSpan(0, 3).setNote(1)
            .setDataClass(FormattedSpan.class);
        FormattedSpanAssert data = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0)
            .setParsed("{@abc}");

        cite.test(5,           raw,   0);
        doc.assertKey(  0,  2, "!>",  0, 0);
        doc.assertField(2, 19, field, 0, 1);
        doc.assertKey( 19, 20, ":",   0, 2);
        data.test(1,           find,  0, 3); /// FormattedSpan
        doc.assertChild(1,     find,  0, 3, 0); /// FormatSpanContent
        doc.assertData(20, 26, find,  0, 3, 0, 0);
        doc.assertKey( 26, 27, "\n",  0, 4);
        doc.assertRest();
    }

    @Test
    public void refID(){
        String find = "qwerty";
        String field = "  source|reference";
        String raw = "!>" + field + "  :" + find + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(buildId(find),
            CatalogueStatus.NOT_FOUND, 0);
        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.REF)
            .setDataSpan(0, 3).setNote(0)
            .setDataClass(DirectorySpan.class)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert data = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setLookup(find)
            .setIdentity(builder);

        cite.test(5,           raw,    0);
        doc.assertKey(  0,  2, "!>",   0, 0);
        doc.assertField(2, 20, field, 0, 1);
        doc.assertKey( 20, 23, "  :",  0, 2);
        data.test(1,           find,   0, 3);       /// DirectorySpan
        doc.assertChild(1,     find,   0, 3, 0);    /// ContentSpan
        doc.assertId(  23, 29, find,   0, 3, 0, 0);
        doc.assertKey( 29, 30, "\n",   0, 4);
        doc.assertRest();
    }

    @Test
    public void editAddField(){
        String before = "!>:abc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(2, "source|in-text", 0);
        commonIntext(doc);
    }

    @Test
    public void editContent(){
        /// -------------0000000000111111111122
        /// -------------0123456789012345678901
        String before = "!>source|in-text:abec";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(19, 20, 0);
        ///             01234567890123
        String after = "!>source|in-text:abc";
        doc.assertDoc(1, after, parsers);
        commonIntext(doc);
    }

    @Test
    public void editRemoveCite(){
        /// -------------0000000000111111122222
        /// -------------0123456789012345601234
        String before = "!>source|in-text:abec";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(0, 1);
        ///             01234567890123
        String after = ">source|in-text:abec";
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
        String field = "source|in-text";
        String after = "!>" + field + ":abc";
        doc.assertDoc(1, after);

        CiteLineAssert cite = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(0, 3).setNote(1)
            .setDataClass(ContentSpan.class);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("abc").setCount(1);

        cite.test(4,           after, 0);
        doc.assertKey(  0,  2, "!>",  0, 0);
        doc.assertField(2, 16, field, 0, 1);
        doc.assertKey( 16, 17, ":",   0, 2);
        data.test(1,           "abc", 0, 3);
        doc.assertData(17, 20, "abc", 0, 3, 0);
        doc.assertRest();
    }
}
