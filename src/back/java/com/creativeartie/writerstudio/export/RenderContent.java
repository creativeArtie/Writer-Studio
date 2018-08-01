package com.creativeartie.writerstudio.export;

import java.util.function.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
public final class RenderContent<T extends Number> extends Render<T>{

    /// %Part 1: intallise and builder

    public class Builder extends Render<T>.Builder<RenderContent<T>>{

        private Builder(){}

        public Builder setSplitContent(Function<RenderContent<T>, String[]> func){
            splitContent = func;
            return this;
        }

        public Builder setCalcuateHeight(Function<RenderContent<T>, T> func){
            calcuateHeight = func;
            return this;
        }

        public Builder setCalculateWidth(Function<RenderContent<T>, T> func){
            calculateWidth = func;
            return this;
        }

        @Override
        protected RenderContent<T> buildChildren(){
            stateNotNull(splitContent, "splitContent");
            stateNotNull(calcuateHeight, "calcuateHeight");
            stateNotNull(splitContent, "splitContent");
            stateNotNull(calculateWidth, "calculateWidth");
            return RenderContent.this;
        }
    }

    public static <U extends Number> RenderContent<U>.Builder builder(){
        return new RenderContent<U>(). new Builder();
    }

    private RenderContent(){}

    /// %Part 2: List render data and functions

    /// %Part 2.1: Render data

    private T maxWidth;
    private String useText;
    private BridgeContent textStyle;
    private DataLineType lineType;

    /// %Part 2.2: Render methods

    private Function<RenderContent<T>, T> calcuateHeight;
    private Function<RenderContent<T>, String[]> splitContent;
    private Function<RenderContent<T>, T> calculateWidth;

    /// %Part 3: Getter for rendering properties

    public String getUseText(){
        return useText;
    }

    public BridgeContent getTextStyle(){
        return textStyle;
    }

    public T getMaxWidth(){
        return maxWidth;
    }

    public DataLineType getLineType(){
        return lineType;
    }

    /// %Part 4: llambda methods calls

    String[] splitContent(ExportContentText<T> use, T width){
        prepRender(use);
        maxWidth = width;
        return splitContent.apply(this);
    }

    public T calculateWidth(ExportContentText<T> use){
        prepRender(use);
        return calculateWidth.apply(this);
    }

    public T calcuateHeight(ExportContentText<T> use){
        prepRender(use);
        return calcuateHeight.apply(this);
    }

    /// %Part 5: Utilities methods

    private void prepRender(ExportContentText<T> use){
        useText = use.getText();
        textStyle = use.getStyle();
        lineType = use.getLineType();
        maxWidth = null;
    }

}
