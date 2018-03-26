package com.creativeartie.writerstudio.pdf;

import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

public class Data{

    public static float inchToPoint(float inches){
        return inches * 72;
    }

    public static float cmToPoint(float cm){
        return cm * 28.3465f;
    }

    private Margin pageMargin;
    private ManuscriptFile outputDoc;
    private DataTitle dataTitle;
    private DataContent dataContent;

    public Data(ManuscriptFile doc){
        pageMargin = new Margin(cmToPoint(3f));
        outputDoc = doc;
        dataTitle = new DataTitle(this);
        dataContent = new DataContent(this);
    }

    public Data(Data data){
        pageMargin = data.pageMargin;
        outputDoc = data.outputDoc;
        dataTitle = data.dataTitle;
        dataContent = data.dataContent;
    }

    public Margin getMargin(){
        return pageMargin;
    }

    public ManuscriptFile getOutputDoc(){
        return outputDoc;
    }

    public WritingText getWritingText(){
        return outputDoc.getDocument();
    }

    public String getData(MetaData key){
        return outputDoc.getText(key);
    }

    public DataTitle getTitleData(){
        return dataTitle;
    }

    public DataContent getContentData(){
        return dataContent;
    }
}