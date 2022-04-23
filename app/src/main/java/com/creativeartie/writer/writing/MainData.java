package com.creativeartie.writer.writing;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import javafx.concurrent.*;

public class MainData extends CodeArea {

	private static Pattern syntax = Pattern.compile(
    		"(?<H1>=[^\n]*)" +
            "|(?<P>[^\n]*)\n"
    );
	
	private ExecutorService executor;

	public MainData() {
		executor = Executors.newSingleThreadExecutor();
		setParagraphGraphicFactory(LineNumberFactory.get(this));
		
        multiPlainChanges().successionEnds(Duration.ofMillis(500))
                .retainLatestUntilLater(executor)
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
		getStylesheets().add("data/text.css");
	}
	
	void shutdown(){
		executor.shutdown();
	}
	
    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        setStyleSpans(0, highlighting);
    }
    

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
    	StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        Matcher matcher = syntax.matcher(text);
        int lastKwEnd = 0;
        while(matcher.find()) {
        	System.out.println(matcher.group("H1"));
            String styleClass =
                    matcher.group("H1") != null ? "heading" :
                    matcher.group("P") != null ? "paragraph" :
                    null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
    }
}