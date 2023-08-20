module com.example.runway_redeclaration {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
  requires org.apache.logging.log4j;
  requires java.desktop;
    requires junit;

    opens com.example.runway_redeclaration to javafx.fxml;
    exports com.example.runway_redeclaration;
    exports com.example.runway_redeclaration.model;
}