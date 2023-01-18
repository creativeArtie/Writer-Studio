package com.creativeartie.writer.javafx;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;

import com.creativeartie.writer.javafx.utils.*;
import static com.creativeartie.writer.javafx.utils.LayoutConstants
    .ResearchConstants.*;

abstract class ResearchPaneView extends BorderPane{

    /// %Part 1: Constructor and Class Fields

    private SplitMenuButton backButton;
    private SplitMenuButton forwardButton;
    private TextField addressBarField;
    private TextField searchBarField;
    private Label titleLabel;
    private Label timeoutLabel;
    private Label clockLabel;
    private WebEngine webEngine;

    ResearchPaneView(){
        setTop(buildTopPane());
        setCenter(buildBrowser());
        setBottom(buildBottomPane());
    }

    /// %Part 2: Layout

    /// %Part 2 (content -> top)

    private VBox buildTopPane(){
        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_CENTER);

        pane.getChildren().addAll(build1stRow(), build2ndRow());

        return pane;
    }

    /// %Part 2 (content -> top -> 1st row)

    private GridPane build1stRow(){
        GridPane pane = new GridPane();

        backButton = new SplitMenuButton();
        CommonLayoutUtility.setWidthPrecent(pane, BACK_MENU_WIDTH);
        backButton.setText(BACK_MENU_TEXT);

        pane.add(backButton, 0, 0);

        forwardButton = new SplitMenuButton();
        forwardButton.setText(FORWARD_MENU_TEXT);
        CommonLayoutUtility.setWidthPrecent(pane, FORWARD_MENU_WIDTH);
        pane.add(forwardButton, 1, 0);


        addressBarField = new TextField();
        addressBarField.setPromptText(ADDRESS_BAR_TEXT);
        CommonLayoutUtility.setWidthPrecent(pane, ADDRESS_BAR_WIDTH);
        pane.add(addressBarField, 2, 0);

        searchBarField = new TextField();
        searchBarField.setPromptText(SEARCH_BAR_TEXT);
        CommonLayoutUtility.setWidthPrecent(pane, SEARCH_BAR_WIDTH);
        pane.add(searchBarField, 3, 0);

        return pane;
    }

    /// %Part 2 (content -> top -> 2nd row)

    private Label build2ndRow(){
        titleLabel = new Label();
        titleLabel.getStyleClass().add("web-address");
        return titleLabel;
    }

    /// %Part 2 (content -> center)

    private WebView buildBrowser(){
        WebView web = new WebView();
        webEngine = web.getEngine();
        return web;
    }

    /// %Part 2 (content -> bottom)

    private HBox buildBottomPane(){
        HBox root = new HBox();
        timeoutLabel = new Label();

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        clockLabel = new Label();

        root.getChildren().addAll(timeoutLabel, spacer, clockLabel);
        return root;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindChildren(control);
    }

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods

    WebEngine getWebEngine(){
        return webEngine;
    }

    Label getTitleLabel(){
        return titleLabel;
    }

    SplitMenuButton getBackButton(){
        return backButton;
    }

    SplitMenuButton getForwardButton(){
        return forwardButton;
    }

    TextField getAddressBarField(){
        return addressBarField;
    }

    TextField getSearchBarField(){
        return searchBarField;
    }

    Label getTimeOutLabel(){
        return timeoutLabel;
    }

    Label getClockLabel(){
        return clockLabel;
    }
}
