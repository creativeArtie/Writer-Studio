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

public class SectionContentMain extends SectionContent<LinedSpan> {
    private boolean paraFirst;
    private LinkedList<Integer> listNumbering;

    public SectionContentMain(WritingExporter parent) throws IOException{
        super(parent);
        paraFirst = true;
        listNumbering = new LinkedList<>();
    }

    @Override
    protected MatterArea parseHeader(ManuscriptFile data) throws IOException{
        MatterArea header = new MatterArea(getPage(), PageAlignment.TOP);
        header.add(new DivisionText(
                getPage().getRenderWidth(), LineAlignment.RIGHT
            ).setLeading(1)
            .appendSimpleText(data.getText(MetaData.LAST_NAME) + "/" +
                data.getText(MetaData.TITLE) + "/" +
                getPageNumber(), getParent().new PdfFont()
            )
        );
        return header;
    }

    @Override
    protected DivisionText parseSpan(LinedSpan span) throws IOException{
        boolean clear = true;
        DivisionText line = null;
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
        if (line != null && clear){
            listNumbering.clear();
        }
        return line;
    }

    private DivisionText addBreak() throws IOException{
        DivisionText ans = newFormatDivision();
        ans.setLineAlignment(LineAlignment.CENTER);
        ans.appendText("#", newFont());
        paraFirst = true;
        return ans;
    }

    private DivisionText parseBullet(LinedSpanLevelList span)
            throws IOException{
        return parse(span, "•");
    }

    private DivisionText parseNumber(LinedSpanLevelList span)
            throws IOException{
        int level = span.getLevel();
        while (listNumbering.size() > level){
            listNumbering.pop();
        }
        while (listNumbering.size() < level){
            listNumbering.push(1);
        }
        int count = listNumbering.pop();
        DivisionText ans = parse(span, count + ".");
        listNumbering.push(count + (ans == null? 0: 1));
        return ans;
    }

    private DivisionTextFormatted parse(LinedSpanLevelList span, String prefix)
            throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();
        if (format.isPresent() && ! format.get().isEmpty()){
            DivisionTextFormatted line = newFormatDivision();
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
    private DivisionText parse(LinedSpanLevelSection span) throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();
        if (format.isPresent() && ! format.get().isEmpty()){
            paraFirst = true;
            DivisionTextFormatted ans = newFormatDivision();
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

    private DivisionText parse(LinedSpanParagraph span) throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();

        if (format.isPresent() && ! format.get().isEmpty()){
            DivisionTextFormatted ans = newFormatDivision();
            if (! paraFirst){
                ans.setFirstIndent(Utilities.cmToPoint(1.25f));
            } else {
                paraFirst = false;
            }
            return ans.addContent(format.get());
        }
        return null;
    }

    private DivisionTextFormatted parse(LinedSpanQuote span)
            throws IOException{
        Optional<FormatSpanMain> format = span.getFormattedSpan();
        if (format.isPresent() && ! format.get().isEmpty()){
            DivisionTextFormatted ans = newFormatDivision();
            ans.setWidth(ans.getWidth() - Utilities.cmToPoint(2f));
            ans.setFirstIndent(Utilities.cmToPoint(2f));
            ans.setIndent(Utilities.cmToPoint(2f));
            paraFirst = false;
            return ans.addContent(format.get());
        }
        return null;
    }

}