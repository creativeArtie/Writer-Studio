package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Prints text on a page with ability to change height and detect overflow.
 */
class PdfMatterContent extends PdfMatter{
    private Margin baseMargins;
    private ArrayList<PdfItem> outputLines;
    private PdfItem currentLine;

    private boolean baisicUnprepared;
    private float fillHeight;
    private float divWidth;
    private float divHeight;
    private float startY;
    private float startX;

    public PdfMatterContent(){
        baseMargins = null;
        outputLines = new ArrayList<>();
        currentLine = null;
        divWidth = 0;
        divHeight = 0;
        baisicUnprepared = true;
        fillHeight = 0;
    }

    public PdfMatterContent setBasics(Input content, StreamData output){
        baseMargins = content.getMargin();
        startY = output.getHeight() - baseMargins.getTop();
        startX = baseMargins.getLeft();
        divWidth = output.getRenderWidth(baseMargins);
        divHeight = output.getRenderHeight(content.getMargin());
        baisicUnprepared = false;
        return this;
    }

    public PdfMatterContent addHeaderSpacing(float height){
        startY -= height;
        divHeight -= height;
        return this;
    }

    public boolean canFit(PdfItem item){
        return item.getHeight() + fillHeight < divHeight;
    }

    public boolean addContentLine(PdfItem item){
        if (item.getHeight() + fillHeight < divHeight){
            outputLines.add(item);
            fillHeight += item.getHeight();
            return true;
        }
        return false;
        /*
        PdfItem last = new PdfItem(item.getWidth());
        Optional<PdfItem> ans = Optional.empty();
        for (PdfItem.Line line: item){
            if (ans.isPresent()){
                ans.get().addLine(line);
            } else {
                if (line.getHeight() + fillHeight < divHeight){
                    last.addLine(line);
                    fillHeight += line.getHeight();
                } else {
                    ans = Optional.of(new PdfItem(item.getWidth()));
                    ans.get().addLine(line);
                }
            }
        }
        outputLines.add(last);
        return ans;*/
    }

    private void isReady(){
        if (baisicUnprepared){
            throw new IllegalStateException("addBasics(...) had not be called");
        }
    }

    @Override
    public float getHeight(){
        return divHeight;
    }

    @Override
    float getWidth(){
        isReady();
        return divWidth;
    }

    @Override
    public float getXLocation(){
        isReady();
        return startX;
    }

    @Override
    public float getYLocation(){
        isReady();
        return startY;
    }

    @Override
    protected List<PdfItem> delegate(){
        return ImmutableList.copyOf(outputLines);
    }

}