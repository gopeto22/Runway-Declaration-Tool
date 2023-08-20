package com.example.runway_redeclaration.view.component;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Notification extends StackPane {
    private static final int DURATION = 5000; // 5 seconds
    private static final int HEIGHT = 60;
    private static final Color BACKGROUND_COLOR = Color.web("#333333");
    private static final Color TEXT_COLOR = Color.web("#ffffff");
    private String notificationType;

    private final Label messageTitle;

    public Notification() {
        super();
        // Create a label to display the title
        messageTitle = new Label();
        //messageTitle.setFont(Font.font(FONT_FAMILY, FONT_WEIGHT, FONT_SIZE));
        messageTitle.setTextFill(TEXT_COLOR);

        // Create a label to display the description
        Label messageDescription = new Label();
        //messageDescription.setFont(Font.font(FONT_FAMILY, FONT_WEIGHT, FONT_SIZE));
        messageDescription.setTextFill(TEXT_COLOR);

        // Create a label to display the type
        Label messageType = new Label();
       // messageType.setFont(Font.font(FONT_FAMILY, FONT_WEIGHT, FONT_SIZE));
        messageType.setTextFill(TEXT_COLOR);

        // Create a label to display the type
        Label messsageTime = new Label();
        //messsageTime.setFont(Font.font(FONT_FAMILY, FONT_WEIGHT, FONT_SIZE));
        messsageTime.setTextFill(TEXT_COLOR);

        // Add the message label to the notification
        getChildren().add(messageTitle);
        // Initialize the timer bar
        Rectangle timer = new Rectangle(0, 0, getWidth(), 5);
        timer.setFill(Color.GREEN);
        timer.setStroke(Color.TRANSPARENT);
        timer.setVisible(false);

        // Add the timer bar to the notification
        getChildren().add(timer);
        // Set the background color and border of the notification
        setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, new CornerRadii(5), null)));
        setPrefHeight(HEIGHT);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5000), new KeyValue(translateYProperty(), -HEIGHT)),
                new KeyFrame(Duration.millis(1000), new KeyValue(translateYProperty(), 0)),
                new KeyFrame(Duration.millis(DURATION), event -> setVisible(false)));
        timeline.setCycleCount(1);
    }

    public void setTitle(String title) {
        messageTitle.setText(title);
    }
    public void setDescription(String description) {
        messageTitle.setText(description);
    }

    public String getNotificationType() {
        return notificationType;
    }
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public void setType(String messageType) {
        messageTitle.setText(messageType);
    }

}

