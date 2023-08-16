module group.scheduler {
    requires java.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires transitive javafx.base;
    requires java.sql;
    requires mysql.connector.j;
    requires java.naming;
    requires java.desktop;
    requires c3p0;

    exports group.scheduler;
    exports group.scheduler.controller;
    exports group.scheduler.model;
    opens group.scheduler to javafx.fxml, javafx.base, javafx.graphics;
    opens group.scheduler.controller to javafx.fxml, javafx.graphics;
    opens group.scheduler.model to javafx.base, javafx.graphics;

}