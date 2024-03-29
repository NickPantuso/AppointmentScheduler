package group.scheduler;

import group.scheduler.utilities.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.beans.PropertyVetoException;
import java.io.IOException;

/**
 * Launches the application.
 * @author Nick Pantuso
 */
public class SchedulerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SchedulerApplication.class.getResource("/LogInForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws PropertyVetoException {
        JDBC.setupConnection();
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}