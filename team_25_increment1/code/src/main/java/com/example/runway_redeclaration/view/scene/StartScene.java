package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.App;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Paths;

/**
 * The start menu of the app. Provides a gateway to the rest of the app by either importing an
 * existing XML file or creating a new one.
 */
public class StartScene extends BaseScene {

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

    var mainPane = new BorderPane();
    startPane.getChildren().add(mainPane);

    /*
    UI Elements
     */
    // Add buttons for navigation.
    var startVBox = new VBox();
    startVBox.setSpacing(10);
    startVBox.setPadding(new Insets(4, 4, 4, 4));
    startVBox.setAlignment(Pos.TOP_CENTER);

    // Import XML button.
    importXMLButton = new Button("Import XML file");

    // New blank file button.
    newBlankFileButton = new Button("New blank file");

    // Add elements to the VBox.
    startVBox.getChildren().addAll(importXMLButton, newBlankFileButton);

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
    File existingXmlFile = existingFileChooser.showOpenDialog(scene.getWindow());
    if (existingXmlFile != null) {
      runwayWindow.setXmlFile(existingXmlFile.getAbsoluteFile());
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
    File blankXmlFile = new File(Paths.get("").toAbsolutePath()
        + "/src/main/java/com/example/runway_redeclaration/blankAirport.xml");

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
    runwayWindow.startMenu();
  }
}

