package com.creativeartie.writerstudio.export;

import java.io.*; // IOException
import java.util.*; // ArrayList, LinkedList, Optional

import com.creativeartie.writerstudio.file.*; // ManuscriptFile
import com.creativeartie.writerstudio.lang.markup.*; // (many)
import com.creativeartie.writerstudio.export.value.*; // LineAlginment, PageAignment, Utitlies

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
        ArrayList<DivisionTextFormatted> lines = new ArrayList<>();
        for (TextDataSpanPrint print: data.getMetaData()
                .getPrint(TextDataType.Area.MAIN_HEADER)){
            lines.add(new DivisionTextFormatted(this).addContent(print));
        }
        header.addAll(lines);
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
        DivisionTextFormatted ans = newFormatDivision();
        List<TextDataSpanPrint> data = getOutputData().getMetaData()
            .getPrint(TextDataType.Area.MAIN_BREAK);
        if (! data.isEmpty()){
            ans.addContent(data.get(0));
        }
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
        Optional<FormattedSpan> format = span.getFormattedSpan();
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
        Optional<FormattedSpan> format = span.getFormattedSpan();
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
        Optional<FormattedSpan> format = span.getFormattedSpan();

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
        Optional<FormattedSpan> format = span.getFormattedSpan();
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