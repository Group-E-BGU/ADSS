package DAL;

import BL.Address;
import BL.Delivery;
import BL.Document;
import BL.Truck;

import javax.print.Doc;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DeliveryDAO {

    public Delivery get(int id) {

        Delivery delivery;

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

        Date date;
        String source;
        String truckSerialNumber;
        int driverID;
        List<String> logs;
        int truckWeight;
        Map<String, Document> documents;

        String sql = "SELECT * FROM Deliveries WHERE id = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                date = rs.getDate("date");
                source = rs.getString("source");
                documents = decodeDocuments(rs.getString("documents"));
                truckSerialNumber = rs.getString("truckSerialNumber");
                driverID = rs.getInt("driverId");
                truckWeight = rs.getInt("truckWeight");
                logs = decodeLogs(rs.getString("logs"));

                delivery = new Delivery(date, source, truckSerialNumber,driverID,truckWeight);
                delivery.setDeliveryId(id);

                delivery.setDocuments(documents);
                delivery.setLogs(logs);

                return delivery;
            } else
                System.out.println("No delivery with ID: " + id + " was found.");
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return null;
    }

    private Map<String, Document> decodeDocuments(String documents) {
        Map<String, Document> decodedDocuments = new HashMap<>();
        String[] separatedDocuments = documents.split("\n");
        String[] separatedProducts;
        String destination;
        Document document;
        Map<String, Integer> products;

        for(String doc : separatedDocuments){
            separatedProducts = doc.split(",");
            // separatedProducts[0] is the destination and the rest are the products and their amounts
            destination = separatedProducts[0];
            products = new HashMap<>();
            // loop through the products and their amounts and decode them
            for(int i = 1; i < separatedProducts.length; i+=2)
                products.put(separatedDocuments[i], Integer.parseInt(separatedProducts[i+1]));

            document = new Document();
            document.setDeliveryGoods(products);

            decodedDocuments.put(destination, document);
        }

        return decodedDocuments;
    }

    public List<Delivery> getAll() {
        List<Delivery> result = new LinkedList<>();
        String sql = "SELECT * FROM Deliveries";
        Delivery delivery;
        List<Delivery> deliveries = new LinkedList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

        Date date;
        String source;
        String truckSerialNumber;
        int driverID;
        Map<String, Document> documents;
        int deliveryId;


        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                String stringDate = rs.getString("date");
                date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
                //date = rs.getDate("date");
                source = rs.getString("source");
                documents = decodeDocuments(rs.getString("documents"));
                truckSerialNumber = rs.getString("truckSerialNumber");
                driverID = rs.getInt("driverId");
                int truckWeight = rs.getInt("truckWeight");
                List<String> logs = decodeLogs(rs.getString("logs"));
                deliveryId = rs.getInt("id");

                delivery = new Delivery(date, source, truckSerialNumber,driverID,truckWeight);
                delivery.setDeliveryId(deliveryId);
                delivery.setDocuments(documents);
                delivery.setLogs(logs);

                deliveries.add(delivery);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return deliveries;
    }

    public int save(Delivery delivery) {

        String sql = "INSERT INTO Deliveries(date, source, truckSerialNumber, driverId, documents, logs, truckWeight) VALUES(?, ?, ?, ?, ?, ?, ?)";

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(delivery.getDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH) + 1;
        //String date = sdf.format(delivery.getDate());
        String date = day + "/" + month + "/" + year;
        String source = delivery.getSource();
        String truckSerialnumber = delivery.getTruckSerialNumber();
        int driverId = delivery.getDriverID();
        List<String> logs = delivery.getLogs();
        int truckWeight = delivery.getTruckWeight();
        Map<String, Document> documents = delivery.getDocuments();
        int deliveryID = -1;

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, source);
            pstmt.setString(3, truckSerialnumber);
            pstmt.setInt(4, driverId);
            pstmt.setString(5, encodeDocuments(documents));
            pstmt.setString(6, encodeLogs(logs));
            pstmt.setInt(7, truckWeight);
            pstmt.executeUpdate();

        } catch (SQLException ignored) {
        }

        sql = "SELECT MAX(id) AS LAST FROM Deliveries";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                deliveryID =  rs.getInt("LAST");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return deliveryID;
    }

    private String encodeDocuments(Map<String, Document> documents) {
        String encodedDocuments = "";

        for(Map.Entry<String, Document> entry : documents.entrySet()){
            encodedDocuments += entry.getKey() + ",";
            encodedDocuments += encodeDocument(entry.getValue());
            encodedDocuments = encodedDocuments.substring(0, encodedDocuments.length() - 1);
            encodedDocuments += "\n";
        }

        return encodedDocuments;
    }

    private String encodeDocument(Document document) {
        String encodedDocument = "";

        for(Map.Entry<String,Integer> entry : document.getDeliveryGoods().entrySet()){
            encodedDocument += entry.getKey() + ",";
            encodedDocument += entry.getValue() + ",";
        }

        return encodedDocument;
    }

    public void update(Delivery delivery) {
        String sql = "UPDATE Deliveries SET date = ?," +
                "source = ?," +
                "truckSerialNumber = ? , " +
                "driverId = ? , " +
                "documents = ? , " +
                "logs = ? , " +
                "truckWeight = ? " +
                "WHERE id = ?";

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(delivery.getDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH) + 1;
        //String date = sdf.format(delivery.getDate());
        String date = day + "/" + month + "/" + year;
        String source = delivery.getSource();
        String truckSerialnumber = delivery.getTruckSerialNumber();
        int driverId = delivery.getDriverID();
        List<String> logs = delivery.getLogs();
        int truckWeight = delivery.getTruckWeight();
        Map<String, Document> documents = delivery.getDocuments();
        int deliveryID = -1;

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, source);
            pstmt.setString(3, truckSerialnumber);
            pstmt.setInt(4, driverId);
            pstmt.setString(5, encodeDocuments(documents));
            pstmt.setString(6, encodeLogs(logs));
            pstmt.setInt(7, truckWeight);
            pstmt.executeUpdate();

        } catch (SQLException ignored) {
        }

    }

    public void delete(int id) {
        String sql = "DELETE FROM Deliveries WHERE id = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            System.out.println(("Delivery with ID: " + id + " has been removed."));
        } catch (SQLException e) {
            System.out.println(("No delivery with document ID: " + id + " is found."));
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

    private static String encodeLogs(List<String> logs) {
        // combine (encode) the logs into one string
        // \n is the char that separates between two different logs
        StringBuilder output = new StringBuilder();

        for (String log : logs)
            output.append(log).append('\n');

        return output.toString();
    }

    private static List<String> decodeLogs(String logs) {
        List<String> decodedLogs = new LinkedList<>();
        String[] splicedLogs = logs.split("\n", -1);

        Collections.addAll(decodedLogs, splicedLogs);

        return decodedLogs;
    }
}