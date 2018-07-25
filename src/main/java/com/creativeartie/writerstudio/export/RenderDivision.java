package com.creativeartie.writerstudio.export;

import java.util.function.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
public class RenderDivision<T extends Number>{
    public class Builder{

/*
        public Builder setContentWidth(Function<BridgeContent,T> func){
            contentWidth = func;
            return this;
        }
*/
        public RenderDivision<T> build(){
            // stateNotNull(contentWidth, "contentWidth");
            return RenderDivision.this;
        }

        private Builder(){}
    }

    public static <U extends Number> RenderDivision<U>.Builder builder(){
        return new RenderDivision<U>(). new Builder();
    }

    private RenderDivision(){}

    private Function<BridgeContent, T> contentWidth;
    private Function<BridgeContent, T> contentHeight;
}
