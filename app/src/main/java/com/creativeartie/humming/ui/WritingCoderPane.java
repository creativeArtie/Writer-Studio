package com.creativeartie.humming.ui;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.event.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.humming.document.*;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * The main writing area. Some code copied from <a href=
 * "https://github.com/FXMisc/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywordsAsyncDemo.java">Java
 * Keywords Async Demo </a>
 *
 * @author wai
 */
public class WritingCoderPane extends CodeArea {
    private ExecutorService executor;
    private Document rootDoc;

    public WritingCoderPane() {
        executor = Executors.newSingleThreadExecutor();
        setParagraphGraphicFactory(LineNumberFactory.get(this));

        textProperty().addListener((observable, oldValue, newValue) -> {
            rootDoc.updateText(newValue);
            if (newValue.isEmpty()) return;
            final StyleSpansBuilder<Collection<String>> styleSpans = new StyleSpansBuilder<>();
            styleSpans.addAll(rootDoc.convertLeaves((leaf) -> new StyleSpan<>(leaf.getCssStyles(), leaf.getLength())));
            setStyleSpans(0, styleSpans.create());
        });
        getStylesheets().add("data/text.css");

        Popup popup = new Popup();
        Label popupMsg = new Label();
        popupMsg.setStyle("-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-padding: 5;");
        popup.getContent().add(popupMsg);

        setMouseOverTextDelay(Duration.ofSeconds(1));
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_BEGIN, e -> {
            int chIdx = e.getCharacterIndex();
            Point2D pos = e.getScreenPosition();
            String name = "";
            for (Span child : rootDoc.locateChildren(chIdx)) {
                String appendText = child.getClass().getSimpleName();
                if (child instanceof LineSpan) {
                    appendText = ((LineSpan) child).getLineStyle().toString();
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
            popupMsg.setText(getText(chIdx, chIdx + 1) + name);
            popup.show(WritingCoderPane.this, pos.getX(), pos.getY() + 10);
        });
        addEventHandler(MouseOverTextEvent.MOUSE_OVER_TEXT_END, e -> {
            popup.hide();
        });
        rootDoc = new Document();
    }

    /**
     * Shut down code
     *
     * @see Main#stop()
     */
    void shutdown() {
        executor.shutdown();
    }
}
