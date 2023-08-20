package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.model.Runway;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * The redeclare scene is used to input details known about the airport/runways to redeclare a
 * runway.
 */
public class ReportScene extends BaseScene {

    /**
     * Redeclare Scene logger.
     */
    private static final Logger logger = LogManager.getLogger(StartScene.class);

    private TextField nameField;
    private TextField heightField;

    ArrayList<Runway> runwayArrayList = new ArrayList<>();

    /**
     * Create a new scene, passing in the window the scene will be displayed in
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public ReportScene(RunwayWindow runwayWindow) {
        super(runwayWindow);
    }

    /**
     * Initialise this scene. Called after creation
     */
    @Override
    public void initialise() {
        // Check for esc key.
        scene.setOnKeyPressed((esc) -> {
            if (esc.getCode().equals(KeyCode.ESCAPE)) {
                // Go back to the menu screen.
                runwayWindow.startMenu();
            }
        });
    }

    /**
     * Build the layout of the scene
     */
    @Override
    public void build() {

        // Create a new RunwayPane object with the width and height of runwayWindow.
        root = new RunwayPane(runwayWindow.getWidth(), runwayWindow.getHeight());

        // Create a new StackPane object for redeclarePane.
        var redeclarePane = new StackPane();

        // Set the maximum width and height of redeclarePane to match runwayWindow.
        redeclarePane.setMaxWidth(runwayWindow.getWidth());
        redeclarePane.setMaxHeight(runwayWindow.getHeight());

        // Add redeclarePane to the root pane.
        root.getChildren().add(redeclarePane);

        // Create a new BorderPane object for mainPane.
        var mainPane = new BorderPane();

        // Add mainPane to redeclarePane.
        redeclarePane.getChildren().add(mainPane);

        // Create a new DocumentBuilderFactory object.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Create a new DocumentBuilder object using the factory.
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // Throw a RuntimeException if there is a ParserConfigurationException.
            throw new RuntimeException(e);
        }

        // Create a new Document object by parsing an XML file.
        Document document;
        try {
            document = builder.parse(runwayWindow.getXmlFile());
        } catch (SAXException | IOException e) {
            // Throw a RuntimeException if there is a SAXException or IOException.
            throw new RuntimeException(e);
        }


    /*
    UI Elements
     */
        // Create a new VBox object for menuVBox.
        // Set the spacing, padding, and alignment of menuVBox.
        var menuVBox = new VBox();
        menuVBox.setSpacing(10);
        menuVBox.setPadding(new Insets(4, 4, 4, 4));
        menuVBox.setAlignment(Pos.CENTER);

        // Create a new HBox object for nameInfoBox.
        // Set the spacing, padding, and alignment of nameInfoBox.
        var nameInfoBox = new HBox();
        nameInfoBox.setSpacing(498);
        nameInfoBox.setPadding(new Insets(50, 0, 7, 0));
        nameInfoBox.setAlignment(Pos.CENTER);

        // Create a new HBox object for nameBox.
        // Set the spacing and padding of nameBox.
        var nameBox = new HBox();
        nameBox.setSpacing(750);
        nameBox.setPadding(new Insets(0, 0, 30, 0));

        // Create a new HBox object for coordInfoBox.
        // Set the spacing, padding, and alignment of coordInfoBox.
        var coordInfoBox = new HBox();
        coordInfoBox.setSpacing(498);
        coordInfoBox.setPadding(new Insets(0, 0, 7, 0));
        coordInfoBox.setAlignment(Pos.CENTER);

        // Create a new HBox object for nFromCenterBox.
        // Set the spacing and padding of nFromCenterBox.
        var nFromCenterBox = new HBox();
        nFromCenterBox.setSpacing(676);
        nFromCenterBox.setPadding(new Insets(0, 0, 5, 0));

        // Create a new HBox object for eFromCenterBox.
        // Set the spacing and padding of eFromCenterBox.
        var eFromCenterBox = new HBox();
        eFromCenterBox.setSpacing(690);
        eFromCenterBox.setPadding(new Insets(0, 0, 5, 0));

