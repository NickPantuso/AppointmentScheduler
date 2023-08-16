package group.scheduler.utilities;

import group.scheduler.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Queries used for the contacts table.
 * @author Nick Pantuso
 */
public abstract class ContactQuery {

    /**
     * Selects all contacts.
     * @return ObservableList of all contacts.
     * @throws SQLException
     */
    public static ObservableList<Contact> select() throws SQLException {
        JDBC.checkConnection();
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<Contact> cList = FXCollections.observableArrayList();
        while(rs.next()) {
            Contact c = new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"),
                    rs.getString("Email"));
            cList.add(c);
        }
        return cList;
    }

    /**
     * Finds the name of a contact via their ID.
     * @param contactId
     * @return name
     * @throws SQLException
     */
    public static String selectName(int contactId) throws SQLException {
        JDBC.checkConnection();
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactId);
        ResultSet rs = ps.executeQuery();
        String name = "";
        while(rs.next()) {
            name = rs.getString("Contact_Name");
        }
        return name;
    }
}
