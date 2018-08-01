package com.creativeartie.writerstudio.export;

public interface ContentSection {

    public DataPageType getPageType();

    public ContentMatter getHeader(OuputPageInfo info);

    public ContentMatter getFooter(OuputPageInfo info);

    public ContentMatter getContent(OuputPageInfo info, int from);
}
