package com.creativeartie.writerstudio.fxgui;

import java.time.*;
import java.time.format.*;
import java.util.*;
import javafx.animation.*;

import org.fxmisc.richtext.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * Controller for the main text area.
 *
 * @see TextPaneView
 */
final class TextPaneControl extends TextPaneView {

    public void returnFocus(){
        updatePosition(getCaretPosition());
        getTextArea().requestFollowCaret();
        getTextArea().requestFocus();
    }

    protected void addListeners(){
        writingTextProperty().addListener((d, o, n) -> loadDocument(n));
        writingStatProperty().addListener((d, o, n) -> updateStats(n));
        caretPositionProperty().addListener(
            (d, o, n) -> updatePosition(n.intValue())
        );
        lastSelectedProperty().addListener((d, o, n) -> updatePosition(n));
        new AnimationTimer(){
            @Override public void handle(long now) {updateTime();}
        }.start();
    }

    private void loadDocument(WritingText text){
        setTextReady(false);
        if (text == null){
            getTextArea().clear();
        } else {
            getTextArea().replaceText(text.getRaw());
            setStyle(text.getLeaves());
        }
        returnFocus();
        setTextReady(true);
    }

    private void setStyle(Collection<SpanLeaf> leaves){
        for (SpanLeaf leaf: leaves){
            getTextArea().setStyle(
                leaf.getStart(), leaf.getEnd(), CodeStyleBuilder.toCss(leaf)
            );
        }
    }

    private void updateStats(WritingStat stat){
        if (stat == null){
            getStatsLabel().setText("");
        } else {
            StatSpanDay record = stat.getRecord();
            int wordCount = record.getPublishWritten();
            double wordPrecent = (wordCount / (double) record.getPublishGoal()) * 100;

            Duration timer = record.getWriteTime();
            long hours = timer.toHours();
            long minutes = timer.toMinutes() % 60;
            long seconds = timer.getSeconds() % 60;
            double timePrecent = (timer.getSeconds() / (double) record.getTimeGoal()
                .getSeconds()) * 100;
            String text = String.format(
                "Publish: %d (%#.2f%%); Time: %d:%02d:%02d (%#.2f%%)",
                wordCount, wordPrecent, hours, minutes, seconds, timePrecent);
            getStatsLabel().setText(text);
        }
    }

    private void updatePosition(SpanBranch location){
        if (location == null) return;
        if (! isTextReady()) return;
        setTextReady(false);
        int position = location.getEnd();
        if (position == getTextArea().getLength()){
            getTextArea().end(NavigationActions.SelectionPolicy.CLEAR);
        } else {
            WritingText text = getWritingText();
            char found = text == null? (char) 0: text.getRaw().charAt(position - 1);
            getTextArea().moveTo(position - (found == '\n'? 1: 0));
        }
        returnFocus();
        setTextReady(true);
        updatePosition(position);
    }

    private void updatePosition(int location){
        if (! isTextReady()) return;
        setTextReady(false);
        getLineTypeLabel().setText(Optional.ofNullable(getWritingText())
            .flatMap(w -> w.getLeaf(location))
            /// s = SpanLeaf
            .flatMap(s -> s.getParent(LinedSpan.class))
            /// s = LinedSpan
            .map(s -> WindowText.getText(s))
            /// s = String
            .orElse("")
        );
        setTextReady(true);
    }

    private void updateTime(){
        getTimeLabel().setText(DateTimeFormatter.ofPattern("HH:mm")
            .format(LocalTime.now()));
    }
}
