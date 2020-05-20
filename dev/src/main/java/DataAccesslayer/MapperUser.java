package DataAccesslayer;

import java.sql.*;

public class MapperUser {

    private static Connection conn;

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
        return "Not Exist";
    }

}

/*    public String email;
    public String password;

    public MapperUser(String email, String password) {
        this.email = email;
        this.password = password;
    }*/