package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;

public final class DataTitle implements Data{
    private DataWriting baseData;
    private ManuscriptFile outputDoc;

    public DataTitle(DataWriting data){
        baseData = data;
        outputDoc = data.getOutputDoc();
    }

    public DataWriting getBaseData(){
        return baseData;
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