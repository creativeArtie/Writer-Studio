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

        getTableTabs().forEach(pane -> pane.loadList(currentDoc));
        getTableOfContent().loadHeadings(currentDoc);
        getTableOfContent().updateTable(getTextArea().getCaretPlaced());
        getTextArea().returnFocus();
        assert textReady(): getTextArea().getText();
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
        getTextArea().setReady(true);
        assert textReady(): getTextArea().getText();
    }

    @Override
    protected void timerAction(long now){
        if (! textReady()){
            return;
        }
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
        if (! textReady()){
            return;
        }
        if (span == null){
            return;
        }
        Range<Integer> range = span.getRange();
        if (! range.contains(getTextArea().getCaretPlaced())){
            getTextArea().moveTo(range.upperEndpoint());
        }
    }

    @Override
    protected void caretChanged(int position){
        if (! textReady()){
            return;
        }
        getTableTabs().forEach(pane -> {
            if (shouldMoveTable(pane, position)){pane.updateLocation(position);}
        });

        HeadingPaneControl content = getTableOfContent();
        if (shouldMoveHead(position)){
            getTableOfContent().updateTable(position);
        }
        getCheatsheet().updateLabels(currentDoc, position);
    }

    private boolean shouldMoveTable(TableDataControl<?> pane, int position){
        SpanBranch span = pane.getItemSelected();
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
        assert getTextArea().getText().equals(currentDoc.getRaw());
    }

    /** Check if text is ready for things other then editing.*/
    private boolean textReady(){
        if (getTextArea().getText().equals(currentDoc.getRaw())){
            return true;
        }
        getTextArea().setReady(false);
        return false;
    }
}
