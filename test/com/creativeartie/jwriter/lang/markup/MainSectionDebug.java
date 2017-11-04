package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;


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

    public void assertSection(SpanBranch span, Span head, int publish, int note,
        EditionType type, CatalogueStatus status, IDBuilder id)
    {
        MainSpanSection test = assertClass(span, MainSpanSection.class);

        DetailStyle[] styles = new DetailStyle[]{AuxiliaryStyle.MAIN_SECTION};

        assertSpan("head", head, test.getSelfSection());
        assertEquals(getError("edition", test), type, test.getEdition());
        assertSpanIdentity(span, id);
        MainNoteDebug.assertMain(test, publish, note);
        assertBranch(test, styles, status);
    }

    @Test
    public void simple(){
        String raw = "abc";
        DocumentAssert doc = assertDoc(1, raw, new MainParser());
        SpanBranch section = doc.assertChild(1, raw, 0);
        SpanBranch line    = doc.assertChild(1, raw, 0, 0);
        SpanBranch format  = doc.assertChild(1, raw, 0, 0, 0);

        IDBuilder builder = buildId("0");
        doc.addId(builder, 0);
        doc.assertIds();

        assertSection(section, null, 1, 0, EditionType.NONE,
            CatalogueStatus.UNUSED, builder);
        LinedRestDebug.assertParagraph(line, format, 1, 0);
    }

    @Test
    public void allSectionLines(){
        String[] texts = new String[]{">quote\n", "#numbered\n", "-bullet\n",
            "!@hyperlink:http://google.com\n", "!^footnote: many text\n",
            "!*endnote: text\n", "!!agenda\n", "***\n", "abc\n"};
        String raw = String.join("", texts);
        DocumentAssert doc = assertDoc(1, raw, new MainParser());
        SpanBranch section = doc.assertChild(texts.length, raw, 0);
        SpanBranch[] lines = new SpanBranch[texts.length];
        int i = 0;
        lines[i] = doc.assertChild(3, texts[i], 0, i++); /// quote
        lines[i] = doc.assertChild(3, texts[i], 0, i++); /// numbered
        lines[i] = doc.assertChild(3, texts[i], 0, i++); /// bullet
        lines[i] = doc.assertChild(5, texts[i], 0, i++); /// hyperlink
        lines[i] = doc.assertChild(5, texts[i], 0, i++); /// footnote
        lines[i] = doc.assertChild(5, texts[i], 0, i++); /// endnote
        lines[i] = doc.assertChild(3, texts[i], 0, i++); /// agenda
        lines[i] = doc.assertChild(1, texts[i], 0, i++); /// break
        lines[i] = doc.assertChild(2, texts[i], 0, i++); /// paragraph
        SpanBranch quote     = doc.assertChild(1, "quote",      0, 0, 1);
        SpanBranch number    = doc.assertChild(1, "numbered",   0, 1, 1);
        SpanBranch bullet    = doc.assertChild(1, "bullet",     0, 2, 1);
        SpanBranch footnote  = doc.assertChild(1, " many text", 0, 4, 3);
        SpanBranch endnote   = doc.assertChild(1, " text",      0, 5, 3);
        SpanBranch agenda    = doc.assertChild(1, "agenda",     0, 6, 1);
        SpanBranch paragraph = doc.assertChild(1, "abc",        0, 8, 0);

        IDBuilder builder = buildId("000");
        doc.addId(builder, 3);

        assertSection(section, null, 4, 1, EditionType.NONE, CatalogueStatus.UNUSED, builder);
        LinedRestDebug.assertQuote(lines[0], quote, 1, 0);
        LinedLevelRestDebug.assertLevel(lines[1], LinedType.NUMBERED, 1, number, 1, 0);
        LinedLevelRestDebug.assertLevel(lines[2], LinedType.BULLET, 1, bullet, 1, 0);

        builder.reset().addCategory("link").setId("hyperlink");
        doc.addId(builder, 4);
        LinedPointerDebug.assertLink(lines[3], "http://google.com",
            CatalogueStatus.UNUSED, builder);

        builder.reset().addCategory("foot").setId("footnote");
        doc.addId(builder, 2);
        LinedPointerDebug.assertNote(lines[4],
            LinedType.FOOTNOTE, footnote, CatalogueStatus.UNUSED, builder);

        builder.reset().addCategory("end").setId("endnote");
        doc.addId(builder, 1);
        LinedPointerDebug.assertNote(lines[5],
            LinedType.ENDNOTE, endnote, CatalogueStatus.UNUSED, builder);

        builder.reset().addCategory("agenda").setId("093");
        doc.addId(builder, 0);
        LinedRestDebug.assertAgenda(lines[6], agenda, 1, builder);

        LinedRestDebug.assertBreak(lines[7]);
        LinedRestDebug.assertParagraph(lines[8], paragraph, 1, 0);

        doc.assertIds();
    }

    @Test
    public void list(){
        String[] texts = new String[]{"# abc\n", "# ccc\n", "#dead\n",
            "#win\n"};
        String raw = String.join("", texts);
        DocumentAssert doc = assertDoc(1, raw, new MainParser());
        SpanBranch section = doc.assertChild(texts.length, raw, 0);
        SpanBranch[] lines = new SpanBranch[texts.length];
        for (int i = 0; i < texts.length; i++){
            lines[i] = doc.assertChild(3, texts[i], 0, i);
        }
        SpanBranch[] content = new SpanBranch[texts.length];
        for(int i = 0; i < texts.length; i++){
            int end = texts[i].length() - 1;
            content[i] = doc.assertChild(1, texts[i].substring(1, end), 0, i, 1);
        }

        IDBuilder builder = buildId("00");
        doc.addId(builder, 0);

        assertSection(section, null, 4, 0, EditionType.NONE,
            CatalogueStatus.UNUSED, builder);
        for (int i = 0; i < texts.length; i++){
            LinedLevelRestDebug.assertLevel(lines[i],
                LinedType.NUMBERED, 1, content[i], 1, 0);
        }

        doc.assertIds();
    }

    @Test
    public void sectionWithId(){
        String[] texts = new String[]{"=@a:next!#DRAFT\n",
            "explain"};
        String raw = String.join("", texts);
        DocumentAssert doc = assertDoc(1, raw, new MainParser());
        SpanBranch section = doc.assertChild(2, raw,  0);
        SpanBranch line1    = doc.assertChild(7, texts[0],  0, 0);
        SpanBranch heading  = doc.assertChild(1, "next!",   0, 0, 4);
        SpanBranch edition  = doc.assertChild(1, "#DRAFT",  0, 0, 5);
        SpanBranch line2    = doc.assertChild(1, texts[1],  0, 1);
        SpanBranch content  = doc.assertChild(1, "explain", 0, 1, 0);

        IDBuilder builder = buildId("00");
        doc.addId(builder, 0);

        assertSection(section, line1, 2, 0, EditionType.DRAFT,
            CatalogueStatus.UNUSED, builder);

        builder = LinedLevelHeadDebug.buildId("a");
        doc.addId(builder,  1);

        LinedLevelHeadDebug.assertHeading(line1, heading, LinedType.HEADING, 1,
            builder, EditionType.DRAFT, 1, 0, CatalogueStatus.UNUSED);
        LinedRestDebug.assertParagraph(line2, content, 1, 0);

        doc.assertIds();
    }

    @Test
    public void emptyToHeading(){
        String[] texts = new String[]{">quote\n", "=next!#STUB\n", "explain\n"};
        String raw = String.join("", texts);
        String sec2 = texts[1] + texts[2];
        DocumentAssert doc = assertDoc(2, raw, new MainParser());
        SpanBranch section1 = doc.assertChild(1, texts[0],  0);
        SpanBranch line1    = doc.assertChild(3, texts[0],  0, 0);
        SpanBranch quote    = doc.assertChild(1, "quote",   0, 0, 1);
        SpanBranch section2 = doc.assertChild(2, sec2,      1);
        SpanBranch line2    = doc.assertChild(4, texts[1],  1, 0);
        SpanBranch heading  = doc.assertChild(1, "next!",   1, 0, 1);
        SpanBranch line3    = doc.assertChild(2, texts[2],  1, 1);
        SpanBranch content  = doc.assertChild(1, "explain", 1, 1, 0);

        IDBuilder builder = new IDBuilder().addCategory("head").setId("00");
        doc.addId(builder, 0);

        assertSection(section1, null, 1, 0, EditionType.NONE,
            CatalogueStatus.UNUSED, builder);
        LinedRestDebug.assertQuote(line1, quote, 1, 0);

        doc.addId(builder.reset().addCategory("head").setId("07"), 1);
        assertSection(section2, line2, 2, 0, EditionType.STUB,
            CatalogueStatus.UNUSED, builder);
        LinedLevelHeadDebug.assertHeading(line2, heading, LinedType.HEADING, 1,
            null, EditionType.STUB, 1, 0, CatalogueStatus.NO_ID);
        LinedRestDebug.assertParagraph(line3, content, 1, 0);

        doc.assertIds();

    }

    /// More test at SupplementSectionDebug
}
