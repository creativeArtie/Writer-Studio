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
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                String text = "";
                if (item instanceof LinedSpanAgenda){
                    /// The selected agenda is a LinedSpanAgenda
                    text = ((LinedSpanAgenda)item).getAgenda();
                } else if (item instanceof FormatSpanAgenda){
                    /// The selected agenda is a FormatSpanAgenda
                    text = ((FormatSpanAgenda)item).getAgenda();
                }
                Label graphic = null;
                if (text.isEmpty()){
                    /// There is no text found.
                    graphic = new Label(WindowText.AGENDA_EMPTY.getText());
                    StyleClass.NO_TEXT.addClass(graphic);
                } else {
                    /// Add the text that is found.
                    graphic = new Label(text);
                }

                /// Completing the setting
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    private final ListView<SpanBranch> agendaList;

    /**
     * Property binded to agendaList.getSelectionModel().selectItemProperty().
     */
    private final SimpleObjectProperty<SpanBranch> agendaSelected;
    /** Property bined to agendaList.foucsedProperty(). */
    private final ReadOnlyBooleanWrapper agendaFocused;

    public AgendaPaneView(){
        agendaList = setupAgendaList();

        agendaSelected = new SimpleObjectProperty<>(this, "agendaSelected");
        agendaSelected.bind(agendaList.getSelectionModel().selectedItemProperty());

        agendaFocused = new ReadOnlyBooleanWrapper(this, "agendaFocused");
        agendaFocused.bind(agendaList.focusedProperty());
    }

    /// Layout Node
    private ListView<SpanBranch> setupAgendaList(){
        ListView<SpanBranch> ans = new ListView<>();
        ans.setCellFactory(param -> new AgendaCell());
        add(new TitledPane(WindowText.AGENDA_TITLE.getText(), ans), 0, 0);
        return ans;
    }

    /// Getters
    protected ListView<SpanBranch> getAgendaList(){
        return agendaList;
    }

    /// Node Properties
    ObjectProperty<SpanBranch> agendaSelectedProperty(){
         return agendaSelected;
    }

    SpanBranch getAgendaSelected(){
        return agendaSelected.getValue();
    }

    void setAgendaSelected(SpanBranch value){
        /// agendaSelected is bind to agendaList.getSectectionModel
        agendaList.getSelectionModel().select(value);
    }

    ReadOnlyBooleanProperty agendaFocusedProperty(){
        return agendaFocused.getReadOnlyProperty();
    }

    boolean isAgendaFocused(){
        return agendaFocused.getValue();
    }

    /// Control Methods
}
