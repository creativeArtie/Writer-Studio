package com.creativeartie.writerstudio.pdf;

import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

public final class DataContentNote implements Data{
    private DataWriting baseData;
    private SpanBranch targetRef;

    public DataContentNote(DataWriting input, SpanBranch ref, StreamData data){
        baseData = input;
        targetRef = ref;
    }

    @Override
    public DataWriting getBaseData(){
        return baseData;
    }

    @Override
    public int hashCode(){
        return targetRef.getStart();
    }

    public boolean matchTarget(DataContentNote compare){
        return targetRef == compare.targetRef;
    }
}