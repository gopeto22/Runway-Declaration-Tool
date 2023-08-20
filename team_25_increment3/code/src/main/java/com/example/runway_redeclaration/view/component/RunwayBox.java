package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.model.Runway;
import com.example.runway_redeclaration.model.Verifier;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * RunwayBox component for a user to input values and then simulate a runway at a given Airport.
 */
public class RunwayBox extends VBox {

    /**
     * notification controller
     */
    NotificationController notifControl;

    /**
     * XML File for this airport, used to get obstacles.
     */
    File xmlFile;

    /**
     * Runway this RunwayBox is representing.
     */
    Runway runway;

    /**
     * Runway Window
     */
    RunwayWindow runwayWindow;

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

    private boolean justRemoved;

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
    Button historyButton;

    /**
     * Text components.
     */
    Text designatorText;
    Text toraText;
    Text clearwayText;
    Text stopwayText;
    Text displacedThresholdText;

    /**
     * History of obstacles
     */
    String obstacleNameHistory = "";
    String obstacleTimeAddedHistory = "";
    String obstacleTimeDeletedHistory = "";

    /**
     * Root element.
     */
    Element rootElement;

    /**
     * RunwayBox constructor.
     */
    public RunwayBox(RunwayWindow window) {
    /*
    Initialisation
     */
        this.xmlFile = window.getXmlFile();
        this.runwayWindow = window;
        obstacleComboBox = new ComboBox<>();
        obstacleComboBox.getStyleClass().add("customtextblack");
        initialiseRootElement();
        initialiseObstacleComboBox();
        justRemoved = true;

    /*
    Set up notification controller
     */
        notifControl = new NotificationController(runwayWindow);

    /*
    UI ELements
     */
        this.getStyleClass().add("componentbg");
        this.setPadding(new Insets(4,4,4,4));
        this.setSpacing(5);

        //infoHBox = new HBox();
        var infoGridPane = new GridPane();

        var runwayText = new Text("Runway Details");
        runwayText.getStyleClass().add("subtitle");

        designatorText = new Text("Designator:");
        designatorText.getStyleClass().add("customtext");
        toraText = new Text("TORA (m):");
        toraText.getStyleClass().add("customtext");
        clearwayText = new Text(" Clearway (m): ");
        clearwayText.getStyleClass().add("customtext");
        stopwayText = new Text(" Stopway (m): ");
        stopwayText.getStyleClass().add("customtext");
        displacedThresholdText = new Text("Displaced threshold (m): ");
        displacedThresholdText.getStyleClass().add("customtext");

        designatorField = new TextField();
        toraField = new TextField();
        clearwayField = new TextField();
        stopwayField = new TextField();
        displacedThresholdField = new TextField();

        infoGridPane.addRow(0, designatorText, designatorField);
        infoGridPane.addRow(1, toraText, toraField, clearwayText, clearwayField, stopwayText, stopwayField);
        infoGridPane.addRow(2, displacedThresholdText, displacedThresholdField);
        //infoHBox.getChildren()
        //        .addAll(designatorText, designatorField, toraText, toraField, clearwayText, clearwayField,
        //                stopwayText, stopwayField, displacedThresholdText, displacedThresholdField);

        buttonHBox = new HBox();
        buttonHBox.setPadding(new Insets(5, 5, 5, 5));
        buttonHBox.setSpacing(5);

        addObstacleButton = new Button("Add obstacle");
        addObstacleButton.getStyleClass().add("smallbutton");
        simulateButton = new Button("Simulate runway");
        simulateButton.getStyleClass().add("smallbutton");
        deleteButton = new Button("Delete runway");
        deleteButton.getStyleClass().add("smallbutton");
        historyButton = new Button("Check obstacle history");
        historyButton.getStyleClass().add("smallbutton");

        buttonHBox.getChildren().addAll(simulateButton, addObstacleButton, deleteButton, historyButton);

        this.getChildren().addAll(runwayText,infoGridPane, buttonHBox);
        //this.getChildren().addAll(infoHBox, buttonHBox);

    /*
    Events
     */
        addObstacleButton.setOnAction(this::addObstacleHandler);
        obstacleComboBox.setOnAction(this::updateObstacleBox);
        historyButton.setOnAction(this::displayObstacleHistory);
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
    public void setRunwayValues(String designator, String tora, String clearway, String stopway, String displacedThreshold, String historyObsName, String historyObsAdd, String historyObsDelete) {
        designatorField.setText(designator);
        toraField.setText(tora);
        clearwayField.setText(clearway);
        stopwayField.setText(stopway);
        displacedThresholdField.setText(displacedThreshold);
        obstacleNameHistory = historyObsName;
        obstacleTimeAddedHistory = historyObsAdd;
        obstacleTimeDeletedHistory = historyObsDelete;
    }

    /**
     * Set and update UI for a predefined obstacleBox
     *
     * @param obstacleBox to be displayed
     */
    public void setObstacleBox(ObstacleBox obstacleBox) {
        this.obstacleBox = obstacleBox;
        addObstacleToRunway(obstacleBox);
        obstacleComboBox.setValue(obstacleBox.getNameValue() + " - " + obstacleBox.getHeightValue() + "m" + "-" + obstacleBox.getWidthValue() + "m" + "-" + obstacleBox.getLengthValue() + "m");
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
            String obstacleWidth = XMLParser.getTagAttributeElement(obstacleElement, "width", "metres");
            String obstacleLength = XMLParser.getTagAttributeElement(obstacleElement, "length", "metres");

            // Add the obstacle to the combobox.
            obstacleComboBox.getItems().add(obstacleName + " - " + obstacleHeight + "m" + "-" + obstacleWidth + "m" + "-" + obstacleLength + "m");

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
        String obstacleWidth = obstacleData[2].trim();
        String obstacleLength = obstacleData[3].trim();
        obstacleBox.setName(obstacleName);
        obstacleBox.setHeight(obstacleHeight.substring(0, obstacleHeight.length() - 1));
        obstacleBox.setWidth(obstacleWidth.substring(0, obstacleWidth.length() - 1));
        obstacleBox.setLength(obstacleLength.substring(0, obstacleLength.length() - 1));

        //add to history
        if (!justRemoved) {
            // Add the last obstacle if didnt remove and jsut changed
            obstacleTimeDeletedHistory += new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new java.util.Date()) + ";";
        }
        obstacleNameHistory += obstacleName + ";";
        obstacleTimeAddedHistory += new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new java.util.Date()) + ";";
        justRemoved = false;
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
        //notification
        notifControl.addNotif("Obstacle added", "Runway", "Obstacle added to runway " + designatorField.getText());

