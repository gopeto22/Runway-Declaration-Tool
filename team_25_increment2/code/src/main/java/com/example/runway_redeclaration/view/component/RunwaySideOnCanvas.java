package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.model.Runway;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * A canvas to display the side on view.
 */
public class RunwaySideOnCanvas extends RunwayTopDownCanvas {

    /**
     * Constructor to create a new side on runway view.
     *
     * @param width the width of the canvas
     * @param height the height of the canvas
     * @param runway the runway being represented
     */
    public RunwaySideOnCanvas(double width, double height, Runway runway) {
        super(width, height, runway);
    }

    /**
     * Draw the runway, outline.
     */
    @Override
    protected void drawRunway() {
        runwayStartY = height * 0.495;
        runwayHeight = height * 0.01;

        // Runway.
        gc.setFill(runwayColor);
        gc.fillRect(runwayStartX, runwayStartY, runwayWidth, runwayHeight);
    }

    /**
     * Draw the obstacle on the runway.
     */
    @Override
    protected void drawObstacle() {
        if (runway.getObstacle() == null) {return;}

        // Obstacle.
        // Dont normalise??
        double obstacleSize = Math.log(runway.getObstacle().getHeight()) * 10;
        double obstacleX;

        if (LEFT_RUNWAY) {
            obstacleX = runwayStartX + normaliseToRunway(runway.getObstacle().getDistanceFromThreshold());
        } else {
            obstacleX = runwayStartX + runwayWidth - normaliseToRunway(runway.getObstacle().getDistanceFromThreshold());
        }
        double obstacleY = height * 0.5;

        gc.setStroke(Color.BLACK);
        gc.setFill(obstacleColor);

        gc.fillRect(obstacleX - obstacleSize / 2, obstacleY - obstacleSize, obstacleSize, obstacleSize);
    }
}
