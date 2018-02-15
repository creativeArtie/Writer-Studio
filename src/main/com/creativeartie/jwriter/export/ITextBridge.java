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
import com.itextpdf.layout.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.pdf.action.*;
import com.itextpdf.kernel.colors.*;

final class ITextBridge{
    private static int HEADING_SIZE = 18;
    private static int TEXT_SIZE = 12;


    public static void pdfManuscript(ManuscriptFile input, File output) throws
            FileNotFoundException{
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.addNewPage();
        try (Document doc = new Document(pdf)){
            for(SpanBranch child: input.getDocument()){
                pdfManuscript(doc, (SectionSpan)child);
            }
        }
    }

    private static void pdfManuscript(Document doc, SectionSpan section){
        Optional<ITextListHandler> list = Optional.empty();
        for (Span child: section){
            boolean listEnds = true;
            if (child instanceof LinedSpanBreak){
                Paragraph paragaraph = new Paragraph("*");
                paragaraph.setTextAlignment(TextAlignment.CENTER);
                doc.add(paragaraph);
            } else if (child instanceof LinedSpanLevelSection){
                doc.add(new AreaBreak());
                LinedSpanLevelSection line = (LinedSpanLevelSection) child;
                Paragraph para = addLine(line.getFormattedSpan());
                para.setFontSize(HEADING_SIZE);
                doc.add(para);
            } else if (child instanceof LinedSpanLevelList){
                listEnds = false;
                LinedSpanLevelList line = (LinedSpanLevelList) child;
                if (! list.isPresent()){
                    list = Optional.of(ITextListHandler.start(doc, line
                        .getLinedType()));
                }
                list = list.get().add(line);
            } else if (child instanceof LinedSpanParagraph){
                LinedSpanParagraph line = (LinedSpanParagraph) child;
                Paragraph para = addLine(line.getFormattedSpan());
                para.setFontSize(TEXT_SIZE);
                doc.add(para);
            } else if (child instanceof SectionSpan){
                pdfManuscript(doc, (SectionSpan) child);
            }
            if (listEnds){
                list.ifPresent(completed -> completed.completed());
                list = Optional.empty();
            }
        }
    }

    static Paragraph addLine(Optional<FormatSpanMain> format){
        Paragraph ans = new Paragraph();
        if (! format.isPresent()){
            return ans;
        }
        for (Span child: format.get()){
            if (child instanceof FormatSpan){
                ans.addAll(addSpan((FormatSpan)child));
            }
        }
        return ans;
    }

    private static ArrayList<ILeafElement> addSpan(FormatSpan format){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        if (format instanceof FormatSpanContent){
            FormatSpanContent content = (FormatSpanContent) format;
            String text = content.getTrimmed();
            if (content.isSpaceBegin()){
                text = " " + text;
            }
            if (content.isSpaceEnd()){
                text = text + " ";
            }
            ans.add(newText(text, format));
        } else if (format instanceof FormatSpanLink){
            FormatSpanLink content = (FormatSpanLink) format;
            ans.add(newText(content.getText(), format));
            Link path;
            Optional<SpanBranch> pointer = content.getPathSpan();
            if (! pointer.isPresent()){
                return ans;
            }
            SpanBranch found = pointer.get();

            if (found instanceof ContentSpan){
                String target = ((ContentSpan)found).getTrimmed();
                PdfAction action = PdfAction.createURI(target);
                path = new Link("<" + target + ">", action);
            } else if (found instanceof LinedSpanPointLink){
                String target = ((LinedSpanPointLink)found).getPath();
                if (target.isEmpty()){
                    return ans;
                }
                PdfAction action = PdfAction.createURI(target);
                path = new Link("<" + target + ">", action);
            } else {
                assert found instanceof LinedSpanLevelSection;
                return ans;
            }
            path.setFontColor(ColorConstants.BLUE);
            ans.add(setFormat(path, format));
        }
        return ans;
    }

    private static Text newText(String data, FormatSpan format){
        Text text = new Text(data);
        setFormat(text, format);
        return text;
    }

    private static Text setFormat(Text text, FormatSpan format){
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
            // text.setFont(StandardFonts.COURIER);
        }
        return text;
    }
}