package com.creativeartie.writerstudio.export;

import java.util.*;

public class OutputContentInfo {
    private final ContentData contentData;

    private final DataLineType lineType;
    private final String fullText;

    private int lineSplit;
    private String startText;
    private String endText;

    OutputContentInfo(ExportData<?> data, DataLineType type){
        contentData = data.getContentData();

        lineType = data.getLineType();
        fullText = contentData.getText();

        lineSplit = data.getLineSplit();
        startText = fullText;
        endText = "";
    }

    public int getLineSplit(){
        return lineSplit;
    }

    public void setLineSplit(int value){
        lineSplit = value;
    }

    String getStartText(){
        return startText;
    }

    public void setStartText(String value){
        startText = value;
    }

    String getEndText(){
        return endText;
    }

    public String getFullText(){
        return fullText;
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

}
