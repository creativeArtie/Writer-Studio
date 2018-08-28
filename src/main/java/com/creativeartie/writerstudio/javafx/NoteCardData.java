package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardData{
    private final ReadOnlyBooleanWrapper inUse;
    private final ReadOnlyObjectWrapper<InfoFieldType> fieldType;
    private final ReadOnlyObjectWrapper<Optional<SpanBranch>> dataText;

    NoteCardData(LinedSpanCite data, boolean source, boolean type){
        boolean use = true;
        InfoFieldType field = data.getInfoDataType();
        if(field == InfoFieldType.ERROR){
            use = false;
        }
        fieldType = new ReadOnlyStringWrapper<>(filedType);
        dataText = new ReadOnlyObjectWrapper<>(data.getData());
        inUse = new ReadOnlyBooleanWrapper(use);
    }
}
