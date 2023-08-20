package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.model.Runway;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

/**
 * Store the measurement information about where to place the breakdown of runway values.
 */
public class RunwayMeasurement {

    /**
     * The graphics context for the canvas to draw to.
     */
    private GraphicsContext gc;

    /**
     * Store the left offset of the runway.
     */
    private double leftOffset;

    /**
     * Store the right offset of the runway.
     */
    private double rightOffset;

    /**
     * The color to draw.
     */
    private Color color;

    /**
     * The arrow size.
     */
    private int arrowSize;

    /**
     * True if the runway is left to right, false otherwise.
     */
    private final boolean LEFT_RUNWAY;

    /**
     * The height to draw at.
     */
    private double height;

    /**
     * The start of the runway x coordinate.
     */
    private double startOfRunwayXCoordinate;

    /**
     * The end of the runway x coordinate.
     */
    private double endOfRunwayXCoordinate;

    /**
     * The width of the canvas.
     */
    private double canvasWidth;

    /**
     * 1 if Taking off over / landing Towards
     * 0 if noCalc
     * -1 if landing Over / Taking off towards
     */
    private int landingTowards;

    /**
     * Runway being represented.
     */
    private Runway runway;

    /**
     * The width of the runway on the canvas.
     */
    private double runwayWidth;

    /**
     * Constructor for the runway measurement class.
     *
     * @param gc                       the graphics context for the canvas being used
     * @param startOfRunwayXCoordinate the x coordinate for the start of the runway in the canvas
     * @param endOfRunwayXCoordinate   the x coordinate for the end of the runway in the canvas
     */
    public RunwayMeasurement(GraphicsContext gc, double startOfRunwayXCoordinate, double endOfRunwayXCoordinate, boolean LEFT_RUNWAY, double canvasWidth, int landingTowards, Runway runway, double runwayWidth) {
        // Initialise variables.
        this.gc = gc;
        this.startOfRunwayXCoordinate = startOfRunwayXCoordinate;
        this.endOfRunwayXCoordinate = endOfRunwayXCoordinate;
        this.LEFT_RUNWAY = LEFT_RUNWAY;
        this.canvasWidth = canvasWidth;
        this.landingTowards = landingTowards;
        this.runway = runway;
        this.runwayWidth = runwayWidth;

        // Initialise the GraphicsContext.
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setLineWidth(1);

        // Initialise the color to default.
        color = Color.BLACK;

        // Initialise offset.
        this.resetOffset();
    }

    /**
     * Reset the offset to default runway values.
     */
    public void resetOffset() {
        leftOffset = startOfRunwayXCoordinate;
        rightOffset = endOfRunwayXCoordinate;
        System.out.println("resetOffset leftOffset = " + startOfRunwayXCoordinate);
        System.out.println("resertOffset rightOffset = " + endOfRunwayXCoordinate);
    }

    /**
     * Set the color for the next drawing.
     *
     * @param color the color to set
     */
    public void setColor(Color color) {
        gc.setFill(color);
        gc.setStroke(color);
    }

    /**
     * Set the height to draw at.
     *
     * @param height of the runway value to draw
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Set the line width of gc.
     *
     * @param size the size to set the line width to
     */
    public void setLineWidth(int size) {
        gc.setLineWidth(size);
    }

    /**
     * Set the size for the next arrow.
     *
     * @param size the size of the arrow head
     */
    public void setArrowSize(int size) {
        arrowSize = size;
    }

//    /**
//     * Draw a redeclared value.
//     *
//     * @param length the length of the value to display
//     */
//    public void drawRedeclaredValue(double length) {
//        //System.out.println("drawRedeclaredValue landingTowards = " + landingTowards);
//        // This means, draw on the same side as normal
//        if (landingTowards <= 0) {
//            drawOriginalValue(length);
//        }
//        // Otherwise, draw on the opposite side.
//        else {
//            if (LEFT_RUNWAY) {
//                drawValueFromRight(length);
//            } else {
//                drawValueFromLeft(length);
//            }
//        }
//    }
//
//    /**
//     * Draw a redeclared value with a label.
//     *
//     * @param text the label to display above the line
//     * @param length the length of the value to display
//     */
//    public void drawRedeclaredValue(String text, double length) {
//        // This means, draw on the same side as normal
//        if (landingTowards <= 0) {
//            drawOriginalValue(text, length);
//        }
//        // Otherwise, draw on the opposite side.
//        else {
//            if (LEFT_RUNWAY) {
//                drawValueFromRight(text, length);
//            } else {
//                drawValueFromLeft(text, length);
//            }
//        }
//    }

