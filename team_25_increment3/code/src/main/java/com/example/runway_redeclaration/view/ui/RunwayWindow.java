package com.example.runway_redeclaration.view.ui;

import com.example.runway_redeclaration.UIColor;
import com.example.runway_redeclaration.model.Runway;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.component.ObstacleDisplay;
import com.example.runway_redeclaration.view.component.RunwayBox;
import com.example.runway_redeclaration.view.scene.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class RunwayWindow {

    private final int width;
    private final int height;

    private final Stage stage;

    private BaseScene currentScene;
    private Scene scene;

    private File xmlFile;
    private UIColor uiColor;

    /**
     * Root element of the XML file. Used to initialise existing data.
     */
    private Element rootElement;

    /**
     * username of current user;
     */
    String username;

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
     * Create a new RunwayWindow attached to the given stage with the specified width and height.
     *
     * @param stage  stage
     * @param width  width
     * @param height height
     */
    public RunwayWindow(Stage stage, int width, int height, UIColor uiColor, Element rootElement) {
        this.width = width;
        this.height = height;
        this.stage = stage;
        this.uiColor = uiColor;
        this.rootElement = rootElement;

        // Setup stage.
        setupStage();

        // Setup resources.
        setupResources();

        // Setup default scene.
        setupDefaultScene();
    }

    /**
     * Initialise the root element of the XML doc.
     */
    private void initialiseRootElement() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(this.getXmlFile());
            doc.getDocumentElement().normalize();
            rootElement = doc.getDocumentElement();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public void initialiseUiColor() {
        initialiseRootElement();

        uiColor = new UIColor();

        if (rootElement.getElementsByTagName("color") == null) {
            return;
        }

        int i = 0;

        Element colorElement = (Element) rootElement.getElementsByTagName("color").item(i);

        while (colorElement != null) {
            String colorName = XMLParser.getTagAttributeElement(colorElement, "name", "name");
            String colorHex = XMLParser.getTagAttributeElement(colorElement, "hexcode", "code");
            Color color = Color.web(colorHex);

            switch(colorName) {
                case "Displaced Threshold":
                    uiColor.setDisplacedThresholdColor(color);
                    break;
                case "Obstacle":
                    uiColor.setObstacleColor(color);
                    break;
                case "Stopway":
                    uiColor.setStopwayColor(color);
                    break;
                case "Clearway":
                    uiColor.setClearwayColor(color);
                    break;
                case "TORA":
                    uiColor.setOriginalToraColor(color);
                    break;
                case "Obstacle Distance from Threshold":
                    uiColor.setObstacleDistanceThresholdColor(color);
                    break;
                case "Blast Distance":
                    uiColor.setBlastDistanceColor(color);
                    break;
                case "ALS":
                    uiColor.setAlsColor(color);
                    break;
                case "RESA":
                    uiColor.setResaColor(color);
                    break;
                case "Strip End":
                    uiColor.setStripEndColor(color);
                    break;
                case "TOCS":
                    uiColor.setSlopeColor(color);
                    break;
                case "Cleared and Graded":
                    uiColor.setClearedAndGradedColor(color);
                    break;
                case "Instrument Strip":
                    uiColor.setInstrumentStripColor(color);
                    break;
                default:
                    System.out.println("Couldn't identify parameter.");
            }

            colorElement = (Element) rootElement.getElementsByTagName("color").item(++i);
        }
    }

    /**
     * UI Color getter.
     *
     * @return the uiColor used for runway display
     */
    public UIColor getUiColor() {
        return uiColor;
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
   * Display the obstacle history scene.
   */
  public void startObsHistory(RunwayBox runwayBox) {
    loadScene(new ObstacleHistoryScene(this, runwayBox));
  }

  /**
   * Display the simulation scene.
   */
  public void startSimulation(Runway runway) {
    loadScene(new SimulationScene(this, runway));
  }

    /**
     * Display the report scene.
     */
    public void startReport() {
        loadScene(new ReportScene(this));
    }

    /**
     * Display the review scene.
     */
    public void startReview() {
        loadScene(new ReviewScene(this));
    }

    /**
     * Display the aircraft scene.
     */
    public void startAircraft() {
        loadScene(new AircraftScene(this));
    }

    /**
     * Display the changelog scene.
     */
    public void startChangelog() {
        loadScene(new ChangelogScene(this));
    }

    /**
     * Display the color palette scene.
     */
    public void startColor() {
        loadScene(new ColorPaletteScene(this));
    }

    /**
     * Display the modify obstacle screen
     */
    public void startModifyObstacle(ObstacleDisplay obstacleDisplay, String name, String height, String width, String length) {
        loadScene(new ModifyObstacleScene(this, obstacleDisplay, name, height, width, length));
    }

    /**
     * set username
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * return username;
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    public Element getRootElement() {
        return rootElement;
    }
}
