package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.pdf.value.*;

import org.apache.pdfbox.pdmodel.font.*;


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
        return new SizedFont(PDType1Font.TIMES_ROMAN, 12);
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

}