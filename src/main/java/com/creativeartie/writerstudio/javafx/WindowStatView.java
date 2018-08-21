package com.creativeartie.writerstudio.javafx;

import javafx.beans.property.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.javafx.utils.*;

import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    WindowStatContants.*;

public abstract class WindowStatView extends Stage{

    /// %Part 1: Constructor and Class Fields

    private ReadOnlyObjectWrapper<WritingStat> writingStat;

    private WindowStatMonthControl calendarPane;
    private Spinner<Integer> wordSpinner;
    private Spinner<Integer> hourSpinner;
    private Spinner<Integer> minuteSpinner;

    WindowStatView(){
        Stage ans = new Stage();
        setTitle(WINDOW_TITLE);
        setResizable(false);
        setScene(buildScene());
        initModality(Modality.APPLICATION_MODAL);

        writingStat = new ReadOnlyObjectWrapper<>(this, "writingStat");
    }

    /// %Part 2: Layout

    /// %Part 2 (stage -> scene)

    private Scene buildScene(){
        Scene scene = new Scene(createMainPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(FileResource.STAT_CSS.getCssPath());
        return scene;
    }

    /// %Part 2 (stage -> scene -> content)

    private BorderPane createMainPane(){
        BorderPane pane = new BorderPane();
        calendarPane = new WindowStatMonthControl();
        pane.setCenter(calendarPane);
        pane.setBottom(buildEditPane());
        return pane;
    }

    /// %Part 2 (stage -> scene -> content -> bottom)

    private GridPane buildEditPane(){
        GridPane pane = new GridPane();

        Label word = new Label(GOAL_WORD_TEXT);

        wordSpinner = new Spinner<>(WORD_GOAL_MIN, WORD_GOAL_MAX,
            WORD_GOAL_DEFAULT);
        wordSpinner.setEditable(true);

        Label time = new Label(GOALS_TIME_TEXT);

        CommonLayoutUtility.addColumnPrecent(pane, GOAL_LABEL_COLUMN);
        CommonLayoutUtility.addColumnPrecent(pane, GOAL_DATA_COLUMN);
        pane.add(word, 0, 0);
        pane.add(wordSpinner, 1, 0);
        pane.add(time, 0, 1);
        pane.add(buildTimeGoalPane(), 1, 1);

        return pane;
    }

    /// %Part 2 (stage -> scene -> content -> bottom -> time data panel)

    private FlowPane buildTimeGoalPane(){
        FlowPane pane = new FlowPane();

        hourSpinner = new Spinner<>(HOUR_GOAL_MIN, HOUR_GOAL_MAX,
            HOUR_GOAL_DEFAULT);
        hourSpinner.setEditable(true);
        hourSpinner.setPrefWidth(100.0);

        Label hour = new Label(HOUR_UNIT);

        minuteSpinner = new Spinner<>(MINS_GOAL_MIN, MINS_GOAL_MAX,
            MINS_GOAL_DEFAULT);
        minuteSpinner.setEditable(true);
        minuteSpinner.setPrefWidth(100.0);

        Label minute = new Label(MINUTE_UNIT);


        pane.getChildren().addAll(hourSpinner, hour, minuteSpinner, minute);
        return pane;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindWritingStat(writingStat, control);
        bindChildren(control);
    }

    protected abstract void bindWritingStat(
        ReadOnlyObjectWrapper<WritingStat> stats, WriterSceneControl control);
    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    public ReadOnlyObjectProperty<WritingStat> writingStatProperty(){
        return writingStat.getReadOnlyProperty();
    }

    public WritingStat getWritingStat(){
        return writingStat.getValue();
    }

    /// %Part 5: Get Child Methods

    WindowStatMonthControl getCalendarPane() {
        return calendarPane;
    }

    Spinner<Integer> getWordSpinner(){
        return wordSpinner;
    }

    Spinner<Integer> getHourSpinner(){
        return hourSpinner;
    }

    Spinner<Integer> getMinuteSpinner(){
        return minuteSpinner;
    }
}
