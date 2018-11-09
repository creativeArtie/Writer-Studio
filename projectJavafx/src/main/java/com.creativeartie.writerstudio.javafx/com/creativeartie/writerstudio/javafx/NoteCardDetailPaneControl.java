package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.scene.control.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    NoteCardConstants.*;

class NoteCardDetailPaneControl extends NoteCardDetailPaneView{
    /// %Part 1: setupChildren

    private NoteCardSpan selectedSpan;
    private ShowMeta showMeta;
    private FilteredList<NoteCardData> metaList;
    private ObjectProperty<SpanBranch> lastSelected;

    @Override
    protected void bindChildren(WriterSceneControl control){
        showCardProperty().addListener((d, o, n) -> loadCard(n));

        getToNoteButton().setOnAction(e -> listenToNoteButton());
        getShowMetaBox().getSelectionModel().selectedItemProperty().addListener(
            (d, o, n) -> listenShowMeta(n));
        showMeta = getShowMetaBox().getSelectionModel().getSelectedItem();

        lastSelected = control.lastSelectedProperty();

        loadCard(null);
    }

    private void loadCard(NoteCardSpan span){
        selectedSpan = span;
        if (span == null){
            getTabs().clear();
            getTabs().add(getEmptyTab());
            return;
        }
        getTabs().clear();
        getTabs().addAll(getMainTab(), getMetaTab());

        TextFlow title = getNoteTitle();
        title.getChildren().clear();
        TextFlowBuilder.loadFormatText(title, span.getTitle());
        title.getChildren().forEach(
            s -> s.getStyleClass().add(NOTE_HEADING_STYLE)
        );

        TextFlow content = getNoteContent();
        content.getChildren().clear();
        for (FormattedSpan child: span.getContent()){
            TextFlowBuilder.loadFormatText(content, Optional.of(child));
            content.getChildren().add(new Text("\n"));
        }

        TableView<NoteCardData> meta = getNoteMetaTable();
        ObservableList<NoteCardData> list = FXCollections.observableArrayList();
        boolean source = false;
        boolean intext = false;
        for (LinedSpanCite cite: span.getChildren(LinedSpanCite.class)){
            NoteCardData data = new NoteCardData(cite, source, intext);
            source = source || data.isCitation();
            intext = intext || data.isInText();
            list.add(data);
        }
        metaList = new FilteredList<>(list);
        metaList.setPredicate(this::checkData);
        meta.setItems(metaList);
    }

    private void listenToNoteButton(){
        if (selectedSpan != null && ! selectedSpan.isRemoved()){
            lastSelected.setValue(selectedSpan);
        }
    }

    private void listenShowMeta(ShowMeta meta){
        showMeta = meta == null? ShowMeta.ALL: meta;
        metaList.setPredicate(this::checkData);
    }

    private boolean checkData(NoteCardData data){
        switch(showMeta){
        case UNUSED:
            return ! data.inUseProperty().getValue();
        case USED:
            return data.inUseProperty().getValue();
        default:
            return true;
        }
    }
}
