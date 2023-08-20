package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.BreakdownTableObject;
import com.example.runway_redeclaration.OriginalRedeclaredTableObject;
import com.example.runway_redeclaration.model.Runway;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.component.RunwayKeyCanvas;
import com.example.runway_redeclaration.view.component.RunwaySideOnCanvas;
import com.example.runway_redeclaration.view.component.RunwayTopDownCanvas;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The simulation scene is used to simulate a runway calculation.
 */
public class SimulationScene extends BaseScene {

    /**
     * The runway top down view.
     */
    RunwayTopDownCanvas topDownCanvas;

    private class StartCoordinates { double x, y; }
    private StartCoordinates topDownStartCoordinates = new StartCoordinates();
    private StartCoordinates sideViewStartCoordinates = new StartCoordinates();

    /**
     * The runway side on view.
     */
    RunwaySideOnCanvas sideOnCanvas;

    /**
     * The runway Hbox.
     */
    private HBox runwayViewHBox;

    private SplitPane sideOnPane;

    private SplitPane topDownPane;

    /**
     * Split buttons.
     */
    private Button topDownButton;

    private Button sideOnButton;

    /**
     * The runway to be visualised.
     */
    private Runway runway;

    /**
     * Root element of the XML file. Used to initialise existing data.
     */
    private Element rootElement = runwayWindow.getRootElement();

    /**
     * Canvas values.
     */
    double canvasWidth;
    double canvasHeight;
    double canvasRotate;

