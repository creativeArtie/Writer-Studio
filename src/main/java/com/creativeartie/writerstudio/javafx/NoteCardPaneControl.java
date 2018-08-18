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
    private TreeMap<CatalogueIdentity, Set<NoteCardSpan>> idSpanMap;

    NoteCardPaneControl(){
        idItemMap = HashBiMap.create();
        idSpanMap = new TreeMap<>(Comparator.nullsFirst(Comparator
            .naturalOrder()));
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));
        getNoteDetailPane().setupProperties(control);
    }

    private void loadText(WritingText text){
        if (text != null){
            writingText = text;
            text.addDocEdited(s -> updateIds());
        }
        updateIds();
    }

    private void updateIds(){
        idItemMap.clear();
        idSpanMap.clear();
        if (writingText == null){
            getIdTree().setRoot(new TreeItem<>());
            return;
        }

        TreeItem<String> root = new TreeItem<>();
        getIdTree().setRoot(root);

        CatalogueMap map = writingText.getCatalogue();
        TreeSet<SpanBranch> set = map.getIds(AuxiliaryData.TYPE_NOTE);
        if (! set.isEmpty()){
            TreeItem<String> empty = new TreeItem<>("Unnamed");
            root.getChildren().add(empty);
            TreeSet<NoteCardSpan> add = new TreeSet<>();
            set.forEach( s -> add.add((NoteCardSpan)s) );
            idItemMap.put(null, empty);
            idSpanMap.put(null, add);
        }

        set = map.getIds(AuxiliaryData.TYPE_RESEARCH);
        if (set.isEmpty()) return;

        TreeItem<String> note = new TreeItem<>("Named");
        root.getChildren().add(note);
        for (SpanBranch branch: set){
            NoteCardSpan add = (NoteCardSpan)  branch;
            CatalogueIdentity id = add.getSpanIdentity().get();
            TreeItem<String> target = note;
            boolean isFirst = true;
            for (String category: id.getCategories()){
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                target = locateItem(target, category);
            }
            if (! idItemMap.containsKey(id)){
                idItemMap.put(id, target);
                idSpanMap.put(id, new TreeSet<>());
            }
            idSpanMap.get(id).add(add);
        }
    }

    private TreeItem<String> locateItem(TreeItem<String> at, String data){
        for(TreeItem<String> child: at.getChildren()){
            String value = child.getValue();
            if (value.equals(data)){
                return child;
            }
        }
        TreeItem<String> create = new TreeItem<>(data);
        at.getChildren().add(create);
        return create;
    }
}
