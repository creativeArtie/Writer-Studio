package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.google.common.collect.*;

class NoteCardPaneControl extends NoteCardPaneView{

    private WritingText writingText;

    protected void handleNoteSelected(MouseEvent event, NoteCardSpan note){
        getNoteDetailPane().setShowCard(note);
    }

    @Override
    protected void bindChildren(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));
        getNoteDetailPane().postLoad(control);
    }

    private void loadText(WritingText text){
        if (text != null){
            writingText = text;
            text.addDocEdited(s -> updateAll());
        }
        updateAll();
    }

    private void updateAll(){
        updateIds();
        updateHeadings();
    }

    private void updateIds(){
        if (writingText == null){
            getIdTree().setRoot(new NoteCardTreeItem<>());
            return;
        }

        NoteCardTreeItem<String> root = new NoteCardTreeItem<>();
        getIdTree().setRoot(root);

        /// get notes with no ids
        CatalogueMap map = writingText.getCatalogue();
        TreeSet<SpanBranch> set = map.getIds(AuxiliaryData.TYPE_NOTE);
        if (! set.isEmpty()){
            NoteCardTreeItem<String> empty = new NoteCardTreeItem<>("Unnamed");
            root.getChildren().add(empty);
            TreeSet<NoteCardSpan> add = new TreeSet<>();
            set.forEach( s -> add.add((NoteCardSpan)s) );
            empty.setNoteList(add);
        }

        set = map.getIds(AuxiliaryData.TYPE_RESEARCH);
        if (set.isEmpty()) return;
        NoteCardTreeItem<String> note = new NoteCardTreeItem<>("Named");
        root.getChildren().add(note);

        for (SpanBranch branch: set){
            NoteCardSpan add = (NoteCardSpan)  branch;
            CatalogueIdentity id = add.getSpanIdentity().get();
            NoteCardTreeItem<String> target = note;
            boolean isFirst = true;
            for (String category: id.getCategories()){
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                target = locateItem(target, category);
            }
            target.addNoteCard(add);

        }
    }

    private void updateLocations(){
        if (writingText == null) return;
    }

    private NoteCardTreeItem<String> locateItem(TreeItem<String> at, String data){
        for(TreeItem<String> child: at.getChildren()){
            String value = child.getValue();
            if (value.equals(data)){
                return (NoteCardTreeItem<String>)child;
            }
        }
        NoteCardTreeItem<String> create = new NoteCardTreeItem<>(data);
        at.getChildren().add(create);
        return create;
    }

    private void updateList(TreeItem<?> item){
        if (item instanceof NoteCardTreeItem){
            getNoteCardList().setItems(((NoteCardTreeItem<?>)item).getNoteList());
        } else {
            getNoteCardList().getItems().clear();
        }
    }

    private void updateHeadings(){
        NoteCardTreeItem<SectionSpanHead> root = new NoteCardTreeItem<>();
        for (SectionSpanHead child:
            writingText.getChildren(SectionSpanHead.class)
        ){
            updateHeadings(root, child);
        }
        getLoactionTree().setRoot(root);
    }

    private void updateHeadings(NoteCardTreeItem<SectionSpanHead> root,
        SectionSpanHead parent)
    {
        for (SectionSpanHead child: parent.getSections()){
            NoteCardTreeItem<SectionSpanHead> item =
                new NoteCardTreeItem<>(child);
            item.addNoteCards(child.getNotes());
            root.getChildren().add(item);
            updateHeadings(item, child);
        }
    }

    private void showCard(NoteCardSpan show){
        getNoteDetailPane().setShowCard(show);
    }
}
