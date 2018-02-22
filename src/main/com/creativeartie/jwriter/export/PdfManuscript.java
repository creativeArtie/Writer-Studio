package com.creativeartie.jwriter.export;

import java.io.*;
import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;

import static com.creativeartie.jwriter.main.Checker.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.*;
import com.itextpdf.layout.renderer.*;
import com.itextpdf.kernel.geom.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.List;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.pdf.action.*;
import com.itextpdf.kernel.colors.*;
import com.itextpdf.kernel.events.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.*;

final class PdfManuscript extends PdfBase{
    private static final int HEADING_SIZE = 18;
    private static final int TEXT_SIZE = 12;
    private static final int QUOTE_PADDING = 40;
    private static final String BULLET_SYMBOL = "•  ";
    private static final String LINE_SEP = "━━━━━━━━━━━━━━━━━━━━";
    private static final float NOTE_RISE = 8f;
    private static final int NOTE_SIZE = 8;

    public PdfManuscript(ManuscriptFile input, File output) throws
            FileNotFoundException{
        super(input, output);
    }

    protected Optional<Paragraph> addLineBreak(LinedSpanBreak line){
        Paragraph ans = new Paragraph("*");
        ans.setTextAlignment(TextAlignment.CENTER);
        return Optional.of(ans);
    }

    protected Optional<Paragraph> addHeading(LinedSpanLevelSection line){
        if (line.getLinedType() == LinedType.OUTLINE){
            return Optional.empty();
        }
        pdfDocument.add(new AreaBreak());
        Paragraph ans = addLine(line.getFormattedSpan());
        ans.setFontSize(HEADING_SIZE);
        return Optional.of(ans);
    }

    protected Optional<Paragraph> addParagraph(LinedSpanParagraph line){
        Paragraph ans = addLine(line.getFormattedSpan());
        ans.setFontSize(TEXT_SIZE);
        return Optional.of(ans);
    }

    protected Optional<Paragraph> addQuote(LinedSpanQuote line){
        Paragraph ans = addLine(line.getFormattedSpan());
        ans.setPaddingLeft(QUOTE_PADDING);
        ans.setPaddingRight(QUOTE_PADDING);
        return Optional.of(ans);
    }


    private Text setFormat(Text text, FormatSpan format){
        if (format.isItalics()){
            text.setItalic();
        }
        if (format.isBold()){
            text.setBold();
        }
        if (format.isUnderline()){
            text.setUnderline();
        }
        if (format.isCoded()){
            try {
                PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);
                text.setFont(font);
            } catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
        return text;
    }
}