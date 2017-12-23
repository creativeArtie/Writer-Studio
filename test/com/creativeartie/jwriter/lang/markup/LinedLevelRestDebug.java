package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class LinedLevelRestDebug {
    private SetupParser[] parsers = new SetupParser[]{
        LinedParseLevel.BULLET, LinedParseLevel.NUMBERED,
        LinedParseRest.PARAGRAPH
    };

    @Test
    public void numbered(){
        String raw = "\t\t\t\t#@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineTest numbered = new ListLevelLineTest()
            .setLinedType(LinedType.NUMBERED).setLevel(5)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(2)
            .setNoteTotal(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(2).setNoteTotal(0);

        numbered.test(   doc,  3,  raw,           0);
        doc.assertKeyLeaf( 0,  5, "\t\t\t\t#",    0, 0);
        main.test(       doc,  1, "@id:Text abc", 0, 1);
        doc.assertChild(       1, "@id:Text abc", 0, 1, 0);
        doc.assertTextLeaf(5, 17, "@id:Text abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(17, 18, "\n",           0, 2);

        doc.assertIds();
    }

    @Test
    public void emptyBullet(){
        String raw = "\t\t\t\t-\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(5)
            .setPublishTotal(0).setNoteTotal(0);

        bullet.test(    doc, 2, raw,            0);
        doc.assertKeyLeaf(0, 5, "\t\t\t\t-",    0, 0);
        doc.assertKeyLeaf(5, 6, "\n",           0, 1);

        doc.assertIds();
    }

    @Test
    public void fullBullet(){
        String raw = "\t\t\t\t-@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        SpanBranch content = doc.assertChild(1, "@id:Text abc", 0, 1);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(5)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(2)
            .setNoteTotal(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(2).setNoteTotal(0);

        bullet.test(      doc, 3, raw,            0);
        doc.assertKeyLeaf( 0,  5, "\t\t\t\t-",    0, 0);
        main.test(       doc,  1, "@id:Text abc", 0, 1);
        doc.assertChild(       1, "@id:Text abc", 0, 1, 0);
        doc.assertTextLeaf(5, 17, "@id:Text abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(17, 18, "\n",           0, 2);

        doc.assertIds();
    }

    @Test
    public void bulletLevel1(){
        String text = "{@hello}{!HELP!}";
        String raw = "-" + text + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = FormatCurlyDebug.buildNoteId("hello");
        doc.addRef(id, CatalogueStatus.NOT_FOUND, 1);
        doc.addId(FormatAgendaDebug.buildId("09"), 0);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(1)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(0)
            .setNoteTotal(1);
        FormatMainTest main = new FormatMainTest()
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

        doc.assertIds();
    }

    @Test
    public void bulletLevel6(){
        String raw = "\t\t\t\t\t-aaa\\\nddd";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(6)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(2)
            .setNoteTotal(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(2).setNoteTotal(0);

        bullet.test(      doc,  2, raw,           0);
        doc.assertKeyLeaf(  0,  6, "\t\t\t\t\t-", 0, 0);
        main.test(        doc,  1, "aaa\\\nddd",  0, 1);
        doc.assertChild(        3, "aaa\\\nddd",  0, 1, 0);
        doc.assertTextLeaf( 6,  9, "aaa",         0, 1, 0, 0);
        doc.assertChild(        2, "\\\n",        0, 1, 0, 1);
        doc.assertKeyLeaf(  9, 10, "\\",          0, 1, 0, 1, 0);
        doc.assertTextLeaf(10, 11, "\n",          0, 1, 0, 1, 1);
        doc.assertTextLeaf(11, 14, "ddd",         0, 1, 0, 2);

        doc.assertIds();
    }

    @Test
    public void outOfRangeBullet(){
        String start = "\t\t\t\t\t\t\t#";
        String formatted = start + "**abc**";
        String raw = formatted + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ParagraphLineTest paragraph = new ParagraphLineTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);

        paragraph.test(   doc,  2, raw,       0);
        main.test(        doc,  4, formatted, 0, 0);
        doc.assertChild(        1, start,     0, 0, 0);
        doc.assertTextLeaf( 0,  8, start,     0, 0, 0, 0);
        doc.assertKeyLeaf(  8, 10, "**",      0, 0, 1);
        doc.assertTextLeaf(10, 13, "abc",     0, 0, 2, 0);
        doc.assertKeyLeaf( 13, 15, "**",      0, 0, 3);
        doc.assertKeyLeaf( 15, 16, "\n",      0, 1);

        doc.assertIds();
    }

    @Test
    public void editBulletLevel(){
        String before = "\t-abc";
        DocumentAssert doc = assertDoc(1, before, parsers);

        doc.insert(1, "\t", 0);
        editCommon(doc, LinedType.BULLET);
    }

    @Test
    public void editNumberedLevel(){
        String before = "\t#abc";
        DocumentAssert doc = assertDoc(1, before, parsers);

        doc.insert(1, "\t", 0);
        editCommon(doc, LinedType.NUMBERED);
    }

    @Test
    public void editText(){
        ///               0 12345
        String before = "\t\t#ac";
        DocumentAssert doc = assertDoc(1, before, parsers);

        doc.insert(4, "b", 0);
        editCommon(doc, LinedType.NUMBERED);
    }

    private void editCommon(DocumentAssert doc, LinedType type){
        ///              0 12                               23   23
        String start = "\t\t" + (type == LinedType.BULLET? "-": "#");
        ///                     3456
        String after = start + "abc";

        doc.assertDoc(1, after);

        ListLevelLineTest bullet = new ListLevelLineTest()
            .setLinedType(type).setLevel(3)
            .setFormattedSpan(doc, 0, 1).setPublishTotal(1)
            .setNoteTotal(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);

        bullet.test(      doc, 2, after, 0);
        doc.assertKeyLeaf(  0, 3, start, 0, 0);
        main.test(        doc, 1, "abc", 0, 1);
        doc.assertChild(       1, "abc", 0, 1, 0);
        doc.assertTextLeaf( 3, 6, "abc", 0, 1, 0, 0);

        doc.assertIds();
    }
}
