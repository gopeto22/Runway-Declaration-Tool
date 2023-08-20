package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.UIColor;
import com.example.runway_redeclaration.model.Runway;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A canvas to display the side on view.
 */
public class RunwaySideOnCanvas extends RunwayTopDownCanvas {

    /**
     * Centre of obstacle coordinate on the X axis.
     */
    private double obstacleX;

    /**
     * Center of obstacle coordinate on the Y axis.
     */
    private double obstacleY;

    /**
     * Length of obstacle (x length)
     */
    double obstacleLength;

    /**
     * Height of obstacle (y length)
     */
    double obstacleHeight;

    /**
     * slope line vars
     */
    Integer alsValue = null;
    Integer tocsValue = null;
    Integer resaValue = null;

    /**
     * Constructor to create a new side on runway view.
     *
     * @param width the width of the canvas
     * @param height the height of the canvas
     * @param runway the runway being represented
     */
    public RunwaySideOnCanvas(double width, double height, Runway runway, UIColor uiColor) {
        super(width, height, runway, uiColor);
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
        obstacleHeight = Math.log(runway.getObstacle().getHeight()) * 10;
        obstacleLength = Math.log(runway.getObstacle().getObsLength()) * 10;

        if (LEFT_RUNWAY) {
            obstacleX = runwayStartX + normaliseToRunwayX(runway.getObstacle().getDistanceFromThreshold()) + normaliseToRunwayX(runway.getDisplacedThreshold());
        } else {
            obstacleX = runwayStartX + runwayWidth - normaliseToRunwayX(runway.getObstacle().getDistanceFromThreshold()) - normaliseToRunwayX(runway.getDisplacedThreshold());
        }
        obstacleY = height * 0.5;

        gc.setStroke(Color.BLACK);
        gc.setFill(obstacleColor);

        gc.fillRect(obstacleX - obstacleLength / 2, obstacleY - obstacleHeight, obstacleLength, obstacleHeight);

        // Add the angle line
        var redeclaredLdaMap = runway.getRedeclaredLdaMap();
        alsValue = redeclaredLdaMap.get("ALS");
        tocsValue = redeclaredLdaMap.get("Slope Calculation");
        resaValue = redeclaredLdaMap.get("RESA");
        gc.setFill(Color.BLACK);
        gc.setLineWidth(1);

        if (alsValue != null) {
            drawAngleLine(normaliseToRunwayX(alsValue));
        }
        if (tocsValue != null) {
            drawAngleLine(normaliseToRunwayX(tocsValue));
        }
        if (resaValue != null) {
            drawAngleLine(normaliseToRunwayX(resaValue));
        }
    }

    /**
     * Draw an angle line from the top of the obstacle to the runway.
     *
     * @param length length in terms of x only for the angle line
     */
    private void drawAngleLine(double length) {
        gc.setLineWidth(2);
        if (runway.isLandingTowards() <= 0) {
            if (LEFT_RUNWAY) {
                gc.strokeLine(obstacleX, obstacleY - obstacleHeight, obstacleX + length, runwayStartY);
            } else {
                gc.strokeLine(obstacleX, obstacleY - obstacleHeight, obstacleX - length, runwayStartY);
            }
        } else {
            if (LEFT_RUNWAY) {
                gc.strokeLine(obstacleX, obstacleY - obstacleHeight, obstacleX - length, runwayStartY);
            } else {
                gc.strokeLine(obstacleX, obstacleY - obstacleHeight, obstacleX + length, runwayStartY);
            }
        }
    }

