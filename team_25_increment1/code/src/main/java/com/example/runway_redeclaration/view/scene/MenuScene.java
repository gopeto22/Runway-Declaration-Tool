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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MenuScene extends BaseScene {

    /**
     * Menu Scene Logger.
     */
    private static final Logger logger = LogManager.getLogger(StartScene.class);

    /**
     * Button to redeclare a runway.
     */
    private Button redeclareRunwayButton;

    /**
     * Button to report an obstacle.
     */
    private Button reportObstacleButton;

    /**
     * Button to review an obstacle.
     */
    private Button reviewObstacleButton;

    /**
     * Button to view changelog.
     */
    private Button changelogButton;

    /**
     * Create a new scene, passing in the window the scene will be displayed in
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public MenuScene(RunwayWindow runwayWindow) {
        super(runwayWindow);

        logger.info("Creating menu scene.");
    }

    @Override
    public void initialise() {
        // Check for esc key.
        scene.setOnKeyPressed((esc) -> {
            if (esc.getCode().equals(KeyCode.ESCAPE)) {
                // Exit app
                App.getInstance().shutdown();
            }
        });
    }

    @Override
    public void build() {
    /*
    Root
     */
        root = new RunwayPane(runwayWindow.getWidth(), runwayWindow.getHeight());

        var menuPane = new StackPane();
        menuPane.setMaxWidth(runwayWindow.getWidth());
        menuPane.setMaxHeight(runwayWindow.getHeight());

        root.getChildren().add(menuPane);

        var mainPane = new BorderPane();
        menuPane.getChildren().add(mainPane);

    /*
    UI Elements
     */
        // Add buttons for navigation.
        var menuVBox = new VBox();
        menuVBox.setSpacing(10);
        menuVBox.setPadding(new Insets(4, 4, 4, 4));
        menuVBox.setAlignment(Pos.TOP_CENTER);

        // Redeclare runway button.
        redeclareRunwayButton = new Button("Redeclare a runway");

        // Report obstacle button.
        reportObstacleButton = new Button("Report an obstacle");

        // Review obstacle button
        reviewObstacleButton = new Button("Review obstacles");

        // Changelog button
        changelogButton = new Button("Changelog");

        // Add elements to the VBox.
        menuVBox.getChildren()
                .addAll(redeclareRunwayButton, reportObstacleButton, reviewObstacleButton, changelogButton);

        // Add components to the screen.
        mainPane.setTop(menuVBox);

    /*
    Button Actions
     */
        redeclareRunwayButton.setOnAction(this::startRedeclare);
        reportObstacleButton.setOnAction(this::startReport);
        reviewObstacleButton.setOnAction(this::startReview);
        changelogButton.setOnAction(this::startChangelog);
    }

    private void startRedeclare(ActionEvent event) {
        runwayWindow.startRedeclare();
    }

    private void startReport(ActionEvent event) {
        runwayWindow.startReport();
    }

    private void startReview(ActionEvent event) {
        runwayWindow.startReview();
    }

    private void startChangelog(ActionEvent event) {
        runwayWindow.startChangelog();
    }
}
