package DAL;

import BL.Truck;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class TruckDAO implements DAO<Truck> {
    @Override
    public Truck get(int id) {
        return null;
    }

    @Override
    public List<Truck> getAll() {
        List<Truck> trucks = new LinkedList<>();

        String sql = "SELECT * FROM Trucks";

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                trucks.add(new Truck(rs.getString("serialNumber"), rs.getString("model"), rs.getInt("weight"), rs.getInt("maxAllowedWeight")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return trucks;
    }

    @Override
    public void save(Truck truck)
    {

        String sql = "INSERT INTO Trucks(serialNumber, weight, maxAllowedWeight, model) VALUES(?,?,?,?)";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, truck.getSerialNumber());
            pstmt.setInt(2, truck.getWeight());
            pstmt.setInt(3, truck.getMaxAllowedWeight());
            pstmt.setString(4, truck.getModel());
            pstmt.executeUpdate();
            System.out.println("Truck has been added successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    @Override
    public void update(Truck truck, String[] params) {

    }

    @Override
    public void delete(Truck truck) {

    }
}
