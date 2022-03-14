package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writer.lang.markup.BranchFormatAsserts.*;
import com.creativeartie.writer.lang.markup.BranchLineAsserts.*;

import static com.creativeartie.writer.lang.DocumentAssert.*;

public class LinedNoteTest {

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
        NoteLineAssert line = new NoteLineAssert(doc)
            .setFormattedSpan(0, 4).setNote(1)
            .setBuildId(createId).setLookup("{@sub-id}");
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(createId).setLookup("sub-id");
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("Text*");

        line.test(6,           raw,       0);
        doc.assertKey(  0,  2, "!%",      0, 0);
        doc.assertKey(  2,  8, "     @",  0, 1);
        id.test(3,             "sub-id",  0, 2);
        doc.assertChild(1,     "sub",     0, 2, 0);
        doc.assertId(   8, 11, "sub",     0, 2, 0, 0);
        doc.assertKey( 11, 12, "-",       0, 2, 1);
        doc.assertChild(1,     "id",      0, 2, 2);
        doc.assertId(  12, 14, "id",      0, 2, 2, 0);
        doc.assertKey( 14, 15, ":",       0, 3);
        main.test(3,           text,      0, 4);
        doc.assertKey( 15, 16, "_",       0, 4, 0);
        doc.assertChild(2,     "Text\\*", 0, 4, 1);
        doc.assertText(16, 20, "Text",    0, 4, 1, 0);
        doc.assertChild(2,     "\\*",     0, 4, 1, 1);
        doc.assertKey( 20, 21, "\\",      0, 4, 1, 1, 0);
        doc.assertText(21, 22, "*",       0, 4, 1, 1, 1);
        doc.assertKey( 22, 23, "_",       0, 4, 2);
        doc.assertKey( 23, 24, "\n",      0, 5);
        doc.assertRest();
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
        doc.assertDoc(1, after);
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
        doc.assertDoc(1, after);
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
        doc.assertDoc(1, after);
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
        doc.assertDoc(1, after);
        commonComplete(doc);
    }

    private void commonComplete(DocumentAssert doc){
        ///           01234567890
        String raw = "!%@id:Text";

        IDBuilder createId = buildId("id");
        NoteLineAssert line = new NoteLineAssert(doc)
            .setFormattedSpan(0, 4).setNote(1)
            .setBuildId(createId).setLookup("{@id}");
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(createId).setLookup("id");
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("Text");

        line.test(5,          raw,    0);
        doc.assertKey( 0,  2, "!%",   0, 0);
        doc.assertKey( 2,  3, "@",    0, 1);
        id.test(1,            "id",   0, 2);
        doc.assertChild(1,    "id",   0, 2, 0);
        doc.assertId(  3,  5, "id",   0, 2, 0, 0);
        doc.assertKey( 5,  6, ":",    0, 3);
        main.test(1,          "Text", 0, 4);
        doc.assertChild(1,    "Text", 0, 4, 0);
        doc.assertText(6, 10, "Text", 0, 4, 0, 0);
        doc.assertRest();
    }


    @Test
    public void noText(){
        String raw = "!%@id:";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder createId = buildId("id");
        NoteLineAssert line = new NoteLineAssert(doc)
            .setNote(0).setBuildId(createId).setLookup("{@id}");
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(createId).setLookup("id");

        line.test(4,         raw,  0);
        doc.assertKey( 0, 2, "!%", 0, 0);
        doc.assertKey( 2, 3, "@",  0, 1);
        id.test(1,           "id", 0, 2);
        doc.assertChild(1,   "id", 0, 2, 0);
        doc.assertId(  3, 5, "id", 0, 2, 0, 0);
        doc.assertKey( 5, 6, ":",  0, 3);
        doc.assertRest();
    }

    @Test
    public void noColon(){
        String raw = "!%@id";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder createId = buildId("id");
        NoteLineAssert line = new NoteLineAssert(doc)
            .setNote(0).setBuildId(createId).setLookup("{@id}");
        DirectoryAssert id = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.RESEARCH)
            .setIdentity(createId).setLookup("id");

        line.test(3,          raw,  0);
        doc.assertKey( 0, 2, "!%", 0, 0);
        doc.assertKey( 2, 3, "@",  0, 1);
        id.test(1,           "id", 0, 2);
        doc.assertChild(1,   "id", 0, 2, 0);
        doc.assertId(  3, 5, "id", 0, 2, 0, 0);
        doc.assertRest();
    }

    @Test
    public void noID(){
        String raw = "!%@:Text";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        NoteLineAssert line = new NoteLineAssert(doc)
            .setFormattedSpan(0, 3).setNote(1);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("Text");

        line.test(4,         raw,    0);
        doc.assertKey( 0, 2, "!%",   0, 0);
        doc.assertKey( 2, 3, "@",    0, 1);
        doc.assertKey( 3, 4, ":",    0, 2);
        main.test(1,         "Text", 0, 3);
        doc.assertChild(1,   "Text", 0, 3, 0);
        doc.assertText(4, 8, "Text", 0, 3, 0, 0);
        doc.assertRest();
    }


    @Test
    public void noIdJustText(){
        String raw = "!%Text";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        NoteLineAssert line = new NoteLineAssert(doc)
            .setFormattedSpan(0, 1).setNote(1);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("Text");

        line.test(2,         raw,    0);
        doc.assertKey( 0, 2, "!%",   0, 0);
        main.test(1,         "Text", 0, 1);
        doc.assertChild(1,   "Text", 0, 1, 0);
        doc.assertText(2, 6, "Text", 0, 1, 0, 0);
        doc.assertRest();
    }

    @Test
    public void noIDText(){
        String raw = "!%";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        NoteLineAssert line = new NoteLineAssert(doc)
            .setNote(0);

        line.test(1,         raw,  0);
        doc.assertKey( 0, 2, "!%", 0, 0);
        doc.assertRest();
    }

    @Test
    public void noIDColon(){
        String raw = "!%@";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        NoteLineAssert line = new NoteLineAssert(doc)
            .setNote(0);

        line.test(2,         raw, 0);
        doc.assertKey( 0, 2, "!%", 0, 0);
        doc.assertKey( 2, 3, "@",  0, 1);
        doc.assertRest();
    }
}