    /**
     * Draw a value to the runway.
     *
     * @param length the length of the runway segment
     */
    public void appendValue(Integer length) {
        var normalisedLength = normaliseToRunway(length);

        if (LEFT_RUNWAY) {
            drawDoubleArrow(leftOffset, height, leftOffset + normalisedLength, height);
            leftOffset += normalisedLength;
            System.out.println("drawOriginalValue leftOffset = " + leftOffset);
        } else {
            drawDoubleArrow(rightOffset - normalisedLength, height, rightOffset , height);
            rightOffset -= normalisedLength;
            System.out.println("drawOriginalValue rightOffset = " + rightOffset);
        }
    }

    /**
     * Draw a value to the runway with text above.
     *
     * @param text   text to draw
     * @param length of the runway component
     */
    public void appendValue(String text, Integer length) {
        appendValue(length);
        drawText(text, length);
    }

    /**
     * Draw the value on the left hand side of the runway.
     *
     * @param length of the value
     */
    public void drawValueFromLeft(Integer length) {
        var normalisedLength = normaliseToRunway(length);

        drawDoubleArrow(leftOffset, height, leftOffset + normalisedLength, height);
        leftOffset += normalisedLength;
        System.out.println("drawValueFromLeft leftOffset = " + leftOffset);
    }

    /**
     * Draw the value on the left hand side of the runway with text.
     *
     * @param text   text to draw
     * @param length of the runway component
     */
    public void drawValueFromLeft(String text, Integer length) {
        drawValueFromLeft(length);
        drawText(text, length);
    }

    /**
     * Draw the value on the right hand side of the runway.
     *
     * @param length of the value
     */
    public void drawValueFromRight(Integer length) {
        var normalisedLength = normaliseToRunway(length);

        drawDoubleArrow(rightOffset - normalisedLength, height, rightOffset, height);
        rightOffset -= normalisedLength;
        System.out.println("drawValueFromRight rightOffset = " + rightOffset);
    }

    /**
     * Draw the value on the right hand side of the runway with text.
     *
     * @param text   text to draw
     * @param length of the runway component
     */
    public void drawValueFromRight(String text, Integer length) {
        drawValueFromRight(length);
        drawText(text, length);
    }

    /**
     * Draw a line with 2 arrow heads by calling the drawArrow function twice.
     *
     * @param x1 start x of the line
     * @param y1 start y of the line
     * @param x2 end x of the line
     * @param y2 end y of the line
     */
    private void drawDoubleArrow(double x1, double y1, double x2, double y2) {
        drawArrow(arrowSize, x1, y1, x2, y2);
        drawArrow(arrowSize, x2, y2, x1, y1);
    }

    /**
     * Draw a line with 2 arrow heads by calling the drawArrow function twice.
     *
     * @param x1 start x of the line
     * @param y1 start y of the line
     * @param x2 end x of the line
     * @param y2 end y of the line
     */
    private void drawDoubleArrow(String text, double x1, double y1, double x2, double y2) {
        drawDoubleArrow(x1, y1, x2, y2);
        drawText(text, x1, y1, x2, y2);
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
     * Draw text above the given line.
     *
     * @param text the text to draw
     * @param x1   startX
     * @param y1   startY
     * @param x2   endX
     * @param y2   endY
     */
    private void drawText(String text, double x1, double y1, double x2, double y2) {
        double xAvg = (x1 + x2) / 2;
        double yAvg = (y1 + y2) / 2;

        gc.setFill(Color.BLACK);
        gc.fillText(text, xAvg, yAvg - canvasWidth * 0.009);
    }

    /**
     * Draw text in the middle of the given length.
     *
     * @param text   to write
     * @param length to draw in the middle of
     */
    private void drawText(String text, Integer length) {
        var normalisedLength = normaliseToRunway(length);
        if (LEFT_RUNWAY) {
            drawText(text, leftOffset - normalisedLength, height, leftOffset, height);
        } else {
            drawText(text, rightOffset + normalisedLength, height, rightOffset, height);
        }
//        // Draw the text relative to the original direction.
//        if (landingTowards <= 0) {
//            if (LEFT_RUNWAY) {
//                drawText(text, leftOffset - normalisedLength, height, leftOffset, height);
//            } else {
//                drawText(text, rightOffset + normalisedLength, height, rightOffset, height);
//            }
//        }
//        // Otherwise, do the opposite calculations.
//        else {
//            if (LEFT_RUNWAY) {
//                drawText(text, rightOffset + normalisedLength, height, rightOffset, height);
//            } else {
//                drawText(text, leftOffset - normalisedLength, height, leftOffset, height);
//            }
//        }
    }

    /**
     * Normalise a runway value relative to the original tora
     *
     * @param runwayVal the original runway value to normlaise
     * @return the normalised value
     */
    private double normaliseToRunway(Integer runwayVal) {
        if (runwayVal == null) {
            System.out.println("Can't normalise null value.");
            return 0;
        }
        // Original tora is the length of the runway available for takeoff, so use this to normalise.
        return ((double) runwayVal / (double) runway.originalToraProperty().get()) * runwayWidth;
    }
}
