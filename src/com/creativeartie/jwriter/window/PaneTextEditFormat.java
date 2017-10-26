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

class PaneTextEditFormat extends PaneTextEditCommon {

    public static PaneTextEditFormat[] getFormats(){
        return new PaneTextEditFormat[]{
            new PaneTextEditFormat(EditIcon.BOLD_FORMAT, span -> span.isBold()),
            new PaneTextEditFormat(EditIcon.ITALIC_FORMAT, span -> 
                span.isItalics()),
            new PaneTextEditFormat(EditIcon.UNDERLINE_FORMAT, span -> 
                span.isUnderline()),
            new PaneTextEditFormat(EditIcon.CODE_FORMAT, span -> 
                span.isCoded())};
    }
    
    private Predicate<FormatSpan> postUpdater;
    
    private PaneTextEditFormat(EditIcon icon, Predicate<FormatSpan> update){
        super(icon);
        postUpdater = update;
    }

    public void update(EditUpdated list){
        Optional<FormatSpan> found = list.getFormatSpan();
        setDisable(! found.isPresent());
        setSelected(found.map(span -> postUpdater.test(span)).orElse(false));
    }
}