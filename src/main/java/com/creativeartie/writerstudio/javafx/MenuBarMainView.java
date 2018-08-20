package com.creativeartie.writerstudio.javafx;

import javafx.scene.control.*;
import javafx.beans.property.*;
import javafx.stage.*;

import com.creativeartie.writerstudio.lang.markup.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    MenuBarConstants.*;

abstract class MenuBarMainView extends MenuBar{

    private MenuItem createItem;
    private MenuItem openItem;
    private MenuItem exportItem;
    private MenuItem saveItem;
    private MenuItem exitItem;
    private MenuItem goalsItem;
    private MenuItem aboutItem;

    private SimpleObjectProperty<WritingFile> writingFile;

    public MenuBarMainView(Stage window){
        getMenus().addAll(buildFileMenu(), buildStatMenu(), buildHelpMenu());

        writingFile = new SimpleObjectProperty<>(this, "writingFile");

    }

    /// %Part 2: Layout
    private final Menu buildFileMenu(){
        Menu file =  new Menu(    FILE);
        createItem = new MenuItem(FILE_NEW);
        openItem =   new MenuItem(FILE_OPEN);
        exportItem = new MenuItem(FILE_EXPORT);
        saveItem =   new MenuItem(FILE_SAVE);
        exitItem =   new MenuItem(FILE_EXIT);
        file.getItems().addAll(createItem, openItem,
            new SeparatorMenuItem(), saveItem, exportItem,
            new SeparatorMenuItem(), exitItem);
        return file;
    }

    private final Menu buildStatMenu(){
        Menu stats = new Menu(    STATS);
        goalsItem =  new MenuItem(STATS_GOALS);
        stats.getItems().addAll(goalsItem);
        return stats;
    }

    private final Menu buildHelpMenu(){
        Menu help = new Menu(    HELP);
        aboutItem = new MenuItem(HELP_ABOUT);
        help.getItems().addAll(aboutItem);
        return help;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 4.1: WritingFile

    public ObjectProperty<WritingFile> writingFileProperty(){
        return writingFile;
    }

    protected SimpleObjectProperty<WritingFile> getWritingFileProperty(){
        return writingFile;
    }

    public WritingFile getWritingFile(){
        return writingFile.getValue();
    }

    public void setWritingFile(WritingFile file){
        writingFile.setValue(file);
    }

    /// %Part 5: Get Child Methods

    final MenuItem getCreateItem(){
        return createItem;
    }

    final MenuItem getOpenItem(){
        return openItem;
    }

    final MenuItem getExportItem(){
        return exportItem;
    }

    final MenuItem getSaveItem(){
        return saveItem;
    }

    final MenuItem getExitItem(){
        return exitItem;
    }

    final MenuItem getGoalsItem(){
        return goalsItem;
    }

    final MenuItem getAboutItem(){
        return aboutItem;
    }

}
