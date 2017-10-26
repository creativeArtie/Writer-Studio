package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;


@RunWith(JUnit4.class)
public class SupplementLinkDebug {/*
    private static SetupParser[] parsers = new SetupParser[]{new MainParser()};
    
    public static final SpanExpectHelper linkHelp(String[] cat, String id,
            String path, String text){
        return FormatLinkDebug.refHelp(cat, id, path, text);
    }
    
    public static final SpanExpectHelper curlyHelp(DirectoryType type, 
            String[] expectRef, String expectId){
        return span -> {
            FormatCurlyDebug.noteHelp(type, expectRef, expectId, 
                CatalogueStatus.READY).test(span);
            Optional<Span> test = ((FormatSpanDirectory)span).getTarget();
        };
    }
    
    private SpanExpect addSection(SpanExpect... children){
        SpanExpect section = new SpanExpect();
        for(SpanExpect child: children){
            section.addChild(child);
        }
        return section;
    }
    
    private SpanExpect addLine(SpanExpectHelper helper, String ... children){
        SpanExpect paragraph = new SpanExpect();
        SpanExpect format = new SpanExpect();
        SpanExpect link = new SpanExpect(helper);
        link.addChildren(children);
        format.addChild(link);
        paragraph.addChild(format);
        paragraph.addChild("\n");
        return paragraph;
    }
    
    @Test
    public void linkComplete(){
        SpanExpect paragraph = addLine(
            linkHelp(new String[]{"link"}, "a", "www.google.com", "Google"), 
            "<@", "a", "|", "Google", ">");
        
        SpanExpect link = new SpanExpect();
        link.addChildren("!@", "a", ":", "www.google.com", "\n");
        
        SpanExpect section = addSection(paragraph, link);
        SpanExpect doc = new SpanExpect();
        doc.addChild(section);
        doc.testAll(parsers);
    }
    
    @Test
    public void linkNoText(){
        SpanExpect paragraph = addLine(
            linkHelp(new String[]{"link"}, "a", "www.google.com", "www.google.com"), 
            "<@", "a", ">");
        
        SpanExpect link = new SpanExpect();
        link.addChildren("!@", "a", ":", "www.google.com", "\n");
        
        SpanExpect section = addSection(paragraph, link);
        SpanExpect doc = new SpanExpect();
        doc.addChild(section);
        doc.testAll(parsers);
    }
    
    @Test
    @Ignore("Is this needed???")
    public void linkTooMuchIDs(){
        SpanExpect paragraph = addLine(
            linkHelp(new String[]{"link"}, "a", "", ""), 
            "<@", "a", ">");
        
        SpanExpect link1 = new SpanExpect();
        link1.addChildren("!@", "a", ":", "www.google.com", "\n");
        SpanExpect link2 = new SpanExpect();
        link2.addChildren("!@", "a", ":", "www.apple.com", "\n");
        
        SpanExpect section = addSection(paragraph, link1, link2);
        SpanExpect doc = new SpanExpect();
        doc.addChild(section);
        doc.testAll(parsers);
    }
    
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
