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
public class FormatLinkDebug {

    private static final SetupParser[] parsers = FormatParseLink.getParsers(
        new boolean[4]);

    public static IDBuilder buildLinkRefId(String name){
        return buildLinkId(name);
    }

    public static IDBuilder buildLinkId(String name){
        return new IDBuilder().addCategory("link").setId(name);
    }

    @Test
    public void refFull(){
        ///           012345678901234
        String raw = "<@cat-id|text>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        editRefCommon(doc);
    }

    @Test
    public void refWithNewline(){
        ///           0  123456
        String raw = "<@\ncat>";
        DocumentAssert doc = DocumentAssert.assertDoc(2, raw, parsers);

        FormatLinkTest ref = new FormatLinkTest()
            .setText("")
            .setCatalogued(CatalogueStatus.NO_ID);

        ref.test(       doc,  1, "<@",     0);
        doc.assertKeyLeaf(0,  2, "<@",     0, 0);
        doc.assertLast("\ncat>");
        doc.assertIds();
    }

    @Test
    public void refEmptyText(){
        ///           01234567890
        String raw = "<@cat-id|>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildLinkId("id").addCategory("cat"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatLinkTest ref = new FormatLinkTest()
            .setText("")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        ContentTest content1 = new ContentTest()
            .setBegin(false).setText("cat")
            .setEnd(false)  .setCount(1);
        ContentTest content2 = new ContentTest()
            .setBegin(false).setText("id")
            .setEnd(false)  .setCount(1);

        ref.test(       doc,  4, raw,      0);
        doc.assertKeyLeaf(0,  2, "<@",     0, 0);
        id.test(        doc,  3, "cat-id", 0, 1);
        content1.test(  doc,  1, "cat",    0, 1, 0);
        doc.assertIdLeaf( 2,  5, "cat",    0, 1, 0, 0);
        doc.assertKeyLeaf(5,  6, "-",      0, 1, 1);
        content2.test(  doc,  1, "id",     0, 1, 2);
        doc.assertIdLeaf( 6,  8, "id",     0, 1, 2, 0);
        doc.assertKeyLeaf(8,  9, "|",      0, 2);
        doc.assertKeyLeaf(9, 10, ">",      0, 3);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void refNoText(){
        ///           0123456789
        String raw = "<@cat-id>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildLinkId("id").addCategory("cat"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatLinkTest ref = new FormatLinkTest()
            .setText("")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        ContentTest content1 = new ContentTest()
            .setBegin(false).setText("cat")
            .setEnd(false)  .setCount(1);
        ContentTest content2 = new ContentTest()
            .setBegin(false).setText("id")
            .setEnd(false)  .setCount(1);

        ref.test(       doc, 3, raw,      0);
        doc.assertKeyLeaf(0, 2, "<@",     0, 0);
        id.test(        doc, 3, "cat-id", 0, 1);
        content1.test(  doc, 1, "cat",    0, 1, 0);
        doc.assertIdLeaf( 2, 5, "cat",    0, 1, 0, 0);
        doc.assertKeyLeaf(5, 6, "-",      0, 1, 1);
        content2.test(  doc, 1, "id",     0, 1, 2);
        doc.assertIdLeaf( 6, 8, "id",     0, 1, 2, 0);
        doc.assertKeyLeaf(8, 9, ">",      0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void refBasic(){
        ///           012345
        String raw = "<@id>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildLinkRefId("id"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatLinkTest ref = new FormatLinkTest()
            .setText("")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        ContentTest content = new ContentTest()
            .setBegin(false).setText("id")
            .setEnd(false)  .setCount(1);

        ref.test(       doc, 3, raw,  0);
        doc.assertKeyLeaf(0, 2, "<@", 0, 0);
        id.test(        doc, 1, "id", 0, 1);
        doc.assertIdLeaf( 2, 4, "id", 0, 1, 0, 0);
        content.test(   doc, 1, "id", 0, 1, 0);
        doc.assertKeyLeaf(4, 5, ">",  0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void refEmpty(){
        ///           0123
        String raw = "<@>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        FormatLinkTest ref = new FormatLinkTest()
            .setText("")
            .setCatalogued(CatalogueStatus.NO_ID);

        ref.test(       doc, 2, raw,  0);
        doc.assertKeyLeaf(0, 2, "<@", 0, 0);
        doc.assertKeyLeaf(2, 3, ">",  0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void refStart(){
        ///           012
        String raw = "<@";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        FormatLinkTest ref = new FormatLinkTest()
            .setText("")
            .setCatalogued(CatalogueStatus.NO_ID, null);

        ref.test(       doc, 1, raw,  0);
        doc.assertKeyLeaf(0, 2, "<@", 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void editRefIdContent(){
        ///           012345678901234
        String raw = "<@caat-id|text>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        doc.delete(3, 4, 0, 1);
        ///             012345678901234
        String after = "<@cat-id|text>";
        doc.assertDoc(1, after);
        editRefCommon(doc);
    }

    @Test
    public void editRefNewContent(){
        ///           012345678901234
        String raw = "<@cat-id>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        doc.insert(8, "|text", 0);
        ///             012345678901234
        String after = "<@cat-id|text>";
        doc.assertDoc(1, after, parsers);
        editRefCommon(doc);
    }


    public void editRefCommon(DocumentAssert doc){
        ///           012345678901234
        String raw = "<@cat-id|text>";

        IDBuilder builder = doc.addRef(
            buildLinkId("id").addCategory("cat"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatLinkTest ref = new FormatLinkTest()
            .setText("text")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        ContentTest content1 = new ContentTest()
            .setBegin(false).setText("cat")
            .setEnd(false)  .setCount(1);
        ContentTest content2 = new ContentTest()
            .setBegin(false).setText("id")
            .setEnd(false)  .setCount(1);
        ContentTest content3 = new ContentTest()
            .setBegin(false).setText("text")
            .setEnd(false)  .setCount(1);

        ref.test(        doc, 5, raw,      0);
        doc.assertKeyLeaf( 0,  2, "<@",    0, 0);
        id.test(         doc, 3, "cat-id", 0, 1);
        content1.test(   doc, 1, "cat",    0, 1, 0);
        doc.assertIdLeaf(  2,  5, "cat",   0, 1, 0, 0);
        doc.assertKeyLeaf( 5,  6, "-",     0, 1, 1);
        content2.test(   doc, 1, "id",     0, 1, 2);
        doc.assertIdLeaf(  6,  8, "id",    0, 1, 2, 0);
        doc.assertKeyLeaf( 8,  9, "|",     0, 2);
        content3.test(   doc, 1, "text",   0, 3);
        doc.assertTextLeaf(9, 13, "text",  0, 3, 0);
        doc.assertKeyLeaf(13, 14, ">",     0, 4);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void linkFull(){
        ///           012345678901
        String raw = "<path|text>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        editLinkCommon(doc);
    }

    @Test
    public void editLinkFill(){
        ///           012345678901
        String raw = "<";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        doc.insert(1, "path|text>", 0);
        ///             012345678901
        String after = "<path|text>";
        doc.assertDoc(1, after, parsers);
        editLinkCommon(doc);
    }

    @Test
    public void editLinkPath(){
        ///           012345678901
        String raw = "<pth|text>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        doc.insert(2, "a", 0);
        ///             012345678901
        String after = "<path|text>";
        doc.assertDoc(1, after, parsers);
        editLinkCommon(doc);
    }

    @Test
    public void linkPath(){
        ///           0123456
        String raw = "<path>";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        FormatLinkTest link = new FormatLinkTest()
            .setText("path").setPath(doc, 0, 1);
        ContentTest content = new ContentTest()
            .setBegin(false).setText("path")
            .setEnd(false)  .setCount(1);

        link.test (      doc, 3, raw,    0);
        doc.assertKeyLeaf( 0, 1, "<",    0, 0);
        content.test(    doc, 1, "path", 0, 1);
        doc.assertPathLeaf(1, 5, "path", 0, 1, 0);
        doc.assertKeyLeaf( 5, 6, ">",    0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void linkWithNewline(){
        ///           01234 567
        String raw = "<path\n>";
        DocumentAssert doc = DocumentAssert.assertDoc(2, raw, parsers);

        FormatLinkTest link = new FormatLinkTest()
            .setText("path").setPath(doc, 0, 1);
        ContentTest content = new ContentTest()
            .setBegin(false).setText("path")
            .setEnd(false)  .setCount(1);

        link.test (      doc, 2, "<path", 0);
        doc.assertKeyLeaf( 0, 1, "<",     0, 0);
        content.test(    doc, 1, "path",  0, 1);
        doc.assertPathLeaf(1, 5, "path",  0, 1, 0);
        doc.assertLast("\n>");
        doc.assertIds();
    }

    @Test
    public void linkStart(){
        ///           01
        String raw = "<";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        FormatLinkTest link = new FormatLinkTest()
            .setText("");

        link.test ( doc,1, raw,    0);
        doc.assertKeyLeaf(0, 1, "<", 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void linkNoEnd(){
        ///           01234567890
        String raw = "<path|text";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        FormatLinkTest link = new FormatLinkTest()
            .setText("text").setPath(doc, 0, 1);
        ContentTest content1 = new ContentTest()
            .setBegin(false).setText("path")
            .setEnd(false)  .setCount(1);
        ContentTest content2 = new ContentTest()
            .setBegin(false).setText("text")
            .setEnd(false)  .setCount(1);

        link.test (      doc, 4, raw,     0);
        doc.assertKeyLeaf( 0,  1, "<",    0, 0);
        content1.test(   doc, 1, "path",  0, 1);
        doc.assertPathLeaf(1,  5, "path", 0, 1, 0);
        content2.test(   doc, 1, "text",  0, 3);
        doc.assertKeyLeaf( 5,  6, "|",    0, 2);
        doc.assertTextLeaf(6, 10, "text", 0, 3, 0);
        doc.assertLast();
        doc.assertIds();
    }

    private void editLinkCommon(DocumentAssert doc){
        ///           012345678901
        String raw = "<path|text>";

        FormatLinkTest link = new FormatLinkTest()
            .setPath(doc, 0, 1).setText("text");
        ContentTest content1 = new ContentTest()
            .setBegin(false).setText("path")
            .setEnd(false)  .setCount(1);
        ContentTest content2 = new ContentTest()
            .setBegin(false).setText("text")
            .setEnd(false)  .setCount(1);

        link.test ( doc,       5, raw,    0);
        doc.assertKeyLeaf( 0,  1, "<",    0, 0);
        content1.test(   doc,  1, "path", 0, 1);
        doc.assertPathLeaf(1,  5, "path", 0, 1, 0);
        doc.assertKeyLeaf( 5,  6, "|",    0, 2);
        content2.test(   doc, 1, "text",  0, 3);
        doc.assertTextLeaf(6, 10, "text", 0, 3, 0);
        doc.assertKeyLeaf(10, 11, ">",    0, 4);
        doc.assertLast();
        doc.assertIds();
    }
}
