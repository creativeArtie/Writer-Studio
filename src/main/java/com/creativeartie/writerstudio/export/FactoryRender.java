package com.creativeartie.writerstudio.export;

/** Positions a sub-section on the page*/
public interface FactoryRender<T extends Number>{

    public RenderCalculator<T> getRenderCalculator();

    public RenderContent<T> getRenderContent();
}
