package com.creativeartie.writer.export;

public interface ContentSection {

    public DataPageType getPageType();

    public ContentMatter getHeader(OutputPageInfo info);

    public ContentMatter getFooter(OutputPageInfo info);

    public ContentMatter getContent(OutputPageInfo info, int from);
}
