package com.creativeartie.writerstudio.export;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public class ExportData<T extends Number> {

    private ContentData inputContent;
    private RenderData<T> renderExporter;
    private final DataLineType lineType;
    private String outputText;
    private boolean isStart;

    ExportData(ContentData input, DataLineType type, RenderData<T> render){
        inputContent = input;
        lineType = type;
        renderExporter = render;
        outputText = input.getText();
        isStart = true;
    }

    private ExportData(ExportData<T> self, String text){
        renderExporter = self.renderExporter;
        inputContent = self.inputContent;
        lineType = self.lineType;
        outputText = text;
    }

    Optional<ExportData<T>> splitContent(T width){
        String[] text = renderExporter.splitContent(outputText, width);
        stateCheck(text.length == 2, "Unexpected split length: " + text.length);
        outputText = text[0];
        if (text[1].length() > 0){
            return Optional.of(new ExportData<>(this, text[1]));
        }
        return Optional.empty();
    }

    String getText(){
        return outputText;
    }

    ContentData getStyle(){
        return inputContent;
    }

    DataLineType getLineType(){
        return lineType;
    }

    boolean isEmpty(){
        return outputText.length() == 0;
    }

    public T getWidth(){
        return renderExporter.getWidth(inputContent, outputText);
    }

    public T getHeight(){
        return renderExporter.getHeight(inputContent, outputText);
    }

    @Override
    public String toString(){
        return "\"" + outputText + "\"";
    }
}
