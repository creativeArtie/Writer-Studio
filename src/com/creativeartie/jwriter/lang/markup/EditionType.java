package com.creativeartie.jwriter.lang.markup;

import com.google.common.base.*;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

import com.creativeartie.jwriter.lang.StyleInfo;
public enum EditionType implements StyleInfo{
    /// Enum order mandated by WindowText
    STUB, DRAFT, FINAL, OTHER, NONE;

    @Override
    public String toString(){
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name());
    }
}
