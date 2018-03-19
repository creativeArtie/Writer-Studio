package com.creativeartie.writerstudio.pdf;

import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

public final class InputBackPages implements Input{
    private InputWriting baseData;

    public InputBackPages(InputWriting InputWriting){
        baseData = InputWriting;
    }

    public InputWriting getBaseData(){
        return baseData;
    }
}