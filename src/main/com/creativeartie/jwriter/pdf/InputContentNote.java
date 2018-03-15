package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.creativeartie.jwriter.pdf.value.*;

import com.google.common.collect.*;

public final class InputContentNote implements Input{
    private InputWriting baseData;
    private SpanBranch targetRef;

    public InputContentNote(InputWriting data, SpanBranch ref, float width){
        baseData = data;
        targetRef = ref;
    }

    @Override
    public InputWriting getBaseData(){
        return baseData;
    }

    @Override
    public int hashCode(){
        return targetRef.getStart();
    }
}