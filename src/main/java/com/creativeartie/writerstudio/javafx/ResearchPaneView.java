package com.creativeartie.writerstudio.javafx;

import java.time.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;

import com.creativeartie.writerstudio.resource.*;


abstract class ResearchPaneView extends BorderPane{

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

    private VBox buildTopPane(){
        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_CENTER);

        pane.getChildren().addAll(build1stRow(), build2ndRow());

        return pane;
    }

    private GridPane build1stRow(){
        GridPane pane = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(10);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(10);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(60);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(20);
        pane.getColumnConstraints().addAll(col1, col2, col3, col4);

        backButton = new SplitMenuButton();
        backButton.setText(WindowText.RESEARCH_BACK.getText());
        pane.add(backButton, 0, 0);

        forwardButton = new SplitMenuButton();
        forwardButton.setText(WindowText.RESEARCH_FORWARD.getText());
        pane.add(forwardButton, 1, 0);


        addressBarField = new TextField();
        addressBarField.setPromptText(WindowText.RESEARCH_URL.getText());
        pane.add(addressBarField, 2, 0);

        searchBarField = new TextField();
        searchBarField.setPromptText(WindowText.RESEARCH_SEARCH.getText());
        pane.add(searchBarField, 3, 0);


        return pane;
    }

    private Label build2ndRow(){
        titleLabel = new Label();
        titleLabel.getStyleClass().add("web-address");
        return titleLabel;
    }

    private WebView buildBrowser(){
        WebView web = new WebView();
        webEngine = web.getEngine();
        return web;
    }

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

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);


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
