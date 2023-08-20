package com.example.runway_redeclaration.view.component;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class NotificationDisplay {
    private static final int MAX_NOTIFICATIONS = 5;
    private static final int DURATION = 5000; // 5 seconds
    private static final int SPACING = 10;
    private static final int PADDING = 10;

    private final Stage stage;
    private final VBox notificationBox;
    private final ArrayList<Notification> notifications;

    String title;
    String type;
    String desc;
    String time;
    String username;

    /**
     * contructor to create a new notification display, takes parameters from notification controller
     * @param title
     * @param type
     * @param desc
     * @param time
     * @param username
     */
    public NotificationDisplay(String title, String type, String desc, String time, String username) {
        this.title = title;
        this.type = type;
        this.desc = desc;
        this.time = time;
        this.username = username;

        // Create a stage to display notifications
        stage = new Stage();
        stage.setTitle("Notifications");

        // Create a VBox to hold notifications
        notificationBox = new VBox();
        notificationBox.setAlignment(Pos.TOP_RIGHT);
        notificationBox.setPadding(new Insets(PADDING));
        notificationBox.setStyle("-fx-background-color: #b0d5d9;");
        notificationBox.setStyle("-fx-border-color: #91afb3; -fx-border-width: 1px;");
        notificationBox.setPadding(new Insets(10));
        notificationBox.setSpacing(SPACING);

        // Create an array list to store notifications
        notifications = new ArrayList<>();

        // Add the notification box to the scene
        Scene scene = new Scene(notificationBox);
        stage.setScene(scene);

        // Get the screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the stage size
        stage.setWidth(300);
        stage.setHeight(250);

        // Calculate the position of the top right corner
        double x = screenBounds.getMaxX() - stage.getWidth();
        double y = screenBounds.getMinY();

        // Set the stage position
        stage.setX(x);
        stage.setY(y);
    }

    public void showNotification() {
        // Create a new notification
        Notification notificationTitle = new Notification();
        notificationTitle.setTitle(title);
        notificationTitle.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold; -fx-text-fill: white;");

        Notification notificationType = new Notification();
        notificationType.setType("Type: " + type);
        notificationTitle.setStyle("-fx-font-size: 14pt; -fx-text-fill: #91afb3;");

        Notification notificationTime = new Notification();
        notificationTime.setType(time);
        notificationTime.setStyle("-fx-font-size: 12pt; -fx-text-fill: #91afb3;");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(400, 50);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Add the notification to the list of notifications
        notifications.add(notificationTitle);
        notifications.add(notificationType);
        notifications.add(notificationTime);

        // Add the notification to the notification box
        notificationBox.getChildren().addAll(notificationTitle, notificationType, notificationTime,scrollPane);

        // Remove the oldest notification if there are too many
        if (notifications.size() > MAX_NOTIFICATIONS) {
            Notification oldestNotification = notifications.remove(0);
            notificationBox.getChildren().remove(oldestNotification);
        }

        // Show the stage if it is not already showing
        if (!stage.isShowing()) {
            stage.show();
        }
        // Create a timeline to remove the notification after a certain amount of time
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(DURATION), e -> {
            notifications.remove(notificationTitle);
            notifications.remove(notificationType);
            notifications.remove(notificationTime);
            notificationBox.getChildren().removeAll(notificationTitle, notificationType, notificationTime);
            // Hide the stage if there are no more notifications
            if (notifications.isEmpty()) {
                stage.hide();
            }
        }));

        // Create a timer animation to show the lifecycle of the notification
        double width = notificationBox.getWidth() - 20;
        Rectangle timer = new Rectangle(width, 5, Color.BLUE);
        HBox timerBox = new HBox(timer);
        timerBox.setPrefSize(width, 5);
        timerBox.setAlignment(Pos.CENTER_LEFT);
        notificationBox.getChildren().add(timerBox);
        KeyValue kv = new KeyValue(timer.widthProperty(), 0);
        KeyFrame kf = new KeyFrame(Duration.millis(DURATION), kv);
        Timeline timerTimeline = new Timeline(kf);

        // Animate the timer bar color and width
        KeyValue kv1 = new KeyValue(timer.fillProperty(), Color.BLUE);
        KeyValue kv2 = new KeyValue(timer.widthProperty(), width * 0.75);
        KeyFrame kf1 = new KeyFrame(Duration.millis(DURATION * 0.5), kv1, kv2);
        KeyValue kv3 = new KeyValue(timer.fillProperty(), Color.BLUE);
        KeyValue kv4 = new KeyValue(timer.widthProperty(), width * 0.5);
        KeyFrame kf2 = new KeyFrame(Duration.millis(DURATION * 0.75), kv3, kv4);
        timerTimeline.getKeyFrames().addAll(kf1,kf2);
        // Add the timer animation to the timeline
        //timeline.getChildren().add(timerTimeline);

        // Start both timelines
        timeline.play();
        timerTimeline.play();
    }
}

