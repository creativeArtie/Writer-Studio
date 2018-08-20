package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import org.fxmisc.richtext.model.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

class WindowMatterControl extends WindowMatterView{

    private WritingData writingData;

    protected void setupChildern(WriterSceneControl control){
        control.writingDataProperty().addListener((d, o, n) -> loadData(n));
        showMatterProperty().addListener((d, o, n) -> showMatter(n));
        getTextArea().plainTextChanges().subscribe(this::updateText);
        getTextArea().caretPositionProperty().addListener((d, o, n) ->
            updateHints(n));
    }

    /// %Part 1: control.writingDataProperty()

    private void loadData(WritingData data){
        writingData = data;
    }

    /// %Part 2: showMatterProperty()

    private void showMatter(TextTypeMatter matter){
        if (getShowMatter() == null) return;
        setTitle(WindowText.getString(matter));

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

    /// %Part 3: getTextArea().plainTextChanges()

    private void updateText(PlainTextChange value) {
        if (writingData == null) return;
        if (getShowMatter() == null) return;

        String text = getTextArea().getText();
        writingData.setMatter(getShowMatter(), text);
        showPreview();
    }

    /// %Part 4: getTextArea().caretPositionProperty()

    private void updateHints(int position){
        SpanLeaf leaf = null;
        TextTypeMatter matter = getShowMatter();
        if (matter != null) {
            if (writingData != null) {
                leaf = writingData.getLeaf(getShowMatter(), position)
                    .orElse(null);
            }
        }

        for (CheatsheetLabel label: getHintLabels()){
            label.showStatus(leaf);
        }
    }

    /// %Part update preview

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
