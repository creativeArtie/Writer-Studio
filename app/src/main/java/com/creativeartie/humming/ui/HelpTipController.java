package com.creativeartie.humming.ui;

import com.creativeartie.humming.main.*;

import javafx.fxml.*;
import javafx.scene.control.*;

public class HelpTipController {
    @FXML
    private Label headingText;
    @FXML
    private Label outlineText;

    @FXML
    protected void initialize() {
        InterfaceText.HEADING_TEXT.setText(headingText);
        InterfaceText.OUTLINE_TEXT.setText(outlineText);
    }

    public HelpTipController() {}
}
