package com.creativeartie.writerstudio.export;

import java.io.*; // IOException

import com.creativeartie.writerstudio.export.value.*; // PageAlignment
import com.creativeartie.writerstudio.file.*; // ManuscriptFile
import com.creativeartie.writerstudio.lang.*; // SpanBranch

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
        DivisionText found = parseSpan(span);
        if (found != null){
            addLine(found);
        }
    }

    protected abstract DivisionText parseSpan(T span) throws IOException;

    void addLine(DivisionText div) throws IOException{
        if (contentArea == null){
            nextPage(PageAlignment.CONTENT);
        }
        float footnote = pageFootnote.getHeight();
        if (contentArea.checkHeight(div, footnote)){
            contentArea.add(div);
            pageFootnote.insertAll();
            return;
        }
        DivisionText allows = DivisionText.copyFormat(div);
        DivisionText checker = DivisionText.copyFormat(div);
        DivisionText overflow = null;
        for (DivisionText.Line line: div){
            footnote = pageFootnote.getHeight(line);
            checker.addLine(line);
            if (contentArea.checkHeight(checker, footnote)){
                allows.addLine(line);
                pageFootnote.insertPending(line);
            } else {
                if (overflow == null){
                    if(allows.isEmpty()){
                        nextPage(PageAlignment.CONTENT);
                        addLine(div);
                        return;
                    }
                    overflow = DivisionText.splitItem(div);
                }
                for (ContentText content: overflow.addLine(line)){
                    pageFootnote.resetFootnote(content);
                }
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

    protected DivisionTextFormatted newFormatDivision(){
        return new DivisionTextFormatted(this);
    }

    @Override
    public void close() throws IOException{
        if (contentArea != null) contentArea.render();
        pageFootnote.nextPage().render();
        currentPage.close();
    }
}