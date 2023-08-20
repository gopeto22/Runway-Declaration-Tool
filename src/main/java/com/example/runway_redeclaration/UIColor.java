package com.example.runway_redeclaration;

import javafx.scene.paint.Color;

/**
 * Class to represent all the different colors that it might have to display.
 */
public class UIColor {
    /**
     * Runway color, grey.
     */
    //private Color runwayColor = Color.LIGHTGREY;
    private Color runwayColor = Color.web("#a9a9a9");

    /**
     * Displaced threshold color, lime.
     */
    //private Color displacedThresholdColor = Color.YELLOWGREEN;
    private Color displacedThresholdColor = Color.web("#bfef45");

    /**
     * Obstacle color, yellow.
     */
    //private Color obstacleColor = Color.KHAKI;
    private Color obstacleColor = Color.web("#ffe119");

    /**
     * Stopway color, pink.
     */
    //private Color stopwayColor = Color.PINK;
    private Color stopwayColor = Color.web("#fabed4");

    /**
     * Clearway color, red.
     */
    //private Color clearwayColor = Color.RED;
    private Color clearwayColor = Color.web("#e6194b");

    /**
     * Original tora color, cyan.
     */
    //private Color originalToraColor = Color.BLUE;
    private Color originalToraColor = Color.web("#42d4f4");

    /**
     * Obstacle distance from threshold color, maroon.
     */
    //private Color obstacleDistanceThresholdColor = Color.BROWN;
    private Color obstacleDistanceThresholdColor = Color.web("#800000");

    /**
     * Blast distance color, orange.
     */
    //private Color blastDistanceColor = Color.ORANGE;
    private Color blastDistanceColor = Color.web("#f58231");

    /**
     * ALS color, magenta.
     */
    //private Color alsColor = Color.DARKKHAKI;
    private Color alsColor = Color.web("#f032e6");

    /**
     * RESA color, mint.
     */
    //private Color resaColor = Color.PURPLE;
    private Color resaColor = Color.web("#aaffc3");

    /**
     * Strip end color, navy.
     */
    //private Color stripEndColor = Color.DARKRED;
    private Color stripEndColor = Color.web("#000075");

    /**
     * Slope (TOCS) color, teal.
     */
    //private Color slopeColor = Color.PALETURQUOISE;
    private Color slopeColor = Color.web("#469990");

    /**
     * Default color (BLACK).
     */
    private Color defaultColor = Color.web("#cccaca");

    /**
     * Cleared and graded area color.
     */
    private Color clearedAndGradedColor = Color.MEDIUMPURPLE;

    /**
     * Instrument strip color., dark purple
     */
    private Color instrumentStripColor = Color.web("#32174d");

    /**
     * @return the ALS Color
     */
    public Color getAlsColor() {
        return alsColor;
    }

    /**
     * @return the blast distance color
     */
    public Color getBlastDistanceColor() {
        return blastDistanceColor;
    }

    /**
     * @return the clearway color
     */
    public Color getClearwayColor() {
        return clearwayColor;
    }

    /**
     * @return the displaced threshold color
     */
    public Color getDisplacedThresholdColor() {
        return displacedThresholdColor;
    }

    /**
     * @return the default color
     */
    public Color getDefaultColor() {
        return defaultColor;
    }

    /**
     * @return the obstacle color
     */
    public Color getObstacleColor() {
        return obstacleColor;
    }

    /**
     * @return the obstacle distance from threshold color
     */
    public Color getObstacleDistanceThresholdColor() {
        return obstacleDistanceThresholdColor;
    }

    /**
     * @return the original tora color
     */
    public Color getOriginalToraColor() {
        return originalToraColor;
    }

    /**
     * @return the RESA color
     */
    public Color getResaColor() {
        return resaColor;
    }

    /**
     * @return the runway color
     */
    public Color getRunwayColor() {
        return runwayColor;
    }

    /**
     * @return the slope color
     */
    public Color getSlopeColor() {
        return slopeColor;
    }

    /**
     * @return the stopway color
     */
    public Color getStopwayColor() {
        return stopwayColor;
    }

    /**
     * @return the strip end color
     */
    public Color getStripEndColor() {
        return stripEndColor;
    }

    /**
     * @return the cleared and graded area color
     */
    public Color getClearedAndGradedColor() {
        return clearedAndGradedColor;
    }

    /**
     * @return the instrument strip color.
     */
    public Color getInstrumentStripColor() {
        return instrumentStripColor;
    }

    public void setAlsColor(Color alsColor) {
        this.alsColor = alsColor;
    }

    public void setBlastDistanceColor(Color blastDistanceColor) {
        this.blastDistanceColor = blastDistanceColor;
    }

    public void setClearedAndGradedColor(Color clearedAndGradedColor) {
        this.clearedAndGradedColor = clearedAndGradedColor;
    }

    public void setClearwayColor(Color clearwayColor) {
        this.clearwayColor = clearwayColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public void setDisplacedThresholdColor(Color displacedThresholdColor) {
        this.displacedThresholdColor = displacedThresholdColor;
    }

    public void setInstrumentStripColor(Color instrumentStripColor) {
        this.instrumentStripColor = instrumentStripColor;
    }

    public void setObstacleColor(Color obstacleColor) {
        this.obstacleColor = obstacleColor;
    }

    public void setObstacleDistanceThresholdColor(Color obstacleDistanceThresholdColor) {
        this.obstacleDistanceThresholdColor = obstacleDistanceThresholdColor;
    }

    public void setOriginalToraColor(Color originalToraColor) {
        this.originalToraColor = originalToraColor;
    }

    public void setResaColor(Color resaColor) {
        this.resaColor = resaColor;
    }

    public void setRunwayColor(Color runwayColor) {
        this.runwayColor = runwayColor;
    }

    public void setSlopeColor(Color slopeColor) {
        this.slopeColor = slopeColor;
    }

    public void setStopwayColor(Color stopwayColor) {
        this.stopwayColor = stopwayColor;
    }

    public void setStripEndColor(Color stripEndColor) {
        this.stripEndColor = stripEndColor;
    }

    public void resetColors() {
        runwayColor = Color.web("#a9a9a9");
        displacedThresholdColor = Color.web("#bfef45");
        obstacleColor = Color.web("#ffe119");
        stopwayColor = Color.web("#fabed4");
        clearwayColor = Color.web("#e6194b");
        originalToraColor = Color.web("#42d4f4");
        obstacleDistanceThresholdColor = Color.web("#800000");
        blastDistanceColor = Color.web("#f58231");
        alsColor = Color.web("#f032e6");
        resaColor = Color.web("#aaffc3");
        stripEndColor = Color.web("#000075");
        slopeColor = Color.web("#469990");
        defaultColor = Color.web("#cccaca");
        clearedAndGradedColor = Color.MEDIUMPURPLE;
        instrumentStripColor = Color.web("#32174d");
    }
}
