package com.creativeartie.writerstudio.export;

public interface RenderMatter<T extends Number> {

    public RenderLine<T> newLine();

    public T addHeight(T old, T add);
    
    public boolean canFitHeight(T cur, T adding);
    
    public T getFootnotePadding();
}
