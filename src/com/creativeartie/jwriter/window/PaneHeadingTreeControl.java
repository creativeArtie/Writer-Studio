package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

public class PaneHeadingTreeControl extends PaneHeadingTreeView{

    public void loadHeadings(List<? extends Section> children){
        getMapper().clear();
        TreeItem<Optional<LinedSpanSection>> root = new TreeItem<>();
        if (children == null || children.isEmpty()){
            root.getChildren().add(new TreeItem<>(Optional.empty()));
        } else {
            loadHeadings(root, children);
        }
        getTree().setRoot(root);
    }

    private void loadHeadings(TreeItem<Optional<LinedSpanSection>> parent,
        List<? extends Section> children)
    {
        for(Section found: children){
            TreeItem<Optional<LinedSpanSection>> child =
                new TreeItem<>(found.getLine());
            parent.getChildren().add(child);
            if (found.getLine().isPresent()){
                getMapper().put(found.getLine(), child);
            }
            loadHeadings(child, found.getChildren());
        }
    }

    public void selectHeading(Optional<LinedSpanSection> section){
        if (section.isPresent()){
            getTree().getSelectionModel().select(getMapper().get(section));
        } else {
            getTree().getSelectionModel().clearSelection();
        }
    }
}