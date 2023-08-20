package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
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
 * The aircraft scene will allow the user to change the aircraft information.
 */
public class AircraftScene extends BaseScene {

    /**
     * notification controller
     */
    NotificationController notifControl = new NotificationController(runwayWindow);

    /**
     * XML doc
     */
    Document document;

    /**
     * Text fields.
     */
    private TextField modelNameField;
    private TextField lengthField;
    private TextField wingspanField;
    private TextField heightField;
    private TextField fuelCapacityField;
    private TextField maxSpeedField;

    /**
     * Root element of the XML file. Used to initialise existing data.
     */
    private Element rootElement;

    /**
     * Create a new scene, passing in the window the scene will be displayed in
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public AircraftScene(RunwayWindow runwayWindow) {
        super(runwayWindow);
    }

    /**
     * Initialise this scene. Called after creation
     */
    @Override
    public void initialise() {
        // Check for esc key
        scene.setOnKeyPressed((esc) -> {
            if (esc.getCode().equals(KeyCode.ESCAPE)) {
                // Go back to app menu screen.
                runwayWindow.startMenu();
            }
        });
    }

    /**
     * Build the layout of the scene
     */
    @Override
    public void build() {
        /*
        Root
         */
        root = new RunwayPane(runwayWindow.getWidth(), runwayWindow.getHeight());

        var aircraftPane = new StackPane();
        aircraftPane.setMaxWidth(runwayWindow.getWidth());
        aircraftPane.setMaxHeight(runwayWindow.getHeight());

        root.getChildren().add(aircraftPane);
        root.getStyleClass().add("background");

        var mainPane = new BorderPane();
        aircraftPane.getChildren().add(mainPane);

        /*
        UI Elements
         */

        // Overall VBox
        var aircraftVBox = new VBox();
        aircraftVBox.setPadding(new Insets(4,4,4,4));
        aircraftVBox.setSpacing(5);
        aircraftVBox.setAlignment(Pos.CENTER);

        // Aircraft information gridpane.
        var aircraftGridpane = new GridPane();
        aircraftGridpane.getStyleClass().add("componentbg");
        aircraftGridpane.setAlignment(Pos.CENTER);
        aircraftGridpane.setPadding(new Insets(6,6,6,6));
        aircraftGridpane.setVgap(10);

        // Model
        var modelNameText = new Text("Model name: ");
        modelNameText.getStyleClass().add("customtext");
        modelNameField = new TextField();
        aircraftGridpane.addRow(0,modelNameText,modelNameField);

        // Length
        var lengthText = new Text("Length (m): ");
        lengthText.getStyleClass().add("customtext");
        lengthField = new TextField();
        aircraftGridpane.addRow(1,lengthText,lengthField);

        // Wingspan
        var wingspanText = new Text("Wingspan (m): ");
        wingspanText.getStyleClass().add("customtext");
        wingspanField = new TextField();
        aircraftGridpane.addRow(2,wingspanText,wingspanField);

        // Height
        var heightText = new Text("Height (m): ");
        heightText.getStyleClass().add("customtext");
        heightField = new TextField();
        aircraftGridpane.addRow(3,heightText,heightField);

        // Fuel capacity
        var fuelCapacityText = new Text("Fuel Capacity (m): ");
        fuelCapacityText.getStyleClass().add("customtext");
        fuelCapacityField = new TextField();
        aircraftGridpane.addRow(4,fuelCapacityText,fuelCapacityField);

        // Max speed.
        var maxSpeedText = new Text("Max Speed (m): ");
        maxSpeedText.getStyleClass().add("customtext");
        maxSpeedField = new TextField();
        aircraftGridpane.addRow(5,maxSpeedText,maxSpeedField);

        /*
        Toolbar
         */
        var aircraftBorderPane = new BorderPane();
        var toolbar = new HBox();
        toolbar.setPadding(new Insets(4,4,4,4));
        toolbar.setSpacing(5);

        // Title.
        var aircraftTitle = new Text("Aircraft");
        aircraftTitle.getStyleClass().add("bigtitle");

        // Back button.
        var backButton = new Button("Back");
        backButton.getStyleClass().add("midbutton");
        backButton.setOnAction(e -> {
            runwayWindow.startMenu();
        });

        // Reset to default button.
        var resetButton = new Button("Reset to Default");
        resetButton.getStyleClass().add("midbutton");
        toolbar.getChildren().addAll(backButton, resetButton);

        var saveButton = new Button("Save aircraft to system");
        saveButton.getStyleClass().add("midbutton");

        aircraftBorderPane.setLeft(toolbar);
        aircraftBorderPane.setCenter(aircraftTitle);

        aircraftVBox.getChildren().addAll(aircraftBorderPane, aircraftGridpane, saveButton);

        mainPane.setTop(aircraftVBox);

        initialiseRootElement();
        initialiseExistingAircraft();

        saveButton.setOnAction(this::saveAircraft);
        resetButton.setOnAction(this::resetAircraft);
    }

