package com.creativeartie.humming.ui;

import java.time.*;
import java.util.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.event.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.humming.document.*;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class WritingController {
    private class ManuscriptProperty extends ReadOnlyObjectWrapper<Manuscript> {
        ManuscriptProperty() {
            super(new Manuscript());
        }

        private void updateText(String text) {
            getValue().updateText(text);
            if (text.isEmpty()) return;
            final StyleSpansBuilder<Collection<String>> styleSpans = new StyleSpansBuilder<>();
            styleSpans.addAll(
                    rootDoc.get().convertLeaves((leaf) -> new StyleSpan<>(leaf.getCssStyles(), leaf.getLength()))
            );
            writingText.setStyleSpans(0, styleSpans.create());
            int line = 0;
            for (CssLineStyles style : rootDoc.get().convertLines((para) -> para.getLineStyle())) {
                writingText.setParagraphStyle(line, List.of(style.getCssName()));
                line++;
            }
            fireValueChangedEvent();
        }
    }

    @FXML
    private ManuscriptProperty rootDoc;
    private Popup popup;
    private Label popupMsg;
    @FXML
    public CodeArea writingText;

    public WritingController() {

        popup = new Popup();
        popupMsg = new Label();
        popupMsg.setStyle("-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-padding: 5;");
        popup.getContent().add(popupMsg);
    }

    @FXML
    public void initialize() {

        rootDoc = new ManuscriptProperty();
        writingText.textProperty().addListener(this::textEdited);

        writingText.setMouseOverTextDelay(Duration.ofSeconds(1));
        writingText.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, this::mouseOverTextEvent);
        writingText.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> popup.hide());
    }

    private void mouseOverTextEvent(MouseOverTextEvent e) {
        int chIdx = e.getCharacterIndex();
        Point2D pos = e.getScreenPosition();
        String name = "";
        for (Span child : rootDoc.get().locateChildren(chIdx)) {
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
    }

    public ReadOnlyObjectProperty<Manuscript> documentProperty() {
        return rootDoc.getReadOnlyProperty();
    }
}
