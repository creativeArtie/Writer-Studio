package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchMainTest.*;
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
public class MainNoteDebug {

    public static IDBuilder buildId(boolean isNameless, String name){
        String category = isNameless? "comment" : "note";
        return new IDBuilder().addCategory(category).setId(name);
    }

    @Test
    public void noIDStyle(){
        String raw = "!%abc {!deed}";
        DocumentAssert doc = assertDoc(1, raw, new MainParser());

        IDBuilder builder = buildId(true, "00");
        doc.addId(builder, 1);
        doc.addId(FormatAgendaDebug.buildId("06"), 0);
        doc.assertIds();

        MainNoteTest note = new MainNoteTest().setNoteTotal(2)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1).setNoteTotal(2);

        note.test(doc, 1, raw, 0);
        line.test(doc, 2, raw, 0, 0);
        doc.assertIds();
    }

    @Test
    public void withIDStyle(){
        String raw = "!%@id:abc";
        DocumentAssert doc = assertDoc(1, raw, new MainParser());

        IDBuilder builder = buildId(false, "id");
        doc.addId(builder, 0);

        MainNoteTest note = new MainNoteTest().setNoteTotal(1)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 4).setNoteTotal(1)
            .setBuildId(builder);

        note.test(doc, 1, raw, 0);
        line.test(doc, 5, raw, 0, 0);
        doc.assertIds();
    }

    @Test
    public void basic(){
        String raw1 = "!%@see:basic note\n";
        String raw2 = "!>in-text: Smith, p3";
        String full = raw1 + raw2;
        DocumentAssert doc = assertDoc(1, full, new MainParser());


        IDBuilder builder = new IDBuilder();
        builder.addCategory("note").setId("see");
        doc.addId(builder, 0);
        doc.assertIds();


        MainNoteTest note = new MainNoteTest().setNoteTotal(4)
            .putData(InfoFieldType.IN_TEXT, doc, 0, 1, 3)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineTest line1 = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 4).setNoteTotal(2)
            .setBuildId(builder).setIsLast(false);
        CiteLineTest line2 = new CiteLineTest()
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(doc, 0, 1, 3).setNoteTotal(2);

        note .test(doc, 2, full, 0);
        line1.test(doc, 6, raw1, 0, 0);
        line2.test(doc, 4, raw2, 0, 1);
        doc.assertIds();
    }

    @Test
    public void backToBackNotes(){
        String raw1 = "!%basic note \n";
        String raw2 = "!%@ed:data\n";
        String full = raw1 + raw2;
        DocumentAssert doc = assertDoc(2, full, new MainParser());

        IDBuilder builder = buildId(true, "00");
        doc.addId(builder, 0);

        MainNoteTest note1 = new MainNoteTest()
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setNoteTotal(2);
        NoteLineTest line1 = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1).setNoteTotal(2)
            .setIsLast(false);

        IDBuilder id = buildId(false, "ed");
        doc.addId(id, 1);

        MainNoteTest note2 = new MainNoteTest().setNoteTotal(1)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        NoteLineTest line2 = new NoteLineTest()
            .setFormattedSpan(doc, 1, 0, 4).setNoteTotal(1)
            .setBuildId(id);

        note1.test(doc, 1, raw1, 0);
        line1.test(doc, 3, raw1, 0, 0);
        note2.test(doc, 1, raw2, 1);
        line2.test(doc, 6, raw2, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void errorSources(){
        String raw1 = "!>dsaf\n";
        String raw2 = "!>in-text: Doe, p40\n";
        String raw3 = "!>in-text: Smith, p3";
        String full = raw1 + raw2 + raw3;
        DocumentAssert doc = assertDoc(1, full, new MainParser());
        IDBuilder builder = buildId(true, "00");
        doc.addId(builder, 0);

        MainNoteTest note = new MainNoteTest()
            .putData(InfoFieldType.IN_TEXT, doc, 0, 1, 3)
            .putData(InfoFieldType.IN_TEXT, doc, 0, 2, 3)
            .setNoteTotal(4)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        CiteLineTest line1 = new CiteLineTest()
            .setInfoType(InfoFieldType.ERROR)
            .setNoteTotal(0).setIsLast(false);
        CiteLineTest line2 = new CiteLineTest()
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(doc, 0, 1, 3).setNoteTotal(2)
            .setIsLast(false);
        CiteLineTest line3 = new CiteLineTest()
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(doc, 0, 2, 3).setNoteTotal(2);

        note .test(doc, 3, full, 0);
        line1.test(doc, 3, raw1, 0, 0);
        line2.test(doc, 5, raw2, 0, 1);
        line3.test(doc, 4, raw3, 0, 2);
        doc.assertLast();
        doc.assertIds();

    }

}
