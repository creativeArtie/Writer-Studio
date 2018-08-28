package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardData{
    private final ReadOnlyBooleanWrapper inUse;
    private final ReadOnlyObjectWrapper<InfoFieldType> fieldType;
    private final ReadOnlyObjectWrapper<Optional<SpanBranch>> dataText;

    NoteCardData(LinedSpanCite data, boolean source, boolean text){
        boolean use = true;
        InfoFieldType field = data.getInfoFieldType();
        switch(field){
        case ERROR:
            use = false;
            break;
        case SOURCE:
        case REF:
            use = ! source;
            break;
        case IN_TEXT:
        case FOOTNOTE:
            use = ! text;
        }
        fieldType = new ReadOnlyObjectWrapper<>(field);
        dataText = new ReadOnlyObjectWrapper<>(data.getData());
        inUse = new ReadOnlyBooleanWrapper(use);
    }

    public ReadOnlyBooleanProperty inUseProperty(){
        return inUse;
    }

    public ReadOnlyObjectProperty<InfoFieldType> fieldTypeProperty(){
        return fieldType;
    }

    public ReadOnlyObjectProperty<Optional<SpanBranch>> dataTextProperty(){
        return dataText;
    }

    boolean isCitation(){
        InfoFieldType type = fieldType.getValue();
        return type == InfoFieldType.SOURCE || type == InfoFieldType.REF;
    }

    boolean isInText(){
        InfoFieldType type = fieldType.getValue();
        return type == InfoFieldType.IN_TEXT || type == InfoFieldType.FOOTNOTE;
    }
}
