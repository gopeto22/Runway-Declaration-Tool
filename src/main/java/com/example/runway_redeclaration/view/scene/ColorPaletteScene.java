package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.UIColor;
import com.example.runway_redeclaration.controller.NotificationController;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.component.ColorPaletteBox;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
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

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * The Color Palette scene is used to change the color of each portion of the runway display.
 */
public class ColorPaletteScene extends BaseScene {

    /**
     * notification controller
     */
    NotificationController notifControl = new NotificationController(runwayWindow);

    /**
     * The UIColor object to store the different colours for the runway.
     */
    private UIColor uiColor;

    /**
     * The vbox to store all of the colour palettes.
     */
    private VBox colorVBox;

    /**
     * Color palette boxes
     */
    private ColorPaletteBox displacedBox;
    private ColorPaletteBox obstacleBox;
    private ColorPaletteBox stopwayBox;
    private ColorPaletteBox clearwayBox;
    private ColorPaletteBox toraBox;
    private ColorPaletteBox obstacleDistanceBox;
    private ColorPaletteBox blastBox;
    private ColorPaletteBox alsBox;
    private ColorPaletteBox resaBox;
    private ColorPaletteBox stripEndBox;
    private ColorPaletteBox slopeBox;
    private ColorPaletteBox clearedAndGradedBox;
    private ColorPaletteBox instrumentStripBox;

    /**
     * Create a new scene, passing in the window the scene will be displayed in
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public ColorPaletteScene(RunwayWindow runwayWindow) {
        super(runwayWindow);

        uiColor = runwayWindow.getUiColor();
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
        root.getStyleClass().add("background");

        var colorPalettePane = new StackPane();
        colorPalettePane.setMaxWidth(runwayWindow.getWidth());
        colorPalettePane.setMaxHeight(runwayWindow.getHeight());

        root.getChildren().add(colorPalettePane);

        var mainPane = new BorderPane();
        colorPalettePane.getChildren().add(mainPane);

        /*
        UI Elements
         */
        colorVBox = new VBox();

        var paletteVBox = new VBox();
        paletteVBox.setPadding(new Insets(4,4,4,4));
        paletteVBox.setMinWidth(runwayWindow.getWidth());
        paletteVBox.setMaxWidth(runwayWindow.getWidth());
        paletteVBox.setFillWidth(true);

        var colorPaletteBorderPane = new BorderPane();
        var backButton = new Button("Back");
        backButton.getStyleClass().add("midbutton");
        backButton.setOnAction(e -> {
            runwayWindow.startMenu();
        });

        var scrollPane = new ScrollPane();
        scrollPane.getStyleClass().addAll("background","nohighlight");
        scrollPane.setMaxWidth(runwayWindow.getWidth() - 20);
        scrollPane.setMinHeight(runwayWindow.getHeight() - 75);
        scrollPane.setMaxHeight(runwayWindow.getHeight() - 75);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        colorVBox.setSpacing(75);

        displacedBox = new ColorPaletteBox("Displaced Threshold", uiColor.getDisplacedThresholdColor(), runwayWindow.getWidth(), uiColor);
        obstacleBox = new ColorPaletteBox("Obstacle", uiColor.getObstacleColor(), runwayWindow.getWidth(), uiColor);
        stopwayBox = new ColorPaletteBox("Stopway", uiColor.getStopwayColor(), runwayWindow.getWidth(), uiColor);
        clearwayBox = new ColorPaletteBox("Clearway", uiColor.getClearwayColor(), runwayWindow.getWidth(), uiColor);
        toraBox = new ColorPaletteBox("TORA", uiColor.getOriginalToraColor(), runwayWindow.getWidth(), uiColor);
        obstacleDistanceBox = new ColorPaletteBox("Obstacle Distance from Threshold", uiColor.getObstacleDistanceThresholdColor(), runwayWindow.getWidth(), uiColor);
        blastBox = new ColorPaletteBox("Blast Distance", uiColor.getBlastDistanceColor(), runwayWindow.getWidth(), uiColor);
        alsBox = new ColorPaletteBox("ALS", uiColor.getAlsColor(), runwayWindow.getWidth(), uiColor);
        resaBox = new ColorPaletteBox("RESA", uiColor.getResaColor(), runwayWindow.getWidth(), uiColor);
        stripEndBox = new ColorPaletteBox("Strip End", uiColor.getStripEndColor(), runwayWindow.getWidth(), uiColor);
        slopeBox = new ColorPaletteBox("TOCS", uiColor.getSlopeColor(), runwayWindow.getWidth(), uiColor);
        clearedAndGradedBox = new ColorPaletteBox("Cleared and Graded", uiColor.getClearedAndGradedColor(), runwayWindow.getWidth(), uiColor);
        instrumentStripBox = new ColorPaletteBox("Instrument Strip", uiColor.getInstrumentStripColor(), runwayWindow.getWidth(), uiColor);

