package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.collections.*;
import javafx.beans.property.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

import com.google.common.collect.*;

/**
 * A data object for Agenda.
 */
public class AgendaData{
    private final ReadOnlyIntegerWrapper agendaLineLocation;
    private final ReadOnlyBooleanWrapper agendaLineType;
    private final ReadOnlyObjectWrapper<SectionSpan> agendaSection;
    private final ReadOnlyStringWrapper agendaText;
    private final SpanBranch targetAgenda;

    public static ObservableList<AgendaData> extractList(WritingText text){
        ArrayList<AgendaData> list = new ArrayList<>();
        for (SpanBranch span: text.getCatalogue().getIds(AuxiliaryData
                .TYPE_AGENDA)){
            if (span instanceof FormatSpanAgenda ||
                    span instanceof LinedSpanAgenda){
                list.add(new AgendaData(span));
            }
        }
        return FXCollections.observableList(list);
    }

    private AgendaData(SpanBranch span){
        assert span instanceof FormatSpanAgenda ||
            span instanceof LinedSpanAgenda: "Wrong span class: " + span;

        boolean type = false;
        String text = null;
        if (span instanceof FormatSpanAgenda){
            text = ((FormatSpanAgenda)span).getAgenda();
        } else {
            assert span instanceof LinedSpanAgenda: "Wrong span class: " + span;
            type = true;
            text = ((LinedSpanAgenda)span).getAgenda();
        }

        agendaLineLocation = new ReadOnlyIntegerWrapper(span.getStartLine());
        agendaLineType = new ReadOnlyBooleanWrapper(type);
        agendaSection = new ReadOnlyObjectWrapper<>(span.getParent(SectionSpan
            .class).orElse(null));
        agendaText = new ReadOnlyStringWrapper(text);
        targetAgenda = span;
    }

    public ReadOnlyIntegerProperty agendaLineLocationProperty(){
        return agendaLineLocation.getReadOnlyProperty();
    }

    public int getAgendaLocaion(){
        return agendaLineLocation.getValue();
    }

    public ReadOnlyBooleanProperty agendaLineTypeProperty(){
        return agendaLineType.getReadOnlyProperty();
    }

    public boolean isAgendaLine(){
        return agendaLineType.getValue();
    }

    public ReadOnlyObjectProperty<SectionSpan> agendaSectionProperty(){
        return agendaSection.getReadOnlyProperty();
    }

    public SectionSpan getAgendaSection(){
        return agendaSection.getValue();
    }

    public ReadOnlyStringProperty agendaTextProperty(){
        return agendaText.getReadOnlyProperty();
    }

    public String getAgendaText(){
        return agendaText.getValue();
    }

    public SpanBranch getTargetSpan(){
        return targetAgenda;
    }
}