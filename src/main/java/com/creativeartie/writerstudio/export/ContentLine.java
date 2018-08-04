package com.creativeartie.writerstudio.export;

import java.util.*;

public interface ContentLine extends Comparable<ContentLine>,
    Iterable<ContentData>
{

    public DataLineType getLineType();

    public void updatePageInfo(OutputPageInfo info);

}
