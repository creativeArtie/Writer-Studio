package com.creativeartie.writer.export;

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

    /** Get the spliting of the line. This is to help where the last split is
     * located, so the content on the new line continues where the last line
     * left off.
     * @see #setLineSplit(int)
     */
    public int getLineSplit(){
        return lineSplit;
    }

    /** Set the spliting of the line. This is to help where the last split is
     * located, so the content on the new line continues where the last line
     * left off.
     * @see #setLineSplit(int)
     */
    public void setLineSplit(int value){
        lineSplit = value;
    }

    /** Gets the rendering text for this span of text. */
    public String getCurrentText(){
        return currentText;
    }

    /** Set the current to full text. This helps with width calculating for
     * footnotes numbers */
    void currentTextAsFull(){
        currentText = fullText;
    }

    String getStartText(){
        return startText;
    }

    /** set the text to keep for this current span. */
    public void setStartText(String value){
        startText = value;
    }

    /** gets the full text that can parse into {@link #setStartText(String)} and
     * {@link #setEndText(String)}. */
    public String getFullText(){
        return fullText;
    }

    void setFullText(String text){
        fullText = text;
    }

    String getEndText(){
        return endText;
    }

    /** Set the text that cannot be use for this line. */
    public void setEndText(String value){
        endText = value;
    }

    /** Gets the line type for formatting. */
    public DataLineType getLineType(){
        return lineType;
    }

    /** Gets the list content formatting. */
    public List<DataContentType> getFormats(){
        return contentData.getFormats();
    }

    /** Gets the link path. */
    public Optional<String> getLinkPath(){
        return contentData.getLinkPath();
    }

    /** Gets space that can be use to fill in more text. */
    public T getWidthSpace(){
        return widthSpace;
    }

    @Override
    public String toString(){
        return "[start=\"" + startText + "\",end=\"" + endText + "\",split=\"" +
            lineSplit + "\"]";
    }

}
