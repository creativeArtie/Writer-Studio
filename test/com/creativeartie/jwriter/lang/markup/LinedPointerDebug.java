package com.creativeartie.jwriter.lang.markup;


import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;

public class LinedPointerDebug{

    public static IDBuilder buildLinkId(String name){
        return new IDBuilder().addCategory("link").setId(name);
    }

    public static IDBuilder buildFootnoteId(String name){
        return new IDBuilder().addCategory("foot").setId(name);
    }

    public static IDBuilder buildEndnoteId(String name){
        return new IDBuilder().addCategory("end").setId(name);
    }

    private final SetupParser[] parsers = LinedParsePointer.values();

    public static final void assertLink(SpanBranch span, String path, CatalogueStatus status, IDBuilder id)
    {
        LinedSpanPointLink link = assertClass(span, LinedSpanPointLink.class);

        DetailStyle[] styles = new DetailStyle[]{LinedType.HYPERLINK, status};

        assertEquals(getError("path", link), path, link.getPath());
        assertSpanIdentity(link, id);
        LinedRestDebug.assertLine(link, 0, 0);
        assertBranch(link, styles, status);
    }

    public static final void assertNote(SpanBranch span, LinedType lined, Span formatted, CatalogueStatus status, IDBuilder id)
    {
        LinedSpanPointNote note = assertClass(span, LinedSpanPointNote.class);

        DetailStyle[] styles = new DetailStyle[]{lined, status};

        assertSpan("formated", formatted, note.getFormattedSpan());
        assertSpanIdentity(note, id);
        LinedRestDebug.assertLine(note, 0, 0);
        assertBranch(note, styles, status);
    }

    @Test
    public void linkFull(){
        String raw = "!@reddit:www.reddit.com\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch link    = doc.assertChild(5, raw,              0);
        SpanBranch idSpan  = doc.assertChild(1, "reddit",         0, 1);
        SpanBranch content = doc.assertChild(1, "www.reddit.com", 0, 3);

        IDBuilder id = buildLinkId("reddit");

        assertLink(link, "www.reddit.com", CatalogueStatus.UNUSED, id);
        DirectoryDebug.assertId(idSpan, DirectoryType.LINK, id);
        new BranchTest.ContentTest().setText("www.reddit.com").setBegin(false)
            .setEnd(false).setCount(1).test(doc, 1, "www.reddit.com", 0, 3);


        doc.assertKeyLeaf( 0,  2, "!@",             0, 0);
        doc.assertIdLeaf(  2,  8, "reddit",         0, 1, 0, 0);
        doc.assertKeyLeaf( 8,  9, ":",              0, 2);
        doc.assertPathLeaf(9, 23, "www.reddit.com", 0, 3, 0);
        doc.assertKeyLeaf(23, 24, "\n",             0, 4);
    }
    @Test
    public void linkNoEnd(){
        String raw = "!@reddit:www.reddit.com";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch link    = doc.assertChild(4, raw,              0);
        SpanBranch idSpan  = doc.assertChild(1, "reddit",         0, 1);
        SpanBranch content = doc.assertChild(1, "www.reddit.com", 0, 3);

        IDBuilder id = buildLinkId("reddit");
        doc.addId(id, 0);

        assertLink(link, "www.reddit.com", CatalogueStatus.UNUSED, id);
        DirectoryDebug.assertId(idSpan, DirectoryType.LINK, id);
        new BranchTest.ContentTest().setText("www.reddit.com").setBegin(false)
            .setEnd(false).setCount(1).test(doc, 1, "www.reddit.com", 0, 3);


        doc.assertKeyLeaf( 0,  2, "!@",             0, 0);
        doc.assertIdLeaf(  2,  8, "reddit",         0, 1, 0, 0);
        doc.assertKeyLeaf( 8,  9, ":",              0, 2);
        doc.assertPathLeaf(9, 23, "www.reddit.com", 0, 3, 0);
        doc.assertIds();
    }

    @Test
    public void linkPathLess(){
        String raw = "!@reddit:";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch link    = doc.assertChild(3, raw,      0);
        SpanBranch idSpan  = doc.assertChild(1, "reddit", 0, 1);

        IDBuilder id = buildLinkId("reddit");
        doc.addId(id, 0);

        assertLink(link, "", CatalogueStatus.UNUSED, id);
        DirectoryDebug.assertId(idSpan, DirectoryType.LINK, id);

        doc.assertKeyLeaf( 0,  2, "!@",     0, 0);
        doc.assertIdLeaf(  2,  8, "reddit", 0, 1, 0, 0);
        doc.assertKeyLeaf( 8,  9, ":",      0, 2);
        doc.assertIds();
    }

    @Test
    public void linkColonLess(){
        String raw = "!@reddit";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch link    = doc.assertChild(2, raw,      0);
        SpanBranch idSpan  = doc.assertChild(1, "reddit", 0, 1);

        IDBuilder id = buildLinkId("reddit");
        doc.addId(id, 0);

        assertLink(link, "", CatalogueStatus.UNUSED, id);
        DirectoryDebug.assertId(idSpan, DirectoryType.LINK, id);

        doc.assertKeyLeaf( 0,  2, "!@",             0, 0);
        doc.assertIdLeaf(  2,  8, "reddit",         0, 1, 0, 0);
        doc.assertIds();
    }


