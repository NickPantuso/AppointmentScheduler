package group.qam2.utilities;

import group.qam2.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Queries used for the first-level-divisions table.
 * @author Nick Pantuso
 */
public abstract class DivisionQuery {

    /**
     * Finds the name of a division based on its ID.
     * @param divId
     * @return name of division
     * @throws SQLException
     */
    public static String selectName(int divId) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divId);
        ResultSet rs = ps.executeQuery();
        String divName = "";
        while(rs.next()) {
            divName = rs.getString("Division");
        }
        return divName;
    }

    /**
     * Finds a first-level-division based on its name.
     * @param divName
     * @return a first-level-division object
     * @throws SQLException
     */
    public static FirstLevelDivision select(String divName) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, divName);
        ResultSet rs = ps.executeQuery();
        FirstLevelDivision d = null;
        while(rs.next()) {
            d = new FirstLevelDivision(rs.getInt("Division_ID"), rs.getString("Division"),
                    rs.getInt("Country_ID"));
        }
        return d;
    }

    /**
     * Selects all first-level-divisions with a matching countryId.
     * @param countryId
     * @return ObservableList of first-level-divisions
     * @throws SQLException
     */
    public static ObservableList<FirstLevelDivision> select(int countryId) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryId);
        ResultSet rs = ps.executeQuery();
        ObservableList<FirstLevelDivision> dList = FXCollections.observableArrayList();
        while(rs.next()) {
            FirstLevelDivision d = new FirstLevelDivision(rs.getInt("Division_ID"), rs.getString("Division"),
                    rs.getInt("Country_ID"));
            dList.add(d);
        }
        return dList;
    }
}
