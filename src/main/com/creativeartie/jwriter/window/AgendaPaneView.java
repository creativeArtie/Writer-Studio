package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.scene.control.cell.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.window.*;

/**
 * The agenda pane stores a list of to do item from either complete lines or in
 * line.
 */
abstract class AgendaPaneView extends TableView<AgendaData>{
    private static class SectionCell extends TableCell<AgendaData, SectionSpan>{
        @Override
        protected void updateItem(SectionSpan item, boolean empty) {
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
            } else {
                /// Allows WindowSpanParser to create the Label
                TextFlow graphic = TextFlowBuilder.loadHeadingLine(item
                    .getHeading());
                setText(null);
                setGraphic(graphic);
            }
        }
    }


    /**
     * ListCell for agendaList.
     */
    private static class TextCell extends TableCell<AgendaData, String> {

        @Override
        public void updateItem(String item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Label graphic = null;
                if (item.isEmpty()){
                    /// There is no text found.
                    graphic = new Label(WindowText.AGENDA_NOTEXT.getText());
                    StyleClass.NO_TEXT.addClass(graphic);
                } else {
                    /// Add the text that is found.
                    graphic = new Label(item);
                }

                /// Completing the setting
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    /**
     * Property binded to agendaList.getSelectionModel().selectItemProperty().
     */
    private final SimpleObjectProperty<SpanBranch> agendaSelected;

    @SuppressWarnings("unchecked") /// For ans.getColumns().addAdd(...)
    public AgendaPaneView(){
        initAgendaColumns();

        agendaSelected = new SimpleObjectProperty<>(this, "agendaSelected");
        // agendaSelected.bind(agendaList.getSelectionModel().selectedItemProperty());
    }

    /// Layout Node
    @SuppressWarnings("unchecked") /// For getColumns().addAdd(AgendaData ...)
    private void initAgendaColumns(){
        TableColumn<AgendaData, Integer> location =
            new TableColumn<>(WindowText.AGENDA_LINE.getText());
        
        TableColumn<AgendaData, Boolean> type =
            new TableColumn<>(WindowText.AGENDA_TYPE.getText());
        
        TableColumn<AgendaData, SectionSpan> section =
            new TableColumn<>(WindowText.AGENDA_SECTION.getText());
        section.setCellFactory(list -> new SectionCell());
        
        TableColumn<AgendaData, String> text =
            new TableColumn<>(WindowText.AGENDA_TEXT.getText());
        getColumns().addAll(location, type, section, text);
        text.setCellFactory(list -> new TextCell());
    }

    /// Getters

    /// Node Properties
    ObjectProperty<SpanBranch> agendaSelectedProperty(){
         return agendaSelected;
    }

    SpanBranch getAgendaSelected(){
        return agendaSelected.getValue();
    }

    void setAgendaSelected(SpanBranch value){
        /// agendaSelected is bind to agendaList.getSectectionModel
        // agendaList.getSelectionModel().select(value);
    }

    /// Control Methods
}