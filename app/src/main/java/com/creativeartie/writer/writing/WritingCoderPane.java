package com.creativeartie.writer.writing;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import javafx.concurrent.*;

public class WritingCoderPane extends CodeArea {

    private ExecutorService executor;

    private Span rootSpan;

    public WritingCoderPane() {
        executor = Executors.newSingleThreadExecutor();
        setParagraphGraphicFactory(LineNumberFactory.get(this));

        multiPlainChanges().successionEnds(Duration.ofMillis(500))
            .retainLatestUntilLater(executor)
            .supplyTask(this::computeHighlightingAsync)
            .awaitLatest(multiPlainChanges()).filterMap(t -> {
                if (t.isSuccess()) return Optional.of(t.get());
                t.getFailure().printStackTrace();
                return Optional.empty();
            }).subscribe(this::applyHighlighting);
        getStylesheets().add("data/text.css");
    }

    void shutdown() {
        executor.shutdown();
    }

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

    private void
        applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        setStyleSpans(0, highlighting);
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        DocBuilder builder = new DocBuilder();
        return builder.getStyles();
    }
}
