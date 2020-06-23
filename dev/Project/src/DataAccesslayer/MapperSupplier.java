package DataAccesslayer;


import BusinessLayer.Contract;
import BusinessLayer.ItemRecord;
import BusinessLayer.Supplier;
import BusinessLayer.Wrotequantities;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapperSupplier {

    private static Connection conn;
    private MapperContract MapContract;
    private MapperWrotequantities MapWorte;

    public MapperSupplier() {
        MapContract = new MapperContract();
        MapWorte = new MapperWrotequantities();
    }

    public void WriteSupplier(String Name, int ID, String address, String bank, String branch, int BankNumber,
                              String Payments, String storeId,Map<Integer, String> ContactsID_Name,
                                Map<Integer, Integer> ContactsID_number) {
        for (Map.Entry<Integer,String> E:ContactsID_Name.entrySet()
             ) {
            WriteContact(storeId,ID,E.getValue(),ContactsID_number.get(E.getKey()),E.getKey());
        }
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:SuperLee.db");

            String sqlstmt = "INSERT INTO Supplier VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1, ID);
            stmt.setString(2, Name);
            stmt.setString(3, address);
            stmt.setString(4, bank);
            stmt.setString(5, branch);
            stmt.setInt(6, BankNumber);
            stmt.setString(7, Payments);
            stmt.setString(8, storeId);

            stmt.executeUpdate();

        } catch (Exception e) {
         //   System.out.println(e.getMessage());
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

    public void UpdateSupplier(String name, int id, String address, String bank, String branch, int bankNumber, String payments, String email_id, Map<Integer, String> contacts_id_name, Map<Integer, Integer> contacts_number) {

        for (Map.Entry<Integer,String> E:contacts_id_name.entrySet()
        ) {
            DeleteContact(email_id,id,E.getKey());
        }
        for (Map.Entry<Integer,String> E:contacts_id_name.entrySet()
        ) {
            WriteContact(email_id,id,E.getValue(),contacts_number.get(E.getKey()),E.getKey());
        }
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "UPDATE Supplier SET " +
                    " name = '"+name+"'," +
                    " address = '"+address+"'," +
                    " bank = '"+bank+"'," +
                    " branch = '"+branch+"'," +
                    " bankNumber = '"+bankNumber+"'," +
                    " payment = '"+payments+"'" +
                    "WHERE id = '"+id+ "' AND StoreId = '"+email_id+"';";

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

    public void DeleteSupplier(int ID, String storeId) {
        DeleteContacts(ID,storeId);
        DeleteContract(ID,storeId);
        DeleteWrote(ID,storeId);
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From Supplier" +
                    " WHERE id = '" + ID + "' AND StoreId = '" + storeId + "';";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);
            stmt.executeUpdate();

        } catch (Exception e) {
           // System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
             //   System.out.println(ex.getMessage());
            }
        }
    }

    private void DeleteContacts(int id, String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From Contact" +
                    " WHERE sid = '" + id + "' AND StoreId = '" + storeId + "';";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);
            stmt.executeUpdate();

        } catch (Exception e) {
         //   System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
             //   System.out.println(ex.getMessage());
            }
        }
    }

    public Supplier GetSupplier(int ID, String storeId) {
        Contract c=getContract(ID,storeId);
        Wrotequantities w=GetWrotequantities(ID, storeId);
        Map<Integer, String> ContactsID_Name = GetContactsID_Name(storeId,ID);
        Map<Integer, Integer> ContactsID_number = GetContactsID_number(storeId,ID);
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Supplier " +
                    "WHERE id = '" + ID + "' AND StoreId = '" + storeId + "';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if (rs.next()) {
                Supplier s = new Supplier(rs.getString(2), rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getString(7), ContactsID_Name, ContactsID_number);
                s.setContract(c);
                s.setWorte(w);
                return s;
            }else
                return null;
        } catch (Exception e) {
          //  System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
            //    System.out.println(ex.getMessage());
            }
        }
        return null;
    }

    public void WriteContact(String storeId, int supId, String name, int number, int id){

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO Contact VALUES (?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1, supId);
            stmt.setString(2, name);
            stmt.setInt(3, number);
            stmt.setInt(4, id);
            stmt.setString(5, storeId);

            stmt.executeUpdate();
        }
        catch (Exception e) {
          //  System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
             //   System.out.println(ex.getMessage());
            }
        }
    }

    public void DeleteContact(String storeId, int supId,int id){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From Contact"+
                    " WHERE sid = '"+supId+ "' AND StoreId = '"+storeId+ "' AND ID = '"+id+"';";

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

    public  Map<Integer, String> GetContactsID_Name(String storeId, int supId){
        Map<Integer, String> list=new HashMap<Integer, String>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Contact " +
                    "WHERE sid = '"+supId+ "' AND StoreId = '"+storeId+"';";

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

    public  Map<Integer, Integer> GetContactsID_number (String storeId, int supId){
        Map<Integer, Integer> list=new HashMap<Integer, Integer>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Contact " +
                    "WHERE sid = '"+supId+ "' AND StoreId = '"+storeId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while(rs.next())
                list.put(rs.getInt(4),rs.getInt(3));
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

    public LinkedList<Supplier> GetSuppliers(String email_id) {
        LinkedList<Supplier> suppliers=new LinkedList<Supplier>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Supplier " +
                    "WHERE StoreId = '" + email_id + "';";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            while (rs.next()) {
               Supplier s=GetSupplier(rs.getInt(1),email_id);
               suppliers.add(s);
            }
        } catch (Exception e) {
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
        return suppliers;
    }


    /****************MapContract**************/
    /*public void WriteContract(int suplaier_ID, boolean fixeDays, boolean leading,String storeId) {
     MapContract.WriteContract(suplaier_ID,fixeDays,leading,storeId);
    }*/

    public void DeleteContract(int suplaier_ID,String StoreId) {
    MapContract.DeleteContract(suplaier_ID,StoreId);
    }

    public Contract getContract(int suplaier_ID, String StoreId) {
        return MapContract.getContract(suplaier_ID,StoreId);
    }

   /*public void UpdateContract(int suplaier_ID, boolean fixeDays, boolean leading,String storeId){
        MapContract.UpdateContract(suplaier_ID,fixeDays,leading,storeId);
    }*/


    /****************MapWorte**************/

    public void WriteWrote(String storeId ,int suplaier_ID, java.util.Map<Integer, Integer> itemsID_amount, Map<Integer,Double> itemsID_assumption){
        MapWorte.WriteWrote(storeId ,suplaier_ID, itemsID_amount,itemsID_assumption);
    }

    public void DeleteWrote(int suplaier_ID,String storeId) {
        MapWorte.DeleteWrote(suplaier_ID,storeId);
    }

    public Map<Integer, Integer> GetItemsID_Amount(int suplaier_ID,String storeId){
       return  MapWorte.GetItemsID_Amount(suplaier_ID,storeId);
    }

    public Map<Integer, Double> GetItemsID_Assumption(int suplaier_ID,String storeId){
       return MapWorte.GetItemsID_Assumption(suplaier_ID, storeId);
    }

    public Wrotequantities GetWrotequantities (int suplaier_ID, String storeId){
      return MapWorte.GetWrotequantities(suplaier_ID,storeId);
    }

}