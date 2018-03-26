package com.creativeartie.writerstudio.pdf;

import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

public final class DataContentNote extends Data{
    private SpanBranch targetRef;

    public DataContentNote(Data input, SpanBranch ref, StreamData data){
        super(input);
        targetRef = ref;
    }

    @Override
    public int hashCode(){
        return targetRef.getStart();
    }

    public boolean matchTarget(DataContentNote compare){
        return targetRef == compare.targetRef;
    }

    public FormatterItem getItem(){
        return new FormatterItem(12); // TODO STUB
    }
}
