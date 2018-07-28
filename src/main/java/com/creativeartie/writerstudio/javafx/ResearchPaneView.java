package com.creativeartie.writerstudio.javafx;

import java.time.*;

import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;

abstract class ResearchPaneView extends BorderPane{

    private SplitMenuButton backButton;
    private SplitMenuButton forwardButton;
    private TextField addressBarField;
    private TextField searchBarField;
    private Label webpageTitle;
    private Label exitTimerLabel;
    private Label clockLabel;
    private WebEngine webEngine;

    private ReadOnlyObjectWrapper<Duration> exitTimer;

    ResearchPaneView(){
        setCenter(buildBrowser());
    }

    /// %Part 2: Layout

    private WebView buildBrowser(){
        WebView web = new WebView();
        webEngine = web.getEngine();
        webEngine.load("http://www.google.com");
        return web;
    }
}
