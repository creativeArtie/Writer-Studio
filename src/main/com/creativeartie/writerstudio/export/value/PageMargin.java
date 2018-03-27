package com.creativeartie.writerstudio.export.value;

public class PageMargin{

    private float leftMargin;
    private float rightMargin;
    private float topMargin;
    private float bottomMargin;

    public PageMargin(float all){
        this(all, all, all, all);
    }

    public PageMargin(float left, float right, float top, float bottom){
        leftMargin = left;
        rightMargin = right;
        topMargin = top;
        bottomMargin = bottom;
    }

    public PageMargin getLeft(float margin){
        leftMargin = margin;
        return this;
    }

    public PageMargin getRight(float margin){
        rightMargin = margin;
        return this;
    }

    public PageMargin getTop(float margin){
        topMargin = margin;
        return this;
    }

    public PageMargin setBottom(float margin){
        bottomMargin = margin;
        return this;
    }

    public float getLeft(){
        return leftMargin;
    }

    public float getRight(){
        return rightMargin;
    }

    public float getTop(){
        return topMargin;
    }

    public float getBottom(){
        return bottomMargin;
    }
}