package com.creativeartie.writerstudio.export;

public interface RenderPage<T extends Number> {
    /// private DataPageType pageType;

    public RenderMatter<T> getHeader();

    public RenderMatter<T> getFooter();

    public RenderMatter<T> getContent();

    public RenderMatter<T> getFootnote();

    public OuputPageInfo getPageInfo();

    public T getEmptyHeight(T header, T footer);
}
