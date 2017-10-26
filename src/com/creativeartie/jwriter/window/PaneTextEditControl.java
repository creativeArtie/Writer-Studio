package com.creativeartie.jwriter.window;

import java.util.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;
import javafx.geometry.*;

import com.creativeartie.jwriter.lang.markup.*;

public class PaneTextEditControl extends PaneTextEditView {

    protected void listenPosition(){
        refreshButtons();
    }

    public void refreshButtons(){
        ManuscriptDocument doc = getDocument();
        if (doc == null){
            return;
        }
        EditUpdated update = new EditUpdated(doc, getPosition());
        for(EditNode node: getControlNodes()){
            node.update(update);
        }
    }
}