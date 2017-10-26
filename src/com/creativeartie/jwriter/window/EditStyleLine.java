package com.creativeartie.jwriter.window;

import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.scene.control.cell.*;
import javafx.scene.control.*;
import java.util.*;
import java.util.Optional;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

import com.google.common.base.*;

public class EditStyleLine{

    private LinedType linedType;
    private Optional<Integer> styleLevel;
    private EditIcon editIcon;
    private String displayText;

    public EditStyleLine(LinedType type){
        this(type, Optional.empty(), type.name());
    }

    public EditStyleLine(LinedType type, int level){
        this(type, Optional.empty(), type.name() + level);
    }

    private EditStyleLine(LinedType type, Optional<Integer> level, String key){
        linedType = type;
        styleLevel = level;
        editIcon = EditIcon.valueOf(type.name() + "_LINE");
        displayText = EditIcon.getKey(key);
    }

    
    public ImageView getIcon(){
        return editIcon.getIcon();
    }
    
    public String getDisplayText(){
        return displayText;
    }
    
    public boolean isMatch(LinedType type, int level){
        /// on `.map` -> not style level (ie. Agenda, Break, etc) or match level 
        return linedType == type && styleLevel.map(i -> level == i).orElse(true);
    }
        
    @Override
    public String toString(){
        return displayText;
    }
}