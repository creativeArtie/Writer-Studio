package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardData{
    private final ReadOnlyBooleanWrapper inUse;
    private final ReadOnlyObjectWrapper<Object> fieldType;
    private final ReadOnlyObjectWrapper<Optional<SpanBranch>> dataText;

    NoteCardData(LinedSpanCite line, boolean source, boolean text){
        boolean use = true;
        Object field;
        switch(line.getInfoFieldType()){
        case SOURCE:
        case REF:
            use = ! source;
            field = line.getInfoFieldType();
            break;
        case IN_TEXT:
        case FOOTNOTE:
            use = ! text;
            field = line.getInfoFieldType();
            break;
        default:
            use = false;
            field = line.leafFromFirst(SpanLeafStyle.FIELD)
                .map(s -> s.getRaw())
                .orElse("");
        }
        assert field instanceof String || field instanceof InfoFieldType;
        fieldType = new ReadOnlyObjectWrapper<>(field);
        dataText = new ReadOnlyObjectWrapper<>(line.getData());
        inUse = new ReadOnlyBooleanWrapper(use);
    }

    public ReadOnlyBooleanProperty inUseProperty(){
        return inUse;
    }

    public ReadOnlyObjectProperty<Object> fieldTypeProperty(){
        return fieldType;
    }

    public ReadOnlyObjectProperty<Optional<SpanBranch>> dataTextProperty(){
        return dataText;
    }

    boolean isCitation(){
        Object found = fieldType.getValue();
        if (found instanceof InfoFieldType){
            InfoFieldType type = (InfoFieldType) found;
            return type == InfoFieldType.SOURCE || type == InfoFieldType.REF;
        }
        return false;
    }

    boolean isInText(){
        Object found = fieldType.getValue();
        if (found instanceof InfoFieldType){
            InfoFieldType type = (InfoFieldType) found;
            return type == InfoFieldType.IN_TEXT ||
                type == InfoFieldType.FOOTNOTE;
        }
        return false;
    }
}
