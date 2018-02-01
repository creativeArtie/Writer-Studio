package com.creativeartie.jwriter.window;

import java.util.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import javafx.scene.control.*;
import org.fxmisc.richtext.model.*;
import javafx.animation.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.resource.*;

import com.google.common.collect.*;

abstract class WriterTabView extends TabPane{
    private Tab[] tabs;

    public WriterTabView(){
        tabs = new Tab[]{
            initAgendaPane(), initLinksPane()
        };
        getTabs().addAll(tabs);
    }

    private Tab initAgendaPane(){
        AgendaPaneControl content = new AgendaPaneControl();
        Tab ans = new Tab(WindowText.TAB_AGENDA.getText(), content);
        return ans;
    }

    private Tab initLinksPane(){
        LinksPaneControl content = new LinksPaneControl();
        Tab ans = new Tab(WindowText.TAB_LINK.getText(), content);
        return ans;
    }

    public AgendaPaneControl getAgendaPane(){
        return (AgendaPaneControl) tabs[0].getContent();
    }

    public LinksPaneControl getLinksPane(){
        return (LinksPaneControl) tabs[1].getContent();
    }
}