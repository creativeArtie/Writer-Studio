package com.creativeartie.writerstudio.stats;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import java.time.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;

import com.creativeartie.writerstudio.resource.*;

import com.creativeartie.writerstudio.window.*;

abstract class PaneMonthView extends GridPane{
    private Button startMonth;
    private Button pastMonth;
    private Button nextMonth;
    private Button endMonth;
    private Label yearMonthText;
    private PaneMonthDayControl[] days;
    private ReadOnlyObjectWrapper<YearMonth> yearMonth;
    private ReadOnlyObjectWrapper<YearMonth> firstMonth;
    private ReadOnlyObjectWrapper<YearMonth> finalMonth;
    private SimpleObjectProperty<RecordList> recordTable;

    public PaneMonthView(){
        days = new PaneMonthDayControl[7 * 6];
        for (int i = 0; i < days.length; i++){
            days[i] = new PaneMonthDayControl();
        }
        startMonth = new Button();
        pastMonth = new Button();
        nextMonth = new Button();
        endMonth = new Button();
        yearMonthText = new Label();

        columnConstraint();
        layoutHeader();
        layoutWeekdays();
        layoutDays();

        yearMonth = new ReadOnlyObjectWrapper<>(this, "yearMonth");
        yearMonth.addListener((data, oldValue, newValue) ->
            listenMonth(newValue));
        firstMonth = new ReadOnlyObjectWrapper<>(this, "firstMonth");
        finalMonth = new ReadOnlyObjectWrapper<>(this, "finalMonth");
        recordTable = new SimpleObjectProperty<>();
        recordTable.addListener((data, oldValue, newValue) ->
            listenRecords(newValue));

        BooleanBinding isFirst = Bindings.createBooleanBinding(() ->
            Optional.ofNullable(firstMonth.get()).map(value ->
                value.equals(yearMonth.get())).orElse(true),
            firstMonth, yearMonth);
        startMonth.disableProperty().bind(isFirst);
        pastMonth.disableProperty().bind(isFirst);

        BooleanBinding isFinal = Bindings.createBooleanBinding(() ->
            Optional.ofNullable(finalMonth.get()).map(value ->
                value.equals(yearMonth.get())).orElse(true),
            finalMonth, yearMonth);
        nextMonth.disableProperty().bind(isFinal);
        endMonth.disableProperty().bind(isFinal);
    }

    /// Getters
    public Button getStartMonth(){
        return startMonth;
    }

    public Button getPastMonth(){
        return pastMonth;
    }

    public Button getNextMonth(){
        return nextMonth;
    }

    public Button getEndMonth(){
        return endMonth;
    }

    public Label getHeadLabel(){
        return yearMonthText;
    }

    public PaneMonthDayView getDayPane(int i){
        try{
            return days[i];
        } catch (Exception ex){
            System.out.println(i);
            System.exit(-1);
            return null;
        }
    }

    public int getDayPaneLength(){
        return days.length;
    }


    /// Layout Node
    private void columnConstraint(){
        ArrayList<ColumnConstraints> columns = new ArrayList<>();
        int numberOfDays = DayOfWeek.values().length;
        for(int i = 0; i < numberOfDays; i++){
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(SceneStatsView.WIDTH / 7);
            column.setMaxWidth(SceneStatsView.WIDTH / 7);
            columns.add(column);
        }
        getColumnConstraints().addAll(columns);
    }

    private void layoutHeader(){
        FlowPane pane = new FlowPane(startMonth, pastMonth);
        startMonth.setGraphic(ButtonIcon.START_MONTH.getIcon());
        pastMonth.setGraphic(ButtonIcon.PAST_MONTH.getIcon());
        pastMonth.setOnAction(event -> setYearMonth(getYearMonth()
            .minusMonths(1)));
        pane.setAlignment(Pos.BOTTOM_LEFT);
        add(pane, 0 ,0, 2, 1);

        StackPane month = new StackPane(yearMonthText);
        yearMonthText.getStyleClass().add("month");
        add(month, 2, 0, 3, 1);

        pane = new FlowPane(nextMonth, endMonth);
        nextMonth.setGraphic(ButtonIcon.NEXT_MONTH.getIcon());
        endMonth.setGraphic(ButtonIcon.END_MONTH.getIcon());
        nextMonth.setOnAction(event -> setYearMonth(getYearMonth()
            .plusMonths(1)));
        pane.setAlignment(Pos.BOTTOM_RIGHT);
        add(pane, 5, 0, 2, 1);
    }

    private void layoutWeekdays(){
        int col = 0;
        for(DayOfWeek day: DayOfWeek.values()){
            Label header = new Label(WindowText.getText(day));
            header.getStyleClass().add("weekday");
            StackPane box = new StackPane(header);
            box.getStyleClass().add("weekday-box");
            add(box, col, 1);
            col++;
        }
    }

    private void layoutDays(){
        int ptr = 0;
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 7; j++){
                add(days[ptr++], j, i + 2);
            }
        }
    }

    /// Node Properties
    public ReadOnlyObjectProperty<YearMonth> yearMonthProperty(){
        return yearMonth.getReadOnlyProperty();
    }

    public YearMonth getYearMonth(){
        return yearMonth.getValue();
    }

    protected void setYearMonth(YearMonth value){
        yearMonth.setValue(value);
    }

    public ObjectProperty<RecordList> recordTableProperty(){
        return recordTable;
    }

    public RecordList getRecordList(){
        return recordTable.getValue();
    }

    public void setRecordList(RecordList value){
        recordTable.setValue(value);
    }

    public ReadOnlyObjectProperty<YearMonth> firstMonthProperty(){
        return firstMonth.getReadOnlyProperty();
    }

    public YearMonth getFirstMonth(){
        return firstMonth.getValue();
    }

    public ReadOnlyObjectProperty<YearMonth> finalMonthProperty(){
        return finalMonth.getReadOnlyProperty();
    }

    public YearMonth getFinalMonth(){
        return finalMonth.getValue();
    }

    public void updateStartEnd(YearMonth start, YearMonth end){
        firstMonth.setValue(start);
        finalMonth.setValue(end);
    }

    /// Control Methods
    protected abstract void listenMonth(YearMonth value);

    protected abstract void listenRecords(RecordList value);
}