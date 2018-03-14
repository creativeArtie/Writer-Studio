package com.creativeartie.jwriter.pdf;

import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;

public final class InputBackPages implements Input{
    private InputWriting baseData;

    public InputBackPages(InputWriting InputWriting){
        baseData = InputWriting;
    }

    public InputWriting getBaseData(){
        return baseData;
    }
}