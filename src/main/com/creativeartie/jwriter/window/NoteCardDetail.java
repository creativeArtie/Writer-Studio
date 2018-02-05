package com.creativeartie.jwriter.window;

import java.util.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.resource.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class NoteCardDetail extends TitledPane{

    public NoteCardDetail(){
        setCollapsible(false);
        clearContent();
    }

    public void clearContent(){
        setText(WindowText.NOTE_CARD_PLACEHOLDER_TITLE.getText());
        setContent(new Label(WindowText.NOTE_CARD_PLACHOLDER_DETAIL.getText()));
    }
}