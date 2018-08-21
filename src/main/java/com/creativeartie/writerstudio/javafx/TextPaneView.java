package com.creativeartie.writerstudio.javafx;

import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    TextPaneConstants.*;


/**
 * A rict text fx text area that show the text
 */
abstract class TextPaneView extends BorderPane {

    /// %Part 1: Constructor and Class Fields

    private InlineCssTextArea textArea;
    private Label lineTypeLabel;
    private Label statLabel;
    private Label timeLabel;
    private ReadOnlyIntegerWrapper caretPosition;

    TextPaneView(){
        caretPosition = new ReadOnlyIntegerWrapper(this, "caretPosition");

        setCenter(buildTextArea());
        setBottom(buildBottomPane());
    }

    /// %Part 2: Layout

    private InlineCssTextArea buildTextArea(){
        textArea = new InlineCssTextArea();
        textArea.setWrapText(true);
        return textArea;
    }

    private GridPane buildBottomPane(){
        GridPane parent = new GridPane();
        CommonLayoutUtility.addColumnPrecent(parent, LABEL_WIDTH);
        parent.add(buildLineTypeLabel(), 0, 0);

        CommonLayoutUtility.addColumnPrecent(parent, LABEL_WIDTH);
        parent.add(buildStatsLabel(), 1, 0);

        CommonLayoutUtility.addColumnPrecent(parent, LABEL_WIDTH);
        parent.add(buildTimeLabel(), 2, 0);

        return parent;
    }


    private FlowPane buildLineTypeLabel(){
        lineTypeLabel = new Label();
        lineTypeLabel.getStyleClass().add(LINE_TYPE_STYLE);
        return new FlowPane(lineTypeLabel);
    }

    private Label buildStatsLabel(){
        statLabel = new Label();
        statLabel.getStyleClass().add(TEXT_STAT_STYLE);
        return statLabel;
    }

    private Label buildTimeLabel(){
        timeLabel = new Label();
        timeLabel.getStyleClass().add(CLOCK_STYLE);
        return timeLabel;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindCaretPosition(caretPosition);
        bindChildren(control);
    }

    protected abstract void bindCaretPosition(ReadOnlyIntegerWrapper property);

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

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
