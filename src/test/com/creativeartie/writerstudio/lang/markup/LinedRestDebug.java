package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
import static com.creativeartie.writerstudio.lang.markup.BranchTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchLineTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;

@RunWith(JUnit4.class)
public class LinedRestDebug {

    public static IDBuilder buildAgendaId(String name){
        return new IDBuilder().addCategory("agenda").setId(name);
    }

    private static final SetupParser[] parsers = LinedParseRest.values();

    @Test
    public void breakBasic(){
        String raw = "***\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        breakEditCommon(doc);
    }

    @Test
    public void breakEditCreate(){
        String before = "**\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(1, "*");
        doc.assertDoc(1, "***\n", parsers);
        breakEditCommon(doc);
    }

    private void breakEditCommon(DocumentAssert doc){
        String raw = "***\n";
        BreakLineTest line = new BreakLineTest();

        line.test(      doc, 1, raw, 0);
        doc.assertKeyLeaf(0, 4, raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void breakEditRemoveByInsert(){
        String before = "***\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(1, "*");
        String after = "****\n";
        doc.assertDoc(1, after, parsers);

        ParagraphLineTest paragraph = new ParagraphLineTest()
            .setPublishTotal(0).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(0).setNoteTotal(0);

        paragraph.test( doc, 2, after,  0);
        main.test(      doc, 2, "****", 0, 0);
        doc.assertKeyLeaf(0, 2, "**",   0, 0, 0);
        doc.assertKeyLeaf(2, 4, "**",   0, 0, 1);
        doc.assertKeyLeaf(4, 5, "\n",   0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void breakEditRemoveByDelete(){
        String before = "***\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(0, 1);
        String after = "**\n";
        doc.assertDoc(1, after, parsers);

        ParagraphLineTest paragraph = new ParagraphLineTest()
            .setPublishTotal(0).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(0).setNoteTotal(0);

        paragraph.test( doc, 2, after, 0);
        main.test(      doc, 1, "**",  0, 0);
        doc.assertKeyLeaf(0, 2, "**",  0, 0, 0);
        doc.assertKeyLeaf(2, 3, "\n",  0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void agendaBasic(){
        String raw = "!!abc**ab";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        doc.assertDoc(1, "!!abc**ab", parsers);
        agendaEditCommon(doc);
    }

    @Test
    public void agendaEditText(){
        ///           0123456
        String before = "!!abcb";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(5, "**a", 0);
        doc.assertDoc(1, "!!abc**ab");
        agendaEditCommon(doc);
    }

    @Test
    public void agendaEditCreated(){
        ///           012345678
        String before = "!abc**ab";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(1, "!");
        doc.assertDoc(1, "!!abc**ab");
        agendaEditCommon(doc);
    }

    private void agendaEditCommon(DocumentAssert doc){
        String text = "abc**ab";
        String raw = "!!" + text;
        IDBuilder id = doc.addId(buildAgendaId("0"), 0);

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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void agendaEditRemoved(){
        ///              0123456
        String before = "!!abc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(0, 2);
        doc.assertDoc(1, "abc");
        paragraphEditCommon(doc);
    }


    @Test
    public void agendaEmpty(){
        String raw = "!!";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addId(buildAgendaId("0"), 0);

        AgendaLineTest agenda = new AgendaLineTest()
            .setAgenda("").setNoteTotal(0)
            .setCatalogued(CatalogueStatus.UNUSED, id);

        agenda.test(    doc,  1, raw,  0);
        doc.assertKeyLeaf( 0, 2, "!!", 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void agendaSpaced(){
        String raw = "!!  \n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addId(buildAgendaId("0"), 0);

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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void agendaFull(){
        String raw = "!!ab\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addId(buildAgendaId("0"), 0);

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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void agendaEscaped(){
        String text = "Hi\\\\";
        String raw = "!!" + text + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addId(buildAgendaId("0"), 0);

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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void quoteBasic(){
        String raw = ">>@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        quoteEditCommon(doc);
    }

    @Test
    public void quoteEditText(){
        ///              0123456789 01
        String before = ">>@idt abc\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(5, ":Tex", 0);
        doc.assertDoc(1, ">>@id:Text abc\n");
        quoteEditCommon(doc);
    }

    @Test
    public void quoteEditCreate(){
        ///              0123456789 01
        String before = "@id:Text abc\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(0, ">>");
        doc.assertDoc(1, ">>@id:Text abc\n");
        quoteEditCommon(doc);
    }

    private void quoteEditCommon(DocumentAssert doc){
        String text = ">@id:Text abc";
        String raw = ">" + text + "\n";
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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void quoteEditRemove(){
        ///              01234
        String before = ">abc";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(0, 1);
        doc.assertDoc(1, "abc");
        paragraphEditCommon(doc);
    }

    @Test
    public void paragraphFull(){
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
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void paragraphBasic(){
        String raw = "abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        paragraphEditCommon(doc);
    }

    @Test
    public void paragraphEditText(){
        String raw = "ac";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        doc.insert(1, "b", 0);
        doc.assertDoc(1, "abc");
        paragraphEditCommon(doc);
    }

    private void paragraphEditCommon(DocumentAssert doc){
        String raw = "abc";

        ParagraphLineTest paragraph = new ParagraphLineTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0);
        FormatMainTest main = new FormatMainTest()
            .setPublishTotal(1).setNoteTotal(0);

        paragraph.test(  doc, 1, raw,   0);
        main.test(       doc, 1, "abc", 0, 0);
        doc.assertChild(      1, "abc", 0, 0, 0);
        doc.assertTextLeaf(0, 3, "abc", 0, 0, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

}
