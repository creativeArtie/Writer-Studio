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

/**
 * Controller for Heading Tree.
 *
 * @see HeadingTreeView
 */
class HeadingTreeControl extends HeadingTreeView{

    @Override
    protected void clearTree(){
        getTree().setRoot(new TreeItem<>());
    }

    protected void loadSections(WritingText text){
        TreeItem<SectionSpan> root = new TreeItem<>();
        for (Span child: text){
            SectionSpan span = (SectionSpan)child;
            TreeItem<SectionSpan> item = new TreeItem<>(span);
            root.getChildren().add(item);
            addChildren(item, span, found ->
                ((SectionSpanHead)found).getSections());
        }
        getTree().setRoot(root);
    }

    protected void loadScenes(SectionSpanHead replace){
        TreeItem<SectionSpan> root = new TreeItem<>();
        addChildren(root, replace, span -> span instanceof SectionSpanHead?
            ((SectionSpanHead)span).getScenes():
            ((SectionSpanScene)span).getSubscenes());
        getTree().setRoot(root);
    }

    /**
     * add the children in a {@link TreeItem}. Parameter {@code insert} is not
     * being inserted into the node. Helper method of
     * {@link #addItem(SectionSpan, Function)} and
     * {@link #replaceTree(SectionSpan, Function)}.
     */
    private void addChildren(TreeItem<SectionSpan> item, SectionSpan insert,
            Function<SectionSpan, List<? extends SectionSpan>> list){
        for (SectionSpan span : list.apply(insert)){
            TreeItem<SectionSpan> child = new TreeItem<>(span);
            if (! list.apply(span).isEmpty()){
                addChildren(child, span, list);
            }
            item.getChildren().add(child);
        }
    }

//    /** Hash map to help with selecting items.*/
//    private HashMap<Optional<LinedSpanLevelSection>,
//        TreeItem<Optional<LinedSpanLevelSection>>> selectionMapper;
//
//    HeadingTreeControl(){
//        selectionMapper = new HashMap<>();
//    }
//
//    /** Remove all items in the tree. */
//    public void clearTree(){
//        selectionMapper.clear();
//
//        /// Stuff an single empty item.
//        TreeItem<Optional<LinedSpanLevelSection>> root = new TreeItem<>();
//        root.getChildren().add(new TreeItem<>(Optional.empty()));
//        getTree().setRoot(root);
//    }
//
//    /**
//     * Selecting a heading found. This method is the reason why
//     * {@link #getMapper()} exists.
//     */
//    public void selectHeading(Optional<LinedSpanLevelSection> section){
//        System.out.println(selectionMapper);
//        if (section.isPresent()){
//            getTree().getSelectionModel().select(selectionMapper.get(section));
//        } else {
//            getTree().getSelectionModel().clearSelection();
//        }
//    }
//
//    /// Common methods where HeadingTreeControl is for SectionSpanHead.
//
//    /** Loads the tree from the {@link WritingText}. */
//    public void loadHeadings(WritingText doc){
//        if (doc.isEmpty()){
//            clearTree();
//            return;
//        }
//
//        /// When this is not empty:
//        selectionMapper.clear();
//        TreeItem<Optional<LinedSpanLevelSection>> root = new TreeItem<>();
//        /// Adding children
//        for (SpanBranch span: doc){
//            root.getChildren().add(loadHeadings((SectionSpanHead)span));
//        }
//        getTree().setRoot(root);
//    }
//
//    /**
//     * Loads a SectionSpanHead into a branch. Helper method of
//     * {@link #loadHeadings(WritingText)}.
//     */
//    private TreeItem<Optional<LinedSpanLevelSection>> loadHeadings(
//            SectionSpanHead section){
//        TreeItem<Optional<LinedSpanLevelSection>> child =
//                new TreeItem<>(section.getHeading());
//        /// Adding children
//        for (SectionSpanHead subsection: section.getSections()){
//            TreeItem<Optional<LinedSpanLevelSection>> item = loadHeadings(
//                subsection);
//            child.getChildren().add(item);
//            selectionMapper.put(subsection.getHeading(), item);
//        }
//        return child;
//    }
//
//    /// Common methods where HeadingTreeControl is for SectionSpanScene.
//
//    /** Loads the tree from the {@link SectionSpanHead}. */
//    public void loadOutline(SectionSpanHead span){
//        selectionMapper.clear();
//        TreeItem<Optional<LinedSpanLevelSection>> root = new TreeItem<>();
//        /// Add children
//        for (SectionSpanScene scene: span.getScenes()){
//            root.getChildren().add(loadOutline(scene));
//            selectionMapper.put(scene.getHeading(), root);
//        }
//        getTree().setRoot(root);
//    }
//
//    /**
//     * Loads a SectionSpanScene into a branch. Helper method of
//     * {@link #loadOutline(SectionSpanHead)}.
//     */
//    private TreeItem<Optional<LinedSpanLevelSection>> loadOutline(
//            SectionSpanScene scene){
//
//        TreeItem<Optional<LinedSpanLevelSection>> child =
//                new TreeItem<>(scene.getHeading());
//        for (SectionSpanScene subsection: scene.getSubscenes()){
//            child.getChildren().add(loadOutline(subsection));
//        }
//        return child;
//    }
}
