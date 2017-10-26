package com.creativeartie.jwriter.window;

import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.collections.*;
import javafx.collections.*;
import javafx.beans.binding.*;
import javafx.util.*;
import java.util.*;
import java.util.Optional;
import java.util.function.*;
import java.util.function.Function;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

import com.google.common.base.*;
import com.google.common.collect.*;

class PaneTextEditStyle extends ComboBox<EditStyleLine> implements EditNode {

    private static class StyleCell extends ListCell<EditStyleLine>{
        @Override
        public void updateItem(EditStyleLine item, boolean empty){
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(new Label(item.getDisplayText(), item.getIcon()));
                setText(null);
            }
        }
    }

    PaneTextEditStyle(){
        ObservableList<EditStyleLine> list = FXCollections.observableArrayList();
        final int LEVEL_MAX = AuxiliaryData.LEVEL_MAX + 1;
        list.add(new EditStyleLine(LinedType.PARAGRAPH));
        for (int i = 1; i < LEVEL_MAX; i++){
            list.add(new EditStyleLine(LinedType.HEADING, i));
        }
        list.add(new EditStyleLine(LinedType.BREAK));
        for (int i = 1; i < LEVEL_MAX; i++){
            list.add(new EditStyleLine(LinedType.NUMBERED, i));
        }
        for (int i = 1; i < LEVEL_MAX; i++){
            list.add(new EditStyleLine(LinedType.BULLET, i));
        }
        list.add(new EditStyleLine(LinedType.QUOTE));
        list.add(new EditStyleLine(LinedType.HYPERLINK));
        list.add(new EditStyleLine(LinedType.FOOTNOTE));
        list.add(new EditStyleLine(LinedType.ENDNOTE));
        for (int i = 1; i < LEVEL_MAX; i++){
            list.add(new EditStyleLine(LinedType.OUTLINE, i));
        }
        list.add(new EditStyleLine(LinedType.NOTE));
        list.add(new EditStyleLine(LinedType.SOURCE));
        list.add(new EditStyleLine(LinedType.AGENDA));
        setItems(list);

        Callback<ListView<EditStyleLine>,ListCell<EditStyleLine>> factory =
            combo -> new StyleCell();
        setCellFactory(factory);
        /// setButtonCell(factory.call(null));
    }

    @Override
    public void update(EditUpdated list){
        Optional<LinedSpan> found = list.findBranch(LinedSpan.class);
        assert found.isPresent();
        LinedType line = found.get().getLinedType();
        int level = found.filter(span -> span instanceof LinedSpanLevel)
            .map(span -> ((LinedSpanLevel)span).getLevel())
            .orElse(-1);
        for (EditStyleLine node: getItems()){
            if (node.isMatch(line, level)){
                getSelectionModel().select(node);
                return;
            }
        }
    }
}