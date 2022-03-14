package com.creativeartie.writer.export.mock;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writer.export.*;

public class MockContentMatter extends ForwardingList<ContentLine>
    implements ContentMatter
{
    private ArrayList<ContentLine> contentLines;

    public MockContentMatter(){
        contentLines = new ArrayList<>();
    }

    public MockContentMatter(List<ContentLine> lines){
        contentLines = new ArrayList<>(lines);
    }

    @Override
    public List<ContentLine> delegate(){
        return contentLines;
    }

    @Override
    public Iterator<ContentLine> iterator(int from){
        return contentLines.subList(from, contentLines.size()).iterator();
    }
}
