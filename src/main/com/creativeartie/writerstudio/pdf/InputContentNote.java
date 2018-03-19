package com.creativeartie.writerstudio.pdf;

import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

public final class InputContentNote implements Input{
    private InputWriting baseData;
    private SpanBranch targetRef;

    public InputContentNote(InputWriting input, SpanBranch ref, StreamData data){
        baseData = input;
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