package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;

public class PaneHeadingControl extends PaneHeadingView{

    private SectionHeading root;

    @Override
    public void loadHeadings(ManuscriptDocument document){
        root = document.getSections();
        getHeadings().loadHeadings(root.getChildren());
        getOutlines().loadHeadings(null);
    }

    @Override
    public void setHeading(ManuscriptDocument doc, int position){
        Checker.checkNotNull(root, "root");
        doc.spansAt(position, MainSpanSection.class).ifPresent(span ->{
            Optional<LinedSpanSection> heading = span.getHeading();
            getHeadings().selectHeading(heading);
            root.findChild(heading).ifPresent(found -> getOutlines()
                .loadHeadings(found.getOutlines()));
            getOutlines().selectHeading(span.getOutline());
        });
    }
}