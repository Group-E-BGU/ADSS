package DataAccesslayer;

import BusinessLayer.Price;

import java.sql.*;

public class MapperPrice {

    private static Connection conn = null;

    /*"CREATE TABLE IF NOT EXISTS Price(" +
                    "id int NOT NULL," +
                    "RetailPrice int," +
                    "StorePrice int," +
                    "IRID int," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(IRID) REFERENCES ItemRecord(id));";


     */

    public void InsertPrice(int id,int IRID, int storePrice, int retailPrice) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO price VALUES (?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1, id);
            stmt.setInt(2, retailPrice);
            stmt.setInt(3, storePrice);
            stmt.setInt(4, IRID);

            stmt.executeUpdate();

        } catch (Exception e) {
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

    public int getMaxPriceId() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT MAX(id) " +
                    "FROM Price ;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return rs.getInt(1);
            else
                return 0;
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
        return 0;
    }

    public static Price getCurrId(int IRID) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT MAX(id), RetailPrice, StorePrice " +
                    "FROM Price " +
                    "WHERE IRID = "+IRID+" ;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return new Price(rs.getInt(1),rs.getInt(2),rs.getInt(3));
            else
                return null;
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