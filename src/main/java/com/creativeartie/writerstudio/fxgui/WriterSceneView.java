package com.creativeartie.writerstudio.fxgui;

import java.util.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

abstract class WriterSceneView extends BorderPane{
    private MenuBarMainControl mainMenuBar;
    private TextPaneControl textPane;
    private CheatsheetPaneControl cheatsheetPane;
    private NoteCardControl noteCardPane;
    private List<TableDataControl<?>> tableTabs;

    private final ReadOnlyObjectWrapper<WritingFile> writingFile;
    private final ReadOnlyObjectWrapper<WritingText> writingText;
    private final ReadOnlyObjectWrapper<WritingStat> writingStat;
    private final ReadOnlyBooleanWrapper textReady;
    private final ReadOnlyIntegerWrapper caretPosition;
    private final SimpleObjectProperty<SpanBranch> lastSelected;
    private final SimpleBooleanProperty refocusText;

    WriterSceneView(Stage window){
        getStylesheets().add(FileResources.getMainCss());

        setTop(buildMainMenu(window));
        setCenter(buildMainSplitPane());

        writingFile = new ReadOnlyObjectWrapper<>(this, "writingFile");
        writingStat = new ReadOnlyObjectWrapper<>(this, "writingStat");
        writingText = new ReadOnlyObjectWrapper<>(this, "writingText");
        caretPosition = new ReadOnlyIntegerWrapper(this, "caretPosition");
        textReady = new ReadOnlyBooleanWrapper(this, "textReady");
        lastSelected = new SimpleObjectProperty<>(this, "lastSelected");
        refocusText = new SimpleBooleanProperty(this, "refocusText");

        addBindings(window);
    }

    /// %Part 2: Layout

    /// %Part 2.1: Menu
    private MenuBarMainControl buildMainMenu(Stage window){
        mainMenuBar = new MenuBarMainControl(window);
        return mainMenuBar;
    }

    /// %Part 2.2: Main Split Pane

    private final SplitPane buildMainSplitPane(){
        SplitPane full = new SplitPane(buildTopTabs(), buildBottomPane());
        full.setOrientation(Orientation.VERTICAL);
        full.setDividerPositions(new double[]{.2, .8});
        return full;
    }

    /// %Part 2.2.1: Top taps

    private final TabPane buildTopTabs(){
        TabPane top = buildCommonTab();
        ImmutableList.Builder<TableDataControl<?>> builder = ImmutableList
            .builder();
        ArrayList<Tab> tabs = new ArrayList<>();

        noteCardPane = new NoteCardControl();
        tabs.add(new Tab(WindowText.TAB_NOTE_CARD.getText(), noteCardPane));

        tabs.add(buildTab(
            new TableAgendaPane(), WindowText.TAB_AGENDA, builder
        ));
        tabs.add(buildTab(
            new TableLinkPane(), WindowText.TAB_LINK, builder
        ));
        tabs.add(buildTab(
            new TableNotePane(DirectoryType.FOOTNOTE), WindowText.TAB_FOOTNOTE,
            builder
        ));
        tabs.add(buildTab(
            new TableNotePane(DirectoryType.ENDNOTE), WindowText.TAB_ENDNOTE,
            builder
        ));
        top.getTabs().addAll(tabs);
        tableTabs = builder.build();
        return top;
    }

    private static final Tab buildTab(TableDataControl<?> tab, WindowText title,
            ImmutableList.Builder<TableDataControl<?>> builder){
        Tab ans = new Tab(title.getText(), tab);
        builder.add(tab);
        return ans;
    }

    /// %Part 2.2.1: Bottom pane

    private final BorderPane buildBottomPane(){
        BorderPane bottom = new BorderPane();
        SplitPane center = new SplitPane(buildLeftTabs(), buildTextPane());
        center.setDividerPositions(new double[]{.0, 1.0});
        bottom.setCenter(center);
        bottom.setBottom(buildCheatsheetPane());
        return bottom;
    }

    /// %Part 2.2.1.1: Bottom pane (BorderPane Center - Split Left)

    private final TabPane buildLeftTabs(){
        TabPane left = buildCommonTab();
        return left;
    }

    /// %Part 2.2.1.2: Bottom pane (BorderPane Center - Split Right)

    private TextPaneControl buildTextPane(){
        textPane = new TextPaneControl();
        return textPane;
    }

    /// %Part 2.2.1.3: Bottom pane (BorderPane Bottom)

    private final CheatsheetPaneControl buildCheatsheetPane(){
        cheatsheetPane = new CheatsheetPaneControl();
        return cheatsheetPane;
    }

    private final static TabPane buildCommonTab(){
        TabPane ans = new TabPane();
        ans.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return ans;
    }


    /// %Part 3: Listener Methods

    protected abstract void addBindings(Stage window);

    /// %Part 4: Properties

    public ReadOnlyObjectProperty<WritingFile> writingFileProperty(){
        return writingFile.getReadOnlyProperty();
    }

    public WritingFile getWritingFile(){
        return writingFile.getValue();
    }

    protected ReadOnlyObjectWrapper<WritingFile> getWritingFileProperty(){
        return writingFile;
    }

    public void setWritingFile(WritingFile file){
        mainMenuBar.setWritingFile(file);
    }

    public ReadOnlyObjectProperty<WritingText> writingTextProperty(){
        return writingText.getReadOnlyProperty();
    }

    protected ReadOnlyObjectWrapper<WritingText> getWritingTextProperty(){
        return writingText;
    }

    public WritingText getWritingText(){
        return writingText.getValue();
    }

    public ReadOnlyObjectProperty<WritingStat> writingStatProperty(){
        return writingStat.getReadOnlyProperty();
    }

    protected ReadOnlyObjectWrapper<WritingStat> getWritingStatProperty(){
        return writingStat;
    }

    public WritingStat getWritingStat(){
        return writingStat.getValue();
    }

    public ReadOnlyBooleanProperty textReadyProperty(){
        return textReady.getReadOnlyProperty();
    }

    protected ReadOnlyBooleanWrapper getTextReadyProperty(){
        return textReady;
    }

    public boolean getTextReady(){
        return textReady.getValue();
    }

    public ReadOnlyIntegerProperty caretPositionProperty(){
        return caretPosition.getReadOnlyProperty();
    }

    protected ReadOnlyIntegerWrapper getCaretPositionProperty(){
        return caretPosition;
    }

    protected ReadOnlyIntegerWrapper caretPlacedProperty(){
        return caretPosition;
    }

    public int getCaretPosition(){
        return caretPosition.getValue();
    }

    public SimpleObjectProperty<SpanBranch> lastSelectedProperty(){
        return lastSelected;
    }

    public SpanBranch getLastSelected(){
        return lastSelected.getValue();
    }

    public void setLastSelected(SpanBranch span){
        lastSelected.setValue(span);
    }

    public BooleanProperty refocusTextProperty(){
        return refocusText;
    }

    public boolean isRefocusText(){
        return refocusText.getValue();
    }

    public void setRefocusText(boolean value){
        refocusText.setValue(value);
    }

    /// %Part 5: Get Child Methods
    MenuBarMainControl getMainMenuBar(){
        return mainMenuBar;
    }

    NoteCardControl getNoteCardPane(){
        return noteCardPane;
    }

    protected List<TableDataControl<?>> getTableTabs(){
        return tableTabs;
    }

    TextPaneControl getTextPane(){
        return textPane;
    }

    CheatsheetPaneControl getCheatsheetPane(){
        return cheatsheetPane;
    }
}
