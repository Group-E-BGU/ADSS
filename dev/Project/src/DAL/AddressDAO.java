package DAL;
import BL.Address;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AddressDAO implements DAO<Address> {
    @Override
    public Address get(int id) {
        // TODO - must change the id to string.
        return null;
    }

    @Override
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

    @Override
    public void save(Address address)
    {

        String sql = "INSERT INTO Addresses(location, contactName, phoneNumber) VALUES(?,?,?)";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, address.getLocation());
            pstmt.setString(2, address.getContactName());
            pstmt.setString(3, address.getPhoneNumber());
            pstmt.executeUpdate();

            System.out.println("Address has been added successfully");

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }



    }

    @Override
    public void update(Address address, String[] params)
    {

    }

    @Override
    public void delete(Address address) {

    }
}