    /**
     * Animate a plane moving - taking off
     */
    public void animatePlaneTakeOff(){
        //stop timeline if already moving
        if (timeline != null && timer != null) {
            timer.stop();
            timeline.stop();
            build();
        }

        Image image = null;
        DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();
        double startX;
        double startY;
        double endX;
        double endY;
        //if obstacle below line then go behind
        boolean behindObs = (runway.getObstacle()!=null) && (!runway.isObstacleAboveLine());

        //if no recalculation done, just go along runway
        if (runway.isLandingTowards() == 0){
            double duration = ((runwayStartX + runwayWidth - runwayStartX)/runwayWidth)*5;

            if (LEFT_RUNWAY){
                startX = runwayStartX;
                startY = runwayStartY - 25;
                endX = runwayStartX + runwayWidth - 40;
                endY = runwayStartY - 25;
                image = new Image(getClass().getResource("/images/sl.png").toExternalForm());
            }
            else {
                startX = width - runwayStartX - 40;
                startY = runwayStartY - 25;
                endX = width - runwayStartX - runwayWidth;
                endY = runwayStartY - 25;
                image = new Image(getClass().getResource("/images/sr.png").toExternalForm());
            }

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(x, startX),
                            new KeyValue(y, startY)
                    ),
                    new KeyFrame(Duration.seconds(duration),
                            new KeyValue(x, endX),
                            new KeyValue(y, endY)
                    ),
                    new KeyFrame(Duration.seconds(duration), e -> {
                        timer.stop();
                        timeline.stop();
                        build();
                    })
            );
        }
        //is taking off away from obstacle
        else if (runway.isLandingTowards() == -1) {
            double duration = ((runwayStartX + runwayWidth - 40) - (runwayStartX + runwayWidth - normaliseToRunwayX(runway.getRedeclaredTora()))) / runwayWidth * 5;

            if (LEFT_RUNWAY) {
                startX = runwayStartX + runwayWidth - normaliseToRunwayX(runway.getRedeclaredTora());
                startY = runwayStartY + (runwayHeight / 2) - 25;
                endX = runwayStartX + runwayWidth - 40;
                endY = runwayStartY + (runwayHeight / 2) - 25;
                image = new Image(getClass().getResource("/images/sl.png").toExternalForm());
            } else {
                startX = width - runwayStartX - runwayWidth + normaliseToRunwayX(runway.getRedeclaredTora()) - 40;
                startY = runwayStartY + (runwayHeight / 2) - 25;
                endX = width - runwayStartX - runwayWidth;
                endY = runwayStartY + (runwayHeight / 2) - 25;
                image = new Image(getClass().getResource("/images/sr.png").toExternalForm());
            }

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(x, startX),
                            new KeyValue(y, startY)
                    ),
                    new KeyFrame(Duration.seconds(duration),
                            new KeyValue(x, endX),
                            new KeyValue(y, endY)
                    ),
                    new KeyFrame(Duration.seconds(duration), e -> {
                        timer.stop();
                        timeline.stop();
                        build();
                    })
            );
        }
        //is taking off towards obstacle
        else if (runway.isLandingTowards() == 1){
            double slopeStart = 0;
            if (tocsValue != null) {
                slopeStart = tocsValue;
            }
            else if (resaValue != null) {
                slopeStart = resaValue;
            }

            double tempObsX = runwayStartX + normaliseToRunwayX(runway.getObstacle().getDistanceFromThreshold()) + normaliseToRunwayX(runway.getDisplacedThreshold());

            double midX;
            double midY;
            double duration1 = ((tempObsX-slopeStart-40)-runwayStartX)/runwayWidth*5;
            double duration2 = ((tempObsX-40)-runwayStartX)/runwayWidth*5;

            if (LEFT_RUNWAY) {
                startX = runwayStartX;
                startY = runwayStartY + (runwayHeight / 2) - 25;
                midX = obstacleX - slopeStart - 40;
                midY = runwayStartY + (runwayHeight / 2) - 25;
                endX = obstacleX - 40;
                endY = obstacleY - obstacleHeight - 25;
                image = new Image(getClass().getResource("/images/sl.png").toExternalForm());
            } else {
                startX = width - runwayStartX - 40;
                startY = runwayStartY + (runwayHeight / 2) - 25;
                midX = obstacleX + slopeStart;
                midY = runwayStartY + (runwayHeight / 2) - 25;
                endX = obstacleX;
                endY = obstacleY - obstacleHeight - 25;
                image = new Image(getClass().getResource("/images/sr.png").toExternalForm());
            }

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(x, startX),
                            new KeyValue(y, startY)
                    ),
                    new KeyFrame(Duration.seconds(duration1),
                            new KeyValue(x, midX),
                            new KeyValue(y, midY)
                    ),
                    new KeyFrame(Duration.seconds(duration2),
                            new KeyValue(x, endX),
                            new KeyValue(y, endY)
                    ),
                    new KeyFrame(Duration.seconds(duration2), e -> {
                        timer.stop();
                        timeline.stop();
                        build();
                    })
            );
        }

        Image finalImage = image;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                build();
                gc.drawImage(finalImage, x.doubleValue(), y.doubleValue(), 40, 40);
                if (behindObs){
                    drawObstacle();
                }
            }
        };

        timer.start();
        timeline.play();
    }

    /**
     * Animate a plane moving - landing
     */
    public void animatePlaneLanding(){
        //stop timeline if already moving
        if (timeline != null && timer != null) {
            timer.stop();
            timeline.stop();
            build();
        }

        Image image = null;
        DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();
        double startX;
        double startY;
        double endX;
        double endY;
        //if obstacle below line then go behind
        boolean behindObs = (runway.getObstacle()!=null) && (!runway.isObstacleAboveLine());

        //if no recalculation done, just go along runway
        if (runway.isLandingTowards() == 0){
            double duration = ((runwayStartX + runwayWidth - thresholdStartX)/runwayWidth)*5;

            if (LEFT_RUNWAY){
                startX = thresholdStartX;
                startY = runwayStartY - 25;
                endX = runwayStartX + runwayWidth - 40;
                endY = runwayStartY - 25;
                image = new Image(getClass().getResource("/images/sl.png").toExternalForm());
            }
            else {
                startX = width - thresholdStartX - 40;
                startY = runwayStartY - 25;
                endX = width - runwayStartX - runwayWidth;
                endY = runwayStartY - 25;
                image = new Image(getClass().getResource("/images/sr.png").toExternalForm());
            }

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(x, startX),
                            new KeyValue(y, startY)
                    ),
                    new KeyFrame(Duration.seconds(duration),
                            new KeyValue(x, endX),
                            new KeyValue(y, endY)
                    ),
                    new KeyFrame(Duration.seconds(duration), e -> {
                        timer.stop();
                        timeline.stop();
                        build();
                    })
            );
        }
        //is landing towards an object
        else if (runway.isLandingTowards() == 1) {
            double duration = ((thresholdStartX + normaliseToRunwayX(runway.getRedeclaredLda()) - 40) - thresholdStartX) / runwayWidth * 5;

            if (LEFT_RUNWAY) {
                startX = thresholdStartX;
                startY = runwayStartY + (runwayHeight / 2) - 25;
                endX = thresholdStartX + normaliseToRunwayX(runway.getRedeclaredLda()) - 40;
                endY = runwayStartY + (runwayHeight / 2) - 25;
                image = new Image(getClass().getResource("/images/sl.png").toExternalForm());
            } else {
                startX = width - thresholdStartX - 40;
                startY = runwayStartY + (runwayHeight / 2) - 25;
                endX = width - thresholdStartX - normaliseToRunwayX(runway.getRedeclaredLda());
                endY = runwayStartY + (runwayHeight / 2) - 25;
                image = new Image(getClass().getResource("/images/sr.png").toExternalForm());
            }

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(x, startX),
                            new KeyValue(y, startY)
                    ),
                    new KeyFrame(Duration.seconds(duration),
                            new KeyValue(x, endX),
                            new KeyValue(y, endY)
                    ),
                    new KeyFrame(Duration.seconds(duration), e -> {
                        timer.stop();
                        timeline.stop();
                        build();
                    })
            );
        }
        //is landing over obstacle
        //is taking off towards obstacle
        else if (runway.isLandingTowards() == -1){
            double slopeStart = 0;
            if (alsValue != null) {
                slopeStart = alsValue;
            }
            else if (resaValue != null) {
                slopeStart = resaValue;
            }

            double tempObsX = runwayStartX + normaliseToRunwayX(runway.getObstacle().getDistanceFromThreshold()) + normaliseToRunwayX(runway.getDisplacedThreshold());

            double midX;
            double midY;
            double duration1=((tempObsX+slopeStart)-tempObsX)/runwayWidth*5;
            double duration2=((runwayStartX + runwayWidth)-tempObsX)/runwayWidth*5;

            if (LEFT_RUNWAY) {
                startX = obstacleX;
                startY = obstacleY - obstacleHeight - 25;
                midX = obstacleX + slopeStart;
                midY = runwayStartY + (runwayHeight / 2) - 25;
                endX = runwayStartX + runwayWidth - 40;
                endY = runwayStartY + (runwayHeight / 2) - 25;
                image = new Image(getClass().getResource("/images/sl.png").toExternalForm());
            } else {
                startX = obstacleX;
                startY = obstacleY - obstacleHeight - 25;
                midX = obstacleX - slopeStart - 40;
                midY = runwayStartY + (runwayHeight / 2) - 25;
                endX = width - runwayStartX - runwayWidth;
                endY = runwayStartY + (runwayHeight / 2) - 25;
                image = new Image(getClass().getResource("/images/sr.png").toExternalForm());
            }

            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(x, startX),
                            new KeyValue(y, startY)
                    ),
                    new KeyFrame(Duration.seconds(duration1),
                            new KeyValue(x, midX),
                            new KeyValue(y, midY)
                    ),
                    new KeyFrame(Duration.seconds(duration2),
                            new KeyValue(x, endX),
                            new KeyValue(y, endY)
                    ),
                    new KeyFrame(Duration.seconds(duration2), e -> {
                        timer.stop();
                        timeline.stop();
                        build();
                    })
            );
        }


        Image finalImage = image;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                build();
                gc.drawImage(finalImage, x.doubleValue(), y.doubleValue(), 40, 40);
                if (behindObs){
                    drawObstacle();
                }
            }
        };

        timer.start();
        timeline.play();
    }
    @Override
    protected void drawClearedAndGradedArea() {
        // Don't draw on the side on canvas.
    }

    @Override
    protected void drawInstrumentStrip() {
        // Don't draw the instrument strip on the side on canvas.
    }

    @Override
    protected void drawVisualStrip() {
        // Don't draw the instrument strip on the side on canvas.
    }
}
