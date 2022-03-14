package com.creativeartie.writer.export;

public interface RenderMatter<T extends Number> {

    public RenderLine<T> newLine(DataLineType type);

    public T addHeight(T old, T add);

    public boolean canFitHeight(@MaybeNull T cur, T adding);

    public T getPadding();
}
