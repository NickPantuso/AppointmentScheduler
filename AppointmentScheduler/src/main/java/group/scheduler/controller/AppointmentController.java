package group.scheduler.controller;

import group.scheduler.model.Appointment;
import group.scheduler.model.Contact;
import group.scheduler.model.Customer;
import group.scheduler.model.User;
import group.scheduler.utilities.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ResourceBundle;

/**
 * Controls the AppointmentForm.
 * @author Nick Pantuso
 */
public class AppointmentController implements Initializable {

    private ObservableList<Customer> customers;
    private ObservableList<User> users;
    private ObservableList<Contact> contacts;

    @FXML private Label apptTitle = new Label();
    @FXML private TextField apptIdTxt = new TextField();
    @FXML private TextField titleTxt = new TextField();
    @FXML private TextField descTxt = new TextField();
    @FXML private TextField locTxt = new TextField();
    @FXML private TextField typeTxt = new TextField();
    @FXML private TextField startYear = new TextField();
    @FXML private ComboBox<String> startMonth = new ComboBox<>();
    @FXML private ComboBox<String> startDay = new ComboBox<>();
    @FXML private ComboBox<String> startTime = new ComboBox<>();
    @FXML private TextField endYear = new TextField();
    @FXML private ComboBox<String> endMonth = new ComboBox<>();
    @FXML private ComboBox<String> endDay = new ComboBox<>();
    @FXML private ComboBox<String> endTime = new ComboBox<>();
    @FXML private ComboBox<Customer> custBox = new ComboBox<>();
    @FXML private ComboBox<User> userBox = new ComboBox<>();
    @FXML private ComboBox<Contact> contactBox = new ComboBox<>();
    @FXML private Label errorLbl = new Label();

    /**
     * Checks for errors in the user's input. Errors include having an empty field, the year field having an invalid value,
     * the date going outside of office hours, and the date overlapping another appointment.
     * @param sZDT the appointment's local start time
     * @param eZDT the appointment's local end time
     * @return false if any errors were found, true otherwise
     * @throws SQLException
     */
    private boolean errorCheck(ZonedDateTime sZDT, ZonedDateTime eZDT) throws SQLException {
        if(titleTxt.getText().equals("") || descTxt.getText().equals("") || locTxt.getText().equals("") || typeTxt.getText().equals("") ||
                  startMonth.getValue() == null || endMonth.getValue() == null || startDay.getValue() == null || endDay.getValue() == null ||
                startTime.getValue() == null || endTime.getValue() == null || custBox.getValue() == null || userBox.getValue() == null || contactBox.getValue() == null) {
            errorLbl.setText("Please make sure there's a value in every field.");
            return false;
        }
        if(startYear.getText().length() != 4 || endYear.getText().length() != 4) {
            errorLbl.setText("The year fields must be in YYYY format.");
            return false;
        }

        if (!checkOfficeHours(sZDT, eZDT)) return false;

        return checkForOverlap(true, sZDT, eZDT);
    }

    /**
     * Checks all appointment dates to make sure the new date does not overlap with existing ones.
     * @param pass is false if there was an overlap found, true otherwise
     * @param sZDT the appointment's local start time
     * @param eZDT the appointment's local end time
     * @return pass
     * @throws SQLException
     */
    private boolean checkForOverlap(boolean pass, ZonedDateTime sZDT, ZonedDateTime eZDT) throws SQLException {
        int apptId = 0;
        if(apptTitle.getText().equals("Update Appointment")) {
            apptId = Integer.parseInt(apptIdTxt.getText());

        }
        for(Appointment appt : AppointmentQuery.select()) {
            if(appt.getApptId() != apptId) {
                ZonedDateTime aStart = appt.getStart();
                ZonedDateTime aEnd = appt.getEnd();
                if(sZDT.isAfter(aStart) && sZDT.isBefore(aEnd)) {
                    errorLbl.setText("This appointment overlaps with an existing appointment.");
                    pass = false;
                } else if(eZDT.isAfter(aStart) && eZDT.isBefore(aEnd)) {
                    errorLbl.setText("This appointment overlaps with an existing appointment.");
                    pass = false;
                } else if(sZDT.equals(aStart) || eZDT.equals(aEnd)) {
                    errorLbl.setText("This appointment overlaps with an existing appointment.");
                    pass = false;
                }
            }
        }
        return pass;
    }

