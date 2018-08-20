package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.stage.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.markup.*;

public class WriterSceneControl extends WriterSceneView {

    /// %Part 1: Public methods

    public WriterSceneControl(Stage window){
        super(window);
    }

    public void setWritingFile(WritingFile file){
        getMainMenuBar().setWritingFile(file);
    }

    public void listenWriterFile(ChangeListener<WritingFile> listener){
        getMainMenuBar().writingFileProperty().addListener(listener);
    }

    /// %Part 2: Property Binding

    @Override
    protected void bindWritingText(ReadOnlyObjectWrapper<WritingText> prop){
        prop.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getMainMenuBar().getWritingFile())
                .map(f -> f.getDocument())
                .orElse(null)
        , getMainMenuBar().writingFileProperty()));
    }

    @Override
    protected void bindWritingStat(ReadOnlyObjectWrapper<WritingStat> prop){
        prop.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getMainMenuBar().getWritingFile())
                .map(f -> f.getRecords())
                .orElse(null)
        , getMainMenuBar().writingFileProperty()));
    }

    @Override
    protected void bindWritingData(ReadOnlyObjectWrapper<WritingData> prop){
        prop.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getMainMenuBar().getWritingFile())
                .map(f -> f.getMetaData())
                .orElse(null)
        , getMainMenuBar().writingFileProperty()));
    }

    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(Scene scene){
        getTextPane().setupProperties(this);
        getResearchPane().setupProperties(this);
        getMainMenuBar().setupProperties(this);
        getMetaDataPane().setupProperties(this);
        getCheatsheetPane().setupProperties(this);
        getHeadingsPane().setupProperties(this);
        for (TableDataControl<?> tab: getDataTables()){
            tab.setupProperties(this);
        }
        getNoteCardPane().setupProperties(this);

        scene.focusOwnerProperty().addListener((d, o, n) -> refocus(n));
        refocusTextProperty().addListener((d, o, n) ->
            refocus(scene.getFocusOwner())
        );
    }

    /// %Part 3.1:  scene.focusOwnerProperty() and refocusTextProperty()

    private void refocus(Node node){
        if (isRefocusText()){
            Platform.runLater( () -> {
                InlineCssTextArea area = getTextPane().getTextArea();
                area.requestFollowCaret();
                area.requestFocus();
                setRefocusText(false);
            });
        }
    }
}
