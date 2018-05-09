package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
import static com.creativeartie.writerstudio.lang.markup.BranchTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchLineTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchSectionTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableListMultimap;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

import com.creativeartie.writerstudio.lang.*;

@RunWith(JUnit4.class)
public class SectionDebug {

    private static final SetupParser PARSER = SectionParseHead.SECTION_1;
    private static String COMMON_DOC = "=Chapter 1\n==Section 1\n" +
        "section 1 text\n!# outline\n=Chapter 2\nsome text\n";

    /// TODO? test SectionSpan#checkStart() but it is a simplish method with
    ///     loads of tests for the methods used in this method

    @Test
    public void sectionWithStuff(){
        DocumentAssert doc = assertDoc(2, COMMON_DOC, PARSER);
        testSections(doc);
    }


    @Test
    public void editChangeSection1_1(){
        ///           0123456789 0123
        String raw = "=Chapter 1\n===Section 1\nsection 1 text\n!# outline\n" +
            "=Chapter 2\nsome text\n";
        DocumentAssert doc = assertDoc(2, raw, PARSER);
        doc.delete(11, 12, 0, 1);
        doc.assertDoc(2, COMMON_DOC, PARSER);
        testSections(doc);
    }

    @Test
    public void editAddOutline(){
        ///           0000000000 111111111122 222222223333333 33
        ///           0123456789 012345678901 234567890123456 78
        String raw = "=Chapter 1\n==Section 1\nsection 1 text\n! outline\n" +
            "=Chapter 2\nsome text\n";
        DocumentAssert doc = assertDoc(2, raw, PARSER);
        doc.insert(39, "#", 0, 1);
        doc.assertDoc(2, COMMON_DOC, PARSER);
        testSections(doc);
    }

    @Test
    public void editChangeOutline(){
        ///           0000000000 111111111122 222222223333333 3334
        ///           0123456789 012345678901 234567890123456 7890
        String raw = "=Chapter 1\n==Section 1\nsection 1 text\n!## outline\n" +
            "=Chapter 2\nsome text\n";
        DocumentAssert doc = assertDoc(2, raw, PARSER);
        /// Note that the edited span is Chapter 1 -> Section 1 -> (Scene 1)
        doc.delete(39, 40, 0, 1, 2);
        doc.assertDoc(2, COMMON_DOC, PARSER);
        testSections(doc);
    }

