package com.creativeartie.writerstudio.lang;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.rules.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.markup.*;

@RunWith(JUnit4.class)
public class DocumentEditDebug{

    @Test
    public void addFromEmpty(){
        String raw = "";
        WritingText base = new WritingText(raw);
        base.insert(0, "abcd");
        testBasic(base);

    }

    @Test
    public void addBasic(){
        String raw = "abc";
        WritingText base = new WritingText(raw);
        base.insert(3, "d");
        testBasic(base);
    }


    private void testBasic(WritingText base){
        String done = "abcd";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done, 0);          /// Section
        doc.assertChild(1,       done, 0, 0);       /// Paragraph
        doc.assertChild(1,       done, 0, 0, 0);    /// Formatted text
        doc.assertChild(1,       done, 0, 0, 0, 0); /// Content
        doc.assertTextLeaf(0, 4, done, 0, 0, 0, 0, 0);
        doc.assertIds();
    }

    @Test
    public void addEscape(){
        String raw = "abc";
        WritingText base = new WritingText(raw);
        base.insert(2, "\\");

        String done = "ab\\c";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,  0);          /// Section
        doc.assertChild(1,       done,  0, 0);       /// Paragraph
        doc.assertChild(1,       done,  0, 0, 0);    /// Formatted text
        doc.assertChild(2,       done,  0, 0, 0, 0); /// Content
        doc.assertTextLeaf(0, 2, "ab",  0, 0, 0, 0, 0);
        doc.assertChild(2,       "\\c", 0, 0, 0, 0, 1);
        doc.assertKeyLeaf( 2, 3, "\\",  0, 0, 0, 0, 1, 0);
        doc.assertTextLeaf(3, 4, "c",   0, 0, 0, 0, 1, 1);
        doc.assertIds();
    }

    @Test
    public void insertBeforeEscape(){
        String raw = "ab\\c";
        WritingText base = new WritingText(raw);
        base.insert(0, "k");

        String done = "kab\\c";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,  0);          /// Section
        doc.assertChild(1,       done,  0, 0);       /// Paragraph
        doc.assertChild(1,       done,  0, 0, 0);    /// Formatted text
        doc.assertChild(2,       done,  0, 0, 0, 0); /// Content
        doc.assertTextLeaf(0, 3, "kab", 0, 0, 0, 0, 0);
        doc.assertChild(2,       "\\c", 0, 0, 0, 0, 1);
        doc.assertKeyLeaf( 3, 4, "\\",  0, 0, 0, 0, 1, 0);
        doc.assertTextLeaf(4, 5, "c",   0, 0, 0, 0, 1, 1);
        doc.assertIds();
    }

    @Test
    public void changeEscape(){
        String raw = "\\d";
        WritingText base = new WritingText(raw);
        base.insert(1, "c");

        String done = "\\cd";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,  0);          /// Section
        doc.assertChild(1,       done,  0, 0);       /// Paragraph
        doc.assertChild(1,       done,  0, 0, 0);    /// Formatted text
        doc.assertChild(2,       done,  0, 0, 0, 0); /// Content
        doc.assertChild(2,       "\\c", 0, 0, 0, 0, 0);
        doc.assertKeyLeaf( 0, 1, "\\",  0, 0, 0, 0, 0, 0);
        doc.assertTextLeaf(1, 2, "c",   0, 0, 0, 0, 0, 1);
        doc.assertTextLeaf(2, 3, "d",   0, 0, 0, 0, 1);
        doc.assertIds();
    }

    @Test
    public void removeBasic(){
        String raw = "qwderty";
        WritingText base = new WritingText(raw);
        base.delete(2, 3);

        String done = "qwerty";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,  0);          /// Section
        doc.assertChild(1,       done,  0, 0);       /// Paragraph
        doc.assertChild(1,       done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,       done,  0, 0, 0, 0); /// Content
        doc.assertTextLeaf(0, 6, done,  0, 0, 0, 0, 0);
        doc.assertIds();
    }

    @Test
    public void removeString(){
        String raw = "qwasdferty";
        WritingText base = new WritingText(raw);
        base.delete(2, 6);

        String done = "qwerty";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,  0);          /// Section
        doc.assertChild(1,       done,  0, 0);       /// Paragraph
        doc.assertChild(1,       done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,       done,  0, 0, 0, 0); /// Content
        doc.assertTextLeaf(0, 6, done,  0, 0, 0, 0, 0);
        doc.assertIds();
    }

    @Test
    public void deleteLastChar(){
        String raw = "abc";
        WritingText base = new WritingText(raw);
        base.delete(2, 3);

        String done = "ab";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,  0);          /// Section
        doc.assertChild(1,       done,  0, 0);       /// Paragraph
        doc.assertChild(1,       done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,       done,  0, 0, 0, 0); /// Content
        doc.assertTextLeaf(0, 2, done,  0, 0, 0, 0, 0);
        doc.assertIds();
    }

    @Test
    public void removesLastSpan(){
        String raw = "=abc#";
        WritingText base = new WritingText(raw);
        base.delete(4, 5);

        String done = "=abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,  0);          /// Section
        doc.assertChild(2,       done,  0, 0);       /// Heading
        doc.assertKeyLeaf(0, 1,  "=",   0, 0, 0);
        doc.assertChild(1,       "abc", 0, 0, 1);    /// Formatted
        doc.assertChild(1,       "abc", 0, 0, 1, 0); /// Formatted
        doc.assertTextLeaf(1, 4, "abc", 0, 0, 1, 0, 0);
        doc.assertIds();
    }

    @Test
    public void removeEscape(){
        String raw = "ab\\c";
        WritingText base = new WritingText(raw);
        base.delete(2, 3);

        String done = "abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,  0);          /// Section
        doc.assertChild(1,       done,  0, 0);       /// Paragraph
        doc.assertChild(1,       done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,       done,  0, 0, 0, 0); /// Content
        doc.assertTextLeaf(0, 3, done,  0, 0, 0, 0, 0);
        doc.assertIds();
    }

    @Test
    public void mergeLineByDelete(){
        String raw = "=asdf\n jkl; #abc";
        WritingText base = new WritingText(raw);
        base.delete(5, 6);

        String done = "=asdf jkl; #abc";
        String content = "asdf jkl; ";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,         done,    0);          /// Section
        doc.assertChild(3,         done,    0, 0);       /// Heading
        doc.assertKeyLeaf(0, 1,    "=",     0, 0, 0);
        doc.assertChild(1,         content, 0, 0, 1);    /// Formatted text
        doc.assertChild(1,         content, 0, 0, 1, 0); /// Content
        doc.assertTextLeaf(1, 11,  content, 0, 0, 1, 0, 0);
        doc.assertChild(2,         "#abc",  0, 0, 2);    /// Status
        doc.assertKeyLeaf(11, 12,  "#",     0, 0, 2, 0);
        doc.assertChild(1,         "abc",   0, 0, 2, 1); /// Status Content
        doc.assertTextLeaf(12, 15, "abc",   0, 0, 2, 1, 0);
        doc.assertIds();
    }

    @Test
    public void addStatus(){
        String raw = "=123 DRAFT";
        WritingText base = new WritingText(raw);
        base.insert(5, "#");

        String done = "=123 #DRAFT";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,     0);          /// Section
        doc.assertChild(3,       done,     0, 0);       /// Heading
        doc.assertKeyLeaf(0, 1,  "=",      0, 0, 0);
        doc.assertChild(1,       "123 ",   0, 0, 1);    /// Formatted text
        doc.assertChild(1,       "123 ",   0, 0, 1, 0); /// Content
        doc.assertTextLeaf(1, 5, "123 ",   0, 0, 1, 0, 0);
        doc.assertChild(1,       "#DRAFT", 0, 0, 2);    /// Status
        doc.assertKeyLeaf(5, 11, "#DRAFT", 0, 0, 2, 0);

        doc.assertIds();
    }

    @Test
    public void mergeLineByEscape(){
        String raw = "#321\nmore text";
        WritingText base = new WritingText(raw);
        base.insert(4, "\\");

        String done = "#321\\\nmore text";
        String content = "321\\\nmore text";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,         0);          /// Section
        doc.assertChild(2,       done,         0, 0);       /// Numbered
        doc.assertKeyLeaf(0,  1, "#",          0, 0, 0);
        doc.assertChild(1,       content,      0, 0, 1);    /// Formatted text
        doc.assertChild(3,       content,      0, 0, 1, 0); /// Content
        doc.assertTextLeaf(1, 4, "321",        0, 0, 1, 0, 0);
        doc.assertChild(2,       "\\\n",       0, 0, 1, 0, 1);
        doc.assertKeyLeaf(4,  5, "\\",         0, 0, 1, 0, 1, 0);
        doc.assertTextLeaf(5, 6, "\n",         0, 0, 1, 0, 1, 1);
        doc.assertTextLeaf(6, 15, "more text", 0, 0, 1, 0, 2);
        doc.assertIds();
    }

    @Test
    public void removeAll(){
        ///           012345678901
        String raw = "=@abc:{@ad}";
        WritingText base = new WritingText(raw);
        base.delete(0, 11);
        DocumentAssert doc = DocumentAssert.assertDoc(0, "", base);
        doc.assertIds();
    }

    @Test
    public void changeAgendaID(){
        String raw = "  {!ad}";
        WritingText base = new WritingText(raw);
        base.delete(0, 2);
        testAgendaID(base);
    }

    @Test
    public void addAgendaID(){
        String raw = "{ad}";
        WritingText base = new WritingText(raw);
        base.insert(1, "!");
        testAgendaID(base);
    }

    @Test
    public void editAgendaContent(){
        String raw = "{!d}";
        WritingText base = new WritingText(raw);
        base.insert(2, "a");
        testAgendaID(base);
    }

    private void testAgendaID(WritingText base){
        String done = "{!ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done, 0);    /// Section
        doc.assertChild(1,       done, 0, 0); /// Paragraph
        doc.assertChild(1,       done, 0, 0, 0); /// Format
        doc.assertChild(3,       done, 0, 0, 0, 0); /// Agenda
        doc.assertKeyLeaf( 0, 2, "{!", 0, 0, 0, 0, 0);
        doc.assertChild(1,       "ad", 0, 0, 0, 0, 1);    /// text
        doc.assertTextLeaf(2, 4, "ad", 0, 0, 0, 0, 1, 0); /// text Content
        doc.assertKeyLeaf( 4, 5, "}",  0, 0, 0, 0, 2);
        doc.addId(FormatAgendaDebug.buildId("0"), 0);
        doc.assertIds(true);
    }

    @Test
    public void changeHeadingID(){
        String raw = "=@abc:{@add}";
        WritingText base = new WritingText(raw);
        base.insert(3, "k");
        commonHeadingId(base);
    }

    @Test
    public void addHeadingID(){
        String raw = "={@add}";
        WritingText base = new WritingText(raw);
        base.insert(1, "@akbc:");
        commonHeadingId(base);
    }

    @Test
    public void removeHeadingID(){
        ///           01234567890123
        String raw = "=@akbc:{^ddd}{@add}";
        WritingText base = new WritingText(raw);
        base.delete(7, 13);
        commonHeadingId(base);
    }

    @Test
    public void changeChildNoteID(){
        ///           012345678901
        String raw = "=@akbc:{@ad}";
        WritingText base = new WritingText(raw);
        base.insert(10, "d");
        commonHeadingId(base);
    }

    @Test
    public void addChildNoteID(){
        ///           01234567890
        String raw = "=@akbc:@add}";
        WritingText base = new WritingText(raw);
        base.insert(7, "{");
        commonHeadingId(base);
    }

    /// TODO test new id from empty

    /// TODO test edit id at end

    /// TODO test add id at end

    /// TODO test remove id at end

    /// TODO test edit id at start

    /// TODO test add id at start

    /// TODO test remove id at start



    private void commonHeadingId(WritingText base){
        String done = "=@akbc:{@add}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,     0);    /// Section
        doc.assertChild(5,       done,     0, 0); /// Heading
        doc.assertKeyLeaf(0,  1, "=",      0, 0, 0);
        doc.assertKeyLeaf(1,  2, "@",      0, 0, 1);
        doc.assertChild(1,       "akbc",   0, 0, 2);    /// ID
        doc.assertChild(1,       "akbc",   0, 0, 2, 0); /// ID Content
        doc.assertIdLeaf(2,   6, "akbc",   0, 0, 2, 0, 0);
        doc.assertKeyLeaf(6,  7, ":",      0, 0, 3);
        doc.assertChild(1,       "{@add}", 0, 0, 4);    /// Content
        doc.assertChild(3,       "{@add}", 0, 0, 4, 0); /// Note
        doc.assertKeyLeaf(7,  9, "{@",     0, 0, 4, 0, 0);
        doc.assertChild(1,       "add",    0, 0, 4, 0, 1);    /// ID
        doc.assertChild(1,       "add",    0, 0, 4, 0, 1, 0); /// ID Content
        doc.assertIdLeaf(9,   12, "add",   0, 0, 4, 0, 1, 0, 0);
        doc.assertKeyLeaf(12, 13, "}",     0, 0, 4, 0, 2);
        doc.addId(LinedLevelHeadDebug.buildId("akbc"), 0);
        doc.addRef(FormatCurlyDebug.buildNoteId("add"),  1);
        doc.assertIds();
    }
}