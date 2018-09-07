package com.creativeartie.writerstudio.javafx;

import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    ReferenceConstants.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.CURLY_KEY;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.CURLY_END;

/**
 * A data object for FieldType.
 */
public class ReferenceData{
    private final ReadOnlyStringWrapper referenceName;
    private final ReadOnlyStringWrapper referenceId;
    private final ReadOnlyStringWrapper referenceDescription;
    private final ReadOnlyStringWrapper referenceExample;

    ReferenceData(FormatTypeField field){
        referenceName = new ReadOnlyStringWrapper(getNameText(field));
        referenceId = new ReadOnlyStringWrapper(CURLY_KEY + field
            .getFieldKey() + CURLY_END);
        referenceDescription = new ReadOnlyStringWrapper(getDescriptionText(field));
        referenceExample = new ReadOnlyStringWrapper(getExampleText(field));
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
