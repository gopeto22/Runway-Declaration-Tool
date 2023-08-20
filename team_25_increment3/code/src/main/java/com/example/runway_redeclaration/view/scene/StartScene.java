package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.App;
import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Paths;

import static javafx.scene.text.TextAlignment.CENTER;

/**
 * The start menu of the app. Provides a gateway to the rest of the app by either importing an
 * existing XML file or creating a new one.
 */
public class StartScene extends BaseScene {

    /**
     * notification controller
     */
    NotificationController notifControl = new NotificationController(runwayWindow);

    /**
     * Start Scene Logger.
     */
    private static final Logger logger = LogManager.getLogger(StartScene.class);

    /**
     * Button to import a new XML file.
     */
    private Button importXMLButton;

    /**
     * File chooser to choose an XML file.
     */
    private FileChooser existingFileChooser;

    /**
     * Button to start a new blank XML file.
     */
    private Button newBlankFileButton;

    /**
     * field for user to enter name
     */
    private TextField nameField;

    /**
     * Root element of the XML file. Used to initialise existing data.
     */
    private Element rootElement;

    /**
     * Create a new start scene
     *
     * @param runwayWindow the RunwayWindow this will be displayed in
     */
    public StartScene(RunwayWindow runwayWindow) {
        super(runwayWindow);

        logger.info("Creating menu scene.");
    }

