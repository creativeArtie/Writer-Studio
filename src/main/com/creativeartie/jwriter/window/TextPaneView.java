package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.beans.property.*;
import javafx.animation.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.window.*;

/**
 * A rict text fx text area that show the text
 */
abstract class TextPaneView extends BorderPane {

    private final InlineCssTextArea textArea;
    private final Button viewMode;
    private final Label currentStats;
    private final Label currentTime;

    private static final long SECOND = 1000000l;
    private long markedTime;

    /** Property binded with textArea.plainTextChanges. */
    private final ReadOnlyObjectWrapper<PlainTextChange> textChanged;
    /** Property edided by modeUsed.onAction */
    private final ReadOnlyObjectWrapper<WindowText> modeUsed;
    /** Property edided by textArea.getText()*/
    private final ReadOnlyObjectWrapper<String> textProperty;
    /** Property binded with textArea.caretPosition. */
    private final ReadOnlyIntegerWrapper caretPlaced;
    /** Property binded with textArea.focused. */
    private final ReadOnlyBooleanWrapper editorFocused;
    /** Property binded with textArea.focused. */
    private final SimpleBooleanProperty readyProperty;

    TextPaneView(){
        textArea = initTextArea();
        BorderPane pane = new BorderPane();
        viewMode = initViewMode(pane);
        currentStats = initStatsLabel(pane);
        currentTime = initTimeLabel(pane);
        setBottom(pane);

        textChanged = new ReadOnlyObjectWrapper<>(this, "textChanged");
        textArea.plainTextChanges().subscribe(value -> {
            if(isReady()){ textChanged.setValue(value);}
        });

        caretPlaced = new ReadOnlyIntegerWrapper(this, "caretPlaced");

        textProperty = new ReadOnlyObjectWrapper<>(this, "text");
        textProperty.bind(textArea.textProperty());

        editorFocused = new ReadOnlyBooleanWrapper(this, "editorFocused");
        editorFocused.bind(textArea.focusedProperty());

        modeUsed = new ReadOnlyObjectWrapper<>(this, "modeUsed");
        modeUsed.setValue(setNextMode(null));

        readyProperty = new SimpleBooleanProperty(this, "ready", false);
        readyProperty.addListener((data, oldValue, newValue) ->{
            if (newValue) {caretPlaced.bind(textArea.caretPositionProperty());}
            else {caretPlaced.unbind();}
        });
        viewMode.setOnAction(evt -> modeUsed
            .setValue(setNextMode(modeUsed.getValue())));
    }

    /// Layout Node
    private InlineCssTextArea initTextArea(){
        InlineCssTextArea area = new InlineCssTextArea();
        area.setWrapText(true);
        setCenter(area);
        return area;
    }

    private Button initViewMode(BorderPane parent){
        Button ans = new Button();
        ans.setDisable(true);
        parent.setLeft(ans);
        return ans;
    }

    private Label initStatsLabel(BorderPane parent){
        Label ans = new Label();
        parent.setCenter(ans);
        return ans;
    }

    private Label initTimeLabel(BorderPane parent){
        Label ans = new Label();
        parent.setRight(ans);
        markedTime = -1;
        new AnimationTimer(){
            @Override
            public void handle(long now) {
                if (markedTime == -1){
                    markedTime = now + SECOND;
                    updateTime(currentTime);
                } else if (markedTime < now){
                    markedTime = now + SECOND;
                    updateTime(currentTime);
                }
            }
        }.start();
        return ans;
    }

    /// Getters
    protected InlineCssTextArea getTextArea(){
        return textArea;
    }

    protected Button getViewModeButton(){
        return viewMode;
    }

    protected Label getCurrentStatsLabel(){
        return currentStats;
    }

    protected Label getCurrentTimeLabel(){
        return currentTime;
    }

    /// Node Properties
    public ReadOnlyObjectProperty<PlainTextChange> textChangedProperty(){
        return textChanged.getReadOnlyProperty();
    }

    public PlainTextChange getTextChanged(){
        return textChanged.getValue();
    }

    public ReadOnlyIntegerProperty caretPlacedProperty(){
        return caretPlaced.getReadOnlyProperty();
    }

    public int getCaretPlaced(){
        return caretPlaced.getValue();
    }

    public ReadOnlyBooleanProperty editorFocusedProperty(){
        return editorFocused.getReadOnlyProperty();
    }

    public boolean isEditorFocused(){
        return editorFocused.getValue();
    }

    public ReadOnlyObjectProperty<WindowText> modeUsedProperty(){
        return modeUsed.getReadOnlyProperty();
    }

    public WindowText getModeUsed(){
        return modeUsed.getValue();
    }

    public ReadOnlyObjectProperty<String> textProperty(){
        return textProperty.getReadOnlyProperty();
    }

    public String getText(){
        return textProperty.getValue();
    }

    public BooleanProperty readyProperty(){
        return readyProperty;
    }

    public boolean isReady(){
        return readyProperty.getValue();
    }

    public void setReady(boolean b){
        readyProperty.setValue(b);
    }

    /// Control Methods
    public abstract WindowText setNextMode(WindowText lastMode);

    abstract void updateTime(Label show);
}