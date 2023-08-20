package com.example.runway_redeclaration.view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Class to display the colors for a runway as a key.
 */
public class RunwayKeyCanvas extends Canvas {

    /**
     * Current x-offset.
     */
    private int xOffset;

    /**
     * Current y-offset.
     */
    private int yOffset;

    /**
     * x-offset increment factor.
     */
    private final int xOffsetInc;

    /**
     * y-offset increment factor.
     */
    private final int yOffsetInc;

    /**
     * Key size.
     */
    private final int keySize;

    /**
     * Height of canvas.
     */
    private final double height;

    /**
     * The graphics context used to draw in this canvas.
     */
    private GraphicsContext gc;

    /**
     * RunwayKeyCanvas constructor.
     */
    public RunwayKeyCanvas(double width, double height) {
        super(width,height);

        xOffsetInc = (int) (width * 0.45);
        xOffset = xOffsetInc / 2;
        yOffsetInc = (int) (height * 0.125);
        yOffset = yOffsetInc / 2;
        keySize = (int) (yOffsetInc * 0.3);
        this.height = height;

        gc = this.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(new Font("Orbitron",10));

        //gc.setFill(Color.WHITE);
        //gc.fillRect(0,0,width,height);
    }

    /**
     * Draw a single key instance.
     *
     * @param color
     * @param value
     */
    public void drawKey(Color color, String value) {
        gc.setFill(color);
        gc.fillRect(xOffset,yOffset, keySize, keySize);
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.fillText(value, xOffset + keySize * 0.5,  yOffset + keySize * 1.9);
        //xOffset += xOffsetInc;
        yOffset += yOffsetInc;

        if (yOffset >= (height - yOffsetInc)) {
            xOffset += xOffsetInc;
            yOffset = yOffsetInc/2;
        }
    }
}
