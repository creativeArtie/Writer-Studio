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
import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    ResearchConstants.*;

final class ResearchPaneControl extends ResearchPaneView {

    /// %Part 1: Private Fields and Constructor

    private WebHistory webHistory;
    private Optional<LocalDateTime> endTime;
    private BooleanProperty refocusText;
    private BooleanProperty disableTab;
    private TabPane mainTabs;

    ResearchPaneControl(){
        endTime = Optional.empty();
    }

    /// %Part 2: Property Binding
    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(WriterSceneControl control){
        mainTabs = control.getMainTabPane();
        disableTab = mainTabs.getTabs().get(1).disableProperty();
        refocusText = control.refocusTextProperty();

        control.writingTextProperty().addListener(
            (d, o, n) -> listenWritingText(n)
        );
        mainTabs.getSelectionModel().selectedItemProperty().addListener(
            (d, o, n) -> listenTabSelected(n.getContent().isVisible())
        );

        new AnimationTimer(){
            @Override
            public void handle(long now) {listenTimer(now); }
        }.start();

        getWebEngine().titleProperty().addListener((d, o, n) ->
            getTitleLabel().setText(n));
        getWebEngine().locationProperty().addListener((d, o, n) ->
            getAddressBarField().setText(n));

        webHistory = getWebEngine().getHistory();
        webHistory.currentIndexProperty().addListener((d, o, n) ->
            listenHistory());
        webHistory.getEntries().addListener(
            (ListChangeListener<WebHistory.Entry>)e -> listenHistory()
        );

        getBackButton().setOnAction(e -> listenButtonClick(-1));
        getForwardButton().setOnAction(e -> listenButtonClick(1));

        getAddressBarField().setOnAction(evt ->
            listenAddress(getAddressBarField().getText())
        );

        getSearchBarField().setOnAction(e -> searchItem());

        getWebEngine().load(HOME_PAGE);
        listenHistory();
    }

    /// %Part 3.1: control.writingTextProperty()

    private void listenWritingText(WritingText text){
        if (text != null){
            text.addDocEdited(s -> updateTimer());
        }
        updateTimer();
    }

    private void updateTimer(){
        endTime = Optional.empty();
        if (disableTab.getValue()){
            disableTab.setValue(false);
        } else {
            listenTabSelected(true);
        }
    }

    /// %Part 3.2: mainTabs.getSelectionModel().selectedItemProperty()

    private void listenTabSelected(boolean visible){
        if (! visible) return;
        if (endTime.isPresent()){
            return;
        }
        endTime = Optional.of(LocalDateTime.now().plus(TIME_LIMITS));

    }

    /// %Part 3.3:  new AnimationTimer(){...}.start()

    private void listenTimer(long timer){
        getClockLabel().setText(
            DateTimeFormatter.ofPattern(CLOCK_FORMAT).format(LocalTime.now()
        ));
        if (endTime.isPresent()){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime ends = endTime.get();
            if (now.isAfter(ends)){
                refocusText.setValue(true);
                disableTab.setValue(true);
                endTime = Optional.empty();
                getWebEngine().load(HOME_PAGE);
                return;
            }
            float time = Duration.between(now, ends).toMillis() / 1000f;
            String text = time > 60?
                String.format(TIMEOUT_MINS, time / 60f):
                String.format(TIMEOUT_SECS, time);
            getTimeOutLabel().setText(text);
        }
    }

    /// %Part 3.4 webHistory.currentIndexProperty() && webHistory.getEntries()

    private void listenHistory(){
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
        item.setOnAction(
            e -> listenButtonClick(position - webHistory.getCurrentIndex())
        );
        return Optional.of(item);
    }

    /// %Part 3.5: getBackButton().setOnAction(...) &&
    /// %          getForwardButton().setOnAction(...) &&
    /// %          MenuItem.setOnAction(...)

    private void listenButtonClick(int offset){
        webHistory.go(offset);
    }

    /// %Part 3.6: getAddressBarField().setOnAction(...)

    private void listenAddress(String text){
        if (! text.startsWith(HTTP_TEST)){
            text = HTTP_START + text;
        }
        getWebEngine().load(text);
    }

    /// %Part 3.6: getSearchBarField().setOnAction(...)

    private void searchItem(){
        String text = getSearchBarField().getText().replace(' ', SEARCH_REPLACE);
        getWebEngine().load(SEARCH_START + text);
    }
}
