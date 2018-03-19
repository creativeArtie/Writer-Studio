package com.creativeartie.writerstudio.pdf;

import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.lang.markup.*;

import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.pdf.value.*;

public interface Input{

    public InputWriting getBaseData();

    public default SizedFont getBaseFont(){
        return getBaseData().getBaseFont();
    }

    public default Margin getMargin(){
        return getBaseData().getMargin();
    }

    public default ManuscriptFile getOutputDoc(){
        return getBaseData().getOutputDoc();
    }

    public default WritingText getWritingText(){
        return getBaseData().getWritingText();
    }
}