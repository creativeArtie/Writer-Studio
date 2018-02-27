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
import com.itextpdf.kernel.pdf.canvas.draw.*;
import com.itextpdf.kernel.pdf.navigation.*;

class PdfSectionHandler{
    private PdfDocument baseDoc;

    public PdfSectionHandler(PdfDocument doc){
        baseDoc = doc;
    }

    public void addHeading(LinedSpanLevelSection heading, Paragraph location){
        if (heading.getLevel() == 1){
            System.out.println(heading.getTitle());
            PdfOutline outline = baseDoc.getOutlines(false);
            PdfOutline child = outline.addOutline(heading.getTitle());
            child.addAction(PdfAction.createGoTo(
                PdfExplicitDestination.createFitH(baseDoc.getLastPage(),
                        baseDoc.getLastPage().getPageSize().getTop())));
        }
    }
}