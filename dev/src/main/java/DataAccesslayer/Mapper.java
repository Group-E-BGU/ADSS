package DataAccesslayer;

import BusinessLayer.ItemRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mapper {

    private Connection conn;

    public Mapper(){
    }

    public void InitializeDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");
            //todo add storeId to the praymery key in every table..
            Statement stmt = conn.createStatement();
            String sqlStmt = "CREATE TABLE IF NOT EXISTS Store(" +
                    "email varchar NOT NULL," +
                    "itemId int," +
                    "NumOfProduct int," +
                    "NumOfOrder int," +
                    "totalAmount int," + //todo?
                    "PRIMARY KEY(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS ItemRecord(" +
                    "id int NOT NULL," +
                    "name varchar UNIQUE NOT NULL," +
                    "minAmount int," +
                    "storageAmount int," +
                    "shelfAmount int," +
                    "totalAmount int," +
                    "shelfNumber int," +
                    "manufacturer varchar," +
                    "StoreId varchar," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS User(" +
                    "email varchar NOT NULL," +
                    "Password varchar NOT NULL," +
                    "PRIMARY KEY(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS ItemDiscount(" +
                    "id int NOT NULL," +
                    "startDate date," +
                    "endDate date," +
                    "percentage int," +
                    "IRID int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email), " +
                    "FOREIGN KEY(IRID) REFERENCES ItemRecord(id));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Supplier(" +
                    "id int NOT NULL," +
                    "name varchar," +
                    "address varchar," +
                    "bank varchar," +
                    "branch int," +
                    "bankNumber int,"+
                    "payment varchar,"+
                    "StoreId varchar," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Category(" +
                    "name varchar NOT NULL," +
                    "CategoryRole int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(name)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS ItemRecord_Supplier(" +
                    "MainCategory varchar," +
                    "SubCategory varchar," +
                    "SubSubCategory varchar," +
                    "IRID int NOT NULL," +
                    "StoreId varchar," +
                    "name varchar,"+
                    "PRIMARY KEY(IRID,SID)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email), " +
                    "FOREIGN KEY(MainCategory) REFERENCES Category(name), " +
                    "FOREIGN KEY(SubCategory) REFERENCES Category(name), " +
                    "FOREIGN KEY(SubSubCategory) REFERENCES Category(name), " +
                    "FOREIGN KEY(IRID) REFERENCES ItemRecord(id));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS CategoryDiscount(" +
                    "id int NOT NULL," +
                    "startDate date," +
                    "endDate date," +
                    "percentage int," +
                    "CN varchar," +
                    "StoreId varchar," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email), " +
                    "FOREIGN KEY(CN) REFERENCES Category(name));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Price(" +
                    "id int NOT NULL," +
                    "RetailPrice int," +
                    "StorePrice int," +
                    "IRID int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email), " +
                    "FOREIGN KEY(IRID) REFERENCES ItemRecord(id));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Item(" +
                    "id int NOT NULL," +
                    "expirationDate date," +
                    "defective bit," +
                    "defectiveDate date," +
                    "IRID int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email), " +
                    "FOREIGN KEY(IRID) REFERENCES ItemRecord(id));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS DefectReport(" +
                    "id int NOT NULL," +
                    "startDate date," +
                    "endDate date," +
                    "StoreId varchar," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Item_DefectReport(" +
                    "DRID int NOT NULL," +
                    "IID int NOT NULL," +
                    "StoreId varchar," +
                    "PRIMARY KEY(DRID,IID)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Orders(" +
                    "id int NOT NULL," +
                    "SID int NOT NULL," +
                    "auto boolean," +
                    "day int,"+
                    "orderDate date," +
                    "arrivalTime date," +
                    "totalPrice double,"+
                    "status varchar,"+
                    "StoreId varchar," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(SID) REFERENCES Supplier(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);
            //todo add OID,PSupplierOD to the praymery key in every table..
            sqlStmt = "CREATE TABLE IF NOT EXISTS ProductOrder(" +
                    "OID int NOT NULL," +
                    "PStoreID int," +
                    "PSupplierOD bit," +
                    "amount int,"+
                    "StoreId varchar," +
                    "PRIMARY KEY(StoreId)," +
                    "FOREIGN KEY(OID) REFERENCES Orders(id));";
            stmt.execute(sqlStmt);


            sqlStmt = "CREATE TABLE IF NOT EXISTS Contract(" +
                    "SupplierId int NOT NULL," +
                    "fixDay bit," +
                    "leading bit," +
                    "StoreId varchar," +
                    "PRIMARY KEY(SupplierId)," +
                    "FOREIGN KEY(SupplierId) REFERENCES Supplier(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS quantityWrote(" +
                    "pid int NOT NULL," +
                    "amount int," +
                    "sale double," +
                    "SID int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(pid)," +
                    "FOREIGN KEY(SID) REFERENCES Supplier(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Contact(" +
                    "sid int NOT NULL," +
                    "name varchar," +
                    "phone int," +
                    "id int,"+
                    "StoreId varchar," +
                    "ContractSupplierId int,"+
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(ContractSupplierId) REFERENCES Contract(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Contact(" +
                    "sid int NOT NULL," +
                    "name varchar," +
                    "phone int," +
                    "ID int,"+
                    "StoreId varchar," +
                    "PRIMARY KEY(ID)," +
                    "FOREIGN KEY(sid) REFERENCES Contract(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS ProductSupplier(" +
                    "pstid int NOT NULL," +
                    "name varchar," +
                    "price double ," +
                    "psid int,"+
                    "StoreId varchar," +
                    "SupId int,"+
                    "PRIMARY KEY(psid)," +
                    "FOREIGN KEY(SupId) REFERENCES Contract(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Days(" +
                    "sid int NOT NULL," +
                    "day int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(sid)," +
                    "FOREIGN KEY(sid) REFERENCES Contract(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

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

    public void WriteUser(String email, String password){
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

                String sqlstmt = "INSERT INTO  User VALUES (?,?)";

                PreparedStatement stmt = conn.prepareStatement(sqlstmt);

                stmt.setString(1,email);
                stmt.setString(2,password);

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

    public void WriteStore(String email, int itemeId, int numOfProduct, int NumOfOrder ){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO  Store VALUES (?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setString(1,email);
            stmt.setInt(2,itemeId);
            stmt.setInt(3,numOfProduct);
            stmt.setInt(4,NumOfOrder);

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

    public void DeleteStore(String email){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE * From Store"+
                   "email = '"+email+"';";

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

            String sqlstmt = "DELETE * From Supplier"+
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

            String sqlstmt = "DELETE * From Contract"+
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

    public DALContract ReadContract(int id) {
        //todo
        return null;
    }

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

            String sqlstmt = "DELETE * From quantityWrote"+
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

    public DALWrotequantities ReadWorte(int id) {
       //todo
        return null;
    }

    public void WriteOrder(String storeId, int id_suplaier, int IdOrder,boolean auto,LinkedList<Integer> days, Date d, Date dd , Double TotalPrice, String status) {

        for (int day: days
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
                stmt.setDouble(7,TotalPrice);
                stmt.setString(8, status);
                stmt.setString(9, storeId);

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

    public void DeleteOrder(String storeId, int OrderId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE * From Orders"+
                    "WHERE id = '"+OrderId+ "' AND StoreId = '"+storeId+"';";

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

    public void WriteProductOrder(String storeId, int IdOrder,Map<Integer,Integer>ProductIDSupplier_ProductID_Store, Map<Integer, Integer> ProductIDSupplier_numberOfItems){

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

            String sqlstmt = "DELETE * From ProductOrder"+
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

    public void DeleteDayFromContract(String storeId, int supplierId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE * From quantityWrote"+
                    "WHERE sid = '"+supplierId+ "' AND StoreId = '"+storeId+"';";

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

    public void WriteContact(String storeId, int supId,String name, int number, int id){

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

    public void DeleteContact(String storeId, int supId,int id){
        try {
             Class.forName("org.sqlite.JDBC");
             conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

             String sqlstmt = "DELETE * From Contact"+
                     "WHERE sid = '"+supId+ "' AND StoreId = '"+storeId+ "' AND ID = '"+id+"';";

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

    public void DeleteProductFromSupplier(String storeId, int supId, int P_sup_Id ){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE * From ProductSuppliet"+
                    "WHERE SupId = '"+supId+ "' AND StoreId = '"+storeId+ "' AND psid = '"+P_sup_Id+"';";

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

    public void WriteItemRecord_Supplier(String storeId, int PId, String MainCategory, String SubCategory, String SubSubCategory, String name ){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO ItemRecord_Supplier VALUES (?,?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setString(1, MainCategory);
            stmt.setString(2, SubCategory);
            stmt.setString(3, SubSubCategory);
            stmt.setInt(4,PId);
            stmt.setString(5, storeId);
            stmt.setString(6, name);

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

    public void DeleteItemRecord_Supplier(String storeId, int PId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "DELETE * From ItemRecord_Supplier"+
                    "WHERE IRID = '"+PId+ "' AND StoreId = '"+storeId+"';";

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

    public String CheckEmailExist(String email) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM User " +
                    "WHERE email = '"+email+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
               return "Exist";
            else
                return "Not Exist";
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
        return "Not Exist";
    }

    public String CheckCorrectPassword(String email, String password) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM User " +
                    "WHERE email = '"+email+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next()) {
                if (rs.getString(2).equals(password)) {
                    return "correct";
                }
            else
                    return "un correct password";
            }
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
        return "Not Exist";
    }

    public int getProductId(String storeId,String product_name, String category, String subcategory, String sub_subcategory, String manufacturer) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ItemRecord_Supplier " +
                    "WHERE MainCategory = '"+category+"' AND SubCategory = '"+subcategory+
                    "' AND SubSubCategory = '"+sub_subcategory+"' AND StoreId = '"+storeId+
                    "' AND name = '"+product_name+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return rs.getInt(4);
            else
                return -1;
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
        return -1;
    }

    public LinkedList<DALOrder> ReadAllInvetation() {
        LinkedList<DALOrder> Invetation =null;
        return Invetation;
    }

    public LinkedList<DALSupplier> ReadAllSupplier() {
        LinkedList<DALSupplier> Vendors =null;
        return Vendors;
    }
}
