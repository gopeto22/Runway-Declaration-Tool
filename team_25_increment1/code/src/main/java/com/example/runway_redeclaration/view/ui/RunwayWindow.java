package com.example.runway_redeclaration.view.ui;

import com.example.runway_redeclaration.model.Runway;
import com.example.runway_redeclaration.view.component.ObstacleDisplay;
import com.example.runway_redeclaration.view.scene.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class RunwayWindow {

  private final int width;
  private final int height;

  private final Stage stage;

  private BaseScene currentScene;
  private Scene scene;

  private File xmlFile;
  private Runway runway;


  /**
   * Create a new RunwayWindow attached to the given stage with the specified width and height.
   *
   * @param stage  stage
   * @param width  width
   * @param height height
   */
  public RunwayWindow(Stage stage, int width, int height) {
    this.width = width;
    this.height = height;

    this.stage = stage;

    // Setup stage.
    setupStage();

    // Setup resources.
    setupResources();

    // Setup default scene.
    setupDefaultScene();

    // Go to start screen.
    startStart();
  }

  /**
   * xmlFile setter.
   *
   * @param xmlFile the file to set
   */
  public void setXmlFile(File xmlFile) {
    this.xmlFile = xmlFile;
  }

  /**
   * xmlFile getter.
   *
   * @return the xmlFile this application is working from
   */
  public File getXmlFile() {
    return xmlFile;
  }

  /**
   * Set up the font and any other resources we need.
   */
  private void setupResources() {
  }


  /**
   * Set up the default settings for the stage itself (the runwayWindow), such as the title and
   * minimum width and height.
   */
  public void setupStage() {
    stage.setTitle("Runway Redeclaration");
    stage.setMinWidth(width);
    stage.setMinHeight(height + 20);
    //stage.setOnCloseRequest(ev -> App.getInstance().shutdown());
  }

  /**
   * Load a given scene which extends BaseScene and switch over.
   *
   * @param newScene new scene to load
   */
  public void loadScene(BaseScene newScene) {
    // Cleanup remains of the previous scene.
    cleanup();

    // Create the new scene and set it up.
    newScene.build();
    currentScene = newScene;
    scene = newScene.setScene();
    stage.setScene(scene);

    // Initialise the scene when ready.
    Platform.runLater(() -> currentScene.initialise());
  }

  public void showScene(BaseScene newScene) {
    // Cleanup remains of the previous scene.
    cleanup();

    // Show the new scene, already built.
    currentScene = newScene;
    scene = newScene.setScene();
    stage.setScene(scene);
  }

  /**
   * Set up the default scene (an empty black scene) when no scene is loaded.
   */
  public void setupDefaultScene() {
    this.scene = new Scene(new Pane(), width, height, Color.BLACK);
    stage.setScene(this.scene);
  }

  /**
   * When switching scenes, perform any cleanup needed, such as removing previous listeners.
   */
  public void cleanup() {
  }

  /**
   * Get the current scene being displayed.
   *
   * @return scene
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Get the width of the RunwayWindow.
   *
   * @return width
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Get the height of the RunwayWindow.
   *
   * @return height
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * Display the start menu.
   */
  public void startStart() {
    loadScene(new StartScene(this));
  }

  /**
   * Display the main menu.
   */
  public void startMenu() {
    loadScene(new MenuScene(this));
  }

  /**
   * Display the redeclare scene.
   */
  public void startRedeclare() {
    loadScene(new RedeclareScene(this));
  }

  /**
   * Display the simulation scene.
   */
  public void startSimulation(Runway runway) {
    loadScene(new SimulationScene(this, runway));
  }

  /**
   * Display the report screen
   */
  public void startReport() {
    loadScene(new ReportScene(this));
  }

  /**
   * Display the review screen
   */
  public void startReview() {
    loadScene(new ReviewScene(this));
  }

  /**
   * Display the changelog
   */
  public void startChangelog() {
    loadScene(new ChangelogScene(this));
  }
  public Runway getRunway() {
    return this.runway;
  }

  /**
   * Display the modify obstacle screen
   */
  public void startModifyObstacle(ObstacleDisplay obstacleDisplay, String name, String height){
    loadScene(new ModifyObstacleScene(this, obstacleDisplay, name, height));
  }


}
