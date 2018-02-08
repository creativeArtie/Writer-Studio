package com.creativeartie.jwriter.export;

import java.io.*;
import com.google.common.collect.*;

import static com.creativeartie.jwriter.main.Checker.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;

/** A list of {@link SpanBranch} with the same {@link CatalogueIdentity}. */
public final class PdfExporter implements Publisher{
    private Document exportDoc;
    public PdfExporter(File export){
        PdfWriter writer = new PdfWriter(export);

        PdfDocument pdf = new PdfDocument(writer);

        exportDoc = new Document(pdf);
    }

    public void addParagraph(FormatSpanMain format){
        Paragraph paragaraph;
        for (Span child: format){
        }
    }

    public void addHeading(FormatSpanMain format){

    }

    public void addSecionBreak(){
    }

    private ArrayList<ILeafElement> addPhrases(FormatSpan format){
        ArrayList<ILeafElement> ans;
        if (format instanceof FormatSpanContent){
            FormatSpanContent content = (FormatSpanContent) format;
            String text = content.getTrimmed();
            if (content.isSpaceBegin()){
                text += " " + text;
            }
            if (content.isSpaceEnd()){
                text += text + " ";
            }
            ans = new Text(text);
        } else if (format instanceof FormatSpanLink){
        }
        return ans;
    }

    @Override
    public void close(){
        exportDoc.close();
    }
}