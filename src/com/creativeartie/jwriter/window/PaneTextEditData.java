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

class PaneTextEditData extends PaneTextEditCommon {

    public PaneTextEditData(){
        super(EditIcon.DATA_TEXT);
    }

    public void update(EditUpdated list){
        switch(list.findBranch(LinedSpan.class).get().getLinedType()){
            case FOOTNOTE:
            case ENDNOTE:
            case HYPERLINK:
            case SOURCE:
                setDisabled(false);
                break;
            default:
                setDisabled(true);
                setSelected(false);
                return;
        }
        setSelected(list.findBranch(FormatSpan.class).isPresent() ||
            list.findBranch(ContentSpan.class).isPresent());
    }
}