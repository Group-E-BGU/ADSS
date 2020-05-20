package DataAccesslayer;

import BusinessLayer.Contract;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MapperContract {

    private static Connection conn;

    public void WriteContract(int suplaier_ID, boolean fixeDays, boolean leading,String storeId,
                              LinkedList<Integer> Days,Map<Integer, Integer> ItemsID_ItemsIDSupplier,
                              Map<Integer, String> ProductIDVendor_Name,Map<Integer, Double> productIDVendor_Price){

        for (Map.Entry<Integer,Integer> IDS_IDV: ItemsID_ItemsIDSupplier.entrySet()
        ) {
           int hh=IDS_IDV.getKey();
           int ee=IDS_IDV.getValue();
           Double pp=productIDVendor_Price.get(IDS_IDV.getValue());
           String oo=ProductIDVendor_Name.get(IDS_IDV.getValue());
           WriteProductToSupplier(storeId,suplaier_ID,hh,ee,pp,oo);
        }

        for (int day:Days
             ) {
            WriteDayToContract(storeId,suplaier_ID,day);
        }


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
            //System.out.println(e.getMessage());
        }
        finally{
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                //System.out.println(ex.getMessage());
            }
        }
    }

    public void DeleteContract(int suplaier_ID,String StoreId){
        DeleteDaysFromContract(StoreId,suplaier_ID);
        DeleteProduct(StoreId,suplaier_ID);
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From Contract"+
                    " WHERE SupplierId = '"+suplaier_ID+ "' AND StoreId = '"+StoreId+"';";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);
            stmt.executeUpdate();

        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        finally{
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
               // System.out.println(ex.getMessage());
            }
        }
    }

    public Contract getContract(int suplaier_ID,String StoreId) {
      LinkedList<Integer> Days=GetDays(StoreId,suplaier_ID);
      Map<Integer, Integer> ItemsID_ItemsIDSupplier=GetItemsID_ItemsIDSupplier(StoreId,suplaier_ID);
      Map<Integer, String> ProductIDVendor_Name=GetProductIDVendor_Name(StoreId,suplaier_ID);
      Map<Integer, Double> productIDVendor_Price=GetproductIDVendor_Price(StoreId,suplaier_ID);
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Contract " +
                    "WHERE SupplierId = '"+suplaier_ID+ "' AND StoreId = '"+StoreId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);

            if(rs.next())
                return new Contract(rs.getInt(1),rs.getBoolean(2),Days,rs.getBoolean(3),ProductIDVendor_Name,ItemsID_ItemsIDSupplier,productIDVendor_Price);
            else
                return null;
        } catch (Exception e) {
          //  System.out.println(e.getMessage());
        }
        finally{
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
              //  System.out.println(ex.getMessage());
            }
        }
        return null;
    }

    public void UpdateContract(int suplaier_ID, boolean fixeDays, boolean leading,String storeId,LinkedList<Integer> Days){
        DeleteDaysFromContract(storeId,suplaier_ID);
        for (int day:Days
             ) {
            WriteDayToContract(storeId,suplaier_ID,day);
        }
    try {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

        String sqlstmt = "UPDATE Contract SET " +
                " fixDay = '"+fixeDays+"'," +
                " leading = '"+leading+"'" +
                "WHERE SupplierId = '"+suplaier_ID+ "' AND StoreId = '"+storeId+"';";

        Statement stmt = conn.createStatement();
        stmt.execute(sqlstmt);

    } catch (Exception e) {
       // System.out.println(e.getMessage());
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
}

    public void WriteDayToContract(String storeId, int supplierId, int day){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO Days VALUES (?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1, supplierId);
            stmt.setInt(2,day);
            stmt.setString(3, storeId);

            stmt.executeUpdate();
        }
        catch (Exception e) {
         //   System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            //    System.out.println(ex.getMessage());
            }
        }
    }

    public void DeleteDaysFromContract(String storeId, int supplierId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From Days"+
                    " WHERE sid = '"+supplierId+ "' AND StoreId = '"+storeId+"';";

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

    public LinkedList<Integer> GetDays(String storeId, int SuppId){
        LinkedList<Integer> list=new LinkedList<Integer>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Days " +
                    "WHERE sid = '"+SuppId+ "' AND StoreId = '"+storeId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                list.add(rs.getInt(2));
            return list;
        } catch (Exception e) {
          //  System.out.println(e.getMessage());
        }
        finally{
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
              //  System.out.println(ex.getMessage());
            }
        }
        return list;
    }

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
        //    System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
           //     System.out.println(ex.getMessage());
            }
        }
    }

    private void DeleteProduct(String storeId, int suplaier_id) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From ProductSupplier"+
                    " WHERE SupId = '"+suplaier_id+ "' AND StoreId = '"+storeId+"';";

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
              //  System.out.println(ex.getMessage());
            }
        }
    }

    public void DeleteProductFromSupplier(String storeId, int supId, int P_sup_Id ){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From ProductSupplier"+
                    " WHERE SupId = '"+supId+ "' AND StoreId = '"+storeId+ "' AND psid = '"+P_sup_Id+"';";

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
             //   System.out.println(ex.getMessage());
            }
        }
    }

    public Map<Integer, Integer> GetItemsID_ItemsIDSupplier(String StoreId, int SupId){
        Map<Integer, Integer> list=new HashMap<Integer, Integer>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ProductSupplier " +
                    "WHERE SupId = '"+SupId+ "' AND StoreId = '"+StoreId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                list.put(rs.getInt(1),rs.getInt(4));
            return list;
        } catch (Exception e) {
         //   System.out.println(e.getMessage());
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
        return list;
    }

    public Map<Integer, String> GetProductIDVendor_Name(String StoreId, int SupId){
        Map<Integer, String> list=new HashMap<Integer, String>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ProductSupplier " +
                    "WHERE SupId = '"+SupId+ "' AND StoreId = '"+StoreId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                list.put(rs.getInt(4),rs.getString(2));
            return list;
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
        return list;
    }

    public Map<Integer, Double> GetproductIDVendor_Price(String StoreId, int SupId){
        Map<Integer, Double> list=new HashMap<Integer, Double>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ProductSupplier " +
                    "WHERE SupId = '"+SupId+ "' AND StoreId = '"+StoreId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                list.put(rs.getInt(4),rs.getDouble(3));
            return list;
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
        return list;
    }

}