package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

/**
 * A pane with two {@link HeadingTreeControl} objects that
 */
final class HeadingsPaneControl extends HeadingsPaneView{

    private WritingText writingText;

    private BooleanProperty refocusText;
    private ObjectProperty<SpanBranch> lastSelected;

    private final TreeMap<SectionSpanHead, TreeItem<SectionSpanHead>> headingMap;
    private TreeItem<SectionSpanScene> selectingOutline;

    HeadingsPaneControl(){
        headingMap = new TreeMap<>(Comparator.comparingInt(s -> s.getStart()));
    }

    /// %Part 1:  setup children

    @Override
    protected void bindChildren(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));
        /*getHeadingTree().getSelectionModel().selectedItemProperty()
                .addListener((d, o, n) ->
            listenHeading(n));
        getOutlineTree().getSelectionModel().selectedItemProperty()
                .addListener((d, o, n) ->
            listenOutline(n));*/
        lastSelected = control.lastSelectedProperty();
        refocusText = control.refocusTextProperty();
    }

    /// %Part 1.1: control.writingTextProperty()
    private void loadText(WritingText text){
        writingText = text;
        if (text != null){
            text.addDocEdited(s -> showHeadings());
            showHeadings();
            showHeading(text.getEnd());
        } else {
            getHeadingTree().setRoot(new TreeItem<>());
            clearOutline();
        }
    }

    /// %Part 1.2: WritingText.addDocEdited / loadText()

    private void showHeadings(){
        if (writingText == null) return;
        TreeItem<SectionSpanHead> root = new TreeItem<>();
        headingMap.clear();
        for (SpanBranch child: writingText){
            if (child instanceof SectionSpanHead){
                root.getChildren().add(showHeadings((SectionSpanHead) child));
            }
        }
        getHeadingTree().setRoot(root);
    }

    private TreeItem<SectionSpanHead> showHeadings(SectionSpanHead head){
        TreeItem<SectionSpanHead> heading = new TreeItem<>(head);
        headingMap.put(head, heading);
        for (SectionSpanHead child: head.getSections()){
            heading.getChildren().add(showHeadings(child));
        }
        return heading;
    }

    /// %Part 2.2: getHeadingTree().getSelectionModel().selectedItemProperty()

    private void listenHeading(TreeItem<SectionSpanHead> head){
        if (head != null){
            lastSelected.setValue(head.getValue());
            showHeading(head.getValue().getStart());
        }
        refocusText.setValue(true);
    }

    /// %Part 2.3: getOutlineTree().getSelectionModel().selectedItemProperty()
    private void listenOutline(TreeItem<SectionSpanScene> scene){
        if (scene != null){
            lastSelected.setValue(scene.getValue());
        }
        refocusText.setValue(true);
    }

    /// %Part 2.3: control.getTextPane().getTextArea().caretPositionProperty()

    private void showHeading(int position){
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
                    root.getChildren().add(loadOutline(scene, position));
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

    private TreeItem<SectionSpanScene> loadOutline(SectionSpanScene scene,
            int position){
        TreeItem<SectionSpanScene> item = new TreeItem<>(scene);
        item.setExpanded(true);
        if (scene.getRange().contains(position)){
            selectingOutline = item;
        }
        for (SectionSpanScene child: scene.getSubscenes()){
            item.getChildren().add(loadOutline(child, position));
        }
        return item;
    }

    private void clearOutline(){
        getOutlineTree().setRoot(new TreeItem<>());
        getOutlinePane().setExpanded(false);
    }
}
