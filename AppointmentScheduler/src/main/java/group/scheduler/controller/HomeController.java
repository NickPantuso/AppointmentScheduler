package group.scheduler.controller;

import group.scheduler.model.Appointment;
import group.scheduler.model.Customer;
import group.scheduler.utilities.AppointmentQuery;
import group.scheduler.utilities.CustomerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controls the HomeForm.
 * @author Nick Pantuso
 */
public class HomeController implements Initializable {

    private FXMLLoader loader;
    private Customer selectedCust;
    private Appointment selectedAppt;

    @FXML private TableView<Customer> customerTable = new TableView<>();
    @FXML private TableColumn<Customer, Integer> custId = new TableColumn<>();
    @FXML private TableColumn<Customer, String> custName = new TableColumn<>();
    @FXML private TableColumn<Customer, String> address = new TableColumn<>();
    @FXML private TableColumn<Customer, String> postal = new TableColumn<>();
    @FXML private TableColumn<Customer, String> phoneNum = new TableColumn<>();
    @FXML private TableColumn<Customer, String> div = new TableColumn<>();

    @FXML private TableView<Appointment> apptTable = new TableView<>();
    @FXML private TableColumn<Appointment, Integer> apptId = new TableColumn<>();
    @FXML private TableColumn<Appointment, String> title = new TableColumn<>();
    @FXML private TableColumn<Appointment, String> desc = new TableColumn<>();
    @FXML private TableColumn<Appointment, String> loc = new TableColumn<>();
    @FXML private TableColumn<Appointment, String> contact = new TableColumn<>();
    @FXML private TableColumn<Appointment, Double> type = new TableColumn<>();
    @FXML private TableColumn<Appointment, Date> start = new TableColumn<>();
    @FXML private TableColumn<Appointment, Date> end = new TableColumn<>();
    @FXML private TableColumn<Appointment, Integer> apptCustId = new TableColumn<>();
    @FXML private TableColumn<Appointment, Integer> userId = new TableColumn<>();

    @FXML private RadioButton weekBtn = new RadioButton();
    @FXML private RadioButton monthBtn = new RadioButton();
    @FXML private RadioButton allBtn = new RadioButton();
    @FXML private Label custError = new Label();
    @FXML private Label apptError = new Label();

    /**
     * Opens the 'Update' CustomerForm. If a customer wasn't selected to update, the user will be notified.
     * @param event the button click
     * @throws IOException
     * @throws SQLException
     */
    @FXML protected void openUpdateCustomerForm(ActionEvent event) throws IOException, SQLException {
        selectedCust = customerTable.getSelectionModel().getSelectedItem();
        if(selectedCust == null) {
            custError.setText("You need to select a customer to update first!");
            return;
        }

        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/CustomerForm.fxml"));
        loader.load();
        Button btn = (Button) event.getSource();
        CustomerController cc = loader.getController();
        cc.makeUpdateCustForm(selectedCust);
        SuperController.openLoadedForm(btn, loader, "Customer Form", 749, 711);
    }

    /**
     * Opens the 'Add' CustomerForm.
     * @param event
     * @throws IOException
     */
    @FXML protected void openAddCustomerForm(ActionEvent event) throws IOException {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/CustomerForm.fxml"));
        loader.load();
        Button btn = (Button) event.getSource();
        CustomerController cc = loader.getController();
        cc.makeAddCustForm();
        SuperController.openLoadedForm(btn, loader, "Customer Form", 749, 711);
    }

    /**
     * Opens the 'Update' AppointmentForm. If an appointment wasn't selected to update, the user will be notified.
     * @param event the button click
     * @throws IOException
     */
    @FXML protected void openUpdateApptForm(ActionEvent event) throws IOException {
        selectedAppt = apptTable.getSelectionModel().getSelectedItem();
        if(selectedAppt == null) {
            apptError.setText("You need to select an appointment to update first!");
            return;
        }

        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AppointmentForm.fxml"));
        loader.load();
        Button btn = (Button) event.getSource();
        AppointmentController ac = loader.getController();
        ac.makeUpdateApptForm(selectedAppt);
        SuperController.openLoadedForm(btn, loader, "Appointment Form", 770, 816);
    }

    /**
     * Opens the 'Add' AppointmentForm.
     * @param event
     * @throws IOException
     */
    @FXML protected void openAddApptForm(ActionEvent event) throws IOException {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AppointmentForm.fxml"));
        loader.load();
        Button btn = (Button) event.getSource();
        AppointmentController ac = loader.getController();
        ac.makeAddApptForm();
        SuperController.openLoadedForm(btn, loader, "Appointment Form", 770, 816);
    }

