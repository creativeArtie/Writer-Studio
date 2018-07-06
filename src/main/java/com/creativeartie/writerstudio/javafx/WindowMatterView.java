package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;

import org.fxmisc.richtext.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

abstract class WindowMatterView extends Stage{

    /// %Part 1: Constructor and Class Fields

    protected static final int WIDTH = 650;
    protected static final int HEIGHT = 500;
    protected static final int AREA_HEIGHT = (500 / 2) - 10;
    private InlineCssTextArea textArea;
    private VBox previewText;
    private List<CheatsheetLabel> hintLabels;

    private final SimpleObjectProperty<TextTypeMatter> showMatter;

    public WindowMatterView(){
        showMatter = new SimpleObjectProperty<>(this, "showMatter");

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        setScene(buildScene());
    }

    /// %Part 2: Layout

    private Scene buildScene(){
        Scene scene = new Scene(createMainPane(), WIDTH, HEIGHT);
        scene.getStylesheets().add(FileResources.getPrintCss());
        return scene;
    }

    private GridPane createMainPane(){
        GridPane pane = new GridPane();
        setPrecentWidth(pane, 100);

        setPrecentHeight(pane, 45);
        pane.add(buildTextArea(), 0, 0);

        setPrecentHeight(pane, 45);
        pane.add(buildPreviewArea(), 0, 1);

        pane.add(buildHintLabels(), 0, 2);

        return pane;
    }

    private InlineCssTextArea buildTextArea(){
        textArea = new InlineCssTextArea();
        return textArea;
    }

    private VBox buildPreviewArea(){
        previewText = new VBox();
        previewText.getStyleClass().add("border");
        return previewText;
    }

    private GridPane buildHintLabels(){
        hintLabels = Arrays.asList(new CheatsheetLabel[]{

            CheatsheetLabel.getLabel(CheatsheetText.FORMAT_AGENDA),
            CheatsheetLabel.getLabel(CheatsheetText.OTHER_ESCAPE),
            CheatsheetLabel.getLabel(CheatsheetText.FORMAT_REF_KEY),
            CheatsheetLabel.getLabel(CheatsheetText.FORMAT_DIRECT_LINK),
            CheatsheetLabel.getLabel(CheatsheetText.FORMAT_REF_LINK),
            CheatsheetLabel.getLabel(CheatsheetText.FORMAT_BOLD),
            CheatsheetLabel.getLabel(CheatsheetText.FORMAT_ITALICS),
            CheatsheetLabel.getLabel(CheatsheetText.FORMAT_UNDERLINE),
            CheatsheetLabel.getLabel(CheatsheetText.FORMAT_CODED)
        });

        GridPane pane = new GridPane();
        pane.getStyleClass().add("border");
        int i = 0;
        for (int row = 0; row < 3; row++){
            setPrecentWidth(pane, 33.333333);
            for (int col = 0; col < 3; col++){
                pane.add(hintLabels.get(i++), row, col);
            }
        }
        setPrecentHeight(pane, 10);
        return pane;
    }

    /**
     * Set the next column by percent width. Helper method of
     * {@link #initHintsLabels()}.
     */
    private void setPrecentWidth(GridPane pane, double value){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(value);
        pane.getColumnConstraints().add(column);
    }

    private void setPrecentHeight(GridPane pane, double value){
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(value);
        pane.getRowConstraints().add(row);
    }

    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    public ObjectProperty<TextTypeMatter> showMatterProperty(){
        return showMatter;
    }

    public TextTypeMatter getShowMatter(){
        return showMatter.getValue();
    }

    public void setShowMatter(TextTypeMatter value){
        showMatter.setValue(value);
    }

    /// %Part 5: Get Child Methods

    InlineCssTextArea getTextArea(){
        return textArea;
    }

    VBox getPreviewText(){
        return previewText;
    }

    List<CheatsheetLabel> getHintLabels() {
        return hintLabels;
    }
}
