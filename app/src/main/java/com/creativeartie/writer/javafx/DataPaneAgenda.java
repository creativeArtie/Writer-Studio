package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.scene.control.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.*;
import com.creativeartie.writer.javafx.utils.*;
import static com.creativeartie.writer.javafx.utils.LayoutConstants.
    AgendaConstants.*;

/**
 * A data object for Agenda.
 */
public class DataPaneAgenda extends DataControl<DataInputAgenda>{

    public DataPaneAgenda(){
        super(EMPTY_TEXT);
    }

    @Override
    /// Layout Node
    @SuppressWarnings("unchecked") /// For getColumns().addAdd(TableColumn ...)
    protected void buildColumns(){
        TableColumn<DataInputAgenda, Number> location = TableCellFactory
            .getNumberColumn(LINE_NAME, d ->
                d.agendaLineLocationProperty());
        TableCellFactory.setPrecentWidth(location, this, LINE_WIDTH);

        TableColumn<DataInputAgenda, Boolean> type = TableCellFactory
            .getBooleanColumn(TYPE_NAME, d ->
                d.agendaLineTypeProperty());
        TableCellFactory.setPrecentWidth(type, this, TYPE_WIDTH);

        TableColumn<DataInputAgenda, SectionSpan> section = TableCellFactory
            .getSectionColumn(SECTION_NAME, d ->
                d.agendaSectionProperty());
        TableCellFactory.setPrecentWidth(section, this, SECTION_WIDTH);

        TableColumn<DataInputAgenda, String> text = TableCellFactory
            .getTextColumn(TEXT_NAME, d -> d.agendaTextProperty(), EMPTY_TEXT);
        TableCellFactory.setPrecentWidth(text, this, TEXT_WIDTH);

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
     protected DataInputAgenda buildSpan(SpanBranch span){
         return new DataInputAgenda(span);
     }

}
