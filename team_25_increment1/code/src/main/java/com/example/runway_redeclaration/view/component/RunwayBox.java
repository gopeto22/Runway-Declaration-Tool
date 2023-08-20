package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.model.Runway;
import com.example.runway_redeclaration.model.Verifier;
import com.example.runway_redeclaration.model.parser.XMLParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RunwayBox component for a user to input values and then simulate a runway at a given Airport.
 */
public class RunwayBox extends VBox {

    /**
     * XML File for this airport, used to get obstacles.
     */
    File xmlFile;

    /**
     * Runway this RunwayBox is representing.
     */
    Runway runway;

    /**
     * The ObstacleBox for a potential obstacle to be added.
     */
    ObstacleBox obstacleBox = null;

    /**
     * Combo box to store obstacles from the XML file.
     */
    ComboBox<String> obstacleComboBox;

    /**
     * HBox to store buttons.
     */
    private HBox buttonHBox;

    /**
     * HBox to store text fields for runway info.
     */
    private HBox infoHBox;

    /**
     * Textfield components.
     */
    TextField designatorField;
    TextField toraField;
    TextField clearwayField;
    TextField stopwayField;
    TextField displacedThresholdField;

    /**
     * Button components.
     */
    Button addObstacleButton;
    Button removeObstacleButton;
    Button simulateButton;
    Button deleteButton;

    /**
     * Text components.
     */
    Text designatorText;
    Text toraText;
    Text clearwayText;
    Text stopwayText;
    Text displacedThresholdText;

    /**
     * Root element.
     */
    Element rootElement;

    /**
     * RunwayBox constructor.
     */
    public RunwayBox(File xmlFile) {
    /*
    Initialisation
     */
        this.xmlFile = xmlFile;
        obstacleComboBox = new ComboBox<>();
        initialiseRootElement();
        initialiseObstacleComboBox();

    /*
    UI ELements
     */
        infoHBox = new HBox();

        designatorText = new Text("Designator:");
        toraText = new Text("TORA (m):");
        clearwayText = new Text("Clearway (m):");
        stopwayText = new Text("Stopway (m):");
        displacedThresholdText = new Text("Displaced threshold (m):");

        designatorField = new TextField();
        toraField = new TextField();
        clearwayField = new TextField();
        stopwayField = new TextField();
        displacedThresholdField = new TextField();

        infoHBox.getChildren()
                .addAll(designatorText, designatorField, toraText, toraField, clearwayText, clearwayField,
                        stopwayText, stopwayField, displacedThresholdText, displacedThresholdField);

        buttonHBox = new HBox();

        addObstacleButton = new Button("Add obstacle");
        simulateButton = new Button("Simulate runway");
        deleteButton = new Button("Delete runway");

        buttonHBox.getChildren().addAll(simulateButton, addObstacleButton, deleteButton);

        this.getChildren().addAll(infoHBox, buttonHBox);

    /*
    Events
     */
        addObstacleButton.setOnAction(this::addObstacleHandler);
        obstacleComboBox.setOnAction(this::updateObstacleBox);
    }

    /**
     * Set runway parameters.
     *
     * @param designator         runway name
     * @param tora               runway tora
     * @param clearway           runway clearway
     * @param stopway            runway stopway
     * @param displacedThreshold runway displacedThreshold
     */
    public void setRunwayValues(String designator, String tora, String clearway, String stopway, String displacedThreshold) {
        designatorField.setText(designator);
        toraField.setText(tora);
        clearwayField.setText(clearway);
        stopwayField.setText(stopway);
        displacedThresholdField.setText(displacedThreshold);
    }

    /**
     * Set and update UI for a predefined obstacleBox
     *
     * @param obstacleBox to be displayed
     */
    public void setObstacleBox(ObstacleBox obstacleBox) {
        this.obstacleBox = obstacleBox;
        addObstacleToRunway(obstacleBox);
        obstacleComboBox.setValue(obstacleBox.getNameValue() + " - " + obstacleBox.getHeightValue() + "m");
    }

    /**
     * Initialise the root element of the XML doc.
     */
    private void initialiseRootElement() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            rootElement = doc.getDocumentElement();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise the obstacle combobox with values from the XML.
     */
    private void initialiseObstacleComboBox() {
        int i = 0;
        Element obstacleElement = (Element) rootElement.getElementsByTagName("obstacle").item(i);

        while (obstacleElement != null) {
            // Get this obstacles name and height.
            String obstacleName = XMLParser.getTagAttributeElement(obstacleElement, "name", "name");
            String obstacleHeight = XMLParser.getTagAttributeElement(obstacleElement, "height", "metres");

            // Add the obstacle to the combobox.
            obstacleComboBox.getItems().add(obstacleName + " - " + obstacleHeight + "m");

            // Increment i before getting the item, otherwise we duplicate the first item.
            obstacleElement = (Element) rootElement.getElementsByTagName("obstacle").item(++i);
        }
    }

