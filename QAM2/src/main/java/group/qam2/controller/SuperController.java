package group.qam2.controller;

import group.qam2.QAM2Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Contains common methods used across all/most controller classes.
 * @author Nick Pantuso
 */
public abstract class SuperController {

    /**
     * Opens an fxml form.
     * @param event the button click
     * @param resource the resource
     * @param title the title
     * @param w the width
     * @param h the height
     * @throws IOException
     */
    public static void openForm(ActionEvent event, String resource, String title, int w, int h) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(QAM2Application.class.getResource(resource));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), w, h);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates an alert.
     * @param type the type of alert
     * @param title the title
     * @param content the content text
     * @return an alert
     */
    public static Alert createAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert;
    }

}
