package com.creativeartie.writer.export;

public interface ContentLine extends Comparable<ContentLine>,
    Iterable<ContentData>
{

    public DataLineType getLineType();

    public void updatePageInfo(OutputPageInfo info);

}
