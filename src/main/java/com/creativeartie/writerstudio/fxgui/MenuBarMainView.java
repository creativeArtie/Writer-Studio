package com.creativeartie.writerstudio.fxgui;

import javafx.scene.control.*;
import javafx.beans.property.*;
import javafx.stage.*;

import com.creativeartie.writerstudio.resource.*;

abstract class MenuBarMainView extends MenuBar{

    private MenuItem createItem;
    private MenuItem openItem;
    private MenuItem exportItem;
    private MenuItem saveItem;
    private MenuItem exitItem;
    private MenuItem goalsItem;
    private MenuItem aboutItem;

    public MenuBarMainView(Stage window){
        getMenus().addAll(buildFileMenu(), buildStatMenu(), buildHelpMenu());

        addListeners();
    }

    /// %Part 2: Layout
    private final Menu buildFileMenu(){
        Menu file = new Menu(WindowText.MENU_FILE.getText());
        createItem = new MenuItem(WindowText.MENU_FILE_NEW.getText());
        openItem = new MenuItem(WindowText.MENU_FILE_OPEN.getText());
        exportItem = new MenuItem(WindowText.MENU_FILE_EXPORT.getText());
        saveItem = new MenuItem(WindowText.MENU_FILE_SAVE.getText());
        exitItem = new MenuItem(WindowText.MENU_FILE_EXIT.getText());
        file.getItems().addAll(createItem, openItem,
            new SeparatorMenuItem(), saveItem, exportItem,
            new SeparatorMenuItem(), exitItem);
        return file;
    }

    private final Menu buildStatMenu(){
        Menu stats = new Menu(WindowText.MENU_STATS.getText());
        goalsItem = new MenuItem(WindowText.MENU_STATS_GOALS.getText());
        stats.getItems().addAll(goalsItem);
        return stats;
    }

    private final Menu buildHelpMenu(){
        Menu help = new Menu(WindowText.MENU_HELP.getText());
        aboutItem = new MenuItem(WindowText.MENU_HELP_ABOUT.getText());
        help.getItems().addAll(aboutItem);
        return help;
    }

    /// %Part 3: Listener Methods

    protected abstract void addListeners();

    /// %Part 4: Properties

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
