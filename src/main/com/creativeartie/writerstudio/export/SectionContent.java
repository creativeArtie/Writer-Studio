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
    private FootnoteItem footnoteAdding;
    private ArrayList<FootnoteItem> footnoteList;
    private MatterArea footnoteArea;

    public SectionContent(WritingExporter parent) throws IOException{
        super(parent);
        currentPage = new PageContent(this);
        pageNumber = 1;
        contentArea = null;
        footnoteArea = null;
        footnoteList = new ArrayList<>();
        footnoteAdding = null;
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
        int index = FootnoteItem.getSpanIndex(note, footnoteList);
        
        if (index == -1){
            index = footnoteList.size();
        }
        DivisionLineFormatted item = newFormatDivision();
        item.appendSimpleText(
            Utilities.toNumberSuperscript(index),
            newFont().changeToSuperscript()
        );
        
        if (note instanceof LinedSpanPointNote){
            Optional<FormatSpanMain> format = ((LinedSpanPointNote)note)
                .getFormattedSpan();
            if (format.isPresent()){
                item.addContent(format.get());
            }
        }
        
        if (footnoteArea != null) {
            item.setLeading(1);
        }
        
        if (index == -1){
            footnoteList.add(footnoteAdding);
        }
        footnoteAdding = new FootnoteItem(note, item);
        return Utilities.toNumberSuperscript(index + 1);
    }
    
    Optional<FootnoteItem> popFootnote(){
        Optional<FootnoteItem> ans = Optional.ofNullable(footnoteAdding);
        footnoteAdding = null;
        return ans;
    }

    private void addLine(DivisionLine div) throws IOException{
        if (contentArea == null){
            nextPage(PageAlignment.CONTENT);
        }
        if (contentArea.checkHeight(div)){
            contentArea.add(div);
            if (footnoteArea == null){
                footnoteArea = new MatterArea(currentPage, PageAlignment
                    .BOTTOM);
            }
            div.addFootnote(footnoteArea);
            return;
        }
        DivisionLine allows = DivisionLine.copyFormat(div);
        DivisionLine checker = DivisionLine.copyFormat(div);
        DivisionLine overflow = null;
        int i = 0;
        for (DivisionLine.Line line: div){
            checker.addLine(line);
            if (contentArea.checkHeight(checker, footnoteArea.getHeight())){
                allows.addLine(line);
                i++;
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
        // footnoteArea.render();
        footnoteArea = null;
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