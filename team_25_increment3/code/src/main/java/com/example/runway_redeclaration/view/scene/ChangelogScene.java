package com.example.runway_redeclaration.view.scene;

import com.example.runway_redeclaration.ChangelogTableObject;
import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.ui.RunwayPane;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * The changelog screen will display a history of notifications within the app.
 */
public class ChangelogScene extends BaseScene {

    /**
     * Root element of the XML file. Used to initialise existing data.
     */
    private Element rootElement;

    /**
     * the table on the changelog scene
     */
    TableView table = new TableView<ChangelogTableObject>();

    private ObservableList<ChangelogTableObject> changelogData = FXCollections.observableArrayList();

    /**
     * Create a new scene, passing in the window the scene will be displayed in
     *
     * @param runwayWindow the runwayWindow to pass in
     */
    public ChangelogScene(RunwayWindow runwayWindow) {
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
        root.getStyleClass().add("background");

        table.setItems(changelogData);
        table.setMinHeight(runwayWindow.getHeight() - 75);
        table.setMaxHeight(runwayWindow.getHeight() - 75);

        var redeclarePane = new StackPane();
        redeclarePane.setMaxWidth(runwayWindow.getWidth());

        root.getChildren().add(redeclarePane);

        var mainPane = new BorderPane();
        redeclarePane.getChildren().add(mainPane);

        // Add components to the main pane as done in the menu scene.
        var changeVBox = new VBox();
        changeVBox.setSpacing(10);
        changeVBox.setPadding(new Insets(4, 4, 4, 4));
        changeVBox.setAlignment(Pos.TOP_CENTER);
        // filter hbox
        Text filterText = new Text("Filter changes by type:");
        HBox filterBox = new HBox();
        filterBox.setSpacing(5);
        filterBox.setAlignment(Pos.CENTER_LEFT);

        //buttons for filter
        Button allButton = new Button("All");
        allButton.getStyleClass().add("midbutton");
        Button ioButton = new Button("I/O");
        ioButton.getStyleClass().add("midbutton");
        Button runwayButton = new Button("Runway");
        runwayButton.getStyleClass().add("midbutton");
        Button obstempButton = new Button("Obstacle template");
        obstempButton.getStyleClass().add("midbutton");
        Button aircraftButton = new Button("Aircraft details");
        aircraftButton.getStyleClass().add("midbutton");
        Button calcButton = new Button("Calculation");
        calcButton.getStyleClass().add("midbutton");

        filterBox.getChildren().addAll(filterText, allButton, ioButton, runwayButton, obstempButton, aircraftButton, calcButton);

        //table
        TableColumn colOne = new TableColumn<ChangelogTableObject, String>("Title");
        colOne.getStyleClass().addAll("customtext", "tablealigncenter", "componentbg");
        colOne.setCellValueFactory(new PropertyValueFactory<ChangelogTableObject, String>("title"));

        TableColumn colTwo = new TableColumn<ChangelogTableObject, String>("Type");
        colTwo.getStyleClass().addAll("customtextblack", "tablealigncenter", "lightbackground");
        colTwo.setCellValueFactory(new PropertyValueFactory<ChangelogTableObject, String>("type"));

        TableColumn colThree = new TableColumn<ChangelogTableObject, String>("Description");
        colThree.getStyleClass().addAll("customtextblack", "tablealigncenter", "lightbackground");
        colThree.setCellValueFactory(new PropertyValueFactory<ChangelogTableObject, String>("desc"));

        TableColumn colFour = new TableColumn<ChangelogTableObject, String>("Timestamp");
        colFour.setSortType(TableColumn.SortType.DESCENDING);
        colFour.getStyleClass().addAll("customtextblack", "tablealigncenter", "lightbackground");
        colFour.setCellValueFactory(new PropertyValueFactory<ChangelogTableObject, String>("time"));

        TableColumn colFive = new TableColumn<ChangelogTableObject, String>("Username");
        colFive.getStyleClass().addAll("customtextblack", "tablealigncenter", "lightbackground");
        colFive.setCellValueFactory(new PropertyValueFactory<ChangelogTableObject, String>("name"));

        colOne.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
        colOne.setResizable(false);

        colTwo.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        colTwo.setResizable(false);

        colThree.prefWidthProperty().bind(table.widthProperty().multiply(0.5));
        colThree.setResizable(false);

        colFour.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        colFour.setResizable(false);

        colFive.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
        colFive.setResizable(false);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(colOne, colTwo, colThree, colFour, colFive);
        table.getSortOrder().add(colFour);

        //read from XML file
        initialiseRootElement();
        initialiseNotifs();

        // filter table data based on selected button
        allButton.setOnAction(e -> {
            table.setItems(changelogData);
        });

        ioButton.setOnAction(event -> {
            var filteredData = changelogData.filtered(obj -> obj.getType().equals("I/O"));
            table.setItems(filteredData);
        });


        runwayButton.setOnAction(event -> {
            var filteredData = changelogData.filtered(obj -> obj.getType().equals("Runway"));
            table.setItems(filteredData);
        });

        obstempButton.setOnAction(event -> {
            var filteredData = changelogData.filtered(obj -> obj.getType().equals("Obstacle template"));
            table.setItems(filteredData);
        });

        aircraftButton.setOnAction(event -> {
            var filteredData = changelogData.filtered(obj -> obj.getType().equals("Aircraft details"));
            table.setItems(filteredData);
        });

        calcButton.setOnAction(event -> {
            var filteredData = changelogData.filtered(obj -> obj.getType().equals("Calculation"));
            table.setItems(filteredData);
        });


        changeVBox.getChildren().addAll(table);

        mainPane.setCenter(changeVBox);

        // Add custom toolbar with back button
        var changelogBorderpane = new BorderPane();
        var changelogTitle = new Text("CHANGELOG");
        changelogTitle.getStyleClass().add("bigtitle");
        var toolbar = new HBox();
        toolbar.setPadding(new Insets(10));
        toolbar.setSpacing(10);

        var backButton = new Button("Back");
        backButton.getStyleClass().add("midbutton");
        backButton.setOnAction(e -> {
            runwayWindow.startMenu();
        });

        toolbar.getChildren().addAll(backButton, filterBox);
        changelogBorderpane.setLeft(toolbar);
        changelogBorderpane.setCenter(changelogTitle);
        mainPane.setTop(changelogBorderpane);

        table.setItems(changelogData);
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
     * Initialise the existing changelog elements from the XML file
     */
    private void initialiseNotifs() {
        if (rootElement.getElementsByTagName("notification") == null) {
            return;
        }

        int i = 0;
        Element notifElement = (Element) rootElement.getElementsByTagName("notification").item(i);

        while (notifElement != null) {
            // Get this notifications's elements
            String title = XMLParser.getTagAttributeElement(notifElement, "title", "title");
            String type = XMLParser.getTagAttributeElement(notifElement, "type", "type");
            String desc = XMLParser.getTagAttributeElement(notifElement, "description", "description");
            String time = XMLParser.getTagAttributeElement(notifElement, "time", "time");
            String name = XMLParser.getTagAttributeElement(notifElement, "username", "username");
            table.getItems().add(new ChangelogTableObject(title, type, desc, time, name));

            notifElement = (Element) rootElement.getElementsByTagName("notification").item(++i);
        }
    }
}
