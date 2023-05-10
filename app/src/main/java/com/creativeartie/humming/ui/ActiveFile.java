package com.creativeartie.humming.ui;

import java.util.*;

import com.creativeartie.humming.document.*;
import com.creativeartie.humming.files.*;

import javafx.beans.property.*;
import javafx.beans.value.*;

public abstract class ActiveFile {
    private static SimpleObjectProperty<ProjectZip> openedProject;
    private static ReadOnlyObjectWrapper<ActiveFile> activeFile;
    private static ReadOnlyIntegerWrapper currentPosition;

    public static SimpleObjectProperty<ProjectZip> getOpenedProject() {
        if (openedProject == null) {
            openedProject = new SimpleObjectProperty<>();
        }
        return openedProject;
    }

    public static ReadOnlyObjectProperty<ActiveFile> activeFileProperty() {
        return getActiveFile().getReadOnlyProperty();
    }

    private static ReadOnlyObjectWrapper<ActiveFile> getActiveFile() {
        if (activeFile == null) {
            activeFile = new ReadOnlyObjectWrapper<>();
        }
        return activeFile;
    }

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

    public static ReadOnlyIntegerProperty getCurrentPosProperty() {
        return getCurrentPosWrapper().getReadOnlyProperty();
    }

    protected abstract ObservableValue<Integer> docCursorProperty();

    protected abstract ReadOnlyObjectProperty<Manuscript> manuscriptProperty();

    private Manuscript getManuscript() {
        return manuscriptProperty().get();
    }

    public List<Span> locateChildrenAtCursor() {
        return locateChildren(docCursorProperty().getValue());
    }

    public List<Span> locateChildren(int location) {
        return getManuscript().locateChildren(location);
    }
}
