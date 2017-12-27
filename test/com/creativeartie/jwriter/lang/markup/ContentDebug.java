package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;


import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.rules.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;

@RunWith(JUnit4.class)
public class ContentDebug{

    private static final SetupParser[] parsers = new SetupParser[]{
        new ContentParser(";")};

    @Test
    public void basic(){
        ///           012345
        String raw = "Hello";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setText(raw) .setBegin(false)
            .setEnd(false).setCount(1);

        content.test(    doc, 1,            raw, 0);
        doc.assertTextLeaf(0, raw.length(), raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void singleSpace(){
        ///           01
        String raw = " ";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setText("") .setBegin(true)
            .setEnd(true).setCount(0);

        content.test(    doc, 1,            raw, 0);
        doc.assertTextLeaf(0, raw.length(), raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void doubleSpace(){
        ///           012
        String raw = "  ";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ContentTest content = new ContentTest()
            .setText("") .setBegin(true)
            .setEnd(true).setCount(0);

        content.test(    doc, 1,            raw, 0);
        doc.assertTextLeaf(0, raw.length(), raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicEscape(){
        ///           0123
        String raw = "\\b";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        ContentTest content = new ContentTest()
            .setText("b") .setBegin(false)
            .setEnd(false).setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("b");

        content.test(    doc, 1, raw,  0);
        escape.test(     doc, 2, raw,  0, 0);
        doc.assertKeyLeaf( 0, 1, "\\", 0, 0, 0);
        doc.assertTextLeaf(1, 2, "b",  0, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void midEscape(){
        ///           0 123 45
        String raw = "a\\bc\t";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        ContentTest content = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(true)  .setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("b");

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
            .setText("\\").setBegin(false)
            .setEnd(false).setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("\\");

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
            .setText("") .setBegin(true)
            .setEnd(true).setCount(0);
        EscapeTest escape = new EscapeTest().setEscape(" ");

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
            .setText("abc").setBegin(true)
            .setEnd(false) .setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("");

        content.test(    doc, 2, raw,     0);
        doc.assertTextLeaf(0, 5, "  abc", 0, 0);
        escape.test(     doc, 1, "\\",    0, 1);
        doc.assertKeyLeaf( 5, 6, "\\",    0, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void newLineEnded(){
        ///           012 34
        String raw = "abc\n";
        DocumentAssert doc = assertDoc(2, raw, parsers);
        ContentTest content = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false) .setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("");

        content.test(    doc, 1, "abc", 0);
        doc.assertTextLeaf(0, 3, "abc", 0, 0);
        doc.assertLast(    3, 4, "\n");
        doc.assertIds();
    }

    @Test
    public void editAddTextSimple(){
        ///              01234
        String before = "hall run";
        DocumentAssert doc = assertDoc(1, before, parsers);
        ///           012345678901
        String raw = "hallway run";
        doc.insert(4, "way");

        doc.assertDoc(1, raw, parsers);
        ContentTest content = new ContentTest()
            .setText(raw) .setBegin(false)
            .setEnd(false).setCount(2);

        content.test(    doc,  1, raw, 0);
        doc.assertTextLeaf(0, 11, raw, 0, 0);
    }

    @Test
    public void editAddEnd(){
        ///              01234567
        String before = "hallway";
        DocumentAssert doc = assertDoc(1, before, parsers);
        ///           012345678901
        String raw = "hallway run";
        doc.insert(7, " run");

        doc.assertDoc(1, raw, parsers);
        ContentTest content = new ContentTest()
            .setText(raw) .setBegin(false)
            .setEnd(false).setCount(2);

        content.test(    doc,  1, raw, 0);
        doc.assertTextLeaf(0, 11, raw, 0, 0);
    }

    @Test
    public void editWithDiffStyle(){
        ///              01234567
        String before = "hallway";
        DocumentAssert doc = assertDoc(1, before, new ContentParser(
            StyleInfoLeaf.FIELD));
        ///           012345678901
        String raw = "hallway run";
        doc.insert(7, " run");

        doc.assertDoc(1, raw, parsers);
        ContentTest content = new ContentTest()
            .setText(raw) .setBegin(false)
            .setEnd(false).setCount(2);

        content.test(     doc,  1, raw, 0);
        doc.assertFieldLeaf(0, 11, raw, 0, 0);
    }

    @Test
    public void editAddEscape(){
        ///              01234
        String before = "ab*d";
        DocumentAssert doc = assertDoc(1, before, parsers);

        String raw = "ab\\*d";
        doc.insert(2, "\\");

        doc.assertDoc(1, raw, parsers);
        ContentTest content = new ContentTest()
            .setText("ab*d").setBegin(false)
            .setEnd(false)  .setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("*");

        content.test(    doc, 3, raw,   0);
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

        String raw = "ab*d";
        doc.delete(2, 3);

        doc.assertDoc(1, raw, parsers);
        ContentTest content = new ContentTest()
            .setText(raw) .setBegin(false)
            .setEnd(false).setCount(1);

        content.test(    doc, 1, raw, 0);
        doc.assertTextLeaf(0, 4, raw, 0, 0);
    }
}