    /**
     * Makes sure the local times from user input don't go outside EST business hours (8AM - 10PM).
     * @param sZDT The appointment's local start time
     * @param eZDT The appointment's local end time
     * @return false if there was an error found, true otherwise
     */
    private boolean checkOfficeHours(ZonedDateTime sZDT, ZonedDateTime eZDT) {
        ZoneId zone = ZoneId.of("America/New_York");
        ZonedDateTime sESTZDT = ZonedDateTime.ofInstant(sZDT.toInstant(), zone);
        ZonedDateTime eESTZDT = ZonedDateTime.ofInstant(eZDT.toInstant(), zone);
        if(sESTZDT.getYear() != eESTZDT.getYear() || sESTZDT.getMonthValue() != eESTZDT.getMonthValue() ||
            sESTZDT.getDayOfMonth() != eESTZDT.getDayOfMonth() || sESTZDT.getHour() < 8 ||
                sESTZDT.getHour() > 22 || eESTZDT.getHour() < 8 || eESTZDT.getHour() > 22) {
            errorLbl.setText("The appointment cannot take place outside of business hours (8 AM to 10 PM EST).");
            return false;
        }
        if(sESTZDT.isAfter(eESTZDT)) {
            errorLbl.setText("The start time must be before the end time.");
            return false;
        }
        return true;
    }

    /**
     * Sets the title to 'Add Appointment' and sets all input fields to empty.
     */
    public void makeAddApptForm() {
        apptTitle.setText("Add Appointment");
        apptIdTxt.setText("Auto - Generated");
        titleTxt.setText("");
        descTxt.setText("");
        locTxt.setText("");
        typeTxt.setText("");
        startYear.setText("");
        endYear.setText("");
    }

    /**
     * Sets the title to 'Update Appointment' and fills in all input fields with the selected appointment.
     * LAMBDA: I used lambda expressions when looping through the database tables to increase code readability and
     * decrease the amount of lines.
     * @param selectedAppt the appointment to fill the input fields
     */
    public void makeUpdateApptForm(Appointment selectedAppt) {
        apptTitle.setText("Update Appointment");
        apptIdTxt.setText(selectedAppt.getApptId()+"");
        titleTxt.setText(selectedAppt.getTitle());
        descTxt.setText(selectedAppt.getDesc());
        locTxt.setText(selectedAppt.getLoc());
        typeTxt.setText(selectedAppt.getType());

        ZonedDateTime sZDT = selectedAppt.getStart();
        ZonedDateTime eZDT = selectedAppt.getEnd();

        startYear.setText(sZDT.getYear()+"");
        endYear.setText(eZDT.getYear()+"");
        startMonth.setValue(sZDT.getMonthValue()+"");
        endMonth.setValue(eZDT.getMonthValue()+"");
        startDay.setValue(sZDT.getDayOfMonth()+"");
        endDay.setValue(eZDT.getDayOfMonth()+"");
        startTime.setValue(sZDT.getHour()+":00");
        endTime.setValue(eZDT.getHour()+":00");

        int custId = selectedAppt.getCustId();
        Customer cust = customers.stream()
                .filter(c -> c.getCustId() == custId)
                .findFirst()
                .orElse(null);
        custBox.setValue(cust);

        int userId = selectedAppt.getUserId();
        User user = users.stream()
                .filter(u -> u.getUserId() == userId)
                .findFirst()
                .orElse(null);
        userBox.setValue(user);

        String contactName = selectedAppt.getContactName();
        Contact contact = contacts.stream()
                .filter(c -> c.getContactName().equals(contactName))
                .findFirst()
                .orElse(null);
        contactBox.setValue(contact);
    }

