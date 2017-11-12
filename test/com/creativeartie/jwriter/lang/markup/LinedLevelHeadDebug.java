package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;

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
        SpanBranch heading = doc.assertChild(6, raw,        0);
        SpanBranch id      = doc.assertChild(3, "sub-id",   0, 2);
        SpanBranch text    = doc.assertChild(4, "W_Under_", 0, 4);
        SpanBranch edition = doc.assertChild(2, "#abc",     0, 5);

        IDBuilder builder = buildId("id").addCategory("sub");
        doc.addId(builder, 0);
        doc.assertIds();

        assertHeading(heading, text, LinedType.HEADING, 3,
            builder, EditionType.OTHER, 1, 0, CatalogueStatus.UNUSED);

        DirectoryDebug.assertId(id, DirectoryType.LINK, builder);
        FormatSpanDebug.assertMain(text, 1, 0);
        EditionDebug.assertEdition(edition, EditionType.OTHER, "abc");

        doc.assertKeyLeaf(  0,  3, "===",   0, 0);
        doc.assertKeyLeaf(  3,  4, "@",     0, 1);
        doc.assertIdLeaf(   4,  7, "sub",   0, 2, 0, 0);
        doc.assertKeyLeaf(  7,  8, "-",     0, 2, 1);
        doc.assertIdLeaf(   8, 10, "id",    0, 2, 2, 0);
        doc.assertKeyLeaf( 10, 11, ":",     0, 3);
        doc.assertTextLeaf(11, 12, "W",     0, 4, 0, 0);
        doc.assertKeyLeaf( 12, 13, "_",     0, 4, 1);
        doc.assertTextLeaf(13, 18, "Under", 0, 4, 2, 0);
        doc.assertKeyLeaf( 18, 19, "_",     0, 4, 3);
        doc.assertKeyLeaf( 19, 20, "#",     0, 5, 0);
        doc.assertTextLeaf(20, 23, "abc",   0, 5, 1, 0);
    }

    @Test
    public void outline(){
        String raw = "!#@id:b {!todo   } #DRAFT #1";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        SpanBranch heading = doc.assertChild(6, raw,             0);
        SpanBranch id      = doc.assertChild(1, "id",            0, 2);
        SpanBranch text    = doc.assertChild(3, "b {!todo   } ", 0, 4);
        SpanBranch edition = doc.assertChild(2, "#DRAFT #1",     0, 5);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 1);

        assertHeading(heading, text, LinedType.OUTLINE, 1,
            builder, EditionType.DRAFT, 0, 2, CatalogueStatus.UNUSED);

        doc.addId(FormatAgendaDebug.buildId("08"), 0);
        doc.assertIds();

        DirectoryDebug.assertId(id, DirectoryType.LINK, builder);
        FormatSpanDebug.assertMain(text, 1, 1);
        EditionDebug.assertEdition(edition, EditionType.DRAFT, "#1");

        doc.assertKeyLeaf(  0,  2, "!#",      0, 0);
        doc.assertKeyLeaf(  2,  3, "@",       0, 1);
        doc.assertIdLeaf(   3,  5, "id",      0, 2, 0, 0);
        doc.assertKeyLeaf(  5,  6, ":",       0, 3);
        doc.assertTextLeaf( 6,  8, "b ",      0, 4, 0, 0);
        doc.assertKeyLeaf(  8, 10, "{!",      0, 4, 1, 0);
        doc.assertTextLeaf(10, 17, "todo   ", 0, 4, 1, 1, 0);
        doc.assertKeyLeaf( 17, 18, "}",       0, 4, 1, 2);
        doc.assertTextLeaf(18, 19, " ",       0, 4, 2, 0);
        doc.assertKeyLeaf( 19, 25, "#DRAFT",  0, 5, 0);
        doc.assertTextLeaf(25, 28, " #1",     0, 5, 1, 0);
    }

    @Test
    public void tripleHeading(){
        String raw = "=abc\n=Chapter 2\n=Chapter 3";
        DocumentAssert doc = DocumentAssert.assertDoc(3, raw, parsers);
        SpanBranch heading1 = doc.assertChild(3, "=abc\n",       0);
        SpanBranch content1 = doc.assertChild(1, "abc",          0, 1);
        SpanBranch heading2 = doc.assertChild(3, "=Chapter 2\n", 1);
        SpanBranch content2 = doc.assertChild(1, "Chapter 2",    1, 1);
        SpanBranch heading3 = doc.assertChild(2, "=Chapter 3",   2);
        SpanBranch content3 = doc.assertChild(1, "Chapter 3",    2, 1);

        assertHeading(heading1, content1, LinedType.HEADING, 1,
            null, EditionType.NONE, 1, 0, CatalogueStatus.NO_ID);
        FormatSpanDebug.assertMain(content1, 1, 0);

        assertHeading(heading2, content2, LinedType.HEADING, 1,
            null, EditionType.NONE, 2, 0, CatalogueStatus.NO_ID);
        FormatSpanDebug.assertMain(content2, 2, 0);

        doc.assertIds();

        assertHeading(heading3, content3, LinedType.HEADING, 1,
            null, EditionType.NONE, 2, 0, CatalogueStatus.NO_ID);
        FormatSpanDebug.assertMain(content3, 2, 0);

        doc.assertKeyLeaf(  0, 1, "=",          0, 0);
        doc.assertTextLeaf( 1, 4, "abc",        0, 1, 0, 0);
        doc.assertKeyLeaf(  4, 5, "\n",         0, 2);
        doc.assertKeyLeaf(  5, 6, "=",          1, 0);
        doc.assertTextLeaf( 6, 15, "Chapter 2", 1, 1, 0, 0);
        doc.assertKeyLeaf( 15, 16, "\n",        1, 2);
        doc.assertKeyLeaf( 16, 17, "=",         2, 0);
        doc.assertTextLeaf(17, 26, "Chapter 3", 2, 1, 0, 0);
    }

    @Test
    public void spacedDirectoryFullHeading(){
        String raw = "===   @id:Title #abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        SpanBranch heading = doc.assertChild(6, raw,      0);
        SpanBranch id      = doc.assertChild(1, "id",     0, 2);
        SpanBranch text    = doc.assertChild(1, "Title ", 0, 4);
        SpanBranch edition = doc.assertChild(2, "#abc",   0, 5);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);
        doc.assertIds();

        assertHeading(heading, text, LinedType.HEADING, 3,
            builder, EditionType.OTHER, 1, 0, CatalogueStatus.UNUSED);

        DirectoryDebug.assertId(id, DirectoryType.LINK, builder);
        FormatSpanDebug.assertMain(text, 1, 0);
        EditionDebug.assertEdition(edition, EditionType.OTHER, "abc");

        doc.assertKeyLeaf(  0, 3, "===",     0, 0);
        doc.assertKeyLeaf(  3, 7, "   @",    0, 1);
        doc.assertIdLeaf(   7, 9, "id",      0, 2, 0, 0);
        doc.assertKeyLeaf(  9, 10, ":",      0, 3);
        doc.assertTextLeaf(10, 16, "Title ", 0, 4, 0, 0);
        doc.assertKeyLeaf( 16, 17, "#",      0, 5, 0);
        doc.assertTextLeaf(17, 20, "abc",    0, 5, 1, 0);
    }

    @Test
    public void noStatusHeading(){
        String raw = "===@id:Title";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        SpanBranch heading = doc.assertChild(5, raw,     0);
        SpanBranch id      = doc.assertChild(1, "id",    0, 2);
        SpanBranch text    = doc.assertChild(1, "Title", 0, 4);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);

        assertHeading(heading, text, LinedType.HEADING, 3,
            builder, EditionType.NONE, 1, 0, CatalogueStatus.UNUSED);

        doc.assertIds();

        DirectoryDebug.assertId(id, DirectoryType.LINK, builder);
        FormatSpanDebug.assertMain(text, 1, 0);

        doc.assertKeyLeaf( 0,  3, "===",   0, 0);
        doc.assertKeyLeaf( 3,  4, "@",     0, 1);
        doc.assertIdLeaf(  4,  6, "id",    0, 2, 0, 0);
        doc.assertKeyLeaf( 6,  7, ":",     0, 3);
        doc.assertTextLeaf(7, 12, "Title", 0, 4, 0, 0);
    }

    @Test
    public void nonSpacedDirectoryTitlelessHeading(){
        String raw = "===@id:#abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        SpanBranch heading = doc.assertChild(5, raw,     0);
        SpanBranch id      = doc.assertChild(1, "id",    0, 2);
        SpanBranch edition = doc.assertChild(2, "#abc",  0, 4);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);
        doc.assertIds();

        assertHeading(heading, null, LinedType.HEADING, 3,
            builder, EditionType.OTHER, 0, 0, CatalogueStatus.UNUSED);


        DirectoryDebug.assertId(id, DirectoryType.LINK, builder);
        EditionDebug.assertEdition(edition, EditionType.OTHER, "abc");

        doc.assertKeyLeaf( 0,  3, "===", 0, 0);
        doc.assertKeyLeaf( 3,  4, "@",   0, 1);
        doc.assertIdLeaf(  4,  6, "id",  0, 2, 0, 0);
        doc.assertKeyLeaf( 6,  7, ":",   0, 3);
        doc.assertKeyLeaf( 7,  8, "#",   0, 4, 0);
        doc.assertTextLeaf(8, 11, "abc", 0, 4, 1, 0);
    }
    @Test
    public void partlyDirectoryHeading(){
        String raw = "===@id";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        SpanBranch heading = doc.assertChild(3, raw,  0);
        SpanBranch id      = doc.assertChild(1, "id", 0, 2);

        IDBuilder builder = buildId("id");
        doc.addId(builder, 0);
        doc.assertIds();

        assertHeading(heading, null, LinedType.HEADING, 3,
            builder, EditionType.NONE, 0, 0, CatalogueStatus.UNUSED);


        DirectoryDebug.assertId(id, DirectoryType.LINK, builder);

        doc.assertKeyLeaf(0,  3, "===", 0, 0);
        doc.assertKeyLeaf(3,  4, "@",   0, 1);
        doc.assertIdLeaf( 4,  6, "id",  0, 2, 0, 0);
    }

    @Test
    public void idLessHeading(){
        String raw = "===Title#abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        SpanBranch heading = doc.assertChild(3, raw,     0);
        SpanBranch content = doc.assertChild(1, "Title", 0, 1);
        SpanBranch edition = doc.assertChild(2, "#abc",  0, 2);

        doc.assertIds();

        assertHeading(heading, content, LinedType.HEADING, 3,
            null, EditionType.OTHER, 1, 0, CatalogueStatus.NO_ID);
        EditionDebug.assertEdition(edition, EditionType.OTHER, "abc");

        doc.assertKeyLeaf( 0,  3, "===",   0, 0);
        doc.assertTextLeaf(3,  8, "Title", 0, 1, 0, 0);
        doc.assertKeyLeaf( 8,  9, "#",     0, 2, 0);
        doc.assertTextLeaf(9, 12, "abc",   0, 2, 1, 0);
    }

    @Test
    public void statusOnlyHeading(){
        String raw = "===#abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        SpanBranch heading = doc.assertChild(2, raw,     0);
        SpanBranch edition = doc.assertChild(2, "#abc",  0, 1);

        doc.assertIds();

        assertHeading(heading, null, LinedType.HEADING, 3,
            null, EditionType.OTHER, 0, 0, CatalogueStatus.NO_ID);
        EditionDebug.assertEdition(edition, EditionType.OTHER, "abc");

        doc.assertKeyLeaf( 0, 3, "===",   0, 0);
        doc.assertKeyLeaf( 3, 4, "#",     0, 1, 0);
        doc.assertTextLeaf(4, 7, "abc",   0, 1, 1, 0);
    }

    @Test
    public void heading6(){
        String raw = "======abc\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        SpanBranch heading = doc.assertChild(3, "======abc\n", 0);
        SpanBranch content = doc.assertChild(1, "abc",         0, 1);

        doc.assertIds();

        assertHeading(heading, content, LinedType.HEADING, 6,
            null, EditionType.NONE, 1, 0, CatalogueStatus.NO_ID);
        FormatSpanDebug.assertMain(content, 1, 0);

        doc.assertKeyLeaf( 0,  6, "======", 0, 0);
        doc.assertTextLeaf(6,  9, "abc",    0, 1, 0, 0);
        doc.assertKeyLeaf( 9, 10, "\n",     0, 2);
    }

    @Test
    public void heading1(){
        String raw = "=abc\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, parsers);
        SpanBranch heading = doc.assertChild(3, "=abc\n", 0);
        SpanBranch content = doc.assertChild(1, "abc",    0, 1);

        doc.assertIds();

        assertHeading(heading, content, LinedType.HEADING, 1,
            null, EditionType.NONE, 1, 0, CatalogueStatus.NO_ID);
        FormatSpanDebug.assertMain(content, 1, 0);

        doc.assertKeyLeaf( 0, 1, "=",   0, 0);
        doc.assertTextLeaf(1, 4, "abc", 0, 1, 0, 0);
        doc.assertKeyLeaf( 4, 5, "\n",  0, 2);
    }
}
