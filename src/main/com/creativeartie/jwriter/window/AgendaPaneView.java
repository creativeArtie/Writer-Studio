package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.binding.*;
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

    /**
     * Property binded to agendaList.getSelectionModel().selectItemProperty().
     */
    private final ReadOnlyObjectWrapper<SpanBranch> agendaSelected;

    @SuppressWarnings("unchecked") /// For ans.getColumns().addAdd(...)
    public AgendaPaneView(){
        initAgendaColumns();
        TableViewHelper.styleTableView(this);

        agendaSelected = new ReadOnlyObjectWrapper<>(this, "agendaSelected");
        ReadOnlyObjectProperty<AgendaData> selected =
            getSelectionModel().selectedItemProperty();
        agendaSelected.bind(Bindings.createObjectBinding(() -> Optional
            .ofNullable(selected.get()) /// Maybe null
            .map(data -> data.getTargetSpan()) /// get target if found
            .orElse(null), selected));
    }

    /// Layout Node
    @SuppressWarnings("unchecked") /// For getColumns().addAdd(AgendaData ...)
    private void initAgendaColumns(){
        TableColumn<AgendaData, Number> location = TableViewHelper
            .getNumberColumn(WindowText.AGENDA_LINE, d ->
                d.agendaLineLocationProperty());
        TableViewHelper.setPrecentWidth(location, this, 10.0);

        TableColumn<AgendaData, Boolean> type = TableViewHelper
            .getBooleanColumn(WindowText.AGENDA_TYPE, d ->
                d.agendaLineTypeProperty());
        TableViewHelper.setPrecentWidth(type, this, 10.0);

        TableColumn<AgendaData, SectionSpan> section = TableViewHelper
            .getSectionColumn(WindowText.AGENDA_SECTION, d ->
                d.agendaSectionProperty());
        TableViewHelper.setPrecentWidth(section, this, 40.0);

        TableColumn<AgendaData, String> text = TableViewHelper.getTextColumn(
            WindowText.AGENDA_TEXT, d -> d.agendaTextProperty());
        TableViewHelper.setPrecentWidth(text, this, 40.0);

        getColumns().addAll(location, type, section, text);
    }

    /// Getters

    /// Node Properties
    ReadOnlyObjectProperty<SpanBranch> agendaSelectedProperty(){
         return agendaSelected.getReadOnlyProperty();
    }

    SpanBranch getAgendaSelected(){
        return agendaSelected.getValue();
    }

    /// Control Methods
}