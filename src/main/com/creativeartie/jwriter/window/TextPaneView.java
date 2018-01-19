package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.layout.*;
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
    /** Property binded with textAra.plainTextChanges. */
    private final ReadOnlyObjectWrapper<PlainTextChange> textChanged;
    /** Property edided by modeUsed.onAction */
    private final ReadOnlyObjectWrapper<WindowText> modeUsed;
    /** Property binded with textArea.caretPosition. */
    private final ReadOnlyIntegerWrapper cursorPlaced;
    /** Property binded with textArea.focused. */
    private final ReadOnlyBooleanWrapper editorFocused;

    TextPaneView(){
        textArea = setupTextArea();
        Button mode = setupViewMode();
        currentStats = new Label();
        currentTime = new Label();
        layoutBottomPane(node, currentStats, currentTime);

        textChanged = new ReadOnlyObjectWrapper<>(this, "textChanged");
        textArea.plainTextChanges().subscribe(value -> textChanged
            .setValue(value));

        cursorPlaced = new ReadOnlyIntegerWrapper(this, "cursorPlaced");
        cursorPlaced.bind(textArea.caretPositionProperty());

        editorFocused = new ReadOnlyBooleanWrapper(this, "editorFocused");
        editorFocused.bind(textArea.focusedProperty());

        modeUsed = new ReadOnlyObjectWrapper<>(this, "modeUsed");
        modeUsed.setValue(setNextMode(null));

        mode.setOnAction(evt -> modeUsed
            .setValue(setNextMode(modeUsed.getValue())));
    }

    /// Layout Node
    private void setupTextArea(){
        InlineCssTextArea area = new InlineCssTextArea();
        area.setWrapText(true);
        setCenter(area);
        return area;
    }

    private Button setupViewMode(){
        Button ans = new Button();
        ans.setDisable(true);
        return ans;
    }

    private BorderPane layoutBottomPane(Node left, Node center, Node right){
        BorderPane pane = new BorderPane();
        pane.setLeft(left);
        pane.setCenter(center);
        pane.setRight(right);
        setBottom(pane);
        return pane;
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

    public ReadOnlyIntegerProperty cursorPlacedProperty(){
        return cursorPlaced.getReadOnlyProperty();
    }

    public int getCursorPlaced(){
        return cursorPlaced.getValue();
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

    /// Control Methods
    public abstract WindowText setNextMode(WindowText lastMode);

}