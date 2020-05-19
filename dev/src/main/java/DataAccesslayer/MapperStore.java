package DataAccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MapperStore {

    private static Connection conn;
    public void WriteStore(String email, int itemeId, int numOfProduct, int NumOfOrder ){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO  Store VALUES (?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setString(1,email);
            stmt.setInt(2,itemeId);
            stmt.setInt(3,numOfProduct);
            stmt.setInt(4,NumOfOrder);
            stmt.setInt(5,0);

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

    public void DeleteStore(String email){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");
            String sqlstmt = "DELETE From Store"+
                    "WHERE email = '"+email+"';";

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
