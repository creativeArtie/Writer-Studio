package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchLineAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchSectionAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
public class SectionTest {

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

        HeadSectionAssert head1 = new HeadSectionAssert(doc)
            .setPublishCount(0)                  .setNoteCount(0)
            .addSection(0, 0)               .setPublishTotal(2)
            .addAllLine(0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionAssert head2 = new HeadSectionAssert(doc)
            .setPublishCount(0)                  .setNoteCount(0)
            .addSection(0, 0, 0)            .setPublishTotal(2)
            .addAllLine(0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionAssert head3 = new HeadSectionAssert(doc)
            .setPublishCount(0)                  .setNoteCount(0)
            .addSection(0, 0, 0, 0)         .setPublishTotal(2)
            .addAllLine(0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionAssert head4 = new HeadSectionAssert(doc)
            .setPublishCount(0)                  .setNoteCount(0)
            .addSection(0, 0, 0, 0, 0)      .setPublishTotal(2)
            .addAllLine(0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionAssert head5 = new HeadSectionAssert(doc)
            .setPublishCount(0)                  .setNoteCount(0)
            .addSection(0, 0, 0, 0, 0, 0)   .setPublishTotal(2)
            .addAllLine(0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadSectionAssert head6 = new HeadSectionAssert(doc)
            .setPublishCount(1)                  .setNoteCount(0)
            .addLine(   0, 0, 0, 0, 0, 0, 0).setPublishTotal(2)
            .setHeading(0, 0, 0, 0, 0, 0, 0).setNoteTotal(0);
        HeadLevelLineAssert line = new HeadLevelLineAssert(doc)
        ///                        1, 2, 3, 4, 5, 6, l, f
            .setFormattedSpan(0, 0, 0, 0, 0, 0, 0, 1)
                      .setEdition(EditionType.NONE)
            .setPublish(1)   .setNote(0)
            .setLinedType(LinedType.HEADING);

        head1.test(1, text, 0);
        head2.test(1, text, 0, 0);
        head3.test(1, text, 0, 0, 0);
        head4.test(1, text, 0, 0, 0, 0);
        head5.test(1, text, 0, 0, 0, 0, 0);
        head6.test(1, text, 0, 0, 0, 0, 0, 0);
        line.test( 2, text, 0, 0, 0, 0, 0, 0, 0);
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
        HeadSectionAssert head1 = new HeadSectionAssert(doc) /// 0
            .setPublishCount(2)   .setNoteCount(0)
            .setPublishTotal(7)   .setNoteTotal(1)
            .addLine(0, 0)
            .addSection(0, 1).setHeading(0, 0)
            .addAllLine(0, 1, 0)
            .addAllLine(0, 1, 1)
            .addAllLine(0, 1, 2, 0);
        HeadLevelLineAssert line0 = new HeadLevelLineAssert(doc) ///0, 0
                      .setEdition(EditionType.NONE)
            .setPublish(2)   .setNote(0)
            .setFormattedSpan(0, 0, 1)
            .setLinedType(LinedType.HEADING);

        HeadSectionAssert head1_1 = new HeadSectionAssert(doc) /// 0, 1
            .setPublishCount(5)      .setNoteCount(0)
            .setPublishTotal(5)      .setNoteTotal(1)
            .addLine(   0, 1, 0).addLine(0, 1, 1)
            .addScene(  0, 1, 2).setHeading(0, 1, 0)
            .addAllLine(0, 1, 2, 0);
        HeadLevelLineAssert line1 = new HeadLevelLineAssert(doc) ///0, 1, 0
                       .setEdition(EditionType.NONE)
            .setPublish(2)    .setNote(0)
            .setFormattedSpan(0, 1, 0, 1)
            .setLinedType(LinedType.HEADING);
        ParagraphLineAssert line2 = new ParagraphLineAssert(doc) /// 0, 1, 1
            .setPublish(3)    .setNote(0)
            .setFormattedSpan(0, 1, 1, 0);

        SceneSectionAssert head1_1_1 = new SceneSectionAssert(doc) /// 0, 1, 2
            .setPublishCount(0)      .setNoteCount(1)
            .setParentHead(0, 1).setHeading(0, 1, 2, 0)
            .addLine(0, 1, 2, 0);
        HeadLevelLineAssert line3 = new HeadLevelLineAssert(doc)/// 0, 1, 2, 0
                         .setEdition(EditionType.NONE)
            .setPublish(0)      .setNote(1)
            .setFormattedSpan(0, 1, 2, 0, 1)
            .setLinedType(LinedType.OUTLINE);

        HeadSectionAssert head2 = new HeadSectionAssert(doc) /// 1
            .setPublishTotal(4)   .setNoteTotal(0)
            .setHeading(1, 0).addLine(1, 0)
            .addLine(1, 1)   ;
        HeadLevelLineAssert line4 = new HeadLevelLineAssert(doc) //1, 0
                      .setEdition(EditionType.NONE)
            .setPublish(2)   .setNote(0)
            .setFormattedSpan(1, 0, 1)
            .setLinedType(LinedType.HEADING);
        ParagraphLineAssert line5 = new ParagraphLineAssert(doc)// 1, 1
            .setPublish(2).setNote(0)
            .setFormattedSpan(1, 1, 0);

        head1.test(    2, sec1,     0);
        line0.test(    3, lines[0], 0, 0);
        head1_1.test(  3, sec1_1,   0, 1);
        line1.test(    3, lines[1], 0, 1, 0);
        head1_1_1.test(1, lines[3], 0, 1, 2);
        line3.test(    3, lines[3], 0, 1, 2, 0);
        head2.test(    2, sec2,     1);
        line4.test(    3, lines[4], 1, 0);
        line5.test(    2, lines[5], 1, 1);
        doc.assertRest();

    }

    public void allSectionLines(){
        String[] texts = new String[]{">quote\n", "#numbered\n", "-bullet\n",
            "!@hyperlink:http://google.com\n", "!^footnote: many text\n",
            "!*endnote: text\n", "!!agenda\n", "***\n", "abc\n"};
        String raw = String.join("", texts);
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        IDBuilder linkId = doc.addId(LinedPointerTest
            .buildLinkId("hyperlink"), 4);
        IDBuilder footnoteId = doc.addId(LinedPointerTest
            .buildFootnoteId("footnote"), 2);
        IDBuilder endnoteId = doc.addId(LinedPointerTest
            .buildEndnoteId("endnote"), 1);
        IDBuilder agendaId = doc.addId(LinedRestTest.buildAgendaId("093"), 0);


        HeadSectionAssert section = new HeadSectionAssert(doc)
            .setPublishTotal(4)   .setNoteTotal(1)
            .setHeading(1, 0).addLine(1, 0);
        for (int i = 0; i < texts.length; i++){
            section.addLine(0, i);
        }
        QuoteLineAssert line1 = new QuoteLineAssert(doc)
            .setFormattedSpan(0, 0, 1)
            .setPublish(1).setNote(0);
        ListLevelLineAssert line2 = new ListLevelLineAssert(doc)
            .setLinedType(LinedType.NUMBERED)
            .setFormattedSpan(0, 1, 1).setPublish(1)
            .setNote(0);
        ListLevelLineAssert line3 = new ListLevelLineAssert(doc)
            .setLinedType(LinedType.BULLET)
            .setFormattedSpan(0, 2, 1).setPublish(1)
            .setNote(0);
        PointerLinkAssert line4 = new PointerLinkAssert(doc)
            .setPath("http://google.com")
            .setCatalogued(CatalogueStatus.UNUSED, linkId);
        PointerNoteAssert line5 = new PointerNoteAssert(doc)
            .setLinedType(LinedType.FOOTNOTE)
            .setFormattedSpan(0, 4, 3)
            .setCatalogued(CatalogueStatus.UNUSED, footnoteId);
        PointerNoteAssert line6 = new PointerNoteAssert(doc)
            .setLinedType(LinedType.ENDNOTE)
            .setFormattedSpan(0, 5, 3)
            .setCatalogued(CatalogueStatus.UNUSED, endnoteId);
        AgendaLineAssert line7 = new AgendaLineAssert(doc)
            .setAgenda("agenda").setNote(1)
            .setCatalogued(CatalogueStatus.UNUSED, agendaId);
        BreakLineAssert line8 = new BreakLineAssert(doc);
        ParagraphLineAssert line9 = new ParagraphLineAssert(doc)
            .setPublish(1).setNote(0)
            .setFormattedSpan(0, 8, 0);

        int i = 0;
        section.test(9, raw, 0);
        line1.test(  3, texts[i], 0, i++); /// quote
        line2.test(  3, texts[i], 0, i++); /// numbered
        line3.test(  3, texts[i], 0, i++); /// bullet
        line4.test(  5, texts[i], 0, i++); /// hyperlink
        line5.test(  5, texts[i], 0, i++); /// footnote
        line6.test(  5, texts[i], 0, i++); /// endnote
        line7.test(  3, texts[i], 0, i++); /// agenda
        line8.test(  1, texts[i], 0, i++); /// break
        line9.test(  2, texts[i], 0, i++); /// paragraph
        doc.assertRest();
    }


    @Test
    public void sectionWithNote(){
        String raw = "!%note";

        DocumentAssert doc = assertDoc(1, raw, PARSER);

        IDBuilder builder = doc.addId(NoteCardTest.buildId(true, "0"), 0);

        HeadSectionAssert head = new HeadSectionAssert(doc)
            .setPublishTotal(0).setNoteTotal(1)
            .addNote(0, 0);
        NoteCardAssert note = new NoteCardAssert(doc).setNote(1)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        head.test(1, raw, 0);
        note.test(1, raw, 0, 0);
        doc.assertRest();
    }

    @Test
    public void sectionWithOutline(){
        String line1 = "!##Heading 2\n";
        String line2 = "Hello World!\n";
        String full = line1 + line2;
        DocumentAssert doc = assertDoc(1, full, PARSER);

         HeadSectionAssert head = new HeadSectionAssert(doc)
            .setPublishTotal(2).setNoteTotal(2)
            .addScene(0, 0)
            .addAllLine(0, 0, 0, 0).addAllLine(0, 0, 0, 1);
         SceneSectionAssert out1 = new SceneSectionAssert(doc)
            .addScene(0, 0, 0)     .setParentHead(0);
         SceneSectionAssert out2 = new SceneSectionAssert(doc)
            .setHeading(0, 0, 0, 0).setParentHead(0)
            .addLine(0, 0, 0, 0)   .addLine(0, 0, 0, 1);
        HeadLevelLineAssert child1 = new HeadLevelLineAssert(doc)
                   .setEdition(EditionType.NONE)
            .setPublish(0).setNote(2)
            .setFormattedSpan(0, 0, 0, 0, 1)
            .setLinedType(LinedType.OUTLINE);
        ParagraphLineAssert child2 = new ParagraphLineAssert(doc)
            .setPublish(2) .setNote(0)
            .setFormattedSpan(0, 0, 0, 1, 0);

        head.test(  1, full,  0);
        out1.test(  1, full,  0, 0);
        out2.test(  2, full,  0, 0, 0);
        child1.test(3, line1, 0, 0, 0, 0);
        child2.test(2, line2, 0, 0, 0, 1);
        doc.assertRest();
    }

    @Test
    public void sectionWithSubheading(){
        String line1 = "==Heading 2\n";
        String line2 = "Hello World!\n";
        String full = line1 + line2;
        DocumentAssert doc = assertDoc(1, full, PARSER);

        HeadSectionAssert head1 = new HeadSectionAssert(doc)
            .setPublishTotal(0)      .setNoteTotal(0)
            .addSection(0, 0)
            .addAllLine(0, 0, 0).addAllLine(0, 0, 1);
         HeadSectionAssert head2 = new HeadSectionAssert(doc)
            .setPublishTotal(4)      .setNoteTotal(0)
            .setHeading(0, 0, 0)
            .addLine(0, 0, 0)   .addLine(0, 0, 1);
        HeadLevelLineAssert child1 = new HeadLevelLineAssert(doc)
                      .setEdition(EditionType.NONE)
            .setPublish(2)   .setNote(0)
            .setLinedType(LinedType.HEADING)
            .setFormattedSpan(0, 0, 0, 1);
        ParagraphLineAssert child2 = new ParagraphLineAssert(doc)
            .setPublish(2)   .setNote(0)
            .setFormattedSpan(0, 0, 1, 0);

        head1.test( 1, full,  0);
        head2.test( 2, full,  0, 0);
        child1.test(3, line1, 0, 0, 0);
        child2.test(2, line2, 0, 0, 1);
        doc.assertRest();
    }

    @Test
    public void sectionWithHeading(){
        String line1 = "=Heading 1\n";
        String line2 = "Hello World!\n";
        String full = line1 + line2;
        DocumentAssert doc = assertDoc(1, full, PARSER);

        HeadSectionAssert head = new HeadSectionAssert(doc)
            .setPublishTotal(4).setNoteTotal(0)
            .setHeading(0, 0)
            .addLine(0, 0).addLine(0, 1);
        HeadLevelLineAssert child1 = new HeadLevelLineAssert(doc)
            .setEdition(EditionType.NONE)
            .setPublish(2).setNote(0)
            .setLinedType(LinedType.HEADING)
            .setFormattedSpan(0, 0, 1);
        ParagraphLineAssert child2 = new ParagraphLineAssert(doc)
            .setPublish(2).setNote(0)
            .setFormattedSpan(0, 1, 0);

        head.test(  2, full,  0);
        child1.test(3, line1, 0, 0);
        child2.test(2, line2, 0, 1);
        doc.assertRest();
    }

    @Test
    public void sectionNoHeading(){
        String line1 = "Hello World!\n";
        String line2 = ">Beep! Beep! Beep?";
        String full = line1 + line2;
        DocumentAssert doc = assertDoc(1, full, PARSER);

        HeadSectionAssert head = new HeadSectionAssert(doc)
            .setPublishTotal(5).setNoteTotal(0)
            .addLine(0, 0).addLine(0, 1);
        ParagraphLineAssert child1 = new ParagraphLineAssert(doc)
            .setPublish(2).setNote(0)
            .setFormattedSpan(0, 0, 0);
        QuoteLineAssert child2 = new QuoteLineAssert(doc)
            .setFormattedSpan(0, 1, 1)
            .setPublish(3).setNote(0);

        head.test(  2, full,  0);
        child1.test(2, line1, 0, 0);
        child2.test(2, line2, 0, 1);
        doc.assertRest();
    }
}
