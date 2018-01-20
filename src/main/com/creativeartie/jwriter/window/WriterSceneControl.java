package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.animation.*;
import java.time.*;
import java.time.format.*;
import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

public class WriterSceneControl extends WriterSceneView {

    private long updateTimer;
    private long editTimer;
    private static final long TIMER_START = -2;
    private static final long TIMER_OFF = -1;
    private static final long EDIT_LENGTH = 30 * 1000000000l;
    private static final long TIMER_LENGHT = 1000;

    public WriterSceneControl(javafx.stage.Stage window){
        super(window);
        updateTimer = TIMER_OFF;
        editTimer = TIMER_OFF;
    }

    protected void controlSetup(){
        new AnimationTimer(){
            @Override
            public void handle(long now) {
                RecordList record = getRecords();
                WritingText doc = getDocument();
                if (record != null && doc != null){
                    if (editTimer == TIMER_START){
                       editTimer = now;
                       record.startWriting(doc.getPublishTotal(),
                          doc.getNoteTotal());
                    }
                    else if (editTimer + EDIT_LENGTH >= now){
                        editTimer = TIMER_OFF;
                        record.stopWriting(doc.getPublishTotal(),
                            doc.getNoteTotal());
                    }
                }
                String time = DateTimeFormatter.ofPattern("HH:mm:ss")
                    .format(LocalTime.now());
                getTextArea().getCurrentTimeLabel().setText(time);
                getTextArea().getCurrentStatsLabel().setText(getStats());
            }
        }.start();
    }

    private String getStats(){
        Record record = getRecords().getRecord();
        int wordCount = record.getPublishTotal();
        double wordPrecent = (wordCount / (double) record.getPublishGoal()) * 100;

        Duration timer = record.getWriteTime();
        long hours = timer.toHours();
        long minutes = timer.toMinutes() % 60;
        long seconds = timer.getSeconds() % 60;
        double timePrecent = (timer.getSeconds() / (double) record.getTimeGoal()
            .getSeconds()) * 100;
        return String.format("Publish: %d (%#.2f%%); Time: %d:%02d:%02d (%#.2f%%)",
            wordCount, wordPrecent, hours, minutes, seconds, timePrecent);
    }

    @Override
    protected synchronized void listenDoc(){
        setTextReady(false);
        getTextArea().loadDoc(getDocument());
        getUserLists().loadNotes(getDocument());
        getUserLists().selectType(DirectoryType.NOTE);
        getTextArea().returnFocus();
        setTextReady(true);
        updateDoc();
        getCheatsheet().updateLabels(getDocument(), getTextArea().getCursorPlaced());
    }

    private void updateDoc(){
        if (updateTimer == TIMER_OFF && isTextReady()) {
            new AnimationTimer(){
                @Override
                public void handle(long now) {
                    if (updateTimer == TIMER_OFF){
                        updateTimer = now;
                    } else if (updateTimer + TIMER_LENGHT < now){
                        getTableOfContent().loadTrees(getDocument());
                        getAgendaPane().loadAgenda(getDocument());
                        int pos = getTextArea().getCursorPlaced();
                        getAgendaPane().updateSelection(getDocument(), pos);
                        getTableOfContent().setHeading(getDocument(), pos);
                        getTextArea().updateCss(getDocument());
                        getUserLists().refreshPane(getDocument());
                        getTextArea().returnFocus();
                        updateTimer = TIMER_OFF;
                        stop();
                    }
                }
            }.start();
        }
    }

    @Override
    protected synchronized void listenTextChange(PlainTextChange change){
        setUpdateded(true);

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        if (isTextReady()){
            int pos = change.getPosition();
            getDocument().delete(pos, change.getRemovalEnd());
            getDocument().insert(pos, change.getInserted());
            updateDoc();
            editTimer = TIMER_START;
        }
    }

    @Override
    protected void listenCaret(int moveTo){
        if (isTextReady()){
            getAgendaPane().updateSelection(getDocument(), moveTo);
            getTableOfContent().setHeading(getDocument(), moveTo);
        }
        getCheatsheet().updateLabels(getDocument(), moveTo);
    }

    protected void listenAgenda(boolean focused){
        if (focused){
            getTextArea().moveTo(getAgendaPane().getAgendaSelected());
            getTextArea().returnFocus();
        }
    }

    protected void listenHeading(boolean focused){
        if (focused){ /// Just in case
            getTableOfContent().getHeadingSelected().ifPresent(span ->
                getTextArea().moveTo(span)
            );
            getTextArea().returnFocus();
        }
    }

    protected void listenOutline(boolean focused){
        if (focused){
            getTableOfContent().getOutlineSelected().ifPresent(span ->
                getTextArea().moveTo(span)
            );
            getTextArea().returnFocus();
        }}

    @Override
    protected void listenLocClicked(int loc){
        getTextArea().moveTo(loc);
        getTextArea().returnFocus();
    }

    @Override
    protected void listenListFocused(boolean focused){
        getTextArea().returnFocus();
    }
}
