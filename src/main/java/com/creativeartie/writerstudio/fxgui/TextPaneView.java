package com.creativeartie.writerstudio.fxgui;

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
    /// %Part 1: Constructor and Class Fields
    private InlineCssTextArea textArea;
    private Label lineTypeLabel;
    private Label currentStats;
    private Label currentTime;

    private final SimpleObjectProperty<WritingText> writingText;
    private final SimpleObjectProperty<WritingStat> writingStat;
    private final SimpleBooleanProperty textReady;
    private final SimpleObjectProperty<SpanBranch> lastSelected;

    private final ReadOnlyObjectWrapper<PlainTextChange> textChanged;
    private final ReadOnlyIntegerWrapper caretPosition;
    private final ReadOnlyBooleanWrapper editorFocused;

    TextPaneView(){
        setCenter(buildTextArea());
        setBottom(buildStatusText());

        writingText = new SimpleObjectProperty<>(this, "writingText");
        writingStat = new SimpleObjectProperty<>(this, "writingStat");
        textReady = new SimpleBooleanProperty(this, "textReady");
        lastSelected = new SimpleObjectProperty<>(this, "lastSelected");

        textChanged = new ReadOnlyObjectWrapper<>(this, "textChanged");
        caretPosition = new ReadOnlyIntegerWrapper(this, "caretPosition");
        editorFocused = new ReadOnlyBooleanWrapper(this, "editorFocused");

        textArea.plainTextChanges().subscribe(v -> textChanged.setValue(v));
        caretPosition.bind(textArea.caretPositionProperty());
        editorFocused.bind(textArea.focusedProperty());


        addListeners();
    }

    /// %Part 2: Layout

    /// %Part 2.1: Main Text Area
    private InlineCssTextArea buildTextArea(){
        textArea = new InlineCssTextArea();
        textArea.setWrapText(true);
        return textArea;
    }

    /// %Part 2.2: Bottom Pane

    private GridPane buildStatusText(){
        GridPane parent = new GridPane();
        parent.add(buildLineTypeLabel(), 0, 0);
        setPrecentWidth(parent, 33.33333333);
        parent.add(buildStatsLabel(), 1, 0);
        setPrecentWidth(parent, 33.33333333);
        parent.add(buildTimeLabel(), 2, 0);
        setPrecentWidth(parent, 33.33333333);

        return parent;
    }

    private HBox buildLineTypeLabel(){
        lineTypeLabel = new Label();

        HBox align = new HBox();
        align.setAlignment(Pos.BASELINE_LEFT);
        align.getChildren().add(lineTypeLabel);

        return align;
    }

    private HBox buildStatsLabel(){
        currentStats = new Label();

        HBox align = new HBox();
        align.setAlignment(Pos.BASELINE_CENTER);
        align.getChildren().add(currentStats);

        return align;
    }

    private HBox buildTimeLabel(){
        currentTime = new Label();

        HBox align = new HBox();
        align.setAlignment(Pos.BASELINE_RIGHT);
        align.getChildren().add(currentTime);

        return align;
    }

    /**
     * Set the next column by percent width.
     * @param value
     *      percent width
     * @see buildLineTypeLabel(GridPane)
     * @see buildStatsLabel(GridPane)
     * @see buildTimeLabel(GridPane)
     */
    private static void setPrecentWidth(GridPane pane, double value){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(value);
        pane.getColumnConstraints().add(column);
    }

    /// %Part 3: Abstract Methods
    protected abstract void addListeners();

    /// %Part 4: Properties

    SimpleObjectProperty<WritingText> writingTextProperty(){
        return writingText;
    }

    public WritingText getWritingText(){
        return writingText.getValue();
    }

    public void setWritingText(WritingText value){
        writingText.setValue(value);
    }

    public BooleanProperty textReadyProperty(){
        return textReady;
    }

    public void setTextReady(boolean value){
        textReady.setValue(value);
    }

    public boolean isTextReady(){
        return textReady.getValue();
    }

    public SimpleObjectProperty<SpanBranch> lastSelectedProperty(){
        return lastSelected;
    }

    public SpanBranch getLastSelected(){
        return lastSelected.getValue();
    }

    public void setLastSelected(SpanBranch span){
        lastSelected.setValue(span);
    }


    public ReadOnlyObjectProperty<PlainTextChange> textChangedProperty(){
        return textChanged.getReadOnlyProperty();
    }

    public PlainTextChange getTextChanged(){
        return textChanged.getValue();
    }

    public ReadOnlyIntegerProperty caretPositionProperty(){
        return caretPosition.getReadOnlyProperty();
    }

    public int getCaretPosition(){
        return caretPosition.getValue();
    }

    public ReadOnlyBooleanProperty editorFocusedProperty(){
        return editorFocused.getReadOnlyProperty();
    }

    public boolean isEditorFocused(){
        return editorFocused.getValue();
    }

    public SimpleObjectProperty<WritingStat> writingStatProperty(){
        return writingStat;
    }

    public WritingStat getWritingStat(){
        return writingStat.getValue();
    }

    public void setWritingStat(WritingStat value){
        writingStat.setValue(value);
    }

    /// %Part 5: Get Child Methods

    InlineCssTextArea getTextArea(){
        return textArea;
    }

    protected Label getLineTypeLabel(){
        return lineTypeLabel;
    }

    protected Label getStatsLabel(){
        return currentStats;
    }

    protected Label getTimeLabel(){
        return currentTime;
    }

}
