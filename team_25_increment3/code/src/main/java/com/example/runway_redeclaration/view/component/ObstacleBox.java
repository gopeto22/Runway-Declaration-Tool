package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.model.Obstacle;
import com.example.runway_redeclaration.model.Verifier;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;


/**
 * The obstacle box class is used to represent obstacles for the runway box class.
 */
public class ObstacleBox extends VBox {

    /**
     * The obstacle represented by this obstacle box.
     */
    private Obstacle obstacle = null;

    /**
     * Text components
     */
    private Text nameText; // TODO: Change to XML drop down?
    private Text heightText; // TODO: Change to XML drop down?
    private Text lengthText;
    private Text widthText;
    private Text nFromCentreText;
    private Text eFromCentreText;
    private Text distanceFromThresholdText;
    private Text clearanceTimeText;

    /**
     * TextField components
     */
    private TextField nameField;
    private TextField heightField;
    private TextField lengthField;
    private TextField widthField;
    private TextField nFromCentreField;
    private TextField eFromCentreField;
    private TextField distanceFromThresholdField;
    private TextField clearanceTimeField;

    /**
     * Constructor for the obstacle box.
     */
    public ObstacleBox() {
        this.getStyleClass().add("componentbg");

        //HBox row1 = new HBox();
        var infoGridPane = new GridPane();

        var obstacleText = new Text("Obstacle Details");
        obstacleText.getStyleClass().add("subtitle");

        nameText = new Text("Obstacle Name: ");
        nameText.getStyleClass().add("customtext");
        nameField = new TextField();
        nameField.setDisable(true);

        heightText = new Text("Height (m): ");
        heightText.getStyleClass().add("customtext");
        heightField = new TextField();
        heightField.setDisable(true);

        widthText = new Text(" Width (m): ");
        widthText.getStyleClass().add("customtext");
        widthField = new TextField();
        widthField.setDisable(true);

        lengthText = new Text(" Length (m): ");
        lengthText.getStyleClass().add("customtext");
        lengthField = new TextField();
        lengthField.setDisable(true);

        //HBox row2 = new HBox();

        nFromCentreText = new Text("North from centreline (m): ");
        nFromCentreText.getStyleClass().add("customtext");
        nFromCentreField = new TextField();

        eFromCentreText = new Text(" East from centreline (m): ");
        eFromCentreText.getStyleClass().add("customtext");
        eFromCentreField = new TextField();

        distanceFromThresholdText = new Text("Distance from threshold (m): ");
        distanceFromThresholdText.getStyleClass().add("customtext");
        distanceFromThresholdField = new TextField();

        clearanceTimeText = new Text("Estimated clearance time (minutes) ");
        clearanceTimeText.getStyleClass().add("customtext");
        clearanceTimeField = new TextField();

        infoGridPane.addRow(0, nameText, nameField);
        infoGridPane.addRow(1, heightText, heightField, widthText, widthField,lengthText, lengthField);
        infoGridPane.addRow(2, nFromCentreText, nFromCentreField, eFromCentreText, eFromCentreField, distanceFromThresholdText, distanceFromThresholdField);
        infoGridPane.addRow(3, clearanceTimeText, clearanceTimeField);
        this.getChildren().addAll(obstacleText,infoGridPane);

        //row1.getChildren().addAll(nameText,nameField,heightText,heightField,widthText,widthField,lengthText,lengthField);
        //row2.getChildren().addAll(nFromCentreText, nFromCentreField, eFromCentreText, eFromCentreField, distanceFromThresholdText, distanceFromThresholdField);
        //this.getChildren().addAll(row1,row2);
    }

    /**
     * Set the obstacle values to represent.
     */
    public void setObstacleValues(String name, String height, String width, String length, String nFromCentre, String eFromCentre,
                                  String distanceFromThreshold, String clearanceTime) {
        nameField.setText(name);
        heightField.setText(height);
        widthField.setText(width);
        lengthField.setText(length);
        nFromCentreField.setText(nFromCentre);
        eFromCentreField.setText(eFromCentre);
        distanceFromThresholdField.setText(distanceFromThreshold);
        clearanceTimeField.setText(clearanceTime);
    }

    /**
     * Error handled obstacle getter.
     *
     * @return the obstacle represented by this obstacle box
     */
    public Obstacle getObstacle() {
        var name = nameField.getText();
        var height = heightField.getText();
        var width = widthField.getText();
        var length = lengthField.getText();
        var nFromCentre = nFromCentreField.getText();
        var eFromCentre = eFromCentreField.getText();
        var distanceFromThreshold = distanceFromThresholdField.getText();
        var clearanceTime = clearanceTimeField.getText();

        // Verify that all int fields are in the correct format.
        var verifyPair = verifyObstacleValues();

        if (verifyPair.getValue()) {
            return new Obstacle(name, Integer.parseInt(height), Integer.parseInt(width), Integer.parseInt(length), Integer.parseInt(nFromCentre),
                    Integer.parseInt(eFromCentre), Integer.parseInt(distanceFromThreshold), Integer.parseInt(clearanceTime));
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
        intFields.add(new Pair<>(widthText.getText(), widthField.getText()));
        intFields.add(new Pair<>(lengthText.getText(), lengthField.getText()));
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
     * get width and length
     * @return
     */
    public String getWidthValue() {
        return widthField.getText();
    }

    public String getLengthValue() {
        return lengthField.getText();
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

    public String getClearanceTimeValue() {
        return clearanceTimeField.getText();
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

    public void setWidth(String width) {
        widthField.setText(width);
    }

    public void setLength(String length) {
        lengthField.setText(length);
    }
}
