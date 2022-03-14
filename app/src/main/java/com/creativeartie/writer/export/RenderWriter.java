package com.creativeartie.writer.export;

public interface RenderWriter<T extends Number> {

    public RenderPage<T> newPage(DataPageType type);
}
