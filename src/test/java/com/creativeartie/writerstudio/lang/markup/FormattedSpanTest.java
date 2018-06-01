package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchFormatAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
public class FormattedSpanTest {

    private static final SetupParser[] parsers = new SetupParser[]{
        AuxiliaryData.FORMATTED_TEXT};

    @Test
    public void basic(){
        String raw = "abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed(raw);
        FormatContentAssert content = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth(raw);

        main.test(1,         raw, 0);
        content.test(1,      raw, 0, 0);
        doc.assertText(0, 3, raw, 0, 0, 0);
        doc.assertRest();
    }

    @Test
    public void basicBold(){
        ///           01234567890
        String raw = "or**an**ge";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("orange");
        FormatContentAssert content1 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("or");
        FormatContentAssert content2 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("an")  .setFormats(FormatTypeStyle.BOLD);
        FormatContentAssert content3 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("ge");

        main.test(5,          raw,  0);
        content1.test(1,      "or", 0, 0);
        doc.assertText(0,  2, "or", 0, 0, 0);
        doc.assertKey( 2,  4, "**", 0, 1);
        content2.test(1,      "an", 0, 2);
        doc.assertText(4,  6, "an", 0, 2, 0);
        doc.assertKey( 6,  8, "**", 0, 3);
        content3.test(1,      "ge", 0, 4);
        doc.assertText(8, 10, "ge", 0, 4, 0);
        doc.assertRest();
    }

    @Test
    public void basicItalics(){
        ///           0123456
        String raw = "g*ee*n ";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("geen");
        FormatContentAssert content1 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("g");
        FormatContentAssert content2 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("ee")  .setFormats(FormatTypeStyle.ITALICS);
        FormatContentAssert content3 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(true)
            .setBoth("n ");

        main.test(5,         raw,  0);
        content1.test(1,     "g",  0, 0);
        doc.assertText(0, 1, "g",  0, 0, 0);
        doc.assertKey( 1, 2, "*",  0, 1);
        content2.test(1,     "ee", 0, 2);
        doc.assertText(2, 4, "ee", 0, 2, 0);
        doc.assertKey( 4, 5, "*",  0, 3);
        content3.test(1,     "n ", 0, 4);
        doc.assertText(5, 7, "n ", 0, 4, 0);
        doc.assertRest();
    }

    @Test
    public void basicUnderlineCoded(){
        ///           012 34567890
        String raw = "g_e\\_e`dd_";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("ge_edd");
        FormatContentAssert content1 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("g");
        FormatContentAssert content2 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("e_e") .setFormats(FormatTypeStyle.UNDERLINE);
        EscapeAssert escape = new EscapeAssert(doc).setEscape("_");
        FormatContentAssert content3 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("dd")  .setFormats(FormatTypeStyle.UNDERLINE, FormatTypeStyle.CODED);

        main.test(6,          raw,     0);
        content1.test(1,      "g",     0, 0);
        doc.assertText(0,  1, "g",     0, 0, 0);
        doc.assertKey( 1,  2, "_",     0, 1);
        content2.test(3,     "e\\_e", 0, 2);
        doc.assertText(2,  3, "e",     0, 2, 0);
        escape.test(2,        "\\_",   0, 2, 1);
        doc.assertKey( 3,  4, "\\",    0, 2, 1, 0);
        doc.assertText(4,  5, "_",     0, 2, 1, 1);
        doc.assertText(5,  6, "e",     0, 2, 2);
        doc.assertKey( 6,  7, "`",     0, 3);
        content3.test(1,      "dd",    0, 4);
        doc.assertText(7,  9, "dd",    0, 4, 0);
        doc.assertKey( 9, 10, "_",     0, 5);
        doc.assertRest();
    }

