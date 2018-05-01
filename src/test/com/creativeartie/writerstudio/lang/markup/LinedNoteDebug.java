package com.creativeartie.writerstudio.lang.markup;


import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.util.Arrays;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
import static com.creativeartie.writerstudio.lang.markup.BranchLineTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchFormatTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchTest.*;

public class LinedNoteDebug {

    private static IDBuilder buildId(String id){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_RESEARCH).setId(id);
    }

    private final SetupParser[] parsers = new SetupParser[]{LinedParseRest.NOTE};

    @Test
    public void noteComplete(){
        String text = "_Text\\*_";
        String raw = "!%     @sub-id:" + text + "\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder createId = buildId("id").addCategory("sub");
        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 4).setNoteTotal(1)
            .setBuildId(createId);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(createId);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(1).setNoteTotal(0);

        line.test(        doc, 6, raw,        0);
        doc.assertKeyLeaf(  0,  2, "!%",      0, 0);
        doc.assertKeyLeaf(  2,  8, "     @",  0, 1);
        id.test(          doc,  3, "sub-id",  0, 2);
        doc.assertChild(        1, "sub",     0, 2, 0);
        doc.assertIdLeaf(   8, 11, "sub",     0, 2, 0, 0);
        doc.assertKeyLeaf( 11, 12, "-",       0, 2, 1);
        doc.assertChild(        1, "id",      0, 2, 2);
        doc.assertIdLeaf(  12, 14, "id",      0, 2, 2, 0);
        doc.assertKeyLeaf( 14, 15, ":",       0, 3);
        main.test(        doc, 3, text,       0, 4);
        doc.assertKeyLeaf( 15, 16, "_",       0, 4, 0);
        doc.assertChild(        2, "Text\\*", 0, 4, 1);
        doc.assertTextLeaf(16, 20, "Text",    0, 4, 1, 0);
        doc.assertChild(        2, "\\*",     0, 4, 1, 1);
        doc.assertKeyLeaf( 20, 21, "\\",      0, 4, 1, 1, 0);
        doc.assertTextLeaf(21, 22, "*",       0, 4, 1, 1, 1);
        doc.assertKeyLeaf( 22, 23, "_",       0, 4, 2);
        doc.assertKeyLeaf( 23, 24, "\n",      0, 5);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noteWithoutNewLine(){
        String raw = "!%@id:Text";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        commonComplete(doc);
    }

    @Test
    public void editChangeText(){
        ///              01234567
        String before = "!%@id:Tt";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(7, "ex", 0);
        ///             01234567890
        String after = "!%@id:Text";
        doc.assertDoc(1, after, parsers);
        commonComplete(doc);
    }

    @Test
    public void editChangeId(){
        ///              0123456789
        String before = "!%@d:Text";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "i", 0, 2);
        ///             01234567890
        String after = "!%@id:Text";
        doc.assertDoc(1, after, parsers);
        commonComplete(doc);
    }

    @Test
    public void editAddColon(){
        ///              0123456789
        String before = "!%@idText";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(5, ":", 0);
        ///             01234567890
        String after = "!%@id:Text";
        doc.assertDoc(1, after, parsers);
        commonComplete(doc);
    }

    @Test
    public void editAddID(){
        ///              0123456
        String before = "!%Text";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(2, "@id:", 0);
        ///             01234567890
        String after = "!%@id:Text";
        doc.assertDoc(1, after, parsers);
        commonComplete(doc);
    }

    private void commonComplete(DocumentAssert doc){
        ///           01234567890
        String raw = "!%@id:Text";

        IDBuilder createId = buildId("id");
        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 4).setNoteTotal(1)
            .setBuildId(createId);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(createId);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(1).setNoteTotal(0);

        line.test(        doc, 5, raw,    0);
        doc.assertKeyLeaf( 0,  2, "!%",   0, 0);
        doc.assertKeyLeaf( 2,  3, "@",    0, 1);
        id.test(         doc,  1, "id",   0, 2);
        doc.assertChild(       1, "id",   0, 2, 0);
        doc.assertIdLeaf(  3,  5, "id",   0, 2, 0, 0);
        doc.assertKeyLeaf( 5,  6, ":",    0, 3);
        main.test(        doc, 1, "Text", 0, 4);
        doc.assertChild(       1, "Text", 0, 4, 0);
        doc.assertTextLeaf(6, 10, "Text", 0, 4, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }


    @Test
    public void noText(){
        String raw = "!%@id:";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder createId = buildId("id");
        NoteLineTest line = new NoteLineTest()
            .setNoteTotal(0).setBuildId(createId);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(createId);

        line.test(       doc, 4, raw,  0);
        doc.assertKeyLeaf( 0, 2, "!%", 0, 0);
        doc.assertKeyLeaf( 2, 3, "@",  0, 1);
        id.test(         doc, 1, "id", 0, 2);
        doc.assertChild(      1, "id", 0, 2, 0);
        doc.assertIdLeaf(  3, 5, "id", 0, 2, 0, 0);
        doc.assertKeyLeaf( 5, 6, ":",  0, 3);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noColon(){
        String raw = "!%@id";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder createId = buildId("id");
        NoteLineTest line = new NoteLineTest()
            .setNoteTotal(0).setBuildId(createId);
        DirectoryTest id = new DirectoryTest()
            .setPurpose(DirectoryType.NOTE)
            .setIdentity(createId);

        line.test(       doc, 3, raw,  0);
        doc.assertKeyLeaf( 0, 2, "!%", 0, 0);
        doc.assertKeyLeaf( 2, 3, "@",  0, 1);
        id.test(         doc, 1, "id", 0, 2);
        doc.assertChild(      1, "id", 0, 2, 0);
        doc.assertIdLeaf(  3, 5, "id", 0, 2, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noId(){
        String raw = "!%@:Text";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 3).setNoteTotal(1);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(1).setNoteTotal(0);

        line.test(       doc, 4, raw,    0);
        doc.assertKeyLeaf( 0, 2, "!%",   0, 0);
        doc.assertKeyLeaf( 2, 3, "@",    0, 1);
        doc.assertKeyLeaf( 3, 4, ":",    0, 2);
        main.test(       doc, 1, "Text", 0, 3);
        doc.assertChild(      1, "Text", 0, 3, 0);
        doc.assertTextLeaf(4, 8, "Text", 0, 3, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }


    @Test
    public void noID(){
        String raw = "!%Text";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        NoteLineTest line = new NoteLineTest()
            .setFormattedSpan(doc, 0, 1).setNoteTotal(1);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(1).setNoteTotal(0);

        line.test(       doc, 2, raw,    0);
        doc.assertKeyLeaf( 0, 2, "!%",   0, 0);
        main.test(       doc, 1, "Text", 0, 1);
        doc.assertChild(      1, "Text", 0, 1, 0);
        doc.assertTextLeaf(2, 6, "Text", 0, 1, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noIDText(){
        String raw = "!%";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        NoteLineTest line = new NoteLineTest()
            .setNoteTotal(0);

        line.test(       doc, 1, raw,  0);
        doc.assertKeyLeaf( 0, 2, "!%", 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noIDColon(){
        String raw = "!%@";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        NoteLineTest line = new NoteLineTest()
            .setNoteTotal(0);

        line.test(        doc, 2, raw, 0);
        doc.assertKeyLeaf( 0, 2, "!%", 0, 0);
        doc.assertKeyLeaf( 2, 3, "@",  0, 1);
        doc.assertLast();
        doc.assertIds();
    }
}
