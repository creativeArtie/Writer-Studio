package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchFormatAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchLineAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class LinedRestTest {

    public static IDBuilder buildAgendaId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_AGENDA).setId(name);
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
        BreakLineAssert line = new BreakLineAssert(doc);

        line.test(1,        raw, 0);
        doc.assertKey(0, 4, raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void breakEditRemoveByInsert(){
        String before = "***\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(1, "*");
        String after = "****\n";
        doc.assertDoc(1, after, parsers);

        ParagraphLineAssert paragraph = new ParagraphLineAssert(doc)
            .setPublish(0).setNote(0)
            .setFormattedSpan(0, 0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(0).setNote(0);

        paragraph.test(2,   after,  0);
        main.test(2,        "****", 0, 0);
        doc.assertKey(0, 2, "**",   0, 0, 0);
        doc.assertKey(2, 4, "**",   0, 0, 1);
        doc.assertKey(4, 5, "\n",   0, 1);
        doc.assertRest();
    }

    @Test
    public void breakEditRemoveByDelete(){
        String before = "***\n";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.delete(0, 1);
        String after = "**\n";
        doc.assertDoc(1, after, parsers);

        ParagraphLineAssert paragraph = new ParagraphLineAssert(doc)
            .setPublish(0).setNote(0)
            .setFormattedSpan(0, 0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(0).setNote(0);

        paragraph.test(2,   after, 0);
        main.test(1,        "**",  0, 0);
        doc.assertKey(0, 2, "**",  0, 0, 0);
        doc.assertKey(2, 3, "\n",  0, 1);
        doc.assertRest();
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

        AgendaLineAssert agenda = new AgendaLineAssert(doc)
            .setAgenda(text).setNote(1)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        ContentAssert content = new ContentAssert(doc)
            .setBoth(text) .setBegin(false)
            .setEnd(false).setCount(1);

        agenda.test(2,       raw,    0);
        doc.assertKey( 0, 2, "!!",   0, 0);
        content.test(1,      text, 0, 1);
        doc.assertText(2, 9, text, 0, 1, 0);
        doc.assertRest();
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

        AgendaLineAssert agenda = new AgendaLineAssert(doc)
            .setAgenda("").setNote(0)
            .setCatalogued(CatalogueStatus.UNUSED, id);

        agenda.test(1,       raw,  0);
        doc.assertKey( 0, 2, "!!", 0, 0);
        doc.assertRest();
    }

    @Test
    public void agendaSpaced(){
        String raw = "!!  \n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addId(buildAgendaId("0"), 0);

        AgendaLineAssert agenda = new AgendaLineAssert(doc)
            .setAgenda("").setNote(0)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        ContentAssert content = new ContentAssert(doc)
            .setBoth("") .setBegin(true)
            .setEnd(true).setCount(0);

        agenda.test(3,       raw,  0);
        doc.assertKey( 0, 2, "!!", 0, 0);
        content.test(1,      "  ", 0, 1);
        doc.assertText(2, 4, "  ", 0, 1, 0);
        doc.assertKey( 4, 5, "\n", 0, 2);
        doc.assertRest();
    }

    @Test
    public void agendaFull(){
        String raw = "!!ab\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addId(buildAgendaId("0"), 0);

        AgendaLineAssert agenda = new AgendaLineAssert(doc)
            .setAgenda("ab").setNote(1)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        ContentAssert content = new ContentAssert(doc)
            .setBoth("ab")  .setBegin(false)
            .setEnd(false).setCount(1);

        agenda.test(3,       raw,  0);
        doc.assertKey( 0, 2, "!!", 0, 0);
        content.test(1,      "ab", 0, 1);
        doc.assertText(2, 4, "ab", 0, 1, 0);
        doc.assertKey( 4, 5, "\n", 0, 2);
        doc.assertRest();
    }

    @Test
    public void agendaEscaped(){
        String text = "Hi\\\\";
        String raw = "!!" + text + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addId(buildAgendaId("0"), 0);

        AgendaLineAssert agenda = new AgendaLineAssert(doc)
            .setAgenda("Hi\\").setNote(1)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        ContentAssert content = new ContentAssert(doc)
            .setBoth("Hi\\").setBegin(false)
            .setEnd(false)  .setCount(1);

        agenda.test(3,       raw,    0);
        doc.assertKey( 0, 2, "!!",   0, 0);
        content.test(2,      text,   0, 1);
        doc.assertText(2, 4, "Hi",   0, 1, 0);
        doc.assertChild(2,   "\\\\", 0, 1, 1);
        doc.assertKey( 4, 5, "\\",   0, 1, 1, 0);
        doc.assertText(5, 6, "\\",   0, 1, 1, 1);
        doc.assertKey( 6, 7, "\n",   0, 2);
        doc.assertRest();
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
        QuoteLineAssert quote = new QuoteLineAssert(doc)
            .setFormattedSpan(0, 1)
            .setPublish(2).setNote(0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(2).setNote(0);

        quote.test(3,         raw,  0);
        doc.assertKey( 0,  1, ">",  0, 0);
        main.test(1,          text, 0, 1);
        doc.assertChild(1,    text, 0, 1, 0);
        doc.assertText(1, 14, text, 0, 1, 0, 0);
        doc.assertKey(14, 15, "\n", 0, 2);
        doc.assertRest();
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

        ParagraphLineAssert paragraph = new ParagraphLineAssert(doc)
            .setPublish(1).setNote(0)
            .setFormattedSpan(0, 0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0);

        paragraph.test(2,    raw,    0);
        main.test(1,         text,   0, 0);
        doc.assertChild(2,   text,   0, 0, 0);
        doc.assertText(0, 4, "ddHi", 0, 0, 0, 0);
        doc.assertChild(2,   "\\\\", 0, 0, 0, 1);
        doc.assertKey( 4, 5, "\\",   0, 0, 0, 1, 0);
        doc.assertText(5, 6, "\\",   0, 0, 0, 1, 1);
        doc.assertKey( 6, 7, "\n",   0, 1);
        doc.assertRest();
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

        ParagraphLineAssert paragraph = new ParagraphLineAssert(doc)
            .setPublish(1).setNote(0)
            .setFormattedSpan(0, 0);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0);

        paragraph.test(1,    raw,   0);
        main.test(1,         "abc", 0, 0);
        doc.assertChild(1,   "abc", 0, 0, 0);
        doc.assertText(0, 3, "abc", 0, 0, 0, 0);
        doc.assertRest();
    }

}
