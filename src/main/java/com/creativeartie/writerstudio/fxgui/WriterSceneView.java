package com.creativeartie.writerstudio.fxgui;

import java.util.*;
import javafx.beans.binding.*;
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
    private List<TableDataControl<?>> tableTabs;

    private final ReadOnlyObjectWrapper<WritingFile> writingFile;
    private final ReadOnlyObjectWrapper<WritingText> writingText;
    private final ReadOnlyObjectWrapper<WritingStat> writingStat;
    private final ReadOnlyBooleanWrapper textReady;
    private final ReadOnlyIntegerWrapper caretPosition;
    private final SimpleObjectProperty<SpanBranch> lastSelected;

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

        writingFile.bind(mainMenuBar.writingFileProperty());
        writingStat.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getWritingFile())
                .map(f -> f.getRecords())
                .orElse(null)
        , writingFile));
        writingText.bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(getWritingFile())
                .map(f -> f.getDocument())
                .orElse(null)
        , writingFile));
        caretPosition.bind(getTextPane().caretPositionProperty());
        textReady.bind(getTextPane().textReadyProperty());

        addListeners(window);
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


    /// %Part 3: Abstract Methods

    protected abstract void addListeners(Stage window);

    /// %Part 4: Properties

    public ReadOnlyObjectProperty<WritingFile> writingFileProperty(){
        return writingFile.getReadOnlyProperty();
    }

    public WritingFile getWritingFile(){
        return writingFile.getValue();
    }

    public void setWritingFile(WritingFile file){
        mainMenuBar.setWritingFile(file);
    }

    public ReadOnlyObjectProperty<WritingText> writingTextProperty(){
        return writingText.getReadOnlyProperty();
    }

    public WritingText getWritingText(){
        return writingText.getValue();
    }

    public ReadOnlyObjectProperty<WritingStat> writingStatProperty(){
        return writingStat.getReadOnlyProperty();
    }

    public WritingStat getWritingStat(){
        return writingStat.getValue();
    }

    public ReadOnlyBooleanProperty textReadyProperty(){
        return textReady.getReadOnlyProperty();
    }

    public boolean getTextReady(){
        return textReady.getValue();
    }

    public ReadOnlyIntegerProperty caretPositionProperty(){
        return caretPosition.getReadOnlyProperty();
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

    /// %Part 5: Get Child Methods

    MenuBarMainControl getMainMenuBar(){
        return mainMenuBar;
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
