package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A span to store data. */
public abstract class InfoDataSpan extends SpanBranch{

    private final InfoDataType dataType;

    /** Creates a {@linkplain InfoDataSpan}.
     *
     * @param children
     *      span children
     * @param type
     *      data type
     */
    protected InfoDataSpan(List<Span> children, InfoDataType type){
        super(children);
        dataType = argumentNotNull(type, "type");
    }

    /** Gets the data type.
     *
     * @return answer
     */
    public final InfoDataType getDataType(){
        return dataType;
    }

    /** Gets the branch data.
     *
     * @return answer
     */
    public abstract SpanBranch getData();

    @Override
    public String toString(){
        return "Data%%" + getDataType() + " " + getData() + "%%";
    }
}
