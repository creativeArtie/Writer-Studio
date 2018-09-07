package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class ContentTest{

    private static final SetupParser[] parsers = new SetupParser[]{
        AuxiliaryData.CONTENT_AGENDA};

    @Test
    public void textCount1(){
        ///           012345
        String raw = "Hello";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth(raw)
            .setEnd(false)  .setCount(1);

        content.test(1,                 raw, 0);
        doc.assertText(0, raw.length(), raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void textCount2(){
        ///           012345
        String raw = "I see";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth(raw)
            .setEnd(false)  .setCount(2);

        content.test(1,                 raw, 0);
        doc.assertText(0, raw.length(), raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void spaceSingle(){
        ///           01
        String raw = " ";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentAssert content = new ContentAssert(doc)
            .setBegin(true).setBoth(" ")
            .setEnd(true)  .setCount(0);

        content.test(1,                 raw, 0);
        doc.assertText(0, raw.length(), raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void spaceDoubled(){
        ///           012
        String raw = "  ";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentAssert content = new ContentAssert(doc)
            .setBegin(true).setBoth(" ")
            .setEnd(true)  .setCount(0);

        content.test(1,                 raw, 0);
        doc.assertText(0, raw.length(), raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void escapeBasic(){
        ///            012
        String raw = "\\b";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("b")
            .setEnd(false)  .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc)
            .setEscape("b");

        content.test(1,      raw,  0);
        escape.test( 2,      raw,  0, 0);
        doc.assertKey( 0, 1, "\\", 0, 0, 0);
        doc.assertText(1, 2, "b",  0, 0, 1);
        doc.assertRest();
    }

    @Test
    public void escapeMiddle(){
        ///           0 123 45
        String raw = "a\\bc\t";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("abc ")
            .setEnd(true)   .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc)
            .setEscape("b");

        content.test(3,      raw,   0);
        doc.assertText(0, 1, "a",   0, 0);
        escape.test( 2,       "\\b", 0, 1);
        doc.assertKey( 1, 2, "\\",  0, 1, 0);
        doc.assertText(2, 3, "b",   0, 1, 1);
        doc.assertText(3, 5, "c\t", 0, 2);
        doc.assertRest();
    }

    @Test
    public void escapeSlash(){
        ///            0 12
        String raw = "\\\\";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("\\")
            .setEnd(false)  .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc)
            .setEscape("\\");

        content.test(1,     raw,  0);
        escape.test( 2,     raw,  0, 0);
        doc.assertKey( 0, 1, "\\", 0, 0, 0);
        doc.assertText(1, 2, "\\", 0, 0, 1);
        doc.assertRest();
    }

    @Test
    public void escapeSpace(){
        ///            012
        String raw = "\\ ";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentAssert content = new ContentAssert(doc)
            .setBegin(true).setBoth(" ")
            .setEnd(true)  .setCount(0);
        EscapeAssert escape = new EscapeAssert(doc)
            .setEscape(" ");

        content.test(1,     raw,  0);
        escape.test( 2,     raw,  0, 0);
        doc.assertKey( 0, 1, "\\", 0, 0, 0);
        doc.assertText(1, 2, " ",  0, 0, 1);
        doc.assertRest();
    }

    @Test
    public void escapeCutOff(){
        ///           01234 56
        String raw = "  abc\\";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentAssert content = new ContentAssert(doc)
            .setBegin(true).setBoth(" abc")
            .setEnd(false) .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc)
            .setEscape("");

        content.test(2,      raw,     0);
        doc.assertText(0, 5, "  abc", 0, 0);
        escape.test(1,       "\\",    0, 1);
        doc.assertKey( 5, 6, "\\",    0, 1, 0);
        doc.assertRest();
    }

    public void escapeEnder(){
        ///           012 345678
        String raw = "abc\\}abd";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("abc}abd")
            .setEnd(false)  .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc)
            .setEscape("}");

        content.test(3,      raw,   0);
        doc.assertText(0, 3, "abc", 0, 0);
        escape.test(1,       "\\}", 0, 1);
        doc.assertKey( 3, 4, "\\",  0, 1, 0);
        doc.assertText(4, 5, "}",   0, 1, 1);
        doc.assertText(5, 8, "abd", 0, 2);
        doc.assertRest();
    }

    @Test
    public void enderNoText(){
        ///           0123456
        String raw = "}abcab";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        doc.assertRest("}abcab");
    }

    @Test
    public void enderNewline(){
        ///           012 3456
        String raw = "abc\nab";
        DocumentAssert doc = assertDoc(2, raw, parsers);
        commonUnparsed(doc, "\n");
    }

    @Test
    public void enderUser(){
        ///           0123456
        String raw = "abc}ab";
        DocumentAssert doc = assertDoc(2, raw, parsers);
        commonUnparsed(doc, "}");
    }

    private void commonUnparsed(DocumentAssert doc, String last){
        String unparsed = last + "ab";

        ContentAssert content = new ContentAssert(doc)
            .setBoth("abc").setBegin(false)
            .setEnd(false) .setCount(1);

        content.test(1,      "abc", 0);
        doc.assertText(0, 3, "abc", 0, 0);
        doc.assertRest(unparsed);
    }


    @Test
    public void editAddTextSimple(){
        ///              01234
        String before = "hall run";
        DocumentAssert doc = assertDoc(1, before, parsers);
        ///           012345678901
        String after = "hallway run";
        doc.insert(4, "way");
        doc.assertDoc(1, after);

        ContentAssert content = new ContentAssert(doc)
            .setBoth(after) .setBegin(false)
            .setEnd(false).setCount(2);

        content.test(1,       after, 0);
        doc.assertText(0, 11, after, 0, 0);
    }

    @Test
    public void editAddToEnd(){
        ///              01234567
        String before = "hallway";
        DocumentAssert doc = assertDoc(1, before, parsers);
        ///           012345678901
        String after = "hallway run";
        doc.insert(7, " run");
        doc.assertDoc(1, after);

        ContentAssert content = new ContentAssert(doc)
            .setBoth(after) .setBegin(false)
            .setEnd(false).setCount(2);

        content.test(1,       after, 0);
        doc.assertText(0, 11, after, 0, 0);
    }

    @Test
    public void editAddEscape(){
        ///              01234
        String before = "ab*d";
        DocumentAssert doc = assertDoc(1, before, parsers);
        String after = "ab\\*d";
        doc.insert(2, "\\");
        doc.assertDoc(1, after);

        ContentAssert content = new ContentAssert(doc)
            .setBoth("ab*d").setBegin(false)
            .setEnd(false)  .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc).setEscape("*");

        content.test(3,      after,   0);
        doc.assertText(0, 2, "ab",  0, 0);
        escape.test(2,       "\\*", 0, 1);
        doc.assertKey( 2, 3, "\\",  0, 1, 0);
        doc.assertText(3, 4, "*",   0, 1, 1);
        doc.assertText(4, 5, "d",   0, 2);
    }

    @Test
    public void editRemoveEscape(){
        ///              01234
        String before = "ab\\*d";
        DocumentAssert doc = assertDoc(1, before, parsers);
        String after = "ab*d";
        doc.delete(2, 3);
        doc.assertDoc(1, after);

        ContentAssert content = new ContentAssert(doc)
            .setBoth(after) .setBegin(false)
            .setEnd(false).setCount(1);

        content.test(1,      after, 0);
        doc.assertText(0, 4, after, 0, 0);
    }

    @Test
    public void editNewlineSplit(){
        ///              012345
        String before = "abcab";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "\n");

        ///             012 3456
        String after = "abc\nab";
        doc.assertDoc(2, after);
        commonUnparsed(doc, "\n");
    }

    @Test
    public void editUserSplit(){
        ///              012345
        String before = "abcab";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "}");

        ///             0123456
        String after = "abc}ab";
        doc.assertDoc(2, after);
        commonUnparsed(doc, "}");
    }
}
