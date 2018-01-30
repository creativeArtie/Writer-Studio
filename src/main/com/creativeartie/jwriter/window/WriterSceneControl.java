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
        getTableOfContent().updateTable(getTextArea().getCaretPlaced());
        getTextArea().returnFocus();
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
    protected void selectionChanged(SpanBranch span){
        System.out.print("selectedChanged: ");
        if (span == null){
            System.out.println("will NOT move");
            return;
        }
        Range<Integer> range = span.getRange();
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
        if (shouldMoveAgenda(position)){
            System.out.print("will MAYBE move agenda");
            getAgendaPane().updateAgenda(position);
        } else {
            System.out.print("will NOT move agenda");
        }
        HeadingPaneControl content = getTableOfContent();
        if (shouldMoveHead(position)){
            System.out.println(" & will MAYBE move table of content");
            getTableOfContent().updateTable(position);
        } else {
            System.out.println(" & will NOT move table of content");
        }
        getCheatsheet().updateLabels(currentDoc, position);
    }

    private boolean shouldMoveAgenda(int position){
        SpanBranch span = getAgendaPane().getAgendaSelected();
        if (span == null){
            return true;
        }
        Range<Integer> pos = span.getRange();
        if (pos.contains(position) || pos.upperEndpoint() == position){
            return false;
        }
        return true;
    }

    private boolean shouldMoveHead(int position){
        SectionSpanHead head = getTableOfContent().getHeadingSelected();
        if (head == null){
            return true;
        }
        Range<Integer> pos = head.getRange();
        if (pos.contains(position) || pos.upperEndpoint() == position){
            if(head.getSections().isEmpty() && head.getScenes().isEmpty()){
                /// 2 ifs because it get too complicated when combined
                return false;
            }
        } else {
            return true;
        }

        /// is inside the SectionSpan, but it is in on of the children?
        List<? extends SectionSpan> list = head.getSections();
        if(! list.isEmpty() && list.get(0).getStart() < position){
            return true;
        }

        list = head.getScenes();
        if (! list.isEmpty() && list.get(0).getStart() < position){
            return true;
        }
        return false;
    }


    @Override
    public void returnFocus(){
        getTextArea().returnFocus();
    }
}
