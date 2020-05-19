package DataAccesslayer;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class MapperOrder {
    private static Connection conn;

    public void WriteOrder(String storeId, int id_suplaier, int IdOrder, boolean auto, LinkedList<Integer> days, java.sql.Date d, Date dd, Double TotalPrice, String status) {

        for (int day : days
        ) {
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

                String sqlstmt = "INSERT INTO Orders  VALUES (?,?,?,?,?,?,?,?";

                PreparedStatement stmt = conn.prepareStatement(sqlstmt);

                stmt.setInt(1, IdOrder);
                stmt.setInt(2, id_suplaier);
                stmt.setBoolean(3, auto);
                stmt.setInt(4, day);
                stmt.setDate(5, d);
                stmt.setDate(6, dd);
                stmt.setDouble(7, TotalPrice);
                stmt.setString(8, status);
                stmt.setString(9, storeId);

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

    public void DeleteOrder(String storeId, int OrderId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From Orders" +
                    "WHERE id = '" + OrderId + "' AND StoreId = '" + storeId + "';";

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
}
   /* public int ID_Inventation;
    public int ID_Vendor;
    public boolean Auto;
    public LinkedList<Integer> Day;
    public LocalDate OrderDate;
    public LocalDate ArrivalTime;
    public Map<Integer, Integer> ItemsID_ItemsIDVendor;
    public Map<Integer, Integer> ItemsID_NumberOfItems;
    public double TotalPrice;
    public String Status;

    public MapperOrder(int ID_Vendor, int Id, boolean auto, LinkedList<Integer> day, LocalDate orderDate, LocalDate arrivalTime, Map<Integer, Integer> itemsID_ItemsIDVendor, Map<Integer, Integer> itemsID_NumberOfItems, double totalPrice, String status){//List<DALContact> vendorContacts, List<DALContact> leadersContacts) {
        this.ID_Vendor = ID_Vendor;
        this.ID_Inventation=Id;
        Auto=auto;
        Day=day;
        OrderDate = orderDate;
        ArrivalTime = arrivalTime;
        ItemsID_ItemsIDVendor = itemsID_ItemsIDVendor;
        ItemsID_NumberOfItems = itemsID_NumberOfItems;
        TotalPrice = totalPrice;
        Status = status;
        //VendorContacts = vendorContacts;
       // LeadersContacts = leadersContacts;
    }*/

