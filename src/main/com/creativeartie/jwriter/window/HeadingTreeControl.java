package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import java.util.*;
import java.util.function.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

class HeadingTreeControl extends HeadingTreeView{

    public void clear(){
        TreeItem<Optional<LinedSpanLevelSection>> root = new TreeItem<>();
        root.getChildren().add(new TreeItem<>());
        getTree().setRoot(root);
    }

    public void loadHeadings(ManuscriptDocument doc){
        getMapper().clear();
        TreeItem<Optional<LinedSpanLevelSection>> root = new TreeItem<>();
        for (SpanBranch span: doc){
            root.getChildren().add(loadHeadings((SectionSpanHead)span));
        }
        getTree().setRoot(root);
    }

    private TreeItem<Optional<LinedSpanLevelSection>> loadHeadings(
            SectionSpanHead section){
        TreeItem<Optional<LinedSpanLevelSection>> child =
                new TreeItem<>(section.getHeading());
        for (SectionSpanHead subsection: section.getSections()){
            child.getChildren().add(loadHeadings(subsection));
            getMapper().put(section.getHeading(), child);
        }
        return child;
    }

    public void loadOutline(SectionSpanHead span){
        getMapper().clear();
        TreeItem<Optional<LinedSpanLevelSection>> root = new TreeItem<>();
        for (SectionSpanScene scene: span.getScenes()){
            root.getChildren().add(loadOutline(scene));
            getMapper().put(scene.getHeading(), root);
        }
        getTree().setRoot(root);
    }

    private TreeItem<Optional<LinedSpanLevelSection>> loadOutline(
            SectionSpanScene scene){

        TreeItem<Optional<LinedSpanLevelSection>> child =
                new TreeItem<>(scene.getHeading());
        for (SectionSpanScene subsection: scene.getSubscenes()){
            child.getChildren().add(loadOutline(subsection));
        }
        return child;
    }

    public void selectHeading(Optional<LinedSpanLevelSection> section){
        if (section.isPresent()){
            getTree().getSelectionModel().select(getMapper().get(section));
        } else {
            getTree().getSelectionModel().clearSelection();
        }
    }
}