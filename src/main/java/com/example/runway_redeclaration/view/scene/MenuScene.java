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
import javafx.scene.text.Text;
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
     * Button to change the colours of the runway parameters
     */
    private Button colorPaletteButton;

    /**
     * Button to change the information about the pilots aircraft.
     */
    private Button aircraftButton;

    /**
     * Button to view changelog.
     */
    private Button changelogButton;

    /**
     * Button to exit the application.
     */
    private Button exitButton;

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
        root.getStyleClass().add("background");

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

        // Menu title
        var menuTitle = new Text("MENU");
        menuTitle.getStyleClass().add("bigtitle");

        // Review obstacle button
        reviewObstacleButton = new Button(" Review obstacle templates ");
        reviewObstacleButton.getStyleClass().add("menubutton");

        // Redeclare runway button.
        redeclareRunwayButton = new Button("       Redeclare a runway       ");
        redeclareRunwayButton.getStyleClass().add("menubutton");

        // Report obstacle button.
        reportObstacleButton = new Button("        Report an obstacle        ");
        reportObstacleButton.getStyleClass().add("menubutton");

        // Colour palette button
        colorPaletteButton = new Button("    Change runway colours    ");
        colorPaletteButton.getStyleClass().add("menubutton");

        // Aircraft information button
        aircraftButton = new Button("Change aircraft information");
        aircraftButton.getStyleClass().add("menubutton");

        // Changelog button
        changelogButton = new Button("               Changelog               ");
        changelogButton.getStyleClass().add("menubutton");

        // Exit button
        exitButton = new Button("                     Exit                     ");
        exitButton.getStyleClass().add("menubutton");

        // Add elements to the VBox.
        menuVBox.getChildren()
                .addAll(menuTitle, redeclareRunwayButton, reportObstacleButton, reviewObstacleButton, colorPaletteButton, aircraftButton , changelogButton, exitButton);

        // Add components to the screen.
        mainPane.setTop(menuVBox);

    /*
    Button Actions
     */
        redeclareRunwayButton.setOnAction(this::startRedeclare);
        reportObstacleButton.setOnAction(this::startReport);
        reviewObstacleButton.setOnAction(this::startReview);
        changelogButton.setOnAction(this::startChangelog);
        aircraftButton.setOnAction(this::startAircraft);
        colorPaletteButton.setOnAction(this::startColor);
        exitButton.setOnAction(this::exit);
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

    private void startAircraft(ActionEvent event) { runwayWindow.startAircraft(); }

    private void startColor(ActionEvent event) { runwayWindow.startColor();}

    private void exit(ActionEvent event) { App.getInstance().shutdown();};
}
