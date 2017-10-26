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

class PaneTextEditSpanAgenda extends PaneTextEditCommon {

    public PaneTextEditSpanAgenda(){
        super(EditIcon.AGENDA_SPAN);
    }

    public void update(EditUpdated list){
        if(list.findBranch(FormatSpanAgenda.class).isPresent()){
            setSelected(true);
            setDisable(false);
        } else {
            setSelected(false);
            setDisable(list.hasContentSpan());
        }
    }
}