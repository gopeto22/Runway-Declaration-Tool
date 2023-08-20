package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.view.component.ObstacleDisplay;
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
import javafx.stage.Stage;

/**
 * The changelog screen will allow the user to modify the parameters of an obstacle
 */
public class ModifyObstacleScene extends BaseScene {

    /**
     * The obstacle display that this is modifying
     */
    private ObstacleDisplay obstacleDisplay;

    /**
     * Variables
     */
    String name;
    String height;

    /**
     * Textfields
     */
    TextField nNameField;
    TextField nHeightField;

    /**
     * Create a new scene, passing in the window the scene will be displayed in
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public ModifyObstacleScene(RunwayWindow runwayWindow, ObstacleDisplay obstacleDisplay, String name, String height) {
        super(runwayWindow);
        this.obstacleDisplay = obstacleDisplay;
        this.name = name;
        this.height = height;
    }

    /**
     * Initialise this scene. Called after creation
     */
    @Override
    public void initialise() {
        // Check for esc key.
        scene.setOnKeyPressed((esc) -> {
            if (esc.getCode().equals(KeyCode.ESCAPE)) {
                // Close this stage.
                ((Stage) scene.getWindow()).close();
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

        var redeclarePane = new StackPane();
        redeclarePane.setMaxWidth(runwayWindow.getWidth());
        redeclarePane.setMaxHeight(runwayWindow.getHeight());

        root.getChildren().add(redeclarePane);
        root.getStyleClass().add("background");

        var mainPane = new BorderPane();
        redeclarePane.getChildren().add(mainPane);

        // Add components to the main pane
        var modifyVBox = new VBox();
        modifyVBox.setSpacing(10);
        modifyVBox.setPadding(new Insets(4, 4, 4, 4));
        modifyVBox.setAlignment(Pos.TOP_CENTER);

        //Display current values of obstacle
        Text currentText = new Text("Current values of obstacle:");
        Text nameText = new Text("Name:\t" + name);
        Text heightText = new Text("Height:\t" + height);

        //Display text + textfields for new values
        Text newText = new Text("New values for obstacle:");
        var nNameBox = new HBox();
        Text nNameText = new Text("New name:\t");
        nNameField = new TextField(name);
        nNameBox.getChildren().addAll(nNameText, nNameField);
        var nHeightBox = new HBox();
        Text nHeightText = new Text("New height (metres):\t");
        nHeightField = new TextField(height);
        nHeightBox.getChildren().addAll(nHeightText, nHeightField);

        //Add cancel/OK buttons
        var buttonBox = new HBox();
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("smallbutton");
        Button okButton = new Button("Ok");
        okButton.getStyleClass().add("smallbutton");
        buttonBox.getChildren().addAll(cancelButton, okButton);

        modifyVBox.getChildren().addAll(currentText,nameText,heightText,newText,nNameBox,nHeightBox,buttonBox);
        mainPane.setCenter(modifyVBox);

        //Handle buttons
        cancelButton.setOnAction(this::closeWindow);
        okButton.setOnAction(this::modifyConfirmed);
    }

    /**
     * Close the scene when the cancel button is pressed
     * @param event
     */
    private void closeWindow(ActionEvent event) {
        ((Stage) scene.getWindow()).close();
    }

    /**
     * Passes new values to the obstacle display when ok button is pressed, then closes window
     * @param event
     */
    private void modifyConfirmed(ActionEvent event) {
        // Check if name contains only string characters and digits
        if (!nNameField.getText().matches("[a-zA-Z0-9]+")) {
            // Display an error message if the name is invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Name");
            alert.setContentText("The name must contain only letters and digits.");
            alert.showAndWait();
            return;
        }
        // Check if height contains only digits
        else if (!nHeightField.getText().matches("[0-9]+")) {
            // Display an error message if the name is invalid
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Height");
            alert.setContentText("The height must contain only digits.");
            alert.showAndWait();
            return;
        }
        else {
            obstacleDisplay.updateObstacle(nNameField.getText(), nHeightField.getText());
            obstacleDisplay.updateObstacleInXml();
            ((Stage) scene.getWindow()).close();
        }
    }
}

