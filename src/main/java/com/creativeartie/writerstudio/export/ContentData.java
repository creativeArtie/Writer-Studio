package com.creativeartie.writerstudio.export;

import java.util.*;

public interface ContentData {

    public String getText();

    public Optional<ContentLine> getFootnote();

    public void updatePageInfo(OutputPageInfo info);

    public boolean isBold();

    public boolean isItalics();

    public boolean isUnderline();

    public boolean isCoded();

    public boolean isSuperscript();

    // public boolean isKeepLast();

}
