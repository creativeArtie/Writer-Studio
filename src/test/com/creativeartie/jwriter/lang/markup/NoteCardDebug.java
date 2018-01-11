package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchSectionTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableListMultimap;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class NoteCardDebug {

    private static final NoteCardParser PARSER = NoteCardParser.PARSER;

    public static IDBuilder buildId(boolean isNameless, String name){
        String category = isNameless? "comment" : "note";
        return new IDBuilder().addCategory(category).setId(name);
    }

    @Test
    public void noteNoId(){
        String raw = "!%abc {!deed}";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        IDBuilder builder = doc.addId(buildId(true, "00"), 1);
        doc.addId(FormatAgendaDebug.buildId("06"), 0);

        NoteCardTest note = new NoteCardTest().setNoteTotal(2)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1).setNoteTotal(2);

        note.test(doc, 1, raw, 0);
        line.test(doc, 2, raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noteWithId(){
        String raw = "!%@id:abc";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        IDBuilder builder = buildId(false, "id");
        doc.addId(builder, 0);

        NoteCardTest note = new NoteCardTest().setNoteTotal(1)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 4).setNoteTotal(1)
            .setBuildId(builder);

        note.test(doc, 1, raw, 0);
        line.test(doc, 5, raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noteBasic(){
        String raw = "!%@see:Note Heading\n" + "!%some note content\n" +
            "!>in-text: Smith, p3";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        testNoteBasic(doc);
    }

    private void testNoteBasic(DocumentAssert doc){
        String raw1 = "!%@see:Note Heading\n";
        String raw2 = "!%some note content\n";
        String raw3 = "!>in-text: Smith, p3";
        String full = raw1 + raw2 + raw3;

        IDBuilder builder = doc.addId(buildId(false, "see"), 0);

        NoteCardTest note = new NoteCardTest().setNoteTotal(7)
            .putData(InfoFieldType.IN_TEXT, doc, 0, 2, 3)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineTest line1 = new NoteLineTest()
            .setNoteTotal(2)    .setFormattedSpan(doc, 0, 0, 4)
            .setBuildId(builder).setIsLast(false);
        NoteLineTest line2 = new NoteLineTest()
            .setNoteTotal(3)    .setFormattedSpan(doc, 0, 1, 1)
            .setIsLast(false)   .setIsFirst(false);
        CiteLineTest line3 = new CiteLineTest()
            .setNoteTotal(2)    .setDataSpan(doc, 0, 2, 3)
            .setInfoType(InfoFieldType.IN_TEXT);

        note.test( doc, 3, full, 0);
        line1.test(doc, 6, raw1, 0, 0);
        line2.test(doc, 3, raw2, 0, 1);
        line3.test(doc, 4, raw3, 0, 2);
        doc.assertLast();
        doc.assertIds();
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

        NoteCardTest note1 = new NoteCardTest()
            .setCatalogued(CatalogueStatus.UNUSED, builder1)
            .setNoteTotal(2);
        NoteLineTest line1 = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1)
            .setNoteTotal(2).setIsLast(false);
        NoteCardTest note2 = new NoteCardTest()
            .setCatalogued(CatalogueStatus.UNUSED, builder2)
            .setNoteTotal(1);
        NoteLineTest line2 = new NoteLineTest()
            .setFormattedSpan(doc, 1, 0, 4)
            .setNoteTotal(1).setBuildId(builder2);

        note1.test(doc, 1, raw1, 0);
        line1.test(doc, 3, raw1, 0, 0);
        note2.test(doc, 1, raw2, 1);
        line2.test(doc, 6, raw2, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noteSourceError(){
        String raw1 = "!>dsaf\n";
        String raw2 = "!>in-text: Doe, p40\n";
        String raw3 = "!>in-text: Smith, p3";
        String full = raw1 + raw2 + raw3;
        DocumentAssert doc = assertDoc(1, full, PARSER);
        IDBuilder builder = doc.addId(buildId(true, "00"), 0);

        NoteCardTest note = new NoteCardTest()
            .putData(InfoFieldType.IN_TEXT, doc, 0, 1, 3)
            .putData(InfoFieldType.IN_TEXT, doc, 0, 2, 3)
            .setNoteTotal(4)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        CiteLineTest line1 = new CiteLineTest()
            .setInfoType(InfoFieldType.ERROR)
            .setNoteTotal(0).setIsLast(false);
        CiteLineTest line2 = new CiteLineTest()
            .setInfoType(InfoFieldType.IN_TEXT).setIsLast(false)
            .setDataSpan(doc, 0, 1, 3)         .setNoteTotal(2);
        CiteLineTest line3 = new CiteLineTest()
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(doc, 0, 2, 3).setNoteTotal(2);

        note.test( doc, 3, full, 0);
        line1.test(doc, 3, raw1, 0, 0);
        line2.test(doc, 5, raw2, 0, 1);
        line3.test(doc, 4, raw3, 0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void editAddLineBasic(){
        ///           012345678901234567 89
        String raw = "!%line 1!%   line2\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.insert(8, "\n", 0);

        IDBuilder builder = doc.addId(buildId(true, "00"), 0);

        NoteCardTest note = new NoteCardTest().setNoteTotal(3)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineTest line1 = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1).setNoteTotal(2)
            .setIsLast(false);
        NoteLineTest line2 = new NoteLineTest()
            .setFormattedSpan(doc, 0, 1, 1).setNoteTotal(1)
            .setIsFirst(false);

        String out1 = "!%line 1\n";
        String out2 = "!%   line2\n";
        String full = out1 + out2;
        doc.assertDoc(  1, full);
        note.test( doc, 2, full, 0);
        line1.test(doc, 3, out1, 0, 0);
        line2.test(doc, 3, out2, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void editAddParagraph(){
        ///           01234567 89
        String raw = "!%line 1\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.insert(8, "\n");

        IDBuilder builder = doc.addId(buildId(true, "00"), 0);

        NoteCardTest note = new NoteCardTest().setNoteTotal(2)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineTest line1 = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1).setNoteTotal(2)
            .setIsLast(false);

        String out1 = "!%line 1\n";
        String out2 = "\n";
        String full = out1 + out2;
        doc.assertDoc(  2, full);
        note.test( doc, 1, out1, 0);
        line1.test(doc, 3, out1, 0, 0);
        doc.assertLast("\n");
        doc.assertIds();
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
    public void editContent(){
        ///           012345678901234567890123
        String raw = "!%@see:Note Heading\n!%note content\n!>in-text: Smith, p3";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        String full =
            "!%@see:Note Heading\n!%some note content\n!>in-text: Smith, p3";
        doc.insert(22, "some ", 0, 1);
        doc.assertDoc(1, full);
        testNoteBasic(doc);
    }
}