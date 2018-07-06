package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

/**
 * A pane with two {@link HeadingTreeControl} objects that
 */
final class HeadingsControl extends HeadingsView{

    private WritingText writingText;
    private int caretPosition;

    private final TreeMap<SectionSpanHead, TreeItem<SectionSpanHead>> headingMap;
    private TreeItem<SectionSpanScene> selectingOutline;

    HeadingsControl(){
        headingMap = new TreeMap<>(Comparator.comparingInt(s -> s.getStart()));
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> setText(n));
        control.getTextPane().getTextArea().caretPositionProperty().addListener(
            (d, o, n) -> selectHeading(n.intValue())
        );
    }

    /// %Part 2.1: control.writingTextProperty()
    private void setText(WritingText text){
        writingText = text;
        if (text != null){
            text.addDocEdited(s -> loadHeadings());
            loadHeadings();
            selectHeading(text.getEnd());
        } else {
            getHeadingTree().setRoot(new TreeItem<>());
            clearOutline();
        }
    }

    private void loadHeadings(){
        if (writingText == null) return;
        TreeItem<SectionSpanHead> root = new TreeItem<>();
        headingMap.clear();
        for (SpanBranch child: writingText){
            if (child instanceof SectionSpanHead){
                root.getChildren().add(loadHeadings((SectionSpanHead) child));
            }
        }
        getHeadingTree().setRoot(root);
    }

    private TreeItem<SectionSpanHead> loadHeadings(SectionSpanHead head){
        TreeItem<SectionSpanHead> heading = new TreeItem<>(head);
        headingMap.put(head, heading);
        for (SectionSpanHead child: head.getSections()){
            heading.getChildren().add(loadHeadings(child));
        }
        return heading;
    }

    /// %Part 2.2: control.getTextPane().getTextArea().caretPositionProperty()

    private void selectHeading(int position){
        if (writingText == null) return;
        Optional<SectionSpanHead> head = writingText.locateLeaf(position)
            .flatMap(s -> s.getParent(SectionSpanHead.class));
        if (head.isPresent()){
            getHeadingTree().getSelectionModel().select(
                headingMap.get(head.get())
            );

            TreeItem<SectionSpanScene> root = new TreeItem<>();
            selectingOutline = null;

            List<SectionSpanScene> scenes = head.get().getScenes();
            if (scenes.isEmpty()){
                clearOutline();
            } else {
                for (SectionSpanScene scene: scenes){
                    root.getChildren().add(loadScene(scene, position));
                }
            }
            getOutlineTree().setRoot(root);

            if (selectingOutline == null){
                getOutlinePane().setExpanded(! root.getChildren().isEmpty());
            } else {
                getOutlineTree().getSelectionModel().select(selectingOutline);
                getOutlinePane().setExpanded(true);
            }
        } else {
            getHeadingTree().getSelectionModel().selectLast();
            clearOutline();
        }
    }

    private TreeItem<SectionSpanScene> loadScene(SectionSpanScene scene,
            int position){
        TreeItem<SectionSpanScene> item = new TreeItem<>(scene);
        item.setExpanded(true);
        if (scene.getRange().contains(position)){
            selectingOutline = item;
        }
        for (SectionSpanScene child: scene.getSubscenes()){
            item.getChildren().add(loadScene(child, position));
        }
        return item;
    }

    private void clearOutline(){
        getOutlineTree().setRoot(new TreeItem<>());
        getOutlinePane().setExpanded(false);
    }
}
