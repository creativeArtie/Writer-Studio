package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

public enum StatTypeKey implements SpecTypeKey{
    PUBLISH_TOTAL, PUBLISH_GOAL, NOTE_TOTAL, TIME_TOTAL, TIME_GOAL, UNKNOWN;

    protected SpecTypeKey getDataType(){
        return ordinal() < NOTE_TOTAL.ordina()?
            SpecTypeData.INTEGER:
            (ordinal() < TIME_GOAL? SpecTypeData.TIME:SpecTypeData.NONE);
    }

    protected String getKeyName(){
        return StatParseField.values()[ordinal()].getKeyName();
    }
}
