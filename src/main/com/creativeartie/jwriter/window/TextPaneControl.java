package com.creativeartie.jwriter.window;

import java.util.*;
import java.time.*;
import java.time.format.*;
import javafx.scene.control.*;

import org.fxmisc.richtext.*;

import com.google.common.base.*;

import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.window.*;

/**
 * Controller for the main text area.
 *
 * @see TextPaneView
 */
class TextPaneControl extends TextPaneView {

    @Override
    void updateTime(Label show){
        String text = DateTimeFormatter.ofPattern("HH:mm:ss")
            .format(LocalTime.now());
        show.setText(text);
    }

    void updateStats(Record record){
        int wordCount = record.getPublishTotal();
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
        getTextArea().replaceText(0, getTextArea().getLength(),
            writing.getRaw());
        setStyle(writing.getLeaves());
    }

    void setStyle(Collection<SpanLeaf> leaves){

        leaves.forEach(leaf -> getTextArea().setStyle(leaf.getStart(),
                    leaf.getEnd(), LeafStyleParser.SCREEN.toCss(leaf))
            );
    }


    public void moveTo(int position){
        if (position == getTextArea().getLength()){
            getTextArea().moveTo(position - 1);
        } else {
            getTextArea().moveTo(position);
        }
    }

    public void returnFocus(){
        getTextArea().requestFollowCaret();
        getTextArea().requestFocus();
    }

    @Override
    public WindowText setNextMode(WindowText last){
        WindowText ans = null;
        if (last == null){
            ans = WindowText.SYNTAX_MODE;
        } else {
            switch (last){
                case SYNTAX_MODE:
                    ans = WindowText.PARSED_MODE;
                    break;
                case PARSED_MODE:
                    ans = WindowText.SYNTAX_MODE;
                    break;
            }
        }
        getViewModeButton().setText(ans.getText());
        return ans;
    }
}