    /**
     * Reset this aircraft to default.
     *
     * @param event reset button clicked
     */
    private void resetAircraft(ActionEvent event) {
        /*
        Add predefined aircraft info.
         */
        initialiseRootElement();

        Node aircraft = document.getElementsByTagName("aircraft").item(0);
        // Remove current aircraft attributes.
        while (aircraft.hasChildNodes()) {
            aircraft.removeChild(aircraft.getFirstChild());
        }

        Element modelElement = document.createElement("model");
        modelElement.setAttribute("name","Boeing 737");
        aircraft.appendChild(modelElement);

        Element lengthElement = document.createElement("length");
        lengthElement.setAttribute("metres","28.65");
        aircraft.appendChild(lengthElement);

        Element wingspanElement = document.createElement("wingspan");
        wingspanElement.setAttribute("metres","28.35");
        aircraft.appendChild(wingspanElement);

        Element heightElement = document.createElement("height");
        heightElement.setAttribute("metres","11.28");
        aircraft.appendChild(heightElement);

        Element fuelCapacityElement = document.createElement("fuelcapacity");
        fuelCapacityElement.setAttribute("litres","17865");
        aircraft.appendChild(fuelCapacityElement);

        Element maxSpeedElement = document.createElement("maxspeed");
        maxSpeedElement.setAttribute("kmph","933");
        aircraft.appendChild(maxSpeedElement);

        // Export the file and start the menu.
        XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());

