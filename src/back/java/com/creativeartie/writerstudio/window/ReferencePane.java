package com.creativeartie.writerstudio.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.scene.control.cell.*;
import javafx.collections.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.resource.*;

class ReferencePane extends TableView<ReferenceData>{

    ReferencePane(){
        setFixedCellSize(30);
        initColumns();
        fillRows();
    }

    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    private void initColumns(){
        TableColumn<ReferenceData, String> name = TableViewHelper
            .getTextColumn(WindowText.REF_NAME, d ->
                d.referenceNameProperty(), WindowText.EMPTY_NA);
        TableViewHelper.setPrecentWidth(name, this, 20.0);

        TableColumn<ReferenceData, String> id = TableViewHelper
            .getTextColumn(WindowText.REF_ID, d ->
                d.referenceIdProperty(), WindowText.EMPTY_NA);
        TableViewHelper.setPrecentWidth(id, this, 20.0);

        TableColumn<ReferenceData, String> describe = TableViewHelper
            .getTextColumn(WindowText.REF_LONG, d ->
                d.referenceDescriptionProperty(), WindowText.EMPTY_NA);
        TableViewHelper.setPrecentWidth(describe, this, 50.0);

        TableColumn<ReferenceData, String> example = TableViewHelper
            .getTextColumn(WindowText.REF_EXAMPLE, d ->
                d.referenceExampleProperty(), WindowText.EMPTY_NA);
        TableViewHelper.setPrecentWidth(example, this, 10.0);

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
