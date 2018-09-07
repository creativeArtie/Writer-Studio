package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.control.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.javafx.utils.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;


import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    MainWindowConstants.*;

abstract class WriterSceneView extends BorderPane{

    /// %Part 1: Constructor and Class Fields

    private ReadOnlyObjectWrapper<WritingText> writingText;
    private ReadOnlyObjectWrapper<WritingStat> writingStat;
    private ReadOnlyObjectWrapper<WritingData> writingData;
    private SimpleBooleanProperty refocusText;
    private SimpleObjectProperty<SpanBranch> lastSelected;

    private MenuBarMainControl mainMenuBar;

    private NoteCardPaneControl noteCardPane;
    private DataPaneAgenda agendaPane;
    private DataPaneLink linkPane;
    private DataPaneNote footnotePane;
    private DataPaneNote endnotePane;

    private CheatsheetPaneControl cheatsheetPane;

    private HeadingsPaneControl headingPane;
    private MetaDataPaneControl metaDataPane;

    private TabPane mainTabs;
    private TextPaneControl textPane;
    private ResearchPaneControl researchPane;

    WriterSceneView(Stage window){
        getStylesheets().add(FileResource.MAIN_CSS.getCssPath());

        writingText = new ReadOnlyObjectWrapper<>(this, "writingText");
        writingStat = new ReadOnlyObjectWrapper<>(this, "writingStat");
        writingData = new ReadOnlyObjectWrapper<>(this, "writingData");
        refocusText = new SimpleBooleanProperty(this, "refocusText");
        lastSelected = new SimpleObjectProperty<>(this, "lastSelected");

        setTop(buildTop(window));
        setCenter(buildSplitMain());
    }

    /// %Part 2: Layout

    /// %Part 2 (pane -> menu bar)

    private MenuBarMainControl buildTop(Stage window){
        mainMenuBar = new MenuBarMainControl(window);
        return mainMenuBar;
    }

    /// %Part 2 (pane -> top-down split)

    private SplitPane buildSplitMain(){
        SplitPane full = new SplitPane(buildTopTabs(), buildContent());
        full.setOrientation(Orientation.VERTICAL);
        full.setDividerPositions(VER_DIVIDER);
        return full;
    }

    /// %Part 2 (pane -> top-down split -> top)

    private TabPane buildTopTabs(){
        TabPane tabs = CommonLayoutUtility.buildTabPane();

        noteCardPane = new NoteCardPaneControl();
        CommonLayoutUtility.addTab(tabs, TAB_NOTE_CARD, noteCardPane);

        agendaPane = new DataPaneAgenda();
        CommonLayoutUtility.addTab(tabs, TAB_AGENDA, agendaPane);

        linkPane = new DataPaneLink();
        CommonLayoutUtility.addTab(tabs, TAB_LINK, linkPane);

        footnotePane = new DataPaneNote(DirectoryType.FOOTNOTE);
        CommonLayoutUtility.addTab(tabs, TAB_FOOTNOTE, footnotePane);

        endnotePane = new DataPaneNote(DirectoryType.ENDNOTE);
        CommonLayoutUtility.addTab(tabs, TAB_ENDNOTE, endnotePane);

        ReferencePane ref = new ReferencePane();
        CommonLayoutUtility.addTab(tabs, TAB_REFERENCE, ref);
        return tabs;
    }

    /// %Part 2 (pane -> top-down split -> bottom)

    private BorderPane buildContent(){
        BorderPane content = new BorderPane();
        content.setCenter(buildSplitCenter());

        cheatsheetPane = new CheatsheetPaneControl();
        content.setBottom(cheatsheetPane);
        return content;
    }

    /// %Part 2 (pane -> top-down split -> bottom -> left-right split)

    private SplitPane buildSplitCenter(){
        SplitPane center = new SplitPane(buildLeftTabs(), buildRightTabs());
        center.setDividerPositions(HOR_DIVIDER);
        return center;
    }

    /// %Part 2 (pane -> top-down split -> bottom -> left-right split -> left)

    private TabPane buildLeftTabs(){
        TabPane tabs = CommonLayoutUtility.buildTabPane();

        headingPane = new HeadingsPaneControl();
        CommonLayoutUtility.addTab(tabs, TAB_HEADINGS, headingPane);

        metaDataPane = new MetaDataPaneControl();
        CommonLayoutUtility.addTab(tabs, TAB_META, metaDataPane);

        return tabs;
    }

    /// %Part 2 (pane -> top-down split -> bottom -> left-right split -> right)

    private TabPane buildRightTabs(){
        mainTabs = CommonLayoutUtility.buildTabPane();
        textPane = new TextPaneControl();
        CommonLayoutUtility.addTab(mainTabs, TAB_CONTENT, textPane);

        researchPane = new ResearchPaneControl();
        CommonLayoutUtility.addTab(mainTabs, TAB_WEB, researchPane);
        return mainTabs;
    }

    /// %Part 3: Setup Properties

    public void postLoad(Scene scene){
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

    /// %Part 4.1: writingText (WritingText)

    public ReadOnlyObjectProperty<WritingText> writingTextProperty(){
        return writingText.getReadOnlyProperty();
    }

    public WritingText getWritingText(){
        return writingText.getValue();
    }

    /// %Part 4.2: writingStat (WritingStat)

    public ReadOnlyObjectProperty<WritingStat> writingStatProperty(){
        return writingStat.getReadOnlyProperty();
    }

    public WritingStat getWritingStat(){
        return writingStat.getValue();
    }

    /// %Part 4.3: writingData (WritingData)

    public ReadOnlyObjectProperty<WritingData> writingDataProperty(){
        return writingData.getReadOnlyProperty();
    }

    public WritingData getWritingData(){
        return writingData.getValue();
    }

    /// %Part 4.4: refocusText (Boolean)

    public BooleanProperty refocusTextProperty(){
        return refocusText;
    }

    public boolean isRefocusText(){
        return refocusText.getValue();
    }

    public void setRefocusText(boolean value){
        refocusText.setValue(value);
    }

    /// %Part 4.3: lastSelected (SpanBranch)

    public ObjectProperty<SpanBranch> lastSelectedProperty(){
        return lastSelected;
    }

    public SpanBranch getLastSelected(){
        return lastSelected.getValue();
    }

    public void setLastSelected(SpanBranch value){
        lastSelected.setValue(value);
    }

    /// %Part 5: Get Child Methods

    MenuBarMainControl getMainMenuBar(){
        return mainMenuBar;
    }

    CheatsheetPaneControl getCheatsheetPane(){
        return cheatsheetPane;
    }

    NoteCardPaneControl getNoteCardPane(){
        return noteCardPane;
    }

    DataPaneAgenda getAgendaPane(){
        return agendaPane;
    }

    DataPaneLink getLinkPane(){
        return linkPane;
    }

    DataPaneNote getFootnotePane(){
        return footnotePane;
    }

    DataPaneNote getEndnotePane(){
        return endnotePane;
    }

    HeadingsPaneControl getHeadingPane(){
        return headingPane;
    }

    MetaDataPaneControl getMetaDataPane(){
        return metaDataPane;
    }

    TabPane getMainTabPane(){
        return mainTabs;
    }

    TextPaneControl getTextPane(){
        return textPane;
    }

    ResearchPaneControl getResearchPane(){
        return researchPane;
    }
}
