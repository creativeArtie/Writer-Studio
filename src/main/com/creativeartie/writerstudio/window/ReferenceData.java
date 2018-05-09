package com.creativeartie.writerstudio.window;

import java.util.*;
import javafx.collections.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.CURLY_KEY;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.CURLY_END;

import com.google.common.collect.*;

/**
 * A data object for FieldTyp.
 */
public class ReferenceData{
    private final ReadOnlyStringWrapper referenceName;
    private final ReadOnlyStringWrapper referenceId;
    private final ReadOnlyStringWrapper referenceDescription;
    private final ReadOnlyStringWrapper referenceExample;

    ReferenceData(TextDataType.FieldType field){
        referenceName = new ReadOnlyStringWrapper(WindowText
            .getNameText(field));
        referenceId = new ReadOnlyStringWrapper(CURLY_KEY + field
            .getFieldKey() + CURLY_END);
        referenceDescription = new ReadOnlyStringWrapper(WindowText
            .getDescriptionText(field));
        referenceExample = new ReadOnlyStringWrapper(WindowText
            .getExampleText(field));
    }

    public ReadOnlyStringProperty referenceNameProperty(){
        return referenceName.getReadOnlyProperty();
    }

    public String getReferenceName(){
        return referenceName.getValue();
    }

    public ReadOnlyStringProperty referenceIdProperty(){
        return referenceId.getReadOnlyProperty();
    }

    public String getReferenceId(){
        return referenceId.getValue();
    }

    public ReadOnlyStringProperty referenceDescriptionProperty(){
        return referenceDescription.getReadOnlyProperty();
    }

    public String getReferenceDescription(){
        return referenceDescription.getValue();
    }

    public ReadOnlyStringProperty referenceExampleProperty(){
        return referenceExample.getReadOnlyProperty();
    }

    public String getReferenceExample(){
        return referenceExample.getValue();
    }
}