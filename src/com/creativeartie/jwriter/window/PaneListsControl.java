package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.google.common.collect.*;

public class PaneListsControl extends PaneListsView{
    private Optional<Range<Integer>> selectedRange;
    private Optional<DirectoryType> selectedType;
    private HashMap<DirectoryType, ObservableList<PaneListsData>> dataMap;

    public PaneListsControl(){
        selectedRange = Optional.empty();
        selectedType = Optional.empty();
        dataMap = new HashMap<>();
    }

    public void loadDoc(ManuscriptDocument doc){
        dataMap.clear();
        for (DirectoryType type: getTypes().getItems()){
            if (type == DirectoryType.NOTE){
                ObservableList<PaneListsData> data = PaneListsData.extractData(
                    doc.getCatalogue().getCategory(
                        DirectoryType.COMMENT.getCategory()
                    ).values(), DirectoryType.COMMENT);
                data.addAll(PaneListsData.extractData(
                    doc.getCatalogue().getCategory(type.getCategory()).values(),
                    DirectoryType.NOTE));
                dataMap.put(type, data);
            } else {
                dataMap.put(type, PaneListsData.extractData(
                    doc.getCatalogue().getCategory(type.getCategory()).values(),
                    type));
            }
        }
    }

    protected void selectType(DirectoryType type){
        getTypes().getSelectionModel().select(type);
    }

    protected void listenType(DirectoryType type){
        updateType(type);
    }

    private void updateType(DirectoryType type){
        if (type == null){
            getNoteDetail().setVisible(false);
            setNoteDetailVisible(false);
            return;
        }
        getDataTable().setItems(dataMap.get(type));
        boolean isNote = type == DirectoryType.NOTE;
        getLineColumn().setVisible(! isNote);
        setNoteDetailVisible(isNote);
        getNoteDetail().clearData();
    }

    @Override
    protected void listenSelected(PaneListsData data){
        if (data != null){
            getNoteDetail().setData(data.getTargetSpan()
                .filter(span -> span instanceof MainSpanNote)
                .map(span -> (MainSpanNote) span));
            selectedRange = data.getTargetSpan().map(span -> span.getRange());
        } else {
            getNoteDetail().clearData();
        }
    }

    @Override
    public void refreshPane(ManuscriptDocument doc){
        PaneListsData back = getDataTable().getSelectionModel()
            .getSelectedItem();
        loadDoc(doc);
        updateType(getTypes().getSelectionModel().getSelectedItem());
        if (back != null){
            CatalogueIdentity id = back.getIdentity();
            int target = back.getTarget();
            for(PaneListsData data: getDataTable().getItems()){
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