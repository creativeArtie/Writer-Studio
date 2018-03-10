package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;

public final class DataBackPages implements Data{
    private DataWriting baseData;

    public DataBackPages(DataWriting DataWriting){
        baseData = DataWriting;
    }

    public DataWriting getBaseData(){
        return baseData;
    }
}