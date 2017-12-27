package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.rules.*;

import java.io.File;
import java.util.Optional;

import java.util.Arrays;
import java.util.ArrayList;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class DirectoryDebug{

    private static final SetupParser[] parsers = new SetupParser[]{
        new DirectoryParser(DirectoryType.NOTE, ";")};

    private static IDBuilder buildId(String id){
        return new IDBuilder().addCategory("note").setId(id);
    }

    @Test
    public void nameOnly(){
        ///           012345
        String raw = "Hello";
        DocumentAssert doc  = assertDoc(1, raw, parsers);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(buildId("hello"));
        ContentTest content = new ContentTest()
            .setBegin(false).setText(raw)
            .setEnd(false)  .setCount(1);

        id.test(       doc, 1,            raw, 0);
        content.test(  doc, 1,            raw, 0, 0);
        doc.assertIdLeaf(0, raw.length(), raw, 0, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void nameEmpty(){
        ///            0123
        String raw = "no-";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(buildId("").addCategory("no"));
        ContentTest content = new ContentTest()
            .setText("no").setBegin(false)
            .setEnd(false).setCount(1);

        id.test(        doc, 2, raw,  0);
        content.test(   doc, 1, "no", 0, 0);
        doc.assertIdLeaf( 0, 2, "no", 0, 0, 0);
        doc.assertKeyLeaf(2, 3, "-",  0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void categorySingle(){
        ///           0123456
        String raw = "cat-hi";
        DocumentAssert doc  = assertDoc(1, raw, parsers);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(buildId("hi").addCategory("cat"));
        ContentTest content1 = new ContentTest()
            .setBegin(false).setText("cat")
            .setEnd(false)  .setCount(1);
        ContentTest content2 = new ContentTest()
            .setBegin(false).setText("hi")
            .setEnd(false)  .setCount(1);

        id.test(        doc, 3, raw,   0);
        content1.test(  doc, 1, "cat", 0, 0);
        doc.assertIdLeaf( 0, 3, "cat", 0, 0, 0);
        doc.assertKeyLeaf(3, 4, "-",   0, 1);
        content2.test(  doc, 1, "hi",  0, 2);
        doc.assertIdLeaf( 4, 6, "hi",  0, 2, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void categoryDouble(){
        ///           01234567890123
        String raw = "cat-gory-name";
        DocumentAssert doc  = assertDoc(1, raw, parsers);

        commonCategory(doc);
    }

    private void commonCategory(DocumentAssert doc){
        ///           01234567890123
        String raw = "cat-gory-name";

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(buildId("name").addCategory("cat", "gory"));
        ContentTest content1 = new ContentTest()
            .setBegin(false).setText("cat")
            .setEnd(false)  .setCount(1);
        ContentTest content2 = new ContentTest()
            .setBegin(false).setText("gory")
            .setEnd(false)  .setCount(1);
        ContentTest content3 = new ContentTest()
            .setBegin(false).setText("name")
            .setEnd(false)  .setCount(1);

        id.test(        doc,  5, raw,    0);
        content1.test(  doc,  1, "cat",  0, 0);
        doc.assertIdLeaf( 0,  3, "cat",  0, 0, 0);
        doc.assertKeyLeaf(3,  4, "-",    0, 1);
        content2.test(  doc,  1, "gory", 0, 2);
        doc.assertIdLeaf( 4,  8, "gory", 0, 2, 0);
        doc.assertKeyLeaf(8,  9, "-",    0, 3);
        content3.test(  doc,  1, "name", 0, 4);
        doc.assertIdLeaf( 9, 13, "name", 0, 4, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void categoryEmpty(){
        ///           01234
        String raw = "-see";
        DocumentAssert doc  = assertDoc(1, raw, parsers);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(buildId("see").addCategory(""));
        ContentTest content = new ContentTest()
            .setBegin(false).setText("see")
            .setEnd(false)  .setCount(1);

        id.test(        doc, 2, raw,   0);
        doc.assertKeyLeaf(0, 1, "-",   0, 0);
        content.test(   doc, 1, "see", 0, 1);
        doc.assertIdLeaf( 1, 4, "see", 0, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void categorySpaceSecond(){
        ///           012345678901234
        String raw = "yes  sir- -WEE";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(buildId("wee").addCategory("yes sir" , ""));
        ContentTest content1 = new ContentTest()
            .setBegin(false).setText("yes sir")
            .setEnd(false)  .setCount(2);
        ContentTest content2 = new ContentTest()
            .setBegin(true) .setText("")
            .setEnd(true)   .setCount(0);
        ContentTest content3 = new ContentTest()
            .setBegin(false).setText("WEE")
            .setEnd(false)  .setCount(1);

        id.test(        doc,   5, raw,        0);
        content1.test(  doc,   1, "yes  sir", 0, 0);
        doc.assertIdLeaf (0,   8, "yes  sir", 0, 0, 0);
        doc.assertKeyLeaf(8,   9, "-",        0, 1);
        content2.test(  doc,   1, " ",        0, 2);
        doc.assertIdLeaf (9,  10, " ",        0, 2, 0);
        doc.assertKeyLeaf(10, 11, "-",        0, 3);
        content3.test(   doc,  1, "WEE",      0, 4);
        doc.assertIdLeaf (11, 14, "WEE",      0, 4, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void categoryEscape(){
        ///            0123
        String raw = "\\-c";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(buildId("-c"));
        ContentTest content = new ContentTest()
            .setBegin(false).setText("-c")
            .setEnd(false)  .setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("-");

        id.test(        doc, 1, raw,    0);
        content.test(   doc, 2, "\\-c", 0, 0);
        escape.test(    doc, 2, "\\-",  0, 0, 0);
        doc.assertKeyLeaf(0, 1, "\\",   0, 0, 0, 0);
        doc.assertIdLeaf( 1, 2, "-",    0, 0, 0, 1);
        doc.assertIdLeaf( 2, 3, "c",    0, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void enderNewline(){
        ///           012 34567
        String raw = "abc\nabc";
        DocumentAssert doc = assertDoc(2, raw, parsers);
        commonUnparsed(doc, "\n");
    }

    @Test
    public void enderUser(){
        ///           01234567
        String raw = "abc;abc";
        DocumentAssert doc = assertDoc(2, raw, parsers);
        commonUnparsed(doc, ";");
    }

    private void commonUnparsed(DocumentAssert doc, String last){
        ///           012       3    4567
        String raw = "abc" + last + "abc";
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(buildId("abc"));
        ContentTest content = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(false)  .setCount(1);

        id.test(      doc, 1, "abc",    0);
        content.test( doc, 1, "abc", 0, 0);
        doc.assertLast(last + "abc");
        doc.assertIds();
    }

    @Test
    public void editCategoryChanged(){
        ///              012345678901234567
        String before = "catgory-gory-name";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(3, 7, 0);

        ///             01234567890123
        String after = "cat-gory-name";
        doc.assertDoc(1, after, parsers);
        commonCategory(doc);
    }

    @Test
    public void editCategoryAdded(){
        ///              012345678
        String before = "cat-name";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(4, "gory-", 0);

        ///             01234567890123
        String after = "cat-gory-name";
        doc.assertDoc(1, after, parsers);
        commonCategory(doc);
    }

    @Test
    public void editSplitUser(){
        ///             01234567
        String before = "abcabc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, ";");

        String after = "abc;abc";
        doc.assertDoc(2, after);
        commonUnparsed(doc, ";");
    }

    @Test
    public void editSplitNewline(){
        ///             01234567
        String before = "abcabc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "\n");

        String after = "abc\nabc";
        doc.assertDoc(2, after);
        commonUnparsed(doc, "\n");
    }
}
