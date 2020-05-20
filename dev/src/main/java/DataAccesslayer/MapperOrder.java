package DataAccesslayer;

import BusinessLayer.ItemRecord;
import BusinessLayer.Order;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class MapperOrder {

    private static Connection conn;

    public void WriteOrder(String storeId, int id_suplaier, int IdOrder, boolean auto, LinkedList<Integer> days, java.sql.Date d, Date dd, Double TotalPrice, String status,Map<Integer,Integer> ProductIDSupplier_ProductID_Store, Map<Integer, Integer> ProductIDSupplier_numberOfItems) {
        WriteProductOrder(storeId,IdOrder,ProductIDSupplier_ProductID_Store,ProductIDSupplier_numberOfItems);
        WriteDays(storeId,IdOrder,days);
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

                String sqlstmt = "INSERT INTO Orders  VALUES (?,?,?,?,?,?,?,?)";

                PreparedStatement stmt = conn.prepareStatement(sqlstmt);

                stmt.setInt(1, IdOrder);
                stmt.setInt(2, id_suplaier);
                stmt.setBoolean(3, auto);
                stmt.setDate(4, d);
                stmt.setDate(5, dd);
                stmt.setDouble(6, TotalPrice);
                stmt.setString(7, status);
                stmt.setString(8, storeId);

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

    private void WriteDays(String storeId,int IdOrder,LinkedList<Integer> days) {
        for (int day : days
        ) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

                String sqlstmt = "INSERT INTO DayForOrders  VALUES (?,?,?)";

                PreparedStatement stmt = conn.prepareStatement(sqlstmt);

                stmt.setInt(1,IdOrder);
                stmt.setInt(2, day);
                stmt.setString(3, storeId);
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

    private LinkedList<Integer> GetDays(String storeId, int Id){
        LinkedList<Integer> Days= new LinkedList<Integer>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM DayForOrders " +
                    "WHERE OID = '"+Id+ "' AND StoreId = '"+storeId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                Days.add(rs.getInt(2));
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
        return Days;
    }

    public void DeleteOrder(String storeId, int OrderId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From Orders" +
                    " WHERE id = '" + OrderId + "' AND StoreId = '" + storeId + "';";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);
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

    public void DeleteOrder_Supplier(String storeId, int supId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Orders " +
                    "WHERE SID = '" + supId + "' AND StoreId = '" + storeId + "';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while (rs.next()) {
                DeleteAllProductOrder(storeId,rs.getInt(1));
                DeleteOrder(storeId,rs.getInt(1));
            }
        }
        catch(Exception e){
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

    public Order GetOrder(int OrderdId, String StoreId){
        Map<Integer, Integer> ItemsID_ItemsIDVendor=GetItemsID_ItemsIDVendor(StoreId,OrderdId);
        Map<Integer, Integer> ItemsID_NumberOfItems=GetItemsID_NumberOfItems(StoreId,OrderdId);
        LinkedList<Integer> Days=GetDays(StoreId,OrderdId);
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Orders " +
                    "WHERE id = '"+OrderdId+ "' AND StoreId = '"+StoreId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return new Order(rs.getInt(2),rs.getInt(1),rs.getBoolean(3),Days,ItemsID_ItemsIDVendor,ItemsID_NumberOfItems,rs.getDouble(7));
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

    public void UpdateOrder(String storeId, int IdOrder, LinkedList<Integer> days, Map<Integer,Integer> ProductIDSupplier_ProductID_Store, Map<Integer, Integer> ProductIDSupplier_numberOfItems) {
        WriteProductOrder(storeId,IdOrder,ProductIDSupplier_ProductID_Store,ProductIDSupplier_numberOfItems);
        for (int day : days
        ) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

                String sqlstmt = "DELETE From DayForOrders" +
                        "WHERE OID = '"+IdOrder+ "' AND StoreId = '"+storeId+ "' AND day = '"+day+"';";

                PreparedStatement stmt = conn.prepareStatement(sqlstmt);
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
    WriteDays(storeId,IdOrder,days);

    }

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

    public void DeleteAllProductOrder(String storeId, int orderId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From ProductOrder"+
                    " WHERE OID = '"+orderId+ "' AND StoreId = '"+storeId+"';";

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

    public Map<Integer, Integer> GetItemsID_ItemsIDVendor(String StoreId,int OId){
        Map<Integer, Integer> list=new HashMap<Integer, Integer>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ProductOrder " +
                    "WHERE OID = '"+OId+ "' AND StoreId = '"+StoreId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                list.put(rs.getInt(2),rs.getInt(3));
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

    public  Map<Integer, Integer> GetItemsID_NumberOfItems(String StoreId,int OId){
        Map<Integer, Integer> list=new HashMap<Integer, Integer>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ProductOrder " +
                    "WHERE OID = '"+OId+ "' AND StoreId = '"+StoreId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                list.put(rs.getInt(2),rs.getInt(4));
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

    public LinkedList<Integer> GetOrdersId(int today, String email_id) {
      LinkedList<Integer> oId=new LinkedList<Integer>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM DayForOrders " +
                    "WHERE day = '"+today+ "' AND StoreId = '"+email_id+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while (rs.next()) {
               oId.add(rs.getInt(1));
            }
                return oId;
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
        return oId;
    }
}



