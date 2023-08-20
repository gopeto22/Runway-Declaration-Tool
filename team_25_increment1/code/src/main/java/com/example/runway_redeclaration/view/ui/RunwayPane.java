package com.example.runway_redeclaration.view.ui;

import com.example.runway_redeclaration.model.Obstacle;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 * The Runway Pane is a special pane which will scale anything inside it to the screen and
 * maintain the aspect ratio.
 * <p>
 * Drawing will be scaled appropriately.
 * <p>
 * This takes the worry about the layout out and will allow the app to scale to any resolution
 * easily.
 * <p>
 * It uses the width and height given which should match the main window size. This will be
 * the base drawing resolution, but will be scaled up or down as the window is resized.
 * <p>
 */
public class RunwayPane extends StackPane {

    private final int width;
    private final int height;
    private double scalar = 1;
    private final boolean autoScale = false;

    /**
     * Create a new scalable RunwayPane with the given drawing width and height.
     *
     * @param width  width
     * @param height height
     */
    public RunwayPane(int width, int height) {
        super();
        this.width = width;
        this.height = height;

        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Update the scalar being used by this draw pane
     *
     * @param scalar scalar
     */
    protected void setScalar(double scalar) {
        this.scalar = scalar;
    }

    /**
     * Use a Graphics Transformation to scale everything inside this pane. Padding is added to the
     * edges to maintain the correct aspect ratio and keep the display centred.
     */
    @Override
    public void layoutChildren() {
        super.layoutChildren();

        if (!autoScale) {
            return;
        }

        //Work out the scale factor height and width
        var scaleFactorHeight = getHeight() / height;
        var scaleFactorWidth = getWidth() / width;

        //Work out whether to scale by width or height
        if (scaleFactorHeight > scaleFactorWidth) {
            setScalar(scaleFactorWidth);
        } else {
            setScalar(scaleFactorHeight);
        }

        //Set up the scale
        Scale scale = new Scale(scalar, scalar);

        //Get the parent width and height
        var parentWidth = getWidth();
        var parentHeight = getHeight();

        //Get the padding needed on the top and left
        var paddingLeft = (parentWidth - (width * scalar)) / 2.0;
        var paddingTop = (parentHeight - (height * scalar)) / 2.0;

        //Perform the transformation
        Translate translate = new Translate(paddingLeft, paddingTop);
        scale.setPivotX(0);
        scale.setPivotY(0);
        getTransforms().setAll(translate, scale);
    }

    public void addObstacle(Obstacle obstacle) {
    }
}


//package com.example.runway_redeclaration.controller;
//
//import com.example.runway_redeclaration.model.Obstacle;
//import com.example.runway_redeclaration.model.Runway;
//import com.example.runway_redeclaration.view.scene.RedeclareScene;
//import javafx.event.ActionEvent;
//import javafx.event.Event;
//import javafx.event.EventHandler;
//import javafx.geometry.Bounds;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.*;
//import javafx.scene.transform.Scale;
//import javafx.scene.transform.Translate;
//import javafx.stage.Stage;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * The Game RunwayPane is a special pane which will scale anything inside it to the screen and maintain the aspect ratio.
// *
// * Drawing will be scaled appropriately.
// *
// * This takes the worry about the layout out and will allow the game to scale to any resolution easily.
// *
// * It uses the width and height given which should match the main window size. This will be the base drawing resolution,
// * but will be scaled up or down as the window is resized.
// *
// * You should not need to modify this class
// */
//public class RunwayPane extends StackPane {
//
//    private final int width;
//    private final int height;
//    private double scalar = 1;
//    private final boolean autoScale = true;
//    private Node node;
//    private Obstacle obstacle = new Obstacle("Building",50,10,20,100);
//
//    /**
//     * Create a new scalable GamePane with the given drawing width and height.
//     * @param width width
//     * @param height height
//     */
//    public RunwayPane(int width, int height) {
//        super();
//        this.width = width;
//        this.height = height;
//
//        setAlignment(Pos.TOP_LEFT);
//        //Adding a button object and setting the label
//        Button addButton = new Button("Add an Obstacle ");
//
//        // Create an EventHandler for the button click event
//        EventHandler<ActionEvent> buttonClickHandler = this::handleButtonClick;
//
//        //Adding a new HBox object and add the button to it
//        HBox boxForButton = new HBox();
//        boxForButton.getChildren().add(addButton);
//
//        //Make the HBox appear at the bottom of the Runway Pane
//        addButton.setOnAction(e -> {
//
//            // Create a new obstacle object
//            // Create a new Scene which will show the ObstacleForm instance
//            // Develop a new Stage which will show the obstacle
//            Stage stageOfObstacle = new Stage();
//            stageOfObstacle.setTitle("Add an obstacle: ");
//            //Present the scene with an obstacle
//            stageOfObstacle.show();
//        });
//
//        getChildren().add(boxForButton);
//    }
//
//    /**
//     * Update the scalar being used by this draw pane
//     * @param scalar scalar
//     */
//    protected void setScalar(double scalar) {
//        this.scalar = scalar;
//    }
//
//    /**
//     * Use a Graphics Transformation to scale everything inside this pane. Padding is added to the edges to maintain
//     * the correct aspect ratio and keep the display centred.
//     */
//    @Override
//    public void layoutChildren() {
//        super.layoutChildren();
//
//        if(!autoScale) {
//            return;
//        }
//
//        //Work out the scale factor height and width
//        var scaleFactorHeight = getHeight() / height;
//        var scaleFactorWidth = getWidth() / width;
//
//        //Work out whether to scale by width or height
//        if (scaleFactorHeight > scaleFactorWidth) {
//            setScalar(scaleFactorWidth);
//        } else {
//            setScalar(scaleFactorHeight);
//        }
//
//        //Set up the scale
//        Scale scale = new Scale(scalar,scalar);
//
//        //Get the parent width and height
//        var parentWidth = getWidth();
//        var parentHeight = getHeight();
//
//        //Get the padding needed on the top and left
//        var paddingLeft = (parentWidth - (width * scalar)) / 2.0;
//        var paddingTop = (parentHeight - (height * scalar)) / 2.0;
//
//        //Perform the transformation
//        Translate translate = new Translate(paddingLeft, paddingTop);
//        scale.setPivotX(0);
//        scale.setPivotY(0);
//        getTransforms().setAll(translate, scale);
//    }
//    private void handleButtonClick(ActionEvent event){
//    }
//
//    /**
//     * A method for adding obstacle to the runway.
//     * Adds the obstacle object to a list of obstacles on the runway and
//     * helps with the recalculation on the runway to present the new obstacle
//     * @param runway
//     * @param obstacles
//     */
//    public void addObstacleToRunway(Runway runway, List<Obstacle> obstacles) {
//        // get the obstacle information from the obstacle pane
//        TextField nameField = obstacle.getNameComponent();
//        TextField heightField = obstacle.getHeightComponent();
//        TextField nFromCentreField = obstacle.getnFromCentreComponent();
//        TextField eFromCentreField = obstacle.geteFromCentreComponent();
//        TextField distanceField = obstacle.getDistanceFromThresholdComponent();
//
//        // create a new Obstacle object with the information from the fields
//        Obstacle newObstacle = new Obstacle(nameField.getText(), Integer.parseInt(heightField.getText()),
//                Integer.parseInt(nFromCentreField.getText()), Integer.parseInt(eFromCentreField.getText()),
//                Integer.parseInt(distanceField.getText()));
//
//        // add the new obstacle to the list of obstacles
//        obstacles.add(newObstacle);
//
//        // update the runway object to include the new obstacle
//        runway.addObstacle(newObstacle);
//
//        // update the view to show the new obstacle
//        // ...
//    }
//    /**
//     * A method for removing an obstacle from the runway.
//     * Remove an existing obstacle from the runway.
//     * Removes the obstacle object from the list of obstacles on the runway
//     * and helps with the recalculation on the runway to remove this obstacle
//     * @param runway
//     * @param obstacles
//     */
//    public void removeObstacleFromRunway(Runway runway, List<Obstacle> obstacles) {
//        // remove the obstacle from the list of obstacles
//        obstacles.remove(obstacle);
//
//        // remove the obstacle from the runway
//        runway.removeObstacle(obstacle);
//
//        // update the reflect the changes
//        // ...
//    }
//    /**
//     * Checks if an obstacle overlaps with another obstacle
//     * Prevents the runway from becoming unsafe because of
//     * overlapping obstacles. Before adding the obstacle.
//     * @param runway
//     * @param obstacles
//     * @return true if the pane needs to be redrawn, false otherwise.
//     */
//    public void checkOverlap(Runway runway, List<Obstacle> obstacles) {
//        for (int i = 0; i < obstacles.size(); i++) {
//            Obstacle obstacle1 = obstacles.get(i);
//            for (int j = i + 1; j < obstacles.size(); j++) {
//                Obstacle obstacle2 = obstacles.get(j);
//                if (obstacle1.getPositionX() + obstacle1.getWidth() > obstacle2.getPositionX() &&
//                        obstacle1.getPositionX() < obstacle2.getPositionX() + obstacle2.getWidth() &&
//                        obstacle1.getPositionY() + obstacle1.getHeight() > obstacle2.getPositionY() &&
//                        obstacle1.getPositionY() < obstacle2.getPositionY() + obstacle2.getHeight()) {
//                }
//            }
//        }
//    }
//
//    /**
//     * Method for clearing all the obstacles from the runway
//     * @param runway
//     * @param obstacles
//     */
//    public void clearObstacles(Runway runway, List<Obstacle> obstacles){
//        obstacles.clear();
//    }
//    public void getObstacles(Runway runway, List<Obstacle> obstacles){
//        obstacles.addAll((Collection<? extends Obstacle>) runway.getObstacle());
//    }
//
//
//    }
//
//
//
