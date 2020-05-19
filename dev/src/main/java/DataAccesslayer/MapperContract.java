package DataAccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MapperContract {
    private static Connection conn;


    public void WriteContract(int suplaier_ID, boolean fixeDays, boolean leading,String storeId){

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO Contract VALUES (?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1,suplaier_ID);
            stmt.setBoolean(2,fixeDays); //todo check if its work
            stmt.setBoolean(3,leading);
            stmt.setString(4,storeId);

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

    public void DeleteContract(int suplaier_ID,String StoreId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From Contract"+
                    "WHERE SupplierId = '"+suplaier_ID+ "' AND StoreId = '"+StoreId+"';";

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

 /*   public int Suplaier_ID;
    public boolean FixeDays;
    public List<Integer> Dayes;
    public boolean leading;
    public Map<Integer, Integer> ItemsID_ItemsIDSupplier;
    public Map<Integer, String> ProductIDSupplier_Name;
    public Map<Integer, Double> productIDSupplier_Price;

    public MapperContract(int suplaier_ID, boolean fixeDays, List<Integer> dayes, boolean leading,
                          Map<Integer, String> productIDSupplier_name, Map<Integer, Integer> ItemsID_ItemsIDsupplier,
                          Map<Integer, Double> producttemsIDSupplier_price) {
        Suplaier_ID = suplaier_ID;
        FixeDays = fixeDays;
        Dayes=dayes;
        this.leading = leading;
        ProductIDSupplier_Name =productIDSupplier_name;
        ItemsID_ItemsIDSupplier=ItemsID_ItemsIDsupplier;
        productIDSupplier_Price =producttemsIDSupplier_price;


    }*/