package com.creativeartie.writerstudio.pdf;

import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;


public final class DataWriting implements Data{

    private Margin pageMargin;
    private ManuscriptFile outputDoc;
    private DataTitle dataTitle;
    private DataContent dataContent;

    public DataWriting(ManuscriptFile doc){
        pageMargin = new Margin(Data.cmToPoint(3f));
        outputDoc = doc;
        dataTitle = new DataTitle(this);
        dataContent = new DataContent(this);
    }

    @Override
    public DataWriting getBaseData(){
        return this;
    }

    @Override
    public Margin getMargin(){
        return pageMargin;
    }

    @Override
    public ManuscriptFile getOutputDoc(){
        return outputDoc;
    }

    @Override
    public WritingText getWritingText(){
        return outputDoc.getDocument();
    }

    @Override
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