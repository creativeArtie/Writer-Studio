package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;


import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.rules.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.BranchTest.*;

@RunWith(JUnit4.class)
public class ContentDebug{

    private static final SetupParser[] parsers = new SetupParser[]{
        AuxiliaryData.CONTENT_AGENDA};

    @Test
    public void textCount1(){
        ///           012345
        String raw = "Hello";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setBegin(false).setText(raw)
            .setEnd(false)  .setCount(1);

        content.test(    doc, 1,            raw, 0);
        doc.assertTextLeaf(0, raw.length(), raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void textCount2(){
        ///           012345
        String raw = "I see";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setBegin(false).setText(raw)
            .setEnd(false)  .setCount(2);

        content.test(    doc, 1,            raw, 0);
        doc.assertTextLeaf(0, raw.length(), raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void spaceSingle(){
        ///           01
        String raw = " ";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setBegin(true).setText("")
            .setEnd(true)  .setCount(0);

        content.test(    doc, 1,            raw, 0);
        doc.assertTextLeaf(0, raw.length(), raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void spaceDoubled(){
        ///           012
        String raw = "  ";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setBegin(true).setText("")
            .setEnd(true)  .setCount(0);

        content.test(    doc, 1,            raw, 0);
        doc.assertTextLeaf(0, raw.length(), raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void escapeBasic(){
        ///            012
        String raw = "\\b";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setBegin(false).setText("b")
            .setEnd(false)  .setCount(1);
        EscapeTest escape = new EscapeTest()
            .setEscape("b");

        content.test(    doc, 1, raw,  0);
        escape.test(     doc, 2, raw,  0, 0);
        doc.assertKeyLeaf( 0, 1, "\\", 0, 0, 0);
        doc.assertTextLeaf(1, 2, "b",  0, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void escapeMiddle(){
        ///           0 123 45
        String raw = "a\\bc\t";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(true)   .setCount(1);
        EscapeTest escape = new EscapeTest()
            .setEscape("b");

        content.test(    doc, 3, raw,   0);
        doc.assertTextLeaf(0, 1, "a",   0, 0);
        escape.test(     doc, 2, "\\b", 0, 1);
        doc.assertKeyLeaf (1, 2, "\\",  0, 1, 0);
        doc.assertTextLeaf(2, 3, "b",   0, 1, 1);
        doc.assertTextLeaf(3, 5, "c\t", 0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void escapeSlash(){
        ///            0 12
        String raw = "\\\\";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setBegin(false).setText("\\")
            .setEnd(false)  .setCount(1);
        EscapeTest escape = new EscapeTest()
            .setEscape("\\");

        content.test(    doc, 1, raw,  0);
        escape.test(     doc, 2, raw,  0, 0);
        doc.assertKeyLeaf( 0, 1, "\\", 0, 0, 0);
        doc.assertTextLeaf(1, 2, "\\", 0, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void escapeSpace(){
        ///            012
        String raw = "\\ ";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setBegin(true).setText("")
            .setEnd(true)  .setCount(0);
        EscapeTest escape = new EscapeTest()
            .setEscape(" ");

        content.test(    doc, 1, raw,  0);
        escape.test(     doc, 2, raw,  0, 0);
        doc.assertKeyLeaf( 0, 1, "\\", 0, 0, 0);
        doc.assertTextLeaf(1, 2, " ",  0, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void escapeCutOff(){
        ///           01234 56
        String raw = "  abc\\";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setBegin(true).setText("abc")
            .setEnd(false) .setCount(1);
        EscapeTest escape = new EscapeTest()
            .setEscape("");

        content.test(    doc, 2, raw,     0);
        doc.assertTextLeaf(0, 5, "  abc", 0, 0);
        escape.test(     doc, 1, "\\",    0, 1);
        doc.assertKeyLeaf( 5, 6, "\\",    0, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    public void escapeEnder(){
        ///           012 345678
        String raw = "abc\\}abd";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        ContentTest content = new ContentTest()
            .setBegin(false).setText("abc}abd")
            .setEnd(false)  .setCount(1);
        EscapeTest escape = new EscapeTest()
            .setEscape("}");

        content.test(    doc, 3, raw,   0);
        doc.assertTextLeaf(0, 3, "abc", 0, 0);
        escape.test(    doc,  1, "\\}", 0, 1);
        doc.assertKeyLeaf( 3, 4, "\\",  0, 1, 0);
        doc.assertTextLeaf(4, 5, "}",   0, 1, 1);
        doc.assertTextLeaf(5, 8, "abd", 0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void enderNoText(){
        ///           0123456
        String raw = "}abcab";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        doc.assertLast("}abcab");
        doc.assertIds();
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

        ContentTest content = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false) .setCount(1);

        content.test(    doc, 1, "abc", 0);
        doc.assertTextLeaf(0, 3, "abc", 0, 0);
        doc.assertLast(unparsed);
        doc.assertIds();
    }


    @Test
    public void editAddTextSimple(){
        ///              01234
        String before = "hall run";
        DocumentAssert doc = assertDoc(1, before, parsers);
        ///           012345678901
        String after = "hallway run";
        doc.insert(4, "way");
        doc.assertDoc(1, after, parsers);

        ContentTest content = new ContentTest()
            .setText(after) .setBegin(false)
            .setEnd(false).setCount(2);

        content.test(    doc,  1, after, 0);
        doc.assertTextLeaf(0, 11, after, 0, 0);
    }

    @Test
    public void editAddToEnd(){
        ///              01234567
        String before = "hallway";
        DocumentAssert doc = assertDoc(1, before, parsers);
        ///           012345678901
        String after = "hallway run";
        doc.insert(7, " run");
        doc.assertDoc(1, after, parsers);

        ContentTest content = new ContentTest()
            .setText(after) .setBegin(false)
            .setEnd(false).setCount(2);

        content.test(    doc,  1, after, 0);
        doc.assertTextLeaf(0, 11, after, 0, 0);
    }

    @Test
    public void editAddEscape(){
        ///              01234
        String before = "ab*d";
        DocumentAssert doc = assertDoc(1, before, parsers);
        String after = "ab\\*d";
        doc.insert(2, "\\");
        doc.assertDoc(1, after, parsers);

        ContentTest content = new ContentTest()
            .setText("ab*d").setBegin(false)
            .setEnd(false)  .setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("*");

        content.test(    doc, 3, after,   0);
        doc.assertTextLeaf(0, 2, "ab",  0, 0);
        escape.test(     doc, 2, "\\*", 0, 1);
        doc.assertKeyLeaf (2, 3, "\\",  0, 1, 0);
        doc.assertTextLeaf(3, 4, "*",   0, 1, 1);
        doc.assertTextLeaf(4, 5, "d",   0, 2);
    }

    @Test
    public void editRemoveEscape(){
        ///              01234
        String before = "ab\\*d";
        DocumentAssert doc = assertDoc(1, before, parsers);
        String after = "ab*d";
        doc.delete(2, 3);
        doc.assertDoc(1, after, parsers);

        ContentTest content = new ContentTest()
            .setText(after) .setBegin(false)
            .setEnd(false).setCount(1);

        content.test(    doc, 1, after, 0);
        doc.assertTextLeaf(0, 4, after, 0, 0);
    }

    @Test
    public void editNewlineSplit(){
        ///              012345
        String before = "abcab";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "\n");

        ///             012 3456
        String after = "abc\nab";
        doc.assertDoc(2, after, parsers);
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
        doc.assertDoc(2, after, parsers);
        commonUnparsed(doc, "}");
    }
}
