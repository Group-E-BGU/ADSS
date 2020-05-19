package DataAccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class MapperWrotequantities {
    private static Connection conn;
    public void WriteWrote(String storeId ,int suplaier_ID, java.util.Map<Integer, Integer> itemsID_amount, Map<Integer,Double> itemsID_assumption){
        for (Map.Entry<Integer,Integer> ID_Amount: itemsID_amount.entrySet()
        ) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

                String sqlstmt = "INSERT INTO quantityWrote VALUES (?,?,?,?,?)";

                PreparedStatement stmt = conn.prepareStatement(sqlstmt);

                stmt.setInt(1, ID_Amount.getKey());
                stmt.setInt(2, ID_Amount.getValue());
                stmt.setDouble(3,itemsID_assumption.get(ID_Amount.getKey()) );
                stmt.setInt(4,suplaier_ID );
                stmt.setString(5,storeId );

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
    }

    public void DeleteWrote(int suplaier_ID,String storeId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From quantityWrote"+
                    "WHERE SupplierId = '"+suplaier_ID+ "' AND StoreId = '"+storeId+"';";

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
/*
    public int Suplaier_ID;
    public Map<Integer, Integer> ItemsID_Amount;
    public Map<Integer, Double> ItemsID_Assumption;

    public MapperWrotequantities(int suplaier_ID,  Map<Integer, Integer> itemsID_Amount, Map<Integer, Double> itemsID_Assumption) {
        Suplaier_ID = suplaier_ID;
        ItemsID_Amount = itemsID_Amount;
        ItemsID_Assumption = itemsID_Assumption;
    }*/