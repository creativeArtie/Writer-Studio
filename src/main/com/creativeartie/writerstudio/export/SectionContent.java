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
    private ManuscriptFile outputData;
    private MatterArea contentArea;
    private int pageNumber;

    public SectionContent(WritingExporter parent) throws IOException{
        super(parent);
        currentPage = new PageContent(this);
        pageNumber = 1;
        contentArea = null;
    }

    public int getPageNumber(){
        return pageNumber;
    }

    public PageContent getPage(){
        return currentPage;
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
        if (contentArea.checkHeight(div)){
            contentArea.add(div);
            return;
        }
        DivisionLine allows = DivisionLine.copyFormat(div);
        DivisionLine checker = DivisionLine.copyFormat(div);
        DivisionLine overflow = null;
        for (DivisionLine.Line line: div){
            checker.addLine(line);
            if (contentArea.checkHeight(checker)){
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
        currentPage.close();
    }
}