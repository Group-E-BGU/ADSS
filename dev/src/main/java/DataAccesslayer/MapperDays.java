package DataAccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MapperDays {
    private static Connection conn;
    public void WriteDayToContract(String storeId, int supplierId, int day){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO Days VALUES (?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1, supplierId);
            stmt.setInt(2,day);
            stmt.setString(3, storeId);

            stmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void DeleteDayFromContract(String storeId, int supplierId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From quantityWrote"+
                    "WHERE sid = '"+supplierId+ "' AND StoreId = '"+storeId+"';";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally{
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
