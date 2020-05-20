package DataAccesslayer;

import BusinessLayer.*;
//import BusinessLayer.ItemRecord;

import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MapperItemRecord {

    //this class also manages Items
    Connection conn = null;

    public void InsertItemRecord(String name,int id, int minAmount, int storageAmount, int shelfAmount, int totalAmount, int shelfNumber, String manufacture, String storeId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO ItemRecord VALUES (?,?,?,?,?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1,id);
            stmt.setString(2,name);
            stmt.setInt(3,minAmount);
            stmt.setInt(4,storageAmount);
            stmt.setInt(5,shelfAmount);
            stmt.setInt(6,totalAmount);
            stmt.setInt(7,shelfNumber);
            stmt.setString(8,manufacture);
            stmt.setString(9,storeId);

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

    public void InsertItem(int IRID, int id ,java.sql.Date expDate) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");
            Date d = new Date(expDate.getTime());
            String sqlstmt = "INSERT INTO Item VALUES (?,"+expDate+",?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setInt(1,id);
            //stmt.setDate(2,expDate);
            stmt.setBoolean(2,false);
            stmt.setDate(3, null);
            stmt.setInt(4,IRID);

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

    public void updateAmounts(int id,int storage, int shelf, int total) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "UPDATE ItemRecord SET storageAmount = "+storage+", shelfAmount = "+shelf+", totalAmount = "+total+" WHERE id = "+id+";";

            Statement stmt = conn.createStatement();
            stmt.execute(sqlstmt);

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

    public void updateDefect(int itemId, int itemRecId, java.sql.Date date, String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");
            Date utilDate = new Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            String sqlstmt = "UPDATE Item " +
                    "SET defective = " + true + " , defectiveDate = "+sqlDate.toString()+
                    " WHERE Item.id = "+itemId+";";


            Statement stmt = conn.createStatement();
            stmt.execute(sqlstmt);

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

    public boolean DeleteItem(String name, int id, String storeId) {
        try {
            if(!geItemIdsByName(name,storeId).contains(id))
                return false;
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");



            String sqlstmt = "DELETE FROM Item WHERE id IN (SELECT Item.id FROM Item JOIN ItemRecord ON ItemRecord.id = IRID WHERE ItemRecord.name = '"+name+"' AND " +
                    "Item.id = "+id+" AND ItemRecord.StoreId = '"+storeId+"')";

            Statement stmt = conn.createStatement();
            stmt.execute(sqlstmt);
            return true;
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
        return false;
    }

    public ItemRecord getItemRecord(String name, String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ItemRecord " +
                    "WHERE name = '"+name+ "' AND StoreId = '"+storeId+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return new ItemRecord(rs.getString(2),rs.getInt(1),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getString(8));
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

    public String printDefectedItems(java.sql.Date beginDate, java.sql.Date endDate, String storeId){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Item " +
                    "WHERE defective =" + true +"  AND defectiveDate >="+ beginDate +" " +
                    "AND defectiveDate <= '"+ endDate +"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            String l = "";
            while (rs.next()) {
                l = l + ("Item ID " + rs.getInt(1) + " is defected \n");
            }
            return l;
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
        return "";
    }

    public List<Integer> geItemIdsByName(String name, String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT Item.id " +
                    "FROM Item JOIN ItemRecord ON IRID = ItemRecord.id "+
                    "WHERE name = '"+name+"' AND ItemRecord.StoreId = '"+storeId+"' ;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            List<Integer> l = new LinkedList<>();
            while (rs.next())
                l.add(rs.getInt(1));
            return l;
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

    public int getMaxItemRecordId(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT MAX(id) " +
                    "FROM ItemRecord ;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return rs.getInt(1);
            else
                return 0;
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
            return 0;
    }

    public int getMaxItemId() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT MAX(id) " +
                    "FROM Item ;";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return rs.getInt(1);
            else
                return 0;
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
        return 0;
    }

    public List<ItemRecord> getItemRecordByCategoryName(String name,String storeId) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ItemRecord_Supplier " +
                    "WHERE StoreId = '"+storeId+"' AND (MainCategory = '"+name+"' OR SubCategory = '"+name+"' OR SubSubCategory = '"+name+"');";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            List<ItemRecord> l = new LinkedList<>();
            while (rs.next()) {
                ItemRecord i = getItemRecordById(rs.getInt(5));
                if(i != null)
                    l.add(i);
            }
            if(l.isEmpty())
                return null;
            return l;
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

    public ItemRecord getItemRecordById(int id) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM ItemRecord " +
                    "WHERE id = "+id+ ";";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return new ItemRecord(rs.getString(2),rs.getInt(1),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getString(8));
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

    public Item getItemById(int id) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Item " +
                    "WHERE id = "+id+ ";";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return new Item(id,rs.getDate(2));
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


}
