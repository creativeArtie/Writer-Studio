package com.creativeartie.writerstudio.pdf;

import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;


public final class InputWriting implements Input{

    private Margin pageMargin;
    private ManuscriptFile outputDoc;

    public InputWriting(ManuscriptFile doc){
        pageMargin = Margin.marginFromInch(1);
        outputDoc = doc;
    }

    @Override
    public InputWriting getBaseData(){
        return this;
    }

    @Override
    public SizedFont getBaseFont(){
        return SizedFont.newTimesRoman(12);
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

    public InputTitle getTitleData(){
        return new InputTitle(this);
    }

    public InputContent getContentData(){
        return new InputContent(this);
    }

}