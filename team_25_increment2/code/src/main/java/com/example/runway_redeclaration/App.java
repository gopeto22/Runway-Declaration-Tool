package com.example.runway_redeclaration;

import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX Application class
 */
public class App extends Application {

  /**
   * Base resolution width
   */
  private final int width = 1080;

  /**
   * Base resolution height
   */
  private final int height = 720;

  private static App instance;
  private Stage stage;

  /**
   * Start the game
   *
   * @param args commandline arguments
   */
  public static void main(String[] args) {
    launch();
  }

  /**
   * Called by JavaFX with the primary stage as a parameter. Begins the game by opening the Game
   * RunwayWindow
   *
   * @param stage the default stage, main runwayWindow
   */
  @Override
  public void start(Stage stage) {
    instance = this;
    this.stage = stage;

    // Open runway window
    openApp();
  }

  /**
   * Create the RunwayWindow with the specified width and height
   */
  public void openApp() {
    // Change the width and height in this class to change the base rendering resolution for all app parts
    var window = new RunwayWindow(stage, width, height);

    // Display the runway window
    stage.show();
  }

  /**
   * Shutdown the app
   */
  public void shutdown() {
    System.exit(0);
  }

  /**
   * Get the singleton App instance
   *
   * @return the app
   */
  public static App getInstance() {
    return instance;
  }

}
