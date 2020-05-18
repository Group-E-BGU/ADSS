package DataAccesslayer;

import BusinessLayer.Category;
import BusinessLayer.ItemRecord;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DALCategory {

    private Connection conn;

    /*"CREATE TABLE IF NOT EXISTS Category(" +
                    "name varchar NOT NULL," +
                    "CategoryRole int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(name)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";*/

    public List<Category> getAllCategories(String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Category " +
                    "WHERE StoreId = '"+storeId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            List<Category> l = new LinkedList<>();
            while (rs.next()) {
                int role = rs.getInt(2);
                if(role == 1)
                    l.add(new Category(Category.CategoryRole.MainCategory, rs.getString(1)));
                else if (role == 2)
                    l.add(new Category(Category.CategoryRole.SubCategory, rs.getString(1)));
                else if (role == 3)
                    l.add(new Category(Category.CategoryRole.SubSubCategory, rs.getString(1)));
            }
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

    public Category getCategory(String name, String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Category " +
                    "WHERE name = '"+name+ "' AND StoreId = '"+storeId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next()) {
                int role = rs.getInt(1);
                if (role == 1)
                    return (new Category(Category.CategoryRole.MainCategory, rs.getString(2)));
                else if (role == 2)
                    return (new Category(Category.CategoryRole.SubCategory, rs.getString(2)));
                else if (role == 3)
                    return (new Category(Category.CategoryRole.SubSubCategory, rs.getString(2)));
            }
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


    public List<Category> getCategoryOfItem(int RID,String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ItemRecord_Supplier " +
                    "WHERE StoreId = '"+storeId+"' AND IRID = "+RID+";";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            List<Category> l = new LinkedList<>();
            if (rs.next()) {
                    l.add(new Category(Category.CategoryRole.MainCategory, rs.getString(1)));
                    l.add(new Category(Category.CategoryRole.SubCategory, rs.getString(2)));
                    l.add(new Category(Category.CategoryRole.SubSubCategory, rs.getString(3)));
            }
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

    public void InsertCategory(String name,int role,String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO Category VALUES (?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setString(1,name);
            stmt.setInt(2,role);
            stmt.setString(3,storeId);

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
