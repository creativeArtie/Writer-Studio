package com.creativeartie.writer.javafx;

import java.util.*;
import javafx.beans.property.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import org.fxmisc.richtext.*;

import com.creativeartie.writer.lang.markup.*;
import com.creativeartie.writer.javafx.utils.*;
import static com.creativeartie.writer.javafx.utils.LayoutConstants.
    MetaDataConstants.*;

abstract class WindowMatterView extends Stage{

    /// %Part 1: Constructor and Class Fields

    private InlineCssTextArea textArea;
    private VBox previewText;
    private List<HintLabel> hintLabels;

    private final SimpleObjectProperty<TextTypeMatter> showMatter;

    public WindowMatterView(){
        showMatter = new SimpleObjectProperty<>(this, "showMatter");

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        setScene(buildScene());
    }

    /// %Part 2: Layout

    /// %Part 2 (stage -> scene)

    private Scene buildScene(){
        Scene scene = new Scene(createMainPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(FileResource.META_CSS.getCssPath());
        return scene;
    }

    /// %Part 2 (stage -> scene -> content)

    private GridPane createMainPane(){
        GridPane pane = new GridPane();
        CommonLayoutUtility.setWidthPrecent(pane, 100);

        CommonLayoutUtility.setHeightPrecent(pane, MATTER_EDIT_HEIGHT);
        pane.add(buildTextArea(), 0, 0);

        CommonLayoutUtility.setHeightPrecent(pane, MATTER_VIEW_HEIGHT);
        pane.add(buildPreviewArea(), 0, 1);

        pane.add(buildHintLabels(), 0, 2);

        return pane;
    }

    /// %Part 2 (stage -> scene -> content -> top)

    private InlineCssTextArea buildTextArea(){
        textArea = new InlineCssTextArea();
        return textArea;
    }

    /// %Part 2 (stage -> scene -> content -> center)

    private VBox buildPreviewArea(){
        previewText = new VBox();
        previewText.getStyleClass().add(BORDER_STYLE);
        return previewText;
    }

    /// %Part 2 (stage -> scene -> content -> bottom)

    private GridPane buildHintLabels(){
        hintLabels = Arrays.asList(new HintLabel[]{

            HintLabel.getLabel(HintText.FORMAT_AGENDA),
            HintLabel.getLabel(HintText.OTHER_ESCAPE),
            HintLabel.getLabel(HintText.FORMAT_REF_KEY),
            HintLabel.getLabel(HintText.FORMAT_DIRECT_LINK),
            HintLabel.getLabel(HintText.FORMAT_REF_LINK),
            HintLabel.getLabel(HintText.FORMAT_BOLD),
            HintLabel.getLabel(HintText.FORMAT_ITALICS),
            HintLabel.getLabel(HintText.FORMAT_UNDERLINE),
            HintLabel.getLabel(HintText.FORMAT_CODED)
        });

        GridPane pane = new GridPane();
        pane.getStyleClass().add(BORDER_STYLE);
        int i = 0;
        for (int row = 0; row < 3; row++){
            CommonLayoutUtility.setWidthPrecent(pane, HINT_WIDTH);
            for (int col = 0; col < 3; col++){
                pane.add(hintLabels.get(i++), row, col);
            }
        }
        return pane;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindChildren(control);
    }

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 4.1: showMatter (TextTypeMatter)

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

    List<HintLabel> getHintLabels() {
        return hintLabels;
    }
}