    @Test
    public void editSection6(){
        ///            012345678
        String raw  = "======ac";
        String text = "======abc";

        DocumentAssert doc = DocumentAssert.assertDoc(1, raw, PARSER);
        ///                1, 2, 3, 4, 5, 6, l
        doc.insert(7, "b", 0, 0, 0, 0, 0, 0, 0);

        HeadSectionTest head1 = new HeadSectionTest().setLevel(1)
            .addSection(doc, 0, 0)               .setPublishTotal(0)
            .addAllLine(doc, 0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionTest head2 = new HeadSectionTest().setLevel(2)
            .addSection(doc, 0, 0, 0)            .setPublishTotal(0)
            .addAllLine(doc, 0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionTest head3 = new HeadSectionTest().setLevel(3)
            .addSection(doc, 0, 0, 0, 0)         .setPublishTotal(0)
            .addAllLine(doc, 0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionTest head4 = new HeadSectionTest().setLevel(4)
            .addSection(doc, 0, 0, 0, 0, 0)      .setPublishTotal(0)
            .addAllLine(doc, 0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionTest head5 = new HeadSectionTest().setLevel(5)
            .addSection(doc, 0, 0, 0, 0, 0, 0)   .setPublishTotal(0)
            .addAllLine(doc, 0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionTest head6 = new HeadSectionTest().setLevel(6)
            .addLine(   doc, 0, 0, 0, 0, 0, 0, 0).setPublishTotal(1)
            .setHeading(doc, 0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadLevelLineTest line = new HeadLevelLineTest()
        ///                        1, 2, 3, 4, 5, 6, l, f
            .setFormattedSpan(doc, 0, 0, 0, 0, 0, 0, 0, 1)
            .setLevel(6)          .setEdition(EditionType.NONE)
            .setPublishTotal(1)   .setNoteTotal(0)
            .setLinedType(LinedType.HEADING);

        head1.test(doc, 1, text, 0);
        head2.test(doc, 1, text, 0, 0);
        head3.test(doc, 1, text, 0, 0, 0);
        head4.test(doc, 1, text, 0, 0, 0, 0);
        head5.test(doc, 1, text, 0, 0, 0, 0, 0);
        head6.test(doc, 1, text, 0, 0, 0, 0, 0, 0);
        line.test( doc, 2, text, 0, 0, 0, 0, 0, 0, 0);
    }

    private void testSections(DocumentAssert doc){
        String[] lines = new String[]{
            /// Section 1:
            "=Chapter 1\n",
                /// Section 1.1:
                    "==Section 1\n", "section 1 text\n", "!# outline\n",
            /// Section 2
            "=Chapter 2\n", "some text\n"};

        String sec1 = lines[0] + lines[1] + lines[2] + lines[3];
        String sec1_1 = lines[1] + lines[2] + lines[3];
        /// outline 3 = lines[3];
        String sec2 = lines[4] + lines[5];

        String full = String.join("", lines);
        HeadSectionTest head1 = new HeadSectionTest() /// 0
            .setPublishTotal(2)   .setNoteTotal(0)
            .addLine(doc, 0, 0)   .setLevel(1)
            .addSection(doc, 0, 1).setHeading(doc, 0, 0)
            .addAllLine(doc, 0, 1, 0)
            .addAllLine(doc, 0, 1, 1)
            .addAllLine(doc, 0, 1, 2, 0);
        HeadLevelLineTest line0 = new HeadLevelLineTest() ///0, 0
            .setLevel(1)          .setEdition(EditionType.NONE)
            .setPublishTotal(2)   .setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0, 1)
            .setLinedType(LinedType.HEADING);

        HeadSectionTest head1_1 = new HeadSectionTest() /// 0, 1
            .setPublishTotal(5)      .setNoteTotal(1)
            .addLine(   doc, 0, 1, 0).addLine(doc, 0, 1, 1)
            .addScene(  doc, 0, 1, 2).setHeading(doc, 0, 1, 0)
            .addAllLine(doc, 0, 1, 2, 0).setLevel(2);
        HeadLevelLineTest line1 = new HeadLevelLineTest() ///0, 1, 0
            .setLevel(2)           .setEdition(EditionType.NONE)
            .setPublishTotal(2)    .setNoteTotal(0)
            .setFormattedSpan(doc, 0, 1, 0, 1)
            .setLinedType(LinedType.HEADING);
        ParagraphLineTest line2 = new ParagraphLineTest() /// 0, 1, 1
            .setPublishTotal(3)    .setNoteTotal(0)
            .setFormattedSpan(doc, 0, 1, 1, 0);

        SceneSectionTest head1_1_1 = new SceneSectionTest() /// 0, 1, 2
            .setParentHead(doc, 0, 1).setHeading(doc, 0, 1, 2, 0)
            .addLine(doc, 0, 1, 2, 0).setLevel(3);
        HeadLevelLineTest line3 = new HeadLevelLineTest()/// 0, 1, 2, 0
            .setLevel(1)             .setEdition(EditionType.NONE)
            .setPublishTotal(0)      .setNoteTotal(1)
            .setFormattedSpan(doc, 0, 1, 2, 0, 1)
            .setLinedType(LinedType.OUTLINE);

        HeadSectionTest head2 = new HeadSectionTest() /// 1
            .setPublishTotal(4)   .setNoteTotal(0)
            .setHeading(doc, 1, 0).addLine(doc, 1, 0)
            .addLine(doc, 1, 1)   .setLevel(1);
        HeadLevelLineTest line4 = new HeadLevelLineTest() //1, 0
            .setLevel(1)          .setEdition(EditionType.NONE)
            .setPublishTotal(2)   .setNoteTotal(0)
            .setFormattedSpan(doc, 1, 0, 1)
            .setLinedType(LinedType.HEADING);
        ParagraphLineTest line5 = new ParagraphLineTest()// 1, 1
            .setPublishTotal(2).setNoteTotal(0)
            .setFormattedSpan(doc, 1, 1, 0);

        head1.test(    doc, 2, sec1,     0);
        line0.test(    doc, 3, lines[0], 0, 0);
        head1_1.test(  doc, 3, sec1_1,   0, 1);
        line1.test(    doc, 3, lines[1], 0, 1, 0);
        line2.test(    doc, 2, lines[2], 0, 1, 1);
        head1_1_1.test(doc, 1, lines[3], 0, 1, 2);
        line3.test(    doc, 3, lines[3], 0, 1, 2, 0);
        head2.test(    doc, 2, sec2,     1);
        line4.test(    doc, 3, lines[4], 1, 0);
        line5.test(    doc, 2, lines[5], 1, 1);
        doc.assertLast();
        doc.assertIds();

    }

    public void allSectionLines(){
        String[] texts = new String[]{">quote\n", "#numbered\n", "-bullet\n",
            "!@hyperlink:http://google.com\n", "!^footnote: many text\n",
            "!*endnote: text\n", "!!agenda\n", "***\n", "abc\n"};
        String raw = String.join("", texts);
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        IDBuilder linkId = doc.addId(LinedPointerDebug
            .buildLinkId("hyperlink"), 4);
        IDBuilder footnoteId = doc.addId(LinedPointerDebug
            .buildFootnoteId("footnote"), 2);
        IDBuilder endnoteId = doc.addId(LinedPointerDebug
            .buildEndnoteId("endnote"), 1);
        IDBuilder agendaId = doc.addId(LinedRestDebug.buildAgendaId("093"), 0);


        HeadSectionTest section = new HeadSectionTest()
            .setPublishTotal(4)   .setNoteTotal(1)
            .setHeading(doc, 1, 0).addLine(doc, 1, 0);
        for (int i = 0; i < texts.length; i++){
            section.addLine(doc, 0, i);
        }
        QuoteLineTest line1 = new QuoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1)
            .setPublishTotal(1).setNoteTotal(0);
        ListLevelLineTest line2 = new ListLevelLineTest()
            .setLinedType(LinedType.NUMBERED).setLevel(1)
            .setFormattedSpan(doc, 0, 1, 1).setPublishTotal(1)
            .setNoteTotal(0);
        ListLevelLineTest line3 = new ListLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(1)
            .setFormattedSpan(doc, 0, 2, 1).setPublishTotal(1)
            .setNoteTotal(0);
        PointerLinkTest line4 = new PointerLinkTest()
            .setPath("http://google.com")
            .setCatalogued(CatalogueStatus.UNUSED, linkId);
        PointerNoteTest line5 = new PointerNoteTest()
            .setLinedType(LinedType.FOOTNOTE)
            .setFormattedSpan(doc, 0, 4, 3)
            .setCatalogued(CatalogueStatus.UNUSED, footnoteId);
        PointerNoteTest line6 = new PointerNoteTest()
            .setLinedType(LinedType.ENDNOTE)
            .setFormattedSpan(doc, 0, 5, 3)
            .setCatalogued(CatalogueStatus.UNUSED, endnoteId);
        AgendaLineTest line7 = new AgendaLineTest()
            .setAgenda("agenda").setNoteTotal(1)
            .setCatalogued(CatalogueStatus.UNUSED, agendaId);
        BreakLineTest line8 = new BreakLineTest();
        ParagraphLineTest line9 = new ParagraphLineTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 8, 0);

        int i = 0;
        section.test(doc, 9, raw, 0);
        line1.test(  doc, 3, texts[i], 0, i++); /// quote
        line2.test(  doc, 3, texts[i], 0, i++); /// numbered
        line3.test(  doc, 3, texts[i], 0, i++); /// bullet
        line4.test(  doc, 5, texts[i], 0, i++); /// hyperlink
        line5.test(  doc, 5, texts[i], 0, i++); /// footnote
        line6.test(  doc, 5, texts[i], 0, i++); /// endnote
        line7.test(  doc, 3, texts[i], 0, i++); /// agenda
        line8.test(  doc, 1, texts[i], 0, i++); /// break
        line9.test(  doc, 2, texts[i], 0, i++); /// paragraph
        doc.assertLast();
        doc.assertIds();
    }


    @Test
    public void sectionWithNote(){
        String raw = "!%note";

        DocumentAssert doc = assertDoc(1, raw, PARSER);

        IDBuilder builder = doc.addId(NoteCardDebug.buildId(true, "0"), 0);

        HeadSectionTest head = new HeadSectionTest()
            .setPublishTotal(0).setNoteTotal(1)
            .addNote(doc, 0, 0);
        NoteCardTest note = new NoteCardTest().setNoteTotal(1)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        head.test(doc, 1, raw, 0);
        note.test(doc, 1, raw, 0, 0);
        doc.assertLast();
        doc.assertIds();
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
            .addAllLine(doc, 0, 0, 0, 0).addAllLine(doc, 0, 0, 0, 1);
         SceneSectionTest out1 = new SceneSectionTest()
            .addScene(doc, 0, 0, 0)     .setParentHead(doc, 0);
         SceneSectionTest out2 = new SceneSectionTest()
            .setHeading(doc, 0, 0, 0, 0).setParentHead(doc, 0)
            .addLine(doc, 0, 0, 0, 0)   .addLine(doc, 0, 0, 0, 1);
        HeadLevelLineTest child1 = new HeadLevelLineTest()
            .setLevel(2)       .setEdition(EditionType.NONE)
            .setPublishTotal(0).setNoteTotal(2)
            .setFormattedSpan(doc, 0, 0, 0, 0, 1)
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
            .setPublishTotal(0)      .setNoteTotal(0)
            .addSection(doc, 0, 0)   .setLevel(1)
            .addAllLine(doc, 0, 0, 0).addAllLine(doc, 0, 0, 1);
         HeadSectionTest head2 = new HeadSectionTest()
            .setPublishTotal(4)      .setNoteTotal(0)
            .setHeading(doc, 0, 0, 0).setLevel(2)
            .addLine(doc, 0, 0, 0)   .addLine(doc, 0, 0, 1);
        HeadLevelLineTest child1 = new HeadLevelLineTest()
            .setLevel(2)          .setEdition(EditionType.NONE)
            .setPublishTotal(2)   .setNoteTotal(0)
            .setLinedType(LinedType.HEADING)
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
            .setLinedType(LinedType.HEADING)
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