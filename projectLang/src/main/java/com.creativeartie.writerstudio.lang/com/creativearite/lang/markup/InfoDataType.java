package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

/** Describe data type. */
public enum InfoDataType{
    /** Data type of {@link FormattedSpan} */
    FORMATTED(FormattedSpan.class),
    /** Data type of {@link DirectorySpan} for {@link DirectoryType#RESEARCH}. */
    NOTE_REF(DirectorySpan.class),
    /** Data type of {@link ContentSpan} */
    TEXT(ContentSpan.class),
    /** Unknown data type. */
    ERROR(null);

    private Class<? extends SpanBranch> dataClass;

    private InfoDataType(Class<? extends SpanBranch> clazz){
        dataClass = clazz;
    }

    public Class<? extends SpanBranch> getDataClass(){
        return dataClass;
    }
}
