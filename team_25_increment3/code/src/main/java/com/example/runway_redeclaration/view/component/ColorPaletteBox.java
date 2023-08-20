package com.example.runway_redeclaration.view.component;

import com.example.runway_redeclaration.UIColor;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * This class represents a single colour to edit in the color palette scene.
 */
public class ColorPaletteBox extends VBox {

    /**
     * The color this palette box is representing.
     */
    private Color color;

    private String name;

    private Rectangle colorSquare;

    /**
     * ColorPalleteScene constructor
     *
     * @param name the name of the runway parameter
     * @param color the colour or the runway parameter
     */
    public ColorPaletteBox(String name, Color color, int width, UIColor uiColor) {
        /*
        Initialisation.
         */
        this.color = color;
        this.name = name;
        this.getStyleClass().add("componentbg");
        this.setPadding(new Insets(5,5,5,5));
        this.setSpacing(2);
        this.setMinWidth(width);
        this.setMaxWidth(width);

        /*
        UI Elements
         */
        var paletteHBox = new HBox();
        paletteHBox.setPadding(new Insets(4,4,4,4));
        paletteHBox.setSpacing(5);

        var colorVBox = new VBox();

        colorSquare = new Rectangle();
        colorSquare.setX(20);
        colorSquare.setY(20);
        colorSquare.setWidth(40);
        colorSquare.setHeight(40);
        colorSquare.setFill(color);

        var colorText = new Text(name);
        colorText.getStyleClass().add("customtext");
        colorText.setTextAlignment(TextAlignment.CENTER);

        var hexColor = new Text("Hex Code: #" + color.toString().substring(2,8));
        hexColor.getStyleClass().add("customtext");

        colorVBox.getChildren().addAll(colorText, hexColor);

        paletteHBox.getChildren().addAll(colorSquare, colorVBox);

        var paletteColorPicker = new ColorPicker(color);
        paletteColorPicker.getStyleClass().add("smallbutton");

        paletteColorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                var newColor = paletteColorPicker.getValue();
                hexColor.setText("Hex Code: #" + newColor.toString().substring(2,8));
                colorSquare.setFill(newColor);

                switch(name) {
                    case "Displaced Threshold":
                        uiColor.setDisplacedThresholdColor(newColor);
                        break;
                    case "Obstacle":
                        uiColor.setObstacleColor(newColor);
                        break;
                    case "Stopway":
                        uiColor.setStopwayColor(newColor);
                        break;
                    case "Clearway":
                        uiColor.setClearwayColor(newColor);
                        break;
                    case "TORA":
                        uiColor.setOriginalToraColor(newColor);
                        break;
                    case "Obstacle Distance from Threshold":
                        uiColor.setObstacleDistanceThresholdColor(newColor);
                        break;
                    case "Blast Distance":
                        uiColor.setBlastDistanceColor(newColor);
                        break;
                    case "ALS":
                        uiColor.setAlsColor(newColor);
                        break;
                    case "RESA":
                        uiColor.setResaColor(newColor);
                        break;
                    case "Strip End":
                        uiColor.setStripEndColor(newColor);
                        break;
                    case "TOCS":
                        uiColor.setSlopeColor(newColor);
                        break;
                    case "Cleared and Graded":
                        uiColor.setClearedAndGradedColor(newColor);
                        break;
                    case "Instrument Strip":
                        uiColor.setInstrumentStripColor(newColor);
                        break;
                    default:
                        System.out.println("Couldn't identify parameter.");
                }
            }
        });

        this.getChildren().addAll(paletteHBox, paletteColorPicker);
    }

    /**
     * color getter.
     *
     * @return the color represented by this color palette
     */
    public Color getColor() {
        // I do it like this because for some reason color refuses to be set to.
        return (Color) colorSquare.getFill();
    }

    /**
     * name getter.
     *
     * @return the name represented by this color palette
     */
    public String getName() {
        return name;
    }
}
