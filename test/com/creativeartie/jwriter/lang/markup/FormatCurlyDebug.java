package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;

import com.creativeartie.jwriter.lang.*;

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
    public void cite(){
        ///               0123 456789
        String raw = "{@a-\\-bc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = buildNoteId("-bc").addCategory("a");
        doc.addRef(builder, CatalogueStatus.NOT_FOUND, 0);

        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.NOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(builder);
        ContentTest content1 = new ContentTest()
            .setText("a")  .setBegin(false)
            .setEnd(false) .setCount(1);
        ContentTest content2 = new ContentTest()
            .setText("-bc").setBegin(false)
            .setEnd(false) .setCount(1);
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

        doc.assertIds();
    }

    @Test
    public void endnote(){
        ///               0123456
        String raw = "{*abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = buildEndnoteId("abc");
        doc.addRef(builder, CatalogueStatus.NOT_FOUND, 0);

        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.ENDNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(builder);
        ContentTest content = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false) .setCount(1);

        note.test(      doc, 3, raw,   0);
        doc.assertKeyLeaf(0, 2, "{*",  0, 0);
        id.test(        doc, 1, "abc", 0, 1);
        content.test(   doc, 1, "abc", 0, 1, 0);
        doc.assertIdLeaf( 2, 5, "abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(5, 6, "}",   0, 2);

        doc.assertIds();
    }

    @Test
    public void footnote(){
        ///               0123456
        String raw = "{^abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = buildFootnoteId("abc");
        doc.addRef(builder, CatalogueStatus.NOT_FOUND, 0);

        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.FOOTNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.FOOTNOTE)
            .setIdentity(builder);
        ContentTest content = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false).setCount(1);


        note.test(      doc, 3, raw,   0);
        doc.assertKeyLeaf(0, 2, "{^",  0, 0);
        id.test(        doc, 1, "abc", 0, 1);
        content.test(   doc, 1, "abc", 0, 1, 0);
        doc.assertIdLeaf( 2, 5, "abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(5, 6, "}",   0, 2);

        doc.assertIds();
    }

    @Test
    public void noEnd(){
        ///               012345
        String raw = "{^abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = buildFootnoteId("abc");
        doc.addRef(builder, CatalogueStatus.NOT_FOUND, 0);

        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.FOOTNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.FOOTNOTE)
            .setIdentity(builder);
        ContentTest content = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false) .setCount(1);

        note.test(      doc, 2, raw,   0);
        doc.assertKeyLeaf(0, 2, "{^",  0, 0);
        id.test(        doc, 1, "abc", 0, 1);
        content.test(   doc, 1, "abc", 0, 1, 0);
        doc.assertIdLeaf( 2, 5, "abc", 0, 1, 0, 0);

        doc.assertIds();
    }

    @Test
    public void onlyStart(){
        ///               012
        String raw = "{^";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.FOOTNOTE);

        note.test(      doc, 1, raw,  0);
        doc.assertKeyLeaf(0, 2, "{^", 0, 0);

        doc.assertIds();
    }

    @Test
    public void noText(){
        ///               0123
        String raw = "{^}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatNoteTest note = new FormatNoteTest()
            .setDirectoryType(DirectoryType.FOOTNOTE);

        note.test(      doc, 2, raw,  0);
        doc.assertKeyLeaf(0, 2, "{^", 0, 0);
        doc.assertKeyLeaf(2, 3, "}",  0, 1);

        doc.assertIds();
    }
}
