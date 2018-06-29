package com.creativeartie.writerstudio.javafx;

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

    private InlineCssTextArea textArea;
    private Label lineTypeLabel;
    private Label statLabel;
    private Label timeLabel;

    private ReadOnlyBooleanWrapper textReady;
    private ReadOnlyIntegerWrapper caretPosition;

    TextPaneView(){
        setCenter(buildTextArea());
        setBottom(buildBottomPane());

        textReady = new ReadOnlyBooleanWrapper(this, "textReady");
        caretPosition = new ReadOnlyIntegerWrapper(this, "caretPosition");
    }

    /// %Part 2: Layout

    private InlineCssTextArea buildTextArea(){
        textArea = new InlineCssTextArea();
        textArea.setWrapText(true);
        return textArea;
    }

    private GridPane buildBottomPane(){
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
        statLabel = new Label();

        HBox align = new HBox();
        align.setAlignment(Pos.BASELINE_CENTER);
        align.getChildren().add(statLabel);

        return align;
    }

    private HBox buildTimeLabel(){
        timeLabel = new Label();

        HBox align = new HBox();
        align.setAlignment(Pos.BASELINE_RIGHT);
        align.getChildren().add(timeLabel);

        return align;
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

    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setTextReadyProperty(textReady);
        setCaretPositionProperty(caretPosition);
        setupChildern(control);
    }

    protected abstract void setTextReadyProperty(ReadOnlyBooleanWrapper prop);

    protected abstract void setCaretPositionProperty(ReadOnlyIntegerWrapper prop);

    protected abstract void setupChildern(WriterSceneControl control);


    /// %Part 4: Properties

    public ReadOnlyBooleanProperty textReadyProperty(){
        return textReady.getReadOnlyProperty();
    }

    public boolean isTextReady(){
        return textReady.getValue();
    }

    public ReadOnlyIntegerProperty caretPositionProperty(){
        return caretPosition.getReadOnlyProperty();
    }

    public int getCaretPosition(){
        return caretPosition.getValue();
    }

    /// %Part 5: Get Child Methods
    protected InlineCssTextArea getTextArea(){
        return textArea;
    }

    protected Label getLineTypeLabel(){
        return lineTypeLabel;
    }

    protected Label getStatsLabel(){
        return statLabel;
    }

    protected Label getTimeLabel(){
        return timeLabel;
    }

}
