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
import com.creativeartie.writerstudio.javafx.utils.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    TextPaneConstants.*;

/**
 * Controller for the main text area.
 *
 * @see TextPaneView
 */
class TextPaneControl extends TextPaneView {

    private boolean isReady;
    private long stopTime;
    private ReadOnlyIntegerWrapper updatedPosition;
    private WritingText writingText;
    private WritingStat writingStat;

    /// %Part 1: Private Fields and Constructor
    /// %Part 2: Property Binding

    protected void bindUpdatedPosition(ReadOnlyIntegerWrapper property){
        updatedPosition = property;
    }
    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(WriterSceneControl control){
        new AnimationTimer(){
            @Override
            public void handle(long now) {listenTimer(now); }
        }.start();

        control.writingStatProperty().addListener((d, o, n) ->
            listenWritingStat(n));
        control.writingTextProperty().addListener((d, o, n) ->
            listenWritingText(n));

        getTextArea().plainTextChanges().subscribe(this::listenTextChange);
        getTextArea().caretPositionProperty().addListener((d, o, n) ->
            listenCaret(n.intValue()));
        isReady = true;
    }

    /// %Part 3.1: new AnimationTimer(...).start();

    private void listenTimer(long now){
        getClockLabel().setText(
            DateTimeFormatter.ofPattern(CLOCK_FORMAT).format(LocalTime.now()
        ));

       showStats();

       if (writingStat == null) return;

        if (stopTime == START){
            stopTime = now + LENGTH;

        } else if (stopTime != STOP){
            writingStat.stopWriting(writingText);
            stopTime = STOP;
        }
    }

    /// %Part 3.2: control.writingStatProperty()

    private void listenWritingStat(WritingStat stat){
        isReady = false;
        writingStat = stat;
        if (stat != null) {
            writingText.addDocEdited(t -> showStats());
        }
        if (writingText != null) writingStat.stopWriting(writingText);
        showStats();
        isReady = true;
    }

    /// %Part 3.3: control.writingTextProperty()

    private void listenWritingText(WritingText text){
        writingText = text;
        if (writingText != null){
            writingText.addDocEdited(d -> listenWritingText());
        }
        listenWritingText();
    }

    private void listenWritingText(){
        isReady = false;
        if (writingText == null) return;
        getTextArea().replaceText(writingText.getRaw());
        updateStyles(writingText.getLeaves());
        if (writingStat != null) writingStat.stopWriting(writingText);
        syncDocuments(null);
        isReady = true;
    }

    /// %Part 3.4: getTextArea().plainTextChanges().subscribe(...);

    private synchronized void listenTextChange(PlainTextChange change){
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        if (! isReady || writingStat == null || writingText == null) return;
        isReady = false;
        /// update the text area
        int pos = change.getPosition();

        writingText.delete(pos, change.getRemovalEnd());
        writingText.insert(pos, change.getInserted());
        updateStyles(writingText.getLeaves());

        /// Update the record
        stopTime = START;
        writingStat.startWriting(writingText);

        syncDocuments(change);
        isReady = true;
    }

    /// %Part 3.5: getTextArea().caretPositionProperty()

    private void listenCaret(int position){
        getLineTypeLabel().setText(writingText.getLeaf(position)
            /// s = SpanLeaf
            .flatMap(s -> s.getParent(LinedSpan.class))
            /// s = LinedSpan
            .map(s -> getLineText(s))
            /// s = String
            .orElse("")
        );
    }

    public static String getLineText(LinedSpan span){
        if (span instanceof LinedSpanLevelSection){
            LinedSpanLevelSection line = (LinedSpanLevelSection) span;
            return (line.isHeading()? HEADING: OUTLINE) +
                " " + line.getLevel();
        } else if( span instanceof  LinedSpanLevelList){
            LinedSpanLevelList line = (LinedSpanLevelList) span;
            return (line.isNumbered()? NUMBERED: BULLET) +
                " " + line.getLevel();
        } else if (span instanceof LinedSpanPointNote){
            return ((LinedSpanPointNote) span).getDirectoryType() ==
                DirectoryType.FOOTNOTE?
                FOOTNOTE: ENDNOTE;
        } else if (span instanceof LinedSpanPointLink){
            return LINK;
        } else if (span instanceof LinedSpanNote){
            return NOTE;
        } else if (span instanceof LinedSpanAgenda){
            return AGENDA;
        } else if (span instanceof LinedSpanQuote){
            return QUOTE;
        } else if (span instanceof LinedSpanBreak){
            return BREAK;
        } else if (span instanceof LinedSpanCite){
            return SOURCE;
        } else if (span instanceof LinedSpanParagraph){
            return PARAGRAPH;
        }
        assert false: "Missing code for: " + span.getClass();
        return "";
    }

    /// %Part 4: Utilities

    /** Way to protect error differences between interface and document */
    private void syncDocuments(PlainTextChange change){
        if(! writingText.getRaw().equals(getTextArea().getText())){
            System.err.println("==========================================");
            System.err.println("Text in interface and in document mismatch");
            System.err.println("reparsing all");
            Thread.currentThread().dumpStack();
            System.err.println("problem change: " + change);
            System.err.println();
            System.err.println("interface text: \n" + getTextArea().getText());
            System.err.println();
            System.err.println("document text: \n" + writingText.getRaw());

            writingText.replaceText(getTextArea().getText());
            updateStyles(writingText.getLeaves());
            listenCaret(getTextArea().getCaretPosition());
        }
    }

    private void updateStyles(Collection<SpanLeaf> leaves){
        for (SpanLeaf leaf: leaves){
            getTextArea().setStyle(
                leaf.getStart(), leaf.getEnd(), CodeStyleBuilder.toCss(leaf)
            );
        }
    }

    private void showStats(){
        if (writingStat == null) return;
        StatSpanDay record = writingStat.getRecord();
        int word = record.getPublishWritten();
        double count = (word / (double) record.getPublishGoal()) * 100;

        Duration timer = record.getWriteTime();
        long hours = timer.toHours();
        long minutes = timer.toMinutes() % 60;
        long seconds = timer.getSeconds() % 60;
        double past = (timer.getSeconds() / (double) record.getTimeGoal()
            .getSeconds()) * 100;
        String text = String.format(STAT_TEXT, word, count, hours, minutes,
            seconds, past);
        getStatsLabel().setText(text);
    }
}
