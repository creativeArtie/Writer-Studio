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
import com.itextpdf.kernel.pdf.navigation.*;

class PdfFrontPageHandler{

    private Rectangle pageSize;
    private PdfCanvas pageCanvas;
    private PdfDocument outputDoc;
    private float leftMargin;
    private float topMargin;
    private float rightMargin;
    private float bottomMargin;

    public PdfFrontPageHandler(PdfPage page, PdfDocument doc, Document margins){
        pageSize = page.getPageSize();
        pageCanvas = new PdfCanvas(page.newContentStreamBefore(),
            page.getResources(), doc);
        outputDoc = doc;
        leftMargin = margins.getLeftMargin();
        topMargin = margins.getTopMargin();
        rightMargin = margins.getRightMargin();
        bottomMargin = margins.getBottomMargin();
    }

    public void addBottom(Div div){
        float height = PdfBase.getElementHeight(div);
        new Canvas(pageCanvas, outputDoc, new Rectangle(
            pageSize.getX() + leftMargin,
            pageSize.getY() + bottomMargin,
            pageSize.getWidth() - leftMargin - rightMargin,
            height
        )).add(div);
    }

    public void addTop(Div div){
        float height = PdfBase.getElementHeight(div);
        new Canvas(pageCanvas, outputDoc, new Rectangle(
            pageSize.getX() + leftMargin,
            pageSize.getTop() - topMargin - height,
            getWidth(), height
        )).add(div);
    }

    public void addCentre(Div div){
        float height = PdfBase.getElementHeight(div);
        new Canvas(pageCanvas, outputDoc, new Rectangle(
            pageSize.getX() + leftMargin,
            pageSize.getHeight() / 2  - height / 2,
            getWidth(), height
        )).add(div);
    }

    private float getWidth(){
        return pageSize.getWidth() - leftMargin - rightMargin;
    }

}