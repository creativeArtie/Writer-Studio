package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

final class NoteCardTreeControl extends NoteCardTreeView{

    private WritingText writingText;
    private class NoteTreeItem extends TreeItem<String>{
        private ArrayList<NoteCardSpan> spanList;

        private NoteTreeItem(String value, NoteCardSpan span){
            super(value);
            spanList = new ArrayList<>();
            spanList.add(span);
        }

        private NoteTreeItem(String value){
            super(value);
        }
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
    }
}
