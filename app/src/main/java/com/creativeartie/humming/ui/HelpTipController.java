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
    private Label headingText, outlineText, catSpan, idSpan, breakLine;
    @FXML
    private Label quoteLine, todoLine, normalLine, tableLine;
    @FXML
    private Label footnoteLine, endnoteLine, imageLine, numberedLine;
    @FXML
    private Label summaryLine, noteLine, fieldLine, bulletLine;
    @FXML
    private Label footRef, endRef, noteRef, metaRef, todoRef;
    @FXML
    private Label boldSpan, underSpan, italicSpan;

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
        updateHighlightStyle(quoteLine, style == CssLineStyles.QUOTE);
        updateHighlightStyle(todoLine, style == CssLineStyles.AGENDA);
        updateHighlightStyle(normalLine, style == CssLineStyles.NORMAL);
        updateHighlightStyle(tableLine, style == CssLineStyles.ROW);
        updateHighlightStyle(footnoteLine, style == CssLineStyles.FOOTNOTE);
        updateHighlightStyle(endnoteLine, style == CssLineStyles.ENDNOTE);
        updateHighlightStyle(imageLine, style == CssLineStyles.IMAGE);
        updateHighlightStyle(numberedLine, style == CssLineStyles.NUMBERED);
        updateHighlightStyle(summaryLine, style == CssLineStyles.HEADER);
        updateHighlightStyle(noteLine, style == CssLineStyles.NOTE);
        updateHighlightStyle(fieldLine, style == CssLineStyles.FIELD);
        updateHighlightStyle(bulletLine, style == CssLineStyles.BULLET);
    }

    private void updateHighlightStyle(Label label, boolean matched) {
        label.getStyleClass().setAll(matched ? "active" : "inactive"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
