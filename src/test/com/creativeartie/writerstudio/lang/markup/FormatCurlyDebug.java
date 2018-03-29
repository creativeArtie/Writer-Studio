package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
import static com.creativeartie.writerstudio.lang.markup.BranchTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;

import com.creativeartie.writerstudio.lang.*;

@RunWith(JUnit4.class)
public class FormatCurlyDebug {

    public static IDBuilder buildNoteId(String name){
        return new IDBuilder().addCategory("note").setId(name);
    }

    public static IDBuilder buildFootnoteId(String name){
        return new IDBuilder().addCategory("foot").setId(name);
    }

    public static IDBuilder buildEndnoteId(String name){
        return new IDBuilder().addCategory("end").setId(name);
    }

    private static final SetupParser[] parsers =
        FormatParseDirectory.getParsers(new boolean[4]);

    @Test
    public void basicCite(){
        ///           0123 456789
        String raw = "{@a-\\-bc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildNoteId("-bc").addCategory("a"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.NOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(builder);
        ContentTest content1 = new ContentTest()
            .setBegin(false).setText("a")
            .setEnd(false)  .setCount(1);
        ContentTest content2 = new ContentTest()
            .setBegin(false).setText("-bc")
            .setEnd(false)  .setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("-");

        note.test(      doc, 3, raw,       0);
        doc.assertKeyLeaf(0, 2, "{@",      0, 0);
        id.test(        doc, 3, "a-\\-bc", 0, 1);
        content1.test(  doc, 1, "a",       0, 1, 0);
        doc.assertIdLeaf( 2, 3, "a",       0, 1, 0, 0);
        doc.assertKeyLeaf(3, 4, "-",       0, 1, 1);
        content2.test(  doc, 2, "\\-bc",   0, 1, 2);
        escape.test(    doc, 2, "\\-",     0, 1, 2, 0);
        doc.assertKeyLeaf(4, 5, "\\",      0, 1, 2, 0, 0);
        doc.assertIdLeaf( 5, 6, "-",       0, 1, 2, 0, 1);
        doc.assertIdLeaf( 6, 8, "bc",      0, 1, 2, 1);
        doc.assertKeyLeaf(8, 9, "}",       0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicEndnote(){
        ///           0123456
        String raw = "{*abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildEndnoteId("abc"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.ENDNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(builder);
        ContentTest content = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(false)  .setCount(1);

        note.test(      doc, 3, raw,   0);
        doc.assertKeyLeaf(0, 2, "{*",  0, 0);
        id.test(        doc, 1, "abc", 0, 1);
        content.test(   doc, 1, "abc", 0, 1, 0);
        doc.assertIdLeaf( 2, 5, "abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(5, 6, "}",   0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void footnote(){
        ///           0123456
        String raw = "{^abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildFootnoteId("abc"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.FOOTNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.FOOTNOTE)
            .setIdentity(builder);
        ContentTest content = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(false)  .setCount(1);

        note.test(      doc, 3, raw,   0);
        doc.assertKeyLeaf(0, 2, "{^",  0, 0);
        id.test(        doc, 1, "abc", 0, 1);
        content.test(   doc, 1, "abc", 0, 1, 0);
        doc.assertIdLeaf( 2, 5, "abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(5, 6, "}",   0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void missingLastColon(){
        ///           012345
        String raw = "{^abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildFootnoteId("abc"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.FOOTNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.FOOTNOTE)
            .setIdentity(builder);
        ContentTest content = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(false)  .setCount(1);

        note.test(      doc, 2, raw,   0);
        doc.assertKeyLeaf(0, 2, "{^",  0, 0);
        id.test(        doc, 1, "abc", 0, 1);
        content.test(   doc, 1, "abc", 0, 1, 0);
        doc.assertIdLeaf( 2, 5, "abc", 0, 1, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void missingLastTwo(){
        ///           012
        String raw = "{^";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.FOOTNOTE);

        note.test(      doc, 1, raw,  0);
        doc.assertKeyLeaf(0, 2, "{^", 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void missingByNewLine(){
        ///           01 23456
        String raw = "{^\nabc";
        DocumentAssert doc = assertDoc(2, raw, parsers);

        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.FOOTNOTE);

        note.test(      doc, 1, "{^",  0);
        doc.assertKeyLeaf(0, 2, "{^", 0, 0);
        doc.assertLast("\nabc");
        doc.assertIds();
    }

    @Test
    public void missingMiddle(){
        ///           0123
        String raw = "{^}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.FOOTNOTE);

        note.test(      doc, 2, raw,  0);
        doc.assertKeyLeaf(0, 2, "{^", 0, 0);
        doc.assertKeyLeaf(2, 3, "}",  0, 1);
        doc.assertLast();
        doc.assertIds();
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
            DirectoryType.NOTE, "{@");
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
        FormatNoteTest note = new FormatNoteTest()
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setDirectoryType(type);
        DirectoryTest ptr = new DirectoryTest()
            .setPurpose(type).setIdentity(builder);
        ContentTest cat = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false) .setCount(1);
        ContentTest id = new ContentTest()
            .setText("dd").setBegin(false)
            .setEnd(false) .setCount(1);

        note.test(      doc, 3, after,    0);
        doc.assertKeyLeaf(0, 2, start,    0, 0);
        ptr.test(       doc, 3, "abc-dd", 0, 1);
        cat.test(       doc, 1, "abc",    0, 1, 0);
        doc.assertIdLeaf( 2, 5, "abc",    0, 1, 0, 0);
        doc.assertKeyLeaf(5, 6, "-",      0, 1, 1);
        id.test(        doc, 1, "dd",     0, 1, 2);
        doc.assertIdLeaf( 6, 8, "dd",     0, 1, 2, 0);
        doc.assertKeyLeaf(8, 9, "}",      0, 2);
        doc.assertLast();
        doc.assertIds();
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
        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.ENDNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(builder);
        ContentTest content = new ContentTest()
            .setBegin(false).setText("c")
            .setEnd(false)  .setCount(1);

        note.test(      doc, 3, "{*c}", 0);
        doc.assertKeyLeaf(0, 2, "{*",   0, 0);
        id.test(        doc, 1, "c",    0, 1);
        content.test(   doc, 1, "c",    0, 1, 0);
        doc.assertIdLeaf( 2, 3, "c",    0, 1, 0, 0);
        doc.assertKeyLeaf(3, 4, "}",    0, 2);
        doc.assertLast("split}");
        doc.assertIds();

    }
}