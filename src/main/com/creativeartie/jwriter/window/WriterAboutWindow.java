package com.creativeartie.jwriter.window;

import java.util.*;
import java.time.*;
import javafx.scene.layout.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import java.io.*;

import com.google.common.io.*;
import com.google.common.base.*;

import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.*;
import com.creativeartie.jwriter.property.window.*;

public class WriterAboutWindow extends Stage{
    protected static int WIDTH = 650;
    protected static int HEIGHT = 500;
    private static String AGPL = getResource("/data/agpl-3.0.txt");
    private static String APACHE = getResource("/data/apache.txt");
    private static String BSD = getResource("/data/bsd.txt");

    private static String getResource(String file){
        try {
            return Resources.asCharSource(WriterAboutWindow.class
                .getResource(file), Charsets.UTF_8).read();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private final TextArea licenseText;

    public WriterAboutWindow(){
        setTitle(WindowText.ABOUT_TITLE.getText());
        setResizable(false);
        licenseText = createTextArea();
        setScene(createScene());
        initModality(Modality.APPLICATION_MODAL);
    }

    private TextArea createTextArea(){
        TextArea text = new TextArea();
        text.setEditable(false);
        text.setPrefColumnCount(80);
        text.setPrefRowCount(80);
        text.setWrapText(true);
        return text;
    }

    private Scene createScene(){
        BorderPane pane = new BorderPane();

        ///Create body
        pane.setCenter(licenseText);

        /// Create top pane:
        Label name = new Label(WindowText.PROGRAM_NAME.getText());
        name.getStyleClass().add("title");
        Hyperlink license = addLicenseLink(WindowText.ABOUT_LICENSE, APACHE);
        FlowPane libs = new FlowPane(
            addLicenseLink(WindowText.ABOUT_GUAVA, APACHE),
            addLicenseLink(WindowText.ABOUT_RICH_TEXT, BSD),
            addLicenseLink(WindowText.ABOUT_ITEXT, AGPL)
        );
        FlowPane top = new FlowPane(Orientation.VERTICAL, name, license, libs);
        top.setAlignment(Pos.CENTER);
        top.getStyleClass().add("top");
        pane.setTop(top);

        Scene ans = new Scene(pane, WIDTH, HEIGHT);
        ans.getStylesheets().add("data/about.css");
        return ans;
    }

    private Hyperlink addLicenseLink(WindowText title, String text){
        Hyperlink ans = new Hyperlink(title.getText());
        ans.setOnAction(evt -> licenseText.setText(text));

        return ans;
    }
}