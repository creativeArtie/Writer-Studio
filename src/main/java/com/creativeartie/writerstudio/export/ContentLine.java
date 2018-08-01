package com.creativeartie.writerstudio.export;

import java.util.*;

public interface ContentLine extends Comparable<ContentLine>{

    public DataLineType getLineType();

    public List<ContentData> getContent();

    public void updatePageInfo(OutputPageInfo info);

}
