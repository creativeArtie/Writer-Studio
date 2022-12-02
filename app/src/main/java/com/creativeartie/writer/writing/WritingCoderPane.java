package com.creativeartie.writer.writing;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import javafx.concurrent.*;

/**
 * The main writing area. Code copied from <a href=
 * "https://github.com/FXMisc/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywordsAsyncDemo.java">Java
 * Keywords Async Demo </a>
 *
 * @author wai
 */
public class WritingCoderPane extends CodeArea {

    private ExecutorService executor;

    private Span rootSpan;

    public WritingCoderPane() {
        executor = Executors.newSingleThreadExecutor();
        setParagraphGraphicFactory(LineNumberFactory.get(this));

        /// update text update only after 500 milliseconds
        multiPlainChanges().successionEnds(Duration.ofMillis(500))
            .retainLatestUntilLater(executor).supplyTask(
                this::computeHighlightingAsync
            ).awaitLatest(multiPlainChanges()).filterMap(t -> {
                if (t.isSuccess()) return Optional.of(t.get());
                t.getFailure().printStackTrace();
                return Optional.empty();
            }).subscribe(this::applyHighlighting);
        getStylesheets().add("data/text.css");
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
     *                     formatted texts
     */
    private void applyHighlighting(
        StyleSpans<Collection<String>> highlighting
    ) {
        setStyleSpans(0, highlighting);
    }

    /**
     * Computes the actual highlights
     *
     * @param  text
     *              the text to format
     * @return      the results of the formats
     * @see         #computeHighlightingAsync()
     */
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        DocBuilder builder = new DocBuilder(true);
        new LinePhrase(text, builder, LinePhrase.TextEnders.NONE);
        return builder.getStyles();
    }
}
