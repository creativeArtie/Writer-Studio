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

    /// %Part 1: Private Fields and Constructor

    private WritingText writingText;
    private WritingStat writingStat;

    private static final long STOP = -2;
    private static final long START = -1;
    private static final long LENGTH = 60 * 1000000l;
    private long stopTime;
    private long restyleTime;

    private ReadOnlyBooleanWrapper textReady;
    private BooleanProperty refocusText;

    TextPaneControl(){
        stopTime = STOP;
    }

    /// %Part 2: Property Binding

    @Override
    protected void setTextReadyProperty(ReadOnlyBooleanWrapper prop){
        textReady = prop;
    }

    /// %Part 3: Bind Children Properties

    @Override
    protected void setupChildern(WriterSceneControl control){
        new AnimationTimer(){
            @Override
            public void handle(long now) {showTime(now); }
        }.start();

        control.writingTextProperty().addListener((d, o, n) -> loadText(n));

        control.writingStatProperty().addListener((d, o, n) -> loadStat(n));

        control.lastSelectedProperty().addListener((d, o, n) -> showSelected(n));

        getTextArea().caretPositionProperty().addListener((d, o, n) ->
            showCaret(n.intValue()));
        getTextArea().plainTextChanges().subscribe(this::textChanged);

        refocusText = control.refocusTextProperty();
    }

    /// %Part 3.1: new AnimationTimer(){...}.start()

    private void showTime(long now){
        getTimeLabel().setText(
            DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()
        ));
        if (stopTime == START){
            stopTime = now + LENGTH;

        } else if (stopTime != STOP){
            if (stopTime < now && textReady.getValue()){
                writingStat.stopWriting(writingText);
                showStats();
                stopTime = STOP;
            }
        }
    }

    /// %Part 3.2: control.writingTextProperty()

    private void loadText(WritingText text){
        writingText = text;
        if (text != null){
            text.addDocEdited(s -> updateText());
        }
        updateText();
    }

    private void updateText(){}

    /// %Part 3.3: control.writingStatProperty()

    private void loadStat(WritingStat stat){}

    /// %Part 3.4: control.lastSelectedProperty()

    private void showSelected(SpanBranch span){}

    /// %Part 3.5: getTextArea().caretPositionProperty()
    private void showCaret(int position){}


    /// %Part 3.6: getTextArea().plainTextChanges()

    private synchronized void textChanged(PlainTextChange change){}

    /// %Part 4: Utilities

    private void updateStyles(Collection<SpanLeaf> leaves){
        for (SpanLeaf leaf: leaves){
            getTextArea().setStyle(
                leaf.getStart(), leaf.getEnd(), CodeStyleBuilder.toCss(leaf)
            );
        }
    }

    private void showStats(){
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
