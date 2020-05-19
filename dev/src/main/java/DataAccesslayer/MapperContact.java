package DataAccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MapperContact {
    private static Connection conn;
    public void WriteContact(String storeId, int supId,String name, int number, int id){

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO Contact VALUES (?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1, supId);
            stmt.setString(2, name);
            stmt.setInt(3, number);
            stmt.setInt(4, id);
            stmt.setString(5, storeId);

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

    public void DeleteContact(String storeId, int supId,int id){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE * From Contact"+
                    "WHERE sid = '"+supId+ "' AND StoreId = '"+storeId+ "' AND ID = '"+id+"';";

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
