package com.creativeartie.writerstudio.lang;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;

public class DocumentEditDebug{

    private static SetupParser PARSER = BranchSectionTest.getParser();

    @Test
    public void addFromEmpty(){
        String raw = "";
        DocumentAssert doc = DocumentAssert.assertDoc(0, raw, PARSER);
         ///          Doc
        doc.insert(0, "abcd");
        testBasic(doc);

    }

    @Test
    public void addBasic(){
        String raw = "abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           Doc, S, L
        doc.insert(3, "d", 0, 0);
        testBasic(doc);
    }


    private void testBasic(DocumentAssert doc){
        String done = "abcd";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done, 0);          /// Section
        doc.assertChild(1,   done, 0, 0);       /// Paragraph
        doc.assertChild(1,   done, 0, 0, 0);    /// Formatted text
        doc.assertChild(1,   done, 0, 0, 0, 0); /// Content
        doc.assertText(0, 4, done, 0, 0, 0, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void addEscape(){
        String raw = "abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///            Doc, S
        doc.insert(2, "\\", 0);

        String done = "ab\\c";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(2,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 2, "ab",  0, 0, 0, 0, 0);
        doc.assertChild(2,   "\\c", 0, 0, 0, 0, 1);
        doc.assertKey( 2, 3, "\\",  0, 0, 0, 0, 1, 0);
        doc.assertText(3, 4, "c",   0, 0, 0, 0, 1, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void insertBeforeEscape(){
        String raw = "ab\\c";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           Doc  S
        doc.insert(0, "k", 0);

        String done = "kab\\c";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(2,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 3, "kab", 0, 0, 0, 0, 0);
        doc.assertChild(2,   "\\c", 0, 0, 0, 0, 1);
        doc.assertKey( 3, 4, "\\",  0, 0, 0, 0, 1, 0);
        doc.assertText(4, 5, "c",   0, 0, 0, 0, 1, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void changeEscape(){
        String raw = "\\d";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           Doc, S, L
        doc.insert(1, "c", 0, 0);

        String done = "\\cd";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(2,   done,  0, 0, 0, 0); /// Content
        doc.assertChild(2,   "\\c", 0, 0, 0, 0, 0);
        doc.assertKey( 0, 1, "\\",  0, 0, 0, 0, 0, 0);
        doc.assertText(1, 2, "c",   0, 0, 0, 0, 0, 1);
        doc.assertText(2, 3, "d",   0, 0, 0, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void removeBasic(){
        String raw = "qwderty";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///         Doc, S, 0
        doc.delete(2, 3, 0, 0);

        String done = "qwerty";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 6, done,  0, 0, 0, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void removeString(){
        String raw = "qwasdferty";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///         Doc, S, L
        doc.delete(2, 6, 0, 0);

        String done = "qwerty";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 6, done,  0, 0, 0, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void deleteLastChar(){
        String raw = "abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///         Doc, S, L
        doc.delete(2, 3, 0, 0);

        String done = "ab";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 2, done,  0, 0, 0, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void removesLastSpan(){
        String raw = "=abc#";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///         Doc, S, L
        doc.delete(4, 5, 0, 0);

        String done = "=abc";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(2,   done,  0, 0);       /// Heading
        doc.assertKey(0, 1,  "=",   0, 0, 0);
        doc.assertChild(1,   "abc", 0, 0, 1);    /// Formatted
        doc.assertChild(1,   "abc", 0, 0, 1, 0); /// Formatted
        doc.assertText(1, 4, "abc", 0, 0, 1, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void removeEscape(){
        String raw = "ab\\c";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///         Doc, S, L
        doc.delete(2, 3, 0, 0);

        String done = "abc";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 3, done,  0, 0, 0, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void mergeLineByDelete(){
        String raw = "=asdf\n jkl; #abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///         Doc, S
        doc.delete(5, 6, 0);

        String done = "=asdf jkl; #abc";
        String content = "asdf jkl; ";
        doc.assertDoc(1, done);
        doc.assertChild(1,     done,    0);          /// Section
        doc.assertChild(3,     done,    0, 0);       /// Heading
        doc.assertKey( 0, 1,    "=",     0, 0, 0);
        doc.assertChild(1,     content, 0, 0, 1);    /// Formatted text
        doc.assertChild(1,     content, 0, 0, 1, 0); /// Content
        doc.assertText(1, 11,  content, 0, 0, 1, 0, 0);
        doc.assertChild(2,     "#abc",  0, 0, 2);    /// Status
        doc.assertKey(11, 12,  "#",     0, 0, 2, 0);
        doc.assertChild(1,     "abc",   0, 0, 2, 1); /// Status Content
        doc.assertText(12, 15, "abc",   0, 0, 2, 1, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void addStatus(){
        String raw = "=123 DRAFT";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           Doc, S, L
        doc.insert(5, "#", 0, 0);

        String done = "=123 #DRAFT";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,     0);          /// Section
        doc.assertChild(3,   done,     0, 0);       /// Heading
        doc.assertKey( 0, 1, "=",      0, 0, 0);
        doc.assertChild(1,   "123 ",   0, 0, 1);    /// Formatted text
        doc.assertChild(1,   "123 ",   0, 0, 1, 0); /// Content
        doc.assertText(1, 5, "123 ",   0, 0, 1, 0, 0);
        doc.assertChild(1,   "#DRAFT", 0, 0, 2);    /// Status
        doc.assertKey(5, 11, "#DRAFT", 0, 0, 2, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void mergeLineByEscape(){
        String raw = "#321\nmore text";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           Doc,  0
        doc.insert(4, "\\", 0);

        String done = "#321\\\nmore text";
        String content = "321\\\nmore text";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,         0);          /// Section
        doc.assertChild(2,   done,         0, 0);       /// Numbered
        doc.assertKey(0,  1, "#",          0, 0, 0);
        doc.assertChild(1,   content,      0, 0, 1);    /// Formatted text
        doc.assertChild(3,   content,      0, 0, 1, 0); /// Content
        doc.assertText(1, 4, "321",        0, 0, 1, 0, 0);
        doc.assertChild(2,   "\\\n",       0, 0, 1, 0, 1);
        doc.assertKey(4,  5, "\\",         0, 0, 1, 0, 1, 0);
        doc.assertText(5, 6, "\n",         0, 0, 1, 0, 1, 1);
        doc.assertText(6, 15, "more text", 0, 0, 1, 0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void removeAll(){
        ///           012345678901
        String raw = "=@abc:{@ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           Doc
        doc.delete(0, 11);
        doc.assertDoc(0, "");
        doc.assertIds();
    }

    @Test
    public void changeAgendaID(){
        String raw = "  {!ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///         Doc, S, L
        doc.delete(0, 2, 0, 0);
        testAgendaID(doc);
    }

    @Test
    public void addAgendaID(){
        String raw = "{ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           Doc, S, L
        doc.insert(1, "!", 0, 0);
        testAgendaID(doc);
    }

    @Test
    public void editAgendaContent(){
        String raw = "{!d}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           Doc, S, L, F  D
        doc.insert(2, "a", 0, 0, 0, 0);
        testAgendaID(doc);
    }

    private void testAgendaID(DocumentAssert doc){
        String done = "{!ad}";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done, 0);    /// Section
        doc.assertChild(1,   done, 0, 0); /// Paragraph
        doc.assertChild(1,   done, 0, 0, 0); /// Format
        doc.assertChild(3,   done, 0, 0, 0, 0); /// Agenda
        doc.assertKey( 0, 2, "{!", 0, 0, 0, 0, 0);
        doc.assertChild(1,   "ad", 0, 0, 0, 0, 1);    /// text
        doc.assertText(2, 4, "ad", 0, 0, 0, 0, 1, 0); /// text Content
        doc.assertKey( 4, 5, "}",  0, 0, 0, 0, 2);
        doc.assertLast();
        doc.addId(FormatAgendaDebug.buildId("0"), 0);
        doc.assertIds();
    }

    @Test
    public void changeHeadingID(){
        String raw = "=@abc:{@add}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           Doc, S, L, D
        doc.insert(3, "k", 0, 0, 2);
        commonHeadingId(doc);
    }

    @Test
    public void addHeadingID(){
        String raw = "={@add}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///                Doc, S, L
        doc.insert(1, "@akbc:", 0, 0);
        commonHeadingId(doc);
    }

    @Test
    public void removeHeadingID(){
        ///           01234567890123
        String raw = "=@akbc:{^ddd}{@add}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///          Doc, S, L
        doc.delete(7, 13, 0, 0);
        commonHeadingId(doc);
    }

    @Test
    public void changeChildNoteID(){
        ///           012345678901
        String raw = "=@akbc:{@ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///            doc, S, L, F, D, I
        doc.insert(10, "d", 0, 0, 4, 0, 1);
        commonHeadingId(doc);
    }

    @Test
    public void addChildNoteID(){
        ///           01234567890
        String raw = "=@akbc:@add}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           doc, S, L
        doc.insert(7, "{", 0, 0);
        commonHeadingId(doc);
    }

    private void commonHeadingId(DocumentAssert doc){
        String done = "=@akbc:{@add}";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,     0);    /// Section
        doc.assertChild(5,   done,     0, 0); /// Heading
        doc.assertKey(0,  1, "=",      0, 0, 0);
        doc.assertKey(1,  2, "@",      0, 0, 1);
        doc.assertChild(1,   "akbc",   0, 0, 2);    /// ID
        doc.assertChild(1,   "akbc",   0, 0, 2, 0); /// ID Content
        doc.assertId(2,   6, "akbc",   0, 0, 2, 0, 0);
        doc.assertKey(6,  7, ":",      0, 0, 3);
        doc.assertChild(1,   "{@add}", 0, 0, 4);    /// Content
        doc.assertChild(3,   "{@add}", 0, 0, 4, 0); /// Note
        doc.assertKey(7,  9, "{@",     0, 0, 4, 0, 0);
        doc.assertChild(1,   "add",    0, 0, 4, 0, 1);    /// ID
        doc.assertChild(1,   "add",    0, 0, 4, 0, 1, 0); /// ID Content
        doc.assertId(9,   12, "add",   0, 0, 4, 0, 1, 0, 0);
        doc.assertKey(12, 13, "}",     0, 0, 4, 0, 2);
        doc.assertLast();
        doc.addId(LinedLevelHeadDebug.buildId("akbc"), 0);
        doc.addRef(FormatCurlyDebug.buildNoteId("add"),  1);
        doc.assertIds();
    }

    @Test
    public void addIdFromEmpty(){
        String raw = "";
        DocumentAssert doc = DocumentAssert.assertDoc(0, raw, PARSER);
        ///              doc,
        doc.insert(0, "=@abc");
        commonIdEnd(doc);
    }

    @Test
    public void editIDLast(){
        ///           01234
        String raw = "=@ac";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///           doc, S, L, I
        doc.insert(3, "b", 0, 0, 2);
        commonIdEnd(doc);
    }


    @Test
    public void addIdLast(){
        ///           01
        String raw = "=@";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///             doc, S, l
        doc.insert(2, "abc", 0, 0);
        commonIdEnd(doc);
    }

    private void commonIdEnd(DocumentAssert doc){
        String done = "=@abc";
        doc.assertDoc(1, done);
        doc.assertChild(1,  done,  0);    /// Section
        doc.assertChild(3,  done,  0, 0); /// Heading
        doc.assertKey(0, 1, "=",   0, 0, 0);
        doc.assertKey(1, 2, "@",   0, 0, 1);
        doc.assertChild(1,  "abc", 0, 0, 2);    /// ID
        doc.assertChild(1,  "abc", 0, 0, 2, 0); /// ID Content
        doc.assertId(2,  5, "abc", 0, 0, 2, 0, 0);
        doc.assertLast();
        doc.addId(LinedLevelHeadDebug.buildId("abc"), 0);
        doc.assertIds();
    }

    @Test
    public void removeIdEnd(){
        ///           0123456
        String raw = "==@abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///         doc, S, S, L
        doc.delete(3, 6, 0, 0, 0);

        String done = "==@";
        doc.assertDoc(1, done);
        doc.assertChild(1,  done,  0);       /// Section
        doc.assertChild(1,  done,  0, 0);    /// Section
        doc.assertChild(2,  done,  0, 0, 0); /// Heading
        doc.assertKey(0, 2, "==",  0, 0, 0, 0);
        doc.assertKey(2, 3, "@",   0, 0, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }
}
