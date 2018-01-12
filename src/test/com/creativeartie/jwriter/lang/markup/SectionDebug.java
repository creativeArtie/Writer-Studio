package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchSectionTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableListMultimap;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class SectionDebug {
    private static final SetupParser PARSER = SectionParseHead.SECTION_1;

    public static IDBuilder buildId(String name){
        return new IDBuilder().addCategory("head").setId(name);
    }

    @Test
    public void sectionWithOutline(){
        String line1 = "!##Heading 2\n";
        String line2 = "Hello World!\n";
        String full = line1 + line2;
        DocumentAssert doc = assertDoc(1, full, PARSER);

         HeadSectionTest head = new HeadSectionTest()
            .setPublishTotal(2).setNoteTotal(2)
            .addScene(doc, 0, 0)
            .addLine(doc, 0, 0, 0, 0).addLine(doc, 0, 0, 0, 1);
         SceneSectionTest out1 = new SceneSectionTest()
            .addScene(doc, 0, 0, 0)     .setParentHead(doc, 0);
         SceneSectionTest out2 = new SceneSectionTest()
            .setHeading(doc, 0, 0, 0, 0).setParentHead(doc, 0)
            .addLine(doc, 0, 0, 0, 0)   .addLine(doc, 0, 0, 0, 1);
        HeadLevelLineTest child1 = new HeadLevelLineTest()
            .setLevel(2)       .setEdition(EditionType.NONE)
            .setPublishTotal(0).setNoteTotal(2)
            .setIsLast(false)  .setFormattedSpan(doc, 0, 0, 0, 0, 1)
            .setLinedType(LinedType.OUTLINE);
        ParagraphLineTest child2 = new ParagraphLineTest()
            .setPublishTotal(2) .setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0, 0, 1, 0);

        head.test(  doc, 1, full,  0);
        out1.test(  doc, 1, full,  0, 0);
        out2.test(  doc, 2, full,  0, 0, 0);
        child1.test(doc, 3, line1, 0, 0, 0, 0);
        child2.test(doc, 2, line2, 0, 0, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }
    @Test
    public void sectionWithSubheading(){
        String line1 = "==Heading 2\n";
        String line2 = "Hello World!\n";
        String full = line1 + line2;
        DocumentAssert doc = assertDoc(1, full, PARSER);

        HeadSectionTest head1 = new HeadSectionTest()
            .setPublishTotal(0).setNoteTotal(0)
            .addSection(doc, 0, 0);
         HeadSectionTest head2 = new HeadSectionTest()
            .setPublishTotal(4).setNoteTotal(0)
            .setHeading(doc, 0, 0, 0)
            .addLine(doc, 0, 0, 0).addLine(doc, 0, 0, 1);
        HeadLevelLineTest child1 = new HeadLevelLineTest()
            .setLevel(2)          .setEdition(EditionType.NONE)
            .setPublishTotal(2)   .setNoteTotal(0)
            .setIsLast(false)     .setLinedType(LinedType.HEADING)
            .setFormattedSpan(doc, 0, 0, 0, 1);
        ParagraphLineTest child2 = new ParagraphLineTest()
            .setPublishTotal(2)   .setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0, 1, 0);

        head1.test( doc, 1, full,  0);
        head2.test( doc, 2, full,  0, 0);
        child1.test(doc, 3, line1, 0, 0, 0);
        child2.test(doc, 2, line2, 0, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void sectionWithHeading(){
        String line1 = "=Heading 1\n";
        String line2 = "Hello World!\n";
        String full = line1 + line2;
        DocumentAssert doc = assertDoc(1, full, PARSER);

         HeadSectionTest head = new HeadSectionTest()
            .setPublishTotal(4).setNoteTotal(0)
            .setHeading(doc, 0, 0)
            .addLine(doc, 0, 0).addLine(doc, 0, 1);
        HeadLevelLineTest child1 = new HeadLevelLineTest()
            .setLevel(1)       .setEdition(EditionType.NONE)
            .setPublishTotal(2).setNoteTotal(0)
            .setIsLast(false)  .setLinedType(LinedType.HEADING)
            .setFormattedSpan(doc, 0, 0, 1);
        ParagraphLineTest child2 = new ParagraphLineTest()
            .setPublishTotal(2).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 1, 0);

        head.test(  doc, 2, full,  0);
        child1.test(doc, 3, line1, 0, 0);
        child2.test(doc, 2, line2, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void sectionNoHeading(){
        String line1 = "Hello World!\n";
        String line2 = ">Beep! Beep! Beep?";
        String full = line1 + line2;
        DocumentAssert doc = assertDoc(1, full, PARSER);

        HeadSectionTest head = new HeadSectionTest()
            .setPublishTotal(5).setNoteTotal(0)
            .addLine(doc, 0, 0).addLine(doc, 0, 1);
        ParagraphLineTest child1 = new ParagraphLineTest()
            .setPublishTotal(2).setNoteTotal(0)
            .setIsLast(false)
            .setFormattedSpan(doc, 0, 0, 0);
        QuoteLineTest child2 = new QuoteLineTest()
            .setFormattedSpan(doc, 0, 1, 1)
            .setPublishTotal(3).setNoteTotal(0);

        head.test(  doc, 2, full,  0);
        child1.test(doc, 2, line1, 0, 0);
        child2.test(doc, 2, line2, 0, 1);
        doc.assertLast();
        doc.assertIds();
    }
}