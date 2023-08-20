package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

/**
 * A Base Scene used in the game. Handles common functionality between all scenes.
 */
public abstract class BaseScene {

  protected final RunwayWindow runwayWindow;
  protected RunwayPane root;
  protected Scene scene;

  /**
   * Create a new scene, passing in the window the scene will be displayed in
   *
   * @param runwayWindow the runwayWindow to pass in
   */
  public BaseScene(RunwayWindow runwayWindow) {
    this.runwayWindow = runwayWindow;
  }

  /**
   * Initialise this scene. Called after creation
   */
  public abstract void initialise();

  /**
   * Build the layout of the scene
   */
  public abstract void build();

  /**
   * Create a new JavaFX scene using the root contained within this scene
   *
   * @return JavaFX scene
   */
  public Scene setScene() {
    var previous = runwayWindow.getScene();
    Scene scene = new Scene(root, previous.getWidth(), previous.getHeight(), Color.BLACK);
    scene.getStylesheets().add(getClass().getResource("/styles/common.css").toExternalForm());
    this.scene = scene;
    return scene;
  }

  /**
   * Get the JavaFX scene contained inside
   *
   * @return JavaFX scene
   */
  public Scene getScene() {
    return this.scene;
  }

}
