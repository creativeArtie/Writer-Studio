package com.creativeartie.humming.ui;

import com.creativeartie.humming.document.*;

import javafx.fxml.*;
import javafx.scene.control.*;

/**
 * Controller for <a href="../../../../../resources/data/helpTip.fxml">
 * helpTip.fxml </a>
 */
public class HelpTipController {
    @FXML
    private Label headingText, outlineText, normalLine, breakLine;
    @FXML
    private Label  tableHeadLine, tableCellLine, numberedLine, bulletLine;
    @FXML
    private Label footnoteLine, imageLine, todoLine, quoteLine;
    @FXML
    private Label summaryLine, noteLine, fieldLine;
    @FXML
    private Label footRef, noteRef, metaRef, errorRef;
    @FXML
    private Label todoRef, boldSpan, underSpan, italicSpan;
    @FXML
    private Label catSpan, idSpan;

    @FXML
    void initialize() {
        updateParagraphHighlight(null);
        ActiveFile.getCurrentPosProperty().addListener((prop, oldValue, newValue) -> runUpdate());
    }

    private void runUpdate() {
        for (Span span : ActiveFile.activeFileProperty().getValue().locateChildrenAtCursor()) {
            if (span instanceof Para) {
                updateParagraphHighlight(((Para) span).getLineStyle());
            }
        }
    }

    private void updateParagraphHighlight(CssLineStyles style) {
        updateHighlightStyle(headingText, style == CssLineStyles.HEADING);
        updateHighlightStyle(outlineText, style == CssLineStyles.OUTLINE);
        updateHighlightStyle(normalLine, style == CssLineStyles.NORMAL);
        updateHighlightStyle(breakLine, style == CssLineStyles.BREAK);
        
        updateHighlightStyle(tableHeadLine, style == CssLineStyles.ROW);
        updateHighlightStyle(tableCellLine, style == CssLineStyles.ROW);
        updateHighlightStyle(numberedLine, style == CssLineStyles.NUMBERED);
        updateHighlightStyle(bulletLine, style == CssLineStyles.BULLET);
        
        updateHighlightStyle(footnoteLine, style == CssLineStyles.FOOTNOTE);
        updateHighlightStyle(imageLine, style == CssLineStyles.IMAGE);
        updateHighlightStyle(quoteLine, style == CssLineStyles.QUOTE);
        updateHighlightStyle(todoLine, style == CssLineStyles.AGENDA);
        
        updateHighlightStyle(summaryLine, style == CssLineStyles.HEADER);
        updateHighlightStyle(noteLine, style == CssLineStyles.NOTE);
        updateHighlightStyle(fieldLine, style == CssLineStyles.FIELD);
    }

    private void updateHighlightStyle(Label label, boolean matched) {
        label.getStyleClass().setAll(matched ? "active" : "inactive"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
