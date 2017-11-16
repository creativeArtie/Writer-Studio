package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class LinedRestDebug {

    private static final SetupParser[] parsers = LinedParseRest.values();

    @Test
    public void testBreak(){
        String raw = "***\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        BreakLineTest line = new BreakLineTest();

        line.test(      doc, 1, raw,      0);
        doc.assertKeyLeaf(0, 4, "***\n",  0, 0);

        doc.assertIds();
    }


    @Test
    public void basicAgenda(){
        String text = "abc**ab";
        String raw = "!!" + text;
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        AgendaLineTest agenda = new AgendaLineTest()
            .setAgenda(text).setNoteTotal(1)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        ContentTest content = new ContentTest()
            .setText(text) .setBegin(false)
            .setEnd(false).setCount(1);

        agenda.test(    doc,  2, raw,    0);
        doc.assertKeyLeaf( 0, 2, "!!",   0, 0);
        content.test(    doc, 1, text, 0, 1);
        doc.assertTextLeaf(2, 9, text, 0, 1, 0);

        doc.assertIds();
    }

    @Test
    public void emptyAgenda(){
        String raw = "!!";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        AgendaLineTest agenda = new AgendaLineTest()
            .setAgenda("").setNoteTotal(0)
            .setCatalogued(CatalogueStatus.UNUSED, id);

        agenda.test(    doc,  1, raw,  0);
        doc.assertKeyLeaf( 0, 2, "!!", 0, 0);

        doc.assertIds();
    }

    @Test
    public void spaceAgenda(){
        String raw = "!!  \n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        AgendaLineTest agenda = new AgendaLineTest()
            .setAgenda("").setNoteTotal(0)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        ContentTest content = new ContentTest()
            .setText("") .setBegin(true)
            .setEnd(true).setCount(0);

        agenda.test(    doc,  3, raw,  0);
        doc.assertKeyLeaf( 0, 2, "!!", 0, 0);
        content.test(    doc, 1, "  ", 0, 1);
        doc.assertTextLeaf(2, 4, "  ", 0, 1, 0);
        doc.assertKeyLeaf( 4, 5, "\n", 0, 2);

        doc.assertIds();
    }

    @Test
    public void fullAgenda(){
        String raw = "!!ab\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        AgendaLineTest agenda = new AgendaLineTest()
            .setAgenda("ab").setNoteTotal(1)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        ContentTest content = new ContentTest()
            .setText("ab")  .setBegin(false)
            .setEnd(false).setCount(1);

        agenda.test(    doc,  3, raw,  0);
        doc.assertKeyLeaf( 0, 2, "!!", 0, 0);
        content.test(    doc, 1, "ab", 0, 1);
        doc.assertTextLeaf(2, 4, "ab", 0, 1, 0);
        doc.assertKeyLeaf( 4, 5, "\n", 0, 2);

        doc.assertIds();
    }

    @Test
    public void escapeAgenda(){
        String text = "Hi\\\\";
        String raw = "!!" + text + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = new IDBuilder().addCategory("agenda")
            .setId("0");
        doc.addId(id, 0);

        AgendaLineTest agenda = new AgendaLineTest()
            .setAgenda("Hi\\").setNoteTotal(1)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        ContentTest content = new ContentTest()
            .setText("Hi\\").setBegin(false)
            .setEnd(false)  .setCount(1);

        agenda.test(    doc,  3, raw,    0);
        doc.assertKeyLeaf( 0, 2, "!!",   0, 0);
        content.test(    doc, 2, text,   0, 1);
        doc.assertTextLeaf(2, 4, "Hi",   0, 1, 0);
        doc.assertChild(      2, "\\\\", 0, 1, 1);
        doc.assertKeyLeaf( 4, 5, "\\",   0, 1, 1, 0);
        doc.assertTextLeaf(5, 6, "\\",   0, 1, 1, 1);
        doc.assertKeyLeaf( 6, 7, "\n",   0, 2);

        doc.assertIds();
    }

    @Test
    public void quote(){
        String text = ">@id:Text abc";
        String raw = ">" + text + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        QuoteLineTest quote = new QuoteLineTest()
            .setFormattedSpan(doc, 0, 1)
            .setPublishTotal(2).setNoteTotal(0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(2).setNoteTotal(0);

        quote.test(     doc,   3, raw,  0);
        doc.assertKeyLeaf( 0,  1, ">",  0, 0);
        main.test(       doc,  1, text, 0, 1);
        doc.assertChild(       1, text, 0, 1, 0);
        doc.assertTextLeaf(1, 14, text, 0, 1, 0, 0);
        doc.assertKeyLeaf(14, 15, "\n", 0, 2);

        doc.assertIds();
    }

    @Test
    public void fullParagraph(){
        String text = "ddHi\\\\";
        String raw = text + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ParagraphLineTest paragraph = new ParagraphLineTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);

        paragraph.test(  doc, 2, raw,    0);
        main.test(       doc, 1, text,   0, 0);
        doc.assertChild(      2, text,   0, 0, 0);
        doc.assertTextLeaf(0, 4, "ddHi", 0, 0, 0, 0);
        doc.assertChild(      2, "\\\\", 0, 0, 0, 1);
        doc.assertKeyLeaf( 4, 5, "\\",   0, 0, 0, 1, 0);
        doc.assertTextLeaf(5, 6, "\\",   0, 0, 0, 1, 1);
        doc.assertKeyLeaf( 6, 7, "\n",   0, 1);

        doc.assertIds();
    }

    @Test
    public void simpleParagraph(){
        String raw = "abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        ParagraphLineTest paragraph = new ParagraphLineTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);

        paragraph.test(  doc, 1, raw,   0);
        main.test(       doc, 1, "abc", 0, 0);
        doc.assertChild(      1, "abc", 0, 0, 0);
        doc.assertTextLeaf(0, 3, "abc", 0, 0, 0, 0);

        doc.assertIds();
    }

}