        // Create a new HBox object for distanceFromThresholdBox.
        // Set the spacing and padding of distanceFromThresholdBox.
        var distanceFromThresholdBox = new HBox();
        distanceFromThresholdBox.setSpacing(701);
        distanceFromThresholdBox.setPadding(new Insets(0, 0, 30, 0));

        // Create HBoxes for the obstacle's measurements and set their properties
        var measureInfoBox = new HBox();
        measureInfoBox.setSpacing(498);
        measureInfoBox.setPadding(new Insets(0, 0, 7, 0));
        measureInfoBox.setAlignment(Pos.CENTER);

        var widthBox = new HBox();
        widthBox.setSpacing(681);
        widthBox.setPadding(new Insets(0, 0, 5, 0));

        var heightBox = new HBox();
        heightBox.setSpacing(678);
        heightBox.setPadding(new Insets(0, 0, 5, 0));

        var lengthBox = new HBox();
        lengthBox.setSpacing(678);
        lengthBox.setPadding(new Insets(0, 0, 30, 0));

        // Create labels and text fields to display obstacle data
        Label nameInfo = new Label("Information about the name of the obstacle: ");
        nameInfo.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        nameInfo.setTextFill(Color.BLACK);

        // Create labels and textFields to display obstacle data
        Label nameLabelValue = new Label("Name of obstacle");
        nameLabelValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        nameLabelValue.setTextFill(Color.BLACK);

