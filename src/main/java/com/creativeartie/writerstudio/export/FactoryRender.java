package com.creativeartie.writerstudio.export;

/** Positions a sub-section on the page*/
public interface FactoryRender<T extends Number>{

    public RenderContent<T> getRenderContent();

    public RenderDivision<T> getRenderDivision();

    public T getZero();
}
