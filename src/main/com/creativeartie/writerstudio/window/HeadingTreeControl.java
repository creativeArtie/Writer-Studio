package com.creativeartie.writerstudio.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;

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

}
