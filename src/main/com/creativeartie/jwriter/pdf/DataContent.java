package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;

public final class DataContent implements Data{
    private DataWriting baseData;

    public DataContent(DataWriting DataWriting){
        baseData = DataWriting;
    }

    public DataWriting getBaseData(){
        return baseData;
    }
}