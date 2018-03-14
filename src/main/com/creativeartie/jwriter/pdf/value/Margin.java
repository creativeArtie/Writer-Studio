package com.creativeartie.jwriter.pdf.value;

import org.apache.pdfbox.pdmodel.*;

public class Margin{

    public static Margin marginFromInch(float inches){
        float pt = inchToPoint(inches);
        return new Margin(pt, pt, pt, pt);
    }

    public static Margin marginFromCentimeters(float cm){
        float pt = inchToPoint(cm);
        return new Margin(pt, pt, pt, pt);
    }

    private static float inchToPoint(float inches){
        return inches * 72;
    }

    private static float cmToPoint(float cm){
        return cm * 28.3465f;
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

    public float calcluateWidth(PDPage page){
        return page.getMediaBox().getWidth() - leftMargin - rightMargin;
    }

    public float calcluateHeight(PDPage page){
        return page.getMediaBox().getHeight() - topMargin - bottomMargin;
    }

}