package com.creativeartie.writerstudio.export;

import java.util.*;
import java.util.function.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
public class RenderMatter<T extends Number> extends Render<T>{


    /// %Part 1: intallise and builder

    public class Builder extends Render<T>.Builder<RenderMatter<T>>{

        public Builder setCanFit(Function<RenderMatter<T>, Boolean> func){
            canFit = func;
            return this;
        }

        public Builder setaddSize(Function<RenderMatter<T>, T> func){
            addSize = func;
            return this;
        }

        public RenderMatter<T> buildChildren(){
            stateNotNull(canFit, "canFit");
            return RenderMatter.this;
        }

        private Builder(){}
    }

    public static <U extends Number> RenderMatter<U>.Builder builder(
        RenderDivision<U> content
    ){
        return new RenderMatter<U>(content). new Builder();
    }

    private RenderMatter(RenderDivision<T> content){
        contentRender = content;
    }

    /// %Part 2: List render data and functions

    /// %Part 2.1: Render data

    private RenderDivision<T> contentRender;
    private Optional<T> maxHeight;
    private T fillHeight;
    private T currentHeight;

    /// %Part 2.2: Render methods

    private Function<RenderMatter<T>, Boolean> canFit;
    private Function<RenderMatter<T>, T> addSize;

    /// %Part 3: Getter for rendering properties

    public RenderDivision<T> getContentRender(){
        return contentRender;
    }

    public Optional<T> getMaxHeight(){
        return maxHeight;
    }

    public T getFillHeight(){
        return fillHeight;
    }

    public T getCurrentHeight(){
        return currentHeight;
    }

    /// %Part 4: llambda methods calls

    public boolean canFit(ExportMatter<T> use, T height){
        prepRender(use);
        currentHeight = height;
        return canFit.apply(this);
    }

    public T addSize(ExportMatter<T> use, T height){
        prepRender(use);
        currentHeight = height;
        return addSize.apply(this);
    }


    /// %Part 5: Utilities methods

    private void prepRender(ExportMatter<T> use){
        fillHeight = use.getFillHeight();
        maxHeight = use.getMaxHeight();
        currentHeight = null;
    }
}
