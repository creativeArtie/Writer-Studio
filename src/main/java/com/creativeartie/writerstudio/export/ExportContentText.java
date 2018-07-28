package com.creativeartie.writerstudio.export;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
final class ExportContentText<T extends Number> {

    private final BridgeContent inputBridge;
    private final DataLineType lineType;
    private final RenderContent<T> outputRender;
    private String outputText;
    private boolean isStart;

    public ExportContentText(BridgeContent text, DataLineType type,
        RenderContent<T> render
    ){
        inputBridge = text;
        lineType = type;
        outputRender = render;
        outputText = text.getText();
        isStart = true;
    }

    private ExportContentText(ExportContentText<T> self, String text){
        outputRender = self.outputRender;
        inputBridge = self.inputBridge;
        lineType = self.lineType;
        outputText = text;
    }

    Optional<ExportContentText<T>> splitContent(T width){
        String[] text = outputRender.splitContent(this, width);
        stateCheck(text.length == 2, "Unexpected split length: " + text.length);
        outputText = text[0];
        if (text[1].length() > 0){
            return Optional.of(new ExportContentText<>(this, text[1]));
        }
        return Optional.empty();
    }

    String getText(){
        return outputText;
    }

    BridgeContent getStyle(){
        return inputBridge;
    }

    DataLineType getLineType(){
        return lineType;
    }

    boolean isEmpty(){
        return outputText.length() == 0;
    }

    public T getWidth(){
        return outputRender.calculateWidth(this);
    }

    public T getHeight(){
        return outputRender.calcuateHeight(this);
    }

    public RenderContent<T> getRender(){
        return outputRender;
    }

    @Override
    public String toString(){
        return "\"" + outputText + "\"";
    }
}
