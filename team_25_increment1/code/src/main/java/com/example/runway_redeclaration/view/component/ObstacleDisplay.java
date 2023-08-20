package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class ObstacleDisplay extends VBox{

    /**
     * The number of the obstacle in the XML file
     */
    int number;

    /**
     * The runway window
     */
    RunwayWindow runwayWindow;

    /**
     * Vars of the obstacle
     */
    String obsName;
    String obsHeight;

    /**
     * Button components
     */
    Button modifyButton;
    Button deleteButton;

    /**
     * Box components
     */
    HBox infoBox;
    HBox buttonBox;

    /**
     * Text variables for the obstacle
     */
    Text nameText;
    Text heightText;
    Text spaceLol;
    Text nameVar;
    Text heightVar;

    /**
     * Constructor to create a new obstacle display box
     */
    public ObstacleDisplay(String name, String height, int number, RunwayWindow runwayWindow) {
        this.obsName = name;
        this.obsHeight = height;
        this.number = number;
        this.runwayWindow = runwayWindow;

    /*
    UI ELements
     */
        infoBox = new HBox();

        nameText = new Text("Name: ");
        spaceLol = new Text("                     ");
        heightText = new Text("Height (m): ");

        nameVar = new Text(name);
        heightVar = new Text(height);

        infoBox.getChildren().addAll(nameText, nameVar, spaceLol, heightText, heightVar);

        buttonBox = new HBox();

        modifyButton = new Button("Modify obstacle");
        deleteButton = new Button("Delete obstacle");

        buttonBox.getChildren().addAll(modifyButton, deleteButton);

        this.getChildren().addAll(infoBox, buttonBox);
    /*
    Events
     */
        modifyButton.setOnAction(this::modifyObstacleHandler);
        deleteButton.setOnAction(this::removeObstacle);
    }

    /**
     * Handles when the 'modify obstacle' button is pressed: opens a popup window so new values can be entered
     * @param event
     */
    private void modifyObstacleHandler(ActionEvent event) {
        // Add a new runway window and open the modify obstacle screen in a new window
        int width = 400;
        int height = 300;

        var newStage = new Stage();
        var newRunwayWindow = new RunwayWindow(newStage, width, height);
        newRunwayWindow.startModifyObstacle(this, obsName, obsHeight);
        newStage.show();
    }

    /**
     * Remove this obstacle from the system GUI
     *
     * @param event remove obstacle from runway button clicked.
     */
    private void removeObstacle(ActionEvent event) {
        // Remove the obstacle box from the screen
        this.getChildren().remove(infoBox);
        this.getChildren().remove(buttonBox);
        infoBox = null;
        buttonBox = null;

        deleteObstacleFromXML();
    }

    /**
     * Update the variables of the obstacle
     */
    public void updateObstacle(String newName, String newHeight){
        infoBox.getChildren().removeAll(nameText, nameVar, spaceLol, heightText, heightVar);
        obsName = newName;
        obsHeight = newHeight;
        nameVar = new Text(newName);
        heightVar = new Text(newHeight);
        infoBox.getChildren().addAll(nameText, nameVar, spaceLol, heightText, heightVar);
    }

    /**
     * Update the XML file with the new values
     */
    public void updateObstacleInXml() {
        try {
            Document document;
            document = XMLParser.importXmlFile(runwayWindow.getXmlFile());

            Node obstacle = document.getElementsByTagName("obstacle").item(number);
            // Remove current obstacle attributes
            while (obstacle.hasChildNodes()) {
                obstacle.removeChild(obstacle.getFirstChild());
            }
            // Add the attributes back to file
            Element obsNameElement = document.createElement("name");
            obsNameElement.setAttribute("name", obsName);
            obstacle.appendChild(obsNameElement);

            Element obsHeightElement = document.createElement("height");
            obsHeightElement.setAttribute("metres", obsHeight);
            obstacle.appendChild(obsHeightElement);

            XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());
            System.out.println("XML updated");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DOMException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removing the corresponding data from the XML file
     */
    public void deleteObstacleFromXML() {
        try {
            Document document;
            document = XMLParser.importXmlFile(runwayWindow.getXmlFile());

            Node obstacle = document.getElementsByTagName("obstacle").item(number);
            obstacle.getParentNode().removeChild(obstacle);

            XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());
            System.out.println("XML updated");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
