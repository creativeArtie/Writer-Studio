package com.creativeartie.writerstudio.javafx;

import javafx.beans.property.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

public abstract class WindowStatView extends Stage{
    protected static int WIDTH = 490;
    protected static int HEIGHT = 490;

    private WindowStatMonthControl calendarPane;
    private Spinner<Integer> wordSpinner;
    private Spinner<Integer> hourSpinner;
    private Spinner<Integer> minuteSpinner;

    private ReadOnlyObjectWrapper<WritingStat> writingStat;

    /// %Part 1: Constructor and Class Fields

    WindowStatView(){
        Stage ans = new Stage();
        setTitle(WindowText.GOALS_TITLE.getText());
        setResizable(false);
        setScene(buildScene());
        initModality(Modality.APPLICATION_MODAL);

        writingStat = new ReadOnlyObjectWrapper<>(this, "writingStat");
    }

    /// %Part 2: Layout

    private Scene buildScene(){
        Scene scene = new Scene(createMainPane(), WIDTH, HEIGHT);
        scene.getStylesheets().add(FileResources.getStatsCss());
        return scene;
    }

    private BorderPane createMainPane(){
        BorderPane pane = new BorderPane();
        calendarPane = new WindowStatMonthControl();
        pane.setCenter(calendarPane);
        pane.setBottom(buildEditPane());
        return pane;
    }

    private GridPane buildEditPane(){
        GridPane pane = new GridPane();

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70);
        pane.getColumnConstraints().addAll(column1, column2);

        Label word = new Label(WindowText.GOAL_WORD_TEXT.getText());

        wordSpinner = new Spinner<>(0, 10000, 100);
        wordSpinner.setEditable(true);

        Label time = new Label(WindowText.GOALS_TIME_TEXT.getText());

        pane.add(word, 0, 0);
        pane.add(wordSpinner, 1, 0);
        pane.add(time, 0, 1);
        pane.add(buildTimeGoalPane(), 1, 1);

        return pane;
    }

    private FlowPane buildTimeGoalPane(){
        FlowPane pane = new FlowPane();

        hourSpinner = new Spinner<>(0, 23, 0);
        hourSpinner.setEditable(true);
        hourSpinner.setPrefWidth(100.0);

        Label hour = new Label(WindowText.HOUR_UNIT.getText());

        minuteSpinner = new Spinner<>(0, 59, 30);
        minuteSpinner.setEditable(true);
        minuteSpinner.setPrefWidth(100.0);

        Label minute = new Label(WindowText.MINUTE_UNIT.getText());


        pane.getChildren().addAll(hourSpinner, hour, minuteSpinner, minute);
        return pane;
    }

    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        bindWritingStat(writingStat, control);
        setupChildern(control);
    }

    protected abstract void bindWritingStat(
        ReadOnlyObjectWrapper<WritingStat> stats, WriterSceneControl control);
    protected abstract void setupChildern(WriterSceneControl control);

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
