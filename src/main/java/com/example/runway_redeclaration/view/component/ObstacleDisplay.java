package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
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
     * notification controller
     */
    NotificationController notifControl;

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
    String obsLength;
    String obsWidth;

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
    Text widthText;
    Text lengthText;
    Text nameVar;
    Text heightVar;
    Text widthVar;
    Text lengthVar;

    /**
     * Constructor to create a new obstacle display box
     */
    public ObstacleDisplay(String name, String height, String width, String length, int number, RunwayWindow runwayWindow) {
        this.obsName = name;
        this.obsHeight = height;
        this.obsLength = length;
        this.obsWidth = width;
        this.number = number;
        this.runwayWindow = runwayWindow;

        notifControl = new NotificationController(runwayWindow);

    /*
    UI ELements
     */
        this.getStyleClass().add("componentbg");
        this.setPadding(new Insets(8,8,8,8));
        this.setSpacing(10);

        infoBox = new HBox();

        nameText = new Text("Name: ");
        nameText.getStyleClass().add("customtextnormal");
        nameVar = new Text(name);
        nameVar.getStyleClass().add("customtext");

        heightText = new Text("\t\tHeight (m): ");
        heightText.getStyleClass().add("customtextnormal");
        heightVar = new Text(height);
        heightVar.getStyleClass().add("customtext");

        widthText = new Text("\t\tWidth (m): ");
        widthText.getStyleClass().add("customtextnormal");
        widthVar = new Text(width);
        widthVar.getStyleClass().add("customtext");

        lengthText = new Text("\t\tLength (m): ");
        lengthText.getStyleClass().add("customtextnormal");
        lengthVar = new Text(length);
        lengthVar.getStyleClass().add("customtext");

        infoBox.getChildren().addAll(nameText, nameVar, heightText, heightVar, widthText, widthVar, lengthText, lengthVar);

        buttonBox = new HBox();
        buttonBox.setPadding(new Insets(4,4,4,4));
        buttonBox.setSpacing(5);

        modifyButton = new Button("Modify obstacle template");
        modifyButton.getStyleClass().add("midbutton");
        deleteButton = new Button("Delete obstacle template");
        deleteButton.getStyleClass().add("midbutton");

        buttonBox.getChildren().addAll(modifyButton, deleteButton);

        this.getChildren().addAll(infoBox, buttonBox);
    /*
    Events
     */
        modifyButton.setOnAction(this::modifyObstacleHandler);
        modifyButton.getStyleClass().add("smallbutton");
        deleteButton.setOnAction(this::removeObstacle);
        deleteButton.getStyleClass().add("smallbutton");
    }

    /**
     * Handles when the 'modify obstacle' button is pressed: opens a popup window so new values can be entered
     * @param event
     */
    private void modifyObstacleHandler(ActionEvent event) {
        // Add a new runway window and open the modify obstacle screen in a new window
        int width = 400;
        int height = 340;

        var newStage = new Stage();
        var newRunwayWindow = new RunwayWindow(newStage, width, height);
        newRunwayWindow.startModifyObstacle(this, obsName, obsHeight, obsWidth, obsLength);
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
        this.getStyleClass().remove("componentbg");
        this.getStyleClass().add("background");

        deleteObstacleFromXML();

        //notification
        notifControl.addNotif("Obstacle template deleted", "Obstacle template", "Obstacle template for " + obsName + " deleted");
    }

    /**
     * Update the variables of the obstacle
     */
    public void updateObstacle(String newName, String newHeight, String newWidth, String newLength){

        //notification
        notifControl.addNotif("Obstacle template modified", "Obstacle template", "Obstacle template for " + obsName + " modified:" +
                "\n\tNew name = " + newName + "\n\tNew height = " + newHeight);

        infoBox.getChildren().removeAll(nameText, nameVar, heightText, heightVar, widthText, widthVar, lengthText, lengthVar);

        obsName = newName;
        obsHeight = newHeight;
        obsWidth = newWidth;
        obsLength = newLength;
        nameVar = new Text(newName);
        nameVar.getStyleClass().add("customtext");
        heightVar = new Text(newHeight);
        heightVar.getStyleClass().add("customtext");
        widthVar = new Text(newWidth);
        widthVar.getStyleClass().add("customtext");
        lengthVar = new Text(newLength);
        lengthVar.getStyleClass().add("customtext");

        infoBox.getChildren().addAll(nameText, nameVar, heightText, heightVar, widthText, widthVar, lengthText, lengthVar);
    }

    /**
     * Update the XML file with the new values
     */
    public void updateObstacleInXml() {
        try {
            Document document;
            document = XMLParser.importXmlFile(runwayWindow.getXmlFile());

            //Node obstacle = document.getElementsByTagName("obstacle").item(number);
            int i = 0;
            Node obstacle = document.getElementsByTagName("obstacle").item(i);

            while (obstacle != null) {
                if (obstacle.getChildNodes().item(0).getAttributes().getNamedItem("name").getTextContent().equals(obsName)) {
                    break;
                }

                obstacle = document.getElementsByTagName("obstacle").item(++i);
            }

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

            Element obsWidthElement = document.createElement("width");
            obsWidthElement.setAttribute("metres", obsWidth);
            obstacle.appendChild(obsWidthElement);

            Element obsLengthElement = document.createElement("length");
            obsLengthElement.setAttribute("metres", obsLength);
            obstacle.appendChild(obsLengthElement);

            XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());
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

            //Node obstacle = document.getElementsByTagName("obstacle").item(number);
            int i = 0;
            Node obstacle = document.getElementsByTagName("obstacle").item(i);

            while (obstacle != null) {
                if (obstacle.getChildNodes().item(0).getAttributes().getNamedItem("name").getTextContent().equals(obsName)) {
                    obstacle.getParentNode().removeChild(obstacle);

                    XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());
                    System.out.println("XML updated");

                    return;
                }

                obstacle = document.getElementsByTagName("obstacle").item(++i);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
