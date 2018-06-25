package com.creativeartie.writerstudio.window;

import java.util.*;
import java.util.Optional;
import java.time.*;
import java.time.format.*;
import javafx.scene.control.*;
import javafx.beans.property.*;

import org.fxmisc.richtext.*;

import com.google.common.base.*;

import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * Controller for the main text area.
 *
 * @see TextPaneView
 */
class TextPaneControl extends TextPaneView {

    private Optional<WritingText> writingText;

    TextPaneControl(){
        writingText = Optional.empty();
    }

    @Override
    void updateTime(Label show){
        String text = DateTimeFormatter.ofPattern("HH:mm:ss")
            .format(LocalTime.now());
        show.setText(text);
    }

    void updateStats(StatSpanDay record){
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
        getCurrentStatsLabel().setText(text);
    }

    void loadDocumentText(WritingText writing){
        writingText = Optional.of(writing);
        getTextArea().replaceText(0, getTextArea().getLength(),
            writing.getRaw());
        setStyle(writing.getLeaves());
    }

    void setStyle(Collection<SpanLeaf> leaves){
        for (SpanLeaf leaf: leaves){
            getTextArea().setStyle(
                leaf.getStart(), leaf.getEnd(), CodeStyleBuilder.toCss(leaf)
            );
        }
    }

    public void moveTo(int position){
        if (position == getTextArea().getLength()){
            getTextArea().end(NavigationActions.SelectionPolicy.CLEAR);
        } else {
            char found = writingText.map(w -> w.getRaw().charAt(position))
                .orElse((char) 0);
            getTextArea().moveTo(position - (found == '\n'? 1: 0));
        }
    }

    public void returnFocus(){
        getTextArea().requestFollowCaret();
        getTextArea().requestFocus();
    }

    static boolean test = true;

    @Override
    void updatePosition(ReadOnlyIntegerWrapper caret){
        if (isReady()){
            caret.setValue(getTextArea().getCaretPosition());
            getLineTypeLabel().setText(writingText
                .flatMap(w -> w.getLeaf(caret.getValue()))
                /// s = SpanLeaf
                .flatMap(s -> s.getParent(LinedSpan.class))
                /// s = LinedSpan
                .map(s -> WindowText.getText(s))
                /// s = String
                .orElse("")
            );
        }
    }
}
