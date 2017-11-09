package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchDataTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class LinedCiteDebug {

    public static void assertCite(SpanBranch span, InfoFieldType field,
        Span data, int noteCount)
    {
        LinedSpanCite test = assertClass(span, LinedSpanCite.class);

        LinedType lined = LinedType.SOURCE;
        DetailStyle[] styles = data == null?
            new DetailStyle[]{lined, field, AuxiliaryStyle.DATA_ERROR} :
            new DetailStyle[]{lined, field};

        assertEquals(getError("field", test), field, test.getFieldType());
        assertEquals(getError("lined", test), lined, test.getLinedType());
        assertSpan(  getError("data",  test), data,  test.getData());
        LinedRestDebug.assertLine(test, 0, noteCount);
        assertBranch(span, styles);
    }

    private static final SetupParser[] parsers = new SetupParser[]{
            LinedParseCite.INSTANCE};

    @Test
    public void inTextComplete(){
        String raw = "!>in-text:a";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        CiteLineTest cite = new CiteLineTest()
            .setInfoType(InfoFieldType.IN_TEXT)
            .setDataSpan(doc, 0, 3).setNoteCount(1);
        FieldTest field = new FieldTest()
            .setType(InfoFieldType.IN_TEXT);
        ContentDataTest data = new ContentDataTest()
            .setData(doc, 0, 3, 0);


        cite.test(        doc,  4, raw,       0);
        doc.assertKeyLeaf(  0,  2, "!>",      0, 0);
        field.test(       doc,  1, "in-text", 0, 1);
        doc.assertFieldLeaf(2,  9, "in-text", 0, 1, 0);
        doc.assertKeyLeaf(  9, 10, ":",       0, 2);
        data.test(        doc,  1, "a",       0, 3);
        doc.assertChild(    1,     "a",       0, 3, 0);
        doc.assertDataLeaf(10, 11, "a",       0, 3, 0, 0);

        doc.assertIds();
    }

    @Test
    public void inTextNoColonNewline(){
        String raw = "!>in-text\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(3, raw,      0);
        SpanBranch field = doc.assertChild(1, "in-text", 0, 1);

        assertCite(cite, InfoFieldType.IN_TEXT, null, 0);
        InfoDebug.assertField(field, InfoFieldType.IN_TEXT);

        doc.assertKeyLeaf(  0,  2, "!>",      0, 0);
        doc.assertFieldLeaf(2,  9, "in-text", 0, 1, 0);
        doc.assertKeyLeaf(  9, 10, "\n",      0, 2);

        doc.assertIds();
    }

    @Test
    public void inTextNoColon(){
        String raw = "!>in-text";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(2, raw,       0);
        SpanBranch field = doc.assertChild(1, "in-text", 0, 1);

        assertCite(cite, InfoFieldType.IN_TEXT, null, 0);
        InfoDebug.assertField(field, InfoFieldType.IN_TEXT);

        doc.assertKeyLeaf(  0, 2, "!>",      0, 0);
        doc.assertFieldLeaf(2, 9, "in-text", 0, 1, 0);

        doc.assertIds();
    }

    @Test
    public void errorNoField(){
        String raw = "!>:a\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(4, raw, 0);

        assertCite(cite, InfoFieldType.ERROR, null, 0);

        doc.assertKeyLeaf( 0, 2, "!>", 0, 0);
        doc.assertKeyLeaf( 2, 3, ":",  0, 1);
        doc.assertTextLeaf(3, 4, "a",  0, 2);
        doc.assertKeyLeaf( 4, 5, "\n", 0, 3);

        doc.assertIds();
    }

    @Test
    public void inTextNoColonData(){
        String raw = "!>in-texta";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(3, raw, 0);
        SpanBranch field = doc.assertChild(1, "in-text", 0, 1);
        SpanBranch data  = doc.assertChild(1, "a",       0, 2);
        SpanBranch text  = doc.assertChild(1, "a",       0, 2, 0);

        assertCite(cite, InfoFieldType.IN_TEXT, data, 1);
        InfoDebug.assertField(field, InfoFieldType.IN_TEXT);
        InfoDebug.assertDataText(data, text);

        doc.assertKeyLeaf(  0,  2, "!>",      0, 0);
        doc.assertFieldLeaf(2,  9, "in-text", 0, 1, 0);
        doc.assertDataLeaf( 9, 10, "a",       0, 2, 0, 0);

        doc.assertIds();
    }

   @Test
    public void errorEmptyData(){
        String raw = "!>sdaf";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(2, raw,    0);
        SpanBranch field = doc.assertChild(1, "sdaf", 0, 1);

        assertCite(cite, InfoFieldType.ERROR, null, 0);
        InfoDebug.assertField(field, InfoFieldType.ERROR);

        doc.assertKeyLeaf(  0,  2, "!>",   0, 0);
        doc.assertFieldLeaf(2,  6, "sdaf", 0, 1, 0);

        doc.assertIds();
    }

    @Test
    public void errorEmptyDataNewLine(){
        String raw = "!>sdaf\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(3, raw,    0);
        SpanBranch field = doc.assertChild(1, "sdaf", 0, 1);

        assertCite(cite, InfoFieldType.ERROR, null, 0);
        InfoDebug.assertField(field, InfoFieldType.ERROR);

        doc.assertKeyLeaf(  0, 2, "!>",   0, 0);
        doc.assertFieldLeaf(2, 6, "sdaf", 0, 1, 0);
        doc.assertKeyLeaf(  6, 7, "\n",   0, 2);

        doc.assertIds();
    }

    @Test
    public void errorEmptyDataWithColon(){
        String raw = "!>sdaf:\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(4, raw,    0);
        SpanBranch field = doc.assertChild(1, "sdaf", 0, 1);

        assertCite(cite, InfoFieldType.ERROR, null, 0);
        InfoDebug.assertField(field, InfoFieldType.ERROR);

        doc.assertKeyLeaf(  0,  2, "!>",   0, 0);
        doc.assertFieldLeaf(2,  6, "sdaf", 0, 1, 0);
        doc.assertKeyLeaf(  6,  7, ":",    0, 2);
        doc.assertKeyLeaf(  7,  8, "\n",   0, 3);

        doc.assertIds();
    }

    @Test
    public void errorEmptyDataWithExraSpaces(){
        String raw = "!>saf:  \n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(5, raw, 0);
        SpanBranch field = doc.assertChild(1, "saf",        0, 1);

        assertCite(cite, InfoFieldType.ERROR, null, 0);
        InfoDebug.assertField(field, InfoFieldType.ERROR);

        doc.assertKeyLeaf(  0, 2, "!>",  0, 0);
        doc.assertFieldLeaf(2, 5, "saf", 0, 1, 0);
        doc.assertKeyLeaf(  5, 6, ":",   0, 2);
        doc.assertTextLeaf( 6, 8, "  ",  0, 3);
        doc.assertKeyLeaf(  8, 9, "\n",  0, 4);

        doc.assertIds();
    }

    @Test
    public void errorUsingError(){
        String raw = "!>error:text\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(5, raw,     0);
        SpanBranch field = doc.assertChild(1, "error", 0, 1);

        assertCite(cite, InfoFieldType.ERROR, null, 0);
        InfoDebug.assertField(field, InfoFieldType.ERROR);

        doc.assertKeyLeaf(  0,  2, "!>",    0, 0);
        doc.assertFieldLeaf(2,  7, "error", 0, 1, 0);
        doc.assertKeyLeaf(  7,  8, ":",     0, 2);
        doc.assertTextLeaf( 8, 12, "text",  0, 3);
        doc.assertKeyLeaf( 12, 13, "\n",    0, 4);

        doc.assertIds();
    }

    @Test
    public void footnote(){
        String raw = "!>footnote:abc\\\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(4, raw,        0);
        SpanBranch field = doc.assertChild(1, "footnote", 0, 1);
        SpanBranch data  = doc.assertChild(1, "abc\\\n",  0, 3);
        SpanBranch text  = doc.assertChild(2, "abc\\\n",  0, 3, 0);

        assertCite(cite, InfoFieldType.FOOTNOTE, data, 1);
        InfoDebug.assertField(field, InfoFieldType.FOOTNOTE);
        InfoDebug.assertDataText(data, text);

        doc.assertKeyLeaf(  0,  2, "!>",       0, 0);
        doc.assertFieldLeaf(2, 10, "footnote", 0, 1, 0);
        doc.assertKeyLeaf( 10, 11, ":",        0, 2);
        doc.assertDataLeaf(11, 14, "abc",      0, 3, 0, 0);
        doc.assertKeyLeaf( 14, 15, "\\",       0, 3, 0, 1, 0);
        doc.assertDataLeaf(15, 16, "\n",       0, 3, 0, 1, 1);

        doc.assertIds();
    }

    /*
    @Test
    public void pagesBasic(){
        String raw = "!>pages:5";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(4, raw,     0);
        SpanBranch field = doc.assertChild(1, "pages", 0, 1);
        SpanBranch data  = doc.assertChild(1, "5",     0, 3);

        assertCite(cite, InfoFieldType.PAGES, data, 0);
        InfoDebug.assertField(field, InfoFieldType.PAGES);
        InfoDebug.assertDataNumber(data, 5);

        doc.assertKeyLeaf(  0, 2, "!>",    0, 0);
        doc.assertFieldLeaf(2, 7, "pages", 0, 1, 0);
        doc.assertKeyLeaf(  7, 8, ":",     0, 2);
        doc.assertDataLeaf( 8, 9, "5",     0, 3, 0);

        doc.assertIds();
    }

    @Test
    public void pagesTrimLeft(){
        String raw = "!>pages:5    ";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(4, raw,     0);
        SpanBranch field = doc.assertChild(1, "pages", 0, 1);
        SpanBranch data  = doc.assertChild(1, "5    ", 0, 3);

        assertCite(cite, InfoFieldType.PAGES, data, 0);
        InfoDebug.assertField(field, InfoFieldType.PAGES);
        InfoDebug.assertDataNumber(data, 5);

        doc.assertKeyLeaf(  0,  2, "!>",    0, 0);
        doc.assertFieldLeaf(2,  7, "pages", 0, 1, 0);
        doc.assertKeyLeaf(  7,  8, ":",     0, 2);
        doc.assertDataLeaf( 8, 13, "5    ", 0, 3, 0);

        doc.assertIds();
    }

    @Test
    public void pagesFull(){
        String raw = "!>pages:    6     \n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(5, raw,          0);
        SpanBranch field = doc.assertChild(1, "pages",      0, 1);
        SpanBranch data  = doc.assertChild(1, "    6     ", 0, 3);

        assertCite(cite, InfoFieldType.PAGES, data, 0);
        InfoDebug.assertField(field, InfoFieldType.PAGES);
        InfoDebug.assertDataNumber(data, 6);

        doc.assertKeyLeaf(  0,  2, "!>",         0, 0);
        doc.assertFieldLeaf(2,  7, "pages",      0, 1, 0);
        doc.assertKeyLeaf(  7,  8, ":",          0, 2);
        doc.assertDataLeaf( 8, 18, "    6     ", 0, 3, 0);
        doc.assertKeyLeaf( 18, 19, "\n",         0, 4);

        doc.assertIds();
    }

    @Test
    public void pagesTextInput(){
        String raw = "!>pages:abc\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(5, raw,          0);
        SpanBranch field = doc.assertChild(1, "pages",      0, 1);

        assertCite(cite, InfoFieldType.PAGES, null, 0);
        InfoDebug.assertField(field, InfoFieldType.PAGES);

        doc.assertKeyLeaf(  0,  2, "!>",    0, 0);
        doc.assertFieldLeaf(2,  7, "pages", 0, 1, 0);
        doc.assertKeyLeaf(  7,  8, ":",     0, 2);
        doc.assertTextLeaf( 8, 11, "abc",   0, 3);
        doc.assertKeyLeaf( 11, 12, "\n",    0, 4);

        doc.assertIds();
    }

    @Test
    public void pagesTextAfter(){
        String raw = "!>pages:22 reads\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(5, raw,          0);
        SpanBranch field = doc.assertChild(1, "pages",      0, 1);

        assertCite(cite, InfoFieldType.PAGES, null, 0);
        InfoDebug.assertField(field, InfoFieldType.PAGES);

        doc.assertKeyLeaf(  0,  2, "!>",    0, 0);
        doc.assertFieldLeaf(2,  7, "pages", 0, 1, 0);
        doc.assertKeyLeaf(  7,  8, ":",     0, 2);
        doc.assertTextLeaf( 8, 16, "22 reads",   0, 3);
        doc.assertKeyLeaf( 16, 17, "\n",    0, 4);

        doc.assertIds();
    }

    @Test
    public void pagesNoData(){
        String raw = "!>pages:";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(3, raw,          0);
        SpanBranch field = doc.assertChild(1, "pages",      0, 1);

        assertCite(cite, InfoFieldType.PAGES, null, 0);
        InfoDebug.assertField(field, InfoFieldType.PAGES);

        doc.assertKeyLeaf(  0, 2, "!>",    0, 0);
        doc.assertFieldLeaf(2, 7, "pages", 0, 1, 0);
        doc.assertKeyLeaf(  7, 8, ":",     0, 2);

        doc.assertIds();
    }

    @Test
    public void pagesDatalessNewLine(){
        String raw = "!>pages:\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(4, raw,          0);
        SpanBranch field = doc.assertChild(1, "pages",      0, 1);

        assertCite(cite, InfoFieldType.PAGES, null, 0);
        InfoDebug.assertField(field, InfoFieldType.PAGES);

        doc.assertKeyLeaf(  0, 2, "!>",    0, 0);
        doc.assertFieldLeaf(2, 7, "pages", 0, 1, 0);
        doc.assertKeyLeaf(  7, 8, ":",     0, 2);
        doc.assertKeyLeaf(  8, 9, "\n",    0, 3);

        doc.assertIds();
    }

    @Test
    public void pagesSpaceDataNewLine(){
        String raw = "!>pages:    \n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(5, raw,          0);
        SpanBranch field = doc.assertChild(1, "pages",      0, 1);

        assertCite(cite, InfoFieldType.PAGES, null, 0);
        InfoDebug.assertField(field, InfoFieldType.PAGES);

        doc.assertKeyLeaf(  0,  2, "!>",    0, 0);
        doc.assertFieldLeaf(2,  7, "pages", 0, 1, 0);
        doc.assertKeyLeaf(  7,  8, ":",     0, 2);
        doc.assertTextLeaf( 8, 12, "    ",  0, 3);
        doc.assertKeyLeaf( 12, 13, "\n",    0, 4);

        doc.assertIds();
    }*/


    @Test
    public void sources(){
        String raw = "!>source:Henry** Reads**\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch cite  = doc.assertChild(5, raw,               0);
        SpanBranch field = doc.assertChild(1, "source",          0, 1);
        SpanBranch data  = doc.assertChild(1, "Henry** Reads**", 0, 3);
        SpanBranch text  = doc.assertChild(4, "Henry** Reads**", 0, 3, 0);

        assertCite(cite, InfoFieldType.SOURCE, data, 2);
        InfoDebug.assertField(field, InfoFieldType.SOURCE);
        InfoDebug.assertDataFormatted(data, text);

        doc.assertKeyLeaf(  0,  2, "!>",     0, 0);
        doc.assertFieldLeaf(2,  8, "source", 0, 1, 0);
        doc.assertKeyLeaf(  8,  9, ":",      0, 2);
        doc.assertDataLeaf( 9, 14, "Henry",  0, 3, 0, 0, 0);
        doc.assertKeyLeaf( 14, 16, "**",     0, 3, 0, 1);
        doc.assertDataLeaf(16, 22, " Reads", 0, 3, 0, 2, 0);
        doc.assertKeyLeaf( 22, 24, "**",     0, 3, 0, 3);
        doc.assertKeyLeaf( 24, 25, "\n",     0, 4);

        doc.assertIds();
    }
}
