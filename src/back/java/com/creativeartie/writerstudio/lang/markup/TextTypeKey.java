package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

public enum TextTypeKey implements SpecTypeKey{
    ALGIN, UNKNOWN;

    private SpecTypeKey getDataType(){
        return this == ALIGN? SpecTypeData.ENUM: SpecTypeData.NONE;
    }
}
