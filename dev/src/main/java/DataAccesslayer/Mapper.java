package DataAccesslayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mapper {

    private Repo Repositpry;
    int ID_Invetation;
    private Connection conn;

    public Mapper(){
        Repositpry=Repo.getInstance();
        ID_Invetation=0;
    }

    public void InitializeDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            Statement stmt = conn.createStatement();
            String sqlStmt = "CREATE TABLE IF NOT EXISTS Store(" +
                    "email varchar NOT NULL," +
                    "itemId int," +
                    "NumOfProduct int," +
                    "NumOfOrder int," +
                    "Column int," +
                    "totalAmount int," +
                    "StoreId varchar," +
                    "storeEmail varchar," +
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
                    "Column int," +
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
                    "SID int NOT NULL," +
                    "MainCategory varchar," +
                    "SubCategory varchar," +
                    "SubSubCategory varchar," +
                    "IRID int NOT NULL," +
                    "StoreId varchar," +
                    "name varchar,"+
                    "PRIMARY KEY(IRID,SID)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email), " +
                    "FOREIGN KEY(SID) REFERENCES Supplier(id), " +
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

            sqlStmt = "CREATE TABLE IF NOT EXISTS ProductOrder(" +
                    "OID int NOT NULL," +
                    "PStoreID int," +
                    "PSupplierOD bit," +
                    "amount int,"+
                    "orderID int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(StoreId)," +
                    "FOREIGN KEY(orderID) REFERENCES Orders(id));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Contract(" +
                    "id int NOT NULL," +
                    "fixDay bit," +
                    "leading bit," +
                    "StoreId varchar," +
                    "SupplierId int,"+
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(SupplierId) REFERENCES Supplier(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS quantityWrote(" +
                    "pid int NOT NULL," +
                    "amount int," +
                    "sale int," +
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
                    "id int NOT NULL," +
                    "name varchar," +
                    "phone int," +
                    "ID inr,"+
                    "StoreId varchar," +
                    "ContractSupplierId int,"+
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(ContractSupplierId) REFERENCES Contract(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS ProductSupplier(" +
                    "stid int NOT NULL," +
                    "name varchar," +
                    "price int," +
                    "sid inr,"+
                    "StoreId varchar," +
                    "ContractSupplierId int,"+
                    "PRIMARY KEY(stid)," +
                    "FOREIGN KEY(ContractSupplierId) REFERENCES Contract(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Days(" +
                    "sid int NOT NULL," +
                    "day int," +
                    "StoreId varchar," +
                    "ContractSupplierId int,"+
                    "PRIMARY KEY(sid)," +
                    "FOREIGN KEY(ContractSupplierId) REFERENCES Contract(id),"+
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

    public void WriteSupplier(String Name, int ID, String address, String bank, String branch, int BankNumber,
                              String Payments, Map<Integer, String> Contacts_ID,
                              Map<Integer, Integer> Contacts_number) {

        DALSupplier Dalush = new DALSupplier(Name, ID, address, bank,branch,BankNumber, Payments, Contacts_ID,
                Contacts_number);
        Repositpry.Suppliers.add(Dalush);
    }

    public String EditSupplier(String Name, int ID, String address, String bank, String branch, int BankNumber,
                               String Payments, Map<Integer, String> Contacts_ID,
                               Map<Integer, Integer> Contacts_number) {

        DALSupplier Dalush = new DALSupplier(Name, ID,address, bank,branch,BankNumber, Payments, Contacts_ID,
                Contacts_number);

        for (DALSupplier Ven: Repositpry.Suppliers
             ) {
        if(Ven.ID==Dalush.ID)
            Repositpry.Suppliers.remove(Ven);
    }
        Repositpry.Suppliers.add(Dalush);
        return "Done";
}

    public String DeleteSupplier(int ID){
        for (DALSupplier Ven: Repositpry.Suppliers
        ) {
            if(Ven.ID==ID)
                Repositpry.Suppliers.remove(Ven);
                return "Done";
        }
        return "the supplier is not exist in the system";
    }

    public String WriteContract(int suplaier_ID, boolean fixeDays, List<Integer> dayes, boolean leading,
                              Map<Integer, String> productIDSupplier_name, Map<Integer, Integer> ItemsID_ItemsIDsupplier,
                              Map<Integer, Double> producttemsIDSupplier_price){

        DALContract Dalush=new DALContract(suplaier_ID, fixeDays,dayes, leading, productIDSupplier_name,
                ItemsID_ItemsIDsupplier, producttemsIDSupplier_price);
        for (DALSupplier Sup:Repositpry.Suppliers
             ) {
            if(Sup.ID==suplaier_ID){
                Sup.Contract=Dalush;
                return "Done";
            }
        }
        return "the supplier is not exist in the system";
    }

    public void DeleteContract(int suplaier_ID){
        for (DALSupplier Sup:Repositpry.Suppliers
        ) {
            if(Sup.ID==suplaier_ID){
                Sup.Contract=null;
            }
        }
    }

    public void WriteWrote(int suplaier_ID, java.util.Map<Integer, Integer> itemsID_amount, Map<Integer,Double> itemsID_assumption){
        DALWrotequantities Dalush=new DALWrotequantities(suplaier_ID,  itemsID_amount, itemsID_assumption);
        for (DALSupplier Sup:Repositpry.Suppliers
        ) {
            if(Sup.ID==suplaier_ID){
                Sup.Worte=Dalush;
            }
        }
    }

    public void DeleteWrote(int suplaier_ID){
        for (DALSupplier Sup:Repositpry.Suppliers
        ) {
            if(Sup.ID==suplaier_ID){
             Sup.Worte=null;
            }
        }
    }

    public LinkedList<DALOrder> ReadAllInvetation() {
        LinkedList<DALOrder> Invetation =Repositpry.Invetations;
        return Invetation;
    }

    public LinkedList<DALSupplier> ReadAllSupplier() {
        LinkedList<DALSupplier> Vendors =Repositpry.Suppliers;
        return Vendors;
    }


    public void WriteOrder(int id_suplaier, int numOfOrder,boolean auto,LinkedList<Integer> day, LocalDate d, LocalDate e , Map<Integer, Integer> itemsID_itemsIDSupplier, Map<Integer, Integer> productIDVendor_numberOfItems, Double TotalPrice, String status) {
        DALOrder Dalush=new DALOrder(id_suplaier,numOfOrder,auto,day,d,e,
                itemsID_itemsIDSupplier,productIDVendor_numberOfItems,TotalPrice,status);
        Repositpry.Invetations.add(Dalush);
    }


    public DALContract ReadContract(int id) {
        for (DALSupplier sup:Repositpry.Suppliers
             ) {
            if(sup.ID==id){
               return sup.Contract;
            }
        }
        return null;
    }

    public DALWrotequantities ReadWorte(int id) {
        for (DALSupplier sup:Repositpry.Suppliers
        ) {
            if(sup.ID==id){
                return sup.Worte;
            }
        }
        return null;
    }

    public String CheckEmailExist(String email) {
        for (DALUser du:Repositpry.Users){
            if (du.email.equals(email)){
                return "Exist";
            }
        }
        return "Not Exist";
    }

    public String CheckCorrectPasword(String email, String password) {
        for (DALUser du:Repositpry.Users
             ) {
            if(du.email.equals(email)){
                if (du.password.equals(password)){
                    return "correct";
                }
                return "un correct password";
            }
        }
        return "Not Exist";
    }

    public String Register(String email, String password) {
        DALUser du=new DALUser(email,password);
        Repositpry.Users.add(du);
        return "Done";
    }

    public int getProductId(String product_name, String category, String subcategory, String sub_subcategory, String manufacturer) {
        //todo
        return -1;
    }

    public void AddProdudt(int id, String product_name, String category, String subcategory, String sub_subcategory, String manufacturer) {
    Repositpry.AddProdudt(id,product_name,category,subcategory,sub_subcategory,manufacturer);

    }
}
