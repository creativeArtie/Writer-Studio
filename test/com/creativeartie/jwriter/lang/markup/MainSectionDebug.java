package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchMainTest.*;


import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;


/// @see SupplementSectionDebug
@RunWith(JUnit4.class)
public class MainSectionDebug {

    public static IDBuilder buildId(String id){
        return new IDBuilder().addCategory("head").setId(id);
    }

    @Test
    public void simple(){
        String raw = "abc";
        DocumentAssert doc = assertDoc(1, raw, new MainParser());

        IDBuilder builder = buildId("0");
        doc.addId(builder, 0);
        doc.assertIds();

        MainSectionTest section = new MainSectionTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setEdition(EditionType.NONE)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        ParagraphLineTest line = new ParagraphLineTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 0, 0);

        section.test(doc, 1, raw, 0);
        line.test(   doc, 1, raw, 0, 0);
    }

    @Test
    public void allSectionLines(){
        String[] texts = new String[]{">quote\n", "#numbered\n", "-bullet\n",
            "!@hyperlink:http://google.com\n", "!^footnote: many text\n",
            "!*endnote: text\n", "!!agenda\n", "***\n", "abc\n"};
        String raw = String.join("", texts);
        DocumentAssert doc = assertDoc(1, raw, new MainParser());

        IDBuilder builder = buildId("000");
        doc.addId(builder, 3);

        MainSectionTest section = new MainSectionTest()
            .setPublishTotal(4).setNoteTotal(1)
            .setEdition(EditionType.NONE)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        QuoteLineTest line1 = new QuoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1)
            .setPublishTotal(1).setNoteTotal(0)
            .setIsLast(false);
        BasicLevelLineTest line2 = new BasicLevelLineTest()
            .setLinedType(LinedType.NUMBERED).setLevel(1)
            .setFormattedSpan(doc, 0, 1, 1).setPublishTotal(1)
            .setNoteTotal(0).setIsLast(false);
        BasicLevelLineTest line3 = new BasicLevelLineTest()
            .setLinedType(LinedType.BULLET).setLevel(1)
            .setFormattedSpan(doc, 0, 2, 1).setPublishTotal(1)
            .setNoteTotal(0).setIsLast(false);

        builder.reset().addCategory("link").setId("hyperlink");
        doc.addId(builder, 4);

        PointerLinkTest line4 = new PointerLinkTest()
            .setPath("http://google.com").setIsLast(false)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        builder.reset().addCategory("foot").setId("footnote");
        doc.addId(builder, 2);

        PointerNoteTest line5 = new PointerNoteTest()
            .setLinedType(LinedType.FOOTNOTE)
            .setFormattedSpan(doc, 0, 4, 3).setIsLast(false)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        builder.reset().addCategory("end").setId("endnote");
        doc.addId(builder, 1);

        PointerNoteTest line6 = new PointerNoteTest()
            .setLinedType(LinedType.ENDNOTE)
            .setFormattedSpan(doc, 0, 5, 3).setIsLast(false)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        builder.reset().addCategory("agenda").setId("093");
        doc.addId(builder, 0);

        AgendaLineTest line7 = new AgendaLineTest()
            .setAgenda("agenda").setNoteTotal(1)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setIsLast(false);
        BreakLineTest line8 = new BreakLineTest().setIsLast(false);
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
        doc.assertIds();
    }

    @Test
    public void list(){
        String[] texts = new String[]{"# abc\n", "# ccc\n", "#dead\n",
            "#win\n"};
        String raw = String.join("", texts);
        DocumentAssert doc = assertDoc(1, raw, new MainParser());

        IDBuilder builder = buildId("00");
        doc.addId(builder, 0);

        MainSectionTest section = new MainSectionTest()
            .setPublishTotal(4).setNoteTotal(0)
            .setEdition(EditionType.NONE)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        BasicLevelLineTest[] lines = new BasicLevelLineTest[texts.length];
        for (int i = 0; i < texts.length; i++){
            lines[i] = new BasicLevelLineTest()
                .setLinedType(LinedType.NUMBERED).setLevel(1)
                .setFormattedSpan(doc, 0, i, 1).setPublishTotal(1)
                .setNoteTotal(0);
            if (i < texts.length - 1){
                lines[i].setIsLast(false);
            }
        }

        section.test(doc, 4, raw, 0);
        for (int i = 0; i < texts.length; i++){
            lines[i].test(  doc, 3, texts[i], 0, i);
        }
        doc.assertIds();
    }

    @Test
    public void sectionWithId(){
        String[] texts = new String[]{"=@a:next!#DRAFT\n",
            "explain"};
        String raw = String.join("", texts);
        DocumentAssert doc = assertDoc(1, raw, new MainParser());

        IDBuilder builder = buildId("00");
        doc.addId(builder, 0);

        MainSectionTest section = new MainSectionTest()
            .setPublishTotal(2).setNoteTotal(0)
            .setEdition(EditionType.DRAFT)
            .setCatalogued(CatalogueStatus.UNUSED, builder);

        builder = LinedLevelHeadDebug.buildId("a");
        doc.addId(builder,  1);

        HeadLevelLineTest line1 = new HeadLevelLineTest()
            .setFormattedSpan(doc, 0, 0, 4).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.DRAFT)
            .setPublishTotal(1).setNoteTotal(0)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setIsLast(false);
        ParagraphLineTest line2 = new ParagraphLineTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setFormattedSpan(doc, 0, 1, 0);

        section.test(doc, 2, raw, 0);
        line1.test(  doc, 7, texts[0], 0, 0);
        line2.test(  doc, 1, texts[1], 0, 1);
        doc.assertIds();
    }

    @Test
    public void emptyToHeading(){
        String[] texts = new String[]{">quote\n", "=next!#STUB\n", "explain\n"};
        String raw = String.join("", texts);
        String sec2 = texts[1] + texts[2];

        DocumentAssert doc = assertDoc(2, raw, new MainParser());

        IDBuilder builder = new IDBuilder().addCategory("head").setId("00");
        doc.addId(builder, 0);

        MainSectionTest section1 = new MainSectionTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setEdition(EditionType.NONE)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        QuoteLineTest line1 = new QuoteLineTest()
            .setFormattedSpan(doc, 0, 0, 1)
            .setPublishTotal(1).setNoteTotal(0)
            .setIsLast(false);

        doc.addId(builder.reset().addCategory("head").setId("07"), 1);

        MainSectionTest section2 = new MainSectionTest()
            .setPublishTotal(2).setNoteTotal(0)
            .setEdition(EditionType.STUB)
            .setCatalogued(CatalogueStatus.UNUSED, builder);
        HeadLevelLineTest line2 = new HeadLevelLineTest()
            .setFormattedSpan(doc, 1, 0, 1).setLinedType(LinedType.HEADING)
            .setLevel(1).setEdition(EditionType.STUB)
            .setPublishTotal(1).setNoteTotal(0)
            .setIsLast(false);
        ParagraphLineTest line3 = new ParagraphLineTest()
            .setPublishTotal(1).setNoteTotal(0)
            .setFormattedSpan(doc, 1, 1, 0);

        section1.test(doc, 1, texts[0], 0);
        line1.test(   doc, 3, texts[0], 0, 0);
        section2.test(doc, 2, sec2,     1);
        line2.test(   doc, 4, texts[1], 1, 0);
        line3.test(   doc, 2, texts[2], 1, 1);
        doc.assertIds();

    }

    /// More test at SupplementSectionDebug
}