    @Test
    public void basicPointKey(){
        ///         0123456
        String raw = "{%abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("1");
        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.ERROR);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        main.test(1,          raw,   0);
        key.test(3,           raw,   0, 0);
        doc.assertKey(  0, 2, "{%",  0, 0, 0);
        data.test(1,          "abc", 0, 0, 1);
        doc.assertField(2, 5, "abc", 0, 0, 1, 0);
        doc.assertKey ( 5, 6, "}",   0, 0, 2);
        doc.assertRest();
    }

    @Test
    public void startedUnderline(){
        ///           01234
        String raw = "_abc  ab";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(0).setParsed("abc ab");
        FormatContentAssert content = new FormatContentAssert(doc)
            .setBegin(false)  .setEnd(false)
            .setBoth("abc ab").setFormats(FormatTypeStyle.UNDERLINE);

        main.test(2,         raw,       0);
        doc.assertKey( 0, 1, "_",       0, 0);
        content.test(1,      "abc  ab", 0, 1);
        doc.assertText(1, 8, "abc  ab", 0, 1, 0);
        doc.assertRest();
    }

    @Test
    public void withEndnote(){
        ///           01234567890
        String raw = "_{*abc}pee";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addRef(
            FormatCurlyTest.buildEndnoteId("abc"),
            CatalogueStatus.NOT_FOUND, 0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("pee");
        FormatNoteAssert cite = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.ENDNOTE)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setFormats(FormatTypeStyle.UNDERLINE);
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(builder).setLookup("abc");
        ContentAssert idText = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);
        FormatContentAssert content = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("pee") .setFormats(FormatTypeStyle.UNDERLINE);

        main.test(3,          raw,      0);
        doc.assertKey( 0,  1, "_",      0, 0);
        cite.test(3,          "{*abc}", 0, 1);
        doc.assertKey( 1,  3, "{*",     0, 1, 0);
        id.test(1,            "abc",    0, 1, 1);
        idText.test(1,        "abc",    0, 1, 1, 0);
        doc.assertId(  3,  6, "abc",    0, 1, 1, 0, 0);
        doc.assertKey( 6,  7, "}",      0, 1, 2);
        content.test(1,       "pee",    0, 2);
        doc.assertText(7, 10, "pee",    0, 2, 0);
        doc.assertRest();
    }

    @Test
    public void betweenTodos(){
        String text = "  abc ddd ";
        String raw = "{!todo}" + text + "{!abc}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(2).setParsed("abc ddd");
        /// "{!todo}"
        IDBuilder builder = doc.addId(FormatAgendaTest.buildId("00"), 0);
        FormatAgendaAssert todo1 = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("todo");
        ContentAssert todo1Text = new ContentAssert(doc)
            .setBegin(false).setBoth("todo")
            .setEnd(false)  .setCount(1);
        /// "  abc ddd"
        FormatContentAssert content = new FormatContentAssert(doc)
            .setBoth(" abc ddd ")  .setBegin(true)
            .setEnd(true);
        builder = doc.addId(FormatAgendaTest.buildId("17"), 1);
        /// "{!abc}"
        FormatAgendaAssert todo2 = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("abc");
        ContentAssert todo2Text = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        main.test(3,           raw,       0);
        todo1.test(3,          "{!todo}", 0, 0);
        doc.assertKey(  0,  2, "{!",     0, 0, 0);
        todo1Text.test(1,     "todo",    0, 0, 1);
        doc.assertText( 2,  6, "todo",   0, 0, 1, 0);
        doc.assertKey(  6,  7, "}",      0, 0, 2);
        content.test(1,        text,     0, 1);
        doc.assertText( 7, 17, text,     0, 1, 0);
        todo2.test(3,          "{!abc}", 0, 2);
        doc.assertKey( 17, 19, "{!",     0, 2, 0);
        todo2Text.test(1,      "abc",    0, 2, 1);
        doc.assertText(19, 22, "abc",    0, 2, 1, 0);
        doc.assertKey( 22, 23, "}",      0, 2, 2);
    }

    @Test
    public void complete(){
        String raw = "Begin\\\\* Say**'s_ Hi `Joy`**_{@note} " +
            "<@link><a.ca| web>{!todo} see";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(6).setNote(1)
            .setParsed("Begin\\ Say's Hi Joy web see");
        /// "Begin\\\\"
        FormatContentAssert text1 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("Begin\\");
        EscapeAssert escape = new EscapeAssert(doc)
            .setEscape("\\");
        /// " Say"
        FormatContentAssert text2 = new FormatContentAssert(doc)
            .setBegin(true).setEnd(false)
            .setBoth(" Say").setFormats(FormatTypeStyle.ITALICS);
        /// "'s"
        FormatContentAssert text3 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("'s").setFormats(FormatTypeStyle.BOLD, FormatTypeStyle.ITALICS);
        /// " Hi "
        FormatContentAssert text4 = new FormatContentAssert(doc)
            .setBegin(true).setEnd(true)
            .setBoth(" Hi ") .setFormats(FormatTypeStyle.BOLD,
                FormatTypeStyle.ITALICS, FormatTypeStyle.UNDERLINE);
        /// "Joy"
        FormatContentAssert text5 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("Joy") .setFormats(FormatTypeStyle.BOLD,
                FormatTypeStyle.ITALICS, FormatTypeStyle.UNDERLINE,
                FormatTypeStyle.CODED);
        /// "{@note}"
        IDBuilder builder = doc.addRef(FormatCurlyTest.buildNoteId("note"), 2);
        FormatNoteAssert cite = new FormatNoteAssert(doc)
            .setDirectoryType(DirectoryType.RESEARCH)
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setFormats(FormatTypeStyle.ITALICS);
        DirectoryAssert citeId = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(builder).setLookup("note");
        /// " "
        FormatContentAssert text6 = new FormatContentAssert(doc)
            .setBegin(true).setEnd(true)
            .setBoth(" ")   .setFormats(FormatTypeStyle.ITALICS);
        /// "<@link>"
        builder = doc.addRef(FormatLinkTest.buildLinkId("link"), 1);
        FormatRefLinkAssert ref = new FormatRefLinkAssert(doc)
            .setText("")
            .setCatalogued(CatalogueStatus.NOT_FOUND, builder)
            .setFormats(FormatTypeStyle.ITALICS);
        DirectoryAssert refId = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK).setIdentity(builder)
            .setLookup("link");
        ContentAssert refIdText = new ContentAssert(doc)
            .setBegin(false).setBoth("link")
            .setEnd(false)  .setCount(1);
        /// "<a.ca| web>"
        FormatDirectLinkAssert link = new FormatDirectLinkAssert(doc)
            .setText("web").setPath("a.ca")
            .setFormats(FormatTypeStyle.ITALICS);
        ContentAssert linkPath = new ContentAssert(doc)
            .setBegin(false).setBoth("a.ca")
            .setEnd(false)  .setCount(1);
        ContentAssert linkText = new ContentAssert(doc)
            .setBegin(true).setBoth(" web")
            .setEnd(false) .setCount(1);
        /// "{!todo}"
        builder = doc.addId(FormatAgendaTest.buildId("55"), 0);
        FormatAgendaAssert todo = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder).setAgenda("todo");
        ContentAssert todoText = new ContentAssert(doc)
            .setBegin(false).setBoth("todo")
            .setEnd(false)  .setCount(1);
        /// " see"
        FormatContentAssert text7 = new FormatContentAssert(doc)
            .setBegin(true).setEnd(false)
            .setBoth(" see").setFormats(FormatTypeStyle.ITALICS);


        ///0           1         2         3         4         5         6
        ///01234 5 6789012345678901234567890123456789012345678901234567890123456
        ///Begin\\\\* Say**'s_ Hi `Joy`**_{@note} <@link><a.ca| web>{!todo} see
        main.test(18,          raw,           0);
        text1.test(2,         "Begin\\\\",    0, 0);
        doc.assertText( 0, 5, "Begin",        0, 0, 0);
        escape.test(2,         "\\\\",        0, 0, 1);
        doc.assertKey(  5, 6,  "\\",          0, 0, 1, 0);
        doc.assertText( 6, 7,  "\\",          0, 0, 1, 1);
        doc.assertKey(  7, 8,  "*",           0, 1);
        text2.test(1,          " Say",        0, 2);
        doc.assertText( 8, 12, " Say",        0, 2, 0);
        doc.assertKey( 12, 14, "**",          0, 3);
        text3.test(1,          "'s",          0, 4);
        doc.assertText(14, 16, "'s",          0, 4, 0);
        doc.assertKey( 16, 17, "_",           0, 5);
        text4.test(1,          " Hi ",        0, 6);
        doc.assertText(17, 21, " Hi ",        0, 6, 0);
        doc.assertKey( 21, 22, "`",           0, 7);
        text5.test(1,          "Joy",         0, 8);
        doc.assertText(22, 25, "Joy",         0, 8, 0);
        doc.assertKey( 25, 26, "`",           0, 9);
        doc.assertKey( 26, 28, "**",          0, 10);
        doc.assertKey( 28, 29, "_",           0, 11);
        cite.test(3,           "{@note}",     0, 12);
        doc.assertKey( 29, 31, "{@",          0, 12, 0);
        citeId.test(1,         "note",        0, 12, 1);
        doc.assertId(  31, 35, "note",        0, 12, 1, 0, 0);
        doc.assertKey( 35, 36, "}",           0, 12, 2);
        text6.test(1,          " ",           0, 13);
        doc.assertText(36, 37, " ",           0, 13, 0);
        ref.test(3,            "<@link>",     0, 14);
        doc.assertKey( 37, 39, "<@",          0, 14, 0);
        refId.test(1,          "link",        0, 14, 1);
        refIdText.test(1,      "link",        0, 14, 1, 0);
        doc.assertId(  39, 43, "link",        0, 14, 1, 0, 0);
        doc.assertKey( 43, 44, ">",           0, 14, 2);
        doc.assertKey( 44, 45, "<",           0, 15, 0);
        link.test(5,           "<a.ca| web>", 0, 15);
        linkPath.test(1,       "a.ca",        0, 15, 1);
        doc.assertPath(45, 49, "a.ca",        0, 15, 1, 0);
        doc.assertKey( 49, 50, "|",           0, 15, 2);
        linkText.test(1,       " web",        0, 15, 3);
        doc.assertText(50, 54, " web",        0, 15, 3, 0);
        doc.assertKey( 54, 55, ">",           0, 15, 4);
        todo.test(3,           "{!todo}",     0, 16);
        doc.assertKey( 55, 57, "{!",          0, 16, 0);
        todoText.test(1,       "todo",        0, 16, 1);
        doc.assertText(57, 61, "todo",        0, 16, 1, 0);
        doc.assertKey( 61, 62, "}",           0, 16, 2);
        text7.test(1,          " see",        0, 17);
        doc.assertText(62, 66, " see",        0, 17, 0);
        ///Begin\\\\* Say**'s_ Hi `Joy`**_{@note} <@link><a.ca| web>{!todo} see
        ///01234 5 6789012345678901234567890123456789012345678901234567890123456
        ///0           1         2         3         4         5         6
        doc.assertRest();
    }

    @Test
    public void editSetEnabled(){
        ///              0123456789012
        String before = "before *bold";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(7, "*");
        commonText(doc);
    }

    private void commonText(DocumentAssert doc){
        ///             01234567890123
        String after = "before **bold";
        doc.assertDoc(1, after);

        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(0).setParsed("before bold");
        FormatContentAssert content1 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(true)
            .setBoth("before ");
        FormatContentAssert content2 = new FormatContentAssert(doc)
            .setBegin(false).setEnd(false)
            .setBoth("bold").setFormats(FormatTypeStyle.BOLD);

        main.test(3,           after,     0);
        content1.test(1,       "before ", 0, 0);
        doc.assertText(0,   7, "before ", 0, 0, 0);
        doc.assertKey( 7,   9, "**",      0, 1);
        content2.test(1,       "bold",    0, 2);
        doc.assertText(9,  13, "bold",    0, 2, 0);
    }
}
