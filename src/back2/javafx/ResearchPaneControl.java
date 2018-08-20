package com.creativeartie.writerstudio.javafx;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.web.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

final class ResearchPaneControl extends ResearchPaneView {

    private WebHistory webHistory;
    private static final Duration LIMITS = Duration.ofMinutes(2);
    private static final String HOME = "https://duckduckgo.com";
    private Optional<LocalDateTime> endTime;
    private BooleanProperty refocusText;
    private BooleanProperty disableTab;
    private TabPane mainTabs;

    ResearchPaneControl(){
        endTime = Optional.empty();
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));
        mainTabs = control.getMainTabPane();
        mainTabs.getSelectionModel().selectedItemProperty().addListener(
            (d, o, n) -> startResearch(n.getContent() == this)
        );
        disableTab = mainTabs.getTabs().get(1).disableProperty();
        refocusText = control.refocusTextProperty();

        new AnimationTimer(){
            @Override
            public void handle(long now) {setTime(now); }
        }.start();

        getWebEngine().titleProperty().addListener((d, o, n) ->
            getTitleLabel().setText(n));
        getWebEngine().locationProperty().addListener((d, o, n) ->
            getAddressBarField().setText(n));

        webHistory = getWebEngine().getHistory();
        webHistory.currentIndexProperty().addListener((d, o, n) ->
            updateHistory());
        webHistory.getEntries().addListener(
            (ListChangeListener<WebHistory.Entry>)e -> updateHistory()
        );

        getBackButton().setOnAction(e -> go(-1));
        getForwardButton().setOnAction(e -> go(1));

        getTimeOutLabel().setText("Hello");

        getAddressBarField().setOnAction(evt ->
            loadUrl(getAddressBarField().getText())
        );

        getSearchBarField().setOnAction(e -> searchItem());

        getWebEngine().load(HOME);
        updateHistory();
    }


    private void loadText(WritingText text){
        if (text != null){
            text.addDocEdited(s -> allowResearch());
        }
        allowResearch();
    }


    private void allowResearch(){
        endTime = Optional.empty();
        disableTab.setValue(false);
    }

    private void startResearch(boolean visible){
        if (! visible) return;
        if (endTime.isPresent()){
            return;
        }
        endTime = Optional.of(LocalDateTime.now().plus(LIMITS));

    }

    private void setTime(long timer){
        getClockLabel().setText(
            DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()
        ));
        if (endTime.isPresent()){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime ends = endTime.get();
            if (now.isAfter(ends)){
                mainTabs.getSelectionModel().selectFirst();
                refocusText.setValue(true);
                disableTab.setValue(true);
                endTime = Optional.empty();
                getWebEngine().load(HOME);
                return;
            }

            float time = Duration.between(now, ends).toMillis() / 1000f;
            String text = time > 60?
                String.format(
                    WindowText.RESEARCH_TIMEOUT_MINS.getText(), time / 60f
                ): String.format(
                    WindowText.RESEARCH_TIMEOUT_SECS.getText(), time
                );
            getTimeOutLabel().setText(text);
        }
    }


    private void go(int offset){
        webHistory.go(offset);
    }

    private void updateHistory(){
        getBackButton().setDisable(true);
        getBackButton().getItems().clear();
        getForwardButton().setDisable(true);
        getForwardButton().getItems().clear();

        if (webHistory.getEntries().isEmpty()){
            return;
        }

        int current = webHistory.getCurrentIndex();
        int ptr = 0;
        for (WebHistory.Entry entry: webHistory.getEntries()){
            if (ptr < current){
                getBackButton().setDisable(false);
                newMenu(ptr, entry).ifPresent(
                    m -> getBackButton().getItems().add(0, m)
                );
            } else if (ptr > current){
                getForwardButton().setDisable(false);
                newMenu(ptr, entry).ifPresent(
                    m -> getForwardButton().getItems().add(m)
                );
            }
            ptr++;
        }
    }

    private Optional<MenuItem> newMenu(int position, WebHistory.Entry entry){
        if(entry.getTitle() == null){
            return Optional.empty();
        }
        MenuItem item = new MenuItem(entry.getTitle());
        item.setOnAction(e -> go(position - webHistory.getCurrentIndex()));
        return Optional.of(item);
    }

    private void loadUrl(String text){
        if (! text.startsWith("http")){
            text = "http://" + text;
        }
        getWebEngine().load(text);
    }

    private void searchItem(){
        String text = getSearchBarField().getText().replace(' ', '+');
        getWebEngine().load("http://duckduckgo.com?q=" + text);
    }
}
