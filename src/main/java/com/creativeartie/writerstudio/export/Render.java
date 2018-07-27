package com.creativeartie.writerstudio.export;

import java.util.function.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Positions a sub-section on the page*/
abstract class Render<T extends Number>{


    /// %Part 1: intallise and builder

    protected abstract class Builder<U extends Render<T>>{

        Builder(){}

        public final Builder setToZero(Supplier<T> func){
            toZero = func;
            return this;
        }

        public final U build(){
            stateNotNull(toZero, "toZero");
            return buildChildren();
        }

        protected abstract U buildChildren();
    }

    Render(){}

    /// %Part 2: List render data and functions

    /// %Part 2.1: Render data

    /// %Part 2.2: Render methods

    private Supplier<T> toZero;

    /// %Part 3: Getter for rendering properties

    /// %Part 4: llambda methods calls

    public T toZero(){
        return toZero.get();
    }

    /// %Part 5: Utilities methods
}
