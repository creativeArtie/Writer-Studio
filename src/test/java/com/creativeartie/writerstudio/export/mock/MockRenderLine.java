package com.creativeartie.writerstudio.export.mock;

import com.creativeartie.writerstudio.export.*;

public class MockRenderLine implements RenderLine<Integer> {

    public RenderData<Integer> newData(){
        return new MockRenderData();
    }

    public Integer addHeight(Integer old, Integer adding){
        return old == null? adding: old + adding;
    }

    public Integer compareHeight(Integer old, Integer compare){
        return old == null? compare: (old < compare? compare: old);
    }

    public Integer getWidthSpace(Integer width, boolean first){
        if (first){
            return 5;
        } else {
            return 10;
        }
    }
}
