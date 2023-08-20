package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.UIColor;
import com.example.runway_redeclaration.model.Runway;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

/**
 * This class is used to display the top down visualisation of the runway.
 */
public class RunwayTopDownCanvas extends Canvas {

    /**
     * The graphics context variable is used to draw on the canvas.
     */
    protected GraphicsContext gc;

    protected Runway runway;

    /**
     * The width of the canvas.
     */
    protected double width;

    /**
     * The height of the canvas.
     */
    protected double height;

    /**
     * The starting position of the runway on the X axis.
     */
    protected double runwayStartX;

    /**
     * The starting position of the runway on the Y axis.
     */
    protected double runwayStartY;

    /**
     * The width of the runway.
     */
    protected double runwayWidth;

    /**
     * The height of the runway.
     */
    protected double runwayHeight;

    /**
     * The angle of the runway.
     */
    protected int runwayAngle;

    /**
     * Boolean value for of the runway is left to right or not.
     */
    protected boolean LEFT_RUNWAY;

    /**
     * UIColor class to set all the colors for the runway components.
     */
    private final UIColor uiColor;

    /**
     * Colours for the different runway values.
     */
    protected Color runwayColor;
    protected Color displacedThresholdColor;
    protected Color obstacleColor;
    protected Color stopwayColor;
    protected Color clearwayColor;
    protected Color originalToraColor;
    protected Color obstacleDistanceThresholdColor;
    protected Color blastDistanceColor;
    protected Color alsColor;
    protected Color resaColor;
    protected Color stripEndColor;
    protected Color slopeColor;
    protected Color defaultColor;
    protected Color clearedAndGradedColor;
    protected Color instrumentStripColor;

    /**
     * displaced threshold
     */
    double thresholdStartX;

    /**
     * animation timer stuff
     */
    Timeline timeline;
    AnimationTimer timer;

    /**
     * Constructor to create
     *
     * @param width
     * @param height
     */
    public RunwayTopDownCanvas(double width, double height, Runway runway, UIColor uiColor) {
        super(width, height);

        this.width = width;
        this.height = height;
        this.runway = runway;

        gc = this.getGraphicsContext2D();
        runwayAngle = Integer.parseInt(runway.getDesignator().substring(0, 2)) * 10;
        LEFT_RUNWAY = runwayAngle < 180;

        this.uiColor = uiColor;

        // Get colours.
        runwayColor = uiColor.getRunwayColor();
        displacedThresholdColor = uiColor.getDisplacedThresholdColor();
        obstacleColor = uiColor.getObstacleColor();
        stopwayColor = uiColor.getStopwayColor();
        clearwayColor = uiColor.getClearwayColor();
        originalToraColor = uiColor.getOriginalToraColor();
        obstacleDistanceThresholdColor = uiColor.getObstacleDistanceThresholdColor();
        blastDistanceColor = uiColor.getBlastDistanceColor();
        alsColor = uiColor.getAlsColor();
        resaColor = uiColor.getResaColor();
        stripEndColor = uiColor.getStripEndColor();
        slopeColor = uiColor.getSlopeColor();
        defaultColor = uiColor.getDefaultColor();
        clearedAndGradedColor = uiColor.getClearedAndGradedColor();
        instrumentStripColor = uiColor.getInstrumentStripColor();

        // Prepare the runway.
        runway.recalculate();

        // Build the Canvas.
        build();
    }

