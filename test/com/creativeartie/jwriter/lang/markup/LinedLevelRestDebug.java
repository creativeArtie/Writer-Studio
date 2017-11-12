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

    public static void assertLevel(SpanBranch span, LinedType lined, int level,
        Span text, int publish, int note)
    {
        LinedSpanLevel test = assertClass(span, LinedSpanLevel.class);
        assertLevel(test, lined, level, text, CatalogueStatus.NO_ID);
        LinedRestDebug.assertLine(test, publish, note);
    }
    public static void assertLevel(LinedSpanLevel test, LinedType lined,
        int level, Span text, CatalogueStatus status)
    {
        DetailStyle[] styles = new DetailStyle[]{lined};
        assertEquals(getError("lined", test), lined,  test.getLinedType());
        assertSpan(getError("text", test), text,  test.getFormattedSpan());
        assertEquals(getError("level", test),      level,  test.getLevel());
        assertBranch(test, styles, status);
    }



    @Test
    public void numbered(){
        String raw = "\t\t\t\t#@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        BasicLevelLineTest numbered = new BasicLevelLineTest()
            .setLinedType(LinedType.NUMBERED).setLevel(5)
            .setFormattedSpan(doc, 0, 1).setPublishCount(2)
            .setNoteCount(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(2).setNoteCount(0);

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

        BasicLevelLineTest bullet = new BasicLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(5)
            .setPublishCount(0).setNoteCount(0);

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

        BasicLevelLineTest bullet = new BasicLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(5)
            .setFormattedSpan(doc, 0, 1).setPublishCount(2)
            .setNoteCount(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(2).setNoteCount(0);

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

        BasicLevelLineTest bullet = new BasicLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(1)
            .setFormattedSpan(doc, 0, 1).setPublishCount(0)
            .setNoteCount(1);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(0).setNoteCount(1);

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

        BasicLevelLineTest bullet = new BasicLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(6)
            .setFormattedSpan(doc, 0, 1).setPublishCount(2)
            .setNoteCount(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(2).setNoteCount(0);

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
            .setPublishCount(1).setNoteCount(0)
            .setFormattedSpan(doc, 0, 0);
        FormatMainTest main = new FormatMainTest()
            .setPublishCount(1).setNoteCount(0);

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
}
