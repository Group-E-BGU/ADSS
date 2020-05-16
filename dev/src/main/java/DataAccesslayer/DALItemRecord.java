package DataAccesslayer;

import BusinessLayer.ItemRecord;

import java.sql.*;

public class DALItemRecord {

    //this class also manages Items
    Connection conn = null;

    public void InsertItemRecord(String name,int id, int minAmount, int storageAmount, int shelfAmount, int totalAmount, int shelfNumber, String manufacture, String storeId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO ItemRecord VALUES (?,?,?,?,?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1,id);
            stmt.setString(2,name);
            stmt.setInt(3,minAmount);
            stmt.setInt(4,storageAmount);
            stmt.setInt(5,shelfAmount);
            stmt.setInt(6,totalAmount);
            stmt.setInt(7,shelfNumber);
            stmt.setString(8,manufacture);
            stmt.setString(9,storeId);

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

    public void DeleteItem() {

    }

    public ItemRecord getItemRecord(String name, String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT *" +
                    "FROM ItemRecord" +
                    "WHERE name = "+name+ " AND StoreId = "+storeId;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);

            return new ItemRecord(rs.getString(2),rs.getInt(1),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getString(8));

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
        return null;
    }
}
