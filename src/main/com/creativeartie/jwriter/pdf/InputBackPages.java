package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.creativeartie.jwriter.pdf.value.*;

public final class InputBackPages implements Input{
    private InputWriting baseData;

    public InputBackPages(InputWriting InputWriting){
        baseData = InputWriting;
    }

    public InputWriting getBaseData(){
        return baseData;
    }
}