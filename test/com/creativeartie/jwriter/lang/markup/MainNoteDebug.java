package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;

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
        SpanBranch note   = doc.assertChild(1, raw,           0);
        SpanBranch line   = doc.assertChild(2, raw,           0, 0);
        SpanBranch format = doc.assertChild(2, "abc {!deed}", 0, 0, 1);
        
        IDBuilder builder = buildId(true, "00");
        doc.addId(builder, 1);
        doc.addId(FormatAgendaDebug.buildId("06"), 0);
        doc.assertIds();

        assertNote(note, ImmutableListMultimap.of(), 2, CatalogueStatus.UNUSED,
            builder);
        LinedNoteDebug.assertNote(line, format, 2, null);
    }

    @Test
    public void withIDStyle(){
        String raw = "!%@id:abc";
        DocumentAssert doc = assertDoc(1, raw, new MainParser());
        SpanBranch note   = doc.assertChild(1, raw,  0);
        SpanBranch line   = doc.assertChild(5, raw,   0, 0);
        SpanBranch format = doc.assertChild(1, "abc", 0, 0, 4);

        IDBuilder builder = buildId(false, "id");
        doc.addId(builder, 0);
        doc.assertIds();

        assertNote(note,
            ImmutableListMultimap.of(), 1, CatalogueStatus.UNUSED, builder);
        LinedNoteDebug.assertNote(line, format, 1, builder);
    }

    @Test
    public void basic(){
        String raw1 = "!%@see:basic note\n";
        String raw2 = "!>in-text: Smith, p3";
        String full = raw1 + raw2;
        DocumentAssert doc = assertDoc(1, full, new MainParser());
        SpanBranch note   = doc.assertChild(2, full,         0);
        SpanBranch line1  = doc.assertChild(6, raw1,         0, 0);
        SpanBranch format = doc.assertChild(1, "basic note", 0, 0, 4);
        SpanBranch line2  = doc.assertChild(4, raw2,         0, 1);
        SpanBranch data   = doc.assertChild(1, " Smith, p3", 0, 1, 3);


        IDBuilder builder = new IDBuilder();
        builder.addCategory("note").setId("see");
        doc.addId(builder, 0);
        doc.assertIds();

        assertNote(note, ImmutableListMultimap.of(InfoFieldType.IN_TEXT,
            (InfoDataSpan<?>)data), 4, CatalogueStatus.UNUSED, builder);
        LinedNoteDebug.assertNote(line1, format, 2, builder);
        LinedCiteDebug.assertCite(line2, InfoFieldType.IN_TEXT, data, 2);
    }

    @Test
    public void backToBackNotes(){
        String raw1 = "!%basic note \n";
        String raw2 = "!%@ed:data\n";
        String full = raw1 + raw2;
        DocumentAssert doc = assertDoc(2, full, new MainParser());
        SpanBranch note1 = doc.assertChild(1, raw1,          0);
        SpanBranch line1 = doc.assertChild(3, raw1,          0, 0);
        SpanBranch text1 = doc.assertChild(1, "basic note ", 0, 0, 1);
        SpanBranch note2 = doc.assertChild(1, raw2,          1);
        SpanBranch line2 = doc.assertChild(6, raw2,          1, 0);
        SpanBranch text2 = doc.assertChild(1, "data",        1, 0, 4);

        IDBuilder builder = buildId(true, "00");
        doc.addId(builder, 0);
        assertNote(note1, ImmutableListMultimap.of(), 2, CatalogueStatus.UNUSED,
            builder);
        LinedNoteDebug.assertNote(line1, text1, 2, null);

        IDBuilder id = buildId(false, "ed");
        doc.addId(id, 1);

        assertNote(note2, ImmutableListMultimap.of(), 1, CatalogueStatus.UNUSED,
            id);
        LinedNoteDebug.assertNote(line2, text2, 1, id);
        doc.assertIds();
    }

    @Test
    public void errorSources(){
        String raw1 = "!>dsaf\n";
        String raw2 = "!>in-text: Doe, p40\n";
        String raw3 = "!>in-text: Smith, p3";
        String full = raw1 + raw2 + raw3;
        DocumentAssert doc = assertDoc(1, full, new MainParser());
        SpanBranch note   = doc.assertChild(3, full,         0);
        SpanBranch line1  = doc.assertChild(3, raw1,         0, 0);
        SpanBranch line2  = doc.assertChild(5, raw2,         0, 1);
        SpanBranch data2  = doc.assertChild(1, " Doe, p40",  0, 1, 3);
        SpanBranch line3  = doc.assertChild(4, raw3,         0, 2);
        SpanBranch data3  = doc.assertChild(1, " Smith, p3", 0, 2, 3);
        
        IDBuilder builder = buildId(true, "00");
        doc.addId(builder, 0);

        assertNote(note, ImmutableListMultimap.of(
            InfoFieldType.IN_TEXT, (InfoDataSpan<?>)data2,
            InfoFieldType.IN_TEXT, (InfoDataSpan<?>)data3), 4,
            CatalogueStatus.UNUSED, builder);
        LinedCiteDebug.assertCite(line1, InfoFieldType.ERROR, null, 0);
        LinedCiteDebug.assertCite(line2, InfoFieldType.IN_TEXT, data2, 2);
        LinedCiteDebug.assertCite(line3, InfoFieldType.IN_TEXT, data3, 2);

    }

}
