package com.creativeartie.writerstudio.javafx;

import java.time.*;
import java.time.format.*;
import java.util.*;
import javafx.animation.*;
import javafx.beans.property.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/**
 * Controller for the main text area.
 *
 * @see TextPaneView
 */
class TextPaneControl extends TextPaneView {

    private WritingText writingText;
    private WritingStat writingStat;

    private static final long STOP = -2;
    private static final long START = -1;
    private static final long LENGTH = 60 * 1000000l;
    private long stopTime;
    private long restyleTime;

    private ReadOnlyBooleanWrapper textReady;

    TextPaneControl(){
        stopTime = STOP;
    }

    @Override
    protected void setTextReadyProperty(ReadOnlyBooleanWrapper prop){
        textReady = prop;
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        new AnimationTimer(){
            @Override
            public void handle(long now) {updateTime(now); }
        }.start();

        control.writingTextProperty().addListener((d, o, n) -> loadText(n));

        control.writingStatProperty().addListener((d, o, n) -> loadStat(n));

        control.lastSelectedProperty().addListener((d, o, n) -> spanSelected(n));

        getTextArea().caretPositionProperty().addListener((d, o, n) ->
            caretMoved(n.intValue()));
        getTextArea().plainTextChanges().subscribe(this::textChanged);

    }

    private void caretMoved(int position){
        getLineTypeLabel().setText(writingText.getLeaf(position)
            /// s = SpanLeaf
            .flatMap(s -> s.getParent(LinedSpan.class))
            /// s = LinedSpan
            .map(s -> WindowText.getText(s))
            /// s = String
            .orElse("")
        );
    }

    /// %Part 1: Animation Timer

    private void updateTime(long now){
        getTimeLabel().setText(
            DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()
        ));
        if (stopTime == START){
            stopTime = now + LENGTH;

        } else if (stopTime != STOP){
            if (stopTime < now && textReady.getValue()){
                writingStat.stopWriting(writingText);
                setStatLabel();
                stopTime = STOP;
            }
        }
    }

    /// %Part 2: WritingTextProperty

    private void loadText(WritingText text){
        textReady.setValue(false);
        writingText = text;
        getTextArea().replaceText(text.getRaw());
        setStyle(text.getLeaves());
        textReady.setValue(text != null);
        caretMoved(getTextArea().getCaretPosition());
    }

    /// %Part 3: WritingDataProperty

    private void loadStat(WritingStat stat){
        textReady.setValue(false);
        writingStat = stat;
        if (stat != null){
            stat.addDocEdited(s -> setStatLabel());
        }
        textReady.setValue(stat != null);
        setStatLabel();
    }

    private void spanSelected(SpanBranch span){
        if (span == null) return;
        if (! isTextReady()) return;

        textReady.setValue(false);
        int position = span.getEnd();
        if (position == getTextArea().getLength()){
            getTextArea().end(NavigationActions.SelectionPolicy.CLEAR);
        } else {
            char found = writingText == null? (char) 0:
                writingText.getRaw().charAt(position - 1);
            getTextArea().moveTo(position - (found == '\n'? 1: 0));
        }
        textReady.setValue(true);
    }

    /// %Part 4: plainTextChanges

    private synchronized void textChanged(PlainTextChange change){
        if (! textReady.getValue()){
            return;
        }

        textReady.setValue(false);
        stateNotNull(writingText, "writingText");
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        /// update the text area
        int pos = change.getPosition();
        writingText.delete(pos, change.getRemovalEnd());
        writingText.insert(pos, change.getInserted());
        setStyle(writingText.getLeaves());

        ///Update the record
        stopTime = START;
        writingStat.startWriting(writingText);

        textReady.setValue(true);
        setStatLabel();
    }

    /// %Part 4: Utilities

    private void setStyle(Collection<SpanLeaf> leaves){
        for (SpanLeaf leaf: leaves){
            getTextArea().setStyle(
                leaf.getStart(), leaf.getEnd(), CodeStyleBuilder.toCss(leaf)
            );
        }
    }

    private void setStatLabel(){
        if (textReady.getValue()){
            StatSpanDay record = writingStat.getRecord();
            int word = record.getPublishWritten();
            double count = (word / (double) record.getPublishGoal()) * 100;

            Duration timer = record.getWriteTime();
            long hours = timer.toHours();
            long minutes = timer.toMinutes() % 60;
            long seconds = timer.getSeconds() % 60;
            double past = (timer.getSeconds() / (double) record.getTimeGoal()
                .getSeconds()) * 100;
            String text = String.format(
                "Publish: %d (%#.2f%%); Time: %d:%02d:%02d (%#.2f%%)",
                word, count, hours, minutes, seconds, past);
            getStatsLabel().setText(text);
        }
    }
}
