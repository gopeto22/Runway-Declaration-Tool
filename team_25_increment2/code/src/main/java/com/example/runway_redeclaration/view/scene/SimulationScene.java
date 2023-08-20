package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.BreakdownTableObject;
import com.example.runway_redeclaration.view.component.RunwaySideOnCanvas;
import com.example.runway_redeclaration.view.component.RunwayTopDownCanvas;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import com.example.runway_redeclaration.model.Runway;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

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
     * The runway to be visualised.
     */
    private Runway runway;

    /**
     * Create a new scene, passing in the window the scene will be displayed in
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public SimulationScene(RunwayWindow runwayWindow, Runway runway) {
        super(runwayWindow);
        this.runway = runway;
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
        simulationVBox.setSpacing(10);
        simulationVBox.setPadding(new Insets(4, 4, 4, 4));
        simulationVBox.setAlignment(Pos.TOP_CENTER);

        // Placeholder images.
        var splitPane = new SplitPane();

        //var image1 = new Image(getClass().getResource("/images/airplane.jpeg").toExternalForm());
//        var image2 = new Image(getClass().getResource("/images/airplane.jpeg").toExternalForm());

        //var imageView1 = new ImageView();
        //imageView1.setFitWidth(350);
        //imageView1.setFitHeight(200);
        //imageView1.setImage(image1);

//        var imageView2 = new ImageView();
//        imageView2.setFitWidth(350);
//        imageView2.setFitHeight(200);
//        imageView2.setImage(image2);

        double canvasWidth = runwayWindow.getWidth() * 0.49;
        double canvasHeight = runwayWindow.getHeight() * 0.49;

        topDownCanvas = new RunwayTopDownCanvas(canvasWidth, canvasHeight, runway);
        sideOnCanvas = new RunwaySideOnCanvas(canvasWidth, canvasHeight, runway);

        EnableDragging(topDownCanvas, topDownStartCoordinates);
        //TODO add after side view visualisation is complete
        EnableDragging(sideOnCanvas, sideViewStartCoordinates);

        var left = new BorderPane();
        left.setCenter(topDownCanvas);
        left.setBottom(AddZoomButtons(topDownCanvas));

        var right = new BorderPane();
        right.setCenter(sideOnCanvas);
        //TODO add after side view visualisation is complete
        right.setBottom(AddZoomButtons(sideOnCanvas));

        splitPane.getItems().addAll(left, right);

        // Original/Redeclared values HBox
        var originalRedeclaredHBox = new HBox();
        originalRedeclaredHBox.setSpacing(10);
        originalRedeclaredHBox.setPadding(new Insets(4, 4, 4, 4));

        // Original values VBox
        var originalVBox = new VBox();

        var originalTitle = new Text("Original");
        originalTitle.setTextAlignment(TextAlignment.CENTER);

        // Bound original runway values.
        var originalToraText = new Text();
        originalToraText.setTextAlignment(TextAlignment.LEFT);
        originalToraText.textProperty().bind(runway.originalToraProperty().asString());

        var originalTodaText = new Text();
        originalTodaText.setTextAlignment(TextAlignment.LEFT);
        originalTodaText.textProperty().bind(runway.originalTodaProperty().asString());

        var originalAsdaText = new Text();
        originalAsdaText.setTextAlignment(TextAlignment.LEFT);
        originalAsdaText.textProperty().bind(runway.originalAsdaProperty().asString());

        var originalLdaText = new Text();
        originalLdaText.setTextAlignment(TextAlignment.LEFT);
        originalLdaText.textProperty().bind(runway.originalLdaProperty().asString());

        // Value wrappers
        var originalToraHBox = new HBox();
        originalToraHBox.getChildren().addAll(new Text("TORA: "), originalToraText, metresText());

        var originalTodaHBox = new HBox();
        originalTodaHBox.getChildren().addAll(new Text("TODA: "), originalTodaText, metresText());

        var originalAsdaHBox = new HBox();
        originalAsdaHBox.getChildren().addAll(new Text("ASDA: "), originalAsdaText, metresText());

        var originalLdaHBox = new HBox();
        originalLdaHBox.getChildren().addAll(new Text("LDA: "), originalLdaText, metresText());

        originalVBox.getChildren()
                .addAll(originalTitle, originalToraHBox, originalTodaHBox, originalAsdaHBox,
                        originalLdaHBox);

        // Redeclared values VBox.
        var redeclaredVBox = new VBox();

        var redeclaredTitle = new Text("Redeclared");
        redeclaredTitle.setTextAlignment(TextAlignment.CENTER);

        // Bound redeclared runway values.
        var redeclaredToraText = new Text();
        redeclaredToraText.setTextAlignment(TextAlignment.LEFT);
        redeclaredToraText.textProperty().bind(runway.redeclaredToraProperty().asString());

        var redeclaredTodaText = new Text();
        redeclaredTodaText.setTextAlignment(TextAlignment.LEFT);
        redeclaredTodaText.textProperty().bind(runway.redeclaredTodaProperty().asString());

        var redeclaredAsdaText = new Text();
        redeclaredAsdaText.setTextAlignment(TextAlignment.LEFT);
        redeclaredAsdaText.textProperty().bind(runway.redeclaredAsdaProperty().asString());

        var redeclaredLdaText = new Text();
        redeclaredLdaText.setTextAlignment(TextAlignment.LEFT);
        redeclaredLdaText.textProperty().bind(runway.redeclaredLdaProperty().asString());

        // Value wrappers
        var redeclaredToraHBox = new HBox();
        redeclaredToraHBox.getChildren().addAll(new Text("TORA: "), redeclaredToraText, metresText());

        var redeclaredTodaHBox = new HBox();
        redeclaredTodaHBox.getChildren().addAll(new Text("TODA: "), redeclaredTodaText, metresText());

        var redeclaredAsdaHBox = new HBox();
        redeclaredAsdaHBox.getChildren().addAll(new Text("ASDA: "), redeclaredAsdaText, metresText());

        var redeclaredLdaHBox = new HBox();
        redeclaredLdaHBox.getChildren().addAll(new Text("LDA: "), redeclaredLdaText, metresText());

        redeclaredVBox.getChildren()
                .addAll(redeclaredTitle, redeclaredToraHBox, redeclaredTodaHBox, redeclaredAsdaHBox,
                        redeclaredLdaHBox);

        originalRedeclaredHBox.getChildren().addAll(originalVBox, redeclaredVBox);

        // Recalculate the runway.
        runway.recalculate();

        // Breakdown of calculations box
        VBox breakdownBox = new VBox();

        var breakdownTitle = new Text("Breakdown of calculations");
        breakdownTitle.setTextAlignment(TextAlignment.CENTER);

        var typeTitle = new Text(runway.getEqType());

        //table of breakdown calculation
        TableView table = new TableView<BreakdownTableObject>();

        TableColumn colOne = new TableColumn<BreakdownTableObject, String>("Equation");
        colOne.setCellValueFactory(new PropertyValueFactory<BreakdownTableObject, String>("eq"));

        TableColumn colTwo = new TableColumn<BreakdownTableObject, String>("Breakdown of Calculations");
        colTwo.setCellValueFactory(new PropertyValueFactory<BreakdownTableObject, String>("calc"));

        TableColumn colThree = new TableColumn<BreakdownTableObject, Integer>("Result");
        colThree.setCellValueFactory(new PropertyValueFactory<BreakdownTableObject, Integer>("result"));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getItems().addAll(new BreakdownTableObject(runway.getToraEq(), runway.getToraCalc(), runway.getRedeclaredTora()));
        table.getItems().addAll(new BreakdownTableObject(runway.getTodaEq(), runway.getTodaCalc(), runway.getRedeclaredToda()));
        table.getItems().addAll(new BreakdownTableObject(runway.getAsdaEq(), runway.getAsdaCalc(), runway.getRedeclaredAsda()));
        table.getItems().addAll(new BreakdownTableObject(runway.getLdaEq(), runway.getLdaCalc(), runway.getRedeclaredLda()));

        table.getColumns().addAll(colOne, colTwo, colThree);
        breakdownBox.getChildren().addAll(breakdownTitle, typeTitle, table);

        // Add components to the screen.
        simulationVBox.getChildren().addAll(splitPane, originalRedeclaredHBox, breakdownBox);
        mainPane.setTop(simulationVBox);
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

    private HBox AddZoomButtons(Canvas canvas) {
        var hbox = new HBox();
        var zoomInButton = new Button("+");
        zoomInButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (canvas.getScaleX() >= 5 || canvas.getScaleY() >= 5)
                    return;

                canvas.setScaleX(canvas.getScaleX() + 0.25);
                canvas.setScaleY(canvas.getScaleY() + 0.25);
            }
        });

        var zoomOutButton = new Button("-");
        zoomOutButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (canvas.getScaleX() <= 1 || canvas.getScaleY() <= 1)
                    return;

                canvas.setScaleX(canvas.getScaleX() - 0.25);
                canvas.setScaleY(canvas.getScaleY() - 0.25);
            }
        });

        zoomOutButton.setMinWidth(zoomInButton.getWidth());
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.getChildren().addAll(zoomInButton, zoomOutButton);

        return hbox;
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
