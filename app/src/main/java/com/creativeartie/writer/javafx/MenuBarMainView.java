package com.creativeartie.writer.javafx;

import javafx.scene.control.*;
import javafx.beans.property.*;
import javafx.stage.*;

import com.creativeartie.writer.lang.markup.*;
import static com.creativeartie.writer.javafx.utils.LayoutConstants.
    MenuBarConstants.*;

abstract class MenuBarMainView extends MenuBar{

    /// %Part 1: Constructor and Class Fields

    private SimpleObjectProperty<WritingFile> writingFile;

    private MenuItem createItem;
    private MenuItem openItem;
    private MenuItem exportItem;
    private MenuItem saveItem;
    private MenuItem exitItem;
    private MenuItem goalsItem;
    private MenuItem aboutItem;

    public MenuBarMainView(Stage window){
        writingFile = new SimpleObjectProperty<>(this, "writingFile");

        getMenus().addAll(buildFileMenu(), buildStatMenu(), buildHelpMenu());
    }

    /// %Part 2: Layout

    /// %Part 2 (menu bar -> file menu)

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

    /// %Part 2 (menu bar -> stat menu)

    private final Menu buildStatMenu(){
        Menu stats = new Menu(    STATS);
        goalsItem =  new MenuItem(STATS_GOALS);
        stats.getItems().addAll(goalsItem);
        return stats;
    }

    /// %Part 2 (menu bar -> help menu)

    private final Menu buildHelpMenu(){
        Menu help = new Menu(    HELP);
        aboutItem = new MenuItem(HELP_ABOUT);
        help.getItems().addAll(aboutItem);
        return help;
    }

    /// %Part 3: Setup Properties

    public void postLoad(WriterSceneControl control){
        bindChildren(control);
    }

    protected abstract void bindChildren(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 4.1: writingFile (WritingFile)

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
