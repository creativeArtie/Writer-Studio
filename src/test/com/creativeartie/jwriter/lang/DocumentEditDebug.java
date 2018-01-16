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
    public void addBasic(){
        String raw = "abc";
        ManuscriptDocument base = new ManuscriptDocument(raw);
        base.insert(3, "d");

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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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
        ManuscriptDocument base = new ManuscriptDocument(raw);
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

    @Test
    @Ignore("Test not ready")
    public void clearChildren(){
        ManuscriptDocument base = new ManuscriptDocument("=@");
        base.delete(1, 2);
    }

    @Test
    @Ignore("Test not ready")
    public void changeCiteRef(){
        ManuscriptDocument base = new ManuscriptDocument("=@abc:{@ad}");
        base.insert(base.getLocalEnd() - 2, "k");
    }

    @Test
    @Ignore("Test not ready")
    public void changeCiteCurly(){
        ManuscriptDocument base = new ManuscriptDocument("=@abc:{@ad}");
        base.insert(base.getLocalEnd() - 1, "k");
    }

    @Test
    @Ignore("Test not ready")
    public void editNote(){
        ManuscriptDocument base = new ManuscriptDocument("!%@abc:{@abc}");
        base.insert(base.getLocalEnd(), "k");
    }

    @Test
    @Ignore("Test not ready")
    public void toHeading(){
        ManuscriptDocument base = new ManuscriptDocument("Canada{@ad}#DRAFT");
        base.insert(0, "=");
    }

    @Test
    @Ignore("Test not ready")
    public void removeHeadingId(){
        ManuscriptDocument base = new ManuscriptDocument("=@abc:{@ad}");
        base.delete(1, 1);
    }

    @Test
    @Ignore("Test not ready")
    public void splitByChanging(){
        ManuscriptDocument base = new ManuscriptDocument("abc\nddd\nooo");
        base.insert(4, "=");

    }

    @Test
    @Ignore("Test not ready")
    public void splitByNewLine(){
        ManuscriptDocument base = new ManuscriptDocument("abc\n=ddd\nooo");

        base.insert(8, "\n");
    }

    /*
    @Test
    public void changeCiteRef(){
        ManuscriptDocument doc = new ManuscriptDocument("=@abc:{@ad}");
        doc.insert(doc.getLocalEnd() - 2, "k");
        CatalogueMap map = doc.getCatalogue();
        assertEquals("Wrong size.", 2, map.size());

        assertEquals("=@abc:{@akd}", doc.getRaw());

        SpanBranch span =    doc.get(0); /// Scetion;
        span = (SpanBranch) span.get(0); /// Heading
        CatalogueData data = getData(map, getId("link", "abc"));
        assertTrue("Span not found: " + span, data.getIds().contains(span));

        assertEquals("=",   span.get(0).getRaw());
        assertEquals("@",   span.get(1).getRaw());
        assertEquals("abc", ((SpanBranch)span.get(2)).get(0).getRaw());
        assertEquals(":",   span.get(3).getRaw());


        span = (SpanBranch) span.get(4); /// Written Text
        span = (SpanBranch) span.get(0); /// Cite span
        data = getData(map, getId("note", "akd"));
        assertTrue("Span not found: " + span, data.getRefs().contains(span));

        assertEquals("{@",               span.get(0)        .getRaw());
        assertEquals("akd", ((SpanBranch)span.get(1)).get(0).getRaw());
        assertEquals("}",                span.get(2)        .getRaw());
    }

    @Test
    public void changeCiteCurly(){
        ManuscriptDocument doc = new ManuscriptDocument("=@abc:{@ad}");
        doc.insert(doc.getLocalEnd() - 1, "k");
        CatalogueMap map = doc.getCatalogue();
        assertEquals("Wrong size.", 2, map.size());

        assertEquals("=@abc:{@adk}", doc.getRaw());

        SpanBranch span =    doc.get(0); /// Scetion;
        span = (SpanBranch) span.get(0); /// Heading
        CatalogueData data = getData(map, getId("link", "abc"));
        assertTrue("Span not found: " + span, data.getIds().contains(span));

        assertEquals("=",   span.get(0).getRaw());
        assertEquals("@",   span.get(1).getRaw());
        assertEquals("abc", ((SpanBranch)span.get(2)).get(0).getRaw());
        assertEquals(":",   span.get(3).getRaw());


        span = (SpanBranch) span.get(4); /// Written Text
        span = (SpanBranch) span.get(0); /// Cite span
        data = getData(map, getId("note", "adk"));
        assertTrue("Span not found: " + span, data.getRefs().contains(span));

        assertEquals("{@",               span.get(0)        .getRaw());
        assertEquals("adk", ((SpanBranch)span.get(1)).get(0).getRaw());
        assertEquals("}",                span.get(2)        .getRaw());
    }

    @Test
    public void editNote(){
        ManuscriptDocument doc = new ManuscriptDocument("!%@abc:{@abc}");
        doc.insert(doc.getLocalEnd(), "k");
        CatalogueMap map = doc.getCatalogue();
        assertEquals("Wrong size.", 1, map.size());

        assertEquals("!%@abc:{@abc}k", doc.getRaw());

        SpanBranch span =    doc.get(0); /// Section;

        CatalogueData data = getData(map, getId("note", "abc"));
        assertTrue("Span not found: " + span, data.getIds().contains(span));

        span = (SpanBranch) span.get(0); /// Note
        assertEquals("!%",  span.get(0).getRaw());
        assertEquals("@",   span.get(1).getRaw());
        assertEquals("abc", ((SpanBranch)span.get(2)).get(0).getRaw());
        assertEquals(":",   span.get(3).getRaw());


        span = (SpanBranch) span.get(4); /// Written Text
        span = (SpanBranch) span.get(0); /// Cite span
        data = getData(map, getId("note", "abc"));
        assertTrue("Span not found: " + span, data.getRefs().contains(span));

        assertEquals("{@",               span.get(0)        .getRaw());
        assertEquals("abc", ((SpanBranch)span.get(1)).get(0).getRaw());
        assertEquals("}",                span.get(2)        .getRaw());

        assertEquals("k", ((SpanBranch)span.getParent().get(1)).get(0).getRaw());
    }

    @Test
    public void toHeading(){
        ManuscriptDocument doc = new ManuscriptDocument("Canada{@ad}#DRAFT");
        doc.insert(0, "=");
        CatalogueMap map = doc.getCatalogue();
        assertEquals("Wrong size.", 1, map.size());

        assertEquals("=Canada{@ad}#DRAFT", doc.getRaw());

        SpanBranch span =    doc.get(0); /// Scetion;
        span = (SpanBranch) span.get(0); /// Heading
        assertEquals("=",   span.get(0).getRaw());

        span = (SpanBranch) span.get(1); /// Written Text
        assertEquals("Canada", ((SpanBranch)span.get(0)).get(0).getRaw());

        span = (SpanBranch) span.get(1); /// Cite
        CatalogueData data = getData(map, getId("note", "ad"));
        assertTrue("Span not found: " + span, data.getRefs().contains(span));

        assertEquals("ad", ((SpanBranch)span.get(1)).get(0).getRaw());
        assertEquals("}",               span.get(2)        .getRaw());

        span = (SpanBranch) span.getParent().getParent().get(2);
        assertEquals("#DRAFT", span.get(0).getRaw());
    }

    @Test
    public void removeHeadingId(){
        ManuscriptDocument doc = new ManuscriptDocument("=@abc:{@ad}");
        doc.delete(1, 1);
        CatalogueMap map = doc.getCatalogue();
        assertEquals("Wrong size.", 1, map.size());

        assertEquals("=abc:{@ad}", doc.getRaw());

        SpanBranch span =    doc.get(0); /// Scetion;
        span = (SpanBranch) span.get(0); /// Heading
        assertEquals("=",   span.get(0).getRaw());

        span = (SpanBranch) span.get(1); /// Written Text
        assertEquals("abc:", ((SpanBranch)span.get(0)).get(0).getRaw());

        span = (SpanBranch) span.get(1); /// Cite
        CatalogueData data = getData(map, getId("note", "ad"));
        assertTrue("Span not found: " + span, data.getRefs().contains(span));

        assertEquals("ad", ((SpanBranch)span.get(1)).get(0).getRaw());
        assertEquals("}",               span.get(2)        .getRaw());
    }

    @Test
    public void splitByChanging(){
        ManuscriptDocument doc = new ManuscriptDocument("abc\nddd\nooo");

        doc.insert(4, "=");

        CatalogueMap map = doc.getCatalogue();
        assertEquals("Wrong size.", 0, map.size());
        assertEquals("abc\n=ddd\nooo", doc.getRaw());

        SpanBranch span =    doc.get(0); /// Section;
        span = (SpanBranch) span.get(0); /// Paragraph
        assertEquals("abc", span.get(0).getRaw());
        assertEquals("\n",  span.get(1).getRaw());

        span = doc.get(1); /// Section
        span = (SpanBranch) span.get(0);
        assertEquals("=",   span.get(0).getRaw());
        assertEquals("ddd", span.get(1).getRaw());
        assertEquals("\n",  span.get(2).getRaw());

        span = doc.get(1); /// Section
        span = (SpanBranch) span.get(1);
        assertEquals("ooo", span.get(0).getRaw());

    }
    @Test
    public void splitByNewLine(){
        ///                                              012345678 9
        ManuscriptDocument doc = new ManuscriptDocument("abc\n=ddd\nooo");

        doc.insert(8, "\n");

        CatalogueMap map = doc.getCatalogue();
        assertEquals("Wrong size.", 0, map.size());
        assertEquals("abc\n=ddd\n\nooo", doc.getRaw());

        SpanBranch span =    doc.get(0); /// Section;
        span = (SpanBranch) span.get(0); /// Paragraph
        assertEquals("abc", span.get(0).getRaw());
        assertEquals("\n",  span.get(1).getRaw());

        span = doc.get(1); /// Section
        span = (SpanBranch) span.get(0);
        assertEquals("=",   span.get(0).getRaw());
        assertEquals("ddd", span.get(1).getRaw());
        assertEquals("\n",  span.get(2).getRaw());

        span = doc.get(1); /// Section
        span = (SpanBranch) span.get(1);
        assertEquals("\n", span.get(0).getRaw());

        span = doc.get(1); /// Section
        span = (SpanBranch) span.get(2);
        assertEquals("ooo", span.get(0).getRaw());

    }

    private CatalogueIdentity getId(String cat, String id){
        return new CatalogueIdentity(Arrays.asList(cat), id);
    }

    private CatalogueData getData(CatalogueMap map, CatalogueIdentity id){
        CatalogueData data = map.get(id);
        assertNotNull("Id not found: " + id, map.get(id));
        return data;

    }*/
}
