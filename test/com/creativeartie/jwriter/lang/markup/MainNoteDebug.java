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

    public static void assertNote(SpanBranch span,
        Multimap<InfoFieldType, InfoDataSpan<?>> sources, int noteCount,
        CatalogueStatus status, IDBuilder id)
    {
        MainSpanNote test = assertClass(span, MainSpanNote.class);

        DetailStyle[] styles = new DetailStyle[]{AuxiliaryStyle.MAIN_NOTE,
            status};

        assertEquals(getError("sources", test), sources, test.getSources());
        assertSpanIdentity(span, id);
        assertMain(test, 0, noteCount);
        assertBranch(span, styles, status);
    }

    public static final void assertMain(SpanBranch span, int publish, int note){
        MainSpan test = (MainSpan) span;
        assertEquals(getError("publish", span), publish, test.getPublishCount());
        assertEquals(getError("note", span), note, test.getNoteCount());
    }

    @Test
    public void noIDStyle(){
        String raw = "!%abc {!deed}";
        DocumentAssert doc = assertDoc(1, raw, new MainParser());

        IDBuilder builder = buildId(true, "00");
        doc.addId(builder, 1);
        doc.addId(FormatAgendaDebug.buildId("06"), 0);
        doc.assertIds();

        MainNoteTest note = new MainNoteTest().setNoteCount(2)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1).setNoteCount(2);

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

        MainNoteTest note = new MainNoteTest().setNoteCount(1)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 4).setNoteCount(1)
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


        MainNoteTest note = new MainNoteTest().setNoteCount(4)
            .putData(InfoFieldType.IN_TEXT, doc, 0, 1, 3)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        NoteLineTest line1 = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 4).setNoteCount(2)
            .setBuildId(builder);
        CiteLineTest line2 = new CiteLineTest()
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(doc, 0, 1, 3).setNoteCount(2);

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
            .setNoteCount(2);
        NoteLineTest line1 = new NoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1).setNoteCount(2);

        IDBuilder id = buildId(false, "ed");
        doc.addId(id, 1);

        MainNoteTest note2 = new MainNoteTest().setNoteCount(1)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        NoteLineTest line2 = new NoteLineTest()
            .setFormattedSpan(doc, 1, 0, 4).setNoteCount(1)
            .setBuildId(id);

        note1.test(doc, 1, raw1, 0);
        line1.test(doc, 3, raw1, 0, 0);
        note2.test(doc, 1, raw2, 1);
        line2.test(doc, 6, raw2, 1, 0);
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
            .setNoteCount(4)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        CiteLineTest line1 = new CiteLineTest()
            .setInfoType(InfoFieldType.ERROR)
            .setNoteCount(0);
        CiteLineTest line2 = new CiteLineTest()
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(doc, 0, 1, 3).setNoteCount(2);
        CiteLineTest line3 = new CiteLineTest()
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(doc, 0, 2, 3).setNoteCount(2);

        note .test(doc, 3, full, 0);
        line1.test(doc, 3, raw1, 0, 0);
        line2.test(doc, 5, raw2, 0, 1);
        line3.test(doc, 4, raw3, 0, 2);
        doc.assertIds();

    }

}
