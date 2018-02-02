package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.collections.*;
import javafx.beans.property.*;
import javafx.scene.control.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

import com.google.common.collect.*;

/**
 * A data object for Agenda.
 */
public class TabledDataAgendaPane extends TableDataControl<TabledDataAgenda>{

    public TabledDataAgendaPane(){
        super(WindowText.AGENDA_EMPTY);
    }


    /// Layout Node
    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    protected void initColumns(){
        TableColumn<TabledDataAgenda, Number> location = TableViewHelper
            .getNumberColumn(WindowText.AGENDA_LINE, d ->
                d.agendaLineLocationProperty());
        TableViewHelper.setPrecentWidth(location, this, 10.0);

        TableColumn<TabledDataAgenda, Boolean> type = TableViewHelper
            .getBooleanColumn(WindowText.AGENDA_TYPE, d ->
                d.agendaLineTypeProperty());
        TableViewHelper.setPrecentWidth(type, this, 10.0);

        TableColumn<TabledDataAgenda, SectionSpan> section = TableViewHelper
            .getSectionColumn(WindowText.AGENDA_SECTION, d ->
                d.agendaSectionProperty());
        TableViewHelper.setPrecentWidth(section, this, 40.0);

        TableColumn<TabledDataAgenda, String> text = TableViewHelper
            .getTextColumn(WindowText.AGENDA_TEXT, d -> d.agendaTextProperty(),
            WindowText.EMPTY_TEXT);
        TableViewHelper.setPrecentWidth(text, this, 40.0);

        getColumns().addAll(location, type, section, text);
    }


    protected List<Class<? extends SpanBranch>> getTargetClass(){
        ArrayList<Class<? extends SpanBranch>> list = new ArrayList<>();
        list.add(FormatSpanAgenda.class);
        list.add(LinedSpanAgenda.class);
        return list;
    }

     protected String getCategory(){
         return AuxiliaryData.TYPE_AGENDA;
     }

     protected TabledDataAgenda buildSpan(SpanBranch span){
         return new TabledDataAgenda(span);
     }

}