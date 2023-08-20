package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.model.Obstacle;
import com.example.runway_redeclaration.model.Verifier;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Pair;


/**
 * The obstacle box class is used to represent obstacles for the runway box class.
 */
public class ObstacleBox extends HBox {

    /**
     * The obstacle represented by this obstacle box.
     */
    private Obstacle obstacle = null;

    /**
     * Text components
     */
    private Text nameText; // TODO: Change to XML drop down?
    private Text heightText; // TODO: Change to XML drop down?
    private Text nFromCentreText;
    private Text eFromCentreText;
    private Text distanceFromThresholdText;

    /**
     * TextField components
     */
    private TextField nameField;
    private TextField heightField;
    private TextField nFromCentreField;
    private TextField eFromCentreField;
    private TextField distanceFromThresholdField;

    /**
     * Constructor for the obstacle box.
     */
    public ObstacleBox() {
        this.getStyleClass().add("componentbg");

        nameText = new Text("Name:");
        heightText = new Text("Height (m):");
        nFromCentreText = new Text("North from centreline (m):");
        eFromCentreText = new Text("East from centreline (m):");
        distanceFromThresholdText = new Text("Distance from threshold (m):");

        nameField = new TextField();
        nameField.setDisable(true);
        heightField = new TextField();
        heightField.setDisable(true);
        nFromCentreField = new TextField();
        eFromCentreField = new TextField();
        distanceFromThresholdField = new TextField();

        this.getChildren()
                .addAll(nameText, nameField, heightText, heightField, nFromCentreText, nFromCentreField,
                        eFromCentreText,
                        eFromCentreField, distanceFromThresholdText, distanceFromThresholdField);
    }

    /**
     * Set the obstacle values to represent.
     */
    public void setObstacleValues(String name, String height, String nFromCentre, String eFromCentre,
                                  String distanceFromThreshold) {
        nameField.setText(name);
        heightField.setText(height);
        nFromCentreField.setText(nFromCentre);
        eFromCentreField.setText(eFromCentre);
        distanceFromThresholdField.setText(distanceFromThreshold);
    }

    /**
     * Error handled obstacle getter.
     *
     * @return the obstacle represented by this obstacle box
     */
    public Obstacle getObstacle() {
        var name = nameField.getText();
        var height = heightField.getText();
        var nFromCentre = nFromCentreField.getText();
        var eFromCentre = eFromCentreField.getText();
        var distanceFromThreshold = distanceFromThresholdField.getText();

        // Verify that all int fields are in the correct format.
        var verifyPair = verifyObstacleValues();

        if (verifyPair.getValue()) {
            return new Obstacle(name, Integer.parseInt(height), Integer.parseInt(nFromCentre),
                    Integer.parseInt(eFromCentre), Integer.parseInt(distanceFromThreshold));
        }

        // If not verified, show the error and return null as the runway, since it cannot be successfully created.
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setContentText(verifyPair.getKey());
        errorAlert.showAndWait();

        return null;
    }

    /**
     * Verify the obstacle values.
     *
     * @return pair of error message, verified
     */
    public Pair<String, Boolean> verifyObstacleValues() {
        // Error handling.
        Verifier verifier = new Verifier();

        List<Pair<String, String>> intFields = new ArrayList<>();
        intFields.add(new Pair<>(heightText.getText(), heightField.getText()));
        intFields.add(new Pair<>(nFromCentreText.getText(), nFromCentreField.getText()));
        intFields.add(new Pair<>(eFromCentreText.getText(), eFromCentreField.getText()));
        intFields.add(new Pair<>(distanceFromThresholdText.getText(), distanceFromThresholdField.getText()));

        return verifier.verifyAllIntFormat(intFields);
    }

    /**
     * Name value getter.
     *
     * @return the name of the object this obstacle box is representing
     */
    public String getNameValue() {
        return nameField.getText();
    }

    /**
     * Height value getter.
     *
     * @return the height of the object this obstacle box is representing
     */
    public String getHeightValue() {
        return heightField.getText();
    }

    /**
     * nFromCentre value getter.
     *
     * @return north distance from centreline
     */
    public String getNFromCentreValue() {
        return nFromCentreField.getText();
    }

    /**
     * eFromCentre value getter.
     *
     * @return east distance from centreline
     */
    public String getEFromCentreValue() {
        return eFromCentreField.getText();
    }

    /**
     * distanceFromThreshold value getter.
     *
     * @return distance of the obstacle from the runway threshold (start of the runway)
     */
    public String getDistanceFromThresholdValue() {
        return distanceFromThresholdField.getText();
    }

    /**
     * Set the name of the obstacle.
     *
     * @param name of the obstacle
     */
    public void setName(String name) {
        nameField.setText(name);
    }

    /**
     * Set the height of the obstacle.
     *
     * @param height in metres
     */
    public void setHeight(String height) {
        heightField.setText(height);
    }
}
