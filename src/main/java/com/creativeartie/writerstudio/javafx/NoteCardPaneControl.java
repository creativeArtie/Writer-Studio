package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.google.common.collect.*;

class NoteCardPaneControl extends NoteCardPaneView{

    private WritingText writingText;
    private HashBiMap<CatalogueIdentity, TreeItem<String>> idItemMap;
    private TreeMap<CatalogueIdentity, Set<SpanBranch>> idSpanMap;

    NoteCardPaneControl(){
        idItemMap = HashBiMap.create();
        idSpanMap = new TreeMap<>(Comparator.nullsFirst(Comparator
            .naturalOrder()));
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        getNoteDetailPane().setupProperties(control);
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));
    }

    private void loadText(WritingText text){
        if (text != null){
            text.addDocEdited(s -> updateIds());
        }
        updateIds();
    }

    private void updateIds(){
        if (writingText == null){
            getIdTree().setRoot(new TreeItem<>());
            idItemMap.clear();

        }
        CatalogueMap map = writingText.getCatalogue();
        TreeSet<SpanBranch> set = map.getIds(AuxiliaryData.TYPE_NOTE);
        TreeItem<String> root = new TreeItem<>();
        if (! set.isEmpty()){
            TreeItem<String> empty = new TreeItem<>();
            root.getChildren().add(empty);
            idItemMap.put(null, empty);
            idSpanMap.put(null, set);
        }
        set = map.getIds(AuxiliaryData.TYPE_RESEARCH);
        for (SpanBranch branch: set){
            CatalogueIdentity id = branch.getSpanIdentity().get();
            TreeItem<String> target = root;
            for (String category: id.getCategories()){
                for (TreeItem<String> child: getChildren()){
                    if (child.getValue.equals(category)) {
                        target = child;
                        break;
                    }
                }
                if (target == root){
                    TreeItem<String> child = new TreeItem(category);
                    target.getChildren().add(child);
                    target = child;
                }
            }

        }
    }
}
