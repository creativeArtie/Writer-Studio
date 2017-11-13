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

    public static void assertHeading(SpanBranch span, Span text, LinedType lined,
        int level, IDBuilder produces, EditionType edition, int publish,
        int note, CatalogueStatus status)
    {
        LinedSpanSection test = assertClass(span, LinedSpanSection.class);

        assertEquals(getError("edition", span),  edition, test.getEdition());
        LinedLevelRestDebug.assertLevel(test, lined, level, text, status);
        LinedRestDebug.assertLine(test, publish, note);
        assertSpanIdentity(test, produces);
    }

    private LinedParseLevel[] parsers = LinedParseLevel.values();

    @Test
    public void heading(){
        String raw = "===@sub-id:W_Under_#abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        IDBuilder builder = buildId("id").addCategory("sub");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
            .setFormattedSpan(doc, 0, 4).setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.OTHER)
            .setPublishCount(1).setNoteCount(0)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);
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
    public void outline(){
        String raw = "!#@id:b {!todo   } #DRAFT #1";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 1);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 4).setLinedType(LinedType.OUTLINE)
            .setLevel(1).setEdition(EditionType.DRAFT)
            .setPublishCount(0).setNoteCount(2)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        doc.addId(FormatAgendaDebug.buildId("08"), 0);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(1);
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
    public void tripleHeading(){
        String raw = "=abc\n=Chapter 2\n=Chapter 3";
        DocumentAssert doc = DocumentAssert.assertDoc(3, raw, parsers);

        HeadLevelLineTest heading1 = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 1).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublishCount(1).setNoteCount(0);
        FormatMainTest content1 = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);

        HeadLevelLineTest heading2 = new HeadLevelLineTest()
           .setFormattedSpan(doc, 1, 1).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublishCount(2).setNoteCount(0);
        FormatMainTest content2 = new FormatMainTest()
            .setPublishCount(2).setNoteCount(0);

        HeadLevelLineTest heading3 = new HeadLevelLineTest()
           .setFormattedSpan(doc, 2, 1).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublishCount(2).setNoteCount(0);
        FormatMainTest content3 = new FormatMainTest()
            .setPublishCount(2).setNoteCount(0);

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

        doc.assertIds();
    }

    @Test
    public void spacedDirectoryFullHeading(){
        String raw = "===   @id:Title #abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 4).setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.OTHER)
            .setPublishCount(1).setNoteCount(0)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);
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

        doc.assertIds();
    }

    @Test
    public void noStatusHeading(){
        String raw = "===@id:Title";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 4).setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.NONE)
            .setPublishCount(1).setNoteCount(0)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(builder);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);

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

        doc.assertIds();
    }

    @Test
    public void nonSpacedDirectoryTitlelessHeading(){
        String raw = "===@id:#abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setLinedType(LinedType.HEADING)
           .setLevel(3).setEdition(EditionType.OTHER)
           .setPublishCount(0).setNoteCount(0)
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

        doc.assertIds();
    }
    @Test
    public void partlyDirectoryHeading(){
        String raw = "===@id";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.NONE)
            .setPublishCount(0).setNoteCount(0)
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

        doc.assertIds();
    }

    @Test
    public void idLessHeading(){
        String raw = "===Title#abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 1).setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.OTHER)
            .setPublishCount(1).setNoteCount(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);
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

        doc.assertIds();
    }

    @Test
    public void statusOnlyHeading(){
        String raw = "===#abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setLinedType(LinedType.HEADING)
            .setLevel(3).setEdition(EditionType.OTHER)
            .setPublishCount(0).setNoteCount(0);
        EditionTest edition = new EditionTest()
            .setEdition(EditionType.OTHER)
            .setText("abc");

        heading.test(    doc, 2, raw,     0);
        doc.assertKeyLeaf( 0, 3, "===",   0, 0);
        edition.test(     doc, 2, "#abc", 0, 1);
        doc.assertKeyLeaf( 3, 4, "#",     0, 1, 0);
        doc.assertTextLeaf(4, 7, "abc",   0, 1, 1, 0);

        doc.assertIds();
    }

    @Test
    public void heading6(){
        String raw = "======abc\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 1).setLinedType(LinedType.HEADING)
            .setLevel(6).setEdition(EditionType.NONE)
            .setPublishCount(1).setNoteCount(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);

        heading.test(    doc,  3, raw,      0);
        doc.assertKeyLeaf( 0,  6, "======", 0, 0);
        main.test(        doc, 1, "abc",    0, 1);
        doc.assertChild(       1, "abc",    0, 1, 0);
        doc.assertTextLeaf(6,  9, "abc",    0, 1, 0, 0);
        doc.assertKeyLeaf( 9, 10, "\n",     0, 2);

        doc.assertIds();
    }

    @Test
    public void heading1(){
        String raw = "=abc\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);

        HeadLevelLineTest heading = new HeadLevelLineTest()
           .setFormattedSpan(doc, 0, 1).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.NONE)
            .setPublishCount(1).setNoteCount(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);

        heading.test(    doc, 3, raw,   0);
        doc.assertKeyLeaf( 0, 1, "=",   0, 0);
        main.test(       doc, 1, "abc", 0, 1);
        doc.assertChild(      1, "abc", 0, 1, 0);
        doc.assertTextLeaf(1, 4, "abc", 0, 1, 0, 0);
        doc.assertKeyLeaf( 4, 5, "\n",  0, 2);

        doc.assertIds();
    }
}
