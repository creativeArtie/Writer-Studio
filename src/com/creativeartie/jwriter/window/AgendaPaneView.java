package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.window.*;

abstract class AgendaPaneView extends GridPane{

    private static class AgendaCell extends ListCell<SpanBranch> {

        @Override
        public void updateItem(SpanBranch item, boolean empty){
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                String text = "";
                if (item instanceof LinedSpanAgenda){
                    text = ((LinedSpanAgenda)item).getAgenda();
                } else if (item instanceof FormatSpanAgenda){
                    text = ((FormatSpanAgenda)item).getAgenda();
                }
                Label graphic = null;
                if (text.isEmpty()){
                    graphic = new Label(WindowText.AGENDA_EMPTY.getText());
                    graphic.setStyle(WindowStyle.NOT_FOUND.toCss());
                } else {
                    graphic = new Label(text);
                }
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    private ListView<SpanBranch> list;
    private SimpleObjectProperty<SpanBranch> agenda;
    private ReadOnlyBooleanWrapper agendaFocused;

    public AgendaPaneView(){
        list = new ListView<>();

        setupList();

        agenda = new SimpleObjectProperty<>(this, "agenda");
        agenda.bind(list.getSelectionModel().selectedItemProperty());

        agendaFocused = new ReadOnlyBooleanWrapper(this, "agendaFocused");
        agendaFocused.bind(list.focusedProperty());
    }

    /// Getters
    protected ListView<SpanBranch> getList(){
        return list;
    }

    /// Layout Node
    private void setupList(){
        TitledPane title = new TitledPane(WindowText.AGENDA_TITLE.getText(),
            list);
        list.setCellFactory(param -> new AgendaCell());
        add(title, 0, 0);
    }

    /// Node Properties
    ObjectProperty<SpanBranch> agendaProperty(){
         return agenda;
    }

    SpanBranch getAgenda(){
        return agenda.getValue();
    }

    void setAgenda(SpanBranch value){
         list.getSelectionModel().select(value);
    }

    ReadOnlyBooleanProperty agendaFocusedProperty(){
        return agendaFocused.getReadOnlyProperty();
    }

    boolean isAgendaFocused(){
        return agendaFocused.getValue();
    }

    /// Control Methods
    protected abstract void selectItem(int index);

    public abstract void updateSelection(ManuscriptDocument doc, int index);

    public abstract void fillAgenda(ManuscriptDocument doc);
}