        // Create a new obstacle box.
        obstacleBox = new ObstacleBox();
        addObstacleToRunway(obstacleBox);
    }

    private void addObstacleToRunway(ObstacleBox obstacleBox) {
        this.getChildren().add(2, obstacleBox);
        this.getChildren().add(3, obstacleComboBox);

        // Add the remove obstacle button.
        removeObstacleButton = new Button("Remove obstacle");
        removeObstacleButton.getStyleClass().add("smallbutton");
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
        //notification
        notifControl.addNotif("Obstacle deleted", "Runway", "Obstacle deleted from runway " + designatorField.getText());

        // Remove the obstacle box.
        this.getChildren().remove(obstacleBox);
        this.getChildren().remove(obstacleComboBox);
        obstacleComboBox.setValue(null);
        obstacleBox = null;

        // Add back addObstacleButton.
        buttonHBox.getChildren().remove(removeObstacleButton);
        buttonHBox.getChildren().add(1, addObstacleButton);

        //Add to history
        obstacleTimeDeletedHistory += new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new java.util.Date()) + ";";
        justRemoved = true;
    }

    /**
     * Open new window showing obstacle history data
     * @param event
     */
    private void displayObstacleHistory(ActionEvent event) {
        var newStage = new Stage();
        var newRunwayWindow = new RunwayWindow(newStage, (int) this.getScene().getWidth(), (int) this.getScene().getHeight());
        newRunwayWindow.startObsHistory(this);
        newStage.show();
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

        try {
            if (Integer.parseInt(toraField.getText()) < 600) {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setContentText("TORA must be at least 600m");
                errorAlert.showAndWait();
                return null;
            }
        } catch (Exception e) {
            System.out.println("Tora too low.");
            // If not verified, show the error and return null as the runway, since it cannot be successfully created.
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setContentText("TORA must be a valid integer.");
            errorAlert.showAndWait();
            return null;
        }

        try {
            if (Integer.parseInt(displacedThresholdField.getText()) > Integer.parseInt(toraField.getText())) {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setContentText("Displaced Threshold cannot be greater than TORA.");
                errorAlert.showAndWait();
                return null;
            }
        } catch (Exception e) {
            // If not verified, show the error and return null as the runway, since it cannot be successfully created.
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setContentText("Displaced Threshold must be a valid integer.");
            errorAlert.showAndWait();
            return null;
        }

        try {
            if (Integer.parseInt(clearwayField.getText()) < 0) {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setContentText("Clearway must be a non-negative integer.");
                errorAlert.showAndWait();
                return null;
            }
        } catch (Exception e) {
            // If not verified, show the error and return null as the runway, since it cannot be successfully created.
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setContentText("Clearway must be a valid integer.");
            errorAlert.showAndWait();
            return null;
        }

        try {
            if (Integer.parseInt(stopwayField.getText()) < 0) {
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setContentText("Stopway must be a non-negative integer.");
                errorAlert.showAndWait();
                return null;
            }
        } catch (Exception e) {
            // If not verified, show the error and return null as the runway, since it cannot be successfully created.
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setContentText("Stopway must be a valid integer.");
            errorAlert.showAndWait();
            return null;
        }

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
     * Verify the designator of this runway.
     *
     * @return true if correct format, false otherwise
     */
    public Boolean verifyDesignator() {
        try {
            var designatorInt = Integer.parseInt(designatorField.getText().substring(0,2));
            if (designatorInt > 36) return false;
            if (designatorField.getText().length() > 3) {
                return false;
            }
            if (designatorField.getText().length() > 2) {
                var designatorChar = designatorField.getText().charAt(2);
                if ((designatorChar != 'L') && (designatorChar != 'C') && (designatorChar != 'R')) return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Couldn't verify designator.");
            return false;
        }
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

    public String getObstacleNameHistory() {
        return obstacleNameHistory;
    }

    public String getObstacleTimeAddedHistory() {
        return obstacleTimeAddedHistory;
    }

    public String getObstacleTimeDeletedHistory() {
        return obstacleTimeDeletedHistory;
    }
}
