package DataAccesslayer;

import BusinessLayer.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mapper {

    private static Connection conn;

    public Mapper(){
    }

    public static void InitializeDB() {
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
                    "PRIMARY KEY(id,StoreId)," +
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
                    "PRIMARY KEY(id,StoreId)," +
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
                    "PRIMARY KEY(StoreId,id)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Category(" +
                    "name varchar NOT NULL," +
                    "CategoryRole int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(name,StoreId)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS ItemRecord_Supplier(" +
                    "MainCategory varchar," +
                    "SubCategory varchar," +
                    "SubSubCategory varchar," +
                    "IRID int NOT NULL," +
                    "StoreId varchar," +
                    "name varchar,"+
                    "PRIMARY KEY(IRID,StoreId)," +
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
                    "PRIMARY KEY(id,StoreId)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email), " +
                    "FOREIGN KEY(CN) REFERENCES Category(name));";
            stmt.execute(sqlStmt);

            //todo-need to add  "StoreId varchar," +??
            sqlStmt = "CREATE TABLE IF NOT EXISTS Price(" +
                    "id int NOT NULL," +
                    "RetailPrice int," +
                    "StorePrice int," +
                    "IRID int," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(IRID) REFERENCES ItemRecord(id));";
            stmt.execute(sqlStmt);
            //todo-need to add  "StoreId varchar," +??
            sqlStmt = "CREATE TABLE IF NOT EXISTS Item(" +
                    "id int NOT NULL," +
                    "expirationDate date," +
                    "defective bit," +
                    "defectiveDate date," +
                    "IRID int," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(IRID) REFERENCES ItemRecord(id));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS DefectReport(" +
                    "id int NOT NULL," +
                    "startDate date," +
                    "endDate date," +
                    "StoreId varchar," +
                    "PRIMARY KEY(id,StoreId)," +
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Item_DefectReport(" +
                    "DRID int NOT NULL," +
                    "IID int NOT NULL," +
                    "StoreId varchar," +
                    "PRIMARY KEY(DRID,IID,StoreId)," +
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
                    "PRIMARY KEY(id,StoreId)," +
                    "FOREIGN KEY(SID) REFERENCES Supplier(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);
            //todo add OID,PSupplierOD to the praymery key in every table..
            sqlStmt = "CREATE TABLE IF NOT EXISTS ProductOrder(" +
                    "OID int NOT NULL," +
                    "PStoreID int," +
                    "PSupplierOD BIT," +
                    "amount int,"+
                    "StoreId varchar," +
                    "PRIMARY KEY(OID,PStoreId,StoreId)," +
                    "FOREIGN KEY(OID) REFERENCES Orders(id));";
            stmt.execute(sqlStmt);


            sqlStmt = "CREATE TABLE IF NOT EXISTS Contract(" +
                    "SupplierId int NOT NULL," +
                    "fixDay bit," +
                    "leading bit," +
                    "StoreId varchar," +
                    "PRIMARY KEY(StoreId,SupplierId)," +
                    "FOREIGN KEY(SupplierId) REFERENCES Supplier(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS quantityWrote(" +
                    "pid int NOT NULL," +
                    "amount int," +
                    "sale double," +
                    "SID int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(StoreId,SID,pid)," +
                    "FOREIGN KEY(SID) REFERENCES Supplier(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Contact(" +
                    "sid int NOT NULL," +
                    "name varchar," +
                    "phone int," +
                    "id int,"+
                    "StoreId varchar," +
                    "PRIMARY KEY(id,StoreId)," +
                    "FOREIGN KEY(sid) REFERENCES Contract(SupplierId),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS ProductSupplier(" +
                    "pstid int NOT NULL," +
                    "name varchar," +
                    "price double ," +
                    "psid int,"+
                    "StoreId varchar," +
                    "SupId int,"+
                    "PRIMARY KEY(psid,StoreId)," +
                    "FOREIGN KEY(SupId) REFERENCES Contract(id),"+
                    "FOREIGN KEY(StoreId) REFERENCES Store(email));";
            stmt.execute(sqlStmt);

            sqlStmt = "CREATE TABLE IF NOT EXISTS Days(" +
                    "sid int NOT NULL," +
                    "day int," +
                    "StoreId varchar," +
                    "PRIMARY KEY(sid,day,StoreId)," +
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

}
