package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.App;
import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Paths;

import static javafx.scene.text.TextAlignment.CENTER;

/**
 * The start menu of the app. Provides a gateway to the rest of the app by either importing an
 * existing XML file or creating a new one.
 */
public class StartScene extends BaseScene {

    /**
     * notification controller
     */
    NotificationController notifControl = new NotificationController(runwayWindow);

    /**
     * Start Scene Logger.
     */
    private static final Logger logger = LogManager.getLogger(StartScene.class);

    /**
     * Button to import a new XML file.
     */
    private Button importXMLButton;

    /**
     * File chooser to choose an XML file.
     */
    private FileChooser existingFileChooser;

    /**
     * Button to start a new blank XML file.
     */
    private Button newBlankFileButton;

    /**
     * field for user to enter name
     */
    private TextField nameField;

    /**
     * Create a new start scene
     *
     * @param runwayWindow the RunwayWindow this will be displayed in
     */
    public StartScene(RunwayWindow runwayWindow) {
        super(runwayWindow);

        logger.info("Creating menu scene.");
    }

    /**
     * Build the menu layout
     */
    @Override
    public void build() {
    /*
    Root
     */
        root = new RunwayPane(runwayWindow.getWidth(), runwayWindow.getHeight());

        var startPane = new StackPane();
        startPane.setMaxWidth(runwayWindow.getWidth());
        startPane.setMaxHeight(runwayWindow.getHeight());

        root.getChildren().add(startPane);
        root.getStyleClass().add("background");

        var mainPane = new BorderPane();
        startPane.getChildren().add(mainPane);

    /*
    UI Elements
     */
        //Title
        Text title = new Text("Runway Redeclaration Software");
        title.getStyleClass().add("bigtitle");

        Text subtitle = new Text("Group 25:\n" +
                "Kai Chevannes (kc2g21), " +
                "Bagir Bazarov (bb1u20), " +
                "Yuehua Yin (yy2g21),\n" +
                "Georgi Iliev (gdi1u21), " +
                "Nathan Fernandes (nf4g21), " +
                "Praveen Chandrarajah (pc2g21)");
        subtitle.getStyleClass().add("subtitle");
        subtitle.setTextAlignment(CENTER);

        // Add buttons for navigation.
        var startVBox = new VBox();
        startVBox.setSpacing(10);
        startVBox.setPadding(new Insets(4, 4, 4, 4));
        startVBox.setAlignment(Pos.TOP_CENTER);

        //Textfield for user to enter name
        var nameBox = new HBox();
        nameBox.setAlignment(Pos.TOP_CENTER);
        Text nameText = new Text("Enter username:");
        nameField = new TextField();
        nameBox.getChildren().addAll(nameText, nameField);

        // Import XML button.
        importXMLButton = new Button("Import XML file");
        importXMLButton.getStyleClass().add("menubutton");

        // New blank file button.
        newBlankFileButton = new Button("New blank file");
        newBlankFileButton.getStyleClass().add("menubutton");

        // Add elements to the VBox.
        startVBox.getChildren().addAll(title, subtitle, nameBox, importXMLButton, newBlankFileButton);

        // Add components to the screen.
        mainPane.setTop(startVBox);

    /*
    Initialise file chooser
     */
        existingFileChooser = new FileChooser();
        existingFileChooser.setTitle("Open XML file");

    /*
    Button Actions
     */
        importXMLButton.setOnAction(this::chooseFile);
        newBlankFileButton.setOnAction(this::startBlankFile);
    }

    /**
     * Choose an XML file.
     *
     * @param event import xml button pressed.
     */
    private void chooseFile(ActionEvent event) {
        //check if username entered
        if (nameField.getText().trim().isEmpty()) {
            // Display an error message if no username entered
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No username");
            alert.setContentText("Please enter a username in order to use the system.");
            alert.showAndWait();
            return;
        }

        File existingXmlFile = existingFileChooser.showOpenDialog(scene.getWindow());
        if (existingXmlFile != null) {
            runwayWindow.setXmlFile(existingXmlFile.getAbsoluteFile());

            runwayWindow.setUsername(nameField.getText());

            //add notification
            notifControl.addNotif("File select", "I/O", "Selected existing XML file");

            runwayWindow.startMenu();
        }
    }

    /**
     * Initialise the start screen.
     */
    @Override
    public void initialise() {
        // Check for esc key
        scene.setOnKeyPressed((esc) -> {
            if (esc.getCode().equals(KeyCode.ESCAPE)) {
                // Exit app
                App.getInstance().shutdown();
            }
        });
    }

    /**
     * Start the menu scene.
     *
     * @param event button pressed
     */
    private void startBlankFile(ActionEvent event) {
        // TODO: Refactor how this works, maybe build a new XML file using a document builder rather than copying one over.
        // TODO: Add XML generation method to XMLParser utility class.

        //check if username entered
        if (nameField.getText().trim().isEmpty()) {
            // Display an error message if no username entered
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No username");
            alert.setContentText("Please enter a username in order to use the system.");
            alert.showAndWait();
            return;
        }

        //File blankXmlFile = new File(Paths.get("").toAbsolutePath()
        //    + "/src/main/java/com/example/runway_redeclaration/blankAirport.xml");
        File blankXmlFile = new File(String.valueOf(Paths.get("").toAbsolutePath() + "/blankAirport.xml"));

        runwayWindow.setXmlFile(blankXmlFile.getAbsoluteFile());

        runwayWindow.setUsername(nameField.getText());

        try {
            File predefinedAirport = new File(Paths.get("").toAbsolutePath()
                    + "/src/main/java/com/example/runway_redeclaration/model/parser/predefinedAirport.xml");

            FileInputStream in = new FileInputStream(predefinedAirport);
            FileOutputStream out = new FileOutputStream(blankXmlFile);

            // Copy the predefinedAirport over to a new file.
            try {
                int n;

                // While there is still data in the existing file, write it to the blank file.
                while ((n = in.read()) != -1) {
                    out.write(n);
                }

                // Close the files.
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        runwayWindow.setXmlFile(blankXmlFile);

        //add notification
        notifControl.addNotif("File select", "I/O", "New blank XML file created");

        runwayWindow.startMenu();
    }
}

