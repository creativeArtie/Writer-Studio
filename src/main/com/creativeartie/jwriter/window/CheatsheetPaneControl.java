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

/**
 * Controller for the Cheatsheet Pane
 *
 * @see CheatsheetPaneView
 */
class CheatsheetPaneControl extends CheatsheetPaneView{

     /** Updates the labels base on the cursor movements. */
    public void updateLabels(WritingText doc, int position){
        getLabels().forEach(label -> label.updateLabelStatus(doc, position));
    }
}