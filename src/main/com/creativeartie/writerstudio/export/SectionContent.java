package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;
import java.awt.*;

import org.apache.pdfbox.pdmodel.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.export.value.*;

public abstract class SectionContent<T extends SpanBranch> extends Section {
    private PageContent currentPage;
    private PageFootnote pageFootnote;
    private ManuscriptFile outputData;
    private MatterArea contentArea;
    private int pageNumber;

    public SectionContent(WritingExporter parent) throws IOException{
        super(parent);
        currentPage = new PageContent(this);
        pageFootnote = new PageFootnote(this);
        pageNumber = 1;
        contentArea = null;
    }

    public int getPageNumber(){
        return pageNumber;
    }

    public PageContent getPage(){
        return currentPage;
    }

    public PageFootnote getFootnote(){
        return pageFootnote;
    }

    public void addHeader(ManuscriptFile data) throws IOException{
        outputData = data;
        addHeader();
    }

    private void addHeader() throws IOException{
        MatterArea header = parseHeader(outputData);
        if (header != null){
            currentPage.setHeader(header);
            header.render();
        }
    }

    protected abstract MatterArea parseHeader(ManuscriptFile data)
        throws IOException;

    void addLine(T span) throws IOException{
        DivisionLine found = parseSpan(span);
        if (found != null){
            addLine(found);
        }
    }

    protected abstract DivisionLine parseSpan(T span) throws IOException;

    String addFootnote(LinedSpan note) throws IOException{
        return Utilities.toNumberSuperscript(1);
    }

    private void addLine(DivisionLine div) throws IOException{
        if (contentArea == null){
            nextPage(PageAlignment.CONTENT);
        }
        float footnote = pageFootnote.getHeight();
        if (contentArea.checkHeight(div, footnote)){
            contentArea.add(div);
            pageFootnote.insertAll();
            return;
        }
        DivisionLine allows = DivisionLine.copyFormat(div);
        DivisionLine checker = DivisionLine.copyFormat(div);
        DivisionLine overflow = null;
        for (DivisionLine.Line line: div){
            footnote = pageFootnote.getHeight(line);
            checker.addLine(line);
            if (contentArea.checkHeight(checker, footnote)){
                allows.addLine(line);
            } else {
                if (overflow == null){
                    if(allows.isEmpty()){
                        nextPage(PageAlignment.CONTENT);
                        addLine(div);
                        return;
                    }
                    overflow = DivisionLine.splitItem(div);
                }
                overflow.addLine(line);
            }
        }
        if (! allows.isEmpty()){
            contentArea.add(allows);
        }
        nextPage(PageAlignment.CONTENT);
        addLine(overflow);
    }

    protected void nextPage(PageAlignment alignment) throws IOException{
        if (contentArea == null){
            contentArea = new MatterArea(currentPage, alignment);
            return;
        }
        contentArea.render();
        pageFootnote.nextPage().render();
        currentPage.close();

        pageNumber++;

        currentPage = new PageContent(this);
        addHeader();
        contentArea = new MatterArea(currentPage, alignment);
    }

    protected DivisionLineFormatted newFormatDivision(){
        return new DivisionLineFormatted(this, getParent());
    }

    @Override
    public void close() throws IOException{
        if (contentArea != null) contentArea.render();
        pageFootnote.nextPage().render();
        currentPage.close();
    }
}