    /**
     * Build the canvas by adding graphics.
     */
    public void build() {
        /*
        Initialisation
         */
        //System.out.println("\n\n\nStarting new build:\n");

        //fill canvas with background colour
        gc.setFill(Color.web("4d4c4c"));
        gc.fillRect(0,0,width, height);

        // Values
        runwayStartX = width * 0.05;
        runwayStartY = height * 0.425;
        runwayWidth = width * 0.9;
        runwayHeight = height * 0.15;
        thresholdStartX = runwayStartX + normaliseToRunwayX(runway.getDisplacedThreshold());
        double stopwayStartX = runwayStartX + runwayWidth + normaliseToRunwayX(runway.getStopway());
        double clearwayStartX = runwayStartX + runwayWidth + normaliseToRunwayX(runway.getClearway());
        int oRASize = 6; // originalRecalculatedArrowSize, arrow size for the double arrows representing original and recalculated values.
        int oRAUnderSize = 3; // originalRecalculatedDisplacement, how far should the original and recalculated values be apart relative to runway height.
        double oRDisp = 0.4 * runwayHeight;
        double oRUnderDisp = 0.1 * runwayHeight;
        double originalLDAHeight = runwayStartY - oRDisp;
        double originalTodaHeight = runwayStartY - 2 * oRDisp;
        double originalAsdaHeight = runwayStartY - 3 * oRDisp;
        double originalToraHeight = runwayStartY - 4 * oRDisp;
        double redeclaredLDAHeight = runwayStartY + runwayHeight + oRDisp;
        double redeclaredTodaHeight = runwayStartY + runwayHeight + 2 * oRDisp;
        double redeclaredAsdaHeight = runwayStartY + runwayHeight + 3 * oRDisp;
        double redeclaredToraHeight = runwayStartY + runwayHeight + 4 * oRDisp;
        double originalLDAUnderHeight = originalLDAHeight + oRUnderDisp;
        double originalTodaUnderHeight = originalTodaHeight + oRUnderDisp;
        double originalAsdaUnderHeight = originalAsdaHeight + oRUnderDisp;
        double originalToraUnderHeight = originalToraHeight + oRUnderDisp;
        double redeclaredLDAUnderHeight = redeclaredLDAHeight + oRUnderDisp;
        double redeclaredTodaUnderHeight = redeclaredTodaHeight + oRUnderDisp;
        double redeclaredAsdaUnderHeight = redeclaredAsdaHeight + oRUnderDisp;
        double redeclaredToraUnderHeight = redeclaredToraHeight + oRUnderDisp;

        /*
        Draw instrument strip
         */
        drawInstrumentStrip();

        /*
        Draw cleared and graded area.
         */
        drawClearedAndGradedArea();

        /*
        Draw visual strip.
         */
        drawVisualStrip();

        /*
        Draw runway
         */
        drawRunway();

        /*
        Draw Obstacle
         */
        drawObstacle();

        // Runway Threshold.
        gc.setStroke(displacedThresholdColor);
        gc.setLineWidth(2);

        if (LEFT_RUNWAY) {
            drawRunwayLine(thresholdStartX);
        } else {
            drawRunwayLine(width - thresholdStartX);
        }

        // Stopway.
        gc.setStroke(stopwayColor);
        gc.setLineWidth(2);

        if (LEFT_RUNWAY) {
            drawRunwayLine(stopwayStartX);
        } else {
            drawRunwayLine(width - stopwayStartX);
        }

        // Clearway.
        gc.setStroke(clearwayColor);
        gc.setLineWidth(2);

        if (LEFT_RUNWAY) {
            drawRunwayLine(clearwayStartX);
        } else {
            drawRunwayLine(width - clearwayStartX);
        }

        // Runway designator.
        drawRunwayDesignators();

        /*
        ----------------------
        Original runway values
        ----------------------
         */
        //System.out.println("Adding original runway values.");
        RunwayMeasurement runwayMeasurementOver = new RunwayMeasurement(gc, runwayStartX, runwayStartX + runwayWidth, LEFT_RUNWAY, width, runway.isLandingTowards() ,runway, runwayWidth);
        RunwayMeasurement runwayMeasurementUnder = new RunwayMeasurement(gc, runwayStartX, runwayStartX + runwayWidth, LEFT_RUNWAY, width, runway.isLandingTowards(), runway, runwayWidth);
        runwayMeasurementOver.setArrowSize(oRASize);
        runwayMeasurementUnder.setArrowSize(oRAUnderSize);

        /*
        Original LDA Over
         */
        //System.out.println("\n Original LDA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(originalLDAHeight);

        // Displaced Threshold
        runwayMeasurementOver.setColor(displacedThresholdColor);
        runwayMeasurementOver.appendValue(runway.getDisplacedThreshold());

        // Original LDA
        var originalLda = runway.originalLdaProperty().get();
        runwayMeasurementOver.setColor(defaultColor);
        runwayMeasurementOver.appendValue("Original LDA (" + originalLda + ")", originalLda);

        /*
        Original LDA Under
         */
        //System.out.println("\n Original LDA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(originalLDAUnderHeight);

        runwayMeasurementUnder.setColor(originalToraColor);
        runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());

