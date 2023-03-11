package com.creativeartie.humming.ui;

import java.time.*;
import java.util.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.event.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.humming.document.*;

import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class WritingController {
    private Document rootDoc;
    Popup popup;
    Label popupMsg;
    @FXML
    public CodeArea writingText;

    public WritingController() {
        rootDoc = new Document();
        popup = new Popup();
        popupMsg = new Label();
        popupMsg.setStyle("-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-padding: 5;");
        popup.getContent().add(popupMsg);
    }

    @FXML
    public void initialize() {
        writingText.textProperty().addListener(this::textEdited);
        writingText.setMouseOverTextDelay(Duration.ofSeconds(1));
        writingText.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, this::mouseOverTextEvent);
        writingText.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> popup.hide());
    }

    private void mouseOverTextEvent(MouseOverTextEvent e) {
        int chIdx = e.getCharacterIndex();
        Point2D pos = e.getScreenPosition();
        String name = "";
        for (Span child : rootDoc.locateChildren(chIdx)) {
            String appendText = child.getClass().getSimpleName();
            if (child instanceof Para) {
                appendText = ((Para) child).getLineStyle().toString();
            } else if (child instanceof SpanLeaf) {
                appendText = ((SpanLeaf) child).getStyle().toString();
            } else if (appendText == "") {
                appendText = "LineSpan";
            }
            if (name != "") {
                name += ", ";
            }
            name += appendText;
        }
        popupMsg.setText(name);
        popup.show(writingText, pos.getX(), pos.getY() + 10);
    }

    private void textEdited(ObservableValue<?> observable, String oldValue, String newValue) {
        rootDoc.updateText(newValue);
        if (newValue.isEmpty()) return;
        final StyleSpansBuilder<Collection<String>> styleSpans = new StyleSpansBuilder<>();
        styleSpans.addAll(rootDoc.convertLeaves((leaf) -> new StyleSpan<>(leaf.getCssStyles(), leaf.getLength())));
        writingText.setStyleSpans(0, styleSpans.create());
    }
}
