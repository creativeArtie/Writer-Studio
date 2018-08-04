package com.creativeartie.writerstudio.export;

import java.util.*;

public interface ContentData {

    public void updatePageInfo(OutputPageInfo info);

    public String getText();

    public Optional<ContentLine> getFootnote();

    public List<DataContentType> getFormats();

    public Optional<String> getLinkPath();

    // public boolean isKeepLast();

}
