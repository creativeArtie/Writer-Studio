package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writer.lang.markup.BranchFormatAsserts.*;

import static com.creativeartie.writer.lang.DocumentAssert.*;

public class FormatLinkTest {

    private static final SetupParser[] parsers = FormatParseLink.getParsers(
        new boolean[4]);

    public static IDBuilder buildLinkRefId(String name){
        return buildLinkId(name);
    }

    public static IDBuilder buildLinkId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_LINK).setId(name);
    }

    @Test
    public void refFull(){
        ///           012345678901234
        String raw = "<@cat-id|text>";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        commonRefLink(doc);
    }

    @Test
    public void refWithNewline(){
        ///           0  123456
        String raw = "<@\ncat>";
        DocumentAssert doc = assertDoc(2, raw, parsers);

        FormatRefLinkAssert ref = new FormatRefLinkAssert(doc)
            .setText("")
            .setCatalogued(CatalogueStatus.NO_ID);

        ref.test(1,          "<@",     0);
        doc.assertKey(0,  2, "<@",     0, 0);
        doc.assertRest("\ncat>");
    }

    @Test
    public void refEmptyText(){
        ///           01234567890
        String raw = "<@cat-id|>";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildLinkId("id").addCategory("cat"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatRefLinkAssert ref = new FormatRefLinkAssert(doc)
            .setText("")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("cat-id");
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("cat")
            .setEnd(false)  .setCount(1);
        ContentAssert content2 = new ContentAssert(doc)
            .setBegin(false).setBoth("id")
            .setEnd(false)  .setCount(1);

        ref.test(4,          raw,      0);
        doc.assertKey(0,  2, "<@",     0, 0);
        id.test(3,           "cat-id", 0, 1);
        content1.test(1,     "cat",    0, 1, 0);
        doc.assertId( 2,  5, "cat",    0, 1, 0, 0);
        doc.assertKey(5,  6, "-",      0, 1, 1);
        content2.test(1,     "id",     0, 1, 2);
        doc.assertId( 6,  8, "id",     0, 1, 2, 0);
        doc.assertKey(8,  9, "|",      0, 2);
        doc.assertKey(9, 10, ">",      0, 3);
        doc.assertRest();
    }

    @Test
    public void refNoText(){
        ///           0123456789
        String raw = "<@cat-id>";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildLinkId("id").addCategory("cat"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatRefLinkAssert ref = new FormatRefLinkAssert(doc)
            .setText("")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("cat-id");
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("cat")
            .setEnd(false)  .setCount(1);
        ContentAssert content2 = new ContentAssert(doc)
            .setBegin(false).setBoth("id")
            .setEnd(false)  .setCount(1);

        ref.test(3,         raw,      0);
        doc.assertKey(0, 2, "<@",     0, 0);
        id.test(3,          "cat-id", 0, 1);
        content1.test(1,    "cat",    0, 1, 0);
        doc.assertId( 2, 5, "cat",    0, 1, 0, 0);
        doc.assertKey(5, 6, "-",      0, 1, 1);
        content2.test(1,    "id",     0, 1, 2);
        doc.assertId( 6, 8, "id",     0, 1, 2, 0);
        doc.assertKey(8, 9, ">",      0, 2);
        doc.assertRest();
    }

    @Test
    public void refBasic(){
        ///           012345
        String raw = "<@id>";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            buildLinkRefId("id"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatRefLinkAssert ref = new FormatRefLinkAssert(doc)
            .setText("")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("id");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("id")
            .setEnd(false)  .setCount(1);

        ref.test(3,         raw,  0);
        doc.assertKey(0, 2, "<@", 0, 0);
        id.test(1,          "id", 0, 1);
        doc.assertId( 2, 4, "id", 0, 1, 0, 0);
        content.test(1,     "id", 0, 1, 0);
        doc.assertKey(4, 5, ">",  0, 2);
        doc.assertRest();
    }

    @Test
    public void refEmpty(){
        ///           0123
        String raw = "<@>";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatRefLinkAssert ref = new FormatRefLinkAssert(doc)
            .setText("")
            .setCatalogued(CatalogueStatus.NO_ID);

        ref.test(2,         raw,  0);
        doc.assertKey(0, 2, "<@", 0, 0);
        doc.assertKey(2, 3, ">",  0, 1);
        doc.assertRest();
    }

    @Test
    public void refStart(){
        ///           012
        String raw = "<@";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatRefLinkAssert ref = new FormatRefLinkAssert(doc)
            .setText("")
            .setCatalogued(CatalogueStatus.NO_ID, null);

        ref.test(1,         raw,  0);
        doc.assertKey(0, 2, "<@", 0, 0);
        doc.assertRest();
    }

    @Test
    public void editRefIdContent(){
        ///           012345678901234
        String raw = "<@caat-id|text>";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        doc.delete(3, 4, 0, 1);
        ///             012345678901234
        String after = "<@cat-id|text>";
        doc.assertDoc(1, after);
        commonRefLink(doc);
    }

    @Test
    public void editRefNewContent(){
        ///           012345678901234
        String raw = "<@cat-id>";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        doc.insert(8, "|text", 0);
        ///             012345678901234
        String after = "<@cat-id|text>";
        doc.assertDoc(1, after);
        commonRefLink(doc);
    }


    public void commonRefLink(DocumentAssert doc){
        ///           012345678901234
        String raw = "<@cat-id|text>";

        IDBuilder builder = doc.addRef(
            buildLinkId("id").addCategory("cat"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatRefLinkAssert ref = new FormatRefLinkAssert(doc)
            .setText("text")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("cat-id");
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("cat")
            .setEnd(false)  .setCount(1);
        ContentAssert content2 = new ContentAssert(doc)
            .setBegin(false).setBoth("id")
            .setEnd(false)  .setCount(1);
        ContentAssert content3 = new ContentAssert(doc)
            .setBegin(false).setBoth("text")
            .setEnd(false)  .setCount(1);

        ref.test(5,           raw,      0);
        doc.assertKey( 0,  2, "<@",    0, 0);
        id.test(3,            "cat-id", 0, 1);
        content1.test(1,      "cat",    0, 1, 0);
        doc.assertId(  2,  5, "cat",   0, 1, 0, 0);
        doc.assertKey( 5,  6, "-",     0, 1, 1);
        content2.test(1,      "id",     0, 1, 2);
        doc.assertId(  6,  8, "id",    0, 1, 2, 0);
        doc.assertKey( 8,  9, "|",     0, 2);
        content3.test(1,      "text",   0, 3);
        doc.assertText(9, 13, "text",  0, 3, 0);
        doc.assertKey(13, 14, ">",     0, 4);
        doc.assertRest();
    }

    @Test
    public void linkFull(){
        ///           012345678901
        String raw = "<path|text>";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        editLinkCommon(doc);
    }

    @Test
    public void editLinkFill(){
        ///           012345678901
        String raw = "<";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        doc.insert(1, "path|text>", 0);
        ///             012345678901
        String after = "<path|text>";
        doc.assertDoc(1, after);
        editLinkCommon(doc);
    }

    @Test
    public void editLinkPath(){
        ///           012345678901
        String raw = "<pth|text>";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        doc.insert(2, "a", 0);
        ///             012345678901
        String after = "<path|text>";
        doc.assertDoc(1, after);
        editLinkCommon(doc);
    }

    @Test
    public void linkPath(){
        ///           0123456
        String raw = "<path>";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatDirectLinkAssert link = new FormatDirectLinkAssert(doc)
            .setText("path").setPath("path");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("path")
            .setEnd(false)  .setCount(1);

        link.test(3,         raw,    0);
        doc.assertKey( 0, 1, "<",    0, 0);
        content.test(1,      "path", 0, 1);
        doc.assertPath(1, 5, "path", 0, 1, 0);
        doc.assertKey( 5, 6, ">",    0, 2);
        doc.assertRest();
    }

    @Test
    public void linkWithNewline(){
        ///           01234 567
        String raw = "<path\n>";
        DocumentAssert doc = assertDoc(2, raw, parsers);

        FormatDirectLinkAssert link = new FormatDirectLinkAssert(doc)
            .setText("path").setPath("path");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("path")
            .setEnd(false)  .setCount(1);

        link.test (2,        "<path", 0);
        doc.assertKey( 0, 1, "<",     0, 0);
        content.test(1,      "path",  0, 1);
        doc.assertPath(1, 5, "path",  0, 1, 0);
        doc.assertRest("\n>");
    }

    @Test
    public void linkStart(){
        ///           01
        String raw = "<";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatDirectLinkAssert link = new FormatDirectLinkAssert(doc)
            .setText("");

        link.test(1,        raw,    0);
        doc.assertKey(0, 1, "<", 0, 0);
        doc.assertRest();
    }

    @Test
    public void linkNoEnd(){
        ///           01234567890
        String raw = "<path|text";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatDirectLinkAssert link = new FormatDirectLinkAssert(doc)
            .setText("text").setPath("path");
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("path")
            .setEnd(false)  .setCount(1);
        ContentAssert content2 = new ContentAssert(doc)
            .setBegin(false).setBoth("text")
            .setEnd(false)  .setCount(1);

        link.test (4,         raw,     0);
        doc.assertKey( 0,  1, "<",    0, 0);
        content1.test(1,       "path",  0, 1);
        doc.assertPath(1,  5, "path", 0, 1, 0);
        content2.test(1,      "text",  0, 3);
        doc.assertKey( 5,  6, "|",    0, 2);
        doc.assertText(6, 10, "text", 0, 3, 0);
        doc.assertRest();
    }

    private void editLinkCommon(DocumentAssert doc){
        ///           012345678901
        String raw = "<path|text>";

        FormatDirectLinkAssert link = new FormatDirectLinkAssert(doc)
            .setPath("path").setText("text");
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("path")
            .setEnd(false)  .setCount(1);
        ContentAssert content2 = new ContentAssert(doc)
            .setBegin(false).setBoth("text")
            .setEnd(false)  .setCount(1);

        link.test (5,         raw,    0);
        doc.assertKey( 0,  1, "<",    0, 0);
        content1.test(1,      "path", 0, 1);
        doc.assertPath(1,  5, "path", 0, 1, 0);
        doc.assertKey( 5,  6, "|",    0, 2);
        content2.test(1,      "text",  0, 3);
        doc.assertText(6, 10, "text", 0, 3, 0);
        doc.assertKey(10, 11, ">",    0, 4);
        doc.assertRest();
    }
}
