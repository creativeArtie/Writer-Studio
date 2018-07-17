package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.control.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

abstract class WriterSceneView extends BorderPane{
    /// %Part 1: Constructor and Class Fields

    private static final double[] VER_DIVIDER = new double[]{.2, .8};
    private static final double[] HOR_DIVIDER = new double[]{.0, 1.0};

    private NoteCardPaneControl noteCardPane;
    private MenuBarMainControl mainMenuBar;
    private CheatsheetPaneControl cheatsheetPane;
    private TextPaneControl textPane;
    private MetaDataPaneControl metaDataPane;
    private List<TableDataControl<?>> dataTables;
    private HeadingsPaneControl headingsPane;

    private ReadOnlyObjectWrapper<WritingText> writingText;
    private ReadOnlyObjectWrapper<WritingStat> writingStat;
    private ReadOnlyObjectWrapper<WritingData> writingData;
    private SimpleObjectProperty<SpanBranch> lastSelected;
    private SimpleBooleanProperty refocusText;

    WriterSceneView(Stage window){
        getStylesheets().add(FileResources.getMainCss());
        setTop(buildTop(window));
        setCenter(buildSplitMain());

        writingText = new ReadOnlyObjectWrapper<>(this, "writingText");
        writingStat = new ReadOnlyObjectWrapper<>(this, "writingStat");
        writingData = new ReadOnlyObjectWrapper<>(this, "writingData");
        lastSelected = new SimpleObjectProperty<>(this, "lastSelected");
        refocusText = new SimpleBooleanProperty(this, "refocusText");
    }

    /// %Part 2: Layout

    private MenuBarMainControl buildTop(Stage window){
        mainMenuBar = new MenuBarMainControl(window);
        return mainMenuBar;
    }

    private SplitPane buildSplitMain(){
        SplitPane full = new SplitPane(buildTopTabs(), buildContent());
        full.setOrientation(Orientation.VERTICAL);
        full.setDividerPositions(VER_DIVIDER);
        return full;
    }

    private TabPane buildTopTabs(){
        TabPane top = buildTabPane();

        ArrayList<Tab> tabs = new ArrayList<>();

        noteCardPane = new NoteCardPaneControl();
        tabs.add(new Tab(WindowText.TAB_NOTE_CARD.getText(), noteCardPane));

        ImmutableList.Builder<TableDataControl<?>> builder =
            ImmutableList.builder();
        tabs.add(buildTab(new TableAgendaPane(), WindowText.TAB_AGENDA, builder));
        tabs.add(buildTab(new TableLinkPane(), WindowText.TAB_LINK, builder));
        tabs.add(buildTab(new TableNotePane(DirectoryType.FOOTNOTE),
            WindowText.TAB_FOOTNOTE, builder));
        tabs.add(buildTab(new TableNotePane(DirectoryType.ENDNOTE),
            WindowText.TAB_ENDNOTE, builder));
        dataTables = builder.build();

        tabs.add(buildTab(WindowText.TAB_REFERENCE, new ReferencePane()));

        top.getTabs().addAll(tabs);
        return top;
    }

    private static final Tab buildTab(TableDataControl<?> tab, WindowText title,
            ImmutableList.Builder<TableDataControl<?>> builder){
        Tab ans = new Tab(title.getText(), tab);
        builder.add(tab);
        return ans;
    }

    private BorderPane buildContent(){
        BorderPane content = new BorderPane();
        content.setCenter(buildSplitCenter());

        cheatsheetPane = new CheatsheetPaneControl();
        content.setBottom(cheatsheetPane);
        return content;
    }

    private SplitPane buildSplitCenter(){
        SplitPane center = new SplitPane(buildLeftTabs(), buildRightTabs());
        center.setDividerPositions(HOR_DIVIDER);
        return center;
    }

    private TabPane buildLeftTabs(){
        TabPane left = buildTabPane();

        headingsPane = new HeadingsPaneControl();
        Tab tree = new Tab(WindowText.TAB_CONTENT.getText(), headingsPane);

        metaDataPane = new MetaDataPaneControl();
        Tab meta = buildTab(WindowText.TAB_META, metaDataPane);

        left.getTabs().addAll(tree, meta);
        return left;
    }

    private TabPane buildRightTabs(){
        TabPane right = buildTabPane();

        textPane = new TextPaneControl();
        Tab main = buildTab(WindowText.TAB_TEXT, textPane);

        right.getTabs().addAll(main);
        return right;

    }

    private Tab buildTab(WindowText text, Node content){
        return new Tab(text.getText(), content);
    }

    private TabPane buildTabPane(){
        TabPane ans = new TabPane();
        ans.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return ans;
    }

    /// %Part 3: Setup Properties

    public void setupProperties(Scene scene){
        bindWritingText(writingText);
        bindWritingStat(writingStat);
        bindWritingData(writingData);
        bindChildren(scene);
    }


    protected abstract void bindWritingText(
        ReadOnlyObjectWrapper<WritingText> text);

    protected abstract void bindWritingStat(
        ReadOnlyObjectWrapper<WritingStat> stat);

    protected abstract void bindWritingData(
        ReadOnlyObjectWrapper<WritingData> data);

    protected abstract void bindChildren(Scene scene);

    /// %Part 4: Properties
    /// %Part 4.2: WritingText

    public ReadOnlyObjectProperty<WritingText> writingTextProperty(){
        return writingText.getReadOnlyProperty();
    }

    public WritingText getWritingText(){
        return writingText.getValue();
    }

    /// %Part 4.3: WritingStat

    public ReadOnlyObjectProperty<WritingStat> writingStatProperty(){
        return writingStat.getReadOnlyProperty();
    }

    public WritingStat getWritingStat(){
        return writingStat.getValue();
    }

    /// %Part 4.3: WritingData

    public ReadOnlyObjectProperty<WritingData> writingDataProperty(){
        return writingData.getReadOnlyProperty();
    }

    public WritingData getWritingData(){
        return writingData.getValue();
    }

    /// %Part 4.3: LastSelected

    public ObjectProperty<SpanBranch> lastSelectedProperty(){
        return lastSelected;
    }

    public SpanBranch getLastSelected(){
        return lastSelected.getValue();
    }

    public void setLastSelected(SpanBranch value){
        lastSelected.setValue(value);
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

    NoteCardPaneControl getNoteCardPane(){
        return noteCardPane;
    }

    CheatsheetPaneControl getCheatsheetPane(){
        return cheatsheetPane;
    }

    MetaDataPaneControl getMetaDataPane(){
        return metaDataPane;
    }

    TextPaneControl getTextPane(){
        return textPane;
    }

    HeadingsPaneControl getHeadingsPane(){
        return headingsPane;
    }

    List<TableDataControl<?>> getDataTables(){
        return dataTables;
    }
}
