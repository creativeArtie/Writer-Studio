package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.stage.*;

import org.fxmisc.richtext.*;
import com.creativeartie.writer.lang.markup.*;

public class WriterSceneControl extends WriterSceneView {

    /// %Part 1: Constructor and public methods

    public WriterSceneControl(Stage window){
        super(window);
    }

    public void setWritingFile(WritingFile file){
        getMainMenuBar().setWritingFile(file);
    }

    public void listenWriterFile(ChangeListener<WritingFile> listener){
        getMainMenuBar().writingFileProperty().addListener(listener);
    }

    public void refocusText(){
        setRefocusText(true);
    }

    /// %Part 2: Property Binding

    @Override
    protected void bindWritingText(ReadOnlyObjectWrapper<WritingText> property){
        property.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getMainMenuBar().getWritingFile())
                .map(f -> f.getDocument())
                .orElse(null)
        , getMainMenuBar().writingFileProperty()));
    }

    @Override
    protected void bindWritingStat(ReadOnlyObjectWrapper<WritingStat> property){
        property.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getMainMenuBar().getWritingFile())
                .map(f -> f.getRecords())
                .orElse(null)
        , getMainMenuBar().writingFileProperty()));
    }

    @Override
    protected void bindWritingData(ReadOnlyObjectWrapper<WritingData> property){
        property.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getMainMenuBar().getWritingFile())
                .map(f -> f.getMetaData())
                .orElse(null)
        , getMainMenuBar().writingFileProperty()));
    }

    /// %Part 3: Bind Children Properties

    protected void bindChildren(Scene scene){
        getMainMenuBar().postLoad(this);

        getNoteCardPane().postLoad(this);
        getAgendaPane().postLoad(this);
        getLinkPane().postLoad(this);
        getFootnotePane().postLoad(this);
        getEndnotePane().postLoad(this);

        getCheatsheetPane().postLoad(this);

        getHeadingPane().postLoad(this);
        getMetaDataPane().postLoad(this);
        getTextPane().postLoad(this);
        getResearchPane().postLoad(this);

        refocusTextProperty().addListener((d, o, n) -> listenRefocusText(n));
    }

    private void listenRefocusText(boolean refocus){
        if (refocus){
            Platform.runLater( () -> {
                getMainTabPane().getSelectionModel().selectFirst();
                InlineCssTextArea area = getTextPane().getTextArea();
                area.requestFollowCaret();
                area.requestFocus();
                setRefocusText(false);
            });
        }
    }

}
