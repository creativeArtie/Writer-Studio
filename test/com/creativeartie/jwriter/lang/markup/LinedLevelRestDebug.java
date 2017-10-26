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
        SpanBranch numbered = doc.assertChild(3, raw,            0);
        SpanBranch content  = doc.assertChild(1, "@id:Text abc", 0, 1);

        assertLevel(numbered, LinedType.NUMBERED, 5, content, 2, 0);
        FormatSpanDebug.assertMain(content, 2, 0);

        doc.assertKeyLeaf( 0,  5, "\t\t\t\t#",    0, 0);
        doc.assertTextLeaf(5, 17, "@id:Text abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(17, 18, "\n",           0, 2);

        doc.assertIds();
    }

    @Test
    public void emptyBullet(){
        String raw = "\t\t\t\t-\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch bullet = doc.assertChild(2, raw, 0);

        assertLevel(bullet, LinedType.BULLET, 5, null, 0, 0);

        doc.assertKeyLeaf(0, 5, "\t\t\t\t-",    0, 0);
        doc.assertKeyLeaf(5, 6, "\n",           0, 1);

        doc.assertIds();
    }

    @Test
    public void fullBullet(){
        String raw = "\t\t\t\t-@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch bullet  = doc.assertChild(3, raw,            0);
        SpanBranch content = doc.assertChild(1, "@id:Text abc", 0, 1);

        assertLevel(bullet, LinedType.BULLET, 5, content, 2, 0);
        FormatSpanDebug.assertMain(content, 2, 0);

        doc.assertKeyLeaf( 0,  5, "\t\t\t\t-",    0, 0);
        doc.assertTextLeaf(5, 17, "@id:Text abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(17, 18, "\n",           0, 2);

        doc.assertIds();
    }

    @Test
    public void bulletLevel1(){
        String raw = "-{@hello}{!HELP!}\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch bullet  = doc.assertChild(3, raw,        0);
        SpanBranch content = doc.assertChild(2, "{@hello}{!HELP!}", 0, 1);

        IDBuilder id = FormatCurlyDebug.buildNoteId("hello");
        doc.addRef(id, CatalogueStatus.NOT_FOUND, 1);

        assertLevel(bullet, LinedType.BULLET, 1, content, 0, 1);
        FormatSpanDebug.assertMain(content, 0, 1);
        new BranchTest.FormatAgendaTest()
            .setCatalogued(doc.addId(FormatAgendaDebug.buildId("09"), 0))
            .setText("HELP!").test(doc, 3, "{!HELP!}", 0, 1, 1);
        new BranchTest.ContentTest()
            .setText("HELP!").setBegin(false)
            .setEnd(false) .setCount(1).test(doc, 1, "HELP!", 0, 1, 1, 1);


        doc.assertKeyLeaf(  0, 1, "-",      0, 0);
        doc.assertKeyLeaf(  1, 3, "{@",     0, 1, 0, 0);
        doc.assertIdLeaf(   3, 8, "hello",  0, 1, 0, 1, 0, 0);
        doc.assertKeyLeaf(  8, 9, "}",      0, 1, 0, 2);
        doc.assertKeyLeaf(  9, 11, "{!",    0, 1, 1, 0);
        doc.assertTextLeaf(11, 16, "HELP!", 0, 1, 1, 1, 0);
        doc.assertKeyLeaf( 16, 17, "}",     0, 1, 1, 2);
        doc.assertKeyLeaf( 17, 18, "\n",    0, 2);

        doc.assertIds();
    }

    @Test
    public void bulletLevel6(){
        String raw = "\t\t\t\t\t-aaa\\\nddd";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch bullet  = doc.assertChild(2, raw,        0);
        SpanBranch content = doc.assertChild(1, "aaa\\\nddd", 0, 1);

        assertLevel(bullet, LinedType.BULLET, 6, content, 2, 0);
        FormatSpanDebug.assertMain(content, 2, 0);

        doc.assertKeyLeaf(  0,  6, "\t\t\t\t\t-", 0, 0);
        doc.assertTextLeaf( 6,  9, "aaa",         0, 1, 0, 0);
        doc.assertKeyLeaf(  9, 10, "\\",          0, 1, 0, 1, 0);
        doc.assertTextLeaf(10, 11, "\n",          0, 1, 0, 1, 1);
        doc.assertTextLeaf(11, 14, "ddd",         0, 1, 0, 2);

        doc.assertIds();
    }

    @Test
    public void outOfRangeBullet(){
        String formatted = "\t\t\t\t\t\t\t#**abc**";
        String raw = formatted + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch para    = doc.assertChild(2, raw,        0);
        SpanBranch content = doc.assertChild(4, formatted,  0, 0);

        LinedRestDebug.assertParagraph(para, content, 1, 0);
        FormatSpanDebug.assertMain(content, 1, 0);

        doc.assertTextLeaf( 0,  8, "\t\t\t\t\t\t\t#", 0, 0, 0, 0);
        doc.assertKeyLeaf(  8, 10, "**",              0, 0, 1);
        doc.assertTextLeaf(10, 13, "abc",             0, 0, 2, 0);
        doc.assertKeyLeaf( 13, 15, "**",              0, 0, 3);
        doc.assertKeyLeaf( 15, 16, "\n",              0, 1);

        doc.assertIds();
    }
}