        nameField = new TextField();
        nameField.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));

        Label measureInfo = new Label("Information about the measures of the obstacle: ");
        measureInfo.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        measureInfo.setTextFill(Color.GREEN);

        Label widthLabelValue = new Label("width of obstacle (metres)");
        widthLabelValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        widthLabelValue.setTextFill(Color.GREEN);

        Label lengthLabelValue = new Label("length coordinate of obstacle (metres)");
        lengthLabelValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        lengthLabelValue.setTextFill(Color.GREEN);

        Label heightLabelValue = new Label("height coordinate of obstacle (metres)");
        heightLabelValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        heightLabelValue.setTextFill(Color.GREEN);

        TextField widthField = new TextField("");
        widthField.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));

        TextField lengthField = new TextField("");
        lengthField.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));

        heightField = new TextField("");
        heightField.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));

        // Add labels and text fields to their corresponding HBoxes
        nameInfoBox.getChildren().add(nameInfo);
        nameBox.getChildren().addAll(nameLabelValue, nameField);
        measureInfoBox.getChildren().add(measureInfo);
        widthBox.getChildren().addAll(widthLabelValue, widthField);
        heightBox.getChildren().addAll(heightLabelValue, heightField);
        lengthBox.getChildren().addAll(lengthLabelValue, lengthField);

        // Get the children components for the menu VBox and add them
        menuVBox.getChildren()
                .addAll(nameInfoBox, nameBox, coordInfoBox, nFromCenterBox, eFromCenterBox, distanceFromThresholdBox,
                        measureInfoBox, widthBox, heightBox, lengthBox);

        // Create a new save button
        Button saveButton = new Button("Save obstacle to system");

        // Add the new save button to the menu VBox
        menuVBox.getChildren().add(saveButton);

        // Set the menu VBox as the center component of the main pane
        mainPane.setCenter(menuVBox);

        // Create a VBox for the obstacle data
        VBox dataBox = new VBox();
        dataBox.setSpacing(10);
        dataBox.setPadding(new Insets(4, 4, 4, 4));
        dataBox.setAlignment(Pos.BOTTOM_CENTER);
        mainPane.setBottom(dataBox);

        saveButton.setOnAction(event -> {
            // Get the name and height of the obstacle from the text fields
            String name = nameField.getText();
            String height = heightField.getText();

            // Check if name contains only string and digits characters
            if (!name.matches("[a-zA-Z0-9]+")) {
                // Display an error message if the name is invalid
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Name");
                alert.setContentText("The name must contain only letters.");
                alert.showAndWait();
                return;
            }

            // Check if height contains only integers
            try {
                Integer.parseInt(height);
            } catch (NumberFormatException e) {
                // Display an error message if the height is invalid
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Height");
                alert.setContentText("The height must be a whole number.");
                alert.showAndWait();
                return;
            }

            // Create the confirmation window
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Are you sure you want to add this obstacle?");
            confirmation.setContentText(String.format("Name: %s\nHeight: %s", name, height));

            // Add the buttons to the confirmation window
            ButtonType acceptButton = new ButtonType("Accept");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmation.getButtonTypes().setAll(acceptButton, cancelButton);

            // Show the confirmation window and wait for a response
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == acceptButton) {
                // Validate obstacle name and height
                if (nameField.getText().isEmpty()) {
                    // Display an error message if the name is empty
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText("Obstacle name cannot be empty");
                    alert.showAndWait();
                    return;
                }

                try {
                    height = String.valueOf(Integer.parseInt(heightField.getText()));
                } catch (NumberFormatException e) {
                    // Display an error message if the height is not a valid number
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText("Obstacle height must be a valid number");
                    alert.showAndWait();
                    return;
                }

                // Create the new obstacle node.
                System.out.println("Creating obstacles element.");
                Node obstacles = document.getElementsByTagName("obstacles").item(0);

                System.out.println("Creating obstacle element.");
                Element obstacle = document.createElement("obstacle");

                System.out.println("Adding the name subnode.");
                Element nameElement = document.createElement("name");
                nameElement.setAttribute("name",name);
                obstacle.appendChild(nameElement);

                System.out.println("Adding the height subnode.");
                Element heightElement = document.createElement("height");
                heightElement.setAttribute("metres",height);
                obstacle.appendChild(heightElement);

                System.out.println("Adding the newly defined obstacle to the obstacles node.");
                obstacles.appendChild(obstacle);

                // Add the obstacle to the root element
                Element root = document.getDocumentElement();
                root.appendChild(obstacle);

                // Save the document to the XML file
                XMLParser.exportXmlFile(document,runwayWindow.getXmlFile());

                // Show a success message
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText("Obstacle added successfully");
                success.showAndWait();
            }
        });

        // Add custom toolbar with back button
        var toolbar = new HBox();
        toolbar.setPadding(new Insets(10));
        toolbar.setSpacing(10);

        var backButton = new Button("Back");
        backButton.setOnAction(e -> {
            runwayWindow.startMenu();
        });

        toolbar.getChildren().add(backButton);
        mainPane.setTop(toolbar);

    /*
    Button Actions
     */
        //newRunwayButton.setOnAction(this::newRunway);
    }

//  /**
//   * Add a new runwayBox to the scene.
//   *
//   * @param event new runway button clicked.
//   */
  /*private void newRunway(ActionEvent event) {
    System.out.println("new runway button pressed.");

    // Create a new runwayBox component.
    var obstacleBox = new ObstacleBox();

    // Setup runwayBox events.
    obstacleBox.setSimulateButtonAction(e -> {
      var obstacle = obstacleBox.getObstacle();
      if (obstacle != null) {
        // Add a new runway window and open the simulation in a separate window.
        // New window has default size of 800x600, should be appropriate.
        int width = 800;
        int height = 600;

        var newStage = new Stage();
        var newRunwayPane = new RunwayPane(200,200);
        newStage.show();
        newRunway(btnSubmit.setOnAction(e -> {
        }););

        //runwayWindow.startSimulation(runway,this);
      }
    });

    obstacleBox.setDeleteButtonAction(e -> {runwaysVBox.getChildren().remove(obstacleBox);});

    // Add the runwayBox to the list of runways.
    runwaysVBox.getChildren().add(obstacleBox);
  }*/
}
