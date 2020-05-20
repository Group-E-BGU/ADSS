package DataAccesslayer;

import BusinessLayer.Wrotequantities;

import java.sql.*;
import java.util.HashMap;
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
                    " WHERE SID = '"+suplaier_ID+ "' AND StoreId = '"+storeId+"';";

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

    public Map<Integer, Integer> GetItemsID_Amount(int suplaier_ID,String storeId){
        Map<Integer, Integer> list=new HashMap<Integer, Integer>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM quantityWrote " +
                    "WHERE SID = '"+suplaier_ID+ "' AND StoreId = '"+storeId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                list.put(rs.getInt(1),rs.getInt(2));
            return list;
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
        return list;
    }

    public Map<Integer, Double> GetItemsID_Assumption(int suplaier_ID,String storeId){
        Map<Integer, Double> list=new HashMap<Integer, Double>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM quantityWrote " +
                    "WHERE SID = '"+suplaier_ID+ "' AND StoreId = '"+storeId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                list.put(rs.getInt(1),rs.getDouble(3));
            return list;
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
        return list;
    }

    public Wrotequantities GetWrotequantities (int suplaier_ID,String storeId){
        Map<Integer, Double> I_As= GetItemsID_Assumption(suplaier_ID,storeId);
        Map<Integer, Integer> I_Am=GetItemsID_Amount(suplaier_ID,storeId);
        Map<Integer, Double> list=new HashMap<Integer, Double>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM quantityWrote " +
                    "WHERE SID = '"+suplaier_ID+ "' AND StoreId = '"+storeId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if (rs.next())
               return new Wrotequantities(rs.getInt(4),I_Am,I_As) ;
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