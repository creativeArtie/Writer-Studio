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
        SpanBranch main = doc.assertChild(2, raw,   0);
        SpanBranch text = doc.assertChild(1, "abc  ab", 0, 1);

        assertMain(main, 2, 0);
        assertContent(text, "abc ab", false, false, FormatType.UNDERLINE);

        doc.assertKeyLeaf( 0, 1, "_",   0, 0);
        doc.assertTextLeaf(1, 8, "abc  ab", 0, 1, 0);

        doc.assertIds();
    }

    @Test
    public void withEndnote(){
        ///           01234567890
        String raw = "_{*abc}pee";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch main = doc.assertChild(3, raw,      0);
        SpanBranch cite = doc.assertChild(3, "{*abc}", 0, 1);
        SpanBranch id   = doc.assertChild(1, "abc",    0, 1, 1);
        SpanBranch text = doc.assertChild(1, "pee",    0, 2);

        IDBuilder builder = FormatCurlyDebug.buildEndnoteId("abc");
        doc.addRef(builder, CatalogueStatus.NOT_FOUND, 0);

        assertMain(main, 1, 0);
        FormatCurlyDebug.assertNote(cite, DirectoryType.ENDNOTE,
            CatalogueStatus.NOT_FOUND, builder, FormatType.UNDERLINE);
        DirectoryDebug.assertId(id, DirectoryType.ENDNOTE, builder);
        assertContent(text, "pee", false, false, FormatType.UNDERLINE);

        doc.assertKeyLeaf( 0,  1, "_",   0, 0);
        doc.assertKeyLeaf( 1,  3, "{*",  0, 1, 0);
        doc.assertIdLeaf(  3,  6, "abc", 0, 1, 1, 0, 0);
        doc.assertKeyLeaf( 6,  7, "}",   0, 1, 2);
        doc.assertTextLeaf(7, 10, "pee", 0, 2, 0);
        doc.assertIds();
    }

    @Test
    public void betweenTodos(){
        String raw = "{!todo}  abc ddd {!abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch main      = doc.assertChild(3, raw,          0);
        SpanBranch todo1     = doc.assertChild(3, "{!todo}",    0, 0);
        SpanBranch todo1Text = doc.assertChild(1, "todo",       0, 0, 1);
        SpanBranch text      = doc.assertChild(1, "  abc ddd ", 0, 1);
        SpanBranch todo2     = doc.assertChild(3, "{!abc}",     0, 2);
        SpanBranch todo2Text = doc.assertChild(1, "abc",        0, 2, 1);

        IDBuilder builder = FormatAgendaDebug.buildId("00");
        doc.addId(builder, 0);

        assertMain(main, 2, 2);
        FormatAgendaDebug.assertAgenda(todo1, "todo", builder);
        new BranchTest.ContentTest().setText("todo").setBegin(false)
            .setEnd(false).setCount(1).test(doc, 1, "todo", 0, 0, 1);

        assertContent(text, "abc ddd", true, true);


        builder = FormatAgendaDebug.buildId("17");
        doc.addId(builder, 1);

        FormatAgendaDebug.assertAgenda(todo2, "abc", builder);
        new BranchTest.ContentTest().setText("abc").setBegin(false)
            .setEnd(false).setCount(1).test(doc, 1, "abc", 0, 2, 1);


        doc.assertKeyLeaf(  0,  2, "{!",         0, 0, 0);
        doc.assertTextLeaf( 2,  6, "todo",       0, 0, 1, 0);
        doc.assertKeyLeaf(  6,  7, "}",          0, 0, 2);
        doc.assertTextLeaf( 7, 17, "  abc ddd ", 0, 1, 0);
        doc.assertKeyLeaf( 17, 19, "{!",         0, 2, 0);
        doc.assertTextLeaf(19, 22, "abc",        0, 2, 1, 0);
        doc.assertKeyLeaf( 22, 23, "}",          0, 2, 2);
        doc.assertIds();
    }

    @Test
    public void complete(){
        String raw = "Begin\\\\* Say**'s_ Hi `Joy`**_{@note} " +
            "<@link><a.ca| web>{!todo} see";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch main     = doc.assertChild(18, raw,           0);
        SpanBranch text1    = doc.assertChild( 2, "Begin\\\\",   0, 0);
        SpanBranch escape   = doc.assertChild( 2, "\\\\",        0, 0, 1);
        SpanBranch text2    = doc.assertChild( 1, " Say",        0, 2);
        SpanBranch text3    = doc.assertChild( 1, "'s",          0, 4);
        SpanBranch text4    = doc.assertChild( 1, " Hi ",        0, 6);
        SpanBranch text5    = doc.assertChild( 1, "Joy",         0, 8);
        SpanBranch cite     = doc.assertChild( 3, "{@note}",     0, 12);
        SpanBranch citeId   = doc.assertChild( 1, "note",        0, 12, 1);
        SpanBranch text6    = doc.assertChild( 1, " ",           0, 13);
        SpanBranch ref      = doc.assertChild( 3, "<@link>",     0, 14);
        SpanBranch refId    = doc.assertChild( 1, "link",        0, 14, 1);
        SpanBranch link     = doc.assertChild( 5, "<a.ca| web>", 0, 15);
        SpanBranch linkPath = doc.assertChild( 1, "a.ca",        0, 15, 1);
        SpanBranch linkText = doc.assertChild( 1, " web",        0, 15, 3);
        SpanBranch todo     = doc.assertChild( 3, "{!todo}",     0, 16);
        SpanBranch todoText = doc.assertChild( 1, "todo",        0, 16, 1);
        SpanBranch text7    = doc.assertChild( 1, " see",        0, 17);

        assertMain(main, 6, 1);

        assertContent(text1, "Begin\\", false, false);
        new BranchTest.EscapeTest().setEscape("\\").test(doc, 2, "\\\\", 0, 0, 1);

        assertContent(text2, "Say", true, false, FormatType.ITALICS);
        assertContent(text3, "'s", false, false, FormatType.BOLD,
            FormatType.ITALICS);
        assertContent(text4, "Hi", true, true, FormatType.BOLD,
            FormatType.ITALICS, FormatType.UNDERLINE);
        assertContent(text5, "Joy", false, false, FormatType.BOLD,
            FormatType.ITALICS, FormatType.UNDERLINE, FormatType.CODED);

        IDBuilder builder = FormatCurlyDebug.buildNoteId("note");
        doc.addRef(builder, 2);

        FormatCurlyDebug.assertNote(cite, DirectoryType.NOTE,
            CatalogueStatus.NOT_FOUND, builder, FormatType.ITALICS);
        DirectoryDebug.assertId(citeId, DirectoryType.NOTE, builder);

        assertContent(text6, "", true, true, FormatType.ITALICS);

        builder = FormatLinkDebug.buildLinkId("link");
        doc.addRef(builder, 1);

        FormatLinkDebug.assertRefLink(ref, "", "",
            CatalogueStatus.NOT_FOUND, builder, FormatType.ITALICS);
        DirectoryDebug.assertId(refId, DirectoryType.LINK, builder);
        FormatLinkDebug.assertDirLink(link, "a.ca", "web", FormatType.ITALICS);
        new BranchTest.ContentTest().setText("a.ca").setBegin(false)
            .setEnd(false).setCount(1).test(doc, 1, "a.ca", 0, 15, 1);

        new BranchTest.ContentTest().setText("web").setBegin(true).setEnd(false)
            .setCount(1).test(doc, 1, " web", 0, 15, 3);


        builder = FormatAgendaDebug.buildId("55");
        doc.addId(builder, 0);

        FormatAgendaDebug.assertAgenda(todo, "todo", builder);
        new BranchTest.ContentTest().setText("todo").setBegin(false)
            .setEnd(false).setCount(1).test(doc, 1, "todo", 0, 16, 1);


        assertContent(text7, "see", true, false, FormatType.ITALICS);


        ///0           1         2         3         4         5         6
        ///01234 5 6789012345678901234567890123456789012345678901234567890123456
        ///Begin\\\\* Say**'s_ Hi `Joy`**_{@note} <@link><a.ca| web>{!todo} see
        doc.assertTextLeaf( 0, 5, "Begin", 0, 0, 0);
        doc.assertKeyLeaf(  5, 6,    "\\", 0, 0, 1, 0);
        doc.assertTextLeaf( 6, 7,    "\\", 0, 0, 1, 1);
        doc.assertKeyLeaf(  7, 8,     "*", 0, 1);
        doc.assertTextLeaf( 8, 12, " Say", 0, 2, 0);
        doc.assertKeyLeaf( 12, 14,   "**", 0, 3);
        doc.assertTextLeaf(14, 16,   "'s", 0, 4, 0);
        doc.assertKeyLeaf( 16, 17,    "_", 0, 5);
        doc.assertTextLeaf(17, 21, " Hi ", 0, 6, 0);
        doc.assertKeyLeaf( 21, 22,    "`", 0, 7);
        doc.assertTextLeaf(22, 25,  "Joy", 0, 8, 0);
        doc.assertKeyLeaf( 25, 26,    "`", 0, 9);
        doc.assertKeyLeaf( 26, 28,   "**", 0, 10);
        doc.assertKeyLeaf( 28, 29,    "_", 0, 11);
        doc.assertKeyLeaf( 29, 31,   "{@", 0, 12, 0);
        doc.assertIdLeaf(  31, 35, "note", 0, 12, 1, 0, 0);
        doc.assertKeyLeaf( 35, 36,    "}", 0, 12, 2);
        doc.assertTextLeaf(36, 37,    " ", 0, 13, 0);
        doc.assertKeyLeaf( 37, 39,   "<@", 0, 14, 0);
        doc.assertIdLeaf(  39, 43, "link", 0, 14, 1, 0, 0);
        doc.assertKeyLeaf( 43, 44,    ">", 0, 14, 2);
        doc.assertKeyLeaf( 44, 45,    "<", 0, 15, 0);
        doc.assertPathLeaf(45, 49, "a.ca", 0, 15, 1, 0);
        doc.assertKeyLeaf( 49, 50,    "|", 0, 15, 2);
        doc.assertTextLeaf(50, 54, " web", 0, 15, 3, 0);
        doc.assertKeyLeaf( 54, 55,    ">", 0, 15, 4);
        doc.assertKeyLeaf( 55, 57,   "{!", 0, 16, 0);
        doc.assertTextLeaf(57, 61, "todo", 0, 16, 1, 0);
        doc.assertKeyLeaf( 61, 62,    "}", 0, 16, 2);
        doc.assertTextLeaf(62, 66, " see", 0, 17, 0);
        ///Begin\\\\* Say**'s_ Hi `Joy`**_{@note} <@link><a.ca| web>{!todo} see
        ///01234 5 6789012345678901234567890123456789012345678901234567890123456
        ///0           1         2         3         4         5         6
        doc.assertIds();
    }
}
