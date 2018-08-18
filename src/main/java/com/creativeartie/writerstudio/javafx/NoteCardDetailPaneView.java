package com.creativeartie.writerstudio.javafx;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.writerstudio.lang.markup.*;

abstract class NoteCardDetailPaneView extends TabPane{
    /// %Part 1: Constructor and Class Fields

    private Tab emptyTab;

    private Tab mainTab;
    private InlineCssTextArea noteTitle;
    private InlineCssTextArea noteContent;

    private Tab metaTab;
    private TableView<NoteCardData> noteMetaTable;
    private ComboBox<String> showType;
    private Button addField;
    private Button deleteField;
    private Button removeUnused;

    private SimpleObjectProperty<NoteCardSpan> showCard;

    NoteCardDetailPaneView(){
        showCard = new SimpleObjectProperty<>(this, "showCard");
        setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        emptyTab = buildEmptyTab();
        mainTab = buildMainTab();
        metaTab = buildMetaTab();
    }

    /// %Part 2: Layout
    private Tab buildEmptyTab(){
        Label text = new Label("Select a note to view");
        BorderPane pane = new BorderPane();
        pane.setCenter(text);
        return new Tab("None Selected", pane);
    }

    private Tab buildMainTab(){
        BorderPane pane = new BorderPane();
        noteTitle = new InlineCssTextArea();
        noteContent = new InlineCssTextArea();
        pane.setTop(noteTitle);
        pane.setCenter(noteContent);
        return new Tab("Content", pane);
    }

    private Tab buildMetaTab(){
        BorderPane pane = new BorderPane();
        pane.setTop(buildNoteControls());
        pane.setCenter(buildMetaTable());
        return new Tab("Meta Data", pane);
    }

    private ToolBar buildNoteControls(){
        showType = new ComboBox<>();
        showType.getItems().addAll("Show Usable", "Show exta", "Show All");
        return new ToolBar(showType);
    }

    private TableView<NoteCardData> buildMetaTable(){
        noteMetaTable = new TableView<>();
        return noteMetaTable;
    }


    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 4.1: Show Note

    public ObjectProperty<NoteCardSpan> showCardProperty(){
        return showCard;
    }

    public NoteCardSpan getShowCard(){
        return showCard.getValue();
    }

    public void setShowCard(NoteCardSpan value){
        showCard.setValue(value);
    }

    /// %Part 5: Get Child Methods

    Tab getEmptyTab(){
        return emptyTab;
    }
}
