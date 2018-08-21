package com.creativeartie.writerstudio.javafx;

import java.util.*;
import java.time.*;
import java.time.format.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    WindowStatChildContants.*;

abstract class WindowStatMonthView extends GridPane{

    /// %Part 1: Constructor and Class Fields

    private SimpleObjectProperty<YearMonth> currentMonth;

    private Button firstButton;
    private Button pastButton;
    private Button nextButton;
    private Button endButton;
    private Label yearMonthLabel;
    private List<WindowStatDayControl> dayPanes;

    WindowStatMonthView(){
        setColumnConstraints();
        add(buildPastNav(), 0 ,0, 2, 1);
        add(buildMonthTitle(), 2, 0, 3, 1);
        add(buildNextNav(), 5, 0, 2, 1);

        int col = 0;
        for(DayOfWeek day: DayOfWeek.values()){
            add(buildDayLabel(day), col, 1);
            col++;
        }

        buildDayPanes();

        currentMonth = new SimpleObjectProperty<>(this, "currentMonth");
    }

    /// %Part 2: Layout

    /// %Part 2 column sizes

    private void setColumnConstraints(){
        ArrayList<ColumnConstraints> columns = new ArrayList<>();
        int size = DayOfWeek.values().length;
        for(int i = 0; i < size; i++){
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(COLUMN_WIDTH);
            column.setMaxWidth(COLUMN_WIDTH);
            columns.add(column);
        }
        getColumnConstraints().addAll(columns);
    }

    /// %Part 2 (content -> top left)

    private FlowPane buildPastNav(){
        firstButton = new Button();
        firstButton.setGraphic(ImageIcon.START_MONTH.getIcon());

        pastButton = new Button();
        pastButton.setGraphic(ImageIcon.PAST_MONTH.getIcon());

        FlowPane pane = new FlowPane(firstButton, pastButton);
        pane.setAlignment(Pos.BOTTOM_LEFT);
        return pane;
    }

    /// %Part 2 (content -> top center)

    private StackPane buildMonthTitle(){
        yearMonthLabel = new Label();
        yearMonthLabel.getStyleClass().add(MONTH_STYLE);
        return new StackPane(yearMonthLabel);
    }

    /// %Part 2 (content -> top right)

    private FlowPane buildNextNav(){
        nextButton = new Button();
        nextButton.setGraphic(ImageIcon.NEXT_MONTH.getIcon());
        endButton = new Button();
        endButton.setGraphic(ImageIcon.END_MONTH.getIcon());

        FlowPane pane = new FlowPane(nextButton, endButton);
        pane.setAlignment(Pos.BOTTOM_RIGHT);
        return pane;
    }

    /// %Part 2 (content -> row) separate setup and insert methods

    private StackPane buildDayLabel(DayOfWeek day){
        LocalDate use = LocalDate.now().with(day);
        String text = DateTimeFormatter.ofPattern("EEE").format(use);
        Label header = new Label(text);
        header.getStyleClass().add(WEEKDAY_STYLE);

        StackPane pane = new StackPane(header);
        pane.getStyleClass().add(WEEKDAY_BOX_STYLE);
        return pane;
    }

    private void buildDayPanes(){
        ImmutableList.Builder<WindowStatDayControl> builder = ImmutableList
            .builder();
        for (int i = 0; i < DAY_PANES; i++){
            builder.add(new WindowStatDayControl());
        }

        dayPanes = builder.build();
        Iterator<WindowStatDayControl> it = dayPanes.iterator();

        for(int i = 0; i < SHOW_WEEKS; i++){
            for (int j = 0; j < DAY_OF_WEEK; j++){
                add(it.next(), j, i + 2); /// + 2 heading rows
            }
        }
    }

    /// %Part 3: Setup Properties

    public void postLoad(WindowStatControl control){
        bindChildren(control);
    }

    protected abstract void bindChildren(WindowStatControl control);

    /// %Part 4: Properties

    /// %Part 4.1: currentMonth (YearMonth)

    public SimpleObjectProperty<YearMonth> currentMonthProperty(){
        return currentMonth;
    }

    public YearMonth getCurrentMonth(){
        return currentMonth.getValue();
    }

    public void setCurrentMonth(YearMonth value){
        currentMonth.setValue(value);
    }

    /// %Part 5: Get Child Methods
    Button getFirstButton(){
        return firstButton;
    }

    Button getPastButton(){
        return pastButton;
    }

    Button getNextButton(){
        return nextButton;
    }

    Button getEndButton(){
        return endButton;
    }

    Label getYearMonthLabel(){
        return yearMonthLabel;
    }

    List<WindowStatDayControl> getDayPanes(){
        return dayPanes;
    }
}
