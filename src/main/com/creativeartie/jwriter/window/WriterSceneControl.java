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

    private static final long STOP = -2;
    private static final long START = -1;
    private static final long LENGTH = 60 * 1000000l;
    private long writeTime;
    private long restyleTime;

    private WritingText currentDoc;
    private RecordList currentRecords;

    public WriterSceneControl(javafx.stage.Stage window){
        super(window);
        writeTime = START;
    }

    @Override
    protected void changeDoc(ManuscriptFile file){
        currentDoc = file.getDocument();
        currentRecords = file.getRecords();

        getTextArea().setReady(false);
        getTextArea().loadDocumentText(currentDoc);
        getTextArea().updateStats(currentRecords.getRecord());
        getTextArea().setReady(true);

        getAgendaPane().loadAgenda(currentDoc);
        getTableOfContent().loadHeadings(currentDoc);
    }

    @Override
    protected synchronized void textChanged(PlainTextChange change){
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        /// update the text area
        int pos = change.getPosition();
        currentDoc.delete(pos, change.getRemovalEnd());
        currentDoc.insert(pos, change.getInserted());

        ///Update the record
        writeTime = START;
        restyleTime = restyleTime == STOP? START: restyleTime;
        currentRecords.startWriting(currentDoc);
        assert getTextArea().getText().equals(currentDoc.getRaw());
    }

    @Override
    protected void timerAction(long now){
        getTextArea().updateStats(currentRecords.getRecord());
        if (writeTime == START){
            writeTime = now + LENGTH;
        } else if (writeTime < now){
            currentRecords.stopWriting(currentDoc);
        }
        if (restyleTime == START){
            restyleTime = now + LENGTH;
        } else if (restyleTime < now){
            getTextArea().setStyle(currentDoc.getLeaves());
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
        getCheatsheet().updateLabels(currentDoc, position);
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
}
