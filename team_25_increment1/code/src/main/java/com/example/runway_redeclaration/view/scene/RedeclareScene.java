package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.model.Verifier;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.component.ObstacleBox;
import com.example.runway_redeclaration.view.component.RunwayBox;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The redeclare scene is used to input details known about the airport/runways to redeclare a
 * runway.
 */
public class RedeclareScene extends BaseScene {

    /**
     * Redeclare Scene logger.
     */
    private static final Logger logger = LogManager.getLogger(StartScene.class);

    /**
     * Textfields for airport values
     */
    private TextField resaBox;
    private TextField stripEndBox;
    private TextField blastProtectionBox;

    /**
     * Text components.
     */
    private Text resaText;
    private Text stripEndText;
    private Text blastProtectionText;

    /**
     * VBox which holds all the runwayBoxes
     */
    private VBox runwaysVBox;

    /**
     * Root element of the XML file. Used to initialise existing data.
     */
    private Element rootElement;

    /**
     * Create a new scene, passing in the window the scene will be displayed in
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public RedeclareScene(RunwayWindow runwayWindow) {
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

        /*
        UI Elements
         */
        // Add buttons for navigation.
        var menuVBox = new VBox();
        menuVBox.setSpacing(10);
        menuVBox.setPadding(new Insets(4, 4, 4, 4));
        menuVBox.setAlignment(Pos.TOP_CENTER);

        //Airport details
        var airportBox = new HBox();
        airportBox.setSpacing(10);
        airportBox.setPadding(new Insets(4, 4, 4, 4));
        airportBox.setAlignment(Pos.TOP_CENTER);
        resaText = new Text("RESA:");
        resaBox = new TextField();
        stripEndText = new Text("Strip End:");
        stripEndBox = new TextField();
        blastProtectionText = new Text("Blast Protection:");
        blastProtectionBox = new TextField();
        airportBox.getChildren().add(resaText);
        airportBox.getChildren().add(resaBox);
        airportBox.getChildren().add(stripEndText);
        airportBox.getChildren().add(stripEndBox);
        airportBox.getChildren().add(blastProtectionText);
        airportBox.getChildren().add(blastProtectionBox);
        menuVBox.getChildren().add(airportBox);

        // Add a new runway
        Button newRunwayButton = new Button("Add new runway");

        // Export these details to the working XML file.
        Button exportToXml = new Button("Export to XML");

        // Add HBox for the 2 buttons.
        HBox buttonHBox = new HBox();
        buttonHBox.setAlignment(Pos.CENTER);
        buttonHBox.setSpacing(2);
        buttonHBox.setPadding(new Insets(4, 4, 4, 4));
        buttonHBox.getChildren().addAll(newRunwayButton, exportToXml);

        // Add buttons to the menu vbox.
        menuVBox.getChildren().add(buttonHBox);

        // Add components to the main pane.
        mainPane.setTop(menuVBox);

        //VBox for runways
        runwaysVBox = new VBox();
        runwaysVBox.setSpacing(10);
        runwaysVBox.setPadding(new Insets(4, 4, 4, 4));
        runwaysVBox.setAlignment(Pos.TOP_CENTER);
        mainPane.setCenter(runwaysVBox);

        /*
        Initialisation of existing data from the XML file.
         */
        System.out.println("Initialising root element.");
        initialiseRootElement();
        System.out.println("Initialising airport details.");
        initialiseAirportDetails();
        System.out.println("Initialising existing runways.");
        initialiseExistingRunways();

