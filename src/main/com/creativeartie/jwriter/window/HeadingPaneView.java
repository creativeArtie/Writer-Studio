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
 * A pane with two {@link HeadingTreeControl} objects that
 */
abstract class HeadingPaneView extends VBox{
//    private HeadingTreeControl headingNode;
//    private HeadingTreeControl outlineNode;
//
//    /** Property bined to headingNode.selectedSectedProperty() */
//    private ReadOnlyObjectWrapper<Optional<LinedSpanLevelSection>> headingSelected;
//    /** Property bined to outlineNode.selectedSectedProperty() */
//    private ReadOnlyObjectWrapper<Optional<LinedSpanLevelSection>> outlineSelected;
//
//    /** Property bined to headingNode.itemFoucedProperty() */
//    private ReadOnlyBooleanWrapper headingFocused;
//    /** Property bined to outlineNode.itemFoucedProperty() */
//    private ReadOnlyBooleanWrapper outlineFocused;
//
//    public HeadingPaneView(){
//        headingNode = setupTree(WindowText.HEADING_TITLE);
//        outlineNode = setupTree(WindowText.OUTLINE_TITLE);
//
//        headingSelected = new ReadOnlyObjectWrapper<>(this, "heaing");
//        headingSelected.bind(headingNode.selectedSectionProperty());
//
//        outlineSelected = new ReadOnlyObjectWrapper<>(this, "outline");
//        outlineSelected.bind(outlineNode.selectedSectionProperty());
//
//        headingFocused = new ReadOnlyBooleanWrapper(this, "headingFocused");
//        headingFocused.bind(headingNode.itemFocusedProperty());
//
//        outlineFocused = new ReadOnlyBooleanWrapper(this, "outlineFocused");
//        outlineFocused.bind(outlineNode.itemFocusedProperty());
//
//    }
//
//    /// Layout Node
//    private HeadingTreeControl setupTree(WindowText title){
//        HeadingTreeControl ans = new HeadingTreeControl();
//        ans.setText(title.getText());
//        getChildren().add(ans);
//        return ans;
//    }
//
//    /// Getters
//    protected HeadingTreeControl getHeadings(){
//        return headingNode;
//    }
//
//    protected HeadingTreeControl getOutlines(){
//        return outlineNode;
//    }
//
//    /// Node Properties
//    ReadOnlyObjectProperty<Optional<LinedSpanLevelSection>>
//            headingSelectedProperty(){
//        return headingSelected.getReadOnlyProperty();
//    }
//
//    Optional<LinedSpanLevelSection> getHeadingSelected(){
//        return headingSelected.getValue();
//    }
//
//    ReadOnlyObjectProperty<Optional<LinedSpanLevelSection>>
//            outlineSelectedProperty(){
//        return outlineSelected.getReadOnlyProperty();
//    }
//
//    Optional<LinedSpanLevelSection> getOutlineSelected(){
//        return outlineSelected.getValue();
//    }
//
//    ReadOnlyBooleanProperty headingFocusedProperty(){
//        return headingFocused.getReadOnlyProperty();
//    }
//
//    boolean isHeadingFocused(){
//        return headingFocused.getValue();
//    }
//
//    ReadOnlyBooleanProperty outlineFocusedProperty(){
//        return outlineFocused.getReadOnlyProperty();
//    }
//
//    boolean isOutlineFocused(){
//        return outlineFocused.getValue();
//    }
//
//    /// Control Methods
//
}
