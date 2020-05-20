package DataAccesslayer;

import java.sql.*;

public class MapperStore {

    private static Connection conn;
    public void WriteStore(String email, int itemeId, int numOfProduct, int NumOfOrder ){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "INSERT INTO  Store VALUES (?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sqlstmt);

            stmt.setString(1,email);
            stmt.setInt(2,itemeId);
            stmt.setInt(3,NumOfOrder);
            stmt.setInt(4,0);

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
            String sqlstmt = "DELETE From Store"+
                    "WHERE email = '"+email+"';";

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

    public void UpdateStore(String email, int itemeId, int numOfOrder) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "UPDATE Store SET " +
                    " itemId= '" + itemeId + "'," +
                    " NumOfOrder = '" + numOfOrder + "'" +
                    "WHERE email = '" + email + "';";

            Statement stmt = conn.createStatement();
            stmt.execute(sqlstmt);

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

    public int getNumOfOrder(String email) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:superLee.db");

            String sqlstmt = "SELECT * " +
                    "FROM Store " +
                    " WHERE email = '"+email+"';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlstmt);
            if(rs.next())
                return rs.getInt(3);
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
    }

