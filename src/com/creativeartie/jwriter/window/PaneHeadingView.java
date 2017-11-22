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

abstract class PaneHeadingView extends VBox{
    private PaneHeadingTreeControl headingNode;
    private PaneHeadingTreeControl outlineNode;
    private ReadOnlyObjectWrapper<Optional<LinedSpanSection>> headingProp;
    private ReadOnlyObjectWrapper<Optional<LinedSpanSection>> outlineProp;
    private ReadOnlyBooleanWrapper headingFocused;
    private ReadOnlyBooleanWrapper outlineFocused;

    public PaneHeadingView(){
        headingNode = new PaneHeadingTreeControl();
        outlineNode = new PaneHeadingTreeControl();

        setupTree(headingNode, WindowText.HEADING_TITLE.getText());
        setupTree(outlineNode, WindowText.OUTLINE_TITLE.getText());

        headingProp = new ReadOnlyObjectWrapper<>(this, "heaing");
        headingProp.bind(headingNode.selectedSectionProperty());

        outlineProp = new ReadOnlyObjectWrapper<>(this, "outline");
        outlineProp.bind(outlineNode.selectedSectionProperty());

        headingFocused = new ReadOnlyBooleanWrapper(this, "headingFocused");
        headingFocused.bind(headingNode.itemFocusedProperty());

        outlineFocused = new ReadOnlyBooleanWrapper(this, "outlineFocused");
        outlineFocused.bind(outlineNode.itemFocusedProperty());

    }

    /// Getters
    protected PaneHeadingTreeControl getHeadings(){
        return headingNode;
    }

    protected PaneHeadingTreeControl getOutlines(){
        return outlineNode;
    }

    /// Layout Node
    private void setupTree(PaneHeadingTreeControl tree, String title){
        tree.setText(title);
        getChildren().add(tree);
    }

    /// Node Properties
    ReadOnlyObjectProperty<Optional<LinedSpanSection>> headingProperty(){
        return headingProp.getReadOnlyProperty();
    }

    Optional<LinedSpanSection> getHeading(){
        return headingProp.getValue();
    }

    ReadOnlyObjectProperty<Optional<LinedSpanSection>> outlineProperty(){
        return outlineProp.getReadOnlyProperty();
    }

    Optional<LinedSpanSection> getOutline(){
        return outlineProp.getValue();
    }

    ReadOnlyBooleanProperty headingFocusedProperty(){
        return headingFocused.getReadOnlyProperty();
    }

    boolean isHeadingFocused(){
        return headingFocused.getValue();
    }

    ReadOnlyBooleanProperty outlineFocusedProperty(){
        return outlineFocused.getReadOnlyProperty();
    }

    boolean isOutlineFocused(){
        return outlineFocused.getValue();
    }

    /// Control Methods
    public abstract void loadHeadings(ManuscriptDocument document);

    public abstract void setHeading(ManuscriptDocument document, int index);

}