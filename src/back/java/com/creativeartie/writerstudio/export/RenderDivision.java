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

        public Builder setCompareHeight(Function<RenderDivision<T>, T> func){
            compareHeight = func;
            return this;
        }

        public Builder setAddRunning(Function<RenderDivision<T>, T> func){
            addRunning = func;
            return this;
        }

        @Override
        protected RenderDivision<T> buildChildren(){
            stateNotNull(calcaluteSpace, "calcaluteSpace");
            stateNotNull(calcaluteFill, "calcaluteFill");
            stateNotNull(compareHeight, "compareHeight");
            stateNotNull(addRunning, "addRunning");
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
    private T newHeight;
    private T fillHeight;
    private T runningTotal;


    /// %Part 2.2: Render methods

    private Function<RenderDivision<T>, T> calcaluteSpace;
    private Function<RenderDivision<T>, T> calcaluteFill;
    private Function<RenderDivision<T>, T> compareHeight;
    private Function<RenderDivision<T>, T> addRunning;

    /// %Part 3: Getter for rendering properties

    RenderContent<T> getContentRender(){
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

    public T getNewHeight(){
        return newHeight;
    }

    public T getFillHeight(){
        return fillHeight;
    }

    public DataLineType getLineType(){
        return lineType;
    }

    public T getRunningTotal(){
        return runningTotal;
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

    public T compareHeight(ExportDivisionTextLine<T> use, T height){
        prepRender(use);
        newHeight = height;
        return compareHeight.apply(this);
    }

    public T addRunning(ExportDivisionTextLine<T> use, T running){
        prepRender(use);
        runningTotal = running;
        return addRunning.apply(this);
    }

    /// %Part 5: Utilities methods

    private void prepRender(ExportDivisionTextLine<T> use){
        contentWidth = null;
        newHeight = null;
        runningTotal = null;
        fillHeight = use.getFillHeight();
        fillWidth = use.getFillWidth();
    }
}
