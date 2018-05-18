package com.creativeartie.writerstudio.lang.markup;


import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
import static com.creativeartie.writerstudio.lang.markup.BranchLineTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchFormatTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchTest.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;

public class LinedPointerDebug{

    public static IDBuilder buildLinkId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_LINK).setId(name);
    }

    public static IDBuilder buildFootnoteId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_FOOTNOTE).setId(name);
    }

    public static IDBuilder buildEndnoteId(String name){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_ENDNOTE).setId(name);
    }

    private final SetupParser[] parsers = LinedParsePointer.values();

    @Test
    public void linkFull(){
        String raw = "!@reddit:www.reddit.com\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = doc.addId(buildLinkId("reddit"), 0);

        PointerLinkTest link = new PointerLinkTest()
            .setPath("www.reddit.com")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryTest idSpan = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(id);
        ContentTest content = new ContentTest()
            .setText("www.reddit.com").setBegin(false)
            .setEnd(false).setCount(1);

        link.test(       doc,  5, raw,              0);
        doc.assertKeyLeaf( 0,  2, "!@",             0, 0);
        idSpan.test(     doc,  1, "reddit",         0, 1);
        doc.assertChild(       1, "reddit",         0, 1, 0);
        doc.assertIdLeaf(  2,  8, "reddit",         0, 1, 0, 0);
        doc.assertKeyLeaf( 8,  9, ":",              0, 2);
        content.test(    doc,  1, "www.reddit.com", 0, 3);
        doc.assertPathLeaf(9, 23, "www.reddit.com", 0, 3, 0);
        doc.assertKeyLeaf(23, 24, "\n",             0, 4);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void linkNoEnd(){
        String raw = "!@reddit:www.reddit.com";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        editCommonLink(doc);
    }

    private void editCommonLink(DocumentAssert doc){
        String raw = "!@reddit:www.reddit.com";
        IDBuilder id = doc.addId(buildLinkId("reddit"), 0);

        PointerLinkTest link = new PointerLinkTest()
            .setPath("www.reddit.com")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryTest idSpan = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(id);
        ContentTest content = new ContentTest()
            .setText("www.reddit.com").setBegin(false)
            .setEnd(false).setCount(1);


        link.test(       doc,  4, raw,              0);
        doc.assertKeyLeaf( 0,  2, "!@",             0, 0);
        idSpan.test(     doc,  1, "reddit",         0, 1);
        doc.assertChild(       1, "reddit",         0, 1, 0);
        doc.assertIdLeaf(  2,  8, "reddit",         0, 1, 0, 0);
        doc.assertKeyLeaf( 8,  9, ":",              0, 2);
        content.test(    doc,  1, "www.reddit.com", 0, 3);
        doc.assertPathLeaf(9, 23, "www.reddit.com", 0, 3, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void linkPathLess(){
        String raw = "!@reddit:";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildLinkId("reddit");
        doc.addId(id, 0);

        PointerLinkTest link = new PointerLinkTest()
            .setPath("")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryTest idSpan = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(id);

        link.test(       doc,  3, raw,      0);
        doc.assertKeyLeaf( 0,  2, "!@",     0, 0);
        idSpan.test(     doc,  1, "reddit", 0, 1);
        doc.assertChild(       1, "reddit", 0, 1, 0);
        doc.assertIdLeaf(  2,  8, "reddit", 0, 1, 0, 0);
        doc.assertKeyLeaf( 8,  9, ":",      0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void linkColonLess(){
        String raw = "!@reddit";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildLinkId("reddit");
        doc.addId(id, 0);

        PointerLinkTest link = new PointerLinkTest()
            .setPath("")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryTest idSpan = new DirectoryTest()
            .setPurpose(DirectoryType.LINK)
            .setIdentity(id);

        link.test(       doc,  2, raw,      0);
        doc.assertKeyLeaf( 0,  2, "!@",     0, 0);
        idSpan.test(     doc,  1, "reddit", 0, 1);
        doc.assertChild(       1, "reddit", 0, 1, 0);
        doc.assertIdLeaf(  2,  8, "reddit", 0, 1, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void editLinkPath(){
        ///              01234567890123456
        String before = "!@reddit:www.com";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(12, ".reddit", 0);
        ///             01234567890123456 78
        String after = "!*abcdef:**test**\n";
        doc.assertDoc(1, after, parsers);
        editCommonLink(doc);
    }

    @Test
    public void editLinkId(){
        ///              0123456789012345678901
        String before = "!@reit:www.reddit.com";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(4, "dd", 0, 1);
        ///             01234567890123456 78
        String after = "!*abcdef:**test**\n";
        doc.assertDoc(1, after, parsers);
        editCommonLink(doc);
    }

    @Test
    public void linkNoId(){
        String raw = "!@:reddit";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        PointerLinkTest link = new PointerLinkTest()
            .setPath("reddit");

        link.test(       doc, 3, raw,      0);
        doc.assertKeyLeaf( 0, 2, "!@",     0, 0);
        doc.assertKeyLeaf (2, 3, ":",      0, 1);
        doc.assertChild(      1, "reddit", 0, 2);
        doc.assertPathLeaf(3, 9, "reddit", 0, 2, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void endnoteFull(){
        ///           01234567890123456 78
        String raw = "!*abcdef:**test**\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildEndnoteId("abcdef");
        doc.addId(id, 0);

        PointerNoteTest note = new PointerNoteTest()
            .setLinedType(LinedType.ENDNOTE)
            .setFormattedSpan(doc, 0, 3)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryTest idSpan = new DirectoryTest()
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(id);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(1).setNoteTotal(0);

        note.test(       doc,   5, raw,      0);
        doc.assertKeyLeaf(  0,  2, "!*",     0, 0);
        idSpan.test(     doc,   1, "abcdef", 0, 1);
        doc.assertChild(        1, "abcdef", 0, 1, 0);
        doc.assertIdLeaf(   2,  8, "abcdef", 0, 1, 0, 0);
        doc.assertKeyLeaf(  8,  9, ":",      0, 2);
        main.test(       doc, 3, "**test**", 0, 3);
        doc.assertKeyLeaf(  9, 11, "**",     0, 3, 0);
        doc.assertTextLeaf(11, 15, "test",   0, 3, 1, 0);
        doc.assertKeyLeaf( 15, 17, "**",     0, 3, 2);
        doc.assertKeyLeaf( 17, 18, "\n",     0, 4);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void endnoteTextLess(){
        String raw = "!*abcdef:";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildEndnoteId("abcdef");
        doc.addId(id, 0);

        PointerNoteTest note = new PointerNoteTest()
            .setLinedType(LinedType.ENDNOTE)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryTest idSpan = new DirectoryTest()
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(id);

        note.test(      doc, 3, raw,      0);
        doc.assertKeyLeaf(0, 2, "!*",     0, 0);
        idSpan.test(    doc, 1, "abcdef", 0, 1);
        doc.assertChild(     1, "abcdef", 0, 1, 0);
        doc.assertIdLeaf( 2, 8, "abcdef", 0, 1, 0, 0);
        doc.assertKeyLeaf(8, 9, ":",      0, 2);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void endnoteColonLess(){
        String raw = "!*abcdef";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildEndnoteId("abcdef");
        doc.addId(id, 0);

        PointerNoteTest note = new PointerNoteTest()
            .setLinedType(LinedType.ENDNOTE)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryTest idSpan = new DirectoryTest()
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(id);

        note.test(      doc, 2, raw,      0);
        doc.assertKeyLeaf(0, 2, "!*",     0, 0);
        idSpan.test(    doc, 1, "abcdef", 0, 1);
        doc.assertChild(     1, "abcdef", 0, 1, 0);
        doc.assertIdLeaf( 2, 8, "abcdef", 0, 1, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void endnoteIDlLess(){
        String raw = "!*:test";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        PointerNoteTest note = new PointerNoteTest()
            .setLinedType(LinedType.ENDNOTE)
            .setFormattedSpan(doc, 0, 2);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(1).setNoteTotal(0);

        note.test(       doc, 3, raw,    0);
        doc.assertKeyLeaf( 0, 2, "!*",   0, 0);
        doc.assertKeyLeaf( 2, 3, ":",    0, 1);
        main.test(       doc, 1, "test", 0, 2);
        doc.assertChild(      1, "test", 0, 2, 0);
        doc.assertTextLeaf(3, 7, "test", 0, 2, 0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void footnote(){
        String raw = "!^abcdef:**test**\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildFootnoteId("abcdef");
        doc.addId(id, 0);

        PointerNoteTest note = new PointerNoteTest()
            .setLinedType(LinedType.FOOTNOTE)
            .setFormattedSpan(doc, 0, 3)
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryTest idSpan = new DirectoryTest()
            .setPurpose(DirectoryType.FOOTNOTE)
            .setIdentity(id);
        FormattedSpanTest main = new FormattedSpanTest()
            .setPublishTotal(1).setNoteTotal(0);

        note.test(       doc,   5, raw,      0);
        doc.assertKeyLeaf(  0,  2, "!^",     0, 0);
        idSpan.test(      doc,  1, "abcdef", 0, 1);
        doc.assertChild(        1, "abcdef", 0, 1);
        doc.assertIdLeaf(   2,  8, "abcdef", 0, 1, 0, 0);
        doc.assertKeyLeaf(  8,  9, ":",      0, 2);
        main.test(       doc, 3, "**test**", 0, 3);
        doc.assertKeyLeaf(  9, 11, "**",     0, 3, 0);
        doc.assertTextLeaf(11, 15, "test",   0, 3, 1, 0);
        doc.assertKeyLeaf( 15, 17, "**",     0, 3, 2);
        doc.assertKeyLeaf( 17, 18, "\n",     0, 4);
        doc.assertLast();
        doc.assertIds();
    }
}
