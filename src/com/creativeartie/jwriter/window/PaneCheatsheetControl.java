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

class PaneCheatsheetControl extends PaneCheatsheetView{


    public void updateLabels(ManuscriptDocument doc, int position){
        for (PaneCheatsheetLabel label: getLabels()){
            WindowStyleBuilder css = new WindowStyleBuilder();
            css.add(label.isTurnOn(doc, position)? WindowStyle.MARKUP_Set:
                WindowStyle.MARKUP_UNSET);
            css.add(WindowStyle.CHEATSHEET_BASE);
            label.setStyle(css.toString());
        }
    }
}