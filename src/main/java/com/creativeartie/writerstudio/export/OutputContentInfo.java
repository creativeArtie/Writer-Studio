package com.creativeartie.writerstudio.export;

import java.util.*;

public class OutputContentInfo<T> {
    private final ContentData contentData;

    private final DataLineType lineType;
    private String fullText;
    private final String currentText;

    private int lineSplit;
    private String startText;
    private String endText;
    private T widthSpace;

    OutputContentInfo(ExportData<?> data, DataLineType type, T space){
        contentData = data.getContentData();

        lineType = data.getLineType();
        fullText = contentData.getText();
        currentText = data.getCurrentText();

        lineSplit = data.getLineSplit();
        startText = fullText;
        endText = "";

        widthSpace = space;
    }

    public int getLineSplit(){
        return lineSplit;
    }

    public void setLineSplit(int value){
        lineSplit = value;
    }

    public String getCurrentText(){
        return currentText;
    }

    String getStartText(){
        return startText;
    }

    public void setStartText(String value){
        startText = value;
    }

    public String getFullText(){
        return fullText;
    }

    void setFullText(String text){
        fullText = text;
    }

    String getEndText(){
        return endText;
    }

    public void setEndText(String value){
        endText = value;
    }

    public DataLineType getLineType(){
        return lineType;
    }

    public boolean isBold(){
        return contentData.isBold();
    }

    public boolean isItalics(){
        return contentData.isItalics();
    }

    public boolean isUnderline(){
        return contentData.isUnderline();
    }

    public boolean isCoded(){
        return contentData.isCoded();
    }

    public boolean isSuperscript(){
        return contentData.isSuperscript();
    }

    public Optional<String> getLinkPath(){
        return contentData.getLinkPath();
    }

    public T getWidthSpaces(){
        return widthSpace;
    }

}
