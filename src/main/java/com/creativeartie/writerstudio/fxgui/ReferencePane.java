package com.creativeartie.writerstudio.fxgui;

import javafx.scene.control.*;
import javafx.collections.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

class ReferencePane extends TableView<ReferenceData>{

    ReferencePane(){
        setFixedCellSize(30);
        initColumns();
        fillRows();
    }

    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    private void initColumns(){
        TableColumn<ReferenceData, String> name = TableDataHelper
            .getTextColumn(WindowText.REF_NAME, d ->
                d.referenceNameProperty(), WindowText.EMPTY_NA);
        TableDataHelper.setPrecentWidth(name, this, 20.0);

        TableColumn<ReferenceData, String> id = TableDataHelper
            .getTextColumn(WindowText.REF_ID, d ->
                d.referenceIdProperty(), WindowText.EMPTY_NA);
        TableDataHelper.setPrecentWidth(id, this, 20.0);

        TableColumn<ReferenceData, String> describe = TableDataHelper
            .getTextColumn(WindowText.REF_LONG, d ->
                d.referenceDescriptionProperty(), WindowText.EMPTY_NA);
        TableDataHelper.setPrecentWidth(describe, this, 50.0);

        TableColumn<ReferenceData, String> example = TableDataHelper
            .getTextColumn(WindowText.REF_EXAMPLE, d ->
                d.referenceExampleProperty(), WindowText.EMPTY_NA);
        TableDataHelper.setPrecentWidth(example, this, 10.0);

        getColumns().addAll(name, id, describe, example);
    }

    private void fillRows(){
        ArrayList<ReferenceData> data = new ArrayList<>();
        for (FormatTypeField type: FormatTypeField.values()){
            if (type != FormatTypeField.ERROR){
                data.add(new ReferenceData(type));
            }
        }
        setItems(FXCollections.observableList(data));
    }


}
