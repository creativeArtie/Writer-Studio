package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.pdf.value.*;

import org.apache.pdfbox.pdmodel.font.*;

public final class InputWriting implements Input{
    static float inchToPoint(float inches){
        return inches * 72f;
    }

    static float millimeterToPoint(float mm){
        return mm * 2.83465f;
    }

    private boolean isInches;
    private float pageMargin;
    private ManuscriptFile outputDoc;

    public InputWriting(ManuscriptFile doc){
        isInches = true;
        pageMargin = toPoint(1);
        outputDoc = doc;
    }

    private float toPoint(float value){
        return isInches? inchToPoint(value): millimeterToPoint(value);
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
    public float getMargin(){
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