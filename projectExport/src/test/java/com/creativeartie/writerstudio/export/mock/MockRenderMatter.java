package com.creativeartie.writerstudio.export.mock;

import com.creativeartie.writerstudio.export.*;

public class MockRenderMatter implements RenderMatter<Integer> {

    private int matterPadding;
    private int firstLineWidth;
    private int lineWidth;

    public MockRenderMatter(){
        matterPadding = 5;
        firstLineWidth = 75;
        lineWidth = 80;
    }

    public MockRenderMatter setFirstLineWidth(int value){
        firstLineWidth = value;
        return this;
    }

    public MockRenderMatter setLineWidth(int value){
        lineWidth = value;
        return this;
    }

    @Override
    public RenderLine<Integer> newLine(DataLineType type){
        return new MockRenderLine(firstLineWidth, lineWidth);
    }

    @Override
    public Integer addHeight(Integer old, Integer add){
        return old + add;
    }

    @Override
    public boolean canFitHeight(Integer cur, Integer adding){
        return cur == null? true: cur < adding;
    }

    @Override
    public Integer getPadding(){
        return matterPadding;
    }

    public MockRenderMatter setPadding(int padding){
        matterPadding = padding;
        return this;
    }
}
