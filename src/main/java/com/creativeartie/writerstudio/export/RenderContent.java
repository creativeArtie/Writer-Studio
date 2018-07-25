package com.creativeartie.writerstudio.export;

import java.util.function.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
public class RenderContent<T>{

    public class Builder{

        public Builder setSplitContent(Function<String, Iterable<String>> func){
            splitContent = func;
            return this;
        }

        public Builder setContentWidth(BiFunction<BridgeContent,String, T> func){
            contentWidth = func;
            return this;
        }

        public RenderContent<T> build(){
            stateNotNull(splitContent, "splitContent");
            stateNotNull(contentWidth, "contentWidth");
            return RenderContent.this;
        }

        private Builder(){}
    }

    public static <U extends Number> RenderContent<U>.Builder builder(){
        return new RenderContent<U>(). new Builder();
    }

    private RenderContent(){}

    private Function<String, Iterable<String>> splitContent;

    private BiFunction<BridgeContent, String, T> contentWidth;

    String[] split(T size, BridgeContent content){
        String self = "";
        String other = "";
        for (String text: splitContent.apply(content.getText())){
        }
    }

    T getWidth(BridgeContent content, String text){
        return contentWidth.apply(content, text);
    }
}