    @Test
    public void linkNoId(){
        String raw = "!@:reddit";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch link    = doc.assertChild(3, raw,      0);
        SpanBranch content = doc.assertChild(1, "reddit", 0, 2);

        assertLink(link, "reddit", CatalogueStatus.NO_ID, null);

        doc.assertKeyLeaf( 0, 2, "!@",      0, 0);
        doc.assertKeyLeaf (2, 3, ":",       0, 1);
        doc.assertPathLeaf(3, 9, "reddit",  0, 2, 0);

        doc.assertIds();
    }

    @Test
    public void endnoteFull(){
        String raw = "!*abcdef:**test**\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch note    = doc.assertChild(5, raw,        0);
        SpanBranch idSpan  = doc.assertChild(1, "abcdef",   0, 1);
        SpanBranch content = doc.assertChild(3, "**test**", 0, 3);

        IDBuilder id = buildEndnoteId("abcdef");
        doc.addId(id, 0);

        assertNote(note, LinedType.ENDNOTE, content,
            CatalogueStatus.UNUSED, id);
        DirectoryDebug.assertId(idSpan, DirectoryType.ENDNOTE, id);
        FormatSpanDebug.assertMain(content, 1, 0);

        doc.assertKeyLeaf(  0,  2, "!*",     0, 0);
        doc.assertIdLeaf(   2,  8, "abcdef", 0, 1, 0, 0);
        doc.assertKeyLeaf(  8,  9, ":",      0, 2);
        doc.assertKeyLeaf(  9, 11, "**",     0, 3, 0);
        doc.assertTextLeaf(11, 15, "test",   0, 3, 1, 0);
        doc.assertKeyLeaf( 15, 17, "**",     0, 3, 2);
        doc.assertKeyLeaf( 17, 18, "\n",     0, 4);
        doc.assertIds();
    }

    @Test
    public void endnoteTextLess(){
        String raw = "!*abcdef:";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch note    = doc.assertChild(3, raw,        0);
        SpanBranch idSpan  = doc.assertChild(1, "abcdef",   0, 1);

        IDBuilder id = buildEndnoteId("abcdef");
        doc.addId(id, 0);

        assertNote(note, LinedType.ENDNOTE, null, CatalogueStatus.UNUSED, id);
        DirectoryDebug.assertId(idSpan, DirectoryType.ENDNOTE, id);

        doc.assertKeyLeaf(0,  2, "!*",     0, 0);
        doc.assertIdLeaf( 2,  8, "abcdef", 0, 1, 0, 0);
        doc.assertKeyLeaf(8,  9, ":",      0, 2);
        doc.assertIds();
    }

    @Test
    public void endnoteColonLess(){
        String raw = "!*abcdef";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch note    = doc.assertChild(2, raw,        0);
        SpanBranch idSpan  = doc.assertChild(1, "abcdef",   0, 1);

        IDBuilder id = buildEndnoteId("abcdef");
        doc.addId(id, 0);

        assertNote(note, LinedType.ENDNOTE, null, CatalogueStatus.UNUSED, id);
        DirectoryDebug.assertId(idSpan, DirectoryType.ENDNOTE, id);

        doc.assertKeyLeaf(0,  2, "!*",     0, 0);
        doc.assertIdLeaf( 2,  8, "abcdef", 0, 1, 0, 0);
        doc.assertIds();
    }

    @Test
    public void endnoteIDlLess(){
        String raw = "!*:test";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch note    = doc.assertChild(3, raw,    0);
        SpanBranch content = doc.assertChild(1, "test", 0, 2);

        assertNote(note, LinedType.ENDNOTE, content,
            CatalogueStatus.NO_ID, null);
        FormatSpanDebug.assertMain(content, 1, 0);

        doc.assertKeyLeaf( 0, 2, "!*",   0, 0);
        doc.assertKeyLeaf( 2, 3, ":",    0, 1);
        doc.assertTextLeaf(3, 7, "test", 0, 2, 0, 0);

        doc.assertIds();
    }

    @Test
    public void footnote(){
        String raw = "!^abcdef:**test**\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        SpanBranch note    = doc.assertChild(5, raw,        0);
        SpanBranch idSpan  = doc.assertChild(1, "abcdef",   0, 1);
        SpanBranch content = doc.assertChild(3, "**test**", 0, 3);

        IDBuilder id = buildFootnoteId("abcdef");
        doc.addId(id, 0);

        assertNote(note, LinedType.FOOTNOTE, content,
            CatalogueStatus.UNUSED, id);
        DirectoryDebug.assertId(idSpan, DirectoryType.FOOTNOTE, id);
        FormatSpanDebug.assertMain(content, 1, 0);

        doc.assertKeyLeaf(  0,  2, "!^",     0, 0);
        doc.assertIdLeaf(   2,  8, "abcdef", 0, 1, 0, 0);
        doc.assertKeyLeaf(  8,  9, ":",      0, 2);
        doc.assertKeyLeaf(  9, 11, "**",     0, 3, 0);
        doc.assertTextLeaf(11, 15, "test",   0, 3, 1, 0);
        doc.assertKeyLeaf( 15, 17, "**",     0, 3, 2);
        doc.assertKeyLeaf( 17, 18, "\n",     0, 4);
        doc.assertIds();
    }
}
