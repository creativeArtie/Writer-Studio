package com.creativeartie.writerstudio.pdf.value;

public class Margin{

    private float leftMargin;
    private float rightMargin;
    private float topMargin;
    private float bottomMargin;

    public Margin(float all){
        this(all, all, all, all);
    }

    public Margin(float left, float right, float top, float bottom){
        leftMargin = left;
        rightMargin = right;
        topMargin = top;
        bottomMargin = bottom;
    }

    public Margin getLeft(float margin){
        leftMargin = margin;
        return this;
    }

    public Margin getRight(float margin){
        rightMargin = margin;
        return this;
    }

    public Margin getTop(float margin){
        topMargin = margin;
        return this;
    }

    public Margin setBottom(float margin){
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