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
public class LinedRestDebug {
    public static final void assertLine(SpanBranch span, int publish, int note){
        LinedSpan test = (LinedSpan) span;
        assertEquals(getError("publish", span), publish, test.getPublishCount());
        assertEquals(getError("note", span), note, test.getNoteCount());
    }

    public static final void assertBreak(SpanBranch span){
        LinedSpanBreak test = assertClass(span, LinedSpanBreak.class);

        DetailStyle[] styles = new DetailStyle[]{LinedType.BREAK};

        assertEquals(getError("type", span), LinedType.BREAK, test.getLinedType());
        assertLine(test, 0, 0);
        assertBranch(span, styles);
    }

    public static final void assertAgenda(SpanBranch span, Span agenda, int note,
        IDBuilder id)
    {
        LinedSpanAgenda test = assertClass(span, LinedSpanAgenda.class);

        DetailStyle[] styles = new DetailStyle[]{LinedType.AGENDA};

        assertEquals(getError("type", span), LinedType.AGENDA, test.getLinedType());
        assertSpan("reason", agenda, test.getAgendaSpan());
        assertSpanIdentity(span, id);
        assertLine(test, 0, note);
        assertBranch(span, styles, CatalogueStatus.UNUSED);
    }

    public static final void assertQuote(SpanBranch span, Span format,
        int publish, int note)
    {
        LinedSpanQuote test = assertClass(span, LinedSpanQuote.class);

        DetailStyle[] styles = new DetailStyle[]{LinedType.QUOTE};

        assertEquals(getError("type", span), LinedType.QUOTE, test.getLinedType());
        assertSpan("text", format, test.getFormattedSpan());
        assertLine(test, publish, note);
        assertBranch(span, styles);
    }

    public static final void assertParagraph(SpanBranch span, Span format,
        int publish, int note)
    {
        LinedSpanParagraph test = assertClass(span, LinedSpanParagraph.class);

        DetailStyle[] styles = new DetailStyle[]{LinedType.PARAGRAPH};

        assertEquals(getError("type", span), LinedType.PARAGRAPH, test.getLinedType());
        assertSpan("text", format, test.getFormattedSpan());
        assertLine(test, publish, note);
        assertBranch(span, styles);
    }

    private static final SetupParser[] parsers = LinedParseRest.values();

    @Test
    public void testBreak(){
        String raw = "***\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch span = doc.assertChild(1, raw, 0);

        assertBreak(span);

        doc.assertKeyLeaf(0, 4, "***\n",  0, 0);

        doc.assertIds();
    }


    @Test
    public void basicAgenda(){
        String raw = "!!abc**ab";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch agenda  = doc.assertChild(2, raw, 0);
        SpanBranch content = doc.assertChild(1, "abc**ab", 0 , 1);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        assertAgenda(agenda, content, 1, id);

        doc.assertKeyLeaf( 0, 2, "!!",      0, 0);
        doc.assertTextLeaf(2, 9, "abc**ab", 0, 1, 0);

        doc.assertIds();
    }

    @Test
    public void emptyAgenda(){
        String raw = "!!";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch agenda  = doc.assertChild(1, raw, 0);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        assertAgenda(agenda, null, 0, id);

        doc.assertKeyLeaf( 0, 2, "!!", 0, 0);

        doc.assertIds();
    }

    @Test
    public void spaceAgenda(){
        String raw = "!!  \n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch agenda  = doc.assertChild(3, raw, 0);
        SpanBranch content = doc.assertChild(1, "  ", 0 , 1);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        assertAgenda(agenda, content, 0, id);

        doc.assertKeyLeaf( 0, 2, "!!", 0, 0);
        doc.assertTextLeaf(2, 4, "  ", 0, 1, 0);
        doc.assertKeyLeaf( 4, 5, "\n", 0, 2);

        doc.assertIds();
    }

    @Test
    public void fullAgenda(){
        String raw = "!!ab\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch agenda  = doc.assertChild(3, raw, 0);
        SpanBranch content = doc.assertChild(1, "ab", 0 , 1);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        assertAgenda(agenda, content, 1, id);

        doc.assertKeyLeaf( 0, 2, "!!", 0, 0);
        doc.assertTextLeaf(2, 4, "ab", 0, 1, 0);
        doc.assertKeyLeaf( 4, 5, "\n", 0, 2);

        doc.assertIds();
    }

    @Test
    public void escapeAgenda(){
        String raw = "!!Hi\\\\\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch agenda  = doc.assertChild(3, raw, 0);
        SpanBranch content = doc.assertChild(2, "Hi\\\\", 0 , 1);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        assertAgenda(agenda, content, 1, id);

        doc.assertKeyLeaf( 0, 2, "!!", 0, 0);
        doc.assertTextLeaf(2, 4, "Hi", 0, 1, 0);
        doc.assertKeyLeaf( 4, 5, "\\", 0, 1, 1, 0);
        doc.assertTextLeaf(5, 6, "\\", 0, 1, 1, 1);
        doc.assertKeyLeaf( 6, 7, "\n", 0, 2);

        doc.assertIds();
    }

    @Test
    public void quote(){
        String raw = ">>@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch quote   = doc.assertChild(3, raw,            0);
        SpanBranch content = doc.assertChild(1, ">@id:Text abc", 0, 1);

        assertQuote(quote, content, 2, 0);
        FormatSpanDebug.assertMain(content, 2, 0);

        doc.assertKeyLeaf( 0,  1, ">",             0, 0);
        doc.assertTextLeaf(1, 14, ">@id:Text abc", 0, 1, 0, 0);
        doc.assertKeyLeaf(14, 15, "\n",            0, 2);

        doc.assertIds();
    }

    @Test
    public void fullParagraph(){
        String raw = "ddHi\\\\\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch paragraph = doc.assertChild(2, raw, 0);
        SpanBranch content   = doc.assertChild(1, "ddHi\\\\", 0 , 0);

        assertParagraph(paragraph, content, 1, 0);

        doc.assertTextLeaf(0, 4, "ddHi", 0, 0, 0, 0);
        doc.assertKeyLeaf( 4, 5, "\\",   0, 0, 0, 1, 0);
        doc.assertTextLeaf(5, 6, "\\",   0, 0, 0, 1, 1);
        doc.assertKeyLeaf( 6, 7, "\n",   0, 1);

        doc.assertIds();
    }

    @Test
    public void simpleParagraph(){
        String raw = "abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch paragraph = doc.assertChild(1, raw, 0);
        SpanBranch content   = doc.assertChild(1, "abc", 0 , 0);

        assertParagraph(paragraph, content, 1, 0);

        doc.assertTextLeaf(0, 3, "abc", 0, 0, 0, 0);

        doc.assertIds();
    }

}
