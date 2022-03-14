package com.creativeartie.writer.export;

public interface RenderPage<T extends Number> {
    /// private DataPageType getPageType;

    public RenderMatter<T> getHeader();

    public RenderMatter<T> getFooter();

    public RenderMatter<T> getContent();

    public RenderMatter<T> getFootnote();

    public OutputPageInfo getPageInfo();

    public T getEmptyHeight(@MaybeNull T header, @MaybeNull T footer);
}
