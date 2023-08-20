package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * The changelog screen will display a history of notifications within the app.
 */
public class ChangelogScene extends BaseScene {

  /**
   * Create a new scene, passing in the window the scene will be displayed in
   *
   * @param runwayWindow the runwayWindow to pass in
   */
  public ChangelogScene(RunwayWindow runwayWindow) {
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
    /*
    Root
     */
    root = new RunwayPane(runwayWindow.getWidth(), runwayWindow.getHeight());

    var redeclarePane = new StackPane();
    redeclarePane.setMaxWidth(runwayWindow.getWidth());
    redeclarePane.setMaxHeight(runwayWindow.getHeight());

    root.getChildren().add(redeclarePane);

    var mainPane = new BorderPane();
    redeclarePane.getChildren().add(mainPane);
  }
}
