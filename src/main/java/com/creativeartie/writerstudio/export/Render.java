package com.creativeartie.writerstudio.export;

import java.util.function.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Positions a sub-section on the page*/
public abstract class Render<T extends Number>{


    /// %Part 1: intallise and builder

    protected abstract class Builder<U extends Render<T>>{

        Builder(){}

        public final Builder setGetZero(Supplier<T> func){
            getZero = func;
            return this;
        }

        public final U build(){
            stateNotNull(getZero, "getZero");
            return buildChildren();
        }

        protected abstract U buildChildren();
    }

    Render(){}

    /// %Part 2: List render data and functions

    /// %Part 2.1: Render data

    /// %Part 2.2: Render methods

    private Supplier<T> getZero;

    /// %Part 3: Getter for rendering properties

    /// %Part 4: llambda methods calls

    public T getZero(){
        return getZero.get();
    }

    /// %Part 5: Utilities methods
}
