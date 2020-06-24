package DataAccesslayer;

import BusinessLayer.Address;
import sun.awt.image.ImageWatched;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class WarningsDAO {
    public void save(String warning, String userType)
    {
        String sql = "INSERT INTO Warnings(warning, workerType) VALUES(?,?)";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, warning);
            pstmt.setString(2, userType);
            pstmt.executeUpdate();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

    }

    public List<String> get(String userType) {

        List<String> warnings = new LinkedList<>();
        String sql = "SELECT * FROM Warnings WHERE workerType = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, userType);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next())
                warnings.add(rs.getString("warning"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return warnings;
    }
}
