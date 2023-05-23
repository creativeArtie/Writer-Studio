package com.creativeartie.humming.ui;

import java.time.*;
import java.util.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.event.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.humming.document.*;
import com.creativeartie.humming.files.*;
import com.creativeartie.humming.main.*;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * Controller for <a href="../../../../../resources/data/writing.fxml">
 * writing.fxml </a>
 */
public class WritingController extends ActiveFile {
    private class ManuscriptProperty extends ReadOnlyObjectWrapper<ManuscriptFile> {
        private void updateText(String text) {
            Manuscript script = getValue().getManuscript();
            script.updateText(text);
            if (text.isEmpty()) return;
            final StyleSpansBuilder<Collection<String>> styleSpans = new StyleSpansBuilder<>();
            styleSpans.addAll(script.convertLeaves((leaf) -> new StyleSpan<>(leaf.getCssStyles(), leaf.getLength())));
            writingText.setStyleSpans(0, styleSpans.create());
            int line = 0;
            for (CssLineStyles style : script.convertLines((para) -> para.getLineStyle())) {
                writingText.setParagraphStyle(line, List.of(style.getCssName()));
                line++;
            }
            assert text.length() == script.getLength();
            fireValueChangedEvent();
        }
    }

    private ManuscriptProperty rootDoc;
    private Popup popup;
    private Label popupMsg;
    @FXML
    private CodeArea writingText;

    /**
     * Constructor for the class. It is public to allow FXML.
     */
    public WritingController() {

        rootDoc = new ManuscriptProperty();
        // TODO replace popup message
        popup = new Popup();
        popupMsg = new Label();
        popupMsg.setStyle("-fx-background-color: black;-fx-text-fill: white;-fx-padding: 5;"); //$NON-NLS-1$
        popup.getContent().add(popupMsg);
    }

    @FXML
    void initialize() {

        writingText.textProperty().addListener(this::textEdited);
        writingText.getStylesheets().add(DataFiles.WRITER_STYLE.getFile().toString());

        writingText.setMouseOverTextDelay(Duration.ofSeconds(1));
        writingText.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, this::mouseOverTextEvent);
        writingText.addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> popup.hide());
    }

    private void mouseOverTextEvent(MouseOverTextEvent e) {
        int chIdx = e.getCharacterIndex();
        Point2D pos = e.getScreenPosition();
        String name = new String();
        for (Span child : rootDoc.get().getManuscript().locateChildren(chIdx)) {
            String appendText = child.getClass().getSimpleName();
            if (child instanceof Para) {
                appendText = ((Para) child).getLineStyle().toString();
            } else if (child instanceof SpanLeaf) {
                appendText = ((SpanLeaf) child).getStyle().toString();
            } else if (appendText.isEmpty()) {
                appendText = "LineSpan"; //$NON-NLS-1$
            }
            if (!name.isEmpty()) {
                name += ", "; //$NON-NLS-1$
            }
            name += appendText;
        }
        popupMsg.setText(name);
        popup.show(writingText, pos.getX(), pos.getY() + 10);
    }

    private void textEdited(ObservableValue<?> observable, String oldValue, String newValue) {
        rootDoc.updateText(newValue);
    }

    @Override
    protected ObservableValue<Integer> docCursorProperty() {
        return writingText.caretPositionProperty();
    }

    @Override
    protected ReadOnlyObjectProperty<ManuscriptFile> manuscriptProperty() {
        return rootDoc.getReadOnlyProperty();
    }

    void setManuscript(ManuscriptFile file) {
        rootDoc.set(file);
    }
}
