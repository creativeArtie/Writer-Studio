package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;

public final class DocumentData{
    static float inchToPoint(float inches){
        return inches * 72f;
    }

    static float millimeterToPoint(float mm){
        return mm * 2.83465f;
    }

    private boolean isInches;
    private float pageMargin;
    private ManuscriptFile outputDoc;

    public DocumentData(ManuscriptFile doc){
        isInches = true;
        pageMargin = toPoint(1);
        outputDoc = doc;
    }

    private float toPoint(float value){
        return isInches? inchToPoint(value): millimeterToPoint(value);
    }

    public float getMargin(){
        return pageMargin;
    }

    public ManuscriptFile getOutputDoc(){
        return outputDoc;
    }

    public WritingText getWritingText(){
        return outputDoc.getDocument();
    }

    public ArrayList<String> getTitleTopText(){
        ArrayList<String> ans = new ArrayList<>();
        ans.add(outputDoc.getText(MetaData.AGENT_NAME));
        ans.add(outputDoc.getText(MetaData.AGENT_ADDRESS));
        ans.add(outputDoc.getText(MetaData.AGENT_EMAIL));
        ans.add(outputDoc.getText(MetaData.AGENT_PHONE));
        return ans;
    }

    public ArrayList<String> getTitleCenterText(){
        ArrayList<String> ans = new ArrayList<>();
        ans.add(outputDoc.getText(MetaData.TITLE));
        ans.add("By");
        ans.add(outputDoc.getText(MetaData.PEN_NAME, MetaData.AUTHOR));
        return ans;
    }

    public ArrayList<String> getTitleBottomText(){
        ArrayList<String> ans = new ArrayList<>();
        ans.add(outputDoc.getText(MetaData.AUTHOR));
        ans.add(outputDoc.getText(MetaData.ADDRESS));
        ans.add(outputDoc.getText(MetaData.PHONE));
        ans.add(outputDoc.getText(MetaData.EMAIL));
        ans.add(outputDoc.getText(MetaData.WEBSITE));
        ans.add(outputDoc.getText(MetaData.AUTHOR) + " © " +
            outputDoc.getText(MetaData.COPYRIGHT));
        return ans;
    }
}