package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * Stores a list of user notes, hyperlinks.
 */
public abstract class NoteCardPaneView extends GridPane{
    /// %Part 1: Constructor and Class Fields
    private TreeView<String> idTree;
    private TreeView<String> locationTree;
    private Button insertAfterButton;
    private Button insertBeforeButton;
    private Button deleteItemButton;

    private NoteCardDetailPaneControl contentPane;
    private TableView<NoteCardData> metaInfoTable;

    public NoteCardPaneView(){

        double width = 100.0 / 3;
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(width);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(width);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(width);
        getColumnConstraints().addAll(column1, column2, column3);


        add(buildPointerPane(), 0, 0);
        add(buildContentPane(), 1, 0);
        add(buildInfoPane(), 2, 0);
    }

    private BorderPane buildPointerPane(){
        BorderPane pane = new BorderPane();
        pane.setCenter(buildLocationTabs());
        pane.setBottom(buildEditButtons());
        setFillHeight(pane, true);
        return pane;
    }

    private TabPane buildLocationTabs(){
        TabPane tab = new TabPane(buildIdViewPane(), buildLocationViewPane());
        tab.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tab;
    }

    private Tab buildIdViewPane(){
        idTree = new TreeView<>();
        idTree.setShowRoot(false);
        return new Tab("Id", idTree);
    }

    private Tab buildLocationViewPane(){
        locationTree = new TreeView<>();
        locationTree.setShowRoot(false);
        return new Tab("Location", locationTree);
    }

    private FlowPane buildEditButtons(){
        insertAfterButton = new Button("Add after");
        insertBeforeButton = new Button("Add before");
        deleteItemButton = new Button("delete");
        FlowPane pane = new FlowPane(insertBeforeButton, insertAfterButton,
            deleteItemButton);
        return pane;
    }

    private NoteCardDetailPaneControl buildContentPane(){
        contentPane = new NoteCardDetailPaneControl();
        setFillHeight(contentPane, false);
        return contentPane;
    }

    private TableView<NoteCardData> buildInfoPane(){
        metaInfoTable = new TableView<>();
        setFillHeight(metaInfoTable, true);
        return metaInfoTable;
    }

    /// %Part 2: Layout

    /// %Part 3: Listener Methods

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods
    NoteCardDetailPaneControl getContentPane(){
        return contentPane;
    }

    TreeView<String> getIdTree(){
        return idTree;
    }

    TreeView<String> getLocationTree(){
        return locationTree;
    }
}
