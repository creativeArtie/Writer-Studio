package com.creativeartie.writerstudio.export;

import java.util.function.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public class RenderWriting<T extends Number> extends Render<T>{


    /// %Part 1: intallise and builder

    public class Builder extends Render<T>.Builder<RenderWriting<T>>{

        public Builder setNewPage(Function<RenderWriting<T>, RenderPage<T>> func){
            newPage = func;
            return this;
        }

        protected RenderWriting<T> buildChildren(){
            stateNotNull(newPage, "newPage");
            return RenderWriting.this;
        }

        private Builder(){}
    }

    public static <U extends Number> RenderWriting<U>.Builder builder(){
        return new RenderWriting<U>(). new Builder();
    }

    private RenderWriting(){}

    /// %Part 2: List render data and functions

    /// %Part 2.1: Render data

    private DataPageType pageType;

    /// %Part 2.2: Render methods

    private Function<RenderWriting<T>, RenderPage<T>> newPage;

    /// %Part 3: Getter for rendering properties

    public DataPageType getPageType(){
        return pageType;
    }

    /// %Part 4: llambda methods calls

    public RenderPage<T> newPage(ExportSection<T> use, DataPageType type){
        prepRender(use);
        pageType = type;
        return newPage.apply(this);
    }

    /// %Part 5: Utilities methods

    private void prepRender(ExportSection<T> use){
        pageType = null;
    }
}
