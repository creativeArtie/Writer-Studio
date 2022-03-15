package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.scene.control.*;
import javafx.scene.text.*;

import com.creativeartie.writer.lang.markup.*;

import com.creativeartie.writer.javafx.utils.*;
import com.creativeartie.writer.javafx.utils.LayoutConstants.
    NoteCardConstants.*;

class NoteCardDetailPaneControl extends NoteCardDetailPaneView{
    /// %Part 1: setupChildren

    private ShowMeta showMeta;
    private FilteredList<NoteCardData> metaList;

    @Override
    protected void bindChildren(WriterSceneControl control){
        showCardProperty().addListener((d, o, n) -> loadCard(n));
        getShowMetaBox().getSelectionModel().selectedItemProperty().addListener(
            (d, o, n) -> listenShowMeta(n));
        showMeta = getShowMetaBox().getSelectionModel().getSelectedItem();
        loadCard(null);
    }

    private void loadCard(NoteCardSpan span){
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
