package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchFormatAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchLineAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
public class LinedLevelHeadTest {

    public static IDBuilder buildId(String name){
        return new IDBuilder().addCategory("link").setId(name);
    }

    private LinedParseLevel[] parsers = LinedParseLevel.values();

    @Test
    public void basicHeading(){
        String raw = "===@sub-id:W_Under_#abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        IDBuilder builder = doc.addId(buildId("id").addCategory("sub"), 0);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
            .setFormattedSpan(0, 4).setLookup("<@sub-id>")
            .setLevel(3).setEdition(EditionType.OTHER)
            .setPublish(1).setNote(0).setDetail("abc")
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("sub-id");
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("WUnder");
        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.OTHER)
            .setDetail("abc");

        heading.test(6,        raw,       0);
        doc.assertKey(  0,  3, "===",     0, 0);
        doc.assertKey(  3,  4, "@",       0, 1);
        id.test(3,             "sub-id",  0, 2);
        doc.assertId(   4,  7, "sub",     0, 2, 0, 0);
        doc.assertKey(  7,  8, "-",       0, 2, 1);
        doc.assertChild(1,     "id",      0, 2, 2);
        doc.assertId(   8, 10, "id",      0, 2, 2, 0);
        doc.assertKey( 10, 11, ":",       0, 3);
        main.test(4,           "W_Under_", 0, 4);
        doc.assertChild(1,     "W",        0, 4, 0);
        doc.assertText(11, 12, "W",       0, 4, 0, 0);
        doc.assertKey( 12, 13, "_",       0, 4, 1);
        doc.assertChild(1,     "Under",   0, 4, 2);
        doc.assertText(13, 18, "Under",   0, 4, 2, 0);
        doc.assertKey( 18, 19, "_",       0, 4, 3);
        edition.test(2,        "#abc",     0, 5);
        doc.assertKey( 19, 20, "#",       0, 5, 0);
        doc.assertChild(1,     "abc",     0, 5, 1);
        doc.assertText(20, 23, "abc",     0, 5, 1, 0);

    }

    @Test
    public void basicOutline(){
        String raw = "!#@id:b {!todo   } #DRAFT #1";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addId(buildId("id"), 1);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
            .setFormattedSpan(0, 4).setHeading(false)
            .setLevel(1).setEdition(EditionType.DRAFT).setDetail("#1")
            .setPublish(0).setNote(2).setLookup("<@id>")
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        doc.addId(FormatAgendaTest.buildId("08"), 0);

        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("id");
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(1).setParsed("b");
        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.DRAFT)
            .setDetail("#1");

        heading.test(6,        raw,             0);
        doc.assertKey(  0,  2, "!#",            0, 0);
        doc.assertKey(  2,  3, "@",             0, 1);
        id.test(1,             "id",            0, 2);
        doc.assertChild(1,     "id",            0, 2, 0);
        doc.assertId(   3,  5, "id",            0, 2, 0, 0);
        doc.assertKey(  5,  6, ":",             0, 3);
        main.test(3,           "b {!todo   } ", 0, 4);
        doc.assertChild(1,     "b ",            0, 4, 0);
        doc.assertText( 6,  8, "b ",            0, 4, 0, 0);
        doc.assertChild(3,     "{!todo   }",    0, 4, 1);
        doc.assertKey(  8, 10, "{!",            0, 4, 1, 0);
        doc.assertChild(1,     "todo   ",       0, 4, 1, 1);
        doc.assertText(10, 17, "todo   ",       0, 4, 1, 1, 0);
        doc.assertKey( 17, 18, "}",             0, 4, 1, 2);
        doc.assertChild(1,     " ",             0, 4, 2);
        doc.assertText(18, 19, " ",             0, 4, 2, 0);
        edition.test(2,        "#DRAFT #1",     0, 5);
        doc.assertKey( 19, 25, "#DRAFT",        0, 5, 0);
        doc.assertChild(1,     " #1",           0, 5, 1);
        doc.assertText(25, 28, " #1",           0, 5, 1, 0);

    }

    @Test
    public void basicTripleHeading(){
        String raw = "=abc\n=Chapter 2\n=Chapter 3";
        DocumentAssert doc = assertDoc(3, raw, parsers);

        HeadLevelLineAssert heading1 = new HeadLevelLineAssert(doc)
            .setFormattedSpan(0, 1).setHeading(true)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublish(1).setNote(0);
        FormattedSpanAssert content1 = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("abc");

        HeadLevelLineAssert heading2 = new HeadLevelLineAssert(doc)
            .setLevel(1)       .setFormattedSpan(1, 1)
            .setPublish(2).setHeading(true)
            .setNote(0)   .setEdition(EditionType.NONE);
        FormattedSpanAssert content2 = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(0).setParsed("Chapter 2");

        HeadLevelLineAssert heading3 = new HeadLevelLineAssert(doc)
           .setFormattedSpan(2, 1).setHeading(true)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublish(2).setNote(0);
        FormattedSpanAssert content3 = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(0).setParsed("Chapter 3");

        heading1.test(3,       "=abc\n",       0);
        doc.assertKey(  0, 1, "=",             0, 0);
        content1.test(1,      "abc",           0, 1);
        doc.assertChild(1,    "abc",           0, 1, 0);
        doc.assertText( 1, 4, "abc",           0, 1, 0, 0);
        doc.assertKey(  4, 5, "\n",            0, 2);
        heading2.test(3,       "=Chapter 2\n", 1);
        doc.assertKey(  5, 6, "=",             1, 0);
        content2.test(1,       "Chapter 2",    1, 1);
        doc.assertChild(1,     "Chapter 2",    1, 1, 0);
        doc.assertText( 6, 15, "Chapter 2",    1, 1, 0, 0);
        doc.assertKey( 15, 16, "\n",           1, 2);
        heading3.test(2,       "=Chapter 3",   2);
        doc.assertKey( 16, 17, "=",            2, 0);
        content3.test(1,       "Chapter 3",    2, 1);
        doc.assertChild(1,     "Chapter 3",    2, 1, 0);
        doc.assertText(17, 26, "Chapter 3",    2, 1, 0, 0);
        doc.assertRest();
    }

    @Test
    public void basicSpacedHeading(){
        String raw = "===   @id:Title #abc";

        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addId(buildId("id"), 0);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
           .setFormattedSpan(0, 4).setHeading(true)
            .setLevel(3).setEdition(EditionType.OTHER).setDetail("abc")
            .setPublish(1).setNote(0).setLookup("<@id>")
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("id");
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("Title");
        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.OTHER)
            .setDetail("abc");

        heading.test(6,        raw,      0);
        doc.assertKey(  0,  3, "===",    0, 0);
        doc.assertKey(  3,  7, "   @",   0, 1);
        id.test(1,             "id",     0, 2);
        doc.assertChild(1,     "id",     0, 2, 0);
        doc.assertId(   7,  9, "id",     0, 2, 0, 0);
        doc.assertKey(  9, 10, ":",      0, 3);
        main.test(1,           "Title ", 0, 4);
        doc.assertChild(1,     "Title ", 0, 4, 0);
        doc.assertText(10, 16, "Title ", 0, 4, 0, 0);
        edition.test(2,        "#abc",   0, 5);
        doc.assertKey( 16, 17, "#",      0, 5, 0);
        doc.assertChild(1,     "abc",    0, 5, 1);
        doc.assertText(17, 20, "abc",    0, 5, 1, 0);
        doc.assertRest();
    }

    @Test
    public void missingStatus(){
        String raw = "===@id:Title";

        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addId(buildId("id"), 0);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
            .setFormattedSpan(0, 4).setHeading(true)
            .setLevel(3).setEdition(EditionType.NONE)
            .setPublish(1).setNote(0).setLookup("<@id>")
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("id");
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("Title");

        heading.test(5,       raw,     0);
        doc.assertKey( 0,  3, "===",   0, 0);
        doc.assertKey( 3,  4, "@",     0, 1);
        id.test(1,            "id",    0, 2);
        doc.assertChild(1,    "id",    0, 2, 0);
        doc.assertId(  4,  6, "id",    0, 2, 0, 0);
        doc.assertKey( 6,  7, ":",     0, 3);
        main.test(1,          "Title", 0, 4);
        doc.assertChild(1,    "Title", 0, 4, 0);
        doc.assertText(7, 12, "Title", 0, 4, 0, 0);
        doc.assertRest();
    }

    @Test
    public void missingTitle(){
        String raw = "===@id:#abc";

        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addId(buildId("id"), 0);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
           .setLevel(3).setEdition(EditionType.OTHER).setDetail("abc")
           .setPublish(0).setNote(0).setLookup("<@id>")
           .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("id");
        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.OTHER)
            .setDetail("abc");

        heading.test(5,       raw,    0);
        doc.assertKey( 0,  3, "===",  0, 0);
        doc.assertKey( 3,  4, "@",    0, 1);
        id.test(1,            "id",   0, 2);
        doc.assertChild(1,    "id",   0, 2, 0);
        doc.assertId(  4,  6, "id",   0, 2, 0, 0);
        doc.assertKey( 6,  7, ":",    0, 3);
        edition.test(2,       "#abc", 0, 4);
        doc.assertKey( 7,  8, "#",    0, 4, 0);
        doc.assertText(8, 11, "abc",  0, 4, 1, 0);
        doc.assertRest();
    }
    @Test
    public void missingIdColon(){
        String raw = "===@id";

        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addId(buildId("id"), 0);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
            .setLevel(3).setEdition(EditionType.NONE)
            .setPublish(0).setNote(0).setLookup("<@id>")
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder).setLookup("id");


        heading.test(3,      raw,   0);
        doc.assertKey(0,  3, "===", 0, 0);
        doc.assertKey(3,  4, "@",   0, 1);
        id.test(1,           "id",  0, 2);
        doc.assertChild(1,   "id",  0, 2, 0);
        doc.assertId( 4,  6, "id",  0, 2, 0, 0);
        doc.assertRest();
    }

    @Test
    public void misingId(){
        String raw = "===Title#abc";

        DocumentAssert doc = assertDoc(1, raw, parsers);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
           .setFormattedSpan(0, 1).setHeading(true)
            .setLevel(3).setEdition(EditionType.OTHER).setDetail("abc")
            .setPublish(1).setNote(0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("Title");
        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.OTHER)
            .setDetail("abc");

        heading.test(3,       raw,     0);
        doc.assertKey( 0,  3, "===",   0, 0);
        main.test(1,          "Title", 0, 1);
        doc.assertChild(       1, "Title", 0, 1, 0);
        doc.assertText(3,  8, "Title", 0, 1, 0, 0);
        edition.test(2,       "#abc",  0, 2);
        doc.assertKey( 8,  9, "#",     0, 2, 0);
        doc.assertChild(1,    "abc",   0, 2, 1);
        doc.assertText(9, 12, "abc",   0, 2, 1, 0);
        doc.assertRest();
    }

    @Test
    public void missingIdTitle(){
        String raw = "===#abc";

        DocumentAssert doc = assertDoc(1, raw, parsers);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
            .setLevel(3).setEdition(EditionType.OTHER).setDetail("abc")
            .setPublish(0).setNote(0);
        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.OTHER)
            .setDetail("abc");

        heading.test(2,      raw,     0);
        doc.assertKey( 0, 3, "===",   0, 0);
        edition.test(2,      "#abc", 0, 1);
        doc.assertKey( 3, 4, "#",     0, 1, 0);
        doc.assertText(4, 7, "abc",   0, 1, 1, 0);
        doc.assertRest();
    }

    @Test
    public void levelHeading6(){
        String raw = "======abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
           .setFormattedSpan(0, 1).setHeading(true)
            .setLevel(6).setEdition(EditionType.NONE)
            .setPublish(1).setNote(0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("abc");

        heading.test(3,       raw,      0);
        doc.assertKey( 0,  6, "======", 0, 0);
        main.test(1,          "abc",    0, 1);
        doc.assertChild(1,    "abc",    0, 1, 0);
        doc.assertText(6,  9, "abc",    0, 1, 0, 0);
        doc.assertKey( 9, 10, "\n",     0, 2);
        doc.assertRest();
    }

    @Test
    public void levelHeading1(){
        String raw = "=abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
            .setFormattedSpan(0, 1).setHeading(true)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublish(1).setNote(0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("abc");

        heading.test(3,      raw,   0);
        doc.assertKey( 0, 1, "=",   0, 0);
        main.test(1,         "abc", 0, 1);
        doc.assertChild(1,   "abc", 0, 1, 0);
        doc.assertText(1, 4, "abc", 0, 1, 0, 0);
        doc.assertKey( 4, 5, "\n",  0, 2);
        doc.assertRest();
    }

    @Test
    public void editHeadingLevel(){
        String before = "===abc#DRAFT text\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(0, 1);
        commonHeading(doc, true);
    }

    @Test
    public void editContent(){
        ///              012345678901234567
        String before = "==ac#DRAFT text\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "b", 0);
        commonHeading(doc, true);
    }

    @Test
    public void editLevelByContent(){
        ///              012345678901234567
        String before = "=abc#DRAFT text\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(1, "=", 0);
        commonHeading(doc, true);
    }

    @Test
    public void editOutlineLevel(){
        String before = "!##abc#DRAFT text\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(1, 2);
        commonHeading(doc, false);
    }

    @Test
    public void editEdition(){
        ///              01234567890123
        String before = "==abc#DRAFT t\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(12, "tex", 0, 2);
        commonHeading(doc, true);
    }

    private void commonHeading(DocumentAssert doc, boolean head){
        String starter;
        int level, publish, note;
        if (head){
            starter = "==";
            level = 2;
            publish = 1;
            note = 0;
        } else {
            starter = "!#";
            level = 1;
            publish = 0;
            note = 1;
        }
        ///             01        23456789012345 6
        String after = starter + "abc#DRAFT text\n";
        doc.assertDoc(1, after);

        HeadLevelLineAssert heading = new HeadLevelLineAssert(doc)
            .setFormattedSpan(0, 1).setHeading(head)
            .setLevel(level).setEdition(EditionType.DRAFT).setDetail("text")
            .setPublish(publish).setNote(note);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("abc");
        EditionAssert edition = new EditionAssert(doc)
            .setEdition(EditionType.DRAFT)
            .setDetail("text");

        heading.test(4,        after,          0);
        doc.assertKey( 0,   2, starter,        0, 0);
        main.test(1,           "abc",          0, 1);
        doc.assertChild(1,     "abc",          0, 1, 0);
        doc.assertText(2,   5, "abc",          0, 1, 0, 0);
        edition.test(2,        "#DRAFT text",  0, 2);
        doc.assertKey( 5,  11, "#DRAFT",       0, 2, 0);
        doc.assertChild(1,     " text",        0, 2, 1);
        doc.assertText(11, 16, " text",        0, 2, 1, 0);
        doc.assertKey( 16, 17, "\n",           0, 3);
        doc.assertRest();
    }
}
