package com.creativeartie.jwriter.lang.markup;


import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.util.Arrays;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;

public class LinedNoteDebug {

    private static IDBuilder buildId(String id){
        return new IDBuilder().addCategory("note").setId(id);
    }

    private final SetupParser[] parsers = new SetupParser[]{LinedParseRest.NOTE};

    public static void assertNote(SpanBranch span, Span format, int noteCount,
        IDBuilder id)
    {
        LinedSpanNote test = assertClass(span, LinedSpanNote.class);

        DetailStyle[] styles = new DetailStyle[]{LinedType.NOTE};

        assertSpan("format", format, test.getFormattedSpan());
        assertEquals(getError("lined", span), LinedType.NOTE, test.getLinedType());
        DirectoryDebug.assertBuildId(test, id, test.buildId());
        LinedRestDebug.assertLine(test, 0, noteCount);
        assertBranch(span, styles);
    }

    private void printLeaves(Document doc){
        for(SpanLeaf leaf : doc.getLeaves()){
            System.out.println(leaf.getStart() + ", " + leaf.getEnd() + ", \"" +
                leaf.getRaw() + "\"");
        }
    }

    @Test
    public void noteComplete(){
        String raw = "!%     @sub-id:_Text\\*_\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch line = doc.assertChild(6, raw,          0);
        SpanBranch id   = doc.assertChild(3, "sub-id",     0, 2);
        SpanBranch text = doc.assertChild(3, "_Text\\*_",  0, 4);

        IDBuilder createId = buildId("id").addCategory("sub");

        assertNote(line, text, 1, createId);
        DirectoryDebug.assertId(id, DirectoryType.NOTE, createId);
        FormatSpanDebug.assertMain(text, 1, 0);

        doc.assertKeyLeaf(  0,  2, "!%",     0, 0);
        doc.assertKeyLeaf(  2,  8, "     @", 0, 1);
        doc.assertIdLeaf(   8, 11, "sub",    0, 2, 0, 0);
        doc.assertKeyLeaf( 11, 12, "-",      0, 2, 1);
        doc.assertIdLeaf(  12, 14, "id",     0, 2, 2, 0);
        doc.assertKeyLeaf( 14, 15, ":",      0, 3);
        doc.assertKeyLeaf( 15, 16, "_",      0, 4, 0);
        doc.assertTextLeaf(16, 20, "Text",   0, 4, 1, 0);
        doc.assertKeyLeaf( 20, 21, "\\",     0, 4, 1, 1, 0);
        doc.assertTextLeaf(21, 22, "*",      0, 4, 1, 1, 1);
        doc.assertKeyLeaf( 22, 23, "_",      0, 4, 2);
        doc.assertKeyLeaf( 23, 24, "\n",     0, 5);

        doc.assertIds();
    }

    @Test
    public void noteWithoutNewLine(){
        String raw = "!%@id:Text";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch line = doc.assertChild(5, raw,    0);
        SpanBranch id   = doc.assertChild(1, "id",   0, 2);
        SpanBranch text = doc.assertChild(1, "Text", 0, 4);

        IDBuilder createId = buildId("id");

        assertNote(line, text, 1, createId);
        DirectoryDebug.assertId(id, DirectoryType.NOTE, createId);
        FormatSpanDebug.assertMain(text, 1, 0);

        doc.assertKeyLeaf( 0,  2, "!%",   0, 0);
        doc.assertKeyLeaf( 2,  3, "@",    0, 1);
        doc.assertIdLeaf(  3,  5, "id",   0, 2, 0, 0);
        doc.assertKeyLeaf( 5,  6, ":",    0, 3);
        doc.assertTextLeaf(6, 10, "Text", 0, 4, 0, 0);

        doc.assertIds();
    }


    @Test
    public void noText(){
        String raw = "!%@id:";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch line = doc.assertChild(4, raw,    0);
        SpanBranch id   = doc.assertChild(1, "id",   0, 2);

        IDBuilder createId = buildId("id");

        assertNote(line, null, 0, createId);
        DirectoryDebug.assertId(id, DirectoryType.NOTE, createId);

        doc.assertKeyLeaf( 0,  2, "!%",   0, 0);
        doc.assertKeyLeaf( 2,  3, "@",    0, 1);
        doc.assertIdLeaf(  3,  5, "id",   0, 2, 0, 0);
        doc.assertKeyLeaf( 5,  6, ":",    0, 3);

        doc.assertIds();
    }

    @Test
    public void noColon(){
        String raw = "!%@id";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch line = doc.assertChild(3, raw,    0);
        SpanBranch id   = doc.assertChild(1, "id",   0, 2);

        IDBuilder createId = buildId("id");

        assertNote(line, null, 0, createId);
        DirectoryDebug.assertId(id, DirectoryType.NOTE, createId);

        doc.assertKeyLeaf( 0,  2, "!%",   0, 0);
        doc.assertKeyLeaf( 2,  3, "@",    0, 1);
        doc.assertIdLeaf(  3,  5, "id",   0, 2, 0, 0);

        doc.assertIds();
    }

    @Test
    public void blankID(){
        String raw = "!%@:Text";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch line = doc.assertChild(4, raw,    0);
        SpanBranch text = doc.assertChild(1, "Text", 0, 3);

        assertNote(line, text, 1, null);
        FormatSpanDebug.assertMain(text, 1, 0);

        doc.assertKeyLeaf( 0, 2, "!%",   0, 0);
        doc.assertKeyLeaf( 2, 3, "@",    0, 1);
        doc.assertKeyLeaf( 3, 4, ":",    0, 2);
        doc.assertTextLeaf(4, 8, "Text", 0, 3, 0, 0);

        doc.assertIds();
    }


    @Test
    public void noID(){
        String raw = "!%Text";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch line = doc.assertChild(2, raw,    0);
        SpanBranch text = doc.assertChild(1, "Text", 0, 1);

        assertNote(line, text, 1, null);
        FormatSpanDebug.assertMain(text, 1, 0);

        doc.assertKeyLeaf( 0, 2, "!%",   0, 0);
        doc.assertTextLeaf(2, 6, "Text", 0, 1, 0, 0);

        doc.assertIds();
    }

    @Test
    public void startOnly(){
        String raw = "!%";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch line = doc.assertChild(1, raw, 0);

        assertNote(line, null, 0, null);

        doc.assertKeyLeaf( 0, 2, "!%", 0, 0);

        doc.assertIds();
    }

    @Test
    public void startID(){
        String raw = "!%@";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch line = doc.assertChild(2, raw, 0);

        assertNote(line, null, 0, null);

        doc.assertKeyLeaf( 0, 2, "!%", 0, 0);
        doc.assertKeyLeaf( 2, 3, "@",  0, 1);

        doc.assertIds();
    }
}
