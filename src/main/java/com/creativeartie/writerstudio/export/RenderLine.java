package com.creativeartie.writerstudio.export;

public interface RenderLine<T extends Number> {

    // public DataLineType getLineType();

    public RenderData<T> newData();

    public T addSize(T old, T adding);

    public T compareSize(T old, T compare);

    public T getWidthSpace(T width, boolean first);
}
