package group.scheduler.controller;

import group.scheduler.model.Country;
import group.scheduler.model.Customer;
import group.scheduler.model.FirstLevelDivision;
import group.scheduler.utilities.CountryQuery;
import group.scheduler.utilities.CustomerQuery;
import group.scheduler.utilities.DivisionQuery;
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
import java.util.ResourceBundle;

/**
 * Controls the CustomerForm.
 * @author Nick Pantuso
 */
public class CustomerController implements Initializable {

    private ObservableList<Country> countries;

    @FXML private Label custTitle = new Label();
    @FXML private TextField custIdTxt = new TextField();
    @FXML private TextField custNameTxt = new TextField();
    @FXML private TextField addressTxt = new TextField();
    @FXML private TextField postalTxt = new TextField();
    @FXML private TextField phoneTxt = new TextField();
    @FXML private ComboBox<Country> countryBox = new ComboBox<>();
    @FXML private ComboBox<FirstLevelDivision> divBox = new ComboBox<>();
    @FXML private Label nameError = new Label();
    @FXML private Label phoneError = new Label();
    @FXML private Label postalError = new Label();
    @FXML private Label countryError = new Label();
    @FXML private Label divError = new Label();
    @FXML private Label emptyError = new Label();

    /**
     * Resets the error labels to empty.
     */
    private void resetErrors() {
        nameError.setText("");
        phoneError.setText("");
        postalError.setText("");
        countryError.setText("");
        divError.setText("");
        emptyError.setText("");
    }

    /**
     * Tests if a string contains only letters and spaces.
     * @param s the string to test
     * @return true if the string only contains letters and spaces, false otherwise
     */
    private boolean isLetterOrSpace(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetter(s.charAt(i)) && !Character.isSpaceChar(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Test if a string contains only numbers.
     * @param s the string to test
     * @return true if the string only contains numbers, false otherwise
     */
    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks for various input errors. Errors to check for are customer names not containing letters, phone numbers containing something other than a number,
     * the postal code being longer than 5 characters, and empty input fields.
     * @param pass is false if any errors were found, true otherwise
     * @return pass
     */
    private boolean errorCheck(boolean pass) {
        if(!isLetterOrSpace(custNameTxt.getText())) {
            nameError.setText("Please only enter letters and spaces.");
            pass = false;
        }
        if(!isNumber(phoneTxt.getText())) {
            phoneError.setText("Please only enter numbers.");
            pass = false;
        }
        if(countryBox.getSelectionModel().getSelectedItem() == null) {
            countryError.setText("Please select a country.");
            pass = false;
        }
        if(divBox.getSelectionModel().getSelectedItem() == null) {
            divError.setText("Please select a first-level division.");
            pass = false;
        }
        if(postalTxt.getLength() > 5) {
            postalError.setText("Postal codes should not be longer than 5 characters.");
            pass = false;
        }
        if(custNameTxt.getText().equals("") || addressTxt.getText().equals("") || postalTxt.getText().equals("") ||
                phoneTxt.getText().equals("")) {
            emptyError.setText("Please fill out all text fields.");
            pass = false;
        }
        return pass;
    }

    /**
     * Sets the title to 'Add Customer' and sets all input fields to empty.
     */
    public void makeAddCustForm() {
        custTitle.setText("Add Customer");
        custIdTxt.setText("Auto - Generated");
        custNameTxt.setText("");
        addressTxt.setText("");
        postalTxt.setText("");
        phoneTxt.setText("");
    }

    /**
     * Sets the title to 'Update Customer' and fills in all input fields with the selected customer.
     * @param selectedCust the customer to fill the input fields
     * @throws SQLException
     */
    public void makeUpdateCustForm(Customer selectedCust) throws SQLException {
        custTitle.setText("Update Customer");
        custIdTxt.setText(selectedCust.getCustId()+"");
        custNameTxt.setText(selectedCust.getCustName());
        addressTxt.setText(selectedCust.getAddress());
        postalTxt.setText(selectedCust.getPostal());
        phoneTxt.setText(selectedCust.getPhoneNum());

        int countryId = DivisionQuery.select(selectedCust.getDivName()).getCountryId();
        for(Country c : countries) {
            if(c.getCountryId() == countryId) {
                countryBox.setValue(c);
                setDivBox();
                divBox.setValue(DivisionQuery.select(selectedCust.getDivName()));
                return;
            }
        }
    }

    /**
     * When the submit button is clicked, checks for input errors before submitting to the database.
     * Depending on whether an 'add' or 'update' form was created, inserts or updates a row in the database.
     * Opens the home form when finished.
     * @param event the button click
     * @throws SQLException
     */
    @FXML protected void submitCust(ActionEvent event) throws SQLException {
        resetErrors();
        if(errorCheck(true)) {
            if (custTitle.getText().equals("Add Customer")) {
                addCust();
            }
            if (custTitle.getText().equals("Update Customer")) {
                updateCust();
            }
            try {
                openHomeForm(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
     * Adds a customer to the database.
     * @throws SQLException
     */
    private void addCust() throws SQLException {
        int divId = DivisionQuery.select(divBox.getValue().getDivName()).getDivId();
        String postalCode = postalTxt.getText().toUpperCase();
        CustomerQuery.insert(custNameTxt.getText(), addressTxt.getText(), postalCode, phoneTxt.getText(), divId);
    }

    /**
     * Updates a customer in the database.
     * @throws SQLException
     */
    private void updateCust() throws SQLException {
        int divId = DivisionQuery.select(divBox.getValue().getDivName()).getDivId();
        String postalCode = postalTxt.getText().toUpperCase();
        CustomerQuery.update(Integer.parseInt(custIdTxt.getText()), custNameTxt.getText(), addressTxt.getText(),
                postalCode, phoneTxt.getText(), divId);
    }

    /**
     * Sets the contents of first-level divisions box with respect to what country has been selected.
     * @throws SQLException
     */
    @FXML protected void setDivBox() throws SQLException {
        divBox.setDisable(false);
        divBox.setItems(DivisionQuery.select(countryBox.getValue().getCountryId()));
    }

    /**
     * Initializes error labels, combo boxes, and observable lists.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resetErrors();
        countryBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Country country) {
                return country.getCountryName();
            }

            @Override
            public Country fromString(String s) {
                return null;
            }
        });
        divBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(FirstLevelDivision firstLevelDivision) {
                return firstLevelDivision.getDivName();
            }

            @Override
            public FirstLevelDivision fromString(String s) {
                return null;
            }
        });
        try {
            countries = CountryQuery.select();
            countryBox.setItems(countries);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
