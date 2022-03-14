package com.creativeartie.writer.export.mock;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writer.export.*;

public class MockContentLine  extends ForwardingList<ContentData>
    implements ContentLine
{
    private static int counter = 0;

    private ArrayList<ContentData> contentData;
    private DataLineType lineType;
    private final int UNIQUE_ID;

    public MockContentLine(String ... texts){
        contentData = new ArrayList<>();
        for(String text: texts){
            contentData.add(new MockContentData(text));
        }
        lineType = DataLineType.LEFT;
        UNIQUE_ID = counter++;
    }

    @Override
    public DataLineType getLineType(){
        return lineType;
    }

    public MockContentLine setLineType(DataLineType type){
        lineType = type;
        return this;
    }

    @Override
    public List<ContentData> delegate(){
        return contentData;
    }

    @Override
    public void updatePageInfo(OutputPageInfo info){}

    @Override
    public int compareTo(ContentLine line){
        if (line instanceof MockContentLine){
            return Comparator.comparingInt(l -> ((MockContentLine)l).UNIQUE_ID)
                .compare(this,  line);
        }
        return -1;
    }
}
