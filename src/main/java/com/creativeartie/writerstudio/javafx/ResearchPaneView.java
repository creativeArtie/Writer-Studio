package com.creativeartie.writerstudio.javafx;

import java.time.*;

import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;

abstract class ResearchPaneView extends BorderPane{

    private SplitMenuButton backButton;
    private SplitMenuButton forwardButton;
    private TextField addressBarField;
    private TextField searchBarField;
    private Label titleLabel;
    private Label timeoutLabel;
    private Label clockLabel;
    private WebEngine webEngine;

    private ReadOnlyObjectWrapper<Duration> exitTimer;

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
		backButton.setText("Back");
		pane.add(backButton, 0, 0);
		
		forwardButton = new SplitMenuButton();
		forwardButton.setText("Forward");
		pane.add(forwardButton, 1, 0);
		
		
		addressBarField = new TextField();
		pane.add(addressBarField, 2, 0);
		
		searchBarField = new TextField();
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
        webEngine.load("http://google.com");
        return web;
    }
    
    private GridPane buildBottomPane(){
		GridPane pane = new GridPane();
		ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        pane.getColumnConstraints().addAll(col1, col2);
        
		timeoutLabel = new Label();
		FlowPane timePane = new FlowPane(timeoutLabel);
		pane.setAlignment(Pos.TOP_LEFT);
		pane.add(timePane, 0, 0);
		
		clockLabel = new Label();
		FlowPane clockPane = new FlowPane(clockLabel);
		pane.setAlignment(Pos.TOP_RIGHT);
		pane.add(clockPane, 1, 0);
		
		return pane;
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
	
	TextField getAddressBarField(){
		return addressBarField;
	}
	
	Label getTimeOutLabel(){
		return timeoutLabel;
	}
	
	Label getClockLabel(){
		return clockLabel;
	}
}
