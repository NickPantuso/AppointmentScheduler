package group.qam2.controller;

import group.qam2.model.*;
import group.qam2.utilities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controls the ReportForm.
 * @author Nick Pantuso
 */
public class ReportController implements Initializable {

    @FXML private ComboBox<Appointment> typeBox = new ComboBox<>();
    @FXML private ComboBox<String> monthBox = new ComboBox<>();
    @FXML private ComboBox<Country> countryBox = new ComboBox<>();
    @FXML private Label typeNum = new Label();
    @FXML private Label monthNum = new Label();
    @FXML private Label countryNum = new Label();
    @FXML private ComboBox<Contact> contactBox = new ComboBox<>();
    @FXML private TableView<Appointment> contactTable = new TableView<>();
    @FXML private TableColumn<Appointment, Integer> apptId = new TableColumn<>();
    @FXML private TableColumn<Appointment, String> title = new TableColumn<>();
    @FXML private TableColumn<Appointment, String> type = new TableColumn<>();
    @FXML private TableColumn<Appointment, String> description = new TableColumn<>();
    @FXML private TableColumn<Appointment, Date> start = new TableColumn<>();
    @FXML private TableColumn<Appointment, Date> end = new TableColumn<>();
    @FXML private TableColumn<Appointment, Integer> custId = new TableColumn<>();

    /**
     * Calculates how many appointments there are for the type chosen.
     * @throws SQLException
     */
    @FXML protected void calculateTypeNum() throws SQLException {
        String type = typeBox.getValue().getType();
        int counter = 0;
        for(Appointment appt : AppointmentQuery.select()) {
            if(appt.getType().equals(type)) {
                counter++;
            }
        }
        typeNum.setText(counter+"");
    }

    /**
     * Calculates how many appointments there are for the month chosen. Does not take into account years.
     * @throws SQLException
     */
    @FXML protected void calculateMonthNum() throws SQLException {
        int month = Integer.parseInt(monthBox.getValue()+"");
        int counter = 0;
        for(Appointment appt : AppointmentQuery.select()) {
            ZonedDateTime ZDT = appt.getStart();
            if(ZDT.getMonthValue() == month) {
                counter++;
            }
        }
        monthNum.setText(counter+"");
    }

    /**
     * Calculates how many customers there are for the country chosen.
     * @throws SQLException
     */
    @FXML protected void calculateCountryNum() throws SQLException {
        int countryId = countryBox.getValue().getCountryId();
        int counter = 0;
        for(Customer cust : CustomerQuery.select()) {
            int custCountry = DivisionQuery.select(cust.getDivName()).getCountryId();
            if(countryId == custCountry) {
                counter++;
            }
        }
        countryNum.setText(counter+"");
    }

    /**
     * Changes the contact table based on the contact chosen.
     * LAMBDA: I used a lambda expression here to simplify the loop and reduce the lines written.
     * @throws SQLException
     */
    @FXML protected void changeTable() throws SQLException {
        String contactName = contactBox.getValue().getContactName();
        ObservableList<Appointment> appts = AppointmentQuery.select();
        ObservableList<Appointment> newAppts = appts.stream()
                .filter(a -> a.getContactName().equals(contactName))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        contactTable.setItems(newAppts);
    }

    /**
     * Opens the HomeForm.
     * @param event the button click
     */
    @FXML protected void openHomeForm(ActionEvent event) throws IOException {
        SuperController.openForm(event, "/HomeForm.fxml", "Home Form", 922, 682);
    }

    /**
     * Initializes the labels that show the results of the calculations, the combo boxes, as well as the contact table.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeNum.setText("0");
        monthNum.setText("0");
        countryNum.setText("0");
        for(int i = 1; i < 13; i++) {
            monthBox.getItems().add(i+"");
        }
        typeBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Appointment appt) {
                return appt.getType();
            }

            @Override
            public Appointment fromString(String s) {
                return null;
            }
        });
        countryBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Country country) {
                return country.getCountryName()+"";
            }

            @Override
            public Country fromString(String s) {
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
            typeBox.setItems(AppointmentQuery.select());
            countryBox.setItems(CountryQuery.select());
            contactBox.setItems(ContactQuery.select());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        apptId.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        description.setCellValueFactory(new PropertyValueFactory<>("desc"));
        start.setCellValueFactory(new PropertyValueFactory<>("startFormat"));
        end.setCellValueFactory(new PropertyValueFactory<>("endFormat"));
        custId.setCellValueFactory(new PropertyValueFactory<>("custId"));
    }
}
