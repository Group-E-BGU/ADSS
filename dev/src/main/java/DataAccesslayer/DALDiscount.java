package DataAccesslayer;

import BusinessLayer.Category;
import BusinessLayer.CategoryDiscount;
import BusinessLayer.ItemDiscount;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DALDiscount {

    private Connection conn;

    /*"CREATE TABLE IF NOT EXISTS CategoryDiscount(" +
                    "id int NOT NULL," +
                    "startDate date," +
                    "endDate date," +
                    "percentage int," +
                    "CN varchar," +
                    "StoreId varchar," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email), " +
                    "FOREIGN KEY(CN) REFERENCES Category(name));";*/

    public void InsertItemDiscount() {
        //if already exists edit it
    }

    public void DeleteItemDiscount() {

    }

    public ItemDiscount getItemDiscount() {
        return null;
    }


    public void InsertCategoryDiscount() {
        //if already exists edit it
    }

    public void DeleteCategoryDiscount() {

    }

    public List<CategoryDiscount> getCategoryDiscounts(Category c,String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM CategoryDiscount "+
                    "WHERE CN = '"+c.getName()+"' AND StoreId = '"+storeId+"' ;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            List<CategoryDiscount> l = new LinkedList<>();
            while (rs.next())
                l.add(new CategoryDiscount(rs.getInt(1),c,rs.getDate(2),rs.getDate(3),rs.getInt(4)));
            return l;
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

    public int getMaxId() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT MAX(id) " +
                    "FROM CategoryDiscount ;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            int id = 0;
            if (rs.next())
                id = rs.getInt(1);

            sqlstmt = "SELECT MAX(id) " +
                    "FROM ItemDiscount ;";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlstmt);
            if (rs.next())
                id = rs.getInt(1);
            return id;
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
        return 0;

    }
}