        initialiseExistingAircraft();
    }

    /**
     * Save this aircraft to XML.
     *
     * @param event save button clicked
     */
    private void saveAircraft(ActionEvent event) {
        System.out.println("Save button clicked.");
        // Check if the model name only contains string characters, digits, and spaces.
        if (!modelNameField.getText().matches("[a-zA-Z0-9' ']+")) {
            // Display an error message if the name is invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Name");
            alert.setContentText("The name must contain only letters and digits.");
            alert.showAndWait();
            return;
        }
        // Check if the length is a valid double.
        try {
            if (Double.parseDouble(lengthField.getText()) < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Length");
                alert.setContentText("The length must be greater than 0.");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Length");
            alert.setContentText("The length must be a valid number (including decimals).");
            alert.showAndWait();
            return;
        }
        // Check if the wingspan is a valid double.
        try {
            if (Double.parseDouble(wingspanField.getText()) < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Wingspan");
                alert.setContentText("The wingspan must be greater than 0.");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Wingspan");
            alert.setContentText("The wingspan must be a valid number (including decimals).");
            alert.showAndWait();
            return;
        }
        // Check if the height is a valid double.
        try {
            if (Double.parseDouble(heightField.getText()) < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Height");
                alert.setContentText("The height must be greater than 0.");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Height");
            alert.setContentText("The height must be a valid number (including decimals).");
            alert.showAndWait();
            return;
        }
        // Check if the fuel capacity is a valid double.
        try {
            if (Double.parseDouble(fuelCapacityField.getText()) < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Fuel Capacity");
                alert.setContentText("The fuel capacity must be greater than 0.");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Fuel Capacity");
            alert.setContentText("The fuel capacity must be a valid number (including decimals).");
            alert.showAndWait();
            return;
        }
        // Check if the max speed is a valid double.
        try {
            if (Double.parseDouble(maxSpeedField.getText()) < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Max Speed");
                alert.setContentText("The max speed must be greater than 0.");
                alert.showAndWait();
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Max Speed");
            alert.setContentText("The max speed must be a valid number (including decimals).");
            alert.showAndWait();
            return;
        }

        // Write the updates to the file.
        try {
            Document document;
            document = XMLParser.importXmlFile(runwayWindow.getXmlFile());

            /*
            Add aircraft info.
             */
            Node aircraft = document.getElementsByTagName("aircraft").item(0);
            // Remove current aircraft attributes.
            while (aircraft.hasChildNodes()) {
                aircraft.removeChild(aircraft.getFirstChild());
            }

            Element modelElement = document.createElement("model");
            modelElement.setAttribute("name",modelNameField.getText());
            aircraft.appendChild(modelElement);

            Element lengthElement = document.createElement("length");
            lengthElement.setAttribute("metres",lengthField.getText());
            aircraft.appendChild(lengthElement);

            Element wingspanElement = document.createElement("wingspan");
            wingspanElement.setAttribute("metres",wingspanField.getText());
            aircraft.appendChild(wingspanElement);

            Element heightElement = document.createElement("height");
            heightElement.setAttribute("metres",heightField.getText());
            aircraft.appendChild(heightElement);

            Element fuelCapacityElement = document.createElement("fuelcapacity");
            fuelCapacityElement.setAttribute("litres",fuelCapacityField.getText());
            aircraft.appendChild(fuelCapacityElement);

            Element maxSpeedElement = document.createElement("maxspeed");
            maxSpeedElement.setAttribute("kmph",maxSpeedField.getText());
            aircraft.appendChild(maxSpeedElement);

            // Create the confirmation window
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Are you sure you want to save these changes?");

            // Add the buttons to the confirmation window
            ButtonType acceptButton = new ButtonType("Accept");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmation.getButtonTypes().setAll(acceptButton, cancelButton);

            // Show the confirmation window and wait for a response
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == acceptButton) {
                XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());

                System.out.println("Sending notification");

                notifControl.addNotif("Aircraft information modified","Aircraft details",
                        "Aircraft modified:" + "\n\tNew model name = " + modelNameField.getText() +
                                "\n\tNew length = " + lengthField.getText() + " metres" +
                                "\n\tNew wingspan = " + wingspanField.getText() + " metres" +
                                "\n\tNew height = " + heightField.getText() + " metres" +
                                "\n\tNew fuel capacity = " + fuelCapacityField.getText() + " litres" +
                                "\n\tNew max speed = " + maxSpeedField.getText() + " km/h");
            } else {
                return;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise the existing aircraft.
     */
    private void initialiseExistingAircraft() {
        // Always only one aircraft element.
        Element aircraftElement = (Element) rootElement.getElementsByTagName("aircraft").item(0);

        String modelName = XMLParser.getTagAttributeElement(aircraftElement, "model","name");
        String length = XMLParser.getTagAttributeElement(aircraftElement,"length","metres");
        String wingspan = XMLParser.getTagAttributeElement(aircraftElement, "wingspan", "metres");
        String height = XMLParser.getTagAttributeElement(aircraftElement, "height", "metres");
        String fuelCapacity = XMLParser.getTagAttributeElement(aircraftElement, "fuelcapacity","litres");
        String maxSpeed = XMLParser.getTagAttributeElement(aircraftElement, "maxspeed", "kmph");

        modelNameField.setText(modelName);
        lengthField.setText(length);
        wingspanField.setText(wingspan);
        heightField.setText(height);
        fuelCapacityField.setText(fuelCapacity);
        maxSpeedField.setText(maxSpeed);
    }

    /**
     * Initialise the root element of the XML doc.
     */
    private void initialiseRootElement() {
        try {
            document = XMLParser.importXmlFile(runwayWindow.getXmlFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(runwayWindow.getXmlFile());
            doc.getDocumentElement().normalize();
            rootElement = doc.getDocumentElement();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
