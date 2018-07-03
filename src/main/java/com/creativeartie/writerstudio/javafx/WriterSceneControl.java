package com.creativeartie.writerstudio.javafx;

import javafx.scene.*;
import java.util.*;
import javafx.scene.layout.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.stage.*;
import java.time.*;
import java.time.format.*;
import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

public class WriterSceneControl extends WriterSceneView {

    public WriterSceneControl(Stage window){
        super(window);
    }

    @Override
    protected void bindWritingText(ReadOnlyObjectWrapper<WritingText> prop){
        prop.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getWritingFile())
                .map(f -> f.getDocument())
                .orElse(null)
        , writingFileProperty()));
    }

    @Override
    protected void bindWritingStat(ReadOnlyObjectWrapper<WritingStat> prop){
        prop.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getWritingFile())
                .map(f -> f.getRecords())
                .orElse(null)
        , writingFileProperty()));
    }

    @Override
    protected void bindWritingData(ReadOnlyObjectWrapper<WritingData> prop){
        prop.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getWritingFile())
                .map(f -> f.getMetaData())
                .orElse(null)
        , writingFileProperty()));
    }

    @Override
    protected void bindChildren(Scene scene){
        getTextPane().setupProperties(this);
        getMainMenuBar().setupProperties(this);
        getMetaDataPane().setupProperties(this);
        getCheatsheetPane().setupProperties(this);
        for (TableDataControl<?> tab: getDataTables()){
            tab.setupProperties(this);
        }
        scene.focusOwnerProperty().addListener((d, o, n) -> refoucs(n));
    }

    private void refoucs(Node node){
        System.out.println(node);
    }
}
