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

abstract class HeadingPaneView extends VBox{
    private HeadingTreeControl headingNode;
    private HeadingTreeControl outlineNode;
    private ReadOnlyObjectWrapper<Optional<LinedSpanLevelSection>> headingProp;
    private ReadOnlyObjectWrapper<Optional<LinedSpanLevelSection>> outlineProp;
    private ReadOnlyBooleanWrapper headingFocused;
    private ReadOnlyBooleanWrapper outlineFocused;

    public HeadingPaneView(){
        headingNode = new HeadingTreeControl();
        outlineNode = new HeadingTreeControl();

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
    protected HeadingTreeControl getHeadings(){
        return headingNode;
    }

    protected HeadingTreeControl getOutlines(){
        return outlineNode;
    }

    /// Layout Node
    private void setupTree(HeadingTreeControl tree, String title){
        tree.setText(title);
        getChildren().add(tree);
    }

    /// Node Properties
    ReadOnlyObjectProperty<Optional<LinedSpanLevelSection>> headingProperty(){
        return headingProp.getReadOnlyProperty();
    }

    Optional<LinedSpanLevelSection> getHeading(){
        return headingProp.getValue();
    }

    ReadOnlyObjectProperty<Optional<LinedSpanLevelSection>> outlineProperty(){
        return outlineProp.getReadOnlyProperty();
    }

    Optional<LinedSpanLevelSection> getOutline(){
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
    public abstract void loadHeadings(WritingText document);

    public abstract void setHeading(WritingText document, int index);

}