package com.creativeartie.writer.export;

public interface RenderLine<T extends Number> {

    // public DataLineType getLineType();

    public RenderData<T> newData();

    public T addSize(@MaybeNull T old, T adding);

    public T compareSize(@MaybeNull T old, T compare);

    public T getWidthSpace(T width, boolean first, DataLineType type);
}
