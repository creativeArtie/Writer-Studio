package com.creativeartie.humming.ui;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.event.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.humming.document.*;

import javafx.concurrent.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * The main writing area. Code copied from <a href=
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

        /// update text update only after 500 milliseconds
        multiPlainChanges().successionEnds(Duration.ofMillis(500)).retainLatestUntilLater(executor)
                .supplyTask(this::computeHighlightingAsync).awaitLatest(multiPlainChanges()).filterMap(t -> {
                    if (t.isSuccess()) return Optional.of(t.get());
                    t.getFailure().printStackTrace();
                    return Optional.empty();
                }).subscribe(this::applyHighlighting);
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

    /**
     * Setup formatting for syntax highlighting
     *
     * @return the task to do
     */
    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = getText();
        Task<StyleSpans<Collection<String>>> task = new Task<>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    /**
     * applies the update
     *
     * @param highlighting
     *        formatted texts
     */
    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        setStyleSpans(0, highlighting);
    }

    /**
     * Computes the actual highlights
     *
     * @param text
     *        the text to format
     *
     * @return the results of the formats
     *
     * @see #computeHighlightingAsync()
     */
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        rootDoc.updateText(text);
        rootDoc.cleanUp();
        final StyleSpansBuilder<Collection<String>> styleSpans = new StyleSpansBuilder<>();
        styleSpans.addAll(rootDoc.convertLeaves((leaf) -> new StyleSpan<>(leaf.getCssStyles(), leaf.getLength())));
        return styleSpans.create();
    }
}
