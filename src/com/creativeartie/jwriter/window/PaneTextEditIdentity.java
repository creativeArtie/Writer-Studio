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

class PaneTextEditIdentity extends PaneTextEditCommon {
    
    public PaneTextEditIdentity(){
        super(EditIcon.ID);
    }

    public void update(EditUpdated list){
        setSelected(
            list.findBranch(DirectorySpan.class)
                .filter(span -> span.getParent() instanceof LinedSpanLevel ||
                    span.getParent() instanceof LinedSpanNote)
                .isPresent()
        );
    }
}