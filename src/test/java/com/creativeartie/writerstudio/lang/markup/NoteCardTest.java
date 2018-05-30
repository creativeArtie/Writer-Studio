package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchLineAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchSectionAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

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
            .setCatalogued(CatalogueStatus.UNUSED, builder);
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
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineAssert line = new NoteLineAssert(doc)
            .setFormattedSpan(0, 0, 4).setNote(1)
            .setBuildId(builder);

        note.test(1, raw, 0);
        line.test(5, raw, 0, 0);
        doc.assertRest();
    }

    private static String COMMON_NOTE_BASE = "!%@see:Note Heading\n" +
            "!%some note content\\\n\n" + "!>in-text: Smith, p3";

    @Test
    public void noteBasic(){
        DocumentAssert doc = assertDoc(1, COMMON_NOTE_BASE, PARSER);

        testNoteBasic(doc);
    }

    private void testNoteBasic(DocumentAssert doc){
        String raw1 = "!%@see:Note Heading\n";
        String raw2 = "!%some note content\\\n\n";
        String raw3 = "!>in-text: Smith, p3";
        String full = raw1 + raw2 + raw3;

        IDBuilder builder = doc.addId(buildId(false, "see"), 0);

        NoteCardAssert note = new NoteCardAssert(doc).setNote(7)
            .setInText(0, 2, 3)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineAssert line1 = new NoteLineAssert(doc)
            .setNote(2).setFormattedSpan(0, 0, 4)
            .setBuildId(builder);
        NoteLineAssert line2 = new NoteLineAssert(doc)
            .setNote(3).setFormattedSpan(0, 1, 1);
        CiteLineAssert line3 = new CiteLineAssert(doc)
            .setNote(2).setDataSpan(0, 2, 3)
            .setInfoType(InfoFieldType.IN_TEXT);

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

        testNoteDouble(doc);
    }

    private void testNoteDouble(DocumentAssert doc){
        String raw1 = "!%basic note \n";
        String raw2 = "!%@ed:data\n";
        String full = raw1 + raw2;
        IDBuilder builder1 = doc.addId(buildId(true,  "00"), 0);
        IDBuilder builder2 = doc.addId(buildId(false, "ed"), 1);

        NoteCardAssert note1 = new NoteCardAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder1)
            .setNote(2);
        NoteLineAssert line1 = new NoteLineAssert(doc)
            .setFormattedSpan(0, 0, 1)
            .setNote(2);
        NoteCardAssert note2 = new NoteCardAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder2)
            .setNote(1);
        NoteLineAssert line2 = new NoteLineAssert(doc)
            .setFormattedSpan(1, 0, 4)
            .setNote(1).setBuildId(builder2);

        note1.test(1, raw1, 0);
        line1.test(3, raw1, 0, 0);
        note2.test(1, raw2, 1);
        line2.test(6, raw2, 1, 0);
        doc.assertRest();
    }

    @Test
    public void noteSourceError(){
        String raw1 = "!>dsaf\n";
        String raw2 = "!>in-text: Doe, p40\n";
        String raw3 = "!>in-text: Smith, p3";
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
            .setInfoType(InfoFieldType.IN_TEXT);
        CiteLineAssert line3 = new CiteLineAssert(doc)
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(0, 2, 3).setNote(2);

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
            .setCatalogued(CatalogueStatus.UNUSED, builder);
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
            .setCatalogued(CatalogueStatus.UNUSED, builder);
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

        testNoteDouble(doc);
    }

    @Test
    public void editAddEscepNewline(){
        ///           0000000000111111111122222222223333333333 4
        ///           0123456789012345678901234567890123456789 0
        String raw = "!%@see:Note Heading\n!%some note content\n" +
            "!>in-text: Smith, p3";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.insert(39, "\\\n", 0, 1);
        doc.assertDoc(1, COMMON_NOTE_BASE);
        testNoteBasic(doc);
    }

    @Test
    public void editContent(){
        ///           012345678901234567890123
        String raw = "!%@see:Note Heading\n!%note content\\\n\n" +
            "!>in-text: Smith, p3";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.insert(22, "some ", 0, 1);
        doc.assertDoc(1, COMMON_NOTE_BASE);
        testNoteBasic(doc);
    }
}
