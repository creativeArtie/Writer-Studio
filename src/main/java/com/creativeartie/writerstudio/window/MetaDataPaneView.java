package com.creativeartie.writerstudio.window;

import java.util.*;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

abstract class MetaDataPaneView extends VBox{
    private final ArrayList<TextField> metaFields;
    private final ArrayList<Button> frontMatter;
    private SimpleObjectProperty<WritingData> metaData;
    private ReadOnlyBooleanWrapper documentLoaded;

    MetaDataPaneView(){
        metaFields = initMetaLabels();
        frontMatter = initMatters();

        metaData = new SimpleObjectProperty<>(this, "metadata");
        metaData.addListener((data, oldValue, newValue) ->
            loadData(newValue));

        documentLoaded = new ReadOnlyBooleanWrapper(this, "documentLoaded",
            false);

        for (TextTypeInfo meta: TextTypeInfo.values()){
            metaFields.get(meta.ordinal()).textProperty()
                .addListener( (d, o, n) -> updateMeta(meta, n));
        }

        for (TextTypeMatter area: TextTypeMatter.values()){
            frontMatter.get(area.ordinal()).setOnAction(e -> updateArea(area));
        }
    }

    /// Layout Node
    private ArrayList<TextField> initMetaLabels(){
        ArrayList<TextField> list = new ArrayList<>();
        GridPane pane = initGridPane(WindowText.DATA_META, 0);
        int i = 1;
        for (TextTypeInfo meta: TextTypeInfo.values()){
            pane.add(new Label(WindowText.getString(meta)), 0, i);
            TextField field = new TextField();
            list.add(field);
            pane.add(field, 1, i);
            i++;
        }
        return list;
    }

    private ArrayList<Button> initMatters(){
        ArrayList<Button> list = new ArrayList<>();
        GridPane pane = initGridPane(WindowText.DATA_AREA, 1);
        int i = 1;
        for (TextTypeMatter area: TextTypeMatter.values()){
            pane.add(new Label(WindowText.getString(area)), 0, i);
            Button button = new Button(WindowText.DATA_EDIT.getText());
            list.add(button);
            pane.add(button, 1, i);
            i++;
        }
        return list;

    }

    private GridPane initGridPane(WindowText text, int row){
        GridPane ans = new GridPane();
        TitledPane pane = new TitledPane();
        pane.setText(text.getText());
        pane.setContent(ans);
        getChildren().add(pane);
        return ans;
    }

    /// Getters
    TextField getTextField(TextTypeInfo meta){
        return metaFields.get(meta.ordinal());
    }

    /// Node Properties
    public ReadOnlyBooleanProperty documentLoaded(){
        return documentLoaded.getReadOnlyProperty();
    }

    public boolean isDocumentLoaded(){
        return documentLoaded.getValue();
    }

    public ObjectProperty<WritingData> metaDataProperty(){
        return metaData;
    }

    public WritingData getMetaData(){
        return metaData.getValue();
    }

    public void setMetaData(WritingData doc){
        metaData.setValue(doc);
    }

    /// Control Methods
    private void loadData(WritingData data){
        loadMetaData(data);
        documentLoaded.setValue(data != null);
    }

    protected abstract void loadMetaData(WritingData data);

    protected abstract void updateMeta(TextTypeInfo meta, String text);

    protected abstract void updateArea(TextTypeMatter area);
}
