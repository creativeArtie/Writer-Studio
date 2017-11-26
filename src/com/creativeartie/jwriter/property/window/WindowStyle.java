package com.creativeartie.jwriter.property.window;

import java.util.*;
import java.io.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.control.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.Span;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.*;
public enum WindowStyle {
    NOT_FOUND("Other.NotFound"), EMPTY_TITLE("Other.EmptyNoteTitle"),
    NOTE_HEADING("ListNote.NoteHeading"), NUMBERED_ID("ListNote.NameCell"),
    MARKUP_Set("CheatSheet.HasSet"),MARKUP_UNSET("CheatSheet.NotSet"),
    CHEATSHEET_BASE("CheatSheet.Base");

    private static PropertyManager styleManager;

    private static PropertyManager getManager(){
        if (styleManager == null){
            try {
                styleManager = new PropertyManager("data/parse-styles",
                    "data/user-styles");
            } catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
        return styleManager;
    }

    static StyleProperty buildStyle(String key){
        return getManager().getStyleProperty(key);
    }

    private StyleProperty styleProp;

    private WindowStyle(String key){
        styleProp = getManager().getStyleProperty(key);
    }

    public StyleProperty getProperty(){
        return styleProp;
    }

    public String toCss(){
        return styleProp.toCss();
    }

    public static StyleProperty getStyle(String key){
        return getManager().getStyleProperty(key);
    }
}