package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.component.ObstacleDisplay;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ReviewScene extends BaseScene {

  /**
   * notification controller
   */
  NotificationController notifControl = new NotificationController(runwayWindow);

  /**
   * XML doc
   */
  Document document;

  /**
   * VBox which holds all the runwayBoxes
   */
  private VBox obstaclesVBox;

  /**
   * Root element of the XML file. Used to initialise existing data.
   */
  private Element rootElement;

  /**
   * Create a new scene, passing in the window the scene will be displayed in
   *
   * @param runwayWindow the runwayWindow to pass in
   */
  public ReviewScene(RunwayWindow runwayWindow) {
    super(runwayWindow);
  }

  /**
   * Initialise this scene. Called after creation
   */
  @Override
  public void initialise() {
    // Check for esc key
    scene.setOnKeyPressed((esc) -> {
      if (esc.getCode().equals(KeyCode.ESCAPE)) {
        // Go back to app menu screen.
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
        root.getStyleClass().add("background");

        var mainPane = new BorderPane();
        redeclarePane.getChildren().add(mainPane);

    /*
        UI Elements
         */
        var menuVBox = new VBox();
        menuVBox.setSpacing(10);
        menuVBox.setPadding(new Insets(4, 4, 4, 4));
        menuVBox.setAlignment(Pos.TOP_CENTER);

        // Title
        var reviewTitle = new Text("REVIEW");
        reviewTitle.getStyleClass().add("bigtitle");

        // Scrollpane for obstacles
        var obstacleScrollpane = new ScrollPane();
        obstacleScrollpane.getStyleClass().add("background");
        obstacleScrollpane.setFitToWidth(true);

        //VBox for runways
        obstaclesVBox = new VBox();
        obstaclesVBox.setSpacing(70);
        obstaclesVBox.setPadding(new Insets(4, 4, 4, 4));
        obstaclesVBox.setAlignment(Pos.TOP_CENTER);
        obstaclesVBox.getStyleClass().add("nohighlight");

        obstacleScrollpane.setContent(obstaclesVBox);
        mainPane.setCenter(obstacleScrollpane);

    /*
     Initialisation of existing data from the XML file.
     */
        initialiseRootElement();
        initialiseObstacles();

        // Add custom toolbar with back button
        var reviewBorderPane = new BorderPane();
        var toolbar = new HBox();
        toolbar.setPadding(new Insets(10));
        toolbar.setSpacing(10);

        var backButton = new Button("Back");
        backButton.getStyleClass().add("midbutton");
        backButton.setOnAction(e -> {
            runwayWindow.startMenu();
        });

        //button for restoring default obstacles
        var restoreButton = new Button("Reset default obstacle templates");
        restoreButton.getStyleClass().add("midbutton");
        restoreButton.setOnAction(this::restoreDefault);

        toolbar.getChildren().addAll(backButton, restoreButton);
        reviewBorderPane.setLeft(toolbar);
        reviewBorderPane.setCenter(reviewTitle);
        mainPane.setTop(reviewBorderPane);
    }

    /**
     * Initialise the root element of the XML doc.
     */
    private void initialiseRootElement() {
        try {
            document = XMLParser.importXmlFile(runwayWindow.getXmlFile());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(runwayWindow.getXmlFile());
            doc.getDocumentElement().normalize();
            rootElement = doc.getDocumentElement();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise the existing obstacles in the XML document.
     */
    private void initialiseObstacles() {
        if (rootElement.getElementsByTagName("obstacle") == null) {
            return;
        }

        int i = 0;

        Element obstacleElement = (Element) rootElement.getElementsByTagName("obstacle").item(i);

        while (obstacleElement != null) {
            // Get this obstacles attributes
            String name = XMLParser.getTagAttributeElement(obstacleElement, "name", "name");
            String height = XMLParser.getTagAttributeElement(obstacleElement, "height", "metres");
            String width = XMLParser.getTagAttributeElement(obstacleElement, "width", "metres");
            String length = XMLParser.getTagAttributeElement(obstacleElement, "length", "metres");

            ObstacleDisplay newOD = new ObstacleDisplay(name, height, width, length, i, runwayWindow);
            obstaclesVBox.getChildren().add(newOD);

            obstacleElement = (Element) rootElement.getElementsByTagName("obstacle").item(++i);
        }
    }

    /**
     * Restore default obstacles to the file
     */
    private void restoreDefault(ActionEvent event) {

        //delete all obstacles from XML
        if (document.getElementsByTagName("obstacle") != null) {

            Node obstacles = document.getElementsByTagName("obstacles").item(0);
            while (obstacles.hasChildNodes()) {
                obstacles.removeChild(obstacles.getFirstChild());
            }
        }

        //add default obstacles
        addObsToXML("Shipping Container", "3","2","2");
        addObsToXML("Small Aircraft", "3","11","8");
        addObsToXML("Construction Crane", "80","10","10");
        addObsToXML("Semitruck Trailer", "4","3","14");
        addObsToXML("Luggage Cart", "2","1","2");

        XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());

        //refresh obstacle displays
        obstaclesVBox.getChildren().clear();
        initialiseRootElement();
        initialiseObstacles();

        //notification
        notifControl.addNotif("Default obstacles restored", "Obstacle template", "Restored default obstacle templates to file");
    }

    public void addObsToXML(String name, String height, String width, String length) {
        // Create the new obstacle node.
        Node obstacles = document.getElementsByTagName("obstacles").item(0);

        Element obstacle = document.createElement("obstacle");

        Element nameElement = document.createElement("name");
        nameElement.setAttribute("name", name);
        obstacle.appendChild(nameElement);

        Element heightElement = document.createElement("height");
        heightElement.setAttribute("metres", height);
        obstacle.appendChild(heightElement);

        Element widthElement = document.createElement("width");
        widthElement.setAttribute("metres", width);
        obstacle.appendChild(widthElement);

        Element lengthElement = document.createElement("length");
        lengthElement.setAttribute("metres", length);
        obstacle.appendChild(lengthElement);

        obstacles.appendChild(obstacle);
    }

}
