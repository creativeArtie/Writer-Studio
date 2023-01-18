package com.creativeartie.writer.javafx;

import javafx.collections.*;
import javafx.scene.control.*;

import com.creativeartie.writer.lang.markup.*;
import com.creativeartie.writer.javafx.utils.*;
import static com.creativeartie.writer.javafx.utils.LayoutConstants.
    ReferenceConstants.*;

class ReferencePane extends TableView<ReferenceData>{

    ReferencePane(){
        setFixedCellSize(30);
        buildColumns();
        fillRows();
    }

    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    private void buildColumns(){
        TableColumn<ReferenceData, String> name = TableCellFactory
            .getTextColumn(REF_NAME, d ->
                d.referenceNameProperty(), EMPTY_NA);
        TableCellFactory.setPrecentWidth(name, this, NAME_COLUMN);

        TableColumn<ReferenceData, String> id = TableCellFactory
            .getTextColumn(REF_ID, d ->
                d.referenceIdProperty(), EMPTY_NA);
        TableCellFactory.setPrecentWidth(id, this, ID_COLUMN);

        TableColumn<ReferenceData, String> describe = TableCellFactory
            .getTextColumn(REF_LONG, d ->
                d.referenceDescriptionProperty(), EMPTY_NA);
        TableCellFactory.setPrecentWidth(describe, this, DESCRIBE_COLUMN);

        TableColumn<ReferenceData, String> example = TableCellFactory
            .getTextColumn(REF_EXAMPLE, d ->
                d.referenceExampleProperty(), EMPTY_NA);
        TableCellFactory.setPrecentWidth(example, this, EXAMPLE_COLUMN);

        getColumns().addAll(name, id, describe, example);
    }

    private void fillRows(){
        ObservableList<ReferenceData> data = FXCollections.observableArrayList();
        for (FormatTypeField type: FormatTypeField.values()){
            if (type != FormatTypeField.ERROR){
                data.add(new ReferenceData(type));
            }
        }
        setItems(data);
    }


}