        /*
        Original TORA Over
         */
        //System.out.println("\n Original TORA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(2);
        runwayMeasurementOver.setHeight(originalToraHeight);

        var originalTora = runway.originalToraProperty().get();
        runwayMeasurementOver.setColor(originalToraColor);
        runwayMeasurementOver.appendValue("Original TORA (" + originalTora + ")", originalTora);

        /*
        Original TODA Over
         */
        //System.out.println("\n Original TODA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(originalTodaHeight);

        var originalToda = runway.originalTodaProperty().get();
        runwayMeasurementOver.setColor(defaultColor);
        runwayMeasurementOver.appendValue("Original TODA (" + originalToda + ")", originalToda);

        /*
        Original TODA Under
         */
        //System.out.println("\n Original TODA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(originalTodaUnderHeight);

        runwayMeasurementUnder.setColor(originalToraColor);
        runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());

        runwayMeasurementUnder.setColor(clearwayColor);
        runwayMeasurementUnder.appendValue(runway.getClearway());

        /*
        Original ASDA Over
         */
        //System.out.println("\n Original ASDA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(originalAsdaHeight);

        var originalAsda = runway.originalAsdaProperty().get();
        runwayMeasurementOver.setColor(defaultColor);
        runwayMeasurementOver.appendValue("Original ASDA (" + originalAsda + ")", originalAsda);

        /*
        Original ASDA Under
         */
        //System.out.println("\n Original ASDA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(originalAsdaUnderHeight);

        runwayMeasurementUnder.setColor(originalToraColor);
        runwayMeasurementUnder.drawValueFromLeft(runway.originalToraProperty().get());

        runwayMeasurementUnder.setColor(stopwayColor);
        runwayMeasurementUnder.drawValueFromLeft(runway.getStopway());

        /*
        ------------------------
        Redeclared runway values
        ------------------------
         */
        var redeclaredLdaMap = runway.getRedeclaredLdaMap();
        var redeclaredToraMap = runway.getRedeclaredToraMap();
        var redeclaredTodaMap = runway.getRedeclaredTodaMap();
        var redeclaredAsdaMap = runway.getRedeclaredAsdaMap();
        String displacedString = "Displaced Threshold";
        String obstacleDistanceThresholdString = "Distance From Threshold";
        String blastString = "Blast Protection";
        String alsString = "ALS";
        String resaString = "RESA";
        String stripEndString = "Strip End";
        String slopeString = "Slope Calculation";

        /*
        Redeclared LDA Over
         */
        //System.out.println("\n Redeclared LDA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(redeclaredLDAHeight);

        // 1. Displaced Threshold
        // 2. Distance From Threshold
        // 3. Blast Protection
        // 4. ALS
        // 5. RESA
        // 6. Strip End
        // 7. Redeclared LDA
        var ldaStripEndValue = redeclaredLdaMap.get(stripEndString);
        var ldaResaValue = redeclaredLdaMap.get(resaString);
        var ldaAlsValue = redeclaredLdaMap.get(alsString);
        var ldaBlastDistanceValue = redeclaredLdaMap.get(blastString);
        var ldaDisplacedValue = redeclaredLdaMap.get(displacedString);
        var ldaObstacleDistanceFromThresholdValue = redeclaredLdaMap.get(obstacleDistanceThresholdString);

        // Normal order.
        if (runway.isLandingTowards() <= 0) {
            // 1. Displaced Threshold
            runwayMeasurementOver.setColor(displacedThresholdColor);
            runwayMeasurementOver.appendValue(ldaDisplacedValue);

            // 2. Obstacle Distance From Threshold
            runwayMeasurementOver.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementOver.appendValue(ldaObstacleDistanceFromThresholdValue);

            // 3. Blast Protection
            runwayMeasurementOver.setColor(blastDistanceColor);
            runwayMeasurementOver.appendValue(ldaBlastDistanceValue);

            // 4. ALS (Slope Calculation)
            runwayMeasurementOver.setColor(alsColor);
            runwayMeasurementOver.appendValue(ldaAlsValue);

            // 5. RESA
            runwayMeasurementOver.setColor(resaColor);
            runwayMeasurementOver.appendValue(ldaResaValue);

            // 6. Strip End
            runwayMeasurementOver.setColor(stripEndColor);
            runwayMeasurementOver.appendValue(ldaStripEndValue);

            // 7. Redeclared LDA
            var redeclaredLda = runway.redeclaredLdaProperty().get();
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared LDA (" + redeclaredLda + ")",redeclaredLda);
        }
        //
        else {
            // 0. :D Displaced Threshold
            runwayMeasurementOver.setColor(displacedThresholdColor);
            runwayMeasurementOver.appendValue(ldaDisplacedValue);

            // 1. Redeclared LDA
            var redeclaredLda = runway.redeclaredLdaProperty().get();
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared LDA (" + redeclaredLda + ")",redeclaredLda);

            // 2. Strip End
            runwayMeasurementOver.setColor(stripEndColor);
            runwayMeasurementOver.appendValue(ldaStripEndValue);

            // 3. RESA
            runwayMeasurementOver.setColor(resaColor);
            runwayMeasurementOver.appendValue(ldaResaValue);
        }

        /*
        Redeclared LDA Under
         */
        //System.out.println("\n Redeclared LDA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(redeclaredLDAUnderHeight);

        if (runway.isLandingTowards() <= 0) {
            runwayMeasurementUnder.setColor(originalToraColor);
            runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());
        } else {
            // 0. :D Displaced Threshold
            runwayMeasurementUnder.setColor(displacedThresholdColor);
            runwayMeasurementUnder.appendValue(ldaDisplacedValue);

            runwayMeasurementUnder.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementUnder.appendValue(ldaObstacleDistanceFromThresholdValue);
        }

        /*
        Redeclared TORA Over
         */
        //System.out.println("\n Redeclared TORA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(redeclaredToraHeight);

        if (runway.isLandingTowards() <= 0) {
            // 1. Displaced Threshold
            runwayMeasurementOver.setColor(displacedThresholdColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(displacedString));

            // 2. Obstacle Distance From Threshold
            runwayMeasurementOver.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(obstacleDistanceThresholdString));

            // 3. Blast Distance
            runwayMeasurementOver.setColor(blastDistanceColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(blastString));

            // 4. Redeclared TORA
            var redeclaredTora = runway.redeclaredToraProperty().get();
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared TORA (" + redeclaredTora + ")", redeclaredTora);
        } else {
            // 1. Redeclared TORA
            var redeclaredTora = runway.redeclaredToraProperty().get();
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared TORA (" + redeclaredTora + ")", redeclaredTora);

            // 2. Strip End
            runwayMeasurementOver.setLineWidth(1);
            runwayMeasurementOver.setColor(stripEndColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(stripEndString));

            // 3. RESA
            runwayMeasurementOver.setColor(resaColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(resaString));

            // 4. Slope Calculation
            runwayMeasurementOver.setColor(slopeColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(slopeString));
        }

        /*
        Redeclared TORA Under
         */
        //System.out.println("\n Redeclared TORA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(redeclaredToraUnderHeight);

        if (runway.isLandingTowards() <= 0) {
            runwayMeasurementUnder.setColor(originalToraColor);
            runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());
        } else {
            runwayMeasurementUnder.setColor(displacedThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(displacedString));

            runwayMeasurementUnder.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(obstacleDistanceThresholdString));
        }

        /*
        Redeclared TODA Over
         */
        //System.out.println("\n Redeclared TODA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(redeclaredTodaHeight);

        if (runway.isLandingTowards() <= 0) {
            // 1. Displaced Threshold
            runwayMeasurementOver.setColor(displacedThresholdColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(displacedString));

            // 2. Obstacle Distance From Threshold
            runwayMeasurementOver.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(obstacleDistanceThresholdString));

            // 3. Blast Distance
            runwayMeasurementOver.setColor(blastDistanceColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(blastString));

            // 4. Redeclared TODA
            var redeclaredToda = runway.redeclaredTodaProperty().get();
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared TODA (" + redeclaredToda + ")", redeclaredToda);
        } else {
            // 1. Redeclared TODA
            var redeclaredToda = runway.redeclaredTodaProperty().get();
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared TODA (" + redeclaredToda + ")", redeclaredToda);

            // 2. Strip End
            runwayMeasurementOver.setColor(stripEndColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(stripEndString));

            // 3. RESA
            runwayMeasurementOver.setColor(resaColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(resaString));

            // 4. Slope Calculation
            runwayMeasurementOver.setColor(slopeColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(slopeString));
        }

        /*
        Redeclared TODA Under
         */
        //System.out.println("\n Redeclared TODA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(redeclaredTodaUnderHeight);

        if (runway.isLandingTowards() <= 0) {
            runwayMeasurementUnder.setColor(originalToraColor);
            runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());

            runwayMeasurementUnder.setColor(clearwayColor);
            runwayMeasurementUnder.appendValue(runway.getClearway());
        } else {
            runwayMeasurementUnder.setColor(displacedThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(displacedString));

            runwayMeasurementUnder.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(obstacleDistanceThresholdString));
        }

        /*
        Redeclared ASDA Over
         */
        //System.out.println("\n Redeclared ASDA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(redeclaredAsdaHeight);

        if (runway.isLandingTowards() <= 0) {
            // 1. Displaced Threshold
            runwayMeasurementOver.setColor(displacedThresholdColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(displacedString));

            // 2. Obstacle Distance From Threshold
            runwayMeasurementOver.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(obstacleDistanceThresholdString));

            // 3. Blast Distance
            runwayMeasurementOver.setColor(blastDistanceColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(blastString));

            // 4. Redeclared ASDA
            var redeclaredAsda = runway.redeclaredAsdaProperty().get();
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared ASDA (" + redeclaredAsda + ")", redeclaredAsda);
        } else {
            // 1. Redeclared ASDA
            var redeclaredAsda = runway.redeclaredAsdaProperty().get();
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared ASDA (" + redeclaredAsda + ")", redeclaredAsda);

            // 2. Strip End
            runwayMeasurementOver.setColor(stripEndColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(stripEndString));

            // 3. RESA
            runwayMeasurementOver.setColor(resaColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(resaString));

            // 4. Slope Calculation
            runwayMeasurementOver.setColor(slopeColor);
            runwayMeasurementOver.appendValue(redeclaredToraMap.get(slopeString));
        }

        /*
        Redeclared ASDA Under
         */
        //System.out.println("\n Redeclared ASDA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(redeclaredAsdaUnderHeight);

        if (runway.isLandingTowards() <= 0) {
            runwayMeasurementUnder.setColor(originalToraColor);
            runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());

            runwayMeasurementUnder.setColor(stopwayColor);
            runwayMeasurementUnder.appendValue(runway.getStopway());
        } else {
            runwayMeasurementUnder.setColor(displacedThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(displacedString));

            runwayMeasurementUnder.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(obstacleDistanceThresholdString));
        }

        // Runway direction arrow.
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        gc.setLineWidth(2.5);

        if (LEFT_RUNWAY) {
            // The runway is left to right.
            drawArrow(12, width * 0.4, height * 0.1, width * 0.6, height * 0.1);
        } else {
            // The runway is right to left.
            drawArrow(12, width * 0.6, height * 0.1, width * 0.4, height * 0.1);
        }

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.web("#cccaca"));
        gc.strokeText("Runway Direction", width * 0.4925, height * 0.085);
        gc.fillText("Runway Direction",width * 0.4925, height * 0.085);
    }

    /**
     * Draw the runway instrument strip (border past the cleared and graded area)
     */
    protected void drawInstrumentStrip() {
        gc.setFill(instrumentStripColor);
        double startX = runwayStartX - normaliseToRunwayX(runway.getStripEnd());
        double startY = runwayStartY + runwayHeight / 2 - normaliseToRunwayY(150);
        double instrumentWidth = runwayWidth + 2 * normaliseToRunwayX(runway.getStripEnd());
        double instrumentHeight = 2 * normaliseToRunwayY(150);
        gc.fillRect(startX, startY, instrumentWidth, instrumentHeight);
    }

    /**
     * Draw the runway visual strip.
     */
    protected void drawVisualStrip() {
        gc.setFill(clearedAndGradedColor);
        double startX = runwayStartX - normaliseToRunwayX(runway.getStripEnd());
        double startY = runwayStartY + runwayHeight / 2 - normaliseToRunwayY(75);
        double visualWidth = runwayWidth + 2 * normaliseToRunwayX(runway.getStripEnd());
        double visualHeight = 2 * normaliseToRunwayY(75);
        gc.fillRect(startX, startY, visualWidth, visualHeight);
    }

    /**
     * Draw the runway cleared and graded area.
     */
    protected void drawClearedAndGradedArea() {
        gc.setFill(clearedAndGradedColor);
        double x1 = runwayStartX + normaliseToRunwayX(150);
        double y1 = runwayStartY + runwayHeight/2 - normaliseToRunwayY(75);
        double x2 = runwayStartX + normaliseToRunwayX(300);
        double y2 = runwayStartY + runwayHeight/2 - normaliseToRunwayY(105);
        double x3 = runwayStartX + runwayWidth - normaliseToRunwayX(300);
        double y3 = y2;
        double x4 = runwayStartX + runwayWidth - normaliseToRunwayX(150);
        double y4 = y1;
        gc.fillPolygon(new double[]{x1,x2,x3,x4}, new double[]{y1,y2,y3,y4}, 4);

        y1 = runwayStartY + runwayHeight/2 + normaliseToRunwayY(75);
        y2 = runwayStartY + runwayHeight/2 + normaliseToRunwayY(105);
        y3 = y2;
        y4 = y1;
        gc.fillPolygon(new double[]{x1,x2,x3,x4}, new double[]{y1,y2,y3,y4},4);
    }

    /**
     * Draw the corresponding logical runways for this runway. Placing the lower code on the left hand side.
     */
    private void drawRunwayDesignators() {
        double yOffset = runwayStartY + runwayHeight * 0.4;
        double leftPos = runwayStartX + runwayWidth * 0.05;
        double rightPos = runwayStartX + runwayWidth * 0.95;
        gc.setFill(Color.web("#cccaca"));
        gc.setStroke(Color.BLACK);
        gc.setFont(new Font("Orbitron",16));
        gc.setLineWidth(1.5);
        if (runway.getDesignator().length() > 2) {
            String inverseDesignator = getInverseLogicalRunway();
            if (LEFT_RUNWAY) {
                gc.strokeText(formatRunwayDesignator(runway.getDesignator()), leftPos, yOffset);
                gc.fillText(formatRunwayDesignator(runway.getDesignator()), leftPos, yOffset);

                gc.strokeText(formatRunwayDesignator(inverseDesignator), rightPos, yOffset);
                gc.fillText(formatRunwayDesignator(inverseDesignator), rightPos, yOffset);
            } else {
                gc.strokeText(formatRunwayDesignator(runway.getDesignator()), rightPos, yOffset);
                gc.fillText(formatRunwayDesignator(runway.getDesignator()), rightPos, yOffset);

                gc.strokeText(formatRunwayDesignator(inverseDesignator), leftPos, yOffset);
                gc.fillText(formatRunwayDesignator(inverseDesignator), leftPos, yOffset);
            }
        } else {
            if (LEFT_RUNWAY) {
                gc.strokeText(runway.getDesignator(), runwayStartX + runwayWidth * 0.05, yOffset);
                gc.fillText(runway.getDesignator(), runwayStartX + runwayWidth * 0.05, yOffset);
            } else {
                gc.strokeText(runway.getDesignator(), runwayStartX + runwayWidth * 0.95, yOffset);
                gc.fillText(runway.getDesignator(), runwayStartX + runwayWidth * 0.95, yOffset);
            }
        }
    }

    /**
     * Get the inverse logical runway to this one.
     */
    private String getInverseLogicalRunway() {
        char logicalDesignator = runway.getDesignator().charAt(2);
        String inverseDesignator = switch (logicalDesignator) {
            case 'L' -> "R";
            case 'C' -> "C";
            case 'R' -> "L";
            default -> "";
        };

        // Make sure that the format is correct.
        if (runwayAngle < 270) {
            return ((360 - runwayAngle) / 10) + inverseDesignator;
        } else {
            return "0" + ((360 - runwayAngle) / 10) + inverseDesignator;
        }
    }

    /**
     * Format the designator so that the number appears underneath the runway code.
     *
     * @param designator to be formatted
     * @return formatted designator
     */
    private String formatRunwayDesignator(String designator) {
        if (designator.length() > 2) {
            return designator.substring(0, 2) + "\n " + designator.charAt(2);
        }
        return designator;
    }

    /**
     * Normalise a runway value relative to the original tora
     *
     * @param runwayVal the original runway value to normalise
     * @return the normalised value
     */
    protected double normaliseToRunwayX(Integer runwayVal) {
        if (runwayVal == null) {
            //System.out.println("Can't normalise null value.");
            return 0;
        }
        // Original tora is the length of the runway available for takeoff, so use this to normalise.
        return ((double) runwayVal / (double) runway.originalToraProperty().get()) * runwayWidth;
    }

    /**
     * Normalise value to the runway height
     *
     * @param runwayVal value to normalise
     * @return the normalised value
     */
    private double normaliseToRunwayY(Integer runwayVal) {
        if (runwayVal == null) {
            //System.out.println("Can't normalise null value.");
            return 0;
        }

        return ((double) runwayVal / (double) runwayHeight * 40);
    }

    /**
     * Draw a vertical line across the runway.
     *
     * @param startX the starting x coordinate for the line to start from
     */
    private void drawRunwayLine(double startX) {
        gc.strokeLine(startX, runwayStartY, startX, runwayStartY + runwayHeight);
    }

    /**
     * Draw an arrow. Code adapted from https://stackoverflow.com/questions/35751576/javafx-draw-line-with-arrow-canvas.
     *
     * @param arrowSize the size of the arrow.
     * @param x1        startX
     * @param y1        startY
     * @param x2        endX
     * @param y2        endY
     */
    private void drawArrow(int arrowSize, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);

        gc.save();
        Transform transform = Transform.translate(x1, y1);
        transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
        gc.setTransform(new Affine(transform));

        //gc.strokeLine(width * 0.005, 0, len - width * 0.005, 0);
        gc.strokeLine(1, 0, len - 1, 0);
        gc.fillPolygon(new double[]{len, len - arrowSize, len - arrowSize, len}, new double[]{0, -arrowSize, arrowSize, 0},
                4);
        gc.restore();
    }

    /**
     * Draw the runway, outline, and centreline.
     */
    protected void drawRunway() {
        // Runway outline.
        gc.setFill(Color.BLACK);
        gc.fillRect(runwayStartX - 1, runwayStartY - 1, runwayWidth + 2, runwayHeight + 2);

        // Runway.
        gc.setFill(runwayColor);
        gc.fillRect(runwayStartX, runwayStartY, runwayWidth, runwayHeight);

        // Centreline
        gc.setFill(Color.BLACK);
        gc.fillRect(runwayStartX, runwayStartY + runwayHeight / 2 - 1, runwayWidth, 2);
    }

    /**
     * Draw obstacle.
     */
    protected void drawObstacle() {
        if (runway.getObstacle() == null) { return; }

        // Obstacle.
        double obstacleWidth = Math.log(runway.getObstacle().getObsWidth()) * 10;
        double obstacleLength = Math.log(runway.getObstacle().getObsLength()) * 10;
        double obstacleX;

        if (LEFT_RUNWAY) {
            obstacleX = runwayStartX + normaliseToRunwayX(runway.getObstacle().getDistanceFromThreshold()) + normaliseToRunwayX(runway.getDisplacedThreshold());
        } else {
            obstacleX = runwayStartX + runwayWidth - normaliseToRunwayX(runway.getObstacle().getDistanceFromThreshold()) - normaliseToRunwayX(runway.getDisplacedThreshold());
        }
        double obstacleY = normaliseToRunwayY(runway.getDistanceFromCentreline());

        gc.setStroke(Color.BLACK);
        gc.setFill(obstacleColor);

        if (runway.isObstacleAboveLine()) {
            //System.out.println("Adding obstacle above the line.");
            gc.fillRect(obstacleX - obstacleLength / 2, runwayStartY + runwayHeight / 2 - obstacleY - obstacleWidth / 2, obstacleLength, obstacleWidth);
        } else {
            //System.out.println("Adding obstacle below the line.");
            gc.fillRect(obstacleX - obstacleLength / 2, runwayStartY + runwayHeight / 2 + obstacleY - obstacleWidth / 2, obstacleLength, obstacleWidth);
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

        //vars
        Image image = null;
        DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();
        double startX;
        double startY;
        double endX;
        double endY;

        //no object or taking off towards object
        if (runway.isLandingTowards() == 0){
            double duration = ((runwayStartX+runwayWidth-40)-runwayStartX)/runwayWidth*5;

            if (LEFT_RUNWAY){
                startX = runwayStartX;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = runwayStartX + runwayWidth - 40;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tl.png").toExternalForm());
            }
            else {
                startX = width - runwayStartX - 40;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = width - runwayStartX - runwayWidth;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tr.png").toExternalForm());
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
        //taking off towards
        else if ( runway.isLandingTowards() == 1){
            double duration = ((runwayStartX + normaliseToRunwayX(runway.getRedeclaredTora()) - 40)-runwayStartX)/runwayWidth*5;

            if (LEFT_RUNWAY){
                startX = runwayStartX;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = runwayStartX + normaliseToRunwayX(runway.getRedeclaredTora()) - 40;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tl.png").toExternalForm());
            }
            else {
                startX = width - runwayStartX - 40;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = width - runwayStartX - normaliseToRunwayX(runway.getRedeclaredTora());
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tr.png").toExternalForm());
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
        //landing away from object (= take off away from)
        else if (runway.isLandingTowards() == -1){
            double duration = ((runwayStartX+runwayWidth-40)-(runwayStartX+runwayWidth-normaliseToRunwayX(runway.getRedeclaredTora())))/runwayWidth*5;

            if (LEFT_RUNWAY){
                startX = runwayStartX + runwayWidth - normaliseToRunwayX(runway.getRedeclaredTora());
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = runwayStartX + runwayWidth - 40;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tl.png").toExternalForm());
            }
            else {
                startX = width - runwayStartX - runwayWidth + normaliseToRunwayX(runway.getRedeclaredTora()) - 40;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = width - runwayStartX - runwayWidth;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tr.png").toExternalForm());
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

        Image finalImage = image;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                build();
                gc.drawImage(finalImage, x.doubleValue(), y.doubleValue(), 40, 40);
            }
        };

        //play animation
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

        //vars
        Image image = null;
        DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();
        double startX;
        double startY;
        double endX;
        double endY;

        //no object
        if (runway.isLandingTowards() == 0){
            double duration = ((runwayStartX+runwayWidth-40)-thresholdStartX)/runwayWidth*5;

            if (LEFT_RUNWAY){
                startX = thresholdStartX;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = runwayStartX + runwayWidth - 40;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tl.png").toExternalForm());
            }
            else {
                startX = width - thresholdStartX - 40;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = width - runwayStartX - runwayWidth;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tr.png").toExternalForm());
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

        //landing away from object (= take off away from)
        else if (runway.isLandingTowards() == -1){
            double duration = ((runwayStartX+runwayWidth-40)-(runwayStartX+runwayWidth-normaliseToRunwayX(runway.getRedeclaredTora())))/runwayWidth*5;

            if (LEFT_RUNWAY){
                startX = runwayStartX + runwayWidth - normaliseToRunwayX(runway.getRedeclaredTora());
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = runwayStartX + runwayWidth - 40;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tl.png").toExternalForm());
            }
            else {
                startX = width - runwayStartX - runwayWidth + normaliseToRunwayX(runway.getRedeclaredTora()) - 40;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = width - runwayStartX - runwayWidth;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tr.png").toExternalForm());
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

        //landing towards object
        else if (runway.isLandingTowards() == 1){
            double duration = ((thresholdStartX+normaliseToRunwayX(runway.getRedeclaredLda())-40)-thresholdStartX)/runwayWidth*5;

            if (LEFT_RUNWAY){
                startX = thresholdStartX;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = thresholdStartX + normaliseToRunwayX(runway.getRedeclaredLda()) - 40;
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tl.png").toExternalForm());
            }
            else {
                startX = width - thresholdStartX - 40;
                startY = runwayStartY + (runwayHeight / 2) - 20;
                endX = width - thresholdStartX - normaliseToRunwayX(runway.getRedeclaredLda());
                endY = runwayStartY + (runwayHeight / 2) - 20;
                image = new Image(getClass().getResource("/images/tr.png").toExternalForm());
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


        Image finalImage = image;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                build();
                gc.drawImage(finalImage, x.doubleValue(), y.doubleValue(), 40, 40);
            }
        };

        //play animation
        timer.start();
        timeline.play();
    }
}
