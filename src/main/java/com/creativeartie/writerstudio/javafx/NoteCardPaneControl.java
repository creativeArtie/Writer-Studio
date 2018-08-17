package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardPaneControl extends NoteCardPaneView{

    private WritingText writingText;
    private TreeMap<CatalogueIdentity, TreeItem<String>> idMap;

    NoteCardDetailPaneControl(){
        idMap = new TreeMap<>();
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));
        getContentPane().setupProperties(control);
    }

    private void loadText(WritingText text){
        writingText = text;
        if (text != null){
            text.addDocEdited(span -> listNotes());
            listNotes();
        }
    }

    private void listNotes(){
        if (writingText == null){
            return;
        }
        CatalogueMap map = writingText.getCatalogue();
        TreeSet<SpanBranch> set = map.getIds(AuxiliaryData.TYPE_NOTE);
        set.addAll(map.getIds(AuxiliaryData.TYPE_RESEARCH));

        TreeItem<String> root = new TreeItem<>();
        for(SpanBranch branch: set){
            NoteCardSpan note = (NoteCardSpan) branch;
            CatalogueIdentity id = note.getSpanIdentity().get();

        }
    }

    private void loadText(CatalogueIdentity id, int index, TreeItem<String> item){
        List<String> category = id.getCategories();
    }
}