    /**
     * Create a new scene, passing in the window the scene will be displayed in
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public SimulationScene(RunwayWindow runwayWindow, Runway runway) {
        super(runwayWindow);
        this.runway = runway;

        canvasWidth = runwayWindow.getWidth() * 0.54;
        canvasHeight = runwayWindow.getHeight() * 0.54;
        canvasRotate = 0;
    }

    /**
     * Initialise this scene. Called after creation
     */
    @Override
    public void initialise() {
        // Check for esc key.
        scene.setOnKeyPressed((esc) -> {
            if (esc.getCode().equals(KeyCode.ESCAPE)) {
                // Close this stage.
                ((Stage) scene.getWindow()).close();
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

        var simulationPane = new StackPane();
        simulationPane.setMaxWidth(runwayWindow.getWidth());
        simulationPane.setMaxHeight(runwayWindow.getHeight());

        root.getChildren().add(simulationPane);
        root.getStyleClass().add("background");

        var mainPane = new BorderPane();
        simulationPane.getChildren().add(mainPane);

    /*
    UI Elements
     */
        // Add components to the main pane as done in the menu scene.
        var simulationVBox = new VBox();
        simulationVBox.setSpacing(8);
        simulationVBox.setPadding(new Insets(4, 4, 4, 4));
        simulationVBox.setAlignment(Pos.TOP_CENTER);

        var simulationBorderPane = new BorderPane();
        var backButton = new Button("Back");
        backButton.getStyleClass().add("midbutton");
        backButton.setOnAction(e -> {
            // Close this stage.
            ((Stage) scene.getWindow()).close();
        });

        // Runway view
        runwayViewHBox = new HBox();
        runwayViewHBox.setPadding(new Insets(4,4,4,4));
        runwayViewHBox.setAlignment(Pos.CENTER);

        topDownCanvas = new RunwayTopDownCanvas(canvasWidth, canvasHeight, runway, runwayWindow.getUiColor());
        sideOnCanvas = new RunwaySideOnCanvas(canvasWidth, canvasHeight, runway, runwayWindow.getUiColor());

        topDownCanvas.setRotate(canvasRotate);
        sideOnCanvas.setRotate(canvasRotate);

        EnableDragging(topDownCanvas, topDownStartCoordinates);
        EnableDragging(sideOnCanvas, sideViewStartCoordinates);

        var topDownBorderPane = new BorderPane();
        var topDownBottomBorderPane = new BorderPane();
        topDownBorderPane.setCenter(topDownCanvas);
        topDownBorderPane.setBottom(topDownBottomBorderPane);
        AddButtons(topDownCanvas, topDownBottomBorderPane);

        var sideOnBorderPane = new BorderPane();
        var sideOnBottomBorderPane = new BorderPane();
        sideOnBorderPane.setCenter(sideOnCanvas);
        sideOnBorderPane.setBottom(sideOnBottomBorderPane);
        AddButtons(sideOnCanvas, sideOnBottomBorderPane);

        topDownPane = new SplitPane();
        topDownPane.getItems().add(topDownBorderPane);
        topDownPane.getStyleClass().add("componentbg");

        sideOnPane = new SplitPane();
        sideOnPane.getItems().add(sideOnBorderPane);
        sideOnPane.getStyleClass().add("componentbg");

        /*
        Runway Key canvas.
         */
        var keyCanvas = new RunwayKeyCanvas(canvasWidth * 0.2, canvasHeight);
        keyCanvas.drawKey(runwayWindow.getUiColor().getOriginalToraColor(),"Original\nTORA");
        keyCanvas.drawKey(runwayWindow.getUiColor().getDisplacedThresholdColor(), "Displaced\nThreshold");
        keyCanvas.drawKey(runwayWindow.getUiColor().getObstacleColor(),"Obstacle");
        keyCanvas.drawKey(runwayWindow.getUiColor().getStopwayColor(),"Stopway");
        keyCanvas.drawKey(runwayWindow.getUiColor().getClearwayColor(),"Clearway");
        keyCanvas.drawKey(runwayWindow.getUiColor().getBlastDistanceColor(),"Blast\nDistance");
        keyCanvas.drawKey(runwayWindow.getUiColor().getObstacleDistanceThresholdColor(),"Obstacle\nDistance From\nThreshold");
        keyCanvas.drawKey(runwayWindow.getUiColor().getAlsColor(),"ALS");
        keyCanvas.drawKey(runwayWindow.getUiColor().getResaColor(),"RESA");
        keyCanvas.drawKey(runwayWindow.getUiColor().getStripEndColor(),"Strip\nEnd");
        keyCanvas.drawKey(runwayWindow.getUiColor().getSlopeColor(),"TOCS");
        keyCanvas.drawKey(runwayWindow.getUiColor().getClearedAndGradedColor(), "Cleared and\nGraded Area");
        keyCanvas.drawKey(runwayWindow.getUiColor().getInstrumentStripColor(), "Instrument\nStrip");

        //buttons on side of canvas
        var changeViewVBox = new VBox();
        changeViewVBox.setPadding(new Insets(4,4,4,4));
        changeViewVBox.setSpacing(5);

        topDownButton = new Button("Top-Down");
        topDownButton.getStyleClass().add("midbutton-selected");
        sideOnButton = new Button("Side-On");
        sideOnButton.getStyleClass().add("midbutton");
//        var takeoffButton = new Button("Simulate take-off");
//        takeoffButton.getStyleClass().add("midbutton");
//        var landingButton = new Button("Simulate landing");
//        landingButton.getStyleClass().add("midbutton");

        //information about plane/clearance
        VBox planeBox = new VBox();
        planeBox.getStyleClass().add("boxOutline");
        planeBox.getStyleClass().add("componentbg");
        planeBox.setPadding(new Insets(4,4,4,4));
        planeBox.setAlignment(Pos.TOP_CENTER);

        Text aircraftDetails = new Text("Aircraft details");
        aircraftDetails.getStyleClass().add("subtitle");

        planeBox.getChildren().addAll(aircraftDetails, displayPlaneDetails());

        changeViewVBox.getChildren().addAll(topDownButton,sideOnButton, planeBox);

        if (runway.getObstacle() != null) {
            VBox clearanceBox = new VBox();
            clearanceBox.getStyleClass().add("boxOutline");
            clearanceBox.getStyleClass().add("componentbg");
            clearanceBox.setPadding(new Insets(4,4,4,4));
            clearanceBox.setAlignment(Pos.TOP_CENTER);

            Text obstacleDetails = new Text("Obstacle details");
            obstacleDetails.getStyleClass().add("subtitle");

            clearanceBox.getChildren().addAll(obstacleDetails, displayObstacleClearance());

            changeViewVBox.getChildren().add(clearanceBox);
        }

        runwayViewHBox.getChildren().addAll(keyCanvas, topDownPane, changeViewVBox);

        // Original/Redeclared values VBox
        var originalRedeclaredVBox = new VBox();
        originalRedeclaredVBox.setAlignment(Pos.CENTER);

        // Original/Redeclared values title
        var originalRedeclaredTitle = new Text("Original/Redeclared values");
        originalRedeclaredTitle.getStyleClass().add("customtext");

        // Original/Redeclared values Table
        var originalRedeclaredTableView = new TableView<OriginalRedeclaredTableObject>();
        originalRedeclaredTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        originalRedeclaredTableView.setFixedCellSize(20);
        originalRedeclaredTableView.setMaxHeight(107);

        var parameterColumn = new TableColumn<OriginalRedeclaredTableObject, String>("Parameter");
        parameterColumn.getStyleClass().addAll("customtext","tablealigncenter","componentbg");
        parameterColumn.setCellValueFactory(new PropertyValueFactory<OriginalRedeclaredTableObject, String>("parameter"));
        var originalValueColumn = new TableColumn<OriginalRedeclaredTableObject, String>("Original Value");
        originalValueColumn.getStyleClass().addAll("customtextblack","tablealigncenter","lightbackground");
        originalValueColumn.setCellValueFactory(new PropertyValueFactory<OriginalRedeclaredTableObject, String>("originalValue"));
        var redeclaredValueColumn = new TableColumn<OriginalRedeclaredTableObject, String>("Redeclared Value");
        redeclaredValueColumn.getStyleClass().addAll("customtextblack","tablealigncenter","lightbackground");
        redeclaredValueColumn.setCellValueFactory(new PropertyValueFactory<OriginalRedeclaredTableObject, String>("redeclaredValue"));

        originalRedeclaredTableView.getColumns().addAll(parameterColumn,originalValueColumn,redeclaredValueColumn);
        originalRedeclaredTableView.getItems().add(new OriginalRedeclaredTableObject("TORA",runway.originalToraProperty().asString().getValue() + " metres",runway.redeclaredToraProperty().asString().getValue() + " metres"));
        originalRedeclaredTableView.getItems().add(new OriginalRedeclaredTableObject("TODA",runway.originalTodaProperty().asString().getValue() + " metres",runway.redeclaredTodaProperty().asString().getValue() + " metres"));
        originalRedeclaredTableView.getItems().add(new OriginalRedeclaredTableObject("ASDA",runway.originalAsdaProperty().asString().getValue() + " metres",runway.redeclaredAsdaProperty().asString().getValue() + " metres"));
        originalRedeclaredTableView.getItems().add(new OriginalRedeclaredTableObject("LDA",runway.originalLdaProperty().asString().getValue() + " metres",runway.redeclaredLdaProperty().asString().getValue() + " metres"));

        originalRedeclaredVBox.getChildren().addAll(originalRedeclaredTitle,originalRedeclaredTableView);

        // Recalculate the runway.
        runway.recalculate();

        // Breakdown of calculations box
        VBox breakdownBox = new VBox();
        breakdownBox.setAlignment(Pos.CENTER);

        var breakdownTitle = new Text("Breakdown of calculations");
        breakdownTitle.getStyleClass().add("customtext");
        breakdownTitle.setTextAlignment(TextAlignment.CENTER);

        var typeTitle = new Text(runway.getEqType());
        typeTitle.getStyleClass().add("customtext");
        typeTitle.setTextAlignment(TextAlignment.CENTER);

        //table of breakdown calculation
        TableView table = new TableView<BreakdownTableObject>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(20);
        table.setMaxHeight(107);

        TableColumn colOne = new TableColumn<BreakdownTableObject, String>("Equation");
        colOne.getStyleClass().addAll("customtext","componentbg");
        colOne.setCellValueFactory(new PropertyValueFactory<BreakdownTableObject, String>("eq"));

        TableColumn colTwo = new TableColumn<BreakdownTableObject, String>("Breakdown of Calculations");
        colTwo.getStyleClass().addAll("customtextblack","lightbackground");
        colTwo.setCellValueFactory(new PropertyValueFactory<BreakdownTableObject, String>("calc"));

        TableColumn colThree = new TableColumn<BreakdownTableObject, Integer>("Result");
        colThree.getStyleClass().addAll("customtextblack","tablealigncenter","lightbackground");
        colThree.setCellValueFactory(new PropertyValueFactory<BreakdownTableObject, Integer>("result"));

        colOne.prefWidthProperty().bind(table.widthProperty().multiply(0.5));
        colTwo.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        colThree.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        colOne.setResizable(false);
        colTwo.setResizable(false);
        colThree.setResizable(false);

        table.getItems().addAll(new BreakdownTableObject(runway.getToraEq(), runway.getToraCalc(), runway.getRedeclaredTora() + " metres"));
        table.getItems().addAll(new BreakdownTableObject(runway.getTodaEq(), runway.getTodaCalc(), runway.getRedeclaredToda() + " metres"));
        table.getItems().addAll(new BreakdownTableObject(runway.getAsdaEq(), runway.getAsdaCalc(), runway.getRedeclaredAsda() + " metres"));
        table.getItems().addAll(new BreakdownTableObject(runway.getLdaEq(), runway.getLdaCalc(), runway.getRedeclaredLda() + " metres"));

        table.getColumns().addAll(colOne, colTwo, colThree);
        breakdownBox.getChildren().addAll(breakdownTitle, typeTitle, table);

        // Add components to the screen.
        //simulationVBox.getChildren().addAll(splitPane, keyCanvas, originalRedeclaredHBox, breakdownBox);
        //simulationVBox.getChildren().addAll(runwayViewHBox, originalRedeclaredHBox, breakdownBox);

        simulationBorderPane.setLeft(backButton);
        simulationBorderPane.setCenter(runwayViewHBox);

        simulationVBox.getChildren().addAll(simulationBorderPane, originalRedeclaredVBox, breakdownBox);
        mainPane.setTop(simulationVBox);

        topDownButton.setOnAction(this::switchTopDown);
        sideOnButton.setOnAction(this::switchSideOn);
//        takeoffButton.setOnAction(this::takeoffButtonHandler);
//        landingButton.setOnAction(this::landingButtonHandler);
    }

    //display obstacle clearance time
    private GridPane displayObstacleClearance() {
        try {
            // Always only one aircraft element.
            int i = 0;
            Node obstacle = rootElement.getElementsByTagName("obstacle").item(i);

            //adapted from https://www.javaprogramto.com/2020/04/java-add-minutes-to-date.html
            Calendar date = Calendar.getInstance();
            long timeInSecs = date.getTimeInMillis();
            Date afterAdding = new Date(timeInSecs + (runway.getObstacle().getClearanceTime() * 60 * 1000));

            Text nameIdentifier = new Text("Name: ");
            nameIdentifier.getStyleClass().add("customtextnormal");
            Text nameDetails = new Text(runway.getObstacle().getName());
            nameDetails.getStyleClass().add("customtext");

            Text lengthIdentifier = new Text("Length: ");
            lengthIdentifier.getStyleClass().add("customtextnormal");
            Text lengthDetails = new Text(String.valueOf(runway.getObstacle().getObsLength()) + " metres");
            lengthDetails.getStyleClass().add("customtext");

            Text widthIdentifier = new Text("Width: ");
            widthIdentifier.getStyleClass().add("customtextnormal");
            Text widthDetails = new Text(String.valueOf(runway.getObstacle().getObsWidth()) + " metres");
            widthDetails.getStyleClass().add("customtext");

            Text heightIdentifier = new Text("Height: ");
            heightIdentifier.getStyleClass().add("customtextnormal");
            Text heightDetails = new Text(String.valueOf(runway.getObstacle().getHeight()) + " metres");
            heightDetails.getStyleClass().add("customtext");

            Text clearanceIdentifier = new Text("Est. clearance \ntime: ");
            clearanceIdentifier.getStyleClass().add("customtextnormal");
            Text clearanceDetails = new Text(new SimpleDateFormat("HH:mm\ndd/MM/yyyy").format(afterAdding));
            clearanceDetails.getStyleClass().add("customtext");
            //Text time1 = new Text(runway.getObstacle().getClearanceTime() + " minutes from now (" + new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(date.getTime()) + ")");
            //Text time2 = new Text("= " + new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(afterAdding));

            var gridpane = new GridPane();
            gridpane.addRow(0, nameIdentifier, nameDetails);
            gridpane.addRow(1, lengthIdentifier, lengthDetails);
            gridpane.addRow(2, widthIdentifier, widthDetails);
            gridpane.addRow(3, heightIdentifier, heightDetails);
            gridpane.addRow(4, clearanceIdentifier, clearanceDetails);
            return gridpane;

        }catch (NullPointerException e){
            e.printStackTrace();
            return (new GridPane());
        }
    }

    //get details of the plane in the XML file
    private GridPane displayPlaneDetails() {
            // Always only one aircraft element.
            Element aircraftElement = (Element) rootElement.getElementsByTagName("aircraft").item(0);

            Text modelIdentifier = new Text("Model: ");
            modelIdentifier.getStyleClass().add("customtextnormal");
            Text modelDetails = new Text(XMLParser.getTagAttributeElement(aircraftElement, "model", "name"));
            modelDetails.getStyleClass().add("customtext");

            Text lengthIdentifier = new Text("Length: ");
            lengthIdentifier.getStyleClass().add("customtextnormal");
            Text lengthDetails = new Text(XMLParser.getTagAttributeElement(aircraftElement, "length", "metres") + " metres");
            lengthDetails.getStyleClass().add("customtext");

            Text wingspanIdentifier = new Text("Wingspan: ");
            wingspanIdentifier.getStyleClass().add("customtextnormal");
            Text wingspanDetails = new Text(XMLParser.getTagAttributeElement(aircraftElement, "wingspan", "metres") + " metres");
            wingspanDetails.getStyleClass().add("customtext");

            Text heightIdentifier = new Text("Height: ");
            heightIdentifier.getStyleClass().add("customtextnormal");
            Text heightDetails = new Text(XMLParser.getTagAttributeElement(aircraftElement, "height", "metres") + " metres");
            heightDetails.getStyleClass().add("customtext");

            Text fuelCapacityIdentifier = new Text("Fuel capacity: ");
            fuelCapacityIdentifier.getStyleClass().add("customtextnormal");
            Text fuelCapacityDetails = new Text(XMLParser.getTagAttributeElement(aircraftElement, "fuelcapacity", "litres") + " litres");
            fuelCapacityDetails.getStyleClass().add("customtext");

            Text maxSpeedIdentifier = new Text("Max. speed: ");
            maxSpeedIdentifier.getStyleClass().add("customtextnormal");
            Text maxSpeedDetails = new Text(XMLParser.getTagAttributeElement(aircraftElement, "maxspeed", "kmph") + " km/h");
            maxSpeedDetails.getStyleClass().add("customtext");

            var gridpane = new GridPane();
            gridpane.addRow(0, modelIdentifier, modelDetails);
            gridpane.addRow(1, lengthIdentifier, lengthDetails);
            gridpane.addRow(2, wingspanIdentifier, wingspanDetails);
            gridpane.addRow(3, heightIdentifier, heightDetails);
            gridpane.addRow(4, fuelCapacityIdentifier, fuelCapacityDetails);
            gridpane.addRow(5, maxSpeedIdentifier, maxSpeedDetails);

            return gridpane;
    }

    private void landingButtonHandler(ActionEvent event) {
        if (runwayViewHBox.getChildren().contains(sideOnPane)){
            sideOnCanvas.animatePlaneLanding();
        }
        else if (runwayViewHBox.getChildren().contains(topDownPane)){
            topDownCanvas.animatePlaneLanding();
        }
    }

    private void takeoffButtonHandler(ActionEvent event) {
        if (runwayViewHBox.getChildren().contains(sideOnPane)){
            sideOnCanvas.animatePlaneTakeOff();
            //???
            sideOnCanvas.animatePlaneTakeOff();
        }
        else if (runwayViewHBox.getChildren().contains(topDownPane)){
            topDownCanvas.animatePlaneTakeOff();
        }
    }

    /**
     * Switch to the side on runway view.
     *
     * @param event sideOnButton clicked
     */
    private void switchSideOn(ActionEvent event) {
        if (runwayViewHBox.getChildren().contains(topDownPane)) {
            runwayViewHBox.getChildren().remove(topDownPane);
            runwayViewHBox.getChildren().add(1,sideOnPane);
            topDownButton.getStyleClass().remove("midbutton-selected");
            topDownButton.getStyleClass().add("midbutton");
            sideOnButton.getStyleClass().remove("midbutton");
            sideOnButton.getStyleClass().add("midbutton-selected");
        }
    }

    /**
     * Switch to the top down runway view.
     *
     * @param event topDownButton clicked
     */
    private void switchTopDown(ActionEvent event) {
        if (runwayViewHBox.getChildren().contains(sideOnPane)) {
            runwayViewHBox.getChildren().remove(sideOnPane);
            runwayViewHBox.getChildren().add(1,topDownPane);
            sideOnButton.getStyleClass().remove("midbutton-selected");
            sideOnButton.getStyleClass().add("midbutton");
            topDownButton.getStyleClass().remove("midbutton");
            topDownButton.getStyleClass().add("midbutton-selected");
        }
    }

    private void EnableDragging(Canvas canvas, StartCoordinates coordinates) {
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                var x = mouseEvent.getSceneX();
                var y = mouseEvent.getSceneY();
                var tx = canvas.getTranslateX();
                var ty = canvas.getTranslateY();

                coordinates.x = x - tx;
                coordinates.y = y - ty;
                canvas.setCursor(Cursor.MOVE);
            }
        });
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (canvas.getScaleX() > 0) {
                    var x = mouseEvent.getSceneX();
                    var y = mouseEvent.getSceneY();

                    var newX = x - coordinates.x;
                    var newY = y - coordinates.y;
                    var width = canvas.getWidth();
                    var height = canvas.getHeight();
                    var tx = canvas.getTranslateX();
                    var ty = canvas.getTranslateY();
                    var a1 = (width - width * canvas.getScaleX()) / 2;
                    var a2 = (height - height * canvas.getScaleY()) / 2;

                    newX = newX < a1 - 20 ? a1 - 20 : newX;
                    newY = newY < a2 - 20 ? a2 - 20 : newY;

                    newX = newX > -a1 + 20 ? -a1 + 20 : newX;
                    newY = newY > -a2 + 20 ? -a2 + 20 : newY;

                    canvas.setTranslateX(newX);
                    canvas.setTranslateY(newY);
                } else {
                    var x = mouseEvent.getSceneX();
                    var y = mouseEvent.getSceneY();

                    var newX = x - coordinates.x;
                    var newY = y - coordinates.y;
                    var width = canvas.getWidth();
                    var height = canvas.getHeight();
                    var tx = canvas.getTranslateX();
                    var ty = canvas.getTranslateY();
                    var a1 = (width - width * (-canvas.getScaleX())) / 2;
                    var a2 = (height - height * (-canvas.getScaleY())) / 2;

                    newX = newX < a1 - 20 ? a1 - 20 : newX;
                    newY = newY < a2 - 20 ? a2 - 20 : newY;

                    newX = newX > -a1 + 20 ? -a1 + 20 : newX;
                    newY = newY > -a2 + 20 ? -a2 + 20 : newY;

                    canvas.setTranslateX(newX);
                    canvas.setTranslateY(newY);
                }

            }
        });
        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                canvas.setCursor(Cursor.HAND);
            }
        });
        canvas.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                canvas.setCursor(Cursor.HAND);
            }
        });
    }

    private void AddButtons(Canvas canvas, BorderPane bottomPane) {
        var hbox = new HBox();
        hbox.setPadding(new Insets(4,4,4,4));
        hbox.setSpacing(2);
        var zoomInButton = new Button(" + ");
        zoomInButton.getStyleClass().add("smallbutton");
        zoomInButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (canvas.getScaleX() < 0) {
                    if (canvas.getScaleX() <= -5 || canvas.getScaleY() <= -5)
                        return;

                    canvas.setScaleX(canvas.getScaleX() - 0.25);
                    canvas.setScaleY(canvas.getScaleY() - 0.25);
                } else {
                    if (canvas.getScaleX() >= 5 || canvas.getScaleY() >= 5)
                        return;

                    canvas.setScaleX(canvas.getScaleX() + 0.25);
                    canvas.setScaleY(canvas.getScaleY() + 0.25);
                }
            }
        });

        var zoomOutButton = new Button(" - ");
        zoomOutButton.getStyleClass().add("smallbutton");
        zoomOutButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (canvas.getScaleX() < 0) {
                    if (canvas.getScaleX() >= -0.5 || canvas.getScaleY() >= -0.5)
                        return;

                    canvas.setScaleX(canvas.getScaleX() + 0.25);
                    canvas.setScaleY(canvas.getScaleY() + 0.25);
                } else {
                    if (canvas.getScaleX() <= 1 || canvas.getScaleY() <= 1)
                        return;

                    canvas.setScaleX(canvas.getScaleX() - 0.25);
                    canvas.setScaleY(canvas.getScaleY() - 0.25);
                }

            }
        });

        zoomOutButton.setMinWidth(zoomInButton.getWidth());

        var resetButton = new Button("reset");
        resetButton.getStyleClass().add("smallbutton");
        resetButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                canvas.setScaleX(1);
                canvas.setScaleY(1);
                canvas.setTranslateX(0);
                canvas.setTranslateY(0);
                canvas.setRotate(0);
            }
        });

        var takeoffButton = new Button("Simulate take-off");
        takeoffButton.getStyleClass().add("smallbutton");
        var landingButton = new Button("Simulate landing");
        landingButton.getStyleClass().add("smallbutton");
        takeoffButton.setOnAction(this::takeoffButtonHandler);
        landingButton.setOnAction(this::landingButtonHandler);

        var rotateButton = new Button("rotate");
        rotateButton.getStyleClass().add("smallbutton");
        rotateButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (canvas.getRotate() != 0) return;

                int rotationFactor = runway.getRotateValue() - 90;
                double scaleFactor;
                if (rotationFactor > 0) {
                    scaleFactor = -0.3 * Math.sin(Math.toRadians(rotationFactor)) + 1;
                } else {
                    scaleFactor = -0.3 * Math.sin(Math.toRadians(-rotationFactor)) + 1;
                }

                if (scaleFactor > 1) {
                    scaleFactor = 2 - scaleFactor;
                }
                System.out.println("Rotation Factor: " + rotationFactor);
                System.out.println("Rotation scale factor: " + scaleFactor);

                canvas.setScaleX(scaleFactor);
                canvas.setRotate(runway.getRotateValue() - 90);

                if (runway.getRotateValue() >= 180) {
                    canvas.setScaleX(canvas.getScaleX() * -1);
                    canvas.setScaleY(canvas.getScaleY() * -1);
                }
            }
        });

        // Just for insets, ik there are 10000 better ways but this is just brain afk copy paste code from above so I know it works and looks the same.
        var playButtonHbox = new HBox();
        playButtonHbox.setPadding(new Insets(4,4,4,4));
        playButtonHbox.setSpacing(2);
        playButtonHbox.setAlignment(Pos.CENTER);

        if (canvas.getClass().getSimpleName().equals("RunwayTopDownCanvas")) {
            playButtonHbox.getChildren().add(rotateButton);
        }

        playButtonHbox.getChildren().addAll(takeoffButton, landingButton);

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(zoomInButton, zoomOutButton, resetButton);

        bottomPane.setRight(hbox);
        bottomPane.setLeft(playButtonHbox);
    }

    /**
     * Text object generator. Simplifies the code since we don't have to manually enter each text
     * field.
     *
     * @return a text object containing the value " metres"
     */
    private Text metresText() {
        return new Text(" metres");
    }
}
