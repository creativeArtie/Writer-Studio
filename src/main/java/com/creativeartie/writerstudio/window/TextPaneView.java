package com.creativeartie.writerstudio.window;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.beans.property.*;
import javafx.animation.*;
import javafx.geometry.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * A rict text fx text area that show the text
 */
abstract class TextPaneView extends BorderPane {

    private final InlineCssTextArea textArea;
    private final Label lineTypeLabel;
    private final Label currentStats;
    private final Label currentTime;

    private static final long SECOND = 1000000l;
    private long markedTime;

    /** Property binded with textArea.plainTextChanges. */
    private final ReadOnlyObjectWrapper<PlainTextChange> textChanged;
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
        GridPane pane = new GridPane();
        lineTypeLabel = initLineTypeLabel(pane);
        currentStats = initStatsLabel(pane);
        currentTime = initTimeLabel(pane);
        setBottom(pane);

        textChanged = new ReadOnlyObjectWrapper<>(this, "textChanged");
        textArea.plainTextChanges().subscribe(value -> {
            if(isReady()){ textChanged.setValue(value);}
        });

        caretPlaced = new ReadOnlyIntegerWrapper(this, "caretPlaced");
        textArea.caretPositionProperty().addListener((data, oldValue, newValue)
            -> updatePosition(caretPlaced));

        textProperty = new ReadOnlyObjectWrapper<>(this, "text");
        textProperty.bind(textArea.textProperty());

        editorFocused = new ReadOnlyBooleanWrapper(this, "editorFocused");
        editorFocused.bind(textArea.focusedProperty());

        readyProperty = new SimpleBooleanProperty(this, "ready", false);
        readyProperty.addListener((data, oldValue, newValue) ->
            updatePosition(caretPlaced));
    }

    /// Layout Node
    private InlineCssTextArea initTextArea(){
        InlineCssTextArea area = new InlineCssTextArea();
        area.setWrapText(true);
        setCenter(area);
        return area;
    }

    private Label initLineTypeLabel(GridPane parent){
        Label ans = new Label();

        HBox align = new HBox();
        align.setAlignment(Pos.BASELINE_LEFT);
        align.getChildren().add(ans);

        parent.add(align, 0, 0);
        setPrecentWidth(parent, 33.33333333);
        return ans;
    }

    private Label initStatsLabel(GridPane parent){
        Label ans = new Label();

        HBox align = new HBox();
        align.setAlignment(Pos.BASELINE_CENTER);
        align.getChildren().add(ans);

        parent.add(align, 1, 0);
        setPrecentWidth(parent, 33.33333333);
        return ans;
    }

    private Label initTimeLabel(GridPane parent){
        Label ans = new Label();

        HBox align = new HBox();
        align.setAlignment(Pos.BASELINE_RIGHT);
        align.getChildren().add(ans);

        parent.add(align, 2, 0);
        setPrecentWidth(parent, 33.33333333);

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

    /**
     * Set the next column by percent width.
     * @param value
     *      percent width
     * @see initLineTypeLabel(GridPane)
     * @see initStatsLabel(GridPane)
     * @see initTimeLabel(GridPane)
     */
    private void setPrecentWidth(GridPane pane, double value){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(value);
        pane.getColumnConstraints().add(column);
    }

    /// Getters
    protected InlineCssTextArea getTextArea(){
        return textArea;
    }

    protected Label getLineTypeLabel(){
        return lineTypeLabel;
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

    abstract void updateTime(Label show);

    abstract void updatePosition(ReadOnlyIntegerWrapper caret);
}