        // Add colors to the color VBox.
        colorVBox.getChildren().addAll(displacedBox, obstacleBox, stopwayBox, clearwayBox, toraBox,
                obstacleDistanceBox, blastBox, alsBox, resaBox, stripEndBox, slopeBox, clearedAndGradedBox, instrumentStripBox);

        scrollPane.setContent(colorVBox);

        var resetButton = new Button("Reset to Default");
        resetButton.getStyleClass().add("midbutton");
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                uiColor.resetColors();
                displacedBox = new ColorPaletteBox("Displaced Threshold", uiColor.getDisplacedThresholdColor(), runwayWindow.getWidth(), uiColor);
                obstacleBox = new ColorPaletteBox("Obstacle", uiColor.getObstacleColor(), runwayWindow.getWidth(), uiColor);
                stopwayBox = new ColorPaletteBox("Stopway", uiColor.getStopwayColor(), runwayWindow.getWidth(), uiColor);
                clearwayBox = new ColorPaletteBox("Clearway", uiColor.getClearwayColor(), runwayWindow.getWidth(), uiColor);
                toraBox = new ColorPaletteBox("TORA", uiColor.getOriginalToraColor(), runwayWindow.getWidth(), uiColor);
                obstacleDistanceBox = new ColorPaletteBox("Obstacle Distance from Threshold", uiColor.getObstacleDistanceThresholdColor(), runwayWindow.getWidth(), uiColor);
                blastBox = new ColorPaletteBox("Blast Distance", uiColor.getBlastDistanceColor(), runwayWindow.getWidth(), uiColor);
                alsBox = new ColorPaletteBox("ALS", uiColor.getAlsColor(), runwayWindow.getWidth(), uiColor);
                resaBox = new ColorPaletteBox("RESA", uiColor.getResaColor(), runwayWindow.getWidth(), uiColor);
                stripEndBox = new ColorPaletteBox("Strip End", uiColor.getStripEndColor(), runwayWindow.getWidth(), uiColor);
                slopeBox = new ColorPaletteBox("TOCS", uiColor.getSlopeColor(), runwayWindow.getWidth(), uiColor);
                clearedAndGradedBox = new ColorPaletteBox("Cleared and Graded", uiColor.getClearedAndGradedColor(), runwayWindow.getWidth(), uiColor);
                instrumentStripBox = new ColorPaletteBox("Instrument Strip", uiColor.getInstrumentStripColor(), runwayWindow.getWidth(), uiColor);

                colorVBox.getChildren().clear();

                // Add colors to the color VBox.
                colorVBox.getChildren().addAll(displacedBox, obstacleBox, stopwayBox, clearwayBox, toraBox,
                        obstacleDistanceBox, blastBox, alsBox, resaBox, stripEndBox, slopeBox, clearedAndGradedBox, instrumentStripBox);

