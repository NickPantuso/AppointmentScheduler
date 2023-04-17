package group.qam2.utilities;

import group.qam2.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Queries used for the countries table.
 * @author Nick Pantuso
 */
public abstract class CountryQuery {

    /**
     * Selects all countries.
     * @return ObservableList of all countries.
     * @throws SQLException
     */
    public static ObservableList<Country> select() throws SQLException {
        String sql = "SELECT * FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<Country> cList = FXCollections.observableArrayList();
        while(rs.next()) {
            Country c = new Country(rs.getInt("Country_ID"), rs.getString("Country"));
            cList.add(c);
        }
        return cList;
    }

}