    /**
     * Opens the ReportForm.
     * @param event the button click
     * @throws IOException
     */
    @FXML protected void openReportForm(ActionEvent event) throws IOException {
        SuperController.openForm(event, "/ReportForm.fxml", "Report Form", 741, 667);
    }

    /**
     * Deletes a customer from the database. If no customer was selected, it notifies the user. If a customer still has appointments
     * scheduled, an alert will pop up on screen denying the action. If nothing is wrong, an alert will pop up asking for confirmation.
     * @throws SQLException
     */
    @FXML protected void deleteCust() throws SQLException {
        selectedCust = customerTable.getSelectionModel().getSelectedItem();
        if(selectedCust == null) {
            custError.setText("You need to select a customer to delete first!");
        } else {
            for(Appointment a : AppointmentQuery.select()) {
                if(a.getCustId() == selectedCust.getCustId()) {
                    Alert alert = SuperController.createAlert(Alert.AlertType.WARNING, "Deletion Warning", "You must delete all of this customer's appointments first.");
                    alert.showAndWait();
                    return;
                }
            }
            Alert alert = SuperController.createAlert(Alert.AlertType.CONFIRMATION, "Deletion Confirmation", "Are you sure you want to delete this customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                custError.setText("Customer successfully deleted!");
                CustomerQuery.delete(selectedCust.getCustId());
                customerTable.setItems(CustomerQuery.select());
            }
        }
    }

    /**
     * Deletes an appointment from the database. If no appointment was selected, it notifies the user.
     * If nothing is wrong, an alert will pop up asking for confirmation, along with the appointment ID and type.
     * @throws SQLException
     */
    @FXML protected void deleteAppt() throws SQLException {
        selectedAppt = apptTable.getSelectionModel().getSelectedItem();
        if(selectedAppt == null) {
            apptError.setText("You need to select an appointment to delete first!");
        } else {
            String content = "Delete the following appointment?\nAppointment ID: " + selectedAppt.getApptId() +
            " \nType: " + selectedAppt.getType();
            Alert alert = SuperController.createAlert(Alert.AlertType.CONFIRMATION, "Deletion Confirmation", content);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                apptError.setText("Appointment successfully deleted!");
                AppointmentQuery.delete(selectedAppt.getApptId());
                apptTable.setItems(AppointmentQuery.select());
            }
        }
    }

    /**
     * Changes the contents of the appointment table based on what radio button is selected.
     * The 'Week' button will show appointments for the current week, the 'Month' button will show appointments for the current month,
     * and the 'All' button will show all appointments scheduled.
     * LAMBDA: I used a lambda expression here because it greatly simplified the loop I needed to write. It shortened the lines of code
     * by 3-4.
     * @throws SQLException
     */
    @FXML protected void changeTable() throws SQLException {
        ObservableList<Appointment> appts = AppointmentQuery.select();
        LocalDateTime LDT = LocalDateTime.now();
        if(allBtn.isSelected()) {
            apptTable.setItems(appts);
        }
        if(weekBtn.isSelected()) {
            int currentWeek = getWeekOfYear(LDT);
            ObservableList<Appointment> newAppts = FXCollections.observableArrayList();
            for(Appointment appt : appts) {
                LocalDateTime aLDT = appt.getStart().toLocalDateTime();
                if(LDT.getYear() == aLDT.getYear()) {
                    int apptWeek = getWeekOfYear(aLDT);
                    if(currentWeek == apptWeek) {
                        newAppts.add(appt);
                    }
                }
            }
            apptTable.setItems(newAppts);
        }
        if(monthBtn.isSelected()) {
            int currentMonth = LDT.getMonthValue();
            ObservableList<Appointment> newAppts = appts.stream()
                    .filter(a -> a.getStart().getYear() == LDT.getYear())
                    .filter(a -> a.getStart().getMonthValue() == currentMonth)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            apptTable.setItems(newAppts);
        }
    }

    /**
     * Returns the local week of year.
     * @param LDT the current LocalDateTime
     * @return the week of year (int)
     */
    private int getWeekOfYear(LocalDateTime LDT) {
        Locale locale = Locale.getDefault();
        return LDT.get(WeekFields.of(locale).weekOfYear());
    }

    /**
     * Initializes what radio button is selected, error labels, and the table views.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allBtn.setSelected(true);
        custError.setText("");
        apptError.setText("");

        try {
            customerTable.setItems(CustomerQuery.select());
            apptTable.setItems(AppointmentQuery.select());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        custId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        custName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postal.setCellValueFactory(new PropertyValueFactory<>("postal"));
        phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        div.setCellValueFactory(new PropertyValueFactory<>("divName"));

        apptId.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        desc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        loc.setCellValueFactory(new PropertyValueFactory<>("loc"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startFormat"));
        end.setCellValueFactory(new PropertyValueFactory<>("endFormat"));
        apptCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }
}
