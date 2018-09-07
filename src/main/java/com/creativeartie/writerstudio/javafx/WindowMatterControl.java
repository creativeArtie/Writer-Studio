package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import org.fxmisc.richtext.model.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    MetaDataConstants.*;

class WindowMatterControl extends WindowMatterView{
    /// %Part 1: Private Fields and Constructor

    private WritingData writingData;

    /// %Part 2: Property Binding
    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(WriterSceneControl control){
        control.writingDataProperty().addListener((d, o, n) -> listenWritingData(n));
        showMatterProperty().addListener((d, o, n) -> listenShowMatter(n));
        getTextArea().plainTextChanges().subscribe(this::listenTextChanges);
        getTextArea().caretPositionProperty().addListener((d, o, n) ->
            listenCaret(n));
    }

    /// %Part 3.1: control.writingDataProperty()

    private void listenWritingData(WritingData data){
        writingData = data;
    }

    /// %Part 3.2: showMatterProperty()

    private void listenShowMatter(TextTypeMatter matter){
        if (getShowMatter() == null) return;
        String title = null;
        setTitle(getString(matter));

        if (writingData != null){
            String text = "";
            for (TextSpanMatter print: writingData.getMatter(matter)){
                if (! text.isEmpty()){
                    text += "\n";
                }
                text += print.getData().map(s -> s.getRaw()).orElse("");
            }
            getTextArea().replaceText(text);
        }
        showPreview();
    }

    /// %Part 3.4: getTextArea().plainTextChanges()

    private void listenTextChanges(PlainTextChange value) {
        if (writingData == null) return;
        if (getShowMatter() == null) return;

        String text = getTextArea().getText();
        writingData.setMatter(getShowMatter(), text);
        showPreview();
    }

    /// %Part 3.5: getTextArea().caretPositionProperty()

    private void listenCaret(int position){
        SpanLeaf leaf = null;
        TextTypeMatter matter = getShowMatter();
        if (matter != null) {
            if (writingData != null) {
                leaf = writingData.getLeaf(getShowMatter(), position)
                    .orElse(null);
            }
        }

        for (HintLabel label: getHintLabels()){
            label.showStatus(leaf);
        }
    }

    /// %Part 4: Utilities

    private void showPreview(){
        if (writingData == null) return;
        if (getShowMatter() == null) return;

        getPreviewText().getChildren().clear();
        for (TextSpanMatter print: writingData.getMatter(getShowMatter())){
            BorderPane line = new BorderPane();
            line.setLeft(buildCombobox(print));
            line.setCenter(TextFlowBuilder.loadMetaText(print));
            getPreviewText().getChildren().add(line);
        }
    }

    private Node buildCombobox(TextSpanMatter line){
        TextDataType ans = line.getDataType();
        ComboBox<TextDataType> box = new ComboBox<>();
        box.getItems().addAll(TextDataType.listAligns());
        box.getSelectionModel().select(ans);
        box.getSelectionModel().selectedItemProperty().addListener((b, o, n) ->{
            line.setFormat(n);
            showPreview();
        });
        return box;
    }
}