                //notifControl.addNotif("");
            }
        });

        var exportButton = new Button("Export to XML");
        exportButton.getStyleClass().add("midbutton");
        exportButton.setOnAction(e -> {
            try {
                exportColors();
            } catch (ParserConfigurationException | IOException | SAXException ex) {
                ex.printStackTrace();
            }
        });

        var toolbar = new HBox();
        toolbar.setPadding(new Insets(4,4,4,4));
        toolbar.setSpacing(5);
        toolbar.getChildren().addAll(backButton, resetButton, exportButton);

        var colorPaletteTitle = new Text("Colour");
        colorPaletteTitle.getStyleClass().add("bigtitle");

        colorPaletteBorderPane.setLeft(toolbar);
        colorPaletteBorderPane.setCenter(colorPaletteTitle);

        // Add to the palette VBox.
        paletteVBox.getChildren().addAll(colorPaletteBorderPane, scrollPane);

        mainPane.setTop(paletteVBox);
    }

    /**
     * Export the details on this screen.
     */
    private void exportColors() throws ParserConfigurationException, IOException, SAXException {
        Document document;
        document = XMLParser.importXmlFile(runwayWindow.getXmlFile());


        Node colors = document.getElementsByTagName("colors").item(0);
        // Remove current colors stored.
        while (colors.hasChildNodes()) {
            colors.removeChild(colors.getFirstChild());
        }

        // Manually add runwayColor and defaultColor.
        Element manualColor1 = document.createElement("color");

        // Add runwayColor name.
        Element runwayColorNameElement = document.createElement("name");
        runwayColorNameElement.setAttribute("name","Runway");
        manualColor1.appendChild(runwayColorNameElement);

        // Add runwayColor hexcode.
        Element runwayColorHexElement = document.createElement("hexcode");
        runwayColorHexElement.setAttribute("code","#a9a9a9");
        manualColor1.appendChild(runwayColorHexElement);

        Element manualColor2 = document.createElement("color");

        // Add defaultColor name
        Element defaultColorNameElement = document.createElement("name");
        defaultColorNameElement.setAttribute("name","Default");
        manualColor2.appendChild(defaultColorNameElement);

        // Add defaltColor hexcode.
        Element defaultColorHexElement = document.createElement("hexcode");
        defaultColorHexElement.setAttribute("code","#cccaca");
        manualColor2.appendChild(defaultColorHexElement);

        colors.appendChild(manualColor1);
        colors.appendChild(manualColor2);

        for (var colorBox : colorVBox.getChildren()) {
            // Create new color element.
            Element color = document.createElement("color");

            Element colorNameElement = document.createElement("name");
            colorNameElement.setAttribute("name", ((ColorPaletteBox) colorBox).getName());
            color.appendChild(colorNameElement);

            Element colorHexcodeElement = document.createElement("hexcode");
            colorHexcodeElement.setAttribute("code", "#"+((ColorPaletteBox) colorBox).getColor().toString().substring(2,8));
            color.appendChild(colorHexcodeElement);

            colors.appendChild(color);
        }

        XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());

        // Show a success message
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Success");
        success.setHeaderText("Exported successfully");
        success.showAndWait();

        // Add notification.
        notifControl.addNotif("File export", "I/O", "Details exported to XML:" +
                "\n\tDisplaced Threshold Color: #" + displacedBox.getColor().toString().substring(2,8) +
                "\n\tObstacle Color: #" + obstacleBox.getColor().toString().substring(2,8) +
                "\n\tStopway Color: #" + stopwayBox.getColor().toString().substring(2,8) +
                "\n\tClearway Color: #" + clearwayBox.getColor().toString().substring(2,8) +
                "\n\tTORA Color: #" + toraBox.getColor().toString().substring(2,8) +
                "\n\tObstacle Distance from Threshold Color: #" + obstacleDistanceBox.getColor().toString().substring(2,8) +
                "\n\tBlast Distance Color: #" + blastBox.getColor().toString().substring(2,8) +
                "\n\tALS Color: #" + alsBox.getColor().toString().substring(2,8) +
                "\n\tRESA Color: #" + resaBox.getColor().toString().substring(2,8) +
                "\n\tStrip End Color: #" + stripEndBox.getColor().toString().substring(2,8) +
                "\n\tSlope Color: #" + slopeBox.getColor().toString().substring(2,8) +
                "\n\tCleared and Graded Color: #" + clearedAndGradedBox.getColor().toString().substring(2,8) +
                "\n\tInstrument Strip Color: #" + instrumentStripBox.getColor().toString().substring(2,8)
        );
    }
}