    /**
     * Build the menu layout
     */
    @Override
    public void build() {
    /*
    Root
     */
        root = new RunwayPane(runwayWindow.getWidth(), runwayWindow.getHeight());

        var startPane = new StackPane();
        startPane.setMaxWidth(runwayWindow.getWidth());
        startPane.setMaxHeight(runwayWindow.getHeight());

        root.getChildren().add(startPane);
        root.getStyleClass().add("background");

        var mainPane = new BorderPane();
        startPane.getChildren().add(mainPane);

    /*
    UI Elements
     */
        //Title
        Text title = new Text("Runway Redeclaration Software");
        title.getStyleClass().add("bigtitle");

        Text subtitle = new Text("Group 25:\n" +
                "Kai Chevannes (kc2g21), " +
                "Bagir Bazarov (bb1u20), " +
                "Yuehua Yin (yy2g21),\n" +
                "Georgi Iliev (gdi1u21), " +
                "Nathan Fernandes (nf4g21), " +
                "Praveen Chandrarajah (pc2g21)");
        subtitle.getStyleClass().add("subtitle");
        subtitle.setTextAlignment(CENTER);

        // Add buttons for navigation.
        var startVBox = new VBox();
        startVBox.setSpacing(10);
        startVBox.setPadding(new Insets(4, 4, 4, 4));
        startVBox.setAlignment(Pos.TOP_CENTER);

        //Textfield for user to enter name
        var nameBox = new HBox();
        nameBox.setAlignment(Pos.TOP_CENTER);
        Text nameText = new Text("Enter username:");
        nameText.getStyleClass().add("customtext");
        nameText.setFont(new Font("Orbitron",18));
        nameField = new TextField();
        nameBox.getChildren().addAll(nameText, nameField);

        // Import XML button.
        importXMLButton = new Button("Import XML file");
        importXMLButton.getStyleClass().add("menubutton");

        // New blank file button.
        newBlankFileButton = new Button("New blank file");
        newBlankFileButton.getStyleClass().add("menubutton");

        // Add elements to the VBox.
        startVBox.getChildren().addAll(title, subtitle, nameBox, importXMLButton, newBlankFileButton);

        // Add components to the screen.
        mainPane.setTop(startVBox);

    /*
    Initialise file chooser
     */
        existingFileChooser = new FileChooser();
        existingFileChooser.setTitle("Open XML file");

    /*
    Button Actions
     */
        importXMLButton.setOnAction(this::chooseFile);
        newBlankFileButton.setOnAction(e -> {
            try {
                startBlankFile();
            } catch (ParserConfigurationException | IOException | SAXException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Choose an XML file.
     *
     * @param event import xml button pressed.
     */
    private void chooseFile(ActionEvent event) {
        //check if username entered
        if (nameField.getText().trim().isEmpty()) {
            // Display an error message if no username entered
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No username");
            alert.setContentText("Please enter a username in order to use the system.");
            alert.showAndWait();
            return;
        }

        File existingXmlFile = existingFileChooser.showOpenDialog(scene.getWindow());
        if (existingXmlFile != null) {
            runwayWindow.setXmlFile(existingXmlFile.getAbsoluteFile());

            // Base this on XML file later.
            runwayWindow.initialiseUiColor();

            runwayWindow.setUsername(nameField.getText());

            //add notification
            notifControl.addNotif("File select", "I/O", "Selected existing XML file");

            runwayWindow.startMenu();
        }
    }

    /**
     * Initialise the start screen.
     */
    @Override
    public void initialise() {
        // Check for esc key
        scene.setOnKeyPressed((esc) -> {
            if (esc.getCode().equals(KeyCode.ESCAPE)) {
                // Exit app
                App.getInstance().shutdown();
            }
        });
    }

    /**
     * Start the menu scene.
     */
    private void startBlankFile() throws ParserConfigurationException, IOException, SAXException {
        // TODO: Refactor how this works, maybe build a new XML file using a document builder rather than copying one over.
        // TODO: Add XML generation method to XMLParser utility class.

        //check if username entered
        if (nameField.getText().trim().isEmpty()) {
            // Display an error message if no username entered
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No username");
            alert.setContentText("Please enter a username in order to use the system.");
            alert.showAndWait();
            return;
        }

        //File blankXmlFile = new File(Paths.get("").toAbsolutePath()
        //    + "/src/main/java/com/example/runway_redeclaration/blankAirport.xml");
        File blankXmlFile = new File(String.valueOf(Paths.get("").toAbsolutePath() + "/blankAirport.xml"));

        runwayWindow.setXmlFile(blankXmlFile.getAbsoluteFile());

        runwayWindow.setUsername(nameField.getText());


        /*
        try {
            // This is why it doesn't work on desktop.
            File predefinedAirport = new File(Paths.get("").toAbsolutePath()
                    + "/src/main/java/com/example/runway_redeclaration/model/parser/predefinedAirport.xml");

            FileInputStream in = new FileInputStream(predefinedAirport);
            FileOutputStream out = new FileOutputStream(blankXmlFile);

            // Copy the predefinedAirport over to a new file.
            try {
                int n;

                // While there is still data in the existing file, write it to the blank file.
                while ((n = in.read()) != -1) {
                    out.write(n);
                }

                // Close the files.
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Root elements.
        Document document = docBuilder.newDocument();
        Element rootElement = document.createElement("airport");
        document.appendChild(rootElement);

        // Add strip end.
        Element stripEnd = document.createElement("stripEnd");
        stripEnd.setAttribute("metres","60");
        rootElement.appendChild(stripEnd);

        // Add blast protection.
        Element blastProtection = document.createElement("blastProtection");
        blastProtection.setAttribute("metres","300");
        rootElement.appendChild(blastProtection);

        // Add RESA.
        Element resa = document.createElement("resa");
        resa.setAttribute("metres","240");
        rootElement.appendChild(resa);

        // Add empty runways.
        Element runways = document.createElement("runways");
        rootElement.appendChild(runways);

        /*
         Add predefined obstacles.
         */
        Element obstacles = document.createElement("obstacles");
        rootElement.appendChild(obstacles);

        obstacles.appendChild(createObstacleElement(document, "Shipping Container", "3", "2", "2"));
        obstacles.appendChild(createObstacleElement(document, "Small Aircraft","3","11","8"));
        obstacles.appendChild(createObstacleElement(document,"Construction Crane","80","10","10"));
        obstacles.appendChild(createObstacleElement(document,"Semitruck Trailer","4","3","14"));
        obstacles.appendChild(createObstacleElement(document,"Luggage Cart","2","1","2"));

        // Add empty notifications.
        Element notifications = document.createElement("notifications");
        rootElement.appendChild(notifications);

        /*
        Add predefined colors.
         */
        Element colors = document.createElement("colors");
        rootElement.appendChild(colors);

        colors.appendChild(createColorElement(document, "Runway", "#a9a9a9"));
        colors.appendChild(createColorElement(document, "Displaced Threshold","#bfef45"));
        colors.appendChild(createColorElement(document, "Obstacle","#ffe119"));
        colors.appendChild(createColorElement(document, "Stopway","#fabed4"));
        colors.appendChild(createColorElement(document, "Clearway","#e6194b"));
        colors.appendChild(createColorElement(document, "TORA","#42d4f4"));
        colors.appendChild(createColorElement(document, "Obstacle Distance from Threshold","#800000"));
        colors.appendChild(createColorElement(document, "Blast Distance","#f58231"));
        colors.appendChild(createColorElement(document, "ALS","#f032e6"));
        colors.appendChild(createColorElement(document,"RESA","#aaffc3"));
        colors.appendChild(createColorElement(document,"Strip End","#000075"));
        colors.appendChild(createColorElement(document, "TOCS","#469990"));
        colors.appendChild(createColorElement(document,"Default","#cccaca"));
        colors.appendChild(createColorElement(document,"Cleared and Graded","#9370db"));
        colors.appendChild(createColorElement(document, "Instrument Strip", "#32174d"));

        /*
        Add predefined aircraft info.
         */
        Element aircraft = document.createElement("aircraft");
        rootElement.appendChild(aircraft);

        Element modelElement = document.createElement("model");
        modelElement.setAttribute("name","Boeing 737");
        aircraft.appendChild(modelElement);

        Element lengthElement = document.createElement("length");
        lengthElement.setAttribute("metres","28.65");
        aircraft.appendChild(lengthElement);

        Element wingspanElement = document.createElement("wingspan");
        wingspanElement.setAttribute("metres","28.35");
        aircraft.appendChild(wingspanElement);

        Element heightElement = document.createElement("height");
        heightElement.setAttribute("metres","11.28");
        aircraft.appendChild(heightElement);

        Element fuelCapacityElement = document.createElement("fuelcapacity");
        fuelCapacityElement.setAttribute("litres","17865");
        aircraft.appendChild(fuelCapacityElement);

        Element maxSpeedElement = document.createElement("maxspeed");
        maxSpeedElement.setAttribute("kmph","933");
        aircraft.appendChild(maxSpeedElement);

        // Export the file and start the menu.
        XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());

        runwayWindow.initialiseUiColor();

        //add notification
        notifControl.addNotif("File select", "I/O", "New blank XML file created");

        runwayWindow.startMenu();
    }

    /**
     * Create an obstacle element from the given parameters.
     *
     * @param document the document to create the obstacle element from
     * @param name the name of the obstacle
     * @param height the height of the obstacle
     * @param width the width of the obstacle
     * @param length the length of the obstacle
     * @return the obstacle element
     */
    private Element createObstacleElement(Document document, String name, String height, String width, String length) {
        // Create the obstacle.
        Element obstacleElement = document.createElement("obstacle");

        // Add the obstacle name.
        Element nameElement = document.createElement("name");
        nameElement.setAttribute("name",name);
        obstacleElement.appendChild(nameElement);

        // Add the obstacle height.
        Element heightElement = document.createElement("height");
        heightElement.setAttribute("metres",height);
        obstacleElement.appendChild(heightElement);

        // Add the obstacle width.
        Element widthElement = document.createElement("width");
        widthElement.setAttribute("metres",width);
        obstacleElement.appendChild(widthElement);

        // Add the obstacles length.
        Element lengthElement = document.createElement("length");
        lengthElement.setAttribute("metres",length);
        obstacleElement.appendChild(lengthElement);

        return obstacleElement;
    }

    /**
     * Create a color element from the given parameters.
     *
     * @param document the document to create the element from
     * @param name the name of the runway parameter
     * @param code the hexcode for the runway paremeter.
     * @return the color element
     */
    private Element createColorElement(Document document, String name, String code) {
        // Create the color element.
        Element colorElement = document.createElement("color");

        // Add the name.
        Element nameElement = document.createElement("name");
        nameElement.setAttribute("name",name);
        colorElement.appendChild(nameElement);

        // Add the hexcode.
        Element hexcodeElement = document.createElement("hexcode");
        hexcodeElement.setAttribute("code",code);
        colorElement.appendChild(hexcodeElement);

        return colorElement;
    }
}

