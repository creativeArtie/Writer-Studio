package com.creativeartie.jwriter.export;

import java.io.*;
import java.util.*;
import com.google.common.collect.*;

import static com.creativeartie.jwriter.main.Checker.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.font.constants.*;

/** A list of {@link SpanBranch} with the same {@link CatalogueIdentity}. */
public final class PdfExporter implements Publisher{
    private Document exportDoc;
    public PdfExporter(File export) throws IOException{
        PdfWriter writer = new PdfWriter(export);

        PdfDocument pdf = new PdfDocument(writer);

        exportDoc = new Document(pdf);
    }

    public void addParagraph(FormatSpanMain format){
        Paragraph paragaraph = new Paragraph();
        for (Span child: format){
            if (child instanceof FormatSpan){
                paragaraph.addAll(createTexts((FormatSpan)child, 14));
            }
        }
        exportDoc.add(paragaraph);
    }

    public void addHeading(FormatSpanMain format){
        Paragraph paragaraph = new Paragraph();
        for (Span child: format){
            if (child instanceof FormatSpan){
                paragaraph.addAll(createTexts((FormatSpan)child, 16));
            }
        }
        exportDoc.add(paragaraph);
    }

    public void addSecionBreak(){
    }

    private ArrayList<ILeafElement> createTexts(FormatSpan format, int size){
        ArrayList<ILeafElement> ans = new ArrayList<>();
        if (format instanceof FormatSpanContent){
            FormatSpanContent content = (FormatSpanContent) format;
            String text = content.getTrimmed();
            if (content.isSpaceBegin()){
                text += " " + text;
            }
            if (content.isSpaceEnd()){
                text += text + " ";
            }
            ans.add(newText(text, format));
        } else if (format instanceof FormatSpanLink){
            FormatSpanLink content = (FormatSpanLink) format;
            String text = content.getText();
            /* String path = content.getPath();
            if (content.isExternal()){
                ans.add(newText(text + "<" + path + ">", format));
            } else {
                ans.add(newText(text + "(" + path + ")", format));
            }*/
        }
        return ans;
    }

    private Text newText(String data, FormatSpan format){
        Text text = new Text(data);
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
            text.setFont(StandardFonts.COURIER);
        }
        return text;
    }

    @Override
    public void close(){
        exportDoc.close();
    }
}