package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

abstract class MetaDataPaneView extends VBox{
    /// %Part 1: Constructor and Class Fields

    /// %Part 1: Constructor and Class Fields
    private TreeMap<TextTypeInfo, TextField> infoFields;
    private TreeMap<TextTypeMatter, Button> matterButtons;

    MetaDataPaneView(){
        getChildren().addAll(buildInfoLabels(), buildMatterLabels());
    }

    /// %Part 2: Layout

    private GridPane buildInfoLabels(){
        infoFields = new TreeMap<>();
        GridPane pane = buildGridPane(WindowText.DATA_META, 0);
        int i = 0;
        for (TextTypeInfo meta: TextTypeInfo.values()){
            Label label = new Label(WindowText.getString(meta));
            TextField field = new TextField();

            infoFields.put(meta, field);
            pane.add(label, 0, i);
            pane.add(field, 1, i);

            i++;
        }
        return pane;
    }

    private GridPane buildMatterLabels(){
        matterButtons = new TreeMap<>();
        GridPane pane = buildGridPane(WindowText.DATA_AREA, 1);

        int i = 0;

        for (TextTypeMatter area: TextTypeMatter.values()){
            Label label = new Label(WindowText.getString(area));
            Button button = new Button(WindowText.DATA_EDIT.getText());

            matterButtons.put(area, button);
            pane.add(label, 0, i);
            pane.add(button, 1, i);

            i++;
        }
        return pane;

    }

    private GridPane buildGridPane(WindowText text, int row){
        GridPane ans = new GridPane();
        TitledPane pane = new TitledPane();
        pane.setText(text.getText());
        pane.setContent(ans);
        getChildren().add(pane);
        return ans;
    }

    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods

    TextField getInfoField(TextTypeInfo info){
        return infoFields.get(info);
    }

    Button getMatterButton(TextTypeMatter matter){
        return matterButtons.get(matter);
    }
}
