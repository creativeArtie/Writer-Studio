package com.creativeartie.writerstudio.export;

import java.util.*;

public class OutputContentInfo<T> {
    private final ContentData contentData;

    private final DataLineType lineType;
    private String fullText;
    private String currentText;

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
        startText = "";
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

    void currentTextAsFull(){
        System.out.println(fullText);
        currentText = fullText;
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

    public List<DataContentType> getFormats(){
        return contentData.getFormats();
    }

    public Optional<String> getLinkPath(){
        return contentData.getLinkPath();
    }

    public T getWidthSpace(){
        return widthSpace;
    }

    @Override
    public String toString(){
        return "[start=\"" + startText + "\",end=\"" + endText + "\",split=\"" +
            lineSplit + "\"]";
    }

}
