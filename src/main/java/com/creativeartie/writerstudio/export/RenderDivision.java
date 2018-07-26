package com.creativeartie.writerstudio.export;

import java.util.function.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
public final class RenderDivision<T extends Number> extends Render<T>{

    /// %Part 1: intallise and builder

    public class Builder extends Render<T>.Builder<RenderDivision<T>>{

        private Builder(){}

        public Builder setCalcaluteSpace(Function<RenderDivision<T>, T> func){
            calcaluteSpace = func;
            return this;
        }

        public Builder setCalcaluteFill(Function<RenderDivision<T>, T> func){
            calcaluteFill = func;
            return this;
        }

        @Override
        public RenderDivision<T> buildChildren(){
            stateNotNull(calcaluteSpace, "calcaluteSpace");
            return RenderDivision.this;
        }
    }

    public static <U extends Number> RenderDivision<U>.Builder builder(
        RenderContent<U> content
    ){
        return new RenderDivision<U>(content). new Builder();
    }

    private RenderDivision(RenderContent<T> content){
        contentRender = content;
    }

    /// %Part 2: List render data and functions

    /// %Part 2.1: Render data

    private RenderContent<T> contentRender;
    private DataLineType lineType;
    private T fillWidth;
    private boolean isFirstLine;
    private T contentWidth;

    /// %Part 2.2: Render methods

    private Function<RenderDivision<T>, T> calcaluteSpace;
    private Function<RenderDivision<T>, T> calcaluteFill;

    /// %Part 3: Getter for rendering properties

    public RenderContent<T> getContentRender(){
        return contentRender;
    }

    public T getFillWidth(){
        return fillWidth;
    }

    public boolean isFirstLine(){
        return isFirstLine;
    }

    public T getContentWidth(){
        return contentWidth;
    }

    public DataLineType getLineType(){
        return lineType;
    }

    /// %Part 4: llambda methods calls

    public T calcaluteSpace(ExportDivisionTextLine<T> use, boolean first){
        prepRender(use);
        isFirstLine = first;
        return calcaluteSpace.apply(this);
    }

    public T calcaluteFill(ExportDivisionTextLine<T> use,
        ExportContentText<T> content
    ){
        prepRender(use);
        contentWidth = content.getWidth();
        return calcaluteFill.apply(this);
    }

    /// %Part 5: Utilities methods

    private void prepRender(ExportDivisionTextLine<T> use){
        contentWidth = null;
        fillWidth = use.getFillWidth();
    }
}
