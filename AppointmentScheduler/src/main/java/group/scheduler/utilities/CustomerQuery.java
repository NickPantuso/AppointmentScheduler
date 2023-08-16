package group.scheduler.utilities;

import group.scheduler.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Queries used for the customers table.
 * @author Nick Pantuso
 */
public abstract class CustomerQuery {

    /**
     * Inserts a new customer into the table.
     * @param custName
     * @param address
     * @param postal
     * @param phoneNum
     * @param divId
     * @throws SQLException
     */
    public static void insert(String custName, String address, String postal, String phoneNum, int divId) throws SQLException {
        JDBC.checkConnection();
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, custName);
        ps.setString(2, address);
        ps.setString(3, postal);
        ps.setString(4, phoneNum);
        ps.setInt(5, divId);
        ps.executeUpdate();
    }

    /**
     * Updates an existing customer via their customer ID.
     * @param custId
     * @param custName
     * @param address
     * @param postal
     * @param phoneNum
     * @param divId
     * @throws SQLException
     */
    public static void update(int custId, String custName, String address, String postal, String phoneNum, int divId) throws SQLException {
        JDBC.checkConnection();
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, custName);
        ps.setString(2, address);
        ps.setString(3, postal);
        ps.setString(4, phoneNum);
        ps.setInt(5, divId);
        ps.setInt(6, custId);
        ps.executeUpdate();
    }

    /**
     * Deletes an existing customer via their customer ID.
     * @param custId
     * @throws SQLException
     */
    public static void delete(int custId) throws SQLException {
        JDBC.checkConnection();
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, custId);
        ps.executeUpdate();
    }

    /**
     * Selects all customers.
     * @return ObservableList of all customers.
     * @throws SQLException
     */
    public static ObservableList<Customer> select() throws SQLException {
        JDBC.checkConnection();
        String sql = "SELECT * FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<Customer> cList = FXCollections.observableArrayList();
        while(rs.next()) {
            Customer c = new Customer(rs.getInt("Customer_ID"), rs.getString("Customer_Name"),
                    rs.getString("Address"), rs.getString("Postal_Code"), rs.getString("Phone"),
                    DivisionQuery.selectName(rs.getInt("Division_ID")));
            cList.add(c);
        }
        return cList;
    }

}
