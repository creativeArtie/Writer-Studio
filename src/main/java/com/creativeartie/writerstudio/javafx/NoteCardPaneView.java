package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    NoteCardConstants.*;

/**
 * A pane with two {@link HeadingTreeControl} objects that
 */
abstract class NoteCardPaneView extends GridPane{

    private class IdCardCell extends TreeCell<String>{
        @Override
        public void updateItem(String item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                System.out.println(getTreeItem().getClass());
                /// Allows TextFlowBuilder to create the Label
                setText(item);
            }
        }
    }

    private class NoteCardCell extends ListCell<NoteCardSpan>{
        @Override
        public void updateItem(NoteCardSpan item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                /// Allows TextFlowBuilder to create the Label
                TextFlow graphic = new TextFlow();
                Text name = new Text(item.getSpanIdentity()
                    .map(i -> i.getName())
                    .orElse("")
                );
                name.getStyleClass().add(LIST_ID_STYLE);
                graphic.getChildren().add(name);
                TextFlowBuilder.loadFormatText(graphic, item.getTitle());
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    private TreeView<String> idTree;
    private TreeView<SectionSpanHead> locationTree;

    private Button insertBeforeButton;
    private Button insertAfterButton;
    private Button deleteButton;
    private ListView<NoteCardSpan> noteCardsList;

    private NoteCardDetailPaneControl noteDetailPane;

    /// %Part 1: Constructor and Class Fields
    public NoteCardPaneView(){
        double width = 100.0 / 3;
        ColumnConstraints columns[] = new ColumnConstraints[3];
        for (int i = 0; i < columns.length; i++){
            columns[i] = new ColumnConstraints();
            columns[i].setPercentWidth(width);
        }
        getColumnConstraints().addAll(columns);
        add(buildLocationPane(), 0, 0);
        add(buildNoteListPane(), 1, 0);
        add(buildDetailPane(), 2, 0);
    }

    /// %Part 2: Layout
    private TabPane buildLocationPane(){
        idTree = new TreeView<>();
        idTree.setShowRoot(false);
        idTree.setCellFactory(p -> {
            TreeCell<String> ans = new IdCardCell();
            ans.setOnMouseClicked(e -> handleListSelected(e, ans.getTreeItem()));
            return ans;
        });
        Tab id = new Tab(TAB_ID, idTree);

        locationTree = new TreeView<>();
        locationTree.setShowRoot(false);
        locationTree.setCellFactory(p -> {
            TreeCellHeading<SectionSpanHead> ans = new TreeCellHeading<>();
            ans.setOnMouseClicked(e -> handleListSelected(e, ans.getTreeItem()));
            return ans;
        });
        Tab location = new Tab(TAB_LOCATION, locationTree);

        TabPane pane = new TabPane(id, location);
        pane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return pane;
    }

    private ListView buildNoteListPane(){

        noteCardsList = new ListView<>();
        noteCardsList.setPlaceholder(new Label(EMPTY_LIST));
        noteCardsList.setCellFactory(l ->  {
            NoteCardCell cell = new NoteCardCell();
            cell.setOnMouseClicked(e -> handleNoteSelected(e, cell.getItem()));
            return cell;
        });
        return noteCardsList;
    }

    private NoteCardDetailPaneControl buildDetailPane(){
        noteDetailPane = new NoteCardDetailPaneControl();
        return noteDetailPane;
    }


    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindChildren(control);
    }

    protected abstract void handleListSelected(MouseEvent event,
        TreeItem<?> note);

    protected abstract void handleNoteSelected(MouseEvent event,
        NoteCardSpan note);

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods

    TreeView<String> getIdTree(){
        return idTree;
    }

    TreeView<SectionSpanHead> getLoactionTree(){
        return locationTree;
    }

    ListView<NoteCardSpan> getNoteCardList(){
        return noteCardsList;
    }

    NoteCardDetailPaneControl getNoteDetailPane(){
        return noteDetailPane;
    }
}
