package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.BranchLineAsserts.*;
import com.creativeartie.writer.lang.markup.BranchSectionAsserts.*;

import static com.creativeartie.writer.lang.DocumentAssert.*;

public class NoteCardTest {

    private static final NoteCardParser PARSER = NoteCardParser.PARSER;

    public static IDBuilder buildId(boolean isNameless, String name){
        String category = isNameless? AuxiliaryData.TYPE_NOTE :
            AuxiliaryData.TYPE_RESEARCH;
        return new IDBuilder().addCategory(category).setId(name);
    }

    @Test
    public void noteNoId(){
        String raw = "!%abc {!deed}";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        IDBuilder builder = doc.addId(buildId(true, "00"), 1);
        doc.addId(FormatAgendaTest.buildId("06"), 0);

        NoteCardAssert note = new NoteCardAssert(doc).setNote(2)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setTitle(0, 0, 1);
        NoteLineAssert line = new NoteLineAssert(doc)
            .setFormattedSpan(0, 0, 1).setNote(2);

        note.test(1, raw, 0);
        line.test(2, raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void noteWithId(){
        String raw = "!%@id:abc";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        IDBuilder builder = buildId(false, "id");
        doc.addId(builder, 0);

        NoteCardAssert note = new NoteCardAssert(doc).setNote(1)
            .setCatalogued(CatalogueStatus.UNUSED, builder).setLookup("{@id}")
            .setTitle(0, 0, 4);
        NoteLineAssert line = new NoteLineAssert(doc)
            .setFormattedSpan(0, 0, 4).setNote(1)
            .setBuildId(builder).setLookup("{@id}");

        note.test(1, raw, 0);
        line.test(5, raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void noteLooped(){
        String line1 = "!%@loop:Heading\n";
        String line2 = "!>source|reference:loop\n";
        String line3 = "!>source|in-text:done";
        String raw = line1 + line2 + line3;

        DocumentAssert doc = assertDoc(1, raw, PARSER);
        IDBuilder builder = doc.addId(buildId(false, "loop"),
            CatalogueStatus.READY, 0);

        NoteCardAssert card = new NoteCardAssert(doc)
            .setNote(2).setLookup("{@loop}")
            .setCatalogued(CatalogueStatus.READY, builder)
            .setTitle(0, 0, 4);
        NoteLineAssert note = new NoteLineAssert(doc)
            .setNote(1).setFormattedSpan(0, 0, 4)
            .setLookup("{@loop}").setBuildId(builder);
        CiteLineAssert cite1 = new CiteLineAssert(doc)
            .setNote(0).setDataSpan(0, 1, 3)
            .setInfoType(InfoFieldType.REF)
            .setCatalogued(CatalogueStatus.READY, builder)
            .setDataClass(DirectorySpan.class);
        CiteLineAssert cite2 = new CiteLineAssert(doc)
            .setNote(1).setDataSpan(0, 2, 3)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataClass(ContentSpan.class);

        card.test(3, raw,   0);
        note.test(6, line1, 0, 0);
        cite1.test(5, line2, 0, 1);
        cite2.test(4, line3, 0, 2);
        doc.assertRest();
    }

    @Test
    public void noteBasic(){
        DocumentAssert doc = assertDoc(1, "!%@see:Note Heading\n" +
            "!%some note content\\\n\n" + "!>source|in-text: Smith, p3", PARSER);

        commonNoteBasic(doc);
    }

    private void commonNoteBasic(DocumentAssert doc){
        String raw1 = "!%@see:Note Heading\n";
        String raw2 = "!%some note content\\\n\n";
        String raw3 = "!>source|in-text: Smith, p3";
        String full = raw1 + raw2 + raw3;

        IDBuilder builder = doc.addId(buildId(false, "see"), 0);

        NoteCardAssert note = new NoteCardAssert(doc).setNote(7)
            .setInText(0, 2, 3).setLookup("{@see}")
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setTitle(0, 0, 4).addContent(0, 1, 1);
        NoteLineAssert line1 = new NoteLineAssert(doc)
            .setNote(2).setFormattedSpan(0, 0, 4)
            .setBuildId(builder).setLookup("{@see}");
        NoteLineAssert line2 = new NoteLineAssert(doc)
            .setNote(3).setFormattedSpan(0, 1, 1);
        CiteLineAssert line3 = new CiteLineAssert(doc)
            .setNote(2).setDataSpan(0, 2, 3)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataClass(ContentSpan.class);

        doc.assertDoc(1, full);
        note.test( 3, full, 0);
        line1.test(6, raw1, 0, 0);
        line2.test(3, raw2, 0, 1);
        line3.test(4, raw3, 0, 2);
        doc.assertRest();
    }

    @Test
    public void noteDouble(){
        String raw = "!%basic note \n!%@ed:data\n";
        DocumentAssert doc = assertDoc(2, raw, PARSER);

        commonNoteDouble(doc);
    }

    private void commonNoteDouble(DocumentAssert doc){
        String raw1 = "!%basic note \n";
        String raw2 = "!%@ed:data\n";
        String full = raw1 + raw2;
        IDBuilder builder1 = doc.addId(buildId(true,  "00"), 0);
        IDBuilder builder2 = doc.addId(buildId(false, "ed"), 1);

        doc.assertDoc   (2, full);

        NoteCardAssert note1 = new NoteCardAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder1)
            .setNote(2).setTitle(0, 0, 1);
        NoteLineAssert line1 = new NoteLineAssert(doc)
            .setFormattedSpan(0, 0, 1)
            .setNote(2);
        NoteCardAssert note2 = new NoteCardAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder2)
            .setNote(1).setLookup("{@ed}").setTitle(1, 0, 4);
        NoteLineAssert line2 = new NoteLineAssert(doc)
            .setFormattedSpan(1, 0, 4)
            .setNote(1).setBuildId(builder2).setLookup("{@ed}");

        note1.test(1, raw1, 0);
        line1.test(3, raw1, 0, 0);
        note2.test(1, raw2, 1);
        line2.test(6, raw2, 1, 0);
        doc.assertRest();
    }

    @Test
    public void noteSourceError(){
        String raw1 = "!>dsaf\n";
        String raw2 = "!>source|in-text: Doe, p40\n";
        String raw3 = "!>source|in-text: Smith, p3";
        String full = raw1 + raw2 + raw3;
        DocumentAssert doc = assertDoc(1, full, PARSER);
        IDBuilder builder = doc.addId(buildId(true, "00"), 0);

        NoteCardAssert note = new NoteCardAssert(doc)
            .setInText(0, 1, 3)
            .setNote(4)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        CiteLineAssert line1 = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.ERROR)
            .setNote(0);
        CiteLineAssert line2 = new CiteLineAssert(doc)
            .setDataSpan(0, 1, 3).setNote(2)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataClass(ContentSpan.class);
        CiteLineAssert line3 = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(0, 2, 3).setNote(2)
            .setDataClass(ContentSpan.class);

        note.test( 3, full, 0);
        line1.test(3, raw1, 0, 0);
        line2.test(5, raw2, 0, 1);
        line3.test(4, raw3, 0, 2);
        doc.assertRest();
    }

    @Test
    public void editAddLineBasic(){
        ///           012345678901234567 89
        String raw = "!%line 1!%   line2\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.insert(8, "\n", 0);

        IDBuilder builder = doc.addId(buildId(true, "00"), 0);

        NoteCardAssert note = new NoteCardAssert(doc).setNote(3)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setTitle(0, 0, 1).addContent(0, 1, 1);
        NoteLineAssert line1 = new NoteLineAssert(doc)
            .setFormattedSpan(0, 0, 1).setNote(2);
        NoteLineAssert line2 = new NoteLineAssert(doc)
            .setFormattedSpan(0, 1, 1).setNote(1);

        String out1 = "!%line 1\n";
        String out2 = "!%   line2\n";
        String full = out1 + out2;
        doc.assertDoc(  1, full);
        note.test( 2, full, 0);
        line1.test(3, out1, 0, 0);
        line2.test(3, out2, 0, 1);
        doc.assertRest();
    }

    @Test
    public void editAddParagraph(){
        ///           01234567 89
        String raw = "!%line 1\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.insert(8, "\n");

        IDBuilder builder = doc.addId(buildId(true, "00"), 0);

        NoteCardAssert note = new NoteCardAssert(doc).setNote(2)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setTitle(0, 0, 1);
        NoteLineAssert line1 = new NoteLineAssert(doc)
            .setFormattedSpan(0, 0, 1).setNote(2);

        String out1 = "!%line 1\n";
        String out2 = "\n";
        String full = out1 + out2;
        doc.assertDoc(  2, full);
        note.test( 1, out1, 0);
        line1.test(3, out1, 0, 0);
        doc.assertRest("\n");
    }

    @Test
    public void editSplitById(){
        ///           0123456789012 3456789012 34
        String raw = "!%basic note \n!%ed:data\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.insert(16, "@");

        commonNoteDouble(doc);
    }

    @Test
    public void editAddEscpeNewline(){
        ///           0000000000111111111122222222223333333333 4
        ///           0123456789012345678901234567890123456789 0
        String raw = "!%@see:Note Heading\n!%some note content\n" +
            "!>source|in-text: Smith, p3";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.insert(39, "\\\n", 0, 1);
        commonNoteBasic(doc);
    }

    @Test
    public void editContent(){
        ///           012345678901234567890123
        String raw = "!%@see:Note Heading\n!%note content\\\n\n" +
            "!>source|in-text: Smith, p3";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.insert(22, "some ", 0, 1);
        commonNoteBasic(doc);
    }
}
