package group.scheduler.utilities;

import group.scheduler.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Queries for the users table.
 * @author Nick Pantuso
 */
public abstract class UserQuery {

    /**
     * Selects all users.
     * @return ObservableList of all users.
     * @throws SQLException
     */
    public static ObservableList<User> select() throws SQLException {
        JDBC.checkConnection();
        String sql = "SELECT * FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<User> uList = FXCollections.observableArrayList();
        while (rs.next()) {
            User u = new User(rs.getInt("User_ID"), rs.getString("User_Name"), rs.getString("Password"));
            uList.add(u);
        }
        return uList;
    }
}
