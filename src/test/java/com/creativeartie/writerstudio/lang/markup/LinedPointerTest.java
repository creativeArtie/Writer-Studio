package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchFormatAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchLineAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
public class LinedPointerTest{

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

        PointerLinkAssert link = new PointerLinkAssert(doc)
            .setPath("www.reddit.com").setLookup("<@reddit>")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryAssert idSpan = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(id).setLookup("reddit");;
        ContentAssert content = new ContentAssert(doc)
            .setBoth("www.reddit.com").setBegin(false)
            .setEnd(false).setCount(1);

        link.test(5,          raw,              0);
        doc.assertKey( 0,  2, "!@",             0, 0);
        idSpan.test(1,        "reddit",         0, 1);
        doc.assertChild(1,    "reddit",         0, 1, 0);
        doc.assertId(  2,  8, "reddit",         0, 1, 0, 0);
        doc.assertKey( 8,  9, ":",              0, 2);
        content.test(1,       "www.reddit.com", 0, 3);
        doc.assertPath(9, 23, "www.reddit.com", 0, 3, 0);
        doc.assertKey(23, 24, "\n",             0, 4);
        doc.assertRest();
    }

    @Test
    public void linkNoEnd(){
        String raw = "!@reddit:www.reddit.com";
        DocumentAssert doc = assertDoc(1, raw, parsers);
        commonLink(doc);
    }

    private void commonLink(DocumentAssert doc){
        String raw = "!@reddit:www.reddit.com";
        IDBuilder id = doc.addId(buildLinkId("reddit"), 0);

        PointerLinkAssert link = new PointerLinkAssert(doc)
            .setPath("www.reddit.com").setLookup("<@reddit>")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryAssert idSpan = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(id).setLookup("reddit");
        ContentAssert content = new ContentAssert(doc)
            .setBoth("www.reddit.com").setBegin(false)
            .setEnd(false).setCount(1);


        link.test(4,          raw,              0);
        doc.assertKey( 0,  2, "!@",             0, 0);
        idSpan.test(1,        "reddit",         0, 1);
        doc.assertChild(1,    "reddit",         0, 1, 0);
        doc.assertId(  2,  8, "reddit",         0, 1, 0, 0);
        doc.assertKey( 8,  9, ":",              0, 2);
        content.test(1,       "www.reddit.com", 0, 3);
        doc.assertPath(9, 23, "www.reddit.com", 0, 3, 0);
        doc.assertRest();
    }

    @Test
    public void linkPathLess(){
        String raw = "!@reddit:";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildLinkId("reddit");
        doc.addId(id, 0);

        PointerLinkAssert link = new PointerLinkAssert(doc)
            .setPath("").setLookup("<@reddit>")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryAssert idSpan = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(id).setLookup("reddit");;

        link.test(3,          raw,      0);
        doc.assertKey( 0,  2, "!@",     0, 0);
        idSpan.test(1,        "reddit", 0, 1);
        doc.assertChild(1,    "reddit", 0, 1, 0);
        doc.assertId(  2,  8, "reddit", 0, 1, 0, 0);
        doc.assertKey( 8,  9, ":",      0, 2);
        doc.assertRest();
    }

    @Test
    public void linkColonLess(){
        String raw = "!@reddit";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildLinkId("reddit");
        doc.addId(id, 0);

        PointerLinkAssert link = new PointerLinkAssert(doc)
            .setPath("").setLookup("<@reddit>")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryAssert idSpan = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.LINK)
            .setIdentity(id).setLookup("reddit");   ;

        link.test(2,          raw,      0);
        doc.assertKey( 0,  2, "!@",     0, 0);
        idSpan.test(1,        "reddit", 0, 1);
        doc.assertChild(1,    "reddit", 0, 1, 0);
        doc.assertId(  2,  8, "reddit", 0, 1, 0, 0);
        doc.assertRest();
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
        commonLink(doc);
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
        commonLink(doc);
    }

    @Test
    public void linkNoId(){
        String raw = "!@:reddit";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        PointerLinkAssert link = new PointerLinkAssert(doc)
            .setPath("reddit");

        link.test(3,         raw,      0);
        doc.assertKey( 0, 2, "!@",     0, 0);
        doc.assertKey (2, 3, ":",      0, 1);
        doc.assertChild(1,   "reddit", 0, 2);
        doc.assertPath(3, 9, "reddit", 0, 2, 0);
        doc.assertRest();
    }

    @Test
    public void endnoteFull(){
        ///           01234567890123456 78
        String raw = "!*abcdef:**test**\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildEndnoteId("abcdef");
        doc.addId(id, 0);

        PointerNoteAssert note = new PointerNoteAssert(doc)
            .setLinedType(LinedType.ENDNOTE)
            .setFormattedSpan(0, 3).setLookup("{*abcdef}")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryAssert idSpan = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(id).setLookup("abcdef");
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("test");

        note.test(5,           raw,      0);
        doc.assertKey(  0,  2, "!*",     0, 0);
        idSpan.test(1,         "abcdef", 0, 1);
        doc.assertChild(1,     "abcdef", 0, 1, 0);
        doc.assertId(   2,  8, "abcdef", 0, 1, 0, 0);
        doc.assertKey(  8,  9, ":",      0, 2);
        main.test(3,           "**test**", 0, 3);
        doc.assertKey(  9, 11, "**",     0, 3, 0);
        doc.assertText(11, 15, "test",   0, 3, 1, 0);
        doc.assertKey( 15, 17, "**",     0, 3, 2);
        doc.assertKey( 17, 18, "\n",     0, 4);
        doc.assertRest();
    }

    @Test
    public void endnoteTextLess(){
        String raw = "!*abcdef:";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildEndnoteId("abcdef");
        doc.addId(id, 0);

        PointerNoteAssert note = new PointerNoteAssert(doc)
            .setLinedType(LinedType.ENDNOTE).setLookup("{*abcdef}")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryAssert idSpan = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(id).setLookup("abcdef");

        note.test(3,        raw,      0);
        doc.assertKey(0, 2, "!*",     0, 0);
        idSpan.test(1,      "abcdef", 0, 1);
        doc.assertChild(1,  "abcdef", 0, 1, 0);
        doc.assertId( 2, 8, "abcdef", 0, 1, 0, 0);
        doc.assertKey(8, 9, ":",      0, 2);
        doc.assertRest();
    }

    @Test
    public void endnoteColonLess(){
        String raw = "!*abcdef";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildEndnoteId("abcdef");
        doc.addId(id, 0);

        PointerNoteAssert note = new PointerNoteAssert(doc)
            .setLinedType(LinedType.ENDNOTE).setLookup("{*abcdef}")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryAssert idSpan = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.ENDNOTE)
            .setIdentity(id).setLookup("abcdef");

        note.test(2,        raw,      0);
        doc.assertKey(0, 2, "!*",     0, 0);
        idSpan.test(1,      "abcdef", 0, 1);
        doc.assertChild(1,  "abcdef", 0, 1, 0);
        doc.assertId( 2, 8, "abcdef", 0, 1, 0, 0);
        doc.assertRest();
    }

    @Test
    public void endnoteIDLess(){
        String raw = "!*:test";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        PointerNoteAssert note = new PointerNoteAssert(doc)
            .setLinedType(LinedType.ENDNOTE)
            .setFormattedSpan(0, 2);
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("test");

        note.test(3,         raw,    0);
        doc.assertKey( 0, 2, "!*",   0, 0);
        doc.assertKey( 2, 3, ":",    0, 1);
        main.test(1,         "test", 0, 2);
        doc.assertChild(1,   "test", 0, 2, 0);
        doc.assertText(3, 7, "test", 0, 2, 0, 0);
        doc.assertRest();
    }

    @Test
    public void footnote(){
        String raw = "!^abcdef:**test**\n";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildFootnoteId("abcdef");
        doc.addId(id, 0);

        PointerNoteAssert note = new PointerNoteAssert(doc)
            .setLinedType(LinedType.FOOTNOTE)
            .setFormattedSpan(0, 3).setLookup("{^abcdef}")
            .setCatalogued(CatalogueStatus.UNUSED, id);
        DirectoryAssert idSpan = new DirectoryAssert(doc)
            .setPurpose(DirectoryType.FOOTNOTE)
            .setIdentity(id).setLookup("abcdef");
        FormattedSpanAssert main = new FormattedSpanAssert(doc)
            .setPublish(1).setNote(0).setParsed("test");

        note.test(5,           raw,      0);
        doc.assertKey(  0,  2, "!^",     0, 0);
        idSpan.test(1,         "abcdef", 0, 1);
        doc.assertChild(1,     "abcdef", 0, 1);
        doc.assertId(   2,  8, "abcdef", 0, 1, 0, 0);
        doc.assertKey(  8,  9, ":",      0, 2);
        main.test(3,           "**test**", 0, 3);
        doc.assertKey(  9, 11, "**",     0, 3, 0);
        doc.assertText(11, 15, "test",   0, 3, 1, 0);
        doc.assertKey( 15, 17, "**",     0, 3, 2);
        doc.assertKey( 17, 18, "\n",     0, 4);
        doc.assertRest();
    }
}