    /**
     * Update the name and height of the obstacle box.
     *
     * @param event combobox changed value.
     */
    private void updateObstacleBox(ActionEvent event) {
        // When there is no value, do nothing.
        if (obstacleComboBox.getValue() == null) {
            return;
        }

        // Otherwise:
        var obstacleData = obstacleComboBox.getValue().split("-");
        String obstacleName = obstacleData[0].trim();
        String obstacleHeight = obstacleData[1].trim();
        obstacleBox.setName(obstacleName);
        obstacleBox.setHeight(obstacleHeight.substring(0, obstacleHeight.length() - 1));
    }

    /**
     * Set the simulate button action handler.
     *
     * @param simulateHandler handler to call when the simulate button is pressed.
     */
    public void setSimulateButtonAction(EventHandler<ActionEvent> simulateHandler) {
        simulateButton.setOnAction(simulateHandler);
    }

    /**
     * Set the delete button action handler.
     *
     * @param deleteHandler handler to call when the delete button is pressed.
     */
    public void setDeleteButtonAction(EventHandler<ActionEvent> deleteHandler) {
        deleteButton.setOnAction(deleteHandler);
    }

    /**
     * Function to add an obstacle to the runway. Choose an object from a predefined list specifying
     * north value from the centreline of the runway, east value from the centreline of the runway and
     * the distance from the runway threshold.
     *
     * @param event add obstacle button pressed
     */
    private void addObstacleHandler(ActionEvent event) {
        // Create a new obstacle box.
        obstacleBox = new ObstacleBox();
        addObstacleToRunway(obstacleBox);
    }

    private void addObstacleToRunway(ObstacleBox obstacleBox) {
        this.getChildren().add(1, obstacleBox);
        this.getChildren().add(2, obstacleComboBox);

        // Add the remove obstacle button.
        removeObstacleButton = new Button("Remove obstacle");
        removeObstacleButton.setOnAction(this::removeObstacleFromRunway);

        buttonHBox.getChildren().remove(addObstacleButton);
        buttonHBox.getChildren().add(1, removeObstacleButton);
    }

    /**
     * Remove an obstacle from a runway.
     *
     * @param event remove obstacle from runway button clicked.
     */
    private void removeObstacleFromRunway(ActionEvent event) {
        // Remove the obstacle box.
        this.getChildren().remove(obstacleBox);
        this.getChildren().remove(obstacleComboBox);
        obstacleComboBox.setValue(null);
        obstacleBox = null;

        // Add back addObstacleButton.
        buttonHBox.getChildren().remove(removeObstacleButton);
        buttonHBox.getChildren().add(1, addObstacleButton);
    }


    /**
     * Error handled runway getter.
     *
     * @return the runway represented by this RunwayBox
     */
    public Runway getRunway() {
        var designator = designatorField.getText();
        var tora = toraField.getText();
        var clearway = clearwayField.getText();
        var stopway = stopwayField.getText();
        var displacedThreshold = displacedThresholdField.getText();

        // Verify that all int fields are the correct format.
        var verifyPair = verifyRunwayFields();

        if (verifyPair.getValue()) {
            runway = new Runway(designator, Integer.parseInt(tora), Integer.parseInt(clearway),
                    Integer.parseInt(stopway), Integer.parseInt(displacedThreshold));

            if (obstacleBox != null) {
                runway.addObstacle(obstacleBox.getObstacle());
            }

            return runway;
        }

        // If not verified, show the error and return null as the runway, since it cannot be successfully created.
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setContentText(verifyPair.getKey());
        errorAlert.showAndWait();

        return null;
    }

    /**
     * Verify the runway fields
     *
     * @return pair containing error message, bool for verified or not
     */
    public Pair<String, Boolean> verifyRunwayFields() {
        // Error handling
        Verifier verifier = new Verifier();

        List<Pair<String, String>> intFields = new ArrayList<>();
        intFields.add(new Pair<>(toraText.getText(), toraField.getText()));
        intFields.add(new Pair<>(clearwayText.getText(), clearwayField.getText()));
        intFields.add(new Pair<>(stopwayText.getText(), stopwayField.getText()));
        intFields.add(new Pair<>(displacedThresholdText.getText(), displacedThresholdField.getText()));

        return verifier.verifyAllIntFormat(intFields);
    }

    /**
     * Getters
     */
    public String getDesignatorValue() {
        return designatorField.getText();
    }

    public String getToraValue() {
        return toraField.getText();
    }

    public String getClearwayValue() {
        return clearwayField.getText();
    }

    public String getStopwayValue() {
        return stopwayField.getText();
    }

    public String getDisplacedThresholdValue() {
        return displacedThresholdField.getText();
    }

    public ObstacleBox getObstacleBox() {
        return obstacleBox;
    }
}
