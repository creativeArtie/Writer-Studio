package com.creativeartie.writerstudio.export;

abstract class Division<T extends Number>{
    private final RenderDivision<T> divisionRenderer;

    Division(RenderDivision<T> renderer){
        divisionRenderer = renderer;
    }

    void render(){
        divisionRenderer.render(this);
    }

    RenderDivision<T> getDivisionRenderer(){
        return divisionRenderer;
    }

}
