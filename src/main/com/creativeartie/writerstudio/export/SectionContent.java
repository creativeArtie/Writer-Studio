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

    public void addLine(LinedSpan span) throws IOException{
        DivisionLine add = renderLine(span);
        if (add != null){
            addContent(add);
        }
    }

    private void addContent(DivisionLine item) throws IOException{
        if (contentArea == null){
            contentArea = new MatterArea(currentPage, PageAlignment.CONTENT);
        }
        if (contentArea.checkHeight(item)){
            contentArea.add(item);
            return;
        }
        DivisionLine allows = DivisionLine.copyFormat(item);
        DivisionLine checker = DivisionLine.copyFormat(item);
        DivisionLine overflow = null;
        for (DivisionLine.Line line: item){
            checker.addLine(line);
            if (contentArea.checkHeight(checker)){
                allows.addLine(line);
            } else {
                if (overflow == null){
                    if(allows.isEmpty()){
                        nextPage(PageAlignment.CONTENT);
                        addContent(item);
                    }
                    overflow = DivisionLine.splitItem(item);
                }
                overflow.addLine(line);
            }
        }
        if (! allows.isEmpty()){
            contentArea.add(allows);
        }

        nextPage(PageAlignment.CONTENT);
        addContent(overflow);
    }

    private DivisionLine newLine(){
        return newLine(LineAlignment.LEFT);
    }
    private DivisionLine newLine(LineAlignment alignment){
        return new DivisionLine(currentPage.getRenderWidth(), alignment);
    }

    private DivisionLine renderLine(LinedSpan child) throws IOException{
        if (child == null){
            return new DivisionLine(
                currentPage.getRenderWidth(), LineAlignment.CENTER
            ).appendSimpleText("THE END", getParent().new PdfFont());
        }
        switch(child.getLinedType()){
        case BREAK:
            DivisionLine line = newLine(LineAlignment.CENTER);
            line.appendText("#", newFont());
            paraFirst = true;
            return line;
        case BULLET:
            return parseBullet((LinedSpanLevelList) child);
        case NUMBERED:
            return parseNumber((LinedSpanLevelList) child);
        case HEADING:
            return parse((LinedSpanLevelSection) child);
        case PARAGRAPH:
            return parse((LinedSpanParagraph) child);
        case QUOTE:
            return parse((LinedSpanQuote) child);
        }
        return null;
    }

    private DivisionLine parseBullet(LinedSpanLevelList line)
            throws IOException{
        return parse(line, "•");
    }

    private DivisionLine parseNumber(LinedSpanLevelList line)
            throws IOException{
        int level = line.getLevel();
        while (listNumbering.size() > level){
            listNumbering.pop();
        }
        while (listNumbering.size() < level){
            listNumbering.push(1);
        }
        int count = listNumbering.pop();
        DivisionLine ans = parse(line, count + ".");
        listNumbering.push(++count);
        return ans;
    }

    private DivisionLine parse(LinedSpanLevelList line, String prefix)
            throws IOException{
        float indent = Utilities.cmToPoint(.5f) + (Utilities.cmToPoint(.5f) *
            line.getLevel());
        DivisionLine item = newLine();
        item.setFirstIndent(indent);
        item.setIndent(indent);
        item.setPrefix(prefix, indent - Utilities.cmToPoint(.75f));
        Optional<FormatSpanMain> span = line.getFormattedSpan();
        if (! span.isPresent()){
            return null;
        }
        parseItem(span.get(), item);
        paraFirst = false;
        return item;
    }


    private DivisionLine parse(LinedSpanLevelSection line)
            throws IOException{
        Optional<FormatSpanMain> span = line.getFormattedSpan();
        if (! span.isPresent()){
            return null;
        }
        DivisionLine item = newLine(LineAlignment.CENTER);
        parseItem(span.get(), item);
        if (line.getLevel() == 1){
            item.setBottomSpacing(Utilities.cmToPoint(1f));
            if (contentArea == null){
                contentArea = new MatterArea(currentPage, PageAlignment.THIRD);
            } else {
                nextPage(PageAlignment.THIRD);
            }
        }
        return item;
    }

    private DivisionLine parse(LinedSpanParagraph line) throws IOException{
        Optional<FormatSpanMain> span = line.getFormattedSpan();
        if (! span.isPresent()){
            return null;
        }
        DivisionLine ans = newLine();
        if (! paraFirst) {
            ans.setFirstIndent(Utilities.cmToPoint(1.25f));
        }
        parseItem(span.get(), ans);
        paraFirst = false;
        return ans;
    }

    private DivisionLine parse(LinedSpanQuote quote) throws IOException{
        DivisionLine ans = newLine();
        ans = new DivisionLine(currentPage.getRenderWidth() -
            Utilities.cmToPoint(2f));
        ans.setFirstIndent(Utilities.cmToPoint(2f));
        ans.setIndent(Utilities.cmToPoint(2f));

        Optional<FormatSpanMain> span = quote.getFormattedSpan();
        if (! span.isPresent()){
            return null;
        }
        parseItem(span.get(), ans);
        paraFirst = false;
        return ans;
    }

    private Optional<DivisionLine> parseItem(FormatSpanMain span)
            throws IOException{
        return parseItem(span, newLine());
    }
    private Optional<DivisionLine> parseItem(FormatSpanMain span,
            DivisionLine item) throws IOException{
        ContentFont font = newFont();
        for (Span child: span){
            if (child instanceof FormatSpan){
                FormatSpan format = (FormatSpan) child;
                String text = getText(format);
                ContentFont add = font;
                if (format.isCoded()){
                    add = add.changeToMono();
                }
                add = add.changeBold(format.isBold());
                add = add.changeItalics(format.isItalics());
                add = add.changeUnderline(format.isUnderline());
                if (child instanceof FormatSpanLink){
                    add = add.changeFontColor(Color.BLUE);
                }
                if (child instanceof FormatSpanDirectory){
                    add = add.changeToSuperscript();
                }
                setAddition(item.appendText(text, add), format);
            }
        }
        if (item.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(item);
        }
    }

    private String getText(FormatSpan span){
        if (span instanceof FormatSpanContent){
            return ((FormatSpanContent) span).getText();
        } else if (span instanceof FormatSpanLink){
            return ((FormatSpanLink)span).getText();
        } else if (span instanceof FormatSpanDirectory){
            FormatSpanDirectory ref = (FormatSpanDirectory) span;
            Optional<SpanBranch> base = ref.getTarget();
            Optional<LinedSpanPointNote> note = base
                .filter(t -> t instanceof LinedSpanPointNote)
                .map(t -> (LinedSpanPointNote) t);
            Optional<String> text = note
                .filter(t -> t.getDirectoryType() == DirectoryType.ENDNOTE)
                .map(t -> getParent().addEndnote(t));
            return text.orElse(span.getRaw());
        }
        return span.getRaw();
    }

    private void setAddition(ArrayList<ContentText> formatter,
            FormatSpan span){
        if (span instanceof FormatSpanLinkDirect){
            String path = ((FormatSpanLinkDirect)span).getPath();
            for (ContentText data: formatter){
                data.setLinkPath(path);
            }
        } else if (span instanceof FormatSpanLinkRef){
            Optional<SpanBranch> target = ((FormatSpanLinkRef) span)
                .getPathSpan();
            target.filter(f -> f instanceof LinedSpanPointLink)
                .map(s -> (LinedSpanPointLink) s)
                .ifPresent(s ->
                    formatter.forEach(c -> c.setLinkPath(s.getPath()))
                );
        }
    }

    private void nextPage(PageAlignment alignment) throws IOException{
        contentArea.render();
        currentPage.close();

        currentPage = new PageContent(this);
        contentArea = new MatterArea(currentPage, alignment);
    }

    @Override
    public void close() throws IOException{
        currentPage.close();
    }
}