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
    private ReadOnlyIntegerWrapper updatedPosition;

    TextPaneView(){
        updatedPosition = new ReadOnlyIntegerWrapper(this, "updatedPosition");

        setCenter(buildTextArea());
        setBottom(buildBottomPane());
    }

    /// %Part 2: Layout

    /// %Part 2 (content -> center)

    private InlineCssTextArea buildTextArea(){
        textArea = new InlineCssTextArea();
        textArea.setWrapText(true);
        return textArea;
    }

    /// %Part 2 (content -> bottom grid)

    private GridPane buildBottomPane(){
        GridPane parent = new GridPane();
        CommonLayoutUtility.setWidthPrecent(parent, LABEL_WIDTH);
        parent.add(buildLineTypeLabel(), 0, 0);

        CommonLayoutUtility.setWidthPrecent(parent, LABEL_WIDTH);
        parent.add(buildStatsLabel(), 1, 0);

        CommonLayoutUtility.setWidthPrecent(parent, LABEL_WIDTH);
        parent.add(buildTimeLabel(), 2, 0);

        return parent;
    }

    /// %Part 2 (content -> bottom grid -> left)

    private FlowPane buildLineTypeLabel(){
        lineTypeLabel = new Label();
        lineTypeLabel.getStyleClass().add(LINE_TYPE_STYLE);
        return new FlowPane(lineTypeLabel);
    }

    /// %Part 2 (content -> bottom grid -> center)

    private Label buildStatsLabel(){
        statLabel = new Label();
        statLabel.getStyleClass().add(TEXT_STAT_STYLE);
        return statLabel;
    }

    /// %Part 2 (content -> bottom grid -> right)

    private Label buildTimeLabel(){
        timeLabel = new Label();
        timeLabel.getStyleClass().add(CLOCK_STYLE);
        return timeLabel;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindUpdatedPosition(updatedPosition);
        bindChildren(control);
    }

    protected abstract void bindUpdatedPosition(ReadOnlyIntegerWrapper property);

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 4.1: updatePosition (Integer)

    public ReadOnlyIntegerProperty updatedPositionProperty(){
        return updatedPosition.getReadOnlyProperty();
    }

    public int getUpdatedPosition(){
        return updatedPosition.getValue();
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
