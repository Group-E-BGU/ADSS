package DAL;

import BL.Address;
import BL.Delivery;
import BL.Document;
import BL.Truck;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class DeliveryDAO {
    /*
    public Delivery get(int id) {

        Delivery delivery;

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

        Date date;
        Address source;
        List<Address> destinations;
        String launchTime;
        String truckSerialNumber;
        int driverID;
        Document document;

        String sql = "SELECT * FROM Deliveries WHERE documentId = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, documentId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                date = rs.getDate("date");
                source = getAddress(rs.getString("source")).getResult();
                destinations = decodeDestinations(rs.getString("destinations")); // TODO - ask mohammad
                launchTime = rs.getString("launchTime"); // TODO - ask mohammad
                truckSerialNumber = rs.getString("truckSerialNumber");
                driverID = rs.getInt("driverId");
                document = getDocument(rs.getInt("documentId")).getResult();

                delivery = new Delivery(0, date, source, destinations, launchTime, truckSerialNumber, driverID);

                result.complete(delivery);
            } else
                result.error("No delivery with document ID: " + documentId + " was found.");
        } catch (SQLException e) {
            result.error("Could not connect to database.");
        }

        return result;
    }

    public List<Delivery> getAll() {
        Result<List<Delivery>> result = new Result<>();
        String sql = "SELECT * FROM Deliveries";
        Delivery tmpDelivery;
        List<Delivery> deliveries = new LinkedList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

        Date date;
        Address source;
        List<Address> destinations;
        String launchTime;
        String truckSerialNumber;
        int driverID;
        Document document;

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                date = rs.getDate("date");
                source = getAddress(rs.getString("source")).getResult();
                destinations = decodeDestinations(rs.getString("destinations")); // TODO - ask mohammad
                launchTime = rs.getString("launchTime"); // TODO - ask mohammad
                truckSerialNumber = rs.getString("truckSerialNumber");
                driverID = rs.getInt("driverId");
                document = getDocument(rs.getInt("documentId")).getResult();

                tmpDelivery = new Delivery(0, date, source, destinations, launchTime, truckSerialNumber, driverID);

                deliveries.add(tmpDelivery);
            }

            result.complete(deliveries);
        } catch (SQLException e) {
            result.error("Could not connect to database.");
        }

        return result;
    }

    public void save(Delivery delivery) {

        String sql = "INSERT INTO Deliveries(documentId, date, source, destinations, launchTime, truckSerialNumber, driverId) VALUES(?, ?, ?, ?, ?, ?, ?)";

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

        int documentId = delivery.getDocument().getDocumentID();
        String date = sdf.format(delivery.getDate());
        String address = delivery.getSource().getLocation();
        String destinations = encodeDestinations(delivery.getDestinations());
        String launchTime = delivery.getLaunchTime();
        String truckSerialnumber = delivery.getTruckSerialNumber();
        int driverId = delivery.getDriverID();

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, documentId);
            pstmt.setString(2, date);
            pstmt.setString(3, address);
            pstmt.setString(4, destinations);
            pstmt.setString(5, launchTime);
            pstmt.setString(6, truckSerialnumber);
            pstmt.setInt(7, driverId);
            pstmt.executeUpdate();
        } catch (SQLException ignored) {
        }

    }

    public void update(Delivery delivery, String[] params) {

    }

    public void delete(Delivery delivery)
    {
        String sql = "DELETE FROM Deliveries WHERE documentId = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, documentId);
            pstmt.executeUpdate();

            result.complete("Delivery with document ID: " + documentId + " has been removed.");
        } catch (SQLException e) {
            result.error("No delivery with document ID: " + documentId + " is found.");
        }

        return result;
    }
    }

    private static String encodeDestinations(List<Address> destinations) {
        StringBuilder encodedDestinations = new StringBuilder();

        for (Address address : destinations)
            encodedDestinations.append(address.getLocation()).append("\n");

        return encodedDestinations.toString();
    }

    private static List<Address> decodeDestinations(String destinations) {
        List<Address> decodedDestinations = new LinkedList<>();
        String[] locations = destinations.split("\n");

        String location;
        String contactName;
        String phoneNumber;

        Address tmpAddress;

        StringBuilder sql = new StringBuilder("SELECT * FROM Addresses WHERE location IN (");

        for (int i = 0; i < locations.length - 1; i++) {
            sql.append(locations[i]).append(",");
        }

        sql.append(locations[locations.length - 1]).append(")");

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {

            // loop through the result set
            while (rs.next()) {
                location = rs.getString("location");
                contactName = rs.getString("contactName");
                phoneNumber = rs.getString("phoneNumber");

                tmpAddress = new Address(location, contactName, phoneNumber);

                decodedDestinations.add(tmpAddress);
            }

        } catch (SQLException e) {
            return null;
        }

        return decodedDestinations;
    }

     */
}