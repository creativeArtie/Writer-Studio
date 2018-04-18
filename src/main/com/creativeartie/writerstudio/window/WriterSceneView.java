package com.creativeartie.writerstudio.window;

import java.util.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import javafx.scene.control.*;
import org.fxmisc.richtext.model.*;
import javafx.animation.*;
import javafx.geometry.*;

import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

import com.google.common.collect.*;

abstract class WriterSceneView extends BorderPane{
    private TextPaneControl textArea;
    private WriterMenuBar menuBar;
    private HeadingPaneControl tableOfContent;
    private MetaDataPaneControl metaDatas;
    private List<TableDataControl<?>> tableTabs;
    private NoteCardControl noteCards;
    private CheatsheetPaneControl langCheatsheet;
    private SimpleObjectProperty<ManuscriptFile> manuscriptFile;
    private double[] verDividerPos;
    private double[] hozDividerPos;

    WriterSceneView(Stage window){
        getStylesheets().add(FileResources.getMainCss());
        verDividerPos = new double[]{.2, .8};
        hozDividerPos = new double[]{.0, 1.0};

        manuscriptFile = new SimpleObjectProperty<>(this, "manuscriptFile");
        manuscriptFile.addListener((data, oldValue, newValue) ->
            changeDoc(newValue));

        /// Top
        menuBar = initMenuBar(window);
        setTop(menuBar);

        /// Upper split pane
        TabPane top = initTabPane();
        noteCards = initNoteCardPane(top);
        noteCards.locationChoosenProperty().addListener(
            (data, oldValue, newValue) -> moveCursor(newValue.intValue())
        );
        tableTabs = Arrays.asList(
            initAgendaPane(top), initLinksPane(top),
            initFootnotePane(top), initEndnotePane(top)
        );
        tableTabs.forEach(pane -> {
            pane.focusedProperty().addListener((data, oldValue, newValue) ->
                returnFocus());
            pane.itemSelectedProperty().addListener(
                (data, oldValue, newValue) -> selectionChanged(newValue)
            );
        });
        initReferencePane(top);

        /// Inner SplitPane - Right
        textArea = initTextPane();
        textArea.textChangedProperty().addListener((data, oldValue, newValue) ->
            textChanged(newValue));
        textArea.caretPlacedProperty().addListener((data, oldValue, newValue) ->
            caretChanged(newValue.intValue()));
        /// Inner SplitPane - Left
        TabPane left = initTabPane();
        tableOfContent = initTableOfContent(left);
        tableOfContent.treeFocusedProperty().addListener((data, oldValue,
            newValue) -> returnFocus());
        tableOfContent.headingSelectedProperty().addListener(
            (data, oldValue, newValue) -> selectionChanged(newValue)
        );
        tableOfContent.outlineSelectedProperty().addListener(
            (data, oldValue, newValue) -> selectionChanged(newValue)
        );
        metaDatas = initMetaTable(left);

        /// bottom
        langCheatsheet = initCheatsheetPane();

        SplitPane center = new SplitPane(left, textArea);
        center.setDividerPositions(hozDividerPos);
        BorderPane bottom = new BorderPane();
        bottom.setCenter(center);

        bottom.setBottom(langCheatsheet);

        SplitPane full = new SplitPane(top, bottom);
        full.setOrientation(Orientation.VERTICAL);
        full.setDividerPositions(verDividerPos);
        setCenter(full);

        new AnimationTimer(){
            @Override public void handle(long now) {timerAction(now);}
        }.start();

    }

    /// Layout Nodes
    private TextPaneControl initTextPane(){
        TextPaneControl out = new TextPaneControl();
        return out;
    }

    private WriterMenuBar initMenuBar(Stage window){
        WriterMenuBar ans = new WriterMenuBar(window);
        ans.manuscriptFileProperty().bindBidirectional(manuscriptFile);
        return ans;
    }

    private TabPane initTabPane(){
        TabPane ans = new TabPane();
        ans.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return ans;
    }

    private TableAgendaPane initAgendaPane(TabPane parent){
        TableAgendaPane ans = new TableAgendaPane();
        Tab tab = new Tab(WindowText.TAB_AGENDA.getText(), ans);
        parent.getTabs().add(tab);
        return ans;
    }

    private TableLinkPane initLinksPane(TabPane parent){
        TableLinkPane ans = new TableLinkPane();
        Tab tab = new Tab(WindowText.TAB_LINK.getText(), ans);
        parent.getTabs().add(tab);
        return ans;
    }

    private TableNotePane initFootnotePane(TabPane parent){
        TableNotePane ans = new TableNotePane(DirectoryType.FOOTNOTE);
        Tab tab = new Tab(WindowText.TAB_FOOTNOTE.getText(), ans);
        parent.getTabs().add(tab);
        return ans;
    }

    private TableNotePane initEndnotePane(TabPane parent){
        TableNotePane ans = new TableNotePane(DirectoryType.ENDNOTE);
        Tab tab = new Tab(WindowText.TAB_ENDNOTE.getText(), ans);
        parent.getTabs().add(tab);
        return ans;
    }

    private ReferencePane initReferencePane(TabPane parent){
        ReferencePane ans = new ReferencePane();
        Tab tab = new Tab(WindowText.TAB_REFERENCE.getText(), ans);
        parent.getTabs().add(tab);
        return ans;
    }

    private NoteCardControl initNoteCardPane(TabPane parent){
        NoteCardControl ans = new NoteCardControl();
        Tab tab = new Tab(WindowText.TAB_NOTE_CARD.getText(), ans);
        parent.getTabs().add(tab);
        return ans;
    }

    private CheatsheetPaneControl initCheatsheetPane(){
        CheatsheetPaneControl ans = new CheatsheetPaneControl();
        return ans;
    }

    private HeadingPaneControl initTableOfContent(TabPane parent){
        HeadingPaneControl ans = new HeadingPaneControl();
        Tab tab = new Tab(WindowText.TAB_CONTENT.getText(), ans);
        parent.getTabs().add(tab);
        return ans;
    }

    private MetaDataPaneControl initMetaTable(TabPane parent){
        MetaDataPaneControl ans = new MetaDataPaneControl();
        Tab tab = new Tab(WindowText.TAB_META.getText(), ans);
        parent.getTabs().add(tab);
        return ans;
    }

    /// Getters
    protected TextPaneControl getTextArea(){
        return textArea;
    }

    protected MetaDataPaneControl getMetaDataPane(){
        return metaDatas;
    }

    protected HeadingPaneControl getTableOfContent(){
        return tableOfContent;
    }

    protected List<TableDataControl<?>> getTableTabs(){
        return tableTabs;
    }

    protected NoteCardControl getNoteCardsPane(){
        return noteCards;
    }

    protected CheatsheetPaneControl getCheatsheet(){
        return langCheatsheet;
    }

    protected WriterMenuBar getMenuBar(){
        return menuBar;
    }

    /// Node Properties
    public ObjectProperty<ManuscriptFile> manuscriptFileProperty(){
        return manuscriptFile;
    }

    public ManuscriptFile getManuscriptFile(){
        return manuscriptFile.getValue();
    }

    public void setManuscriptFile(ManuscriptFile doc){
        manuscriptFile.setValue(doc);
    }

    /// Control Methods
    protected abstract void changeDoc(ManuscriptFile file);

    protected abstract void textChanged(PlainTextChange changes);

    protected abstract void selectionChanged(SpanBranch range);

    protected abstract void caretChanged(int position);

    protected abstract void returnFocus();

    protected abstract void timerAction(long now);

    protected abstract void moveCursor(int position);
}