package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.window.*;

class CheatsheetPaneControl extends CheatsheetPaneView{

    public void updateLabels(ManuscriptDocument doc, int position){
        for (CheatsheetLabel label: getLabels()){
            WindowStyleBuilder css = new WindowStyleBuilder();
            css.add(label.isSetted(doc, position)? WindowStyle.MARKUP_Set:
                WindowStyle.MARKUP_UNSET);
            css.add(label.isAllowed(doc, position)? WindowStyle.SYNTAX_ALLOW:
                WindowStyle.SYNTAX_FORBID);
            css.add(WindowStyle.CHEATSHEET_BASE);
            label.setStyle(css.toString());
        }
    }
}