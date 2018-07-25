package com.creativeartie.writerstudio.export;

import java.util.function.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
public class RenderDivision<T extends Number>{


    /// %Part 1: intallise and builder

    public class Builder{

        public Builder setCalcaluteSpace(Function<RenderDivision<T>, T> func){
            calcaluteSpace = func;
            return this;
        }

        public Builder setCalcaluteFill(Function<RenderDivision<T>, T> func){
            calcaluteFill = func;
            return this;
        }

        public RenderDivision<T> build(){
            stateNotNull(calcaluteSpace, "calcaluteSpace");
            return RenderDivision.this;
        }

        private Builder(){}
    }

    public static <U extends Number> RenderDivision<U>.Builder builder(){
        return new RenderDivision<U>(). new Builder();
    }

    private RenderDivision(){}

    /// %Part 2: List render data and functions

    /// %Part 2.1: Render data

    private T fillWidth;
    private boolean isFirstLine;
    private ExportContentText<T> applyContent;

    /// %Part 2.2: Render methods

    private Function<RenderDivision<T>, T> calcaluteSpace;
    private Function<RenderDivision<T>, T> calcaluteFill;

    /// %Part 3: Getter for rendering properties

    public T getFillWidth(){
        return fillWidth;
    }

    public boolean isFirstLine(){
        return isFirstLine;
    }

    public ExportContentText<T> getApplyContent(){
        return applyContent;
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
        applyContent = content;
        return calcaluteFill.apply(this);
    }

    /// %Part 5: Utilities methods

    private void prepRender(ExportDivisionTextLine<T> use){
        applyContent = null;
        fillWidth = use.getFillWidth();
    }
}
