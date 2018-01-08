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

/**
 * The agenda pane stores a list of to do item from either complete lines or in
 * line.
 */
abstract class AgendaPaneView extends GridPane{

    /**
     * ListCell for agendaList.
     */
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

    private ListView<SpanBranch> agendaList;
    private SimpleObjectProperty<SpanBranch> selectedAgenda;
    private ReadOnlyBooleanWrapper agendaFocused;

    public AgendaPaneView(){
        agendaList = setupAgendaList();

        selectedAgenda = new SimpleObjectProperty<>(this, "selectedAgenda");
        selectedAgenda.bind(agendaList.getSelectionModel().selectedItemProperty());

        agendaFocused = new ReadOnlyBooleanWrapper(this, "agendaFocused");
        agendaFocused.bind(agendaList.focusedProperty());
    }

    /// Getters
    protected ListView<SpanBranch> getAgendaList(){
        return agendaList;
    }

    /// Layout Node
    private ListView<SpanBranch> setupAgendaList(){
        ListView<SpanBranch> ans = new ListView<>();
        ans.setCellFactory(param -> new AgendaCell());
        add(new TitledPane(WindowText.AGENDA_TITLE.getText(), ans), 0, 0);
        return ans;
    }

    /// Node Properties
    ObjectProperty<SpanBranch> selectedAgendaProperty(){
         return selectedAgenda;
    }

    SpanBranch getSelectedAgenda(){
        return selectedAgenda.getValue();
    }

    void setSelectedAgenda(SpanBranch value){
        /// selectedAgenda is bind to agendaList.getSectectionModel
        agendaList.getSelectionModel().select(value);
    }

    ReadOnlyBooleanProperty agendaFocusedProperty(){
        return agendaFocused.getReadOnlyProperty();
    }

    boolean isAgendaFocused(){
        return agendaFocused.getValue();
    }

    /// Control Methods

    /**
     * Change the selection the ListView {@linkplain #getAgendaList()}.
     */
    public abstract void updateSelection(ManuscriptDocument doc, int index);

    /**
     * Fills the ListView {@linkplain #getAgendaList()} with agendas.
     */
    public abstract void fillAgenda(ManuscriptDocument doc);
}