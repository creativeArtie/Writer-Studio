package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.javafx.utils.*;

class NoteCardDetailPaneControl extends NoteCardDetailPaneView{
    /// %Part 1: setupChildren

    private NoteCardSpan selectedSpan;

    @Override
    protected void bindChildren(WriterSceneControl control){
        showCardProperty().addListener((d, o, n) -> loadCard(n));
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

        TextFlow content = getNoteContent();
        content.getChildren().clear();
        for (FormattedSpan child: span.getContent()){
            TextFlowBuilder.loadFormatText(content, Optional.of(child));
            content.getChildren().add(new Text("\n"));
        }

    }
}
