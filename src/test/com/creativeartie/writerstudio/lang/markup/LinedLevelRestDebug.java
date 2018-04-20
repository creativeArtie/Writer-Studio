package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
import static com.creativeartie.writerstudio.lang.markup.BranchLineTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;

@RunWith(JUnit4.class)
public class LinedLevelRestDebug {
    private SetupParser[] parsers = new SetupParser[]{
        LinedParseLevel.BULLET, LinedParseLevel.NUMBERED,
        LinedParseRest.PARAGRAPH
    };

    @Test
    public void basicNumbered(){
        String raw = "#####@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineTest numbered = new ListLevelLineTest()
            .setLinedType(LinedType.NUMBERED).setLevel(5)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(2)
            .setNoteTotal(0);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(2).setNoteTotal(0);

        numbered.test(   doc,  3,  raw,           0);
        doc.assertKeyLeaf( 0,  5, "#####",        0, 0);
        main.test(       doc,  1, "@id:Text abc", 0, 1);
        doc.assertChild(       1, "@id:Text abc", 0, 1, 0);
        doc.assertTextLeaf(5, 17, "@id:Text abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(17, 18, "\n",           0, 2);

        doc.assertIds();
    }

    @Test
    public void basicEmptyBullet(){
        String raw = "-----\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(5)
            .setPublishTotal(0).setNoteTotal(0);

        bullet.test(    doc, 2, raw,            0);
        doc.assertKeyLeaf(0, 5, "-----",    0, 0);
        doc.assertKeyLeaf(5, 6, "\n",           0, 1);

        doc.assertIds();
    }

    @Test
    public void basicBullet(){
        String raw = "-----@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        SpanBranch content = doc.assertChild(1, "@id:Text abc", 0, 1);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(5)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(2)
            .setNoteTotal(0);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(2).setNoteTotal(0);

        bullet.test(      doc, 3, raw,            0);
        doc.assertKeyLeaf( 0,  5, "-----",        0, 0);
        main.test(       doc,  1, "@id:Text abc", 0, 1);
        doc.assertChild(       1, "@id:Text abc", 0, 1, 0);
        doc.assertTextLeaf(5, 17, "@id:Text abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(17, 18, "\n",           0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void bulletLevel1(){
        String text = "{@hello}{!HELP!}";
        String raw = "-" + text + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addRef(FormatCurlyDebug.buildNoteId("hello"),
            CatalogueStatus.NOT_FOUND, 1);
        doc.addId(FormatAgendaDebug.buildId("09"), 0);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(1)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(0)
            .setNoteTotal(1);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(0).setNoteTotal(1);

        bullet.test(      doc, 3, raw,         0);
        doc.assertKeyLeaf(  0, 1, "-",         0, 0);
        main.test(        doc, 2, text,        0, 1);
        doc.assertChild(       3, "{@hello}",  0, 1, 0);
        doc.assertKeyLeaf(  1, 3, "{@",        0, 1, 0, 0);
        doc.assertChild(       1, "hello",     0, 1, 0, 1);
        doc.assertChild(       1, "hello",     0, 1, 0, 1, 0);
        doc.assertIdLeaf(   3, 8, "hello",     0, 1, 0, 1, 0, 0);
        doc.assertKeyLeaf(  8, 9, "}",         0, 1, 0, 2);
        doc.assertChild(       3, "{!HELP!}",  0, 1, 1);
        doc.assertKeyLeaf(  9, 11, "{!",       0, 1, 1, 0);
        doc.assertChild(       1, "HELP!",     0, 1, 1, 1);
        doc.assertTextLeaf(11, 16, "HELP!",    0, 1, 1, 1, 0);
        doc.assertKeyLeaf( 16, 17, "}",        0, 1, 1, 2);
        doc.assertKeyLeaf( 17, 18, "\n",       0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void bulletLevel6(){
        String raw = "------aaa\\\nddd";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(6)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(2)
            .setNoteTotal(0);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(2).setNoteTotal(0);

        bullet.test(      doc,  2, raw,          0);
        doc.assertKeyLeaf(  0,  6, "------",     0, 0);
        main.test(        doc,  1, "aaa\\\nddd", 0, 1);
        doc.assertChild(        3, "aaa\\\nddd", 0, 1, 0);
        doc.assertTextLeaf( 6,  9, "aaa",        0, 1, 0, 0);
        doc.assertChild(        2, "\\\n",       0, 1, 0, 1);
        doc.assertKeyLeaf(  9, 10, "\\",         0, 1, 0, 1, 0);
        doc.assertTextLeaf(10, 11, "\n",         0, 1, 0, 1, 1);
        doc.assertTextLeaf(11, 14, "ddd",        0, 1, 0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void basicNonNumbered(){
        ///             0123456
        String start = "######";
        ///                 678901234
        String formatted = "#**abc**";
        String raw = start + formatted + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineTest numbered = new ListLevelLineTest()
            .setLinedType(LinedType.NUMBERED).setLevel(6)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(1)
            .setNoteTotal(0);
        FormattedSpanTest format = new FormattedSpanTest()
            .setPublishTotal(1).setNoteTotal(0);

        numbered.test(    doc,  3, raw,       0);
        doc.assertKeyLeaf(  0,  6, start,     0, 0);
        format.test(      doc,  4, formatted, 0, 1);
        doc.assertChild(       1, "#",        0, 1, 0);
        doc.assertTextLeaf( 6, 7, "#",        0, 1, 0, 0);
        doc.assertKeyLeaf(  7,  9, "**",      0, 1, 1);
        doc.assertChild(        1, "abc",     0, 1, 2);
        doc.assertTextLeaf( 9, 12, "abc",     0, 1, 2, 0);
        doc.assertKeyLeaf( 12, 14, "**",      0, 1, 3);
        doc.assertKeyLeaf( 14, 15, "\n",      0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void editBulletLevel(){
        String before = "--abc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(1, "-", 0);
        editCommon(doc, LinedType.BULLET);
    }

    @Test
    public void editNumberedLevel(){
        String before = "##abc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(1, "#", 0);
        editCommon(doc, LinedType.NUMBERED);
    }

    @Test
    public void editText(){
        ///               0 12345
        String before = "###ac";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(4, "b", 0);
        editCommon(doc, LinedType.NUMBERED);
    }

    private void editCommon(DocumentAssert doc, LinedType type){
        ///                                       0123   0123
        String start = type == LinedType.BULLET? "---": "###";
        ///                     3456
        String after = start + "abc";

        doc.assertDoc(1, after);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(type).setLevel(3)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(1)
            .setNoteTotal(0);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(1).setNoteTotal(0);

        bullet.test(      doc, 2, after, 0);
        doc.assertKeyLeaf(  0, 3, start, 0, 0);
        main.test(        doc, 1, "abc", 0, 1);
        doc.assertChild(       1, "abc", 0, 1, 0);
        doc.assertTextLeaf( 3, 6, "abc", 0, 1, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }
}
