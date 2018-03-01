package com.creativeartie.jwriter.output;

import com.itextpdf.layout.renderer.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.*;
import com.itextpdf.kernel.geom.*;
import com.itextpdf.io.source.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.layout.*;

abstract class PdfPageRender{

    private final OutputInfo docInfo;
    private final PdfDocument outputDoc;
    private PdfPage curPage;
    private Rectangle pageSize;
    private PdfCanvas pageCanvas;

    private float topPadding;
    private float bottomPadding;

    PdfPageRender(OutputInfo info, PdfFileOutput file){
        docInfo = info;
        outputDoc = file.getPdfDocument();
        addNewPage();
    }

    void addNewPage(){
        curPage = outputDoc.addNewPage();
        pageSize = curPage.getCropBox();
        pageCanvas = new PdfCanvas(curPage.newContentStreamBefore(),
            curPage.getResources(), outputDoc);
    }


    OutputInfo getInfo(){
        return docInfo;
    }

    /// Code copied and modified from
    /// https://developers.itextpdf.com/examples/page-events/clone-page-events-headers-and-footers#2654-tableheader.java
    float getElementHeight(IBlockElement element){
        IRenderer renderer = element.createRendererSubTree();
        Document doc = new Document(new PdfDocument(new PdfWriter(
            new ByteArrayOutputStream()
        )));
        float margin = docInfo.getMargin();
        doc.setMargins(margin, margin, margin, margin);
        renderer.setParent(doc.getRenderer());
        return renderer
            .layout(new LayoutContext(new LayoutArea(0, PageSize.A4)))
            .getOccupiedArea().getBBox().getHeight();

    }

    void addBottom(Div div){
        float height = getElementHeight(div);
        new Canvas(pageCanvas, outputDoc, new Rectangle(
            pageSize.getX() + docInfo.getMargin(),
            pageSize.getY() + docInfo.getMargin(),
            pageSize.getWidth() - (docInfo.getMargin() * 2),
            height
        )).add(div);
    }

    void addTop(Div div){
        float height = getElementHeight(div);
        new Canvas(pageCanvas, outputDoc, new Rectangle(
            pageSize.getX() + docInfo.getMargin(),
            pageSize.getTop() - docInfo.getMargin() - height,
            getWidth(), height
        )).add(div);
    }

    void addCentre(Div div){
        float height = getElementHeight(div);
        new Canvas(pageCanvas, outputDoc, new Rectangle(
            pageSize.getX() + docInfo.getMargin(),
            pageSize.getHeight() / 2  - height / 2,
            getWidth(), height
        )).add(div);
    }

    void setTopPadding(Div div){
        topPadding = div == null? 0: getElementHeight(div);
        topPadding += docInfo.getMargin();
    }

    void setBottomPadding(Div div){
        bottomPadding = div == null? 0: getElementHeight(div);
        bottomPadding += getElementHeight(div) + docInfo.getMargin();
    }

    void addContent(Div div){
        float height = getElementHeight(div);
        new Canvas(pageCanvas, outputDoc, new Rectangle(
            pageSize.getX() + docInfo.getMargin(),
            pageSize.getTop() - docInfo.getMargin() - height,
            getWidth(), height
        )).add(div);
    }

    float getMaxContentHeight(){
        return pageSize.getHeight() - topPadding - bottomPadding -
            (docInfo.getMargin() * 2);
    }

    private float getWidth(){
        return pageSize.getWidth() - (docInfo.getMargin() * 2);
    }
}