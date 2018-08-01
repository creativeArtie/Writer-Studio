package com.creativeartie.writerstudio.export;

public interface RenderData<T extends Number> {

    public RenderLine<T> newFootnote();

    public OutputContentInfo update(OutputContentInfo info);

    public OutputContentInfo split(OutputContentInfo info);
}
