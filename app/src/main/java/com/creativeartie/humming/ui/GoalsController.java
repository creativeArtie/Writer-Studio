package com.creativeartie.humming.ui;

import java.time.*;
import java.time.format.*;

import com.creativeartie.humming.main.*;

import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Controller for <a href="../../../../../resources/data/goals.fxml"> goals.fxml
 * </a>
 */
public class GoalsController {
    @FXML
    private Label yearMonth;
    @FXML
    private Button last, next;
    @FXML
    private GridPane days;
    private static final int weeksOfMonth = 6;
    private static final int daysOfWeek = DayOfWeek.values().length;
    private Label[][] dayLabels;
    private SimpleObjectProperty<YearMonth> displayingMonth;

    @FXML
    void initialize() {
        displayingMonth = new SimpleObjectProperty<>();
        dayLabels = new Label[weeksOfMonth][daysOfWeek];
        for (int row = 0; row < weeksOfMonth; row++) {
            for (int col = 0; col < daysOfWeek; col++) {
                HBox box = new HBox();
                Label use = new Label();
                box.getChildren().add(use);
                box.getStyleClass().add(CssStyles.CALENDAR_GRID_CELL.toString());
                if (col == 0) {
                    box.getStyleClass().add(CssStyles.CALENDAR_FIRST_COLUMN.toString());
                }
                dayLabels[row][col] = use;
                days.add(box, col, row + 1);
            }
        }

        displayingMonth.addListener((observable, oldValue, newValue) -> updateCalendar(newValue));
        displayingMonth.set(YearMonth.now());
        last.setOnMouseClicked(me -> {
            displayingMonth.set(displayingMonth.get().minusMonths(1));
        });
        next.setOnMouseClicked(me -> {
            displayingMonth.set(displayingMonth.get().plusMonths(1));
        });
    }

    private void updateCalendar(YearMonth month) {
        yearMonth.setText(month.format(DateTimeFormatter.ofPattern(UIText.General.YEAR_MONTH_FORMAT.getText())));
        if (month.equals(YearMonth.now())) {
            yearMonth.getStyleClass().setAll(CssStyles.ACTIVE.toString());
        } else {
            yearMonth.getStyleClass().clear();
        }

        LocalDate date1 = month.atDay(1);
        int startCol = date1.getDayOfWeek().ordinal();
        LocalDate useDate = date1.minusDays(startCol + 1);
        for (int row = 0; row < weeksOfMonth; row++) {
            for (int col = 0; col < daysOfWeek; col++) {
                dayLabels[row][col].setText(Integer.toString(useDate.getDayOfMonth()));

                if (useDate.getMonth() == month.getMonth()) {
                    dayLabels[row][col].getStyleClass().clear();
                } else {
                    dayLabels[row][col].getStyleClass().setAll(CssStyles.INACTIVE.toString());
                }

                useDate = useDate.plusDays(1);
            }
        }
    }
}
