package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.*;
import static com.creativeartie.writer.javafx.utils.LayoutConstants.
    NoteCardConstants.*;

class NoteCardPaneControl extends NoteCardPaneView{

    private WritingText writingText;

    protected void handleListSelected(MouseEvent event, TreeItem<?> note){
        if (event.getButton().equals(MouseButton.PRIMARY) &&
            note instanceof NoteCardTreeItem)
        {
            getNoteCardList().setItems(((NoteCardTreeItem<?>)note).getNoteList());
        }
    }

    protected void handleNoteSelected(MouseEvent event, NoteCardSpan note){
        if (event.getButton().equals(MouseButton.PRIMARY)){
            getNoteDetailPane().setShowCard(note);
        }
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
        getNoteCardList().getItems().clear();
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
            NoteCardTreeItem<String> empty = new NoteCardTreeItem<>(UNNAME_CARD);
            root.getChildren().add(empty);
            TreeSet<NoteCardSpan> add = new TreeSet<>();
            set.forEach( s -> add.add((NoteCardSpan)s) );
            empty.setNoteList(add);
        }

        set = map.getIds(AuxiliaryData.TYPE_RESEARCH);
        if (set.isEmpty()) return;
        NoteCardTreeItem<String> note = new NoteCardTreeItem<>(NAMED_CARD);
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
        updateHeadings(root, writingText);
        getLoactionTree().setRoot(root);
    }

    private void updateHeadings(NoteCardTreeItem<SectionSpanHead> root,
        SpanNode<?> parent)
    {
        for (SectionSpanHead child: parent.getChildren(SectionSpanHead.class)){
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
