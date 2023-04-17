package group.qam2.utilities;

import group.qam2.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Queries used for the appointments table.
 * @author Nick Pantuso
 */
public abstract class AppointmentQuery {

    /**
     * Inserts a new appointment into the table.
     * @param title
     * @param desc
     * @param loc
     * @param type
     * @param start
     * @param end
     * @param custId
     * @param userId
     * @param contactId
     * @throws SQLException
     */
    public static void insert(String title, String desc, String loc, String type, Timestamp start, Timestamp end, int custId, int userId, int contactId) throws SQLException {
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, loc);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, custId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.executeUpdate();
    }

    /**
     * Updates an existing appointment via it's appointment ID.
     * @param title
     * @param desc
     * @param loc
     * @param type
     * @param start
     * @param end
     * @param custId
     * @param userId
     * @param contactId
     * @param apptId
     * @throws SQLException
     */
    public static void update(String title, String desc, String loc, String type, Timestamp start, Timestamp end, int custId, int userId, int contactId, int apptId) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, loc);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, custId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, apptId);
        ps.executeUpdate();
    }

    /**
     * Deletes an appointment via its appointment ID.
     * @param apptId
     * @throws SQLException
     */
    public static void delete(int apptId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, apptId);
        ps.executeUpdate();
    }

    /**
     * Selects all appointments.
     * @return ObservableList of all appointments.
     * @throws SQLException
     */
    public static ObservableList<Appointment> select() throws SQLException {
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<Appointment> aList = FXCollections.observableArrayList();
        while(rs.next()) {
            Appointment a = new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                    rs.getTimestamp("Start"), rs.getTimestamp("End"), rs.getInt("Customer_ID"),
                    rs.getInt("User_ID"), ContactQuery.selectName(rs.getInt("Contact_ID")));
            aList.add(a);
        }
        return aList;
    }

}
