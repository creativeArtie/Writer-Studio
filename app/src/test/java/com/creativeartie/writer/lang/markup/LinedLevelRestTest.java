package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.BranchFormatAsserts.*;
import com.creativeartie.writer.lang.markup.BranchLineAsserts.*;

import static com.creativeartie.writer.lang.DocumentAssert.*;
public class LinedLevelRestTest {
    private SetupParser[] parsers = new SetupParser[]{
        LinedParseLevel.BULLET, LinedParseLevel.NUMBERED,
        LinedParseRest.PARAGRAPH
    };

    @Test
    public void basicNumbered(){
        String raw = "#####@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineAssert numbered = new ListLevelLineAssert(doc)
            .setLevel(5)           .setNote(0)
            .setFormattedSpan(0, 1).setPublish(2);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(0).setParsed("@id:Text abc");

        numbered.test(3,      raw,           0);
        doc.assertKey( 0,  5, "#####",        0, 0);
        main.test(1,          "@id:Text abc", 0, 1);
        doc.assertChild(1,    "@id:Text abc", 0, 1, 0);
        doc.assertText(5, 17, "@id:Text abc", 0, 1, 0, 0);
        doc.assertKey(17, 18, "\n",           0, 2);

    }

    @Test
    public void basicEmptyBullet(){
        String raw = "-----\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineAssert bullet = new ListLevelLineAssert(doc)
            .setPublish(0).setNote(0)
            .setLevel(5).setNumbered(false);

        bullet.test(2,      raw,            0);
        doc.assertKey(0, 5, "-----",    0, 0);
        doc.assertKey(5, 6, "\n",           0, 1);

    }

    @Test
    public void basicBullet(){
        String raw = "-----@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        SpanBranch content = doc.assertChild(1, "@id:Text abc", 0, 1);

        ListLevelLineAssert bullet = new ListLevelLineAssert(doc)
            .setNumbered(false).setLevel(5)
            .setFormattedSpan(0, 1).setPublish(2)
            .setNote(0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(0).setParsed("@id:Text abc");

        bullet.test(3,        raw,            0);
        doc.assertKey( 0,  5, "-----",        0, 0);
        main.test(1,          "@id:Text abc", 0, 1);
        doc.assertChild(1,    "@id:Text abc", 0, 1, 0);
        doc.assertText(5, 17, "@id:Text abc", 0, 1, 0, 0);
        doc.assertKey(17, 18, "\n",           0, 2);
        doc.assertRest();
    }

    @Test
    public void bulletLevel1(){
        String text = "{@hello}{!HELP!}";
        String raw = "-" + text + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addRef(FormatCurlyTest.buildNoteId("hello"),
            CatalogueStatus.NOT_FOUND, 1);
        doc.addId(FormatAgendaTest.buildId("09"), 0);

        ListLevelLineAssert bullet = new ListLevelLineAssert(doc)
            .setNumbered(false).setLevel(1)
            .setFormattedSpan(0, 1).setPublish(0)
            .setNote(1);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(0).setNote(1);

        bullet.test(3,        raw,         0);
        doc.assertKey(  0, 1, "-",         0, 0);
        main.test(2,          text,        0, 1);
        doc.assertChild(3,    "{@hello}",  0, 1, 0);
        doc.assertKey(  1, 3, "{@",        0, 1, 0, 0);
        doc.assertChild(1,    "hello",     0, 1, 0, 1);
        doc.assertChild(1,    "hello",     0, 1, 0, 1, 0);
        doc.assertId(   3, 8, "hello",     0, 1, 0, 1, 0, 0);
        doc.assertKey(  8, 9, "}",         0, 1, 0, 2);
        doc.assertChild(3,    "{!HELP!}",  0, 1, 1);
        doc.assertKey(  9, 11, "{!",       0, 1, 1, 0);
        doc.assertChild(1,     "HELP!",     0, 1, 1, 1);
        doc.assertText(11, 16, "HELP!",    0, 1, 1, 1, 0);
        doc.assertKey( 16, 17, "}",        0, 1, 1, 2);
        doc.assertKey( 17, 18, "\n",       0, 2);
        doc.assertRest();
    }

    @Test
    public void bulletLevel6(){
        String raw = "------aaa\\\nddd";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineAssert bullet = new ListLevelLineAssert(doc)
            .setNumbered(false).setLevel(6)
            .setFormattedSpan(0, 1).setPublish(2)
            .setNote(0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(0).setParsed("aaa ddd");

        bullet.test(2,         raw,          0);
        doc.assertKey(  0,  6, "------",     0, 0);
        main.test(1,           "aaa\\\nddd", 0, 1);
        doc.assertChild(3,     "aaa\\\nddd", 0, 1, 0);
        doc.assertText( 6,  9, "aaa",        0, 1, 0, 0);
        doc.assertChild(2,     "\\\n",       0, 1, 0, 1);
        doc.assertKey(  9, 10, "\\",         0, 1, 0, 1, 0);
        doc.assertText(10, 11, "\n",         0, 1, 0, 1, 1);
        doc.assertText(11, 14, "ddd",        0, 1, 0, 2);
        doc.assertRest();
    }

    @Test
    public void basicNonNumbered(){
        ///             0123456
        String start = "######";
        ///                 678901234
        String formatted = "#**abc**";
        String raw = start + formatted + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineAssert numbered = new ListLevelLineAssert(doc)
            .setLevel(6)           .setNote(0)
            .setFormattedSpan(0, 1).setPublish(1);
        FormattedSpanAssert format = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("#abc");

        numbered.test(3,       raw,       0);
        doc.assertKey(  0,  6, start,     0, 0);
        format.test(4,         formatted, 0, 1);
        doc.assertChild(1,     "#",        0, 1, 0);
        doc.assertText( 6,  7, "#",        0, 1, 0, 0);
        doc.assertKey(  7,  9, "**",      0, 1, 1);
        doc.assertChild(1,     "abc",     0, 1, 2);
        doc.assertText( 9, 12, "abc",     0, 1, 2, 0);
        doc.assertKey( 12, 14, "**",      0, 1, 3);
        doc.assertKey( 14, 15, "\n",      0, 2);
        doc.assertRest();
    }

    @Test
    public void editBulletLevel(){
        String before = "--abc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(1, "-", 0);
        commonLevel(doc, false);
    }

    @Test
    public void editNumberedLevel(){
        String before = "##abc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(1, "#", 0);
        commonLevel(doc, true);
    }

    @Test
    public void editText(){
        ///               0 12345
        String before = "###ac";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(4, "b", 0);
        commonLevel(doc, true);
    }

    private void commonLevel(DocumentAssert doc, boolean numbered){
        ///                                       0123   0123
        String start = numbered?  "###":"---";
        ///                     3456
        String after = start + "abc";

        doc.assertDoc(1, after);

        ListLevelLineAssert bullet = new ListLevelLineAssert(doc)
            .setFormattedSpan(0, 1).setPublish(1)
            .setNote(0).setNumbered(numbered)
            .setLevel(3);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("abc");

        bullet.test(2,        after, 0);
        doc.assertKey(  0, 3, start, 0, 0);
        main.test(1,          "abc", 0, 1);
        doc.assertChild(1,    "abc", 0, 1, 0);
        doc.assertText( 3, 6, "abc", 0, 1, 0, 0);
        doc.assertRest();
    }
}
