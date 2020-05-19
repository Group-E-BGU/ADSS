package DataAccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class MapperProductOrder {

    private static Connection conn;

    public void WriteProductOrder(String storeId, int IdOrder, Map<Integer,Integer> ProductIDSupplier_ProductID_Store, Map<Integer, Integer> ProductIDSupplier_numberOfItems){

        for (Map.Entry<Integer,Integer> PId_num: ProductIDSupplier_numberOfItems.entrySet()
        ) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

                String sqlstmt = "INSERT INTO ProductOrder  VALUES (?,?,?,?,?)";

                PreparedStatement stmt = conn.prepareStatement(sqlstmt);

                stmt.setInt(1, IdOrder);
                stmt.setInt(2, ProductIDSupplier_ProductID_Store.get(PId_num.getKey()));
                stmt.setInt(3, PId_num.getKey());
                stmt.setInt(4, PId_num.getValue());
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
    }

    public void DeleteProductOrder(String storeId, int orderId, int ID_P_Sup){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From ProductOrder"+
                    "WHERE OID = '"+orderId+ "' AND StoreId = '"+storeId+ "' AND PSupplierOD = '"+ID_P_Sup+"';";

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
