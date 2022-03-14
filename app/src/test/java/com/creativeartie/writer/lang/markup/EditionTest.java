package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.BranchBasicAsserts.*;

import static com.creativeartie.writer.lang.DocumentAssert.*;
public class EditionTest {

    private static final SetupParser[] parsers = EditionParser.values();

    @Test
    public void stubBasic(){
        ///           012345
        String raw = "#STUB";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.STUB)
            .setDetail("");

        edition.test(1,     raw, 0);
        doc.assertKey(0, 5, raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void stubLowercase(){
        ///           012345
        String raw = "#Stub";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.OTHER)
            .setDetail("Stub");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("Stub")
            .setEnd(false)  .setCount(1);

        edition.test(2,     raw,    0);
        doc.assertKey( 0, 1, "#",    0, 0);
        content.test(1,     "Stub", 0, 1);
        doc.assertText(1, 5, "Stub", 0, 1, 0);
        doc.assertRest();
    }

    @Test
    public void basicDraft(){
        ///           0123456
        String raw = "#DRAFT";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.DRAFT)
            .setDetail("");

        edition.test(1,     raw, 0);
        doc.assertKey(0, 6, raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void basicFinal(){
        ///           0123456
        String raw = "#FINAL";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.FINAL)
            .setDetail("");

        edition.test(1,     raw, 0);
        doc.assertKey(0, 6, raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void basicOther(){
        ///           01
        String raw = "#";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.OTHER)
            .setDetail("");

        edition.test(1,     raw, 0);
        doc.assertKey(0, 1, raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void basicOtherDetail(){
        ///           01234
        String raw = "#abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.OTHER)
            .setDetail("abc");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        edition.test(2,      raw,    0);
        doc.assertKey(0, 1,  "#",    0, 0);
        content.test(1,      "abc",  0, 1);
        doc.assertText(1, 4, "abc",  0, 1, 0);
        doc.assertRest();
    }

    @Test
    public void basicFinalEscaped(){
        ///           0 1234567
        String raw = "#\\FINAL";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.OTHER)
            .setDetail("FINAL");
        ContentAssert content = new ContentAssert(doc)
            .setBoth("FINAL").setBegin(false)
            .setEnd(false)   .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc).setEscape("F");

        edition.test(2,      raw,       0);
        doc.assertKey(0, 1,  "#",       0, 0);
        content.test(2,      "\\FINAL", 0, 1);
        escape.test(2,       "\\F",     0, 1, 0);
        doc.assertKey(1, 2,  "\\",      0, 1, 0, 0);
        doc.assertText(2, 3, "F",       0, 1, 0, 1);
        doc.assertText(3, 7, "INAL",    0, 1, 1);
        doc.assertRest();
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
        ///            01234567890123456
        String after = "#FINAL version 8";
        doc.assertDoc(1, after);
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
        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.FINAL)
            .setDetail("version 8");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(true).setBoth(" version 8")
            .setEnd(false) .setCount(2);

        edition.test(2,       after,       0);
        doc.assertKey( 0, 6,  "#FINAL",     0, 0);
        content.test(1,       " version 8", 0, 1);
        doc.assertText(6, 16, " version 8", 0, 1, 0);
        doc.assertRest();
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

        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.FINAL)
            .setDetail("k");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(true).setBoth(" k")
            .setEnd(false) .setCount(1);

        edition.test(2,      "#FINAL k", 0);
        doc.assertKey(0, 6,  "#FINAL",   0, 0);
        content.test(1,      " k",       0, 1);
        doc.assertText(6, 8, " k",       0, 1, 0);
        doc.assertRest("\nsplit");
    }
}
