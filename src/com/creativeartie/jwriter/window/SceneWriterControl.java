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

public class SceneWriterControl extends SceneWriterView {

    private long timer;
    private static final long TIMER_OFF = -1;
    private static final long TIMER_LENGHT = 1000;

    public SceneWriterControl(javafx.stage.Stage window){
        super(window);
        timer = TIMER_OFF;
    }

    protected void controlSetup(){
        new AnimationTimer(){
            @Override
            public void handle(long now) {
                RecordTable record = getRecords();
                ManuscriptDocument doc = getDocument();
                if (record != null && doc != null){
                    if (isEdited()){
                        setEdited(false);
                        record.startWriting(doc.getPublishCount(),
                            doc.getNoteCount());
                    } else {
                        record.stopWriting(doc.getPublishCount(),
                            doc.getNoteCount());
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
        int wordCount = record.getPublishCount();
        double wordPrecent = (wordCount / (double) record.getPublishGoal()) * 100;

        Duration timer = record.getWriteDuration();
        long hours = timer.toHours();
        long minutes = timer.toMinutes() % 60;
        long seconds = timer.getSeconds() % 60;
        double timePrecent = (timer.getSeconds() / (double) record.getTimeGoal()
            .getSeconds()) * 100;
        return String.format("Publish: %d (%#.2f%%); Time: %d:%02d:%02d (%#.2f%%)",
            wordCount, wordPrecent, hours, minutes, seconds, timePrecent);
    }

    @Override
    protected void listenDoc(){
        setTextReady(false);
        getTextArea().loadDoc(getDocument());
        getUserLists().loadDoc(getDocument());
        getUserLists().selectType(DirectoryType.NOTE);
        getTextArea().returnFocus();
        setTextReady(true);
        updateDoc();
    }

    private void updateDoc(){
        if (timer == TIMER_OFF && isTextReady()) {
            new AnimationTimer(){
                @Override
                public void handle(long now) {
                    if (timer == TIMER_OFF){
                        timer = now;
                    } else if (timer + TIMER_LENGHT < now){
                        getTableOfContent().loadHeadings(getDocument());
                        getAgendaList().fillAgenda(getDocument());
                        int pos = getTextArea().getPosition();
                        getAgendaList().updateSelection(getDocument(), pos);
                        getTableOfContent().setHeading(getDocument(), pos);
                        getTextArea().updateCss(getDocument());
                        getUserLists().refreshPane(getDocument());
                        getTextArea().returnFocus();
                        timer = TIMER_OFF;
                        stop();
                    }
                }
            }.start();
        }
    }

    @Override
    protected void listenTextChange(PlainTextChange change){
        setEdited(true);
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        if (isTextReady()){
            int pos = change.getPosition();
            getDocument().delete(pos, change.getRemovalEnd());
            getDocument().insert(pos, change.getInserted());
            updateDoc();
        }
    }

    @Override
    protected void listenCaret(int moveTo){
        if (isTextReady()){
            getAgendaList().updateSelection(getDocument(), moveTo);
            getTableOfContent().setHeading(getDocument(), moveTo);
        }
    }

    protected void listenAgenda(boolean focused){
        if (focused){
            getTextArea().moveTo(getAgendaList().getAgenda());
            getTextArea().returnFocus();
        }
    }

    protected void listenHeading(boolean focused){
        if (focused){ /// Just in case
            getTableOfContent().getHeading().ifPresent(span ->
                getTextArea().moveTo(span)
            );
            getTextArea().returnFocus();
        }
    }

    protected void listenOutline(boolean focused){
        if (focused){
            getTableOfContent().getOutline().ifPresent(span ->
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