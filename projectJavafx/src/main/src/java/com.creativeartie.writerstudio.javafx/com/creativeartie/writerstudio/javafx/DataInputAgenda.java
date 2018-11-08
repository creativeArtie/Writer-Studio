package com.creativeartie.writerstudio.javafx;

import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

/**
 * A data object for Agenda.
 */
public class DataInputAgenda implements DataInput{
    private final ReadOnlyIntegerWrapper agendaLineLocation;
    private final ReadOnlyBooleanWrapper agendaLineType;
    private final ReadOnlyObjectWrapper<SectionSpan> agendaSection;
    private final ReadOnlyStringWrapper agendaText;
    private final SpanBranch targetAgenda;

    DataInputAgenda(SpanBranch span){
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

    @Override
    public SpanBranch getTargetSpan(){
        return targetAgenda;
    }
}
