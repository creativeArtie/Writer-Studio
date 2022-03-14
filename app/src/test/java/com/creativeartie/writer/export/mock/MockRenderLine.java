package com.creativeartie.writer.export.mock;

import com.creativeartie.writer.export.*;

public class MockRenderLine implements RenderLine<Integer> {

    private final int firstLine;
    private final int midLine;

    public MockRenderLine(){
        this(5, 10);
    }

    public MockRenderLine(int first, int mid){
        firstLine = first;
        midLine = mid;
    }

    public RenderData<Integer> newData(){
        return new MockRenderData();
    }

    public Integer addSize(Integer old, Integer adding){
        return old == null? adding: old + adding;
    }

    public Integer compareSize(Integer old, Integer compare){
        return old == null? compare: (old < compare? compare: old);
    }

    public Integer getWidthSpace(Integer width, boolean first, DataLineType type){
        if (first){
            return firstLine - (width == null? 0: width);
        } else {
            return midLine - (width == null? 0: width);
        }
    }
}
