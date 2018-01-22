package com.creativeartie.jwriter.lang;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.rules.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.creativeartie.jwriter.lang.markup.*;

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
    public void changeHeadingID(){
        String raw = "=@abc:{@ad}";
        WritingText base = new WritingText(raw);
        base.insert(3, "k");


        String done = "=@akbc:{@ad}";
        DocumentAssert doc = DocumentAssert.assertDoc(1, done, base);
        doc.assertChild(1,       done,    0);    /// Section
        doc.assertChild(5,       done,    0, 0); /// Heading
        doc.assertKeyLeaf(0,  1, "=",     0, 0, 0);
        doc.assertKeyLeaf(1,  2, "@",     0, 0, 1);
        doc.assertChild(1,       "akbc",  0, 0, 2);    /// ID
        doc.assertChild(1,       "akbc",  0, 0, 2, 0); /// ID Content
        doc.assertIdLeaf(2,   6, "akbc",  0, 0, 2, 0, 0);
        doc.assertKeyLeaf(6,  7, ":",     0, 0, 3);
        doc.assertChild(1,       "{@ad}", 0, 0, 4);    /// Content
        doc.assertChild(3,       "{@ad}", 0, 0, 4, 0); /// Note
        doc.assertKeyLeaf(7,  9, "{@",    0, 0, 4, 0, 0);
        doc.assertChild(1,       "ad",    0, 0, 4, 0, 1);    /// ID
        doc.assertChild(1,       "ad",    0, 0, 4, 0, 1, 0); /// ID Content
        doc.assertIdLeaf(9,   11, "ad",   0, 0, 4, 0, 1, 0, 0);
        doc.assertKeyLeaf(11, 12, "}",    0, 0, 4, 0, 2);
        doc.addId(LinedLevelHeadDebug.buildId("akbc"), 0);
        doc.addRef(FormatCurlyDebug.buildNoteId("ad"),  1);
        doc.assertIds();
    }
}
