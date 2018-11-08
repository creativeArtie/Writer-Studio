package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.javafx.utils.*;

import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    MetaDataConstants.*;

abstract class MetaDataPaneView extends VBox{
    /// %Part 1: Constructor and Class Fields

    /// %Part 1: Constructor and Class Fields
    private TreeMap<TextTypeInfo, TextField> infoFields;
    private TreeMap<TextTypeMatter, Button> matterButtons;

    MetaDataPaneView(){
        getChildren().addAll(buildInfoLabels(), buildMatterLabels());
    }

    /// %Part 2: Layout

    /// %Part 2 (Content -> top)

    private TitledPane buildInfoLabels(){
        infoFields = new TreeMap<>();
        GridPane pane = new GridPane();

        int i = 0;
        for (TextTypeInfo meta: TextTypeInfo.values()){
            Label label = new Label(getString(meta));
            TextField field = new TextField();

            infoFields.put(meta, field);
            pane.add(label, 0, i);
            pane.add(field, 1, i);

            i++;
        }
        return buildTitledGrid(META_DATA_TITLE, pane);
    }

    /// %Part 2 (Content -> bottom)

    private TitledPane buildMatterLabels(){
        matterButtons = new TreeMap<>();
        GridPane pane = new GridPane();

        int i = 0;
        for (TextTypeMatter area: TextTypeMatter.values()){
            Label label = new Label(getString(area));
            Button button = new Button(EDIT_BUTTON);

            matterButtons.put(area, button);
            pane.add(label, 0, i);
            pane.add(button, 1, i);

            i++;
        }
        return buildTitledGrid(MATTER_AREA_TITLE, pane);

    }

    /// %Part 2.1: Utilities

    private static TitledPane buildTitledGrid(String text, GridPane child){
        TitledPane pane = new TitledPane();
        pane.setText(text);
        pane.setContent(child);
        return pane;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindChildren(control);
    }

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods

    TextField getInfoField(TextTypeInfo info){
        return infoFields.get(info);
    }

    Button getMatterButton(TextTypeMatter matter){
        return matterButtons.get(matter);
    }
}
