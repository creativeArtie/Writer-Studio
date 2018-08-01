package com.creativeartie.writerstudio.export;

public interface RenderLine<T extends Number> {

    public RenderData<T> newData();

    public T addHeight(T old, T adding);

    public T compareHeight(T old, T compare);

    public T getSpaceWidth(T width);
}
