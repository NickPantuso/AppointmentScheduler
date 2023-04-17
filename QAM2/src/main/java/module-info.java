module group.qam2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens group.qam2 to javafx.fxml, javafx.base;
    exports group.qam2;
    opens group.qam2.controller to javafx.fxml;
    exports group.qam2.controller;
    opens group.qam2.model to javafx.base;
    exports group.qam2.model;
}