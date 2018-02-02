package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.scene.control.cell.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.resource.*;

/**
 * The agenda pane stores a list of to do item from either complete lines or in
 * line.
 */
interface TableData{
    public SpanBranch getTargetSpan();
}