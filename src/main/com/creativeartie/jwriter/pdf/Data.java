package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.markup.*;

import org.apache.pdfbox.pdmodel.font.*;

public interface Data{

    public DataWriting getBaseData();

    public default PDFont getBaseFontType(){
        return getBaseData().getBaseFontType();
    }

    public default int getBaseFontSize(){
        return getBaseData().getBaseFontSize();
    }

    public default float getMargin(){
        return getBaseData().getMargin();
    }

    public default ManuscriptFile getOutputDoc(){
        return getBaseData().getOutputDoc();
    }

    public default WritingText getWritingText(){
        return getBaseData().getWritingText();
    }
}