package com.creativeartie.writerstudio.javafx;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import java.net.*;

import com.google.common.io.*;
import com.google.common.base.*;

import com.creativeartie.writerstudio.resource.*;

public class WindowAbout extends Stage{

    /// %Part 1: Constructor and Class Fields

    protected static final int WIDTH = 650;
    protected static final int HEIGHT = 500;
    private static final String APACHE = getResource(FileResources.getApacheLicense());
    private static final String BSD = getResource(FileResources.getBsdLicense());

    private static String getResource(URL stream){
        try {
            return Resources.asCharSource(stream, Charsets.UTF_8).read();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private TextArea licenseText;

    public WindowAbout(){
        setTitle(WindowText.ABOUT_TITLE.getText());
        setResizable(false);
        setScene(buildScene());
        initModality(Modality.APPLICATION_MODAL);
    }

    private Scene buildScene(){
        Scene ans = new Scene(buildMainPane(), WIDTH, HEIGHT);
        ans.getStylesheets().add(FileResources.getAboutCss());
        return ans;
    }
    /// %Part 2: Layout

    private BorderPane buildMainPane(){
        BorderPane pane = new BorderPane();

        pane.setTop(buildCreditPane());
        pane.setCenter(buildLicenseText());

        return pane;
    }

    private FlowPane buildCreditPane(){
        Label name = new Label(WindowText.PROGRAM_NAME.getText());
        name.getStyleClass().add("title");

        Hyperlink license = buildLicenseLink(WindowText.ABOUT_LICENSE, APACHE);

        FlowPane lib = build3rdPartyPane();

        FlowPane top = new FlowPane(Orientation.VERTICAL, name, license, lib);
        top.setAlignment(Pos.CENTER);
        top.getStyleClass().add("top");
        return top;
    }

    private FlowPane build3rdPartyPane(){
        return new FlowPane(
            buildLicenseLink(WindowText.ABOUT_GUAVA, APACHE),
            buildLicenseLink(WindowText.ABOUT_RICH_TEXT, BSD),
            buildLicenseLink(WindowText.ABOUT_PDF_BOX, APACHE)
        );
    }

    private TextArea buildLicenseText(){
        licenseText = new TextArea();
        licenseText.setEditable(false);
        licenseText.setPrefColumnCount(80);
        licenseText.setPrefRowCount(80);
        licenseText.setWrapText(true);
        return licenseText;
    }

    private Hyperlink buildLicenseLink(WindowText title, String text){
        Hyperlink ans = new Hyperlink(title.getText());
        ans.setOnAction(evt -> licenseText.setText(text));

        return ans;
    }
}
