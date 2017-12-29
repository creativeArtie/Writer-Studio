package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class LinedLevelHeadDebug {

    public static IDBuilder buildId(String name){
        return new IDBuilder().addCategory("link").setId(name);
    }

    private LinedParseLevel[] parsers = LinedParseLevel.values();

    @Test
    public void basicHeading(){
        String raw = "===@sub-id:W_Under_#abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        IDBuilder builder = buildId("id").addCategory("sub");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
            .setFormattedSpan(doc, 0, 4).setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.OTHER)
            .setPublishTotal(1).setNoteTotal(0)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.OTHER)
            .setText("abc");

        heading.test(     doc,  6, raw,       0);
        doc.assertKeyLeaf(  0,  3, "===",     0, 0);
        doc.assertKeyLeaf(  3,  4, "@",       0, 1);
        id.test(          doc,  3, "sub-id",  0, 2);
        doc.assertIdLeaf(   4,  7, "sub",     0, 2, 0, 0);
        doc.assertKeyLeaf(  7,  8, "-",       0, 2, 1);
        doc.assertChild(        1, "id",      0, 2, 2);
        doc.assertIdLeaf(   8, 10, "id",      0, 2, 2, 0);
        doc.assertKeyLeaf( 10, 11, ":",       0, 3);
        main.test(        doc, 4, "W_Under_", 0, 4);
        doc.assertChild(       1, "W",        0, 4, 0);
        doc.assertTextLeaf(11, 12, "W",       0, 4, 0, 0);
        doc.assertKeyLeaf( 12, 13, "_",       0, 4, 1);
        doc.assertChild(        1, "Under",   0, 4, 2);
        doc.assertTextLeaf(13, 18, "Under",   0, 4, 2, 0);
        doc.assertKeyLeaf( 18, 19, "_",       0, 4, 3);
        edition.test(     doc, 2, "#abc",     0, 5);
        doc.assertKeyLeaf( 19, 20, "#",       0, 5, 0);
        doc.assertChild(        1, "abc",     0, 5, 1);
        doc.assertTextLeaf(20, 23, "abc",     0, 5, 1, 0);

        doc.assertIds();
    }

    @Test
    public void basicOutline(){
        String raw = "!#@id:b {!todo   } #DRAFT #1";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 1);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 4).setLinedType(LinedType.OUTLINE)
            .setLevel(1).setEdition(EditionType.DRAFT)
            .setPublishTotal(0).setNoteTotal(2)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        doc.addId(FormatAgendaDebug.buildId("08"), 0);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(1);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.DRAFT)
            .setText("#1");

        heading.test(     doc,  6, raw,       0);
        doc.assertKeyLeaf(  0,  2, "!#",      0, 0);
        doc.assertKeyLeaf(  2,  3, "@",       0, 1);
        id.test(          doc,  1, "id",      0, 2);
        doc.assertChild(        1, "id",      0, 2, 0);
        doc.assertIdLeaf(   3,  5, "id",      0, 2, 0, 0);
        doc.assertKeyLeaf(  5,  6, ":",       0, 3);
        main.test(   doc, 3, "b {!todo   } ", 0, 4);
        doc.assertChild(        1, "b ",      0, 4, 0);
        doc.assertTextLeaf( 6,  8, "b ",      0, 4, 0, 0);
        doc.assertChild(     3, "{!todo   }", 0, 4, 1);
        doc.assertKeyLeaf(  8, 10, "{!",      0, 4, 1, 0);
        doc.assertChild(        1, "todo   ", 0, 4, 1, 1);
        doc.assertTextLeaf(10, 17, "todo   ", 0, 4, 1, 1, 0);
        doc.assertKeyLeaf( 17, 18, "}",       0, 4, 1, 2);
        doc.assertChild(        1, " ",       0, 4, 2);
        doc.assertTextLeaf(18, 19, " ",       0, 4, 2, 0);
        edition.test(    doc, 2, "#DRAFT #1", 0, 5);
        doc.assertKeyLeaf( 19, 25, "#DRAFT",  0, 5, 0);
        doc.assertChild(        1, " #1",     0, 5, 1);
        doc.assertTextLeaf(25, 28, " #1",     0, 5, 1, 0);

        doc.assertIds();
    }

    @Test
    public void basicTripleHeading(){
        String raw = "=abc\n=Chapter 2\n=Chapter 3";
        DocumentAssert doc = DocumentAssert.assertDoc(3, raw, parsers);

        HeadLevelLineTest heading1 = new HeadLevelLineTest()
            .setFormattedSpan(doc, 0, 1).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublishTotal(1).setNoteTotal(0)
            .setIsLast(false);
        FormatMainTest content1 = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);

        HeadLevelLineTest heading2 = new HeadLevelLineTest()
           .setFormattedSpan(doc, 1, 1).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublishTotal(2).setNoteTotal(0)
            .setIsLast(false);
        FormatMainTest content2 = new FormatMainTest()
            .setPublishTotal(2).setNoteTotal(0);

        HeadLevelLineTest heading3 = new HeadLevelLineTest()
           .setFormattedSpan(doc, 2, 1).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublishTotal(2).setNoteTotal(0);
        FormatMainTest content3 = new FormatMainTest()
            .setPublishTotal(2).setNoteTotal(0);

        heading1.test(    doc, 3, "=abc\n",       0);
        doc.assertKeyLeaf(  0, 1, "=",            0, 0);
        content1.test(    doc, 1, "abc",          0, 1);
        doc.assertChild(       1, "abc",          0, 1, 0);
        doc.assertTextLeaf( 1, 4, "abc",          0, 1, 0, 0);
        doc.assertKeyLeaf(  4, 5, "\n",           0, 2);
        heading2.test(    doc, 3, "=Chapter 2\n", 1);
        doc.assertKeyLeaf(  5, 6, "=",            1, 0);
        content2.test(    doc, 1, "Chapter 2",    1, 1);
        doc.assertChild(       1, "Chapter 2",    1, 1, 0);
        doc.assertTextLeaf( 6, 15, "Chapter 2",   1, 1, 0, 0);
        doc.assertKeyLeaf( 15, 16, "\n",          1, 2);
        heading3.test(    doc, 2, "=Chapter 3",   2);
        doc.assertKeyLeaf( 16, 17, "=",           2, 0);
        content3.test(    doc, 1, "Chapter 3",    2, 1);
        doc.assertChild(        1, "Chapter 3",   2, 1, 0);
        doc.assertTextLeaf(17, 26, "Chapter 3",   2, 1, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicSpacedHeading(){
        String raw = "===   @id:Title #abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 4).setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.OTHER)
            .setPublishTotal(1).setNoteTotal(0)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.OTHER)
            .setText("abc");

        heading.test(     doc,  6, raw,      0);
        doc.assertKeyLeaf(  0,  3, "===",    0, 0);
        doc.assertKeyLeaf(  3,  7, "   @",   0, 1);
        id.test(          doc,  1, "id",     0, 2);
        doc.assertChild(        1, "id",     0, 2, 0);
        doc.assertIdLeaf(   7,  9, "id",     0, 2, 0, 0);
        doc.assertKeyLeaf(  9, 10, ":",      0, 3);
        main.test(        doc,  1, "Title ", 0, 4);
        doc.assertChild(        1, "Title ", 0, 4, 0);
        doc.assertTextLeaf(10, 16, "Title ", 0, 4, 0, 0);
        edition.test(     doc, 2, "#abc",    0, 5);
        doc.assertKeyLeaf( 16, 17, "#",      0, 5, 0);
        doc.assertChild(        1, "abc",    0, 5, 1);
        doc.assertTextLeaf(17, 20, "abc",    0, 5, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void missingStatus(){
        String raw = "===@id:Title";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 4).setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.NONE)
            .setPublishTotal(1).setNoteTotal(0)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);

        heading.test(    doc,  5, raw,     0);
        doc.assertKeyLeaf( 0,  3, "===",   0, 0);
        doc.assertKeyLeaf( 3,  4, "@",     0, 1);
        id.test(          doc, 1, "id",    0, 2);
        doc.assertChild(       1, "id",    0, 2, 0);
        doc.assertIdLeaf(  4,  6, "id",    0, 2, 0, 0);
        doc.assertKeyLeaf( 6,  7, ":",     0, 3);
        main.test(        doc, 1, "Title", 0, 4);
        doc.assertChild(       1, "Title", 0, 4, 0);
        doc.assertTextLeaf(7, 12, "Title", 0, 4, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void missingTitle(){
        String raw = "===@id:#abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setLinedType(LinedType.HEADING)
           .setLevel(3).setEdition(EditionType.OTHER)
           .setPublishTotal(0).setNoteTotal(0)
           .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.OTHER)
            .setText("abc");

        heading.test(    doc,  5, raw,    0);
        doc.assertKeyLeaf( 0,  3, "===",  0, 0);
        doc.assertKeyLeaf( 3,  4, "@",    0, 1);
        id.test(          doc, 1, "id",   0, 2);
        doc.assertChild(       1, "id",   0, 2, 0);
        doc.assertIdLeaf(  4,  6, "id",   0, 2, 0, 0);
        doc.assertKeyLeaf( 6,  7, ":",    0, 3);
        edition.test(     doc, 2, "#abc", 0, 4);
        doc.assertKeyLeaf( 7,  8, "#",    0, 4, 0);
        doc.assertTextLeaf(8, 11, "abc",  0, 4, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }
    @Test
    public void missingIdColon(){
        String raw = "===@id";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.NONE)
            .setPublishTotal(0).setNoteTotal(0)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);


        heading.test(   doc,  3, raw,   0);
        doc.assertKeyLeaf(0,  3, "===", 0, 0);
        doc.assertKeyLeaf(3,  4, "@",   0, 1);
        id.test(         doc, 1, "id",  0, 2);
        doc.assertChild(      1, "id",  0, 2, 0);
        doc.assertIdLeaf( 4,  6, "id",  0, 2, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void misingId(){
        String raw = "===Title#abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 1).setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.OTHER)
            .setPublishTotal(1).setNoteTotal(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.OTHER)
            .setText("abc");

        heading.test(    doc,  3, raw,     0);
        doc.assertKeyLeaf( 0,  3, "===",   0, 0);
        main.test(        doc, 1, "Title", 0, 1);
        doc.assertChild(       1, "Title", 0, 1, 0);
        doc.assertTextLeaf(3,  8, "Title", 0, 1, 0, 0);
        edition.test(     doc, 2, "#abc",  0, 2);
        doc.assertKeyLeaf( 8,  9, "#",     0, 2, 0);
        doc.assertChild(       1, "abc",   0, 2, 1);
        doc.assertTextLeaf(9, 12, "abc",   0, 2, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void missingIdTitle(){
        String raw = "===#abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.OTHER)
            .setPublishTotal(0).setNoteTotal(0);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.OTHER)
            .setText("abc");

        heading.test(    doc, 2, raw,     0);
        doc.assertKeyLeaf( 0, 3, "===",   0, 0);
        edition.test(     doc, 2, "#abc", 0, 1);
        doc.assertKeyLeaf( 3, 4, "#",     0, 1, 0);
        doc.assertTextLeaf(4, 7, "abc",   0, 1, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void levelHeading6(){
        String raw = "======abc\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 1).setLinedType(LinedType.HEADING)
            .setLevel(6).setEdition(EditionType.NONE)
            .setPublishTotal(1).setNoteTotal(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);

        heading.test(    doc,  3, raw,      0);
        doc.assertKeyLeaf( 0,  6, "======", 0, 0);
        main.test(        doc, 1, "abc",    0, 1);
        doc.assertChild(       1, "abc",    0, 1, 0);
        doc.assertTextLeaf(6,  9, "abc",    0, 1, 0, 0);
        doc.assertKeyLeaf( 9, 10, "\n",     0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void levelHeading1(){
        String raw = "=abc\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 1).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublishTotal(1).setNoteTotal(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);

        heading.test(    doc, 3, raw,   0);
        doc.assertKeyLeaf( 0, 1, "=",   0, 0);
        main.test(       doc, 1, "abc", 0, 1);
        doc.assertChild(      1, "abc", 0, 1, 0);
        doc.assertTextLeaf(1, 4, "abc", 0, 1, 0, 0);
        doc.assertKeyLeaf( 4, 5, "\n",  0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void editHeadingLevel(){
        String before = "===abc#DRAFT text\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, before, parsers);
        doc.delete(0, 1, 0);
        editCommon(doc, LinedType.HEADING);
    }

    @Test
    public void editContent(){
        ///              012345678901234567
        String before = "==ac#DRAFT text\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, before, parsers);
        doc.insert(3, "b", 0);
        editCommon(doc, LinedType.HEADING);
    }

    @Test
    public void editLevelByContent(){
        ///              012345678901234567
        String before = "=abc#DRAFT text\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, before, parsers);
        doc.insert(1, "=", 0);
        editCommon(doc, LinedType.HEADING);
    }

    @Test
    public void editOutlineLevel(){
        String before = "!##abc#DRAFT text\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, before, parsers);
        doc.delete(1, 2, 0);
        editCommon(doc, LinedType.OUTLINE);
    }

    @Test
    public void editEdition(){
        ///              01234567890123
        String before = "==abc#DRAFT t\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, before, parsers);
        doc.insert(12, "tex", 0, 2);
        editCommon(doc, LinedType.HEADING);
    }

    private void editCommon(DocumentAssert doc, LinedType type){
        String starter;
        int level, publish, note;
        if (type == LinedType.HEADING){
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

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 1).setLinedType(type)
            .setLevel(level).setEdition(EditionType.DRAFT)
            .setPublishTotal(publish).setNoteTotal(note);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.DRAFT)
            .setText("text");

        heading.test(    doc,   4, after,          0);
        doc.assertKeyLeaf( 0,   2, starter,        0, 0);
        main.test(       doc,   1, "abc",          0, 1);
        doc.assertChild(        1, "abc",          0, 1, 0);
        doc.assertTextLeaf(2,   5, "abc",          0, 1, 0, 0);
        edition.test(    doc,   2, "#DRAFT text",  0, 2);
        doc.assertKeyLeaf( 5,  11, "#DRAFT",       0, 2, 0);
        doc.assertChild(        1, " text",        0, 2, 1);
        doc.assertTextLeaf(11, 16, " text",        0, 2, 1, 0);
        doc.assertKeyLeaf( 16, 17, "\n",           0, 3);
        doc.assertLast();
        doc.assertIds();
    }
}
