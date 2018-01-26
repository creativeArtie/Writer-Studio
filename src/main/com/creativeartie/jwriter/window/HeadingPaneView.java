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
    private HeadingTreeControl headingNode;
    private HeadingTreeControl outlineNode;

    /** Property bined to headingNode.selectedSectedProperty() */
    private ReadOnlyObjectWrapper<SectionSpanHead> headingSelected;
    /** Property bined to outlineNode.selectedSectedProperty() */
    private ReadOnlyObjectWrapper<SectionSpanScene> outlineSelected;

    /** Property bined to headingNode/outlineNode.itemFoucedProperty() */
    private ReadOnlyBooleanWrapper treeFocused;

    public HeadingPaneView(){
        headingNode = initTree(WindowText.HEADING_TITLE);
        outlineNode = initTree(WindowText.OUTLINE_TITLE);

        ReadOnlyObjectProperty<SectionSpan> head = headingNode
            .selectedSectionProperty();
        headingSelected = new ReadOnlyObjectWrapper<>(this, "heaing");
        headingSelected.bind(Bindings.createObjectBinding(
            () -> (SectionSpanHead) head.getValue(), head));

        ReadOnlyObjectProperty<SectionSpan> out = outlineNode
            .selectedSectionProperty();
        outlineSelected = new ReadOnlyObjectWrapper<>(this, "outline");
        outlineSelected.bind(Bindings.createObjectBinding(
            () -> (SectionSpanScene) out.getValue(), out));

        treeFocused = new ReadOnlyBooleanWrapper(this, "treeFocused");
        treeFocused.bind(Bindings.or(headingNode.itemFocusedProperty(),
            outlineNode.itemFocusedProperty()));
    }

    /// Layout Node
    private HeadingTreeControl initTree(WindowText title){
        HeadingTreeControl ans = new HeadingTreeControl();
        ans.setText(title.getText());
        getChildren().add(ans);
        return ans;
    }

    /// Getters
    protected HeadingTreeControl getHeadings(){
        return headingNode;
    }

    protected HeadingTreeControl getOutlines(){
        return outlineNode;
    }

    /// Node Properties
    ReadOnlyObjectProperty<SectionSpanHead> headingSelectedProperty(){
        return headingSelected.getReadOnlyProperty();
    }

    SectionSpanHead getHeadingSelected(){
        return headingSelected.getValue();
    }

    ReadOnlyObjectProperty<SectionSpanScene> outlineSelectedProperty(){
        return outlineSelected.getReadOnlyProperty();
    }

    SectionSpanScene getOutlineSelected(){
        return outlineSelected.getValue();
    }

    ReadOnlyBooleanProperty treeFocusedProperty(){
        return treeFocused.getReadOnlyProperty();
    }

    boolean isTreeFocused(){
        return treeFocused.getValue();
    }

    /// Control Methods

}
