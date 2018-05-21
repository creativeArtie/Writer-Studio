package com.creativeartie.writerstudio.window;

import java.util.*;
import javafx.collections.*;
import javafx.beans.property.*;
import javafx.scene.control.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.google.common.collect.*;

/**
 * A data object for Agenda.
 */
public class TableAgendaPane extends TableDataControl<TableAgendaData>{

    public TableAgendaPane(){
        super(WindowText.AGENDA_EMPTY);
    }

    @Override
    /// Layout Node
    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    protected void initColumns(){
        TableColumn<TableAgendaData, Number> location = TableViewHelper
            .getNumberColumn(WindowText.AGENDA_LINE, d ->
                d.agendaLineLocationProperty());
        TableViewHelper.setPrecentWidth(location, this, 10.0);

        TableColumn<TableAgendaData, Boolean> type = TableViewHelper
            .getBooleanColumn(WindowText.AGENDA_TYPE, d ->
                d.agendaLineTypeProperty());
        TableViewHelper.setPrecentWidth(type, this, 10.0);

        TableColumn<TableAgendaData, SectionSpan> section = TableViewHelper
            .getSectionColumn(WindowText.AGENDA_SECTION, d ->
                d.agendaSectionProperty());
        TableViewHelper.setPrecentWidth(section, this, 40.0);

        TableColumn<TableAgendaData, String> text = TableViewHelper
            .getTextColumn(WindowText.AGENDA_TEXT, d -> d.agendaTextProperty(),
            WindowText.EMPTY_TEXT);
        TableViewHelper.setPrecentWidth(text, this, 40.0);

        getColumns().addAll(location, type, section, text);
    }

    @Override
    protected List<Class<? extends SpanBranch>> getTargetClass(){
        ArrayList<Class<? extends SpanBranch>> list = new ArrayList<>();
        list.add(FormatSpanAgenda.class);
        list.add(LinedSpanAgenda.class);
        return list;
    }

    @Override
     protected String getCategory(){
         return AuxiliaryData.TYPE_AGENDA;
     }

    @Override
     protected TableAgendaData buildSpan(SpanBranch span){
         return new TableAgendaData(span);
     }

}