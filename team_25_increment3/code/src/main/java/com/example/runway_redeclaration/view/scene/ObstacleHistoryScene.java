package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.ObstacleHistoryTableObject;
import com.example.runway_redeclaration.view.component.RunwayBox;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * The changelog screen will display a history of notifications within the app.
 */
public class ObstacleHistoryScene extends BaseScene {

    /**
     * the table on the changelog scene
     */
    TableView table = new TableView<ObstacleHistoryTableObject>();

    private ObservableList<ObstacleHistoryTableObject> obsHistoryData = FXCollections.observableArrayList();

    /**
     * The designator of the runway
     */
    private RunwayBox runwayBox;

    /**
     * Create a new scene, passing in the window the scene will be displayed in and the runwayBox it was initialised from
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public ObstacleHistoryScene(RunwayWindow runwayWindow, RunwayBox runwayBox) {
        super(runwayWindow);
        this.runwayBox = runwayBox;
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
        root.getStyleClass().add("background");

        table.setItems(obsHistoryData);
        table.setMinHeight(runwayWindow.getHeight());
        table.setMaxHeight(runwayWindow.getHeight());

        var obsHistoryPane = new StackPane();
        obsHistoryPane.setMaxWidth(runwayWindow.getWidth());
        obsHistoryPane.setMaxHeight(runwayWindow.getHeight());

        root.getChildren().add(obsHistoryPane);

        var mainPane = new BorderPane();
        obsHistoryPane.getChildren().add(mainPane);

        // Add components to the main pane as done in the menu scene.
        var historyVBox = new VBox();
        historyVBox.setSpacing(10);
        historyVBox.setPadding(new Insets(4, 4, 4, 4));
        historyVBox.setAlignment(Pos.TOP_CENTER);

        //table
        TableColumn colOne = new TableColumn<ObstacleHistoryTableObject, String>("Obstacle Name");
        colOne.setCellValueFactory(new PropertyValueFactory<ObstacleHistoryTableObject, String>("name"));
        colOne.getStyleClass().addAll("componentbg","customtext","tablealigncenter");

        TableColumn colTwo = new TableColumn<ObstacleHistoryTableObject, String>("Time Added");
        colTwo.setCellValueFactory(new PropertyValueFactory<ObstacleHistoryTableObject, String>("add"));
        colTwo.getStyleClass().addAll("background","customtext","tablealigncenter");

        TableColumn colThree= new TableColumn<ObstacleHistoryTableObject, String>("Time Deleted");
        colThree.setCellValueFactory(new PropertyValueFactory<ObstacleHistoryTableObject, String>("del"));
        colThree.getStyleClass().addAll("background","customtext","tablealigncenter");

        colOne.prefWidthProperty().bind(table.widthProperty().multiply(0.33));
        colOne.setResizable(false);

        colTwo.prefWidthProperty().bind(table.widthProperty().multiply(0.33));
        colTwo.setResizable(false);

        colThree.prefWidthProperty().bind(table.widthProperty().multiply(0.33));
        colThree.setResizable(false);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(colOne,colTwo,colThree);

        historyVBox.getChildren().addAll(table);
        mainPane.setCenter(historyVBox);

        //initialise elements
        initialiseObsHistory();

        // Title
        var title = new Text("Obstacle history data for runway " + runwayBox.getDesignatorValue());
        title.setTextAlignment(TextAlignment.CENTER);
        title.getStyleClass().add("bigtitle");

        mainPane.setTop(title);
    }

    /**
     * Initialise the existing obstacle history data
     */
    private void initialiseObsHistory() {
        if (runwayBox.getObstacleNameHistory().length() == 0) {
            return;
        }

        String[] obsNames = runwayBox.getObstacleNameHistory().split(";");
        String[] obsTimeAdd = runwayBox.getObstacleTimeAddedHistory().split(";");
        String[] obsTimeDel;
        if (runwayBox.getObstacleTimeAddedHistory().length() > 0) {
            obsTimeDel = runwayBox.getObstacleTimeDeletedHistory().split(";");
        } else {
            obsTimeDel = new String[]{""};
        }


        Integer del = obsTimeDel.length;

        try {
            for (int i = 0; i < del; i++) {
                table.getItems().add(new ObstacleHistoryTableObject(obsNames[i], obsTimeAdd[i], obsTimeDel[i]));
            }

            table.getItems().add(new ObstacleHistoryTableObject(obsNames[del], obsTimeAdd[del], ""));
        }catch (IndexOutOfBoundsException e){
            //e.printStackTrace();
        }
    }
}
