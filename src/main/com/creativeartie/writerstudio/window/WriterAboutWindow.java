package com.creativeartie.writerstudio.window;

import java.util.*;
import java.time.*;
import javafx.scene.layout.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import java.io.*;
import java.net.*;

import com.google.common.io.*;
import com.google.common.base.*;

import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.resource.*;

public class WriterAboutWindow extends Stage{
    protected static int WIDTH = 650;
    protected static int HEIGHT = 500;
    private static String APACHE = getResource(FileResources.getApacheLicense());
    private static String BSD = getResource(FileResources.getBsdLicense());

    private static String getResource(URL stream){
        try {
            return Resources.asCharSource(stream, Charsets.UTF_8).read();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private final TextArea licenseText;

    public WriterAboutWindow(){
        setTitle(WindowText.ABOUT_TITLE.getText());
        setResizable(false);
        licenseText = initTextArea();
        setScene(createScene());
        initModality(Modality.APPLICATION_MODAL);
    }

    private TextArea initTextArea(){
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
        Hyperlink license = initLicenseLink(WindowText.ABOUT_LICENSE, APACHE);
        FlowPane libs = new FlowPane(
            initLicenseLink(WindowText.ABOUT_GUAVA, APACHE),
            initLicenseLink(WindowText.ABOUT_RICH_TEXT, BSD),
            initLicenseLink(WindowText.ABOUT_PDF_BOX, APACHE)
        );
        FlowPane top = new FlowPane(Orientation.VERTICAL, name, license, libs);
        top.setAlignment(Pos.CENTER);
        top.getStyleClass().add("top");
        pane.setTop(top);

        Scene ans = new Scene(pane, WIDTH, HEIGHT);
        ans.getStylesheets().add(FileResources.getAboutCss());
        return ans;
    }

    private Hyperlink initLicenseLink(WindowText title, String text){
        Hyperlink ans = new Hyperlink(title.getText());
        ans.setOnAction(evt -> licenseText.setText(text));

        return ans;
    }
}