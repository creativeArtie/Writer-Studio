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

    private static final SetupParser[] parsers = new SetupParser[]{
        AuxiliaryData.FORMATTED_BASIC};

    @Test
    public void basic(){
        String raw = "abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        FormatContentTest content = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText(raw);

        main.test(       doc, 1, raw, 0);
        content.test(    doc, 1, raw, 0, 0);
        doc.assertTextLeaf(0, 3, raw, 0, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicBold(){
        ///           01234567890
        String raw = "or**an**ge";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        FormatContentTest content1 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("or");
        FormatContentTest content2 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("an")  .setFormats(FormatType.BOLD);
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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicItalics(){
        ///           0123456
        String raw = "g*ee*n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        FormatContentTest content1 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("g");
        FormatContentTest content2 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("ee")  .setFormats(FormatType.ITALICS);
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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicUnderlineCoded(){
        ///           012 34567890
        String raw = "g_e\\_e`dd_";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        FormatContentTest content1 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("g");
        FormatContentTest content2 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("e_e") .setFormats(FormatType.UNDERLINE);
        EscapeTest escape = new BranchTest.EscapeTest().setEscape("_");
        FormatContentTest content3 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("dd")  .setFormats(FormatType.UNDERLINE, FormatType.CODED);

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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void startedUnderline(){
        ///           01234
        String raw = "_abc  ab";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(2).setNoteTotal(0);
        FormatContentTest content = new FormatContentTest()
            .setBegin(false)  .setEnd(false)
            .setText("abc ab").setFormats(FormatType.UNDERLINE);

        main.test(       doc, 2, raw,       0);
        doc.assertKeyLeaf( 0, 1, "_",       0, 0);
        content.test(    doc, 1, "abc  ab", 0, 1);
        doc.assertTextLeaf(1, 8, "abc  ab", 0, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void withEndnote(){
        ///           01234567890
        String raw = "_{*abc}pee";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            FormatCurlyDebug.buildEndnoteId("abc"),
            CatalogueStatus.NOT_FOUND, 0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);
        FormatNoteTest cite = new FormatNoteTest()
            .setDirectoryType(DirectoryType.ENDNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setFormats(FormatType.UNDERLINE);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(builder);
        ContentTest idText = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(false)  .setCount(1);
        FormatContentTest content = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("pee") .setFormats(FormatType.UNDERLINE);

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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void betweenTodos(){
        String text = "  abc ddd ";
        String raw = "{!todo}" + text + "{!abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(2).setNoteTotal(2);
        /// "{!todo}"
        IDBuilder builder = doc.addId(FormatAgendaDebug.buildId("00"), 0);
        FormatAgendaTest todo1 = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setText("todo");
        ContentTest todo1Text = new ContentTest()
            .setBegin(false).setText("todo")
            .setEnd(false)  .setCount(1);
        /// "  abc ddd"
        FormatContentTest content = new FormatContentTest()
            .setText(text)  .setBegin(false)
            .setEnd(false);
        builder = doc.addId(FormatAgendaDebug.buildId("17"), 1);
        /// "{!abc}"
        FormatAgendaTest todo2 = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setText("abc");
        ContentTest todo2Text = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(false)  .setCount(1);

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
            .setPublishTotal(6).setNoteTotal(1);
        /// "Begin\\\\"
        FormatContentTest text1 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("Begin\\");
        EscapeTest escape = new EscapeTest()
            .setEscape("\\");
        /// " Say"
        FormatContentTest text2 = new FormatContentTest()
            .setBegin(true).setEnd(false)
            .setText("Say").setFormats(FormatType.ITALICS);
        /// "'s"
        FormatContentTest text3 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("'s").setFormats(FormatType.BOLD, FormatType.ITALICS);
        /// " Hi "
        FormatContentTest text4 = new FormatContentTest()
            .setBegin(true).setEnd(true)
            .setText("Hi") .setFormats(FormatType.BOLD, FormatType.ITALICS,
                                       FormatType.UNDERLINE);
        /// "Joy"
        FormatContentTest text5 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("Joy") .setFormats(FormatType.BOLD, FormatType.ITALICS,
                                        FormatType.UNDERLINE, FormatType.CODED);
        /// "{@note}"
        IDBuilder builder = doc.addRef(FormatCurlyDebug.buildNoteId("note"), 2);
        FormatNoteTest cite = new FormatNoteTest()
            .setDirectoryType(DirectoryType.NOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setFormats(FormatType.ITALICS);
        DirectoryTest citeId = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(builder);
        /// " "
        FormatContentTest text6 = new FormatContentTest()
            .setBegin(true).setEnd(true)
            .setText("")   .setFormats(FormatType.ITALICS);
        /// "<@link>"
        builder = doc.addRef(FormatLinkDebug.buildLinkId("link"), 1);
        FormatLinkTest ref = new FormatLinkTest()
            .setText("")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setFormats(FormatType.ITALICS);
        DirectoryTest refId = new DirectoryTest()
            .setPurpose(DirectoryType.LINK).setIdentity(builder);
        ContentTest refIdText = new BranchTest.ContentTest()
            .setBegin(false).setText("link")
            .setEnd(false)  .setCount(1);
        /// "<a.ca| web>"
        FormatLinkTest link = new FormatLinkTest()
            .setText("web").setPath(doc, 0, 15, 1)
            .setFormats(FormatType.ITALICS);
        ContentTest linkPath = new BranchTest.ContentTest()
            .setBegin(false).setText("a.ca")
            .setEnd(false)  .setCount(1);
        ContentTest linkText = new BranchTest.ContentTest()
            .setBegin(true).setText("web")
            .setEnd(false) .setCount(1);
        /// "{!todo}"
        builder = doc.addId(FormatAgendaDebug.buildId("55"), 0);
        FormatAgendaTest todo = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, builder).setText("todo");
        ContentTest todoText = new ContentTest()
            .setBegin(false).setText("todo")
            .setEnd(false)  .setCount(1);
        /// " see"
        FormatContentTest text7 = new FormatContentTest()
            .setBegin(true).setEnd(false)
            .setText("see").setFormats(FormatType.ITALICS);


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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void editSetEnabled(){
        ///              0123456789012
        String before = "before *bold";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(7, "*");
        editCommon(doc);
    }

    private void editCommon(DocumentAssert doc){
        ///             01234567890123
        String after = "before **bold";
        doc.assertDoc(1, after);

        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(2).setNoteTotal(0);
        FormatContentTest content1 = new FormatContentTest()
            .setBegin(false).setEnd(true)
            .setText("before");
        FormatContentTest content2 = new FormatContentTest()
            .setBegin(false).setEnd(false)
            .setText("bold").setFormats(FormatType.BOLD);

        main.test(       doc,   3, after,     0);
        content1.test(   doc,   1, "before ", 0, 0);
        doc.assertTextLeaf(0,   7, "before ", 0, 0, 0);
        doc.assertKeyLeaf( 7,   9, "**",      0, 1);
        content2.test(   doc,   1, "bold",    0, 2);
        doc.assertTextLeaf(9,  13, "bold",    0, 2, 0);
        doc.assertIds();
    }
}
