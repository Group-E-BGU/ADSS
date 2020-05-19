package DataAccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MapperProductToSupplier {

    private static Connection conn;

    public void WriteProductToSupplier(String storeId, int supId, int P_store_Id, int P_sup_Id, double price, String name){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO ProductSupplier VALUES (?,?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1, P_store_Id);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.setInt(4, P_sup_Id);
            stmt.setString(5, storeId);
            stmt.setInt(6, supId);

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

    public void DeleteProductFromSupplier(String storeId, int supId, int P_sup_Id ){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From ProductSuppliet"+
                    "WHERE SupId = '"+supId+ "' AND StoreId = '"+storeId+ "' AND psid = '"+P_sup_Id+"';";

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
