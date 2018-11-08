package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchFormatAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
public class FormatCurlyTest {

    public static IDBuilder buildNoteId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_RESEARCH).setId(name);
    }

    public static IDBuilder buildFootnoteId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_FOOTNOTE).setId(name);
    }

    public static IDBuilder buildEndnoteId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_ENDNOTE).setId(name);
    }

    private static final SetupParser[] parsers =
        FormatParsePointId.getParsers(new boolean[4]);

    @Test
    public void basicCite(){
        ///           0123 456789
        String raw = "{@a-\\-bc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildNoteId("-bc").addCategory("a"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatNoteAssert note = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.RESEARCH)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(builder)
            .setLookup("a-\\-bc");
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("a")
            .setEnd(false)  .setCount(1);
        ContentAssert content2 = new ContentAssert(doc)
            .setBegin(false).setBoth("-bc")
            .setEnd(false)  .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc).setEscape("-");

        note.test(3,        raw,       0);
        doc.assertKey(0, 2, "{@",      0, 0);
        id.test(3,          "a-\\-bc", 0, 1);
        content1.test(1,    "a",       0, 1, 0);
        doc.assertId( 2, 3, "a",       0, 1, 0, 0);
        doc.assertKey(3, 4, "-",       0, 1, 1);
        content2.test(2,    "\\-bc",   0, 1, 2);
        escape.test(2,      "\\-",     0, 1, 2, 0);
        doc.assertKey(4, 5, "\\",      0, 1, 2, 0, 0);
        doc.assertId( 5, 6, "-",       0, 1, 2, 0, 1);
        doc.assertId( 6, 8, "bc",      0, 1, 2, 1);
        doc.assertKey(8, 9, "}",       0, 2);
        doc.assertRest();
    }

    @Test
    public void basicEndnote(){
        ///           0123456
        String raw = "{*abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildEndnoteId("abc"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatNoteAssert note = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.ENDNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(builder).setLookup("abc");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        note.test(3,        raw,   0);
        doc.assertKey(0, 2, "{*",  0, 0);
        id.test(1,          "abc", 0, 1);
        content.test(1,     "abc", 0, 1, 0);
        doc.assertId( 2, 5, "abc", 0, 1, 0, 0);
        doc.assertKey(5, 6, "}",   0, 2);
        doc.assertRest();
    }

    @Test
    public void footnote(){
        ///           0123456
        String raw = "{^abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildFootnoteId("abc"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatNoteAssert note = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.FOOTNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.FOOTNOTE)
            .setIdentity(builder).setLookup("abc");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        note.test(3,        raw,   0);
        doc.assertKey(0, 2, "{^",  0, 0);
        id.test(1,          "abc", 0, 1);
        content.test(1,     "abc", 0, 1, 0);
        doc.assertId( 2, 5, "abc", 0, 1, 0, 0);
        doc.assertKey(5, 6, "}",   0, 2);
        doc.assertRest();
    }

    @Test
    public void missingLastColon(){
        ///           012345
        String raw = "{^abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildFootnoteId("abc"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatNoteAssert note = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.FOOTNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.FOOTNOTE)
            .setIdentity(builder).setLookup("abc");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        note.test(2,        raw,   0);
        doc.assertKey(0, 2, "{^",  0, 0);
        id.test(1,          "abc", 0, 1);
        content.test(1,     "abc", 0, 1, 0);
        doc.assertId( 2, 5, "abc", 0, 1, 0, 0);
        doc.assertRest();
    }

    @Test
    public void missingLastTwo(){
        ///           012
        String raw = "{^";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatNoteAssert note = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.FOOTNOTE);

        note.test(1,        raw,  0);
        doc.assertKey(0, 2, "{^", 0, 0);
        doc.assertRest();
    }

    @Test
    public void missingByNewLine(){
        ///           01 23456
        String raw = "{^\nabc";
        DocumentAssert doc = assertDoc(2, raw, parsers);

        FormatNoteAssert note = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.FOOTNOTE);

        note.test(1,        "{^",  0);
        doc.assertKey(0, 2, "{^", 0, 0);
        doc.assertRest("\nabc");
    }

    @Test
    public void missingMiddle(){
        ///           0123
        String raw = "{^}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatNoteAssert note = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.FOOTNOTE);

        note.test(2,        raw,  0);
        doc.assertKey(0, 2, "{^", 0, 0);
        doc.assertKey(2, 3, "}",  0, 1);
        doc.assertRest();
    }

    @Test
    public void editCategoryChanged(){
        ///              012345678
        String before = "{*ac-dd}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "b", 0, 1);
        commonEdited(doc);
    }

    @Test
    public void editNameChanged(){
        ///              01234567
        String before = "{*abc-d}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(6, "d", 0, 1);
        commonEdited(doc);
    }

    @Test
    public void editAddCat(){
        ///              01234567
        String before = "{*abcdd}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(5, "-", 0, 1);
        commonEdited(doc);
    }

    @Test
    public void editAddId(){
        ///              01234567
        String before = "{*}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(2, "abc-dd", 0);
        commonEdited(doc);
    }

    @Test
    public void editNote(){
        ///              01234567
        String before = "{@abc}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(5, "-dd", 0);
        commonEdited(doc, buildNoteId("dd").addCategory("abc"),
            DirectoryType.RESEARCH, "{@");
    }

    @Test
    public void editFootnote(){
        ///              01234567
        String before = "{^ab-dd}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(4, "c", 0, 1);
        commonEdited(doc, buildFootnoteId("dd").addCategory("abc"),
            DirectoryType.FOOTNOTE, "{^");
    }

    private void commonEdited(DocumentAssert doc){
        commonEdited(doc, buildEndnoteId("dd").addCategory("abc"),
            DirectoryType.ENDNOTE, "{*");
    }

    private void commonEdited(DocumentAssert doc, IDBuilder builder,
            DirectoryType type, String start){
        ///                0    12345678
        String after = start + "abc-dd}";
        doc.assertDoc(1, after);

        doc.addRef(builder, CatalogueStatus.NOT_FOUND, 0);
        FormatNoteAssert note = new FormatNoteAssert(doc)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setDirectoryType(type);
        DirectoryAssert ptr = new DirectoryAssert(doc)
            .setPurpose(type).setIdentity(builder)
            .setLookup("abc-dd");
        ContentAssert cat = new ContentAssert(doc)
            .setBoth("abc").setBegin(false)
            .setEnd(false) .setCount(1);
        ContentAssert id = new ContentAssert(doc)
            .setBoth("dd").setBegin(false)
            .setEnd(false) .setCount(1);

        note.test(3,        after,    0);
        doc.assertKey(0, 2, start,    0, 0);
        ptr.test(3,         "abc-dd", 0, 1);
        cat.test(1,         "abc",    0, 1, 0);
        doc.assertId( 2, 5, "abc",    0, 1, 0, 0);
        doc.assertKey(5, 6, "-",      0, 1, 1);
        id.test(1,          "dd",     0, 1, 2);
        doc.assertId( 6, 8, "dd",     0, 1, 2, 0);
        doc.assertKey(8, 9, "}",      0, 2);
        doc.assertRest();
    }

    @Test
    public void editSplitNote(){
        ///              0123456789
        String before = "{*csplit}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "}");
        ///             01234567890
        String after = "{*c}split}";
        doc.assertDoc(2, after);

        IDBuilder builder = doc.addRef(
            buildEndnoteId("c"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatNoteAssert note = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.ENDNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(builder).setLookup("c");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("c")
            .setEnd(false)  .setCount(1);

        note.test(3,        "{*c}", 0);
        doc.assertKey(0, 2, "{*",   0, 0);
        id.test(1,          "c",    0, 1);
        content.test(1,     "c",    0, 1, 0);
        doc.assertId( 2, 3, "c",    0, 1, 0, 0);
        doc.assertKey(3, 4, "}",    0, 2);
        doc.assertRest("split}");

    }
}
