package com.creativeartie.writerstudio.javafx;

import java.net.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.javafx.utils.*;

import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    WindowAboutConstants.*;

public class WindowAbout extends Stage{

    /// %Part 1: Constructor and Class Fields

    private TextArea licenseText;

    public WindowAbout(){
        setTitle(WINDOW_TITLE);
        setResizable(false);
        setScene(buildScene());
        initModality(Modality.APPLICATION_MODAL);
    }

    /// %Part 2: Layout

    /// %Part 2 (stage -> scene)

    private Scene buildScene(){
        Scene ans = new Scene(buildMainPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        ans.getStylesheets().add(FileResource.ABOUT_CSS.getCssPath());
        return ans;
    }

    /// %Part 2 (stage -> scene -> content)

    private BorderPane buildMainPane(){
        BorderPane pane = new BorderPane();

        pane.setTop(buildCreditPane());
        pane.setCenter(buildLicenseText());
        pane.setBottom(buildBottomPane());

        return pane;
    }

    /// %Part 2 (stage -> scene -> content -> top)

    private FlowPane buildCreditPane(){
        Label name = new Label(PROGRAM_NAME);
        name.getStyleClass().add(TITLE_STYLE);

        Hyperlink license = buildLicenseLink(LICENSE, APACHE);

        FlowPane lib = build3rdPartyPane();

        FlowPane top = new FlowPane(Orientation.VERTICAL, name, license, lib);
        top.setAlignment(Pos.CENTER);
        top.getStyleClass().add(TOP_STYLE);
        return top;
    }

    /// %Part 2 (stage -> scene -> content -> top -> credits - bottom)

    private FlowPane build3rdPartyPane(){
        return new FlowPane(
            buildLicenseLink(GUAVA, APACHE),
            buildLicenseLink(RICH_TEXT, BSD),
            buildLicenseLink(PDF_BOX, APACHE)
        );
    }

    private Hyperlink buildLicenseLink(String title, String text){
        Hyperlink ans = new Hyperlink(title);
        ans.setOnAction(evt -> licenseText.setText(text));

        return ans;
    }

    /// %Part 2 (stage -> scene -> content -> center)

    private TextArea buildLicenseText(){
        licenseText = new TextArea();
        licenseText.setEditable(false);
        licenseText.setPrefColumnCount(80);
        licenseText.setPrefRowCount(80);
        licenseText.setWrapText(true);
        return licenseText;
    }

    /// %Part 2 (stage -> scene -> content -> bottom)

    private TextFlow buildBottomPane(){
        Text text = new Text(SOURCE_TEXT);
        /* // Code failed for some reason...
        Hyperlink link = new Hyperlink(SOURCE_LINK);
        link.setOnAction(e -> Main.getApplication().getHostServices()
            .showDocument(SOURCE_LINK));
        */
        Text link = new Text(SOURCE_LINK);

        TextFlow pane = new TextFlow();
        pane.setTextAlignment(TextAlignment.CENTER);
        pane.getChildren().addAll(text, link);
        return pane;
    }
}
