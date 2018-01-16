package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;


@RunWith(JUnit4.class)
public class SupplementLinkDebug {

    @Test
    public void linkComplete(){
        String path = "www.google.com";
        String text = "Google";
        String test = "<@a|" + text + ">";
        String raw = test + "\n!@a:" + path + "\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            new WritingText(raw));
        IDBuilder builder = FormatLinkDebug.buildLinkId("a");
        doc.addRef(builder, 0);
        doc.addId(builder);
        new FormatLinkTest().setPath(path).setText(text)
            .setCatalogued(CatalogueStatus.READY, builder)
            ///               section, line, format, link
            .test(doc, 5, test,     0,    0,      0,    0);
    }

    @Test
    public void linkNoText(){
        String path = "www.google.com";
        String test = "<@a>";
        String raw = test + "\n!@a:" + path + "\n";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            new WritingText(raw));
        IDBuilder builder = FormatLinkDebug.buildLinkId("a");
        doc.addRef(builder, 0);
        doc.addId(builder);
        new FormatLinkTest().setPath(path).setText(path)
            .setCatalogued(CatalogueStatus.READY, builder)
            ///               section, line, format, link
            .test(doc, 3, test,     0,    0,      0,    0);
    }

    /// linkNoPath -> see FormatLinkDebug

    @Test
    public void linkConflictID(){
        String path = "www.google.com";
        String text = "Google";
        String test = "<@a|" + text + ">";
        String raw = test + "\n!@a:" + path + "\n@a:apple.com";
        DocumentAssert doc = DocumentAssert.assertDoc(1, raw,
            new WritingText(raw));
        IDBuilder builder = FormatLinkDebug.buildLinkId("a");
        doc.addRef(builder, 0);
        doc.addId(builder);
        new FormatLinkTest().setPath(path).setText("Google")
            .setCatalogued(CatalogueStatus.READY, builder)
            ///               section, line, format, link
            .test(doc, 5, test,     0,    0,      0,    0);
    }


    /*

    @Test
    public void footnoteComplete(){
        SpanExpect paragraph = addLine(curlyHelp(DirectoryType.FOOTNOTE,
            new String[]{"foot"}, "n"), "{^", "n", "}");

        SpanExpect note = new SpanExpect();
        note.addChildren("!^", "n", ":", "text", "\n");

        SpanExpect section = addSection(note, paragraph);
        SpanExpect doc = new SpanExpect();
        doc.addChild(section);
        Document main = doc.testAll(parsers);

        SpanBranch setup = main.get(0); /// Main Section
        setup = (SpanBranch)setup.get(1); /// Paragraph
        setup = (SpanBranch)setup.get(0); /// Format span
        setup = (SpanBranch)setup.get(0); /// SetupPointer span
        Optional<Span> test = ((FormatSpanDirectory)setup).getTarget();

        SpanBranch expect = main.get(0); /// Main Section
        expect = (SpanBranch)expect.get(0); /// footnote
        assertTrue("Span not found.", test.isPresent());
        assertSame("Span are not equal", test.get(), expect);
    }

    @Test
    public void endnoteComplete(){
        SpanExpect note = new SpanExpect();
        note.addChildren("!*", "n", ":", "text", "\n");

        SpanExpect paragraph = addLine(curlyHelp(DirectoryType.ENDNOTE,
            new String[]{"end"}, "n"), "{*", "n", "}");

        SpanExpect section = addSection(paragraph, note);
        SpanExpect doc = new SpanExpect();
        doc.addChild(section);
        Document main = doc.testAll(parsers);

        SpanBranch setup = main.get(0); /// Main Section
        setup = (SpanBranch)setup.get(0); /// Paragraph
        setup = (SpanBranch)setup.get(0); /// Format span
        setup = (SpanBranch)setup.get(0); /// SetupPointer span
        Optional<Span> test = ((FormatSpanDirectory)setup).getTarget();

        SpanBranch expect = main.get(0); /// Main Section
        expect = (SpanBranch)expect.get(1); /// footnote
        assertTrue("Span not found.", test.isPresent());
        assertSame("Span are not equal", test.get(), expect);
    }

    @Test
    public void citeComplete(){
        SpanExpect note = new SpanExpect();
        SpanExpect noteLine = new SpanExpect();
        noteLine.addChildren("!%", "@", "n", ":", "title", "\n");
        note.addChild(noteLine);

        SpanExpect paragraph = addLine(curlyHelp(DirectoryType.NOTE,
            new String[]{"note"}, "n"), "{@", "n", "}");

        SpanExpect doc = new SpanExpect();
        doc.addChild(note);
        doc.addChild(addSection(paragraph));
        Document main = doc.testAll(parsers);

        SpanBranch setup = main.get(1); /// Main Section
        setup = (SpanBranch)setup.get(0); /// Paragraph
        setup = (SpanBranch)setup.get(0); /// Format span
        setup = (SpanBranch)setup.get(0); /// SetupPointer span
        Optional<Span> test = ((FormatSpanDirectory)setup).getTarget();

        SpanBranch expect = main.get(0); /// Note Section
        assertTrue("Span not found.", test.isPresent());
        assertSame("Span are not equal", test.get(), expect);
    }*/
}
