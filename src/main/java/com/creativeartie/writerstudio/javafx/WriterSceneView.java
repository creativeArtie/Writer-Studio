package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import javafx.scene.control.*;
import org.fxmisc.richtext.model.*;
import javafx.animation.*;
import javafx.geometry.*;

import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

import com.google.common.collect.*;

abstract class WriterSceneView extends BorderPane{
    /// %Part 1: Constructor and Class Fields

    private static final double[] VER_DIVIDER = new double[]{.2, .8};
    private static final double[] HOR_DIVIDER = new double[]{.0, 1.0};

    private MenuBarMainControl mainMenuBar;
    private CheatsheetPaneControl cheatsheetPane;
    private TextPaneControl textPane;
    private MetaDataPaneControl metaDataPane;

    private ReadOnlyObjectWrapper<WritingText> writingText;
    private ReadOnlyObjectWrapper<WritingStat> writingStat;
    private ReadOnlyObjectWrapper<WritingData> writingData;
    private SimpleObjectProperty<SpanBranch> lastSelected;

    WriterSceneView(Stage window){
        getStylesheets().add(FileResources.getMainCss());
        setTop(buildTop(window));
        setCenter(buildSplitMain());

        writingText = new ReadOnlyObjectWrapper<>(this, "writingText");
        writingStat = new ReadOnlyObjectWrapper<>(this, "writingStat");
        writingData = new ReadOnlyObjectWrapper<>(this, "writingData");
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
        Tab ref = buildTab(WindowText.TAB_NOTE_CARD, new ReferencePane());
        top.getTabs().addAll(ref);
        return top;
    }

    private BorderPane buildContent(){
        BorderPane content = new BorderPane();
        content.setCenter(buildSplitCenter());

        cheatsheetPane = new CheatsheetPaneControl();
        content.setBottom(cheatsheetPane);
        return content;
    }

    private SplitPane buildSplitCenter(){
        textPane = new TextPaneControl();
        SplitPane center = new SplitPane(buildLeftTabs(), textPane);
        center.setDividerPositions(HOR_DIVIDER);
        return center;
    }

    private TabPane buildLeftTabs(){
        metaDataPane = new MetaDataPaneControl();

        TabPane left = buildTabPane();
        Tab meta = buildTab(WindowText.TAB_META, metaDataPane);
        left.getTabs().addAll(meta);
        return left;
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

    public void setupProperties(){
        bindWritingText(writingText);
        bindWritingStat(writingStat);
        bindWritingData(writingData);
        bindChildren();
    }


    protected abstract void bindWritingText(
        ReadOnlyObjectWrapper<WritingText> text);

    protected abstract void bindWritingStat(
        ReadOnlyObjectWrapper<WritingStat> stat);

    protected abstract void bindWritingData(
        ReadOnlyObjectWrapper<WritingData> data);

    protected abstract void bindChildren();

    /// %Part 4: Properties

    public ObjectProperty<WritingFile> writingFileProperty(){
        return mainMenuBar.writingFileProperty();
    }

    public WritingFile getWritingFile(){
        return mainMenuBar.getWritingFile();
    }

    public void setWritingFile(WritingFile value){
        mainMenuBar.setWritingFile(value);
    }
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

    /// %Part 4.3: CaretPosition

    public ReadOnlyIntegerProperty caretPositionProperty(){
        return textPane.caretPositionProperty();
    }

    public int getCaretPosition(){
        return textPane.getCaretPosition();
    }

    /// %Part 4.4: LastSelected

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

    MetaDataPaneControl getMetaDataPane(){
        return metaDataPane;
    }

    TextPaneControl getTextPane(){
        return textPane;
    }
}
