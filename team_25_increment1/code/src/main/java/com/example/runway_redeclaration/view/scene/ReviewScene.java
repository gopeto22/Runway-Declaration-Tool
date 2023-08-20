package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.component.ObstacleDisplay;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ReviewScene extends BaseScene {

  /**
   * VBox which holds all the runwayBoxes
   */
  private VBox obsatclesVBox;

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

    var mainPane = new BorderPane();
    redeclarePane.getChildren().add(mainPane);

    /*
        UI Elements
         */
    var menuVBox = new VBox();
    menuVBox.setSpacing(10);
    menuVBox.setPadding(new Insets(4, 4, 4, 4));
    menuVBox.setAlignment(Pos.TOP_CENTER);


    //VBox for runways
    obsatclesVBox = new VBox();
    obsatclesVBox.setSpacing(10);
    obsatclesVBox.setPadding(new Insets(4, 4, 4, 4));
    obsatclesVBox.setAlignment(Pos.TOP_CENTER);
    mainPane.setCenter(obsatclesVBox);

    /*
     Initialisation of existing data from the XML file.
     */
    initialiseRootElement();
    initialiseObstacles();
  }
  /**
   * Initialise the root element of the XML doc.
   */
  private void initialiseRootElement() {
    try {
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

      ObstacleDisplay newOD = new ObstacleDisplay(name, height, i, runwayWindow);
      obsatclesVBox.getChildren().add(newOD);

      obstacleElement = (Element) rootElement.getElementsByTagName("obstacle").item(++i);
    }
  }
}
