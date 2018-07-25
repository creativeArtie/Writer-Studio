package com.creativeartie.writerstudio.export;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
final class ExportContentText<T extends Number> implements ExportBase<T>{
    private final FactoryRender<T> renderFactory;

    private final BridgeContent inputText;
    private String outputText;

    public ExportContentText(FactoryRender<T> factory, BridgeContent text){
        renderFactory = factory;
        inputText = text;
        outputText = text.getText();
    }

    private ExportContentText(ExportContentText<T> self, String text){
        renderFactory = self.renderFactory;
        inputText = self.inputText;
        outputText = text;
    }

    Optional<ExportContentText<T>> split(T width){
        String[] text = getRenderContent().split(this, width);
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
        return inputText;
    }

    boolean isEmpty(){
        return outputText.length() == 0;
    }

    public T getWidth(){
        return getRenderContent().calculateWidth(this);
    }

    @Override
    public FactoryRender<T> getRender(){
        return renderFactory;
    }

    @Override
    public String toString(){
        return "\"" + outputText + "\"";
    }
}