        /*
        Button Actions
         */
        newRunwayButton.setOnAction(this::newRunway);
        exportToXml.setOnAction(e -> {
            try {
                exportAirport();
            } catch (ParserConfigurationException | IOException | SAXException ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Export the details on this screen
     */
    private void exportAirport() throws ParserConfigurationException, IOException, SAXException {
        Document document;
        document = XMLParser.importXmlFile(runwayWindow.getXmlFile());

        // Verify the airport fields.
        var airportVerifyPair = verifyAirportFields();
        if (!airportVerifyPair.getValue()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(airportVerifyPair.getKey());
            alert.showAndWait();
            return;
        }

        Element stripEndElement = (Element) document.getElementsByTagName("stripEnd").item(0);
        stripEndElement.setAttribute("metres", stripEndBox.getText());
        Element blastProtection = (Element) document.getElementsByTagName("blastProtection").item(0);
        blastProtection.setAttribute("metres", blastProtectionBox.getText());
        Element resa = (Element) document.getElementsByTagName("resa").item(0);
        resa.setAttribute("metres", resaBox.getText());

        /*
        Add runways
         */
        // Get existing runway node.
        Node runways = document.getElementsByTagName("runways").item(0);
        // Remove current runways stored.
        while (runways.hasChildNodes()) {
            runways.removeChild(runways.getFirstChild());
        }

        for (var runwayBox : runwaysVBox.getChildren()) {
            System.out.println("Processing a runway.");
            if (runwayBox instanceof RunwayBox) {
                // Verify runway field values.
                var runwayVerifyPair = ((RunwayBox) runwayBox).verifyRunwayFields();
                if (!runwayVerifyPair.getValue()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(runwayVerifyPair.getKey());
                    alert.showAndWait();
                    return;
                }

                // Create new runway element.
                System.out.println("Creating a runway element.");
                Element runway = document.createElement("runway");

                // Get runway values.
                Element designatorElement = document.createElement("designator");
                designatorElement.setAttribute("code", ((RunwayBox) runwayBox).getDesignatorValue());
                runway.appendChild(designatorElement);

                Element toraElement = document.createElement("tora");
                toraElement.setAttribute("metres", ((RunwayBox) runwayBox).getToraValue());
                runway.appendChild(toraElement);

                Element clearwayElement = document.createElement("clearway");
                clearwayElement.setAttribute("metres", ((RunwayBox) runwayBox).getClearwayValue());
                runway.appendChild(clearwayElement);

                Element stopwayElement = document.createElement("stopway");
                stopwayElement.setAttribute("metres", ((RunwayBox) runwayBox).getStopwayValue());
                runway.appendChild(stopwayElement);

                Element displacedThresholdElement = document.createElement("displacedThreshold");
                displacedThresholdElement.setAttribute("metres", ((RunwayBox) runwayBox).getDisplacedThresholdValue());
                runway.appendChild(displacedThresholdElement);

                // Add obstacle template
                Element obstacleNameElement = document.createElement("obstacleName");
                runway.appendChild(obstacleNameElement);

                Element obstacleHeightElement = document.createElement("obstacleHeight");
                runway.appendChild(obstacleHeightElement);

                Element nFromCentreElement = document.createElement("nFromCentre");
                runway.appendChild(nFromCentreElement);

                Element eFromCentreElement = document.createElement("eFromCentre");
                runway.appendChild(eFromCentreElement);

                Element distanceFromThresholdElement = document.createElement("distanceFromThreshold");
                runway.appendChild(distanceFromThresholdElement);

                // Add obstacle values if there is one.
                ObstacleBox obstacleBox = ((RunwayBox) runwayBox).getObstacleBox();
                if (obstacleBox != null) {
                    // Verify obstacle values.
                    var obstacleVerifyPair = obstacleBox.verifyObstacleValues();
                    if (!obstacleVerifyPair.getValue()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText(obstacleVerifyPair.getKey());
                        alert.showAndWait();
                        return;
                    }
                    obstacleNameElement.setAttribute("name", obstacleBox.getNameValue());
                    obstacleHeightElement.setAttribute("metres", obstacleBox.getHeightValue());
                    nFromCentreElement.setAttribute("metres", obstacleBox.getNFromCentreValue());
                    eFromCentreElement.setAttribute("metres", obstacleBox.getEFromCentreValue());
                    distanceFromThresholdElement.setAttribute("metres", obstacleBox.getDistanceFromThresholdValue());
                } else {
                    obstacleNameElement.setAttribute("name", "");
                    obstacleHeightElement.setAttribute("metres", "");
                    nFromCentreElement.setAttribute("metres", "");
                    eFromCentreElement.setAttribute("metres", "");
                    distanceFromThresholdElement.setAttribute("metres", "");
                }

                runways.appendChild(runway);
            }
        }

       XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());

        // Show a success message
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Success");
        success.setHeaderText("Exported successfully");
        success.showAndWait();
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
     * Initialise the airport details.
     */
    private void initialiseAirportDetails() {
        System.out.println("Getting stripEnd value.");
        String stripEnd = XMLParser.getTagAttributeElement(rootElement, "stripEnd", "metres");
        System.out.println("Getting blastProtection value.");
        String blastProtection = XMLParser.getTagAttributeElement(rootElement, "blastProtection", "metres");
        System.out.println("Getting resa value.");
        String resa = XMLParser.getTagAttributeElement(rootElement, "resa", "metres");

        stripEndBox.setText(stripEnd);
        blastProtectionBox.setText(blastProtection);
        resaBox.setText(resa);
    }

    /**
     * Initialise the existing runways in the XML document.
     */
    private void initialiseExistingRunways() {
        if (rootElement.getElementsByTagName("runway") == null) {
            return;
        }

        int i = 0;


        Element runwayElement = (Element) rootElement.getElementsByTagName("runway").item(i);

        while (runwayElement != null) {
            // Get this runways attributes
            String runwayDesignator = XMLParser.getTagAttributeElement(runwayElement, "designator",
                    "code");
            String runwayTora = XMLParser.getTagAttributeElement(runwayElement, "tora", "metres");
            String runwayClearway = XMLParser.getTagAttributeElement(runwayElement, "clearway", "metres");
            String runwayStopway = XMLParser.getTagAttributeElement(runwayElement, "stopway", "metres");
            String runwayDisplacedThreshold = XMLParser.getTagAttributeElement(runwayElement,
                    "displacedThreshold", "metres");
            String obstacleName = XMLParser.getTagAttributeElement(runwayElement, "obstacleName", "name");
            String obstacleHeight = XMLParser.getTagAttributeElement(runwayElement, "obstacleHeight",
                    "metres");
            String obstacleNFromCentre = XMLParser.getTagAttributeElement(runwayElement, "nFromCentre",
                    "metres");
            String obstacleEFromCentre = XMLParser.getTagAttributeElement(runwayElement, "eFromCentre",
                    "metres");
            String obstacleDistanceFromThreshold = XMLParser.getTagAttributeElement(runwayElement,
                    "distanceFromThreshold", "metres");

            RunwayBox newRunwayBox = new RunwayBox(runwayWindow.getXmlFile());
            newRunwayBox.setRunwayValues(runwayDesignator, runwayTora, runwayClearway, runwayStopway,
                    runwayDisplacedThreshold);

            // Check if there is an obstacle.
            if (!obstacleName.equals("")) {
                ObstacleBox newObstacleBox = new ObstacleBox();
                newObstacleBox.setObstacleValues(obstacleName, obstacleHeight, obstacleNFromCentre,
                        obstacleEFromCentre, obstacleDistanceFromThreshold);
                newRunwayBox.setObstacleBox(newObstacleBox);
            }

            setupRunwayBox(newRunwayBox);

            runwayElement = (Element) rootElement.getElementsByTagName("runway").item(++i);
        }
    }

    /**
     * Add a new runwayBox to the scene.
     *
     * @param event new runway button clicked.
     */
    private void newRunway(ActionEvent event) {
        // Validate input
        if (Integer.parseInt(resaBox.getText()) < 0 || Integer.parseInt(stripEndBox.getText()) < 0 ||
                Integer.parseInt(blastProtectionBox.getText()) < 0 ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Negative input is not valid.");
            alert.showAndWait();
            return;
        }
        System.out.println("new runway button pressed.");

        // Create a new runwayBox component.
        var runwayBox = new RunwayBox(runwayWindow.getXmlFile());

        // Setup new runway
        setupRunwayBox(runwayBox);
    }

    /**
     * Set up the runway box, add functionality to the buttons.
     *
     * @param runwayBox the runway box to set up
     */
    private void setupRunwayBox(RunwayBox runwayBox) {
        // Setup runwayBox events.
        runwayBox.setSimulateButtonAction(e -> {
            var runway = runwayBox.getRunway();
            if (runway != null) {
                // Add airport values
                var verifyPair = verifyAirportFields();

                if (verifyPair.getValue()) {
                    runway.updateAirport(Integer.parseInt(stripEndBox.getText()), Integer.parseInt(blastProtectionBox.getText()), Integer.parseInt(resaBox.getText()));

                    // Add a new runway window and open the simulation in a separate window.
                    int width = (int) this.getScene().getWidth();
                    int height = (int) this.getScene().getHeight();

                    var newStage = new Stage();
                    var newRunwayWindow = new RunwayWindow(newStage, width, height);
                    newRunwayWindow.startSimulation(runway);
                    newStage.show();
                    //runwayWindow.startSimulation(runway,this);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(verifyPair.getKey());
                    alert.showAndWait();
                }
            }
        });

        runwayBox.setDeleteButtonAction(e -> {
            runwaysVBox.getChildren().remove(runwayBox);
        });

        // Add the runwayBox to the list of runways.
        runwaysVBox.getChildren().add(runwayBox);
    }

    /**
     * Verify all the fields relating to airport specific information.
     *
     * @return a pair containing an error message if a field couldn't be verified and whether the input was verified.
     */
    private Pair<String, Boolean> verifyAirportFields() {
        Verifier verifier = new Verifier();
        List<Pair<String, String>> airportFields = new ArrayList<>();
        airportFields.add(new Pair<>(stripEndText.getText(), stripEndBox.getText()));
        airportFields.add(new Pair<>(blastProtectionText.getText(), blastProtectionBox.getText()));
        airportFields.add(new Pair<>(resaText.getText(), resaBox.getText()));

        return verifier.verifyAllIntFormat(airportFields);
    }

}
