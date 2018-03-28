package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.common.*;

import com.creativeartie.writerstudio.export.value.*;

public class MatterArea extends ForwardingList<DivisionLine> {
    private PageContent outputPage;
    private PDPageContentStream contentStream;
    private ArrayList<DivisionLine> divisionLines;
    private final float maxHeight;
    private PageAlignment pageAlignment;

    private float fillHeight;

    private boolean hasStarted;
    private float localX;
    private float localY;
    private float areaWidth;
    private ContentFont textFont;
    private ArrayList<ContentPostEditor> postEditors;
    private LineAlignment lineAlignment;

    MatterArea(PageContent page, PageAlignment alignment){
        outputPage = page;
        hasStarted = false;
        contentStream = page.getContentStream();
        pageAlignment = alignment;
        textFont = null;
        areaWidth = page.getRenderWidth();
        maxHeight = page.getHeight();
        fillHeight = localX = localY = 0;
        divisionLines = new ArrayList<>();
        postEditors = new ArrayList<>();
        lineAlignment = LineAlignment.LEFT;
    }

    public boolean checkHeight(DivisionLine item){
        return item.getHeight() + fillHeight < maxHeight;
    }

    public boolean checkHeight(DivisionLine item, float footnote){
        return item.getHeight() + footnote + fillHeight < maxHeight;
    }


    MatterArea render() throws IOException{
        hasStarted = true;

        contentStream.beginText();

        /// Initital placement
        float leading = 0;
        float x = outputPage.getStartX();
        if (! isEmpty()){
            DivisionLine child = get(0);
            if (! child.isEmpty()){
                leading = child.get(0).getTextHeight() * (child.getLeading() - 1);
            }
        }
        float y = outputPage.getStartY(pageAlignment, this) + leading;
        moveText(x, y);

        /// show text
        for (DivisionLine block: this){
            changeAlign(block.getLineAlignment());
            if (block.getPrefix().isPresent()){
                /// move back to print prefix
                moveText(block.getPrefixDistance(), 0);
                contentStream.showText(block.getPrefix().get());
                /// move forward to print text
                moveText(-block.getPrefixDistance(), 0);
            }
            for (DivisionLine.Line line: block){
                /// move to indent
                moveText(line.getIndent(), 0);
                printText(line);
                /// move to next line
                moveText(0, -line.getHeight());
                /// move to remove indent
                moveText(-line.getIndent(), 0);
            }
        }
        contentStream.endText();
        for (ContentPostEditor consumer: postEditors){
            consumer.edit(outputPage.getPage(), contentStream);
        }
        return this;
    }

    private void changeAlign(LineAlignment next) throws IOException{
        if (lineAlignment == next){
            return;
        }
        switch (lineAlignment){
        case CENTER:
            switch(next){
            case RIGHT:  moveText((areaWidth / 2), 0); break;
            case CENTER: assert false;                    break;
            default:     moveText(-(areaWidth / 2), 0);
            } break;
        case RIGHT:
            switch(next){
            case RIGHT:  assert false;                  break;
            case CENTER: moveText(-(areaWidth / 2), 0); break;
            default:     moveText(-areaWidth, 0);
            } break;
        default: switch (next){
            case RIGHT:  moveText(areaWidth, 0);     break;
            case CENTER: moveText(areaWidth / 2, 0); break;
            default:     moveText(0, 0);
            }
        }
        lineAlignment = next;
    }

    private void printText(DivisionLine.Line line) throws IOException{
        /// Center and set right
        if (lineAlignment == LineAlignment.RIGHT){
            moveText(-line.getWidth(), 0);
        } else if (lineAlignment == LineAlignment.CENTER){
           moveText(-(line.getWidth() / 2), 0);
        }
        float textLocalX = localX;
        for (ContentText text: line){
            PDRectangle rect = new PDRectangle(textLocalX, localY,
                text.getWidth(), text.getHeight());
            changeFont(text.getFont());
            contentStream.showText(text.getText());
            postEditors.addAll(text.getPostTextConsumers(rect));
            textLocalX += text.getWidth();
        }
        if (lineAlignment == LineAlignment.RIGHT){
            moveText(line.getWidth(), 0);
        } else if (lineAlignment == LineAlignment.CENTER){
            moveText(line.getWidth() / 2, 0);
        }
    }

    private void changeFont(ContentFont font) throws IOException{
        if (textFont == null || ! font.equals(textFont)){
            contentStream.setNonStrokingColor(font.getColor());
            contentStream.setFont(font.getFont(), font.getSize());
            textFont = font;
        }
    }

    private void moveText(float x, float y) throws IOException{
        localX += x;
        localY += y;
        contentStream.newLineAtOffset(x, y);
    }

    @Override
    public void add(int index, DivisionLine item){
        divisionLines.add(index, item);
        fillHeight += item.getHeight();
    }

    @Override
    public boolean add(DivisionLine line){
        return standardAdd(line);
    }

    @Override
    public boolean addAll(Collection<? extends DivisionLine> c) {
        return standardAddAll(c);
    }

    @Override
    protected List<DivisionLine> delegate(){
        return divisionLines;
    }
}