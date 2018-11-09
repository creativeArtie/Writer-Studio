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

    /// %Part 1: Private Fields and Constructor

    private WritingText writingText;

    private ObjectProperty<SpanBranch> lastSelected;

    private final TreeMap<SectionSpanHead, TreeItem<SectionSpanHead>> headingMap;
    private TreeItem<SectionSpanScene> selectingOutline;

    HeadingsPaneControl(){
        headingMap = new TreeMap<>(Comparator.comparingInt(s -> s.getStart()));
    }

    /// %Part 2: Property Binding

    protected void handleSelection(MouseEvent event, SectionSpan span){
        if (event.getButton().equals(MouseButton.PRIMARY)){
            lastSelected.setValue(span);
        }
    }

    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(WriterSceneControl control){
        control.writingTextProperty().addListener(
            (d, o, n) -> listenWritingText(n)
        );
        control.getTextPane().getTextArea().caretPositionProperty().addListener(
            (d, o, n) -> listenCaret(n.intValue())
        );
        lastSelected = control.lastSelectedProperty();
    }

    /// %Part 3.1: control.writingTextProperty()

    private void listenWritingText(WritingText text){
        writingText = text;
        if (text != null){
            text.addDocEdited(s -> loadHeadings());
            loadHeadings();
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
                root.getChildren().add(
                    loadChildHeadings((SectionSpanHead) child)
                );
            }
        }
        getHeadingTree().setRoot(root);
        listenCaret(writingText.getEnd());
    }

    private TreeItem<SectionSpanHead> loadChildHeadings(SectionSpanHead head){
        TreeItem<SectionSpanHead> heading = new TreeItem<>(head);
        headingMap.put(head, heading);
        for (SectionSpanHead child: head.getSections()){
            heading.getChildren().add(loadChildHeadings(child));
        }
        return heading;
    }

    /// %Part 3.2: control.getTextPane().getTextArea().caretPositionProperty()

    private void listenCaret(int position){
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
            getHeadingTree().getSelectionModel().clearSelection();
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

    /// %Part 4: Utilities

    private void clearOutline(){
        getOutlineTree().setRoot(new TreeItem<>());
        getOutlinePane().setExpanded(false);
    }
}
