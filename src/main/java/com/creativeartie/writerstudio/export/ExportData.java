package com.creativeartie.writerstudio.export;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public class ExportData<T extends Number> {

    private ContentData inputContent;
    private RenderData<T> renderExporter;
    private String outputContent;
    private DataLineType lineType;
    private int lineSplit;
    private T keepNext;

    ExportData(ContentData content, DataLineType type, RenderData<T> renderer){
        inputContent = content;
        lineType = type;
        renderExporter = renderer;
        outputContent = content.getText();
        updateContent();
        lineSplit = 0;
        keepNext = null;
    }

    private ExportData(ExportData<T> data, OutputContentInfo info){
        inputContent = data.inputContent;
        renderExporter = data.renderExporter;
        outputContent = info.getEndText();
        lineType = data.lineType;
        lineSplit = info.getLineSplit();
        keepNext = data.keepNext;
    }

    private void updateContent(){
        OutputContentInfo<T> info = new OutputContentInfo<>(this, lineType, null);
        info = renderExporter.update(info);
        lineSplit = info.getLineSplit();
        outputContent = info.getEndText();
    }

    Optional<ExportData<T>> split(T spaces){
        OutputContentInfo<T> info = new OutputContentInfo<>(this, lineType, spaces);
        info = renderExporter.split(info, keepNext);
        lineSplit = info.getLineSplit();
        outputContent = info.getStartText();
        if (info.getEndText().length() > 0){
            return Optional.of(new ExportData<T>(this, info));
        }
        return Optional.empty();
    }

    Optional<ExportLineMain<T>> getFootnote(){
        Optional<ContentLine> line = inputContent.getFootnote();
        if (line.isPresent()){
            RenderLine<T> render = renderExporter.newFootnote();
            return Optional.of(new ExportLineMain<T>(line.get(), render));
        }
        return Optional.empty();
    }

    void updateContent(OutputPageInfo info){
        inputContent.updatePageInfo(info);
        updateContent();
    }

    T getFillWidth(){
        OutputContentInfo<T> info = new OutputContentInfo<>(this, lineType, null);
        return renderExporter.getWidth(info);
    }

    T getFillHeight(){
        OutputContentInfo<T> info = new OutputContentInfo<>(this, lineType, null);
        return renderExporter.getHeight(info);
    }

    ContentData getContentData(){
        return inputContent;
    }

    DataLineType getLineType(){
        return lineType;
    }

    String getCurrentText(){
        return outputContent;
    }

    int getLineSplit(){
        return lineSplit;
    }

    boolean isFilled(){
        return outputContent.length() > 0;
    }

    boolean isKeepLast(){
        return inputContent.isKeepLast();
    }

    void setKeepNext(T width){
        keepNext = width;
    }

    T getKeepNext(){
        return keepNext;
    }

    @Override
    public String toString(){
        return "{" + outputContent + "}";
    }
}
