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
            String css = Utilities.getCss(label.isTurnOn(doc, position)?
                "CheatSheet.TurnOn": "CheatSheet.TurnOff");
            css += Utilities.getCss("CheatSheet.Base");
            label.setStyle(css);
        }
    }
}