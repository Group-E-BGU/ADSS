package DataAccesslayer;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapperSupplier {
    private static Connection conn;
    public void WriteSupplier(String Name, int ID, String address, String bank, String branch, int BankNumber,
                              String Payments,String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO  Supplier VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1,ID);
            stmt.setString(2,Name);
            stmt.setString(3,address);
            stmt.setString(4,bank);
            stmt.setString(5,branch);
            stmt.setInt(6,BankNumber);
            stmt.setString(7,Payments);
            stmt.setString(8,storeId);

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

    public void EditSupplier(String Name, int ID, String address, String bank, String branch, int BankNumber,
                             String Payments,String storeId) {

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO Supplier VALUES (?,?,?,?,?,?,?,?)"+
                    "WHERE id = '"+ID+ "' AND StoreId = '"+storeId+"';";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setString(2,Name);
            stmt.setString(3,address);
            stmt.setString(4,bank);
            stmt.setString(5,branch);
            stmt.setInt(6,BankNumber);
            stmt.setString(7,Payments);

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

    public void DeleteSupplier(int ID, String storeId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE From Supplier"+
                    "WHERE id = '"+ID+ "' AND StoreId = '"+storeId+"';";

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

/*public String Name;
    public int ID;
    public String Address;
    public String Bank;
    public String Branch;
    public int BankNumber;
    public String Payments;
    public Map<Integer, String> ContactsID_Name;
    public Map<Integer, Integer> ContactsID_number;

    public MapperContract Contract;
    public MapperWrotequantities Worte;

    public MapperSupplier(String name, int ID,String address, String bank,String branch, int bankNumber,
                       String payments, Map<Integer, String> Contacts_ID,
                       Map<Integer, Integer> Contacts_number) {
        Name = name;
        this.ID = ID;
        Address=address;
        Bank=bank;
        Branch=branch;
        BankNumber = bankNumber;
        Payments = payments;
        ContactsID_Name=Contacts_ID;
        ContactsID_number=Contacts_number;
        Contract=null;
        Worte=null;

    }
}*/