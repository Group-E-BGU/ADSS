package DataAccesslayer;
import BusinessLayer.Document;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentDAO
{
    public void saveDocument(Document document) {
        String sql = "INSERT INTO Documents(deliveryGoods, ID, destination) VALUES(?,?,?)";

        String deliveryGoods = encodeDeliveryGoods(document.getDeliveryGoods());
        int id = document.getDocumentID();
        String destination = document.getDestination();

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deliveryGoods);
            pstmt.setInt(2, id);
            pstmt.setString(3, destination);
            pstmt.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    public List<Document> getAllDocuments() {
        List<Document> documents = new LinkedList<>();
        // tmp doc fields
        Map<String, Integer> deliveryGoods = new HashMap<>();
        int id;
        String destination;
        // tmp doc
        Document tmpDocument = new Document();

        // get the String that represents the encoded documents
        String sql = "SELECT * FROM Documents";

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                // get the delivery goods and the logs from the database and decode them
                deliveryGoods = decodeDeliveryGoods(rs.getString("deliverGoods"));
                id = rs.getInt("ID");
                destination = rs.getString("destination");
                // set the fields
                tmpDocument.setDeliveryGoods(deliveryGoods);
                tmpDocument.setDocumentID(id);
                tmpDocument.setDestination(destination);

                // add the document to the documents list
                documents.add(tmpDocument);
            }
        } catch (SQLException ignored) {

        }

        return documents;
    }

    public static Document getDocument(int documentId) {
        Document document = new Document();

        Map<String, Integer> deliveryGoods;
        String destination;

        String sql = "SELECT * FROM Documents WHERE ID = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setInt(1, documentId);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                // get the delivery goods and the destination from the database and decode them
                deliveryGoods = decodeDeliveryGoods(rs.getString("deliveryGoods"));
                destination = rs.getString("destination");

                // set the document fields
                document.setDeliveryGoods(deliveryGoods);
                document.setDocumentID(rs.getInt("ID"));
                document.setDestination(destination);

            }
        } catch (SQLException ignored) {
        }

        return document;
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

    private static String encodeDeliveryGoods(Map<String, Integer> deliveryGoods) {
        StringBuilder encodedGoods = new StringBuilder();

        // combine (encode) the goods into one string in the following format :
        // each pair of key and value will be converted to : (<key>\n<value>)
        // each key is a string, so it won't change
        // each value is an Integer so it will be represented as a string, in its decimal format

        // iterate the map of the destinations and goods (delivery goods field)
        for (Map.Entry<String, Integer> entry : deliveryGoods.entrySet()) {
            encodedGoods.append("(").append(entry.getKey()).append("\n").append(entry.getValue().toString()).append(")");
        }

        return encodedGoods.toString();
    }

    private static Map<String, Integer> decodeDeliveryGoods(String deliveryGoods) {
        HashMap<String, Integer> decodedGoods = new HashMap<>();
        // separate the encoded string by parentheses
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(deliveryGoods);
        String[] destinationGoods;

        while (m.find()) {
            destinationGoods = m.group(1).split("\n");

            decodedGoods.put(destinationGoods[0], new Integer(destinationGoods[1]));
        }

        return decodedGoods;
    }

}
