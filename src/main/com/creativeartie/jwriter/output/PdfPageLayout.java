package com.creativeartie.jwriter.output;

import com.itextpdf.layout.renderer.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.*;
import com.itextpdf.kernel.geom.*;
import com.itextpdf.io.source.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.layout.*;

class PdfPageLayout{
    private Div footnoteDiv;
    private float baseMargin;
    private float outMargin;

    public PdfPageLayout(float margin){
        outMargin = baseMargin = margin;
        footnoteDiv = new Div();
    }

    void addFootnote(IBlockElement line){
        footnoteDiv.add(line);
        outMargin = PdfPageRender.getElementHeight(footnoteDiv, baseMargin) +
            baseMargin;
    }

    Div getFootnoteDiv(){
        return footnoteDiv;
    }

    @Override
    public String toString(){
        return outMargin + "";
    }

    float getMargin(){
        return outMargin;
    }
}