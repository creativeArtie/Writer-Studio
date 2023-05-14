package com.creativeartie.humming.ui;

import java.util.*;

import com.creativeartie.humming.document.*;
import com.creativeartie.humming.files.*;

import javafx.beans.property.*;
import javafx.beans.value.*;

/**
 * A tab containing current manuscript file.
 */
public abstract class ActiveFile {
    private static SimpleObjectProperty<ProjectZip> openedProject;
    private static ReadOnlyObjectWrapper<ActiveFile> activeFile;
    private static ReadOnlyIntegerWrapper currentPosition;

    /**
     * Gets the property for the project zip
     *
     * @return the project zip property
     */
    public static SimpleObjectProperty<ProjectZip> getOpenedProject() {
        if (openedProject == null) {
            openedProject = new SimpleObjectProperty<>();
        }
        return openedProject;
    }

    /**
     * Gets the read only active file. Require because setting the active file using
     * property binding
     *
     * @return the read only active file property.
     */
    public static ReadOnlyObjectProperty<ActiveFile> activeFileProperty() {
        return getActiveFile().getReadOnlyProperty();
    }

    private static ReadOnlyObjectWrapper<ActiveFile> getActiveFile() {
        if (activeFile == null) {
            activeFile = new ReadOnlyObjectWrapper<>();
        }
        return activeFile;
    }

    /**
     * Set the currently active file.
     *
     * @param file
     *        the controller to set
     */
    public static void setActiveFile(ActiveFile file) {
        getActiveFile().setValue(file);
        currentPosition.bind(file.docCursorProperty());
    }

    private static ReadOnlyIntegerWrapper getCurrentPosWrapper() {
        if (currentPosition == null) {
            currentPosition = new ReadOnlyIntegerWrapper();
        }
        return currentPosition;
    }

    /**
     * Gets the current position property.
     *
     * @return the current position.
     */
    public static ReadOnlyIntegerProperty getCurrentPosProperty() {
        return getCurrentPosWrapper().getReadOnlyProperty();
    }

    /**
     * Gets the document cursor position.
     *
     * @return the position.
     */
    protected abstract ObservableValue<Integer> docCursorProperty();

    /**
     * Gets the manuscript file.
     *
     * @return the manuscript.
     */
    protected abstract ReadOnlyObjectProperty<Manuscript> manuscriptProperty();

    private Manuscript getManuscript() {
        return manuscriptProperty().get();
    }

    /**
     * Locate children node at the cursor
     *
     * @return the list nodes.
     *
     * @see #locateChildren(int)
     */
    public List<Span> locateChildrenAtCursor() {
        return locateChildren(docCursorProperty().getValue());
    }

    /**
     * Locate children node at a location
     *
     * @param location
     *        the location
     *
     * @return the list nodes.
     *
     * @see #locateChildrenAtCursor()
     */
    public List<Span> locateChildren(int location) {
        return getManuscript().locateChildren(location);
    }
}
