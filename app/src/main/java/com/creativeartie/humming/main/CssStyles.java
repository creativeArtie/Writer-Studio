package com.creativeartie.humming.main;

import com.creativeartie.humming.ui.*;
import com.google.common.base.*;

/**
 * CSS style class.
 *
 * @author wai
 */
public enum CssStyles {
    /**
     * Heading for image and text file names.
     *
     * @see MainWindowController
     */
    FILE_HEADING,
    /**
     * Details for text file names.
     *
     * @see MainWindowController
     */
    FILE_DETIAL,
    /**
     * Set text bold to show active/current.
     *
     * @see HelpTipController
     * @see GoalsController
     */
    ACTIVE,
    /**
     * Set text bold to show inactive/fade out.
     *
     * @see HelpTipController
     * @see GoalsController
     */
    INACTIVE,
    /**
     * Set the border of calendar cells
     *
     * @see GoalsController
     */
    CALENDAR_GRID_CELL,

    /**
     * Set the border of calendar cells of the first columns
     *
     * @see GoalsController
     */
    CALENDAR_FIRST_COLUMN;

    @Override
    public String toString() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }
}
