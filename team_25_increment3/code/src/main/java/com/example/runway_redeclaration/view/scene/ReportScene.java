package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;

/**
 * The redeclare scene is used to input details known about the airport/runways to redeclare a
 * runway.
 */
public class ReportScene extends BaseScene {

    /**
     * notification controller
     */
    NotificationController notifControl = new NotificationController(runwayWindow);

    private TextField nameField;
    private TextField heightField;
    private TextField widthField;
    private TextField lengthField;

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
        root.getStyleClass().add("background");

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
        menuVBox.setAlignment(Pos.TOP_CENTER);

        // Title
        var reportTitle = new Text("REPORT");
        reportTitle.getStyleClass().add("bigtitle");

        // Add custom toolbar with back button
        var reportBorderPane = new BorderPane();
        var toolbar = new HBox();
        toolbar.setPadding(new Insets(10));
        toolbar.setSpacing(10);

        var backButton = new Button("Back");
        backButton.getStyleClass().add("midbutton");
        backButton.setOnAction(e -> {
            runwayWindow.startMenu();
        });

        reportBorderPane.setLeft(toolbar);
        reportBorderPane.setCenter(reportTitle);
        toolbar.getChildren().add(backButton);

        // Create labels and textFields to display obstacle data
        Text nameLabelValue = new Text("Name of obstacle: ");
        nameLabelValue.getStyleClass().add("customtext");
        //nameLabelValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        //nameLabelValue.setTextFill(Color.BLACK);

        nameField = new TextField();

        Text widthLabelValue = new Text("Width of obstacle (m): ");
        widthLabelValue.getStyleClass().add("customtext");
        //widthLabelValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        //widthLabelValue.setTextFill(Color.BLACK);

        Text lengthLabelValue = new Text("Length of obstacle (m): ");
        lengthLabelValue.getStyleClass().add("customtext");
        //lengthLabelValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        //lengthLabelValue.setTextFill(Color.BLACK);

        Text heightLabelValue = new Text("Height of obstacle (m): ");
        heightLabelValue.getStyleClass().add("customtext");
        //heightLabelValue.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
        //heightLabelValue.setTextFill(Color.BLACK);

        widthField = new TextField("");
        //widthField.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

        lengthField = new TextField("");
        //lengthField.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

        heightField = new TextField("");
        //heightField.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));

        // Add labels and text fields to their corresponding HBoxes
        //nameInfoBox.getChildren().add(nameInfo);

        var reportGridPane = new GridPane();
        reportGridPane.getStyleClass().add("componentbg");
        reportGridPane.setAlignment(Pos.CENTER);
        reportGridPane.setPadding(new Insets(6,6,6,6));
        reportGridPane.setVgap(10);

        reportGridPane.addRow(0, nameLabelValue, nameField);
        reportGridPane.addRow(1, widthLabelValue, widthField);
        reportGridPane.addRow(2, heightLabelValue, heightField);
        reportGridPane.addRow(3, lengthLabelValue, lengthField);

        // Create a new save button
        Button saveButton = new Button("Save obstacle template to system");
        saveButton.getStyleClass().add("midbutton");

        menuVBox.getChildren().addAll(reportBorderPane, reportGridPane, saveButton);

        // Set the menu VBox as the center component of the main pane
        mainPane.setTop(menuVBox);

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
            String length;
            String width;

            //check for optional fields
            Boolean hasWidth = false;
            if (widthField.getText().isEmpty()){
                width = height;
            }
            else {
                width = widthField.getText();
                hasWidth = true;
            }

            Boolean hasLength = false;
            if (lengthField.getText().isEmpty()){
                length = height;
            }
            else {
                length = lengthField.getText();
                hasLength = true;
            }

            // string with fields of the obstacle
            String info = "Name: " + name + "\nHeight: " + height;
            if (hasLength){
                info = info + "\nLength: " + length;
            } else {
                info = info + "\nLength (default): " + length;
            }
            if (hasWidth) {
                info = info + "\nWidth: " + width;
            } else {
                info = info + "\nWidth (default): " + width;
            }

            // Check if name contains only string and digits characters
            if (!name.matches("[a-zA-Z]|[a-zA-Z]+[a-zA-Z0-9' ']+")) {
                // Display an error message if the name is invalid
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Name");
                alert.setContentText("The name must contain only letters/numbers and start\nwith a letter.");
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
            confirmation.setContentText(info);

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
                nameElement.setAttribute("name", name);
                obstacle.appendChild(nameElement);

                System.out.println("Adding the height subnode.");
                Element heightElement = document.createElement("height");
                heightElement.setAttribute("metres", height);
                obstacle.appendChild(heightElement);

                System.out.println("Adding the width subnode.");
                Element widthElement = document.createElement("width");
                widthElement.setAttribute("metres", width);
                obstacle.appendChild(widthElement);

                System.out.println("Adding the length subnode.");
                Element lengthElement = document.createElement("length");
                lengthElement.setAttribute("metres", length);
                obstacle.appendChild(lengthElement);

                System.out.println("Adding the newly defined obstacle to the obstacles node.");
                obstacles.appendChild(obstacle);

                // Save the document to the XML file
                XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());

                // Show a success message
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText("Obstacle added successfully");
                success.showAndWait();

                //notification
                notifControl.addNotif("Obstacle template added", "Obstacle template", "Obstacle template for " + name + " added");
            }
        });
    }
}
