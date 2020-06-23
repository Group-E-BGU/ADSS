package DataAccesslayer;

import java.sql.*;

public class MapperItemRecord_Supplier
{
    private static Connection conn;

    public void WriteItemRecord_Supplier(String storeId, int PId, String MainCategory, String SubCategory, String SubSubCategory, String name,String manufacturer ){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:SuperLee.db");

            String sqlstmt = "INSERT INTO ItemRecord_Supplier VALUES (?,?,?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setString(1, MainCategory);
            stmt.setString(2, SubCategory);
            stmt.setString(3, SubSubCategory);
            stmt.setString(4,manufacturer);
            stmt.setInt(5,PId);
            stmt.setString(6, storeId);
            stmt.setString(7, name);

            stmt.executeUpdate();
        }
        catch (Exception e) {
           // System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
              //  System.out.println(ex.getMessage());
            }
        }
    }

    public void DeleteItemRecord_Supplier(String storeId, int PId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From ItemRecord_Supplier"+
                    "WHERE IRID = '"+PId+ "' AND StoreId = '"+storeId+"';";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);
            stmt.executeUpdate();

        } catch (Exception e) {
          //  System.out.println(e.getMessage());
        }
        finally{
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            //    System.out.println(ex.getMessage());
            }
        }
    }

    public int getProductId(String storeId,String product_name, String category, String subcategory, String sub_subcategory, String manufacturer) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ItemRecord_Supplier " +
                    "WHERE MainCategory = '"+category+"'" +
                    " AND SubCategory = '"+subcategory+"' "+
                    " AND SubSubCategory = '"+sub_subcategory+"' " +
                    " AND Manufacturer = '"+manufacturer+"' "+
                    " AND StoreId = '"+storeId+"' "+
                    " AND name = '"+product_name+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return rs.getInt(5);
            else
                return -1;
        } catch (Exception e) {
          //  System.out.println(e.getMessage());
        }
        finally{
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
             //   System.out.println(ex.getMessage());
            }
        }
        return -1;
    }

}