    /**
     * When the submit button is clicked, checks for null, format, and input errors before submitting to the database.
     * Depending on whether an 'add' or 'update' form was created, inserts or updates a row in the database.
     * Opens the home form when finished.
     * @param event the button click
     * @throws SQLException
     */
    @FXML protected void submitAppt(ActionEvent event) throws SQLException {
        errorLbl.setText("");
        ZonedDateTime sZDT;
        ZonedDateTime eZDT;
        try {
            sZDT = setZonedDateTime(startYear, startMonth, startDay, startTime);
            eZDT = setZonedDateTime(endYear, endMonth, endDay, endTime);
        } catch (NullPointerException e) {
            errorLbl.setText("Please make sure there's a value in every field.");
            return;
        } catch (NumberFormatException e) {
            errorLbl.setText("Please make sure there's a value in every field and that the year is a number.");
            return;
        } catch (DateTimeException e){
            errorLbl.setText("That date does not exist.");
            return;
        }
        if(errorCheck(sZDT, eZDT)) {
            if (apptTitle.getText().equals("Add Appointment")) {
                addAppt(sZDT, eZDT);
            }
            if (apptTitle.getText().equals("Update Appointment")) {
                updateAppt(sZDT, eZDT);
            }
            try {
                openHomeForm(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Adds an appointment to the database.
     * @param sZDT the appointment's local start time
     * @param eZDT the appointment's local end time
     * @throws SQLException
     */
    private void addAppt(ZonedDateTime sZDT, ZonedDateTime eZDT) throws SQLException {
        Timestamp sStmp = Timestamp.valueOf(sZDT.toLocalDateTime());
        Timestamp eStmp = Timestamp.valueOf(eZDT.toLocalDateTime());
        AppointmentQuery.insert(titleTxt.getText(), descTxt.getText(), locTxt.getText(), typeTxt.getText(),
                sStmp, eStmp, custBox.getValue().getCustId(), userBox.getValue().getUserId(),
                contactBox.getValue().getContactId());
    }

    /**
     * Updates an appointment in the database.
     * @param sZDT the appointment's local start time
     * @param eZDT the appointment's local end time
     * @throws SQLException
     */
    private void updateAppt(ZonedDateTime sZDT, ZonedDateTime eZDT) throws SQLException {
        Timestamp sStmp = Timestamp.valueOf(sZDT.toLocalDateTime());
        Timestamp eStmp = Timestamp.valueOf(eZDT.toLocalDateTime());
        AppointmentQuery.update(titleTxt.getText(), descTxt.getText(), locTxt.getText(), typeTxt.getText(),
                sStmp, eStmp, custBox.getValue().getCustId(), userBox.getValue().getUserId(),
                contactBox.getValue().getContactId(), Integer.parseInt(apptIdTxt.getText()));
    }

    /**
     * Opens the home form.
     * @param event the button click
     * @throws IOException
     */
    @FXML protected void openHomeForm(ActionEvent event) throws IOException {
        SuperController.openForm(event, "/HomeForm.fxml", "Home Form", 922, 682);
    }

    /**
     * Creates a local ZonedDateTime based on the parameters.
     * @param yearTxt the year
     * @param monthTxt the month value
     * @param dayTxt the day
     * @param timeTxt the hour
     * @return a local ZonedDateTime
     */
    private ZonedDateTime setZonedDateTime(TextField yearTxt, ComboBox<String> monthTxt, ComboBox<String> dayTxt, ComboBox<String> timeTxt) {
        int year = Integer.parseInt(yearTxt.getText());
        int month = Integer.parseInt(monthTxt.getValue()+"");
        int day = Integer.parseInt(dayTxt.getValue()+"");
        String timeStr = timeTxt.getValue();
        int time = Integer.parseInt(timeStr.substring(0, timeStr.length() - 3));
        LocalDate LD = LocalDate.of(year, month, day);
        LocalTime LT = LocalTime.of(time, 0);
        LocalDateTime LDT = LocalDateTime.of(LD, LT);
        ZoneId zone = ZoneId.systemDefault();
        return ZonedDateTime.of(LDT, zone);
    }

    /**
     * Initializes error labels, combo boxes, and observable lists.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLbl.setText("");
        for(int i = 1; i < 13; i++) {
            startMonth.getItems().add(i+"");
            endMonth.getItems().add(i+"");
        }
        for(int i = 1; i < 32; i++) {
            startDay.getItems().add(i+"");
            endDay.getItems().add(i+"");
        }
        for(int i = 0; i < 24; i++) {
            startTime.getItems().add(i+":00");
            endTime.getItems().add(i+":00");
        }
        custBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Customer customer) {
                return customer.getCustId()+"";
            }

            @Override
            public Customer fromString(String s) {
                return null;
            }
        });
        userBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(User user) {
                return user.getUserId()+"";
            }

            @Override
            public User fromString(String s) {
                return null;
            }
        });
        contactBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Contact contact) {
                return contact.getContactName();
            }

            @Override
            public Contact fromString(String s) {
                return null;
            }
        });
        try {
            customers = CustomerQuery.select();
            custBox.setItems(customers);
            users = UserQuery.select();
            userBox.setItems(users);
            contacts = ContactQuery.select();
            contactBox.setItems(contacts);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
