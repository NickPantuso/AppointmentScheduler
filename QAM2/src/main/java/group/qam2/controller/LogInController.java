package group.qam2.controller;

import group.qam2.model.Appointment;
import group.qam2.model.User;
import group.qam2.utilities.AppointmentQuery;
import group.qam2.utilities.UserQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controls the LogInForm.
 * @author Nick Pantuso
 */
public class LogInController implements Initializable {

    private boolean french = false;
    private ResourceBundle rb;

    @FXML private TextField userTxt = new TextField();
    @FXML private PasswordField passTxt = new PasswordField();
    @FXML private Button logInBtn = new Button();
    @FXML private Label errorLbl = new Label();
    @FXML private Label zoneLbl = new Label();
    @FXML private Label logLbl = new Label();
    @FXML private Label userLbl = new Label();
    @FXML private Label passLbl = new Label();

    /**
     * Opens the HomeForm. Notifies the user whether there's an appointment within 15 minutes of their log in.
     * @param event the button click
     * @throws IOException
     * @throws SQLException
     */
    private void openHomeForm(ActionEvent event) throws IOException, SQLException {
        SuperController.openForm(event, "/HomeForm.fxml", "Home Form", 922, 682);
        if (!checkForUpcomingAppts()) {
            Alert alert = SuperController.createAlert(Alert.AlertType.INFORMATION, "Appointment Notification", "There are no appointments starting within 15 minutes.");
            alert.showAndWait();
        }
    }

    /**
     * If there's an appointment within 15 minutes of the user's log in, displays an alert with the appointment ID
     * and local start date/time.
     * @return true if there is an appointment, false if otherwise
     * @throws SQLException
     */
    private boolean checkForUpcomingAppts() throws SQLException {
        LocalTime currentTime = LocalTime.now();
        long timeDifference;
        for(Appointment appt : AppointmentQuery.select()) {
            ZonedDateTime sZDT = appt.getStart();
            LocalTime startTime = sZDT.toLocalTime();
            timeDifference = ChronoUnit.MINUTES.between(currentTime, startTime);
            if(timeDifference <= 15 && timeDifference > 0) {
                String content = "There's an appointment taking place in 15 minutes or less.\nAppointment ID: " +
                        appt.getApptId() + " \nDate: " + sZDT.getMonthValue() + "/" +
                        sZDT.getDayOfMonth() + " \nTime: " + sZDT.getHour() + ":00 (Local Time)";
                Alert alert = SuperController.createAlert(Alert.AlertType.INFORMATION, "Appointment Notification", content);
                alert.showAndWait();
                return true;
            }
        }
        return false;
    }

    /**
     * When the submit button is clicked, tries to find the user in the database. If they're found, opens the home form. If the user
     * doesn't exist, an error will show. All log in attempts are recorded to file with a timestamp and whether it was a successful attempt.
     * If the user's language is in French, changes the error label to French.
     * @param event the button click
     * @throws SQLException
     * @throws IOException
     */
    @FXML protected void onSubmitClick(ActionEvent event) throws SQLException, IOException {
        boolean successful = false;
        LocalDateTime LDT = LocalDateTime.now();
        Timestamp stamp = Timestamp.valueOf(LDT);
        String filename = "login_activity.txt";
        FileWriter fwriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fwriter);

        for(User user : UserQuery.select()) {
            if(user.getUserName().equals(userTxt.getText()) && user.getPass().equals(passTxt.getText())) {
                openHomeForm(event);
                successful = true;
                outputFile.println("Timestamp: " + stamp + " Successful: " + true);
                break;
            }
        }
        if(!successful) {
            outputFile.println("Timestamp: " + stamp + " Successful: " + false);
        }
        outputFile.close();

        if(french) {
            errorLbl.setText(rb.getString("Error"));
        } else {
            errorLbl.setText("Sorry, that username and/or password is incorrect.");
        }
    }

    /**
     * Checks the default language of the user. If it's French, changes the log in form to read in French.
     * If the default language is not French, the language defaults to English.
     * Displays the time zone of the user.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(Locale.getDefault().getLanguage().equals("fr")) {
            rb = ResourceBundle.getBundle("/Nat", Locale.getDefault());
            french = true;
            logLbl.setText(rb.getString("LogIn"));
            userLbl.setText(rb.getString("Username") + ":");
            passLbl.setText(rb.getString("Password") + ":");
            logInBtn.setText(rb.getString("Submit"));
        }
        zoneLbl.setText("(Zone: " + ZoneId.systemDefault() + ")");
        errorLbl.setText("");
    }
}