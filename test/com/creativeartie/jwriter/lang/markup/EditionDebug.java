package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.util.ArrayList;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class EditionDebug {

    private static final SetupParser[] parsers = EditionParser.values();

    @Test
    public void stubBasic(){
        ///           012345
        String raw = "#STUB";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionTest edition = new EditionTest()
            .setEdition(EditionType.STUB)
            .setText("");

        edition.test(   doc, 1, raw, 0);
        doc.assertKeyLeaf(0, 5, raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicDraft(){
        ///           0123456
        String raw = "#DRAFT";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionTest edition = new EditionTest()
            .setEdition(EditionType.DRAFT)
            .setText("");

        edition.test(   doc, 1, raw, 0);
        doc.assertKeyLeaf(0, 6, raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicFinal(){
        ///           0123456
        String raw = "#FINAL";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionTest edition = new EditionTest()
            .setEdition(EditionType.FINAL)
            .setText("");

        edition.test(   doc, 1, raw, 0);
        doc.assertKeyLeaf(0, 6, raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicOther(){
        ///           01
        String raw = "#";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionTest edition = new EditionTest()
            .setEdition(EditionType.OTHER)
            .setText("");

        edition.test(   doc, 1, raw, 0);
        doc.assertKeyLeaf(0, 1, raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicOtherDetail(){
        ///           01234
        String raw = "#abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionTest edition = new EditionTest()
            .setEdition(EditionType.OTHER)
            .setText("abc");
        ContentTest content = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(false)  .setCount(1);

        edition.test(   doc,  2, raw,    0);
        doc.assertKeyLeaf( 0, 1, "#",    0, 0);
        content.test(    doc, 1, "abc",  0, 1);
        doc.assertTextLeaf(1, 4, "abc",  0, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicFinalEscaped(){
        ///           0 1234567
        String raw = "#\\FINAL";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionTest edition = new EditionTest()
            .setEdition(EditionType.OTHER)
            .setText("FINAL");
        ContentTest content = new ContentTest()
            .setText("FINAL").setBegin(false)
            .setEnd(false)   .setCount(1);
        EscapeTest escape = new EscapeTest().setEscape("F");

        edition.test(    doc, 2, raw,       0);
        doc.assertKeyLeaf( 0, 1, "#",       0, 0);
        content.test(    doc, 2, "\\FINAL", 0, 1);
        escape.test(     doc, 2, "\\F",     0, 1, 0);
        doc.assertKeyLeaf( 1, 2, "\\",      0, 1, 0, 0);
        doc.assertTextLeaf(2, 3, "F",       0, 1, 0, 1);
        doc.assertTextLeaf(3, 7, "INAL",    0, 1, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicFinalDetailed(){
        ///           01234567890123456
        String raw = "#FINAL version 8";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        commonEdited(doc);
    }

    @Test
    public void editText(){
        ///              01234567890123456
        String before = "#FINAL version8";
        DocumentAssert doc = assertDoc(1, before, parsers);

        doc.insert(14, " ", 0);

        commonEdited(doc);
    }

    @Test
    public void editToFinal(){
        String before = "#\\FINAL version 8";
        DocumentAssert doc = assertDoc(1, before, parsers);

        doc.delete(1, 2, 0);

        commonEdited(doc);
    }

    public void commonEdited(DocumentAssert doc){

        ///            01234567890123456
        String after = "#FINAL version 8";
        doc.assertDoc(1,  after);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.FINAL)
            .setText("version 8");
        ContentTest content = new ContentTest()
            .setBegin(true).setText("version 8")
            .setEnd(false) .setCount(2);

        edition.test(     doc, 2,  after,       0);
        doc.assertKeyLeaf(  0, 6, "#FINAL",     0, 0);
        content.test(     doc, 1, " version 8", 0, 1);
        doc.assertTextLeaf(6, 16, " version 8", 0, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void editSplit(){
        ///              01234567890123
        String before = "#FINAL ksplit";
        DocumentAssert doc = assertDoc(1, before, parsers);

        doc.insert(8, "\n");

        ///             012345678 90123456
        String after = "#FINAL k\nsplit";
        doc.assertDoc(2,  after);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.FINAL)
            .setText("k");
        ContentTest content = new ContentTest()
            .setBegin(true).setText("k")
            .setEnd(false) .setCount(1);

        edition.test(     doc, 2, "#FINAL k", 0);
        doc.assertKeyLeaf(  0, 6, "#FINAL",   0, 0);
        content.test(     doc, 1, " k",       0, 1);
        doc.assertTextLeaf( 6, 8,  " k",       0, 1, 0);
        doc.assertLast("\nsplit");
        doc.assertIds();
    }
}
