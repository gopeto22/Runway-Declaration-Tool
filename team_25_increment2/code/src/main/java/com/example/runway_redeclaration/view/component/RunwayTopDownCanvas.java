package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.model.Runway;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

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
     * Colours for the different runway values.
     */
    protected final Color runwayColor = Color.LIGHTGREY;
    protected final Color displacedThresholdColor = Color.YELLOWGREEN;
    protected final Color obstacleColor = Color.KHAKI;
    protected final Color stopwayColor = Color.PINK;
    protected final Color clearwayColor = Color.RED;
    protected final Color originalToraColor = Color.BLUE;
    protected final Color obstacleDistanceThresholdColor = Color.BROWN;
    protected final Color blastDistanceColor = Color.ORANGE;
    protected final Color alsColor = Color.DARKKHAKI;
    protected final Color resaColor = Color.PURPLE;
    protected final Color stripEndColor = Color.DARKRED;
    protected final Color slopeColor = Color.PALETURQUOISE;
    protected final Color redeclaredToraColor = Color.FUCHSIA;
    protected final Color defaultColor = Color.BLACK;

    /**
     * Constructor to create
     *
     * @param width
     * @param height
     */
    public RunwayTopDownCanvas(double width, double height, Runway runway) {
        super(width, height);

        this.width = width;
        this.height = height;
        this.runway = runway;

        gc = this.getGraphicsContext2D();
        runwayAngle = Integer.parseInt(runway.getDesignator().substring(0, 2)) * 10;
        LEFT_RUNWAY = runwayAngle < 180;

        // Prepare the runway.
        runway.recalculate();

        // Build the Canvas.
        build();
    }

    /**
     * Build the canvas by adding graphics.
     */
    private void build() {
        /*
        Initialisation
         */
        System.out.println("\n\n\nStarting new build:\n");

        // Values
        runwayStartX = width * 0.05;
        runwayStartY = height * 0.425;
        runwayWidth = width * 0.9;
        runwayHeight = height * 0.15;
        double thresholdStartX = runwayStartX + normaliseToRunway(runway.getDisplacedThreshold());
        double stopwayStartX = runwayStartX + runwayWidth + normaliseToRunway(runway.getStopway());
        double clearwayStartX = runwayStartX + runwayWidth + normaliseToRunway(runway.getClearway());
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
        System.out.println("Adding original runway values.");
        RunwayMeasurement runwayMeasurementOver = new RunwayMeasurement(gc, runwayStartX, runwayStartX + runwayWidth, LEFT_RUNWAY, width, runway.isLandingTowards() ,runway, runwayWidth);
        RunwayMeasurement runwayMeasurementUnder = new RunwayMeasurement(gc, runwayStartX, runwayStartX + runwayWidth, LEFT_RUNWAY, width, runway.isLandingTowards(), runway, runwayWidth);
        runwayMeasurementOver.setArrowSize(oRASize);
        runwayMeasurementUnder.setArrowSize(oRAUnderSize);

        /*
        Original LDA Over
         */
        System.out.println("\n Original LDA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(originalLDAHeight);

        // Displaced Threshold
        runwayMeasurementOver.setColor(displacedThresholdColor);
        runwayMeasurementOver.appendValue(runway.getDisplacedThreshold());

        // Original LDA
        runwayMeasurementOver.setColor(defaultColor);
        runwayMeasurementOver.appendValue("Original LDA", runway.originalLdaProperty().get());

        /*
        Original LDA Under
         */
        System.out.println("\n Original LDA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(originalLDAUnderHeight);

        runwayMeasurementUnder.setColor(originalToraColor);
        runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());

        /*
        Original TORA Over
         */
        System.out.println("\n Original TORA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(2);
        runwayMeasurementOver.setHeight(originalToraHeight);

        runwayMeasurementOver.setColor(originalToraColor);
        runwayMeasurementOver.appendValue("Original TORA", runway.originalToraProperty().get());

        /*
        Original TODA Over
         */
        System.out.println("\n Original TODA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(originalTodaHeight);

        runwayMeasurementOver.setColor(defaultColor);
        runwayMeasurementOver.appendValue("Original TODA", runway.originalTodaProperty().get());

        /*
        Original TODA Under
         */
        System.out.println("\n Original TODA Under");
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
        System.out.println("\n Original ASDA Over");
        runwayMeasurementOver.resetOffset();
        runwayMeasurementOver.setLineWidth(1);
        runwayMeasurementOver.setHeight(originalAsdaHeight);

        runwayMeasurementOver.setColor(defaultColor);
        runwayMeasurementOver.appendValue("Original ASDA", runway.originalAsdaProperty().get());

        /*
        Original ASDA Under
         */
        System.out.println("\n Original ASDA Under");
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
        System.out.println("\n Redeclared LDA Over");
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
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared LDA",runway.redeclaredLdaProperty().get());
        }
        //
        else {
            // 1. Redeclared LDA
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared LDA",runway.redeclaredLdaProperty().get());

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
        System.out.println("\n Redeclared LDA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(redeclaredLDAUnderHeight);

        if (runway.isLandingTowards() <= 0) {
            runwayMeasurementUnder.setColor(originalToraColor);
            runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());
        } else {
            runwayMeasurementUnder.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementUnder.appendValue(ldaObstacleDistanceFromThresholdValue);
        }

        /*
        Redeclared TORA Over
         */
        System.out.println("\n Redeclared TORA Over");
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
            //runwayMeasurementOver.setLineWidth(2);
            //runwayMeasurementOver.setColor(redeclaredToraColor);
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared TORA", runway.redeclaredToraProperty().get());
        } else {
            // 1. Redeclared TORA
            //runwayMeasurementOver.setLineWidth(2);
            //runwayMeasurementOver.setColor(redeclaredToraColor);
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared TORA", runway.redeclaredToraProperty().get());

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
        System.out.println("\n Redeclared TORA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(redeclaredToraUnderHeight);

        if (runway.isLandingTowards() <= 0) {
            runwayMeasurementUnder.setColor(originalToraColor);
            runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());
        } else {
            runwayMeasurementUnder.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(obstacleDistanceThresholdString));

            runwayMeasurementUnder.setColor(displacedThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(displacedString));
        }

        /*
        Redeclared TODA Over
         */
        System.out.println("\n Redeclared TODA Over");
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
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared TODA", runway.redeclaredTodaProperty().get());
        } else {
            // 1. Redeclared TODA
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared TODA", runway.redeclaredTodaProperty().get());

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
        System.out.println("\n Redeclared TODA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(redeclaredTodaUnderHeight);

        if (runway.isLandingTowards() <= 0) {
            runwayMeasurementUnder.setColor(originalToraColor);
            runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());

            runwayMeasurementUnder.setColor(clearwayColor);
            runwayMeasurementUnder.appendValue(runway.getClearway());
        } else {
            runwayMeasurementUnder.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(obstacleDistanceThresholdString));

            runwayMeasurementUnder.setColor(displacedThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(displacedString));
        }

        /*
        Redeclared ASDA Over
         */
        System.out.println("\n Redeclared ASDA Over");
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
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared ASDA", runway.redeclaredAsdaProperty().get());
        } else {
            // 1. Redeclared ASDA
            runwayMeasurementOver.setColor(defaultColor);
            runwayMeasurementOver.appendValue("Redeclared ASDA", runway.redeclaredAsdaProperty().get());

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
        System.out.println("\n Redeclared ASDA Under");
        runwayMeasurementUnder.resetOffset();
        runwayMeasurementUnder.setLineWidth(1);
        runwayMeasurementUnder.setHeight(redeclaredAsdaUnderHeight);

        if (runway.isLandingTowards() <= 0) {
            runwayMeasurementUnder.setColor(originalToraColor);
            runwayMeasurementUnder.appendValue(runway.originalToraProperty().get());

            runwayMeasurementUnder.setColor(stopwayColor);
            runwayMeasurementUnder.appendValue(runway.getStopway());
        } else {
            runwayMeasurementUnder.setColor(obstacleDistanceThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(obstacleDistanceThresholdString));

            runwayMeasurementUnder.setColor(displacedThresholdColor);
            runwayMeasurementUnder.appendValue(redeclaredToraMap.get(displacedString));
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
    }

    /**
     * Draw the corresponding logical runways for this runway. Placing the lower code on the left hand side.
     */
    private void drawRunwayDesignators() {
        double yOffset = runwayStartY + runwayHeight * 0.4;
        double leftPos = runwayStartX + runwayWidth * 0.05;
        double rightPos = runwayStartX + runwayWidth * 0.95;
        gc.setFill(Color.BLACK);
        if (runway.getDesignator().length() > 2) {
            String inverseDesignator = getInverseLogicalRunway();
            if (LEFT_RUNWAY) {
                gc.fillText(formatRunwayDesignator(runway.getDesignator()), leftPos, yOffset);
                gc.fillText(formatRunwayDesignator(inverseDesignator), rightPos, yOffset);
            } else {
                gc.fillText(formatRunwayDesignator(runway.getDesignator()), rightPos, yOffset);
                gc.fillText(formatRunwayDesignator(inverseDesignator), leftPos, yOffset);
            }
        } else {
            if (LEFT_RUNWAY) {
                gc.fillText(runway.getDesignator(), runwayStartX + runwayWidth * 0.05, yOffset);
            } else {
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
        if (runwayAngle < 180) {
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
     * @param runwayVal the original runway value to normlaise
     * @return the normalised value
     */
    protected double normaliseToRunway(Integer runwayVal) {
        if (runwayVal == null) {
            System.out.println("Can't normalise null value.");
            return 0;
        }
        // Original tora is the length of the runway available for takeoff, so use this to normalise.
        return ((double) runwayVal / (double) runway.originalToraProperty().get()) * runwayWidth;
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
        gc.setFill(defaultColor);
        gc.fillRect(runwayStartX - 1, runwayStartY - 1, runwayWidth + 2, runwayHeight + 2);

        // Runway.
        gc.setFill(runwayColor);
        gc.fillRect(runwayStartX, runwayStartY, runwayWidth, runwayHeight);

        // Centreline
        gc.setFill(defaultColor);
        gc.fillRect(runwayStartX, runwayStartY + runwayHeight / 2 - 1, runwayWidth, 2);
    }

    /**
     * Draw obstacle.
     */
    protected void drawObstacle() {
        if (runway.getObstacle() == null) { return; }

        // Obstacle.
        double obstacleSize = width * 0.03;
        double obstacleX;

        if (LEFT_RUNWAY) {
            obstacleX = runwayStartX + normaliseToRunway(runway.getObstacle().getDistanceFromThreshold());
        } else {
            obstacleX = runwayStartX + runwayWidth - normaliseToRunway(runway.getObstacle().getDistanceFromThreshold());
        }
        double obstacleY = normaliseToRunway(runway.getDistanceFromCentreline());

        gc.setStroke(Color.BLACK);
        gc.setFill(obstacleColor);

        if (runway.isObstacleAboveLine()) {
            System.out.println("Adding obstacle above the line.");
            gc.fillRect(obstacleX - obstacleSize / 2, runwayStartY + runwayHeight / 2 - obstacleY - obstacleSize / 2, obstacleSize, obstacleSize);
        } else {
            System.out.println("Adding obstacle below the line.");
            gc.fillRect(obstacleX - obstacleSize / 2, runwayStartY + runwayHeight / 2 + obstacleY - obstacleSize / 2, obstacleSize, obstacleSize);
        }
    }
}
