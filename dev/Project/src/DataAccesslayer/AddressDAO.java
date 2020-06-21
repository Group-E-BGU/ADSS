package DataAccesslayer;
import BusinessLayer.Address;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AddressDAO {


    public Address get(String location) {

        Address address= null;
        String sql = "SELECT * FROM Addresses WHERE location = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, location);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next())
               address = new Address(rs.getString("location"), rs.getString("contactName"), rs.getString("phoneNumber"));
            else
                System.out.println("No address with location :" + location + " has found.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return address;
    }


    public List<Address> getAll() {

        List<Address> addresses = new LinkedList<>();


        String sql = "SELECT * FROM Addresses";

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                addresses.add(new Address(rs.getString("location"), rs.getString("contactName"), rs.getString("phoneNumber")));
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            return null;
        }


        return addresses;
    }


    public void save(Address address)
    {

        String sql = "INSERT INTO Addresses(location, contactName, phoneNumber) VALUES(?,?,?)";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, address.getLocation());
            pstmt.setString(2, address.getContactName());
            pstmt.setString(3, address.getPhoneNumber());
            pstmt.executeUpdate();

            System.out.println("The address has been added successfully");

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }



    }


    public void update(Address address, String[] params)
    {

    }


    public void delete(Address address) {

    }
}
