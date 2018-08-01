package com.creativeartie.writerstudio.export;

import java.util.*;

public interface ContentData {

    public void updatePageInfo(OutputPageInfo info);

    public String getText();

    public Optional<ContentLine> getFootnote();

    public boolean isBold();

    public boolean isItalics();

    public boolean isUnderline();

    public boolean isCoded();

    public boolean isSuperscript();

    public Optional<String> getLinkPath();

    // public boolean isKeepLast();

}
