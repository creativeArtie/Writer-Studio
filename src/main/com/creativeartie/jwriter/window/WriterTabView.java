package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import javafx.scene.control.*;
import org.fxmisc.richtext.model.*;
import javafx.animation.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.resource.*;

import com.google.common.collect.*;

abstract class WriterTabView extends TabPane{
    private List<TableDataControl<?>> tableTabs;
    private NoteCardControl noteCards;

    public WriterTabView(){
        setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        noteCards = initNoteCardPane();
        tableTabs = Arrays.asList(
            initAgendaPane(), initLinksPane(), initFootnotePane(),
            initEndnotePane()
        );
    }

    private TableAgendaPane initAgendaPane(){
        TableAgendaPane ans = new TableAgendaPane();
        Tab tab = new Tab(WindowText.TAB_AGENDA.getText(), ans);
        getTabs().add(tab);
        return ans;
    }

    private TableLinkPane initLinksPane(){
        TableLinkPane ans = new TableLinkPane();
        Tab tab = new Tab(WindowText.TAB_LINK.getText(), ans);
        getTabs().add(tab);
        return ans;
    }

    private TableNotePane initFootnotePane(){
        TableNotePane ans = new TableNotePane(DirectoryType.FOOTNOTE);
        Tab tab = new Tab(WindowText.TAB_FOOTNOTE.getText(), ans);
        getTabs().add(tab);
        return ans;
    }

    private TableNotePane initEndnotePane(){
        TableNotePane ans = new TableNotePane(DirectoryType.ENDNOTE);
        Tab tab = new Tab(WindowText.TAB_ENDNOTE.getText(), ans);
        getTabs().add(tab);
        return ans;
    }

    private NoteCardControl initNoteCardPane(){
        NoteCardControl ans = new NoteCardControl();
        Tab tab = new Tab(WindowText.TAB_NOTE_CARD.getText(), ans);
        getTabs().add(tab);
        return ans;
    }

    public List<TableDataControl<?>> getTableTabs(){
        return tableTabs;
    }

    public NoteCardControl getNoteCardsPane(){
        return noteCards;
    }
}