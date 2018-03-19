package com.creativeartie.writerstudio.pdf;

import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

public final class DataBackPages implements Data{
    private DataWriting baseData;

    public DataBackPages(DataWriting DataWriting){
        baseData = DataWriting;
    }

    public DataWriting getBaseData(){
        return baseData;
    }
}