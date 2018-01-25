package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import java.time.*;
import java.time.format.*;
import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

public class WriterSceneControl extends WriterSceneView {

    private static final long START = -1;
    private static final long LENGTH = 60 * 1000000l;
    private long markedTime;

    private WritingText currentDoc;
    private RecordList currentRecords;

    public WriterSceneControl(javafx.stage.Stage window){
        super(window);
        markedTime = START;
    }

    @Override
    protected void changeDoc(ManuscriptFile file){
        currentDoc = file.getDocument();
        currentRecords = file.getRecords();

        getTextArea().setReady(false);
        getTextArea().loadDocumentText(currentDoc);
        getTextArea().updateStats(file.getRecords().getRecord());
        getTextArea().setReady(true);

        getAgendaPane().loadAgenda(currentDoc);
    }

    @Override
    protected synchronized void textChanged(PlainTextChange change){
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        /// update the text area
        int pos = change.getPosition();
        currentDoc.delete(pos, change.getRemovalEnd());
        currentDoc.insert(pos, change.getInserted());

        ///Update the record
        getTextArea().updateStats(getManuscriptFile().getRecords().getRecord());
        getTextArea().setStyle(currentDoc.getLeaves());
        markedTime = START;
        currentRecords.startWriting(currentDoc);
        assert getTextArea().getText().equals(currentDoc.getRaw());
    }

    @Override
    protected void updateRecord(long now){
        if (markedTime == START){
            markedTime = now + LENGTH;
        } else if (markedTime < now){
            currentRecords.stopWriting(currentDoc);
        }
    }

    @Override
    protected void selectionChanged(Range<Integer> range){
        System.out.print("selectedChanged: ");
        if (! range.contains(getTextArea().getCaretPlaced())){
            System.out.println("will move");
            getTextArea().moveTo(range.upperEndpoint());
        } else {
            System.out.println("will NOT move");
        }
    }

    @Override
    protected void caretChanged(int position){
        System.out.print("caretChanged: ");
        if (shouldMove(getAgendaPane().getAgendaSelected(), position)){
            System.out.println("will MAYBE move agenda");
            getAgendaPane().updateSelection(position);
        } else {
            System.out.println("will NOT move agenda");
        }
    }

    private boolean shouldMove(SpanBranch span, int position){
        if (span == null){
            return true;
        }
        Range<Integer> pos = span.getRange();
        if (pos.contains(position) || pos.upperEndpoint() == position){
            return false;
        }
        return true;
    }

    @Override
    public void returnFocus(){
        getTextArea().returnFocus();
    }
/*

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
        getCheatsheet().updateLabels(getDocument(), getTextArea().getCaretPlaced());
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
                        int pos = getTextArea().getCaretPlaced();
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
    }*/
}
