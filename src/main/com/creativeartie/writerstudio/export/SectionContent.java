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

public class SectionContent extends Section {
    private PageContent currentPage;
    private ManuscriptFile outputData;
    private MatterArea contentArea;
    private int pageNumber;
    private boolean paraFirst;
    private LinkedList<Integer> listNumbering;
    private ArrayList<LinedSpanPointNote> endnoteList;

    public SectionContent(WritingExporter parent) throws IOException{
        super(parent);
        currentPage = new PageContent(this);
        pageNumber = 1;
        paraFirst = true;
        listNumbering = new LinkedList<>();
        endnoteList = new ArrayList<>();
        paraFirst = true;
        contentArea = null;
    }

    public void addHeader(ManuscriptFile data) throws IOException{
        outputData = data;
        addHeader();
    }

    private void addHeader() throws IOException{
        MatterArea header = new MatterArea(currentPage, PageAlignment.TOP);
        header.add(new DivisionLine(
                currentPage.getRenderWidth(), LineAlignment.RIGHT
            ).setLeading(1)
            .appendSimpleText(outputData.getText(MetaData.LAST_NAME) + "/" +
                outputData.getText(MetaData.TITLE) + "/" +
                pageNumber, getParent().new PdfFont()
            )
        );
        currentPage.setHeader(header);
        header.render();
    }

    void addLine(LinedSpan span) throws IOException{
        DivisionLine line = null;
        switch(span.getLinedType()){
        case HEADING:
            line = parse((LinedSpanLevelSection) span);
            break;
        case PARAGRAPH:
            line = parse((LinedSpanParagraph) span);
            break;
        }
        if (line != null){
            addLine(line);
        }
    }

    private void addLine(DivisionLine div) throws IOException{
        if (contentArea == null){
            contentArea = new MatterArea(currentPage, PageAlignment.CONTENT);
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

    private void nextPage(PageAlignment alignment) throws IOException{
        contentArea.render();
        currentPage.close();

        pageNumber++;

        currentPage = new PageContent(this);
        addHeader();
        contentArea = new MatterArea(currentPage, alignment);
    }

    private DivisionLine parse(LinedSpanLevelSection span) throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();
        if (format.isPresent() && ! format.get().isEmpty()){
            paraFirst = true;
            return new DivisionLineFormatted(currentPage.getRenderWidth(),
                getParent()).addContent(format.get());
        }
        return null;
    }

    private DivisionLine parse(LinedSpanParagraph span) throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();
        if (format.isPresent() && ! format.get().isEmpty()){
            DivisionLineFormatted ans = new DivisionLineFormatted(
                    currentPage.getRenderWidth(), getParent()
                );
            if (! paraFirst){
                ans.setFirstIndent(Utilities.cmToPoint(1.25f));
            } else {
                paraFirst = false;
            }
            return ans.addContent(format.get());
        }
        return null;
    }


    @Override
    public void close() throws IOException{
        if (contentArea != null) contentArea.render();
        currentPage.close();
    }
}