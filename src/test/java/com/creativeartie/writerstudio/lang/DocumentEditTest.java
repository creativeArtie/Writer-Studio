package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.markup.*;

@DisplayName("Some Document Editing Tester")
public class DocumentEditTest{

    private static Document build(String raw){
        return new WritingText(raw);
    }

    @Test
    @DisplayName("Add from Empty")
    public void addFromEmpty(){
        String raw = "";
        DocumentAssert doc = DocumentAssert.assertDoc(0, raw, build(raw));
         ///          Doc
        doc.insert(0, "abcd");
        testBasic(doc);

    }

    @Test
    @DisplayName("Add Basic")
    public void addBasic(){
        String raw = "abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
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
        doc.assertRest();
    }

    @Test
    @DisplayName("Add Escape")
    public void addEscape(){
        String raw = "abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
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
        doc.assertRest();
    }

    @Test
    @DisplayName("Add at Beginning")
    public void addBeforeEscape(){
        String raw = "ab\\c";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
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
        doc.assertRest();
    }

    @Test
    @DisplayName("Add at Escape Span")
    public void changeEscape(){
        String raw = "\\d";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
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
        doc.assertRest();
    }

    @Test
    @DisplayName("Delete Basic")
    public void removeBasic(){
        String raw = "qwderty";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///         Doc, S, 0
        doc.delete(2, 3, 0, 0);

        String done = "qwerty";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 6, done,  0, 0, 0, 0, 0);
        doc.assertRest();
    }

    @Test
    @DisplayName("Delete Last Characters")
    public void removeLast(){
        String raw = "qwasdferty";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///         Doc, S, L
        doc.delete(2, 6, 0, 0);

        String done = "qwerty";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 6, done,  0, 0, 0, 0, 0);
        doc.assertRest();
    }

    @Test
    @DisplayName("Delete First Characters")
    public void removeFirst(){
        String raw = "aab";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///         Doc, S, L
        doc.delete(0, 1, 0, 0);

        String done = "ab";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 2, done,  0, 0, 0, 0, 0);
        doc.assertRest();
    }

    @Test
    @DisplayName("Delete Last Span")
    public void removesLastSpan(){
        String raw = "=abc#";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
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
        doc.assertRest();
    }

    @Test
    @DisplayName("Delete Span")
    public void removeEscape(){
        String raw = "ab\\c";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///         Doc, S, L
        doc.delete(2, 3, 0, 0);

        String done = "abc";
        doc.assertDoc(1, done);
        doc.assertChild(1,   done,  0);          /// Section
        doc.assertChild(1,   done,  0, 0);       /// Paragraph
        doc.assertChild(1,   done,  0, 0, 0);    /// Formatted text
        doc.assertChild(1,   done,  0, 0, 0, 0); /// Content
        doc.assertText(0, 3, done,  0, 0, 0, 0, 0);
        doc.assertRest();
    }

    @Test
    @DisplayName("Delete New Line")
    public void mergeLineByDelete(){
        String raw = "=asdf\n jkl; #abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
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
        doc.assertRest();
    }

    @Test
    @DisplayName("Add Last Span")
    public void addStatus(){
        String raw = "=123 DRAFT";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
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
        doc.assertRest();
    }

    @Test
    @DisplayName("Add to Remove")
    public void mergeLineByEscape(){
        String raw = "#321\nmore text";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
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
        doc.assertRest();
    }

    @Test
    @DisplayName("Delete All")
    public void removeAll(){
        ///           012345678901
        String raw = "=@abc:{@ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///           Doc
        doc.delete(0, 11);
        doc.assertDoc(0, "");
        doc.assertRest();
    }

    @Test
    @DisplayName("Delete Spaces")
    public void changeAgendaID(){
        String raw = "  {!ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///         Doc, S, L
        doc.delete(0, 2, 0, 0);
        testAgendaID(doc);
    }

    @Test
    @DisplayName("Add to Convert Span")
    public void addAgendaID(){
        String raw = "{ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///           Doc, S, L
        doc.insert(1, "!", 0, 0);
        testAgendaID(doc);
    }

    @Test
    @DisplayName("Add to Child Span")
    public void changeAgendaContent(){
        String raw = "{!d}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
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
        doc.addId(FormatAgendaDebug.buildId("0"), 0);
        doc.assertRest();
    }

    @Test
    @DisplayName("Heading Content Changes")
    public void changeHeadingID(){
        String raw = "=@abc:{@add}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///           Doc, S, L, D
        doc.insert(3, "k", 0, 0, 2);
        commonHeadingId(doc);
    }

    @Test
    @DisplayName("Heading ID Added")
    public void addHeadingID(){
        String raw = "={@add}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///                Doc, S, L
        doc.insert(1, "@akbc:", 0, 0);
        commonHeadingId(doc);
    }

    @Test
    @DisplayName("Heading Ref Span Removed")
    public void removeHeadingID(){
        ///           01234567890123
        String raw = "=@akbc:{^ddd}{@add}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///          Doc, S, L
        doc.delete(7, 13, 0, 0);
        commonHeadingId(doc);
    }

    @Test
    @DisplayName("Heading Ref Span Edited")
    public void changeChildNoteID(){
        ///           012345678901
        String raw = "=@akbc:{@ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///            doc, S, L, F, D, I
        doc.insert(10, "d", 0, 0, 4, 0, 1);
        commonHeadingId(doc);
    }

    @Test
    @DisplayName("Heading Ref Span Added")
    public void addChildNoteID(){
        ///           01234567890
        String raw = "=@akbc:@add}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///           doc, S, L
        doc.insert(7, "{", 0, 0);
        commonHeadingId(doc);
    }

    /// common heading test
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
        doc.addId(LinedLevelHeadDebug.buildId("akbc"), 0);
        doc.addRef(FormatCurlyDebug.buildNoteId("add"),  1);
        doc.assertRest();
    }

    @Test
    @DisplayName("ID Add from Empty")
    public void addIdFromEmpty(){
        String raw = "";
        DocumentAssert doc = DocumentAssert.assertDoc(0, raw, build(raw));
        ///              doc,
        doc.insert(0, "=@abc");
        commonIdEnd(doc);
    }

    @Test
    @DisplayName("ID Edited")
    public void changeIDLast(){
        ///           01234
        String raw = "=@ac";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///           doc, S, L, I
        doc.insert(3, "b", 0, 0, 2);
        commonIdEnd(doc);
    }


    @Test
    @DisplayName("ID Added to Last")
    public void addIdLast(){
        ///           01
        String raw = "=@";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///             doc, S, l
        doc.insert(2, "abc", 0, 0);
        commonIdEnd(doc);
    }

    /// common id tests
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
        doc.addId(LinedLevelHeadDebug.buildId("abc"), 0);
        doc.assertRest();
    }

    @Test
    @DisplayName("ID Removed")
    public void removeIdEnd(){
        ///           0123456
        String raw = "==@abc";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, build(raw));
        ///         doc, S, S, L
        doc.delete(3, 6, 0, 0, 0);

        String done = "==@";
        doc.assertDoc(1, done);
        doc.assertChild(1,  done,  0);       /// Section
        doc.assertChild(1,  done,  0, 0);    /// Section
        doc.assertChild(2,  done,  0, 0, 0); /// Heading
        doc.assertKey(0, 2, "==",  0, 0, 0, 0);
        doc.assertKey(2, 3, "@",   0, 0, 0, 1);
        doc.assertRest();
    }
}
