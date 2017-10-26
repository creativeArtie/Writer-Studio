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

class PaneTextEditLink extends PaneTextEditCommon {
    
    public static PaneTextEditLink[] getLinks(){
        return new PaneTextEditLink[]{
            new PaneTextEditLink(EditIcon.LINK_DIRECT, FormatSpanLinkDirect.class),
            new PaneTextEditLink(EditIcon.LINK_REF, FormatSpanLinkRef.class)
        };
    }
    
    private Class<? extends FormatSpanLink> checkClass;
    
    private PaneTextEditLink(EditIcon icon, Class<? extends FormatSpanLink> clz){
        super(icon);
        checkClass = clz;
    }

    public void update(EditUpdated list){
        setSelected(list.findBranch(checkClass).isPresent());
        setDisable(list.hasContentSpan());
    }
}