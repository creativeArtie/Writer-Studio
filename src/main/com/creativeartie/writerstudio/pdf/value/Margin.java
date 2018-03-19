package com.creativeartie.writerstudio.pdf.value;

import com.creativeartie.writerstudio.pdf.*;

public class Margin{

    public static Margin marginFromInch(float inches){
        float pt = Data.inchToPoint(inches);
        return new Margin(pt, pt, pt, pt);
    }

    public static Margin marginFromCentimeters(float cm){
        float pt = Data.inchToPoint(cm);
        return new Margin(pt, pt, pt, pt);
    }

    private float leftMargin;
    private float rightMargin;
    private float topMargin;
    private float bottomMargin;

    private Margin(float left, float right, float top, float bottom){
        leftMargin = left;
        rightMargin = right;
        topMargin = top;
        bottomMargin = bottom;
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