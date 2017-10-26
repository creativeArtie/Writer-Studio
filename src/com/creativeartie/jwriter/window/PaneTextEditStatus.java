package com.creativeartie.jwriter.window;

import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.geometry.*;
import javafx.event.*;
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

public class PaneTextEditStatus extends MenuButton implements EditNode{
    private ToggleGroup dataGroup;
    
    public PaneTextEditStatus(){
        dataGroup = new ToggleGroup();
        RadioMenuItem[] list = new RadioMenuItem[EditionType.values().length];
        for(EditionType edition: EditionType.values()){
            String text = EditIcon.getKey(edition.name());
            RadioMenuItem item = new RadioMenuItem(text);
            getItems().add(item);
            item.setToggleGroup(dataGroup);
        }
        setGraphic(EditIcon.STATUS.getIcon());
    }
    
    public void update(EditUpdated list){
        setDisable(list.findBranch(MainSpanSection.class).map(span -> 
            !(span.getHeading().isPresent() || span.getOutline().isPresent())
        ).orElse(true));
        dataGroup.selectToggle(dataGroup.getToggles().get(
            list.findBranch(MainSpanSection.class)
                .map(span -> span.getEdition())
                .orElse(EditionType.NONE)
            .ordinal()));
    }
}