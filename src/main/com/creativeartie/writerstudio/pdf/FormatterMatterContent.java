package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Prints text on a page with ability to change height and detect overflow.
 */
class FormatterMatterContent extends FormatterMatter{
    private Margin baseMargins;
    private ArrayList<FormatterItem> outputLines;
    private FormatterItem currentLine;

    private boolean baisicUnprepared;
    private float fillHeight;
    private float divWidth;
    private float divHeight;
    private float startY;
    private float startX;

    public FormatterMatterContent(){
        baseMargins = null;
        outputLines = new ArrayList<>();
        currentLine = null;
        divWidth = 0;
        divHeight = 0;
        baisicUnprepared = true;
        fillHeight = 0;
    }

    public FormatterMatterContent setBasics(DataContent content, StreamData output){
        baseMargins = content.getMargin();
        startY = output.getHeight() - baseMargins.getTop();
        startX = baseMargins.getLeft();
        divWidth = output.getRenderWidth(baseMargins);
        divHeight = output.getRenderHeight(content.getMargin());
        baisicUnprepared = false;
        return this;
    }

    public FormatterMatterContent addHeaderSpacing(float height){
        startY -= height;
        divHeight -= height;
        return this;
    }

    public FormatterMatterContent setStartY(float start){
        float diff = startY - start;
        startY = start;
        divHeight -= diff;
        return this;
    }

    public boolean canFit(FormatterItem item){
        return item.getHeight() + fillHeight < divHeight;
    }

    public boolean canFit(FormatterItem item, float footnote){
        return item.getHeight() + footnote + fillHeight < divHeight;
    }

    public void addContentLine(FormatterItem item){
        outputLines.add(item);
        fillHeight += item.getHeight();
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
    public float getStartY(){
        isReady();
        return startY;
    }

    @Override
    protected List<FormatterItem> delegate(){
        return ImmutableList.copyOf(outputLines);
    }

}
