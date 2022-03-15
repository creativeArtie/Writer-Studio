package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.BranchBasicAsserts.*;

import static com.creativeartie.writer.lang.DocumentAssert.*;
public class DirectoryTest{

    private static final SetupParser[] parsers = new SetupParser[]{
        DirectoryParser.REF_NOTE};

    private static IDBuilder buildId(String id){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_RESEARCH).setId(id);
    }

    @Test
    public void nameOnly(){
        ///           012345
        String raw = "Hello";
        DocumentAssert doc  = assertDoc(1, raw, parsers);

        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(buildId("hello"))
            .setLookup("hello");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth(raw)
            .setEnd(false)  .setCount(1);

        id.test(1,                    raw, 0);
        content.test(1,               raw, 0, 0);
        doc.assertId(0, raw.length(), raw, 0, 0, 0);
        doc.assertRest();
    }

    @Test
    public void nameEmpty(){
        ///            0123
        String raw = "no-";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(buildId("").addCategory("no"))
            .setLookup("no-");
        ContentAssert content = new ContentAssert(doc)
            .setBoth("no").setBegin(false)
            .setEnd(false).setCount(1);

        id.test(2,          raw,  0);
        content.test(1,     "no", 0, 0);
        doc.assertId( 0, 2, "no", 0, 0, 0);
        doc.assertKey(2, 3, "-",  0, 1);
        doc.assertRest();
    }

    @Test
    public void categorySingle(){
        ///           0123456
        String raw = "cat-hi";
        DocumentAssert doc  = assertDoc(1, raw, parsers);

        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(buildId("hi").addCategory("cat"))
            .setLookup(raw);
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("cat")
            .setEnd(false)  .setCount(1);
        ContentAssert content2 = new ContentAssert(doc)
            .setBegin(false).setBoth("hi")
            .setEnd(false)  .setCount(1);

        id.test(3,          raw,   0);
        content1.test(1,    "cat", 0, 0);
        doc.assertId( 0, 3, "cat", 0, 0, 0);
        doc.assertKey(3, 4, "-",   0, 1);
        content2.test(1,    "hi",  0, 2);
        doc.assertId( 4, 6, "hi",  0, 2, 0);
        doc.assertRest();
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

        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(buildId("name").addCategory("cat", "gory"))
            .setLookup(raw);
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("cat")
            .setEnd(false)  .setCount(1);
        ContentAssert content2 = new ContentAssert(doc)
            .setBegin(false).setBoth("gory")
            .setEnd(false)  .setCount(1);
        ContentAssert content3 = new ContentAssert(doc)
            .setBegin(false).setBoth("name")
            .setEnd(false)  .setCount(1);

        id.test( 5,          raw,    0);
        content1.test(1,     "cat",  0, 0);
        doc.assertId( 0, 3,  "cat",  0, 0, 0);
        doc.assertKey(3, 4,  "-",    0, 1);
        content2.test(1,     "gory", 0, 2);
        doc.assertId( 4, 8,  "gory", 0, 2, 0);
        doc.assertKey(8, 9,  "-",    0, 3);
        content3.test(1,     "name", 0, 4);
        doc.assertId( 9, 13, "name", 0, 4, 0);
        doc.assertRest();
    }

    @Test
    public void categoryEmpty(){
        ///           01234
        String raw = "-see";
        DocumentAssert doc  = assertDoc(1, raw, parsers);

        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(buildId("see").addCategory(""))
            .setLookup("-see");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("see")
            .setEnd(false)  .setCount(1);

        id.test(2,           raw,   0);
        doc.assertKey(0, 1, "-",   0, 0);
        content.test(1,     "see", 0, 1);
        doc.assertId( 1, 4, "see", 0, 1, 0);
        doc.assertRest();
    }

    @Test
    public void categorySpaceSecond(){
        ///           012345678901234
        String raw = "yes  sir- -WEE";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(buildId("wee").addCategory("yes sir" , ""))
            .setLookup("yes sir--wee");
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("yes sir")
            .setEnd(false)  .setCount(2);
        ContentAssert content2 = new ContentAssert(doc)
            .setBegin(true) .setBoth(" ")
            .setEnd(true)   .setCount(0);
        ContentAssert content3 = new ContentAssert(doc)
            .setBegin(false).setBoth("WEE")
            .setEnd(false)  .setCount(1);

        id.test(5,           raw,        0);
        content1.test(1,     "yes  sir", 0, 0);
        doc.assertId (0, 8,  "yes  sir", 0, 0, 0);
        doc.assertKey(8, 9,  "-",        0, 1);
        content2.test(1,      " ",        0, 2);
        doc.assertId ( 9, 10, " ",        0, 2, 0);
        doc.assertKey(10, 11, "-",        0, 3);
        content3.test(1,      "WEE",      0, 4);
        doc.assertId (11, 14, "WEE",      0, 4, 0);
        doc.assertRest();
    }

    @Test
    public void categoryEscape(){
        ///            0123
        String raw = "\\-c";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(buildId("-c"))
            .setLookup("\\-c");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("-c")
            .setEnd(false)  .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc).setEscape("-");

        id.test(1,         raw,    0);
        content.test(2,    "\\-c", 0, 0);
        escape.test(2,     "\\-",  0, 0, 0);
        doc.assertKey(0, 1, "\\",  0, 0, 0, 0);
        doc.assertId( 1, 2, "-",   0, 0, 0, 1);
        doc.assertId( 2, 3, "c",   0, 0, 1);
        doc.assertRest();
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
        String raw = "abc}abc";
        DocumentAssert doc = assertDoc(2, raw, parsers);
        commonUnparsed(doc, "}");
    }

    private void commonUnparsed(DocumentAssert doc, String last){
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(buildId("abc"))
            .setLookup("abc");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        id.test(1,         "abc", 0);
        content.test(1,    "abc", 0, 0);
        doc.assertId(0, 3, "abc", 0, 0, 0);
        doc.assertRest(last + "abc");
    }

    @Test
    public void editCategoryChanged(){
        ///              012345678901234567
        String before = "catgory-gory-name";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(3, 7, 0);

        ///             01234567890123
        String after = "cat-gory-name";
        doc.assertDoc(1, after);
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
        doc.assertDoc(1, after);
        commonCategory(doc);
    }

    @Test
    public void editSplitUser(){
        ///             01234567
        String before = "abcabc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "}");

        String after = "abc}abc";
        doc.assertDoc(2, after);
        commonUnparsed(doc, "}");
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
