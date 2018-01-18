package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.window.*;

import com.google.common.collect.*;

/**
 * Controller for Notes and Line Table.
 *
 * @see NotesPaneView
 */
class NotesPaneControl extends NotesPaneView{
    private HashMap<DirectoryType, ObservableList<NotesData>> dataMap;

    public NotesPaneControl(){
        dataMap = new HashMap<>();
    }

    /** Loads user-defined lists. */
    public void loadNotes(WritingText doc){
        dataMap.clear();
        for (DirectoryType type: getTypes().getItems()){
            if (type == DirectoryType.NOTE){
                ObservableList<NotesData> data = NotesData.extractData(
                    doc.getCatalogue().getCategory(
                        DirectoryType.COMMENT.getCategory()
                    ).values(), DirectoryType.COMMENT);
                data.addAll(NotesData.extractData(
                    doc.getCatalogue().getCategory(type.getCategory()).values(),
                    DirectoryType.NOTE));
                dataMap.put(type, data);
            } else {
                dataMap.put(type, NotesData.extractData(
                    doc.getCatalogue().getCategory(type.getCategory()).values(),
                    type));
            }
        }
    }

    /** Select the type of lists.*/
    protected void selectType(DirectoryType type){
        getTypes().getSelectionModel().select(type);
    }

    @Override
    protected void listenType(DirectoryType type){
        updateType(type);
    }

    /**
     * Update the data table base. Helper method of
     * {@link #selectType(DirectoryType)} and
     * {@link #listenType(DirectoryType)}.
     */
    private void updateType(DirectoryType type){
        if (type == null){
            getNoteDetail().setVisible(false);
            setNoteDetailVisible(false);
            return;
        }
        getTableTitle().setText(WindowText.getText(type));
        getDataTable().setItems(dataMap.get(type));
        boolean isNote = type == DirectoryType.NOTE;
        getLineColumn().setVisible(! isNote);
        setNoteDetailVisible(isNote);
        getNoteDetail().clearSelection();
    }

    @Override
    protected void listenSelected(NotesData data){
        if (data != null){
            getNoteDetail().setCardNote(data.getTargetSpan()
                .filter(span -> span instanceof NoteCardSpan)
                .map(span -> (NoteCardSpan) span));
        } else {
            getNoteDetail().clearSelection();
        }
    }

    public void refreshPane(WritingText doc){
        NotesData back = getDataTable().getSelectionModel()
            .getSelectedItem();
        loadNotes(doc);
        updateType(getTypes().getSelectionModel().getSelectedItem());
        if (back != null){
            CatalogueIdentity id = back.getIdentity();
            int target = back.getTarget();
            for(NotesData data: getDataTable().getItems()){
                if (data.getIdentity().equals(id) && data.getTarget() ==
                    target)
                {
                    getDataTable().getSelectionModel().select(data);
                    return;
                }
            }
        }
    }

}