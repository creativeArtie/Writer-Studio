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
        boolean clear = true;
        DivisionLine line = null;
        switch(span.getLinedType()){
        case BREAK:
            line = addBreak();
            break;
        case BULLET:
            line = parseBullet((LinedSpanLevelList) span);
            break;
        case NUMBERED:
            line = parseNumber((LinedSpanLevelList) span);
            clear = false;
            break;
        case HEADING:
            line = parse((LinedSpanLevelSection) span);
            break;
        case PARAGRAPH:
            line = parse((LinedSpanParagraph) span);
            break;
        case QUOTE:
            line = parse((LinedSpanQuote) span);
        }
        if (line != null){
            addLine(line);
            if (clear){
                listNumbering.clear();
            }
        }
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

    private void nextPage(PageAlignment alignment) throws IOException{
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

    private DivisionLine addBreak() throws IOException{
        DivisionLine ans = newFormatDivision();
        ans.setLineAlignment(LineAlignment.CENTER);
        ans.appendText("#", newFont());
        paraFirst = true;
        return ans;
    }

    private DivisionLine parseBullet(LinedSpanLevelList span)
            throws IOException{
        return parse(span, "•");
    }

    private DivisionLine parseNumber(LinedSpanLevelList span)
            throws IOException{
        int level = span.getLevel();
        while (listNumbering.size() > level){
            listNumbering.pop();
        }
        while (listNumbering.size() < level){
            listNumbering.push(1);
        }
        int count = listNumbering.pop();
        DivisionLine ans = parse(span, count + ".");
        listNumbering.push(++count);
        return ans;
    }

    private DivisionLineFormatted parse(LinedSpanLevelList span, String prefix)
            throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();
        if (format.isPresent() && ! format.get().isEmpty()){
            DivisionLineFormatted line = newFormatDivision();
            float indent = Utilities.cmToPoint(.5f) +
                (Utilities.cmToPoint(.5f) * span.getLevel());
            line.setFirstIndent(indent);
            line.setIndent(indent);
            line.setPrefix(prefix, indent - Utilities.cmToPoint(.75f));
            paraFirst = false;
            return line.addContent(format.get());
        }
        return null;
    }

    @SuppressWarnings("fallthrough")
    private DivisionLine parse(LinedSpanLevelSection span) throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();
        if (format.isPresent() && ! format.get().isEmpty()){
            paraFirst = true;
            DivisionLineFormatted ans = newFormatDivision();
            switch (span.getLevel()){
            case 1:
                nextPage(PageAlignment.THIRD);
            case 2:
                ans.setLineAlignment(LineAlignment.CENTER);
                ans.setBottomSpacing(Utilities.cmToPoint(1f));
                break;
            case 3:
                ans.setBottomSpacing(Utilities.cmToPoint(.8f));
                ans.setLineAlignment(LineAlignment.CENTER);
                break;
            case 4:
                ans.setBottomSpacing(Utilities.cmToPoint(.6f));
                ans.setLineAlignment(LineAlignment.CENTER);
                break;
            case 5:
                ans.setBottomSpacing(Utilities.cmToPoint(.4f));
            default:
                assert span.getLevel() == 6;
                ans.setLineAlignment(LineAlignment.CENTER);

            }
            return ans.addContent(format.get());
        }
        return null;
    }

    private DivisionLine parse(LinedSpanParagraph span) throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();
        if (format.isPresent() && ! format.get().isEmpty()){
            DivisionLineFormatted ans = newFormatDivision();
            if (! paraFirst){
                ans.setFirstIndent(Utilities.cmToPoint(1.25f));
            } else {
                paraFirst = false;
            }
            return ans.addContent(format.get());
        }
        return null;
    }

    private DivisionLineFormatted parse(LinedSpanQuote span)
            throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();
        if (format.isPresent() && ! format.get().isEmpty()){
            DivisionLineFormatted ans = newFormatDivision();
            ans.setWidth(ans.getWidth() - Utilities.cmToPoint(2f));
            ans.setFirstIndent(Utilities.cmToPoint(2f));
            ans.setIndent(Utilities.cmToPoint(2f));
            paraFirst = false;
            return ans.addContent(format.get());
        }
        return null;
    }

    private DivisionLineFormatted newFormatDivision(){
        return new DivisionLineFormatted(currentPage.getRenderWidth(),
            getParent());
    }


    @Override
    public void close() throws IOException{
        if (contentArea != null) contentArea.render();
        currentPage.close();
    }
}