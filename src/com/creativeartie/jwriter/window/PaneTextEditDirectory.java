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

import com.google.common.collect.*;

class PaneTextEditDirectory extends PaneTextEditCommon {
    public static PaneTextEditDirectory[] getSpans(){
        return new PaneTextEditDirectory[]{
            new PaneTextEditDirectory(EditIcon.FOOTNOTE_SPAN,
                DirectoryType.FOOTNOTE),
            new PaneTextEditDirectory(EditIcon.ENDNOTE_SPAN,
                DirectoryType.ENDNOTE),
            new PaneTextEditDirectory(EditIcon.CITE_SPAN,
                DirectoryType.NOTE)
        };
    }

    private DirectoryType testType;

    private PaneTextEditDirectory(EditIcon icon, DirectoryType type){
        super(icon);
        testType = type;
    }

    public void update(EditUpdated list){
        Optional<FormatSpanDirectory> found = list
            .findBranch(FormatSpanDirectory.class);
        if (found.isPresent() && found.get().getIdType() == testType){
            setSelected(true);
            setDisable(false);
        } else {
            setSelected(false);
            setDisable(list.hasContentSpan());
        }
    }
}