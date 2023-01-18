package com.creativeartie.writer.export;

public interface RenderData<T extends Number> {

    public RenderLine<T> newFootnote();

    public OutputContentInfo<T> update(OutputContentInfo<T> info);

    public OutputContentInfo<T> split(OutputContentInfo<T> info);

    public OutputContentInfo<T> split(OutputContentInfo<T> info, T extra);

    public T getWidth(OutputContentInfo<T> info);

    public T getHeight(OutputContentInfo<T> info);
}
