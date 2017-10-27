package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class FormatSpanDebug {

    public static DetailStyle[] mergeStyle(DetailStyle[] styles,
        FormatType ... formats)
    {
        DetailStyle[] info = new DetailStyle[formats.length + styles.length];
        System.arraycopy(styles,  0, info, 0,             styles.length);
        System.arraycopy(formats, 0, info, styles.length, formats.length);
        return info;
    }

    public static void assertFormats(FormatSpan test, FormatType ... formats){
        assertFormat(test, test.isBold(),      FormatType.BOLD, formats);
        assertFormat(test, test.isItalics(),   FormatType.ITALICS, formats);
        assertFormat(test, test.isUnderline(), FormatType.UNDERLINE, formats);
        assertFormat(test, test.isCoded(),     FormatType.CODED, formats);
    }

    private static void assertFormat(FormatSpan span, boolean format,
            FormatType type,  FormatType[] formats)
    {
        boolean isTrue = false;
        for (FormatType expected: formats){
            if (type == expected){
                isTrue = true;
            }
        }
        assertEquals(getError(type.name().toLowerCase() + " format", span),
            isTrue, format);
    }

    public static void assertMain(SpanBranch span, int publish, int note){
        FormatSpanMain test = assertClass(span, FormatSpanMain.class);

        assertEquals(getError("publish", test), publish, test.getPublishCount());
        assertEquals(getError("note", test),    note,    test.getNoteCount());
        assertBranch(span);
    }

    public static void assertContent(SpanBranch span, String text,
        boolean isBegin, boolean isEnd, FormatType ... formats)
    {

        FormatSpanContent test = assertClass(span, FormatSpanContent.class);

        assertEquals("Wrong text for " + span, text, test.getParsed());
        ContentDebug.assertBasics(test, text, isBegin, isEnd);
        assertFormats(test, formats);
        assertBranch(span, formats);
    }

    private static final SetupParser[] parsers = new SetupParser[]{
        new FormatParser()};

    @Test
    public void basic(){
        String raw = "abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);
        FormatContentTest content = new FormatContentTest()
            .setText(raw) .setBegin(false)
            .setEnd(false);

        main.test(       doc, 1, raw, 0);
        content.test(    doc, 1, raw, 0, 0);
        doc.assertTextLeaf(0, 3, raw, 0, 0, 0);
        doc.assertIds();
    }

    @Test
    public void bold(){
        ///           01234567890
        String raw = "or**an**ge";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);
        FormatContentTest content1 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("or");
        FormatContentTest content2 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("an").setFormats(FormatType.BOLD);
        FormatContentTest content3 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("an");

        main.test(       doc,  5, raw,  0);
        content1.test(   doc,  1, "or", 0, 0);
        doc.assertTextLeaf(0,  2, "or", 0, 0, 0);
        doc.assertKeyLeaf( 2,  4, "**", 0, 1);
        content2.test(   doc,  1, "an", 0, 2);
        doc.assertTextLeaf(4,  6, "an", 0, 2, 0);
        doc.assertKeyLeaf( 6,  8, "**", 0, 3);
        content3.test(   doc,  1, "ge", 0, 4);
        doc.assertTextLeaf(8, 10, "ge", 0, 4, 0);

        doc.assertIds();
    }

    @Test
    public void italics(){
        ///           0123456
        String raw = "g*ee*n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);
        FormatContentTest content1 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("g");
        FormatContentTest content2 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("ee").setFormats(FormatType.ITALICS);
        FormatContentTest content3 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("n");

        main.test(       doc, 5, raw,  0);
        content1.test(   doc, 1, "g",  0, 0);
        doc.assertTextLeaf(0, 1, "g",  0, 0, 0);
        doc.assertKeyLeaf( 1, 2, "*",  0, 1);
        content2.test(   doc, 1, "ee", 0, 2);
        doc.assertTextLeaf(2, 4, "ee", 0, 2, 0);
        doc.assertKeyLeaf( 4, 5, "*",  0, 3);
        content3.test(   doc, 1, "n",  0, 4);
        doc.assertTextLeaf(5, 6, "n",  0, 4, 0);

        doc.assertIds();
    }

    @Test
    public void underlineCoded(){
        ///           012 34567890
        String raw = "g_e\\_e`dd_";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);
        FormatContentTest content1 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("g");
        FormatContentTest content2 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("e_e").setFormats(FormatType.UNDERLINE);
        EscapeTest escape = new BranchTest.EscapeTest().setEscape("_");
        FormatContentTest content3 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("dd").setFormats(FormatType.UNDERLINE, FormatType.CODED);

        main.test(       doc,  6, raw,     0);
        content1.test(   doc,  1, "g",     0, 0);
        doc.assertTextLeaf(0,  1, "g",     0, 0, 0);
        doc.assertKeyLeaf( 1,  2, "_",     0, 1);
        content2.test(   doc,  3, "e\\_e", 0, 2);
        doc.assertTextLeaf(2,  3, "e",     0, 2, 0);
        escape.test(     doc,  2, "\\_",   0, 2, 1);
        doc.assertKeyLeaf( 3,  4, "\\",    0, 2, 1, 0);
        doc.assertTextLeaf(4,  5, "_",     0, 2, 1, 1);
        doc.assertTextLeaf(5,  6, "e",     0, 2, 2);
        doc.assertKeyLeaf( 6,  7, "`",     0, 3);
        content3.test(   doc,  1, "dd",    0, 4);
        doc.assertTextLeaf(7,  9, "dd",    0, 4, 0);
        doc.assertKeyLeaf( 9, 10, "_",     0, 5);

        doc.assertIds();
    }

    @Test
    public void startedUnderline(){
        ///           01234
        String raw = "_abc  ab";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatMainTest main = new FormatMainTest()
            .setPublishCount(2).setNoteCount(0);
        FormatContentTest content = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("abc ab").setFormats(FormatType.UNDERLINE);

        main.test(       doc, 2, raw,       0);
        doc.assertKeyLeaf( 0, 1, "_",       0, 0);
        content.test(    doc, 1, "abc  ab", 0, 1);
        doc.assertTextLeaf(1, 8, "abc  ab", 0, 1, 0);

        doc.assertIds();
    }

    @Test
    public void withEndnote(){
        ///           01234567890
        String raw = "_{*abc}pee";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = FormatCurlyDebug.buildEndnoteId("abc");
        doc.addRef(builder, CatalogueStatus.NOT_FOUND, 0);

        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);
        FormatNoteTest cite = new FormatNoteTest()
            .setDirectoryType(DirectoryType.ENDNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setFormats(FormatType.UNDERLINE);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(builder);
        ContentTest idText = new ContentTest()
            .setText("abc") .setBegin(false)
            .setEnd(false).setCount(1);
        FormatContentTest content = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("pee").setFormats(FormatType.UNDERLINE);

        main.test(       doc,  3, raw,      0);
        doc.assertKeyLeaf( 0,  1, "_",      0, 0);
        cite.test(       doc,  3, "{*abc}", 0, 1);
        doc.assertKeyLeaf( 1,  3, "{*",     0, 1, 0);
        id.test(         doc,  1, "abc",    0, 1, 1);
        idText.test(     doc,  1, "abc",    0, 1, 1, 0);
        doc.assertIdLeaf(  3,  6, "abc",    0, 1, 1, 0, 0);
        doc.assertKeyLeaf( 6,  7, "}",      0, 1, 2);
        content.test(    doc,  1, "pee",    0, 2);
        doc.assertTextLeaf(7, 10, "pee",    0, 2, 0);
        doc.assertIds();
    }

    @Test
    public void betweenTodos(){
        String raw = "{!todo}  abc ddd {!abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = FormatAgendaDebug.buildId("00");
        doc.addId(builder, 0);

        String text = "  abc ddd ";
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(2)    .setNoteCount(2);
        FormatAgendaTest todo1 = new FormatAgendaTest()
            .setCatalogued(builder).setText("todo");
        ContentTest todo1Text = new ContentTest()
            .setText("todo").setBegin(false)
            .setEnd(false)  .setCount(1);
        FormatContentTest content = new FormatContentTest()
            .setText(text)  .setBegin(false)
            .setEnd(false);

        builder = FormatAgendaDebug.buildId("17");
        doc.addId(builder, 1);

        FormatAgendaTest todo2 = new FormatAgendaTest()
            .setCatalogued(builder).setText("abc");
        ContentTest todo2Text = new ContentTest()
            .setText("abc")        .setBegin(false)
            .setEnd(false)         .setCount(1);

        main.test(        doc, 3, raw,       0);
        todo1.test(       doc, 3, "{!todo}", 0, 0);
        doc.assertKeyLeaf(  0,  2, "{!",     0, 0, 0);
        todo1Text.test(   doc, 1, "todo",    0, 0, 1);
        doc.assertTextLeaf( 2,  6, "todo",   0, 0, 1, 0);
        doc.assertKeyLeaf(  6,  7, "}",      0, 0, 2);
        content.test(     doc,  1, text,     0, 1);
        doc.assertTextLeaf( 7, 17, text,     0, 1, 0);
        todo2.test(       doc,  3, "{!abc}", 0, 2);
        doc.assertKeyLeaf( 17, 19, "{!",     0, 2, 0);
        todo2Text.test(   doc,  1, "abc",    0, 2, 1);
        doc.assertTextLeaf(19, 22, "abc",    0, 2, 1, 0);
        doc.assertKeyLeaf( 22, 23, "}",      0, 2, 2);
        doc.assertIds();
    }

    @Test
    public void complete(){
        String raw = "Begin\\\\* Say**'s_ Hi `Joy`**_{@note} " +
            "<@link><a.ca| web>{!todo} see";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatMainTest main = new FormatMainTest()
            .setPublishCount(6).setNoteCount(1);

        FormatContentTest text1 = new FormatContentTest()
            .setText("Begin\\").setBegin(false).setEnd(false);
        EscapeTest escape = new EscapeTest()
            .setEscape("\\");

        FormatContentTest text2 = new FormatContentTest()
            .setText("Say").setBegin(true).setEnd(false)
            .setFormats(FormatType.ITALICS);
        FormatContentTest text3 = new FormatContentTest()
            .setText("'s").setBegin(false).setEnd(false)
            .setFormats(FormatType.BOLD, FormatType.ITALICS);
        FormatContentTest text4 = new FormatContentTest()
            .setText("Hi").setBegin(true).setEnd(true)
            .setFormats(FormatType.BOLD, FormatType.ITALICS,
                       FormatType.UNDERLINE);
        FormatContentTest text5 = new FormatContentTest()
            .setText("Joy").setBegin(false).setEnd(false)
            .setFormats(FormatType.BOLD, FormatType.ITALICS,
                       FormatType.UNDERLINE, FormatType.CODED);

        IDBuilder builder = FormatCurlyDebug.buildNoteId("note");
        doc.addRef(builder, 2);

        FormatNoteTest cite = new FormatNoteTest()
            .setDirectoryType(DirectoryType.NOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setFormats(FormatType.ITALICS);
        DirectoryTest citeId = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(builder);

        FormatContentTest text6 = new FormatContentTest()
            .setText("").setBegin(true).setEnd(true)
            .setFormats(FormatType.ITALICS);

        builder = FormatLinkDebug.buildLinkId("link");
        doc.addRef(builder, 1);

        FormatLinkTest ref = new FormatLinkTest()
            .setPath("").setText("")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setFormats(FormatType.ITALICS);
        DirectoryTest refId = new DirectoryTest()
            .setPurpose(DirectoryType.LINK).setIdentity(builder);
        ContentTest refIdText = new BranchTest.ContentTest()
            .setText("link").setBegin(false)
            .setEnd(false) .setCount(1);


        FormatLinkTest link = new FormatLinkTest()
            .setPath("a.ca").setText("web")
            .setFormats(FormatType.ITALICS);
        ContentTest linkPath = new BranchTest.ContentTest()
            .setText("a.ca").setBegin(false)
            .setEnd(false)  .setCount(1);
        ContentTest linkText = new BranchTest.ContentTest()
            .setText("web").setBegin(true)
            .setEnd(false) .setCount(1);


        builder = FormatAgendaDebug.buildId("55");
        doc.addId(builder, 0);

        FormatAgendaTest todo = new FormatAgendaTest().
            setCatalogued(builder).setText("todo");
        ContentTest todoText = new ContentTest()
            .setText("todo").setBegin(false)
            .setEnd(false).setCount(1);

        FormatContentTest text7 = new FormatContentTest()
            .setText("see").setBegin(true).setEnd(false)
            .setFormats(FormatType.ITALICS);


        ///0           1         2         3         4         5         6
        ///01234 5 6789012345678901234567890123456789012345678901234567890123456
        ///Begin\\\\* Say**'s_ Hi `Joy`**_{@note} <@link><a.ca| web>{!todo} see
        main.test(        doc, 18, raw,          0);
        text1.test(       doc, 2, "Begin\\\\",   0, 0);
        doc.assertTextLeaf( 0, 5, "Begin",       0, 0, 0);
        escape.test(      doc, 2, "\\\\",        0, 0, 1);
        doc.assertKeyLeaf(  5, 6,    "\\",       0, 0, 1, 0);
        doc.assertTextLeaf( 6, 7,    "\\",       0, 0, 1, 1);
        doc.assertKeyLeaf(  7, 8,     "*",       0, 1);
        text2.test(       doc, 1, " Say",        0, 2);
        doc.assertTextLeaf( 8, 12, " Say",       0, 2, 0);
        doc.assertKeyLeaf( 12, 14,   "**",       0, 3);
        text3.test(       doc, 1, "'s",          0, 4);
        doc.assertTextLeaf(14, 16,   "'s",       0, 4, 0);
        doc.assertKeyLeaf( 16, 17,    "_",       0, 5);
        text4.test(       doc, 1, " Hi ",        0, 6);
        doc.assertTextLeaf(17, 21, " Hi ",       0, 6, 0);
        doc.assertKeyLeaf( 21, 22,    "`",       0, 7);
        text5.test(       doc, 1, "Joy",         0, 8);
        doc.assertTextLeaf(22, 25,  "Joy",       0, 8, 0);
        doc.assertKeyLeaf( 25, 26,    "`",       0, 9);
        doc.assertKeyLeaf( 26, 28,   "**",       0, 10);
        doc.assertKeyLeaf( 28, 29,    "_",       0, 11);
        cite.test(        doc, 3, "{@note}",     0, 12);
        doc.assertKeyLeaf( 29, 31,   "{@",       0, 12, 0);
        citeId.test(      doc, 1, "note",        0, 12, 1);
        doc.assertIdLeaf(  31, 35, "note",       0, 12, 1, 0, 0);
        doc.assertKeyLeaf( 35, 36,    "}",       0, 12, 2);
        text6.test(       doc, 1, " ",           0, 13);
        doc.assertTextLeaf(36, 37,    " ",       0, 13, 0);
        ref.test(         doc, 3, "<@link>",     0, 14);
        doc.assertKeyLeaf( 37, 39,   "<@",       0, 14, 0);
        refId.test(       doc,  1, "link",       0, 14, 1);
        refIdText.test(   doc,  1, "link",       0, 14, 1, 0);
        doc.assertIdLeaf(  39, 43, "link",       0, 14, 1, 0, 0);
        doc.assertKeyLeaf( 43, 44,    ">",       0, 14, 2);
        doc.assertKeyLeaf( 44, 45,    "<",       0, 15, 0);
        link.test(        doc, 5, "<a.ca| web>", 0, 15);
        linkPath.test(    doc, 1, "a.ca",        0, 15, 1);
        doc.assertPathLeaf(45, 49, "a.ca",       0, 15, 1, 0);
        doc.assertKeyLeaf( 49, 50,    "|",       0, 15, 2);
        linkText.test(    doc, 1, " web",        0, 15, 3);
        doc.assertTextLeaf(50, 54, " web",       0, 15, 3, 0);
        doc.assertKeyLeaf( 54, 55,    ">",       0, 15, 4);
        todo.test(        doc, 3, "{!todo}",     0, 16);
        doc.assertKeyLeaf( 55, 57,   "{!",       0, 16, 0);
        todoText.test(    doc, 1, "todo",        0, 16, 1);
        doc.assertTextLeaf(57, 61, "todo",       0, 16, 1, 0);
        doc.assertKeyLeaf( 61, 62,    "}",       0, 16, 2);
        text7.test(       doc, 1, " see",        0, 17);
        doc.assertTextLeaf(62, 66, " see",       0, 17, 0);
        ///Begin\\\\* Say**'s_ Hi `Joy`**_{@note} <@link><a.ca| web>{!todo} see
        ///01234 5 6789012345678901234567890123456789012345678901234567890123456
        ///0           1         2         3         4         5         6
        doc.assertIds();
    }
}
