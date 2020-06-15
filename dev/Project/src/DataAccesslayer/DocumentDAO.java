package DataAccesslayer;

public class DocumentDAO
{
/*
    public void addDocument(Document document) {
        String sql = "INSERT INTO Documents(TruckWeight, logs, deliveryGoods) VALUES(?,?,?)";
        // encode the non-primitive fields
        String logs = encodeLogs(document.getLogs());
        String deliveryGoods = encodeDeliveryGoods(document.getDeliveryGoods());

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, document.getTruckWeight());
            pstmt.setString(2, logs);
            pstmt.setString(3, deliveryGoods);
            pstmt.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    public Result<List<Document>> getAllDocuments() {
        List<Document> documents = new LinkedList<>();
        List<String> logs;
        List<Product> products;
        Map<String, List<Product>> deliveryGoods;
        Result<List<Document>> result = new Result<>();
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
                logs = decodeLogs(rs.getString("logs"));

                // set the document fields
                tmpDocument.setLogs(logs);
                tmpDocument.setTruckWeight(rs.getInt("TruckWeight"));
                tmpDocument.setDeliveryGoods(deliveryGoods);
                tmpDocument.setDocumentID(rs.getInt("ID"));

                // add the document to the documents list
                documents.add(tmpDocument);
            }

            result.complete(documents);
        } catch (SQLException e) {
            result.error("Could not connect to database.");
        }

        return result;
    }

    public static Result<Document> getDocument(int documentId) {
        Result<Document> result = new Result<>();
        Document document = new Document();
        List<String> logs;
        Map<String, List<Product>> deliveryGoods;

        String sql = "SELECT * FROM Documents WHERE ID = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setInt(1, documentId);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                // get the delivery goods and the logs from the database and decode them
                deliveryGoods = decodeDeliveryGoods(rs.getString("deliveryGoods"));
                logs = decodeLogs(rs.getString("logs"));

                // set the document fields
                document.setLogs(logs);
                document.setTruckWeight(rs.getInt("TruckWeight"));
                document.setDeliveryGoods(deliveryGoods);
                document.setDocumentID(rs.getInt("ID"));

            } else
                result.error("No document with ID :" + documentId + " is found.");

        } catch (SQLException e) {
            result.error("Could not connect to database");
        }

        return result;
    }

    private static String encodeLogs(List<String> logs) {
        // combine (encode) the logs into one string
        // \n is the char that separates between two different logs
        String output = "";

        for (String log : logs)
            output += log + '\n';

        return output;
    }

    private static List<String> decodeLogs(String logs) {
        List<String> decodedLogs = new LinkedList<>();
        String[] splicedLogs = logs.split("\n", -1);

        Collections.addAll(decodedLogs, splicedLogs);

        return decodedLogs;
    }

    private static String encodeDeliveryGoods(Map<String, List<Product>> deliveryGoods) {
        String encodedGoods = "";

        // combine (encode) the goods into one string in the following format :
        // each pair of key and value will be converted to : (<key>\n<value>)
        // each key is a string, so it won't change
        // each value is a list, so it will be converted to a string, following this format :
        // each product will be encoded to a pair, the left value is the CN of the product in Products table in the database
        // the right value is the amount of the product, these two values will be separated by ','

        // iterate the map of the destinations and goods (delivery goods field)
        for (Map.Entry<String, List<Product>> entry : deliveryGoods.entrySet()) {
            encodedGoods += "(" + entry.getKey() + "\n";

            // iterate the list of the goods
            for (Product product : entry.getValue()) {
                // add the left value of the pair, the CN of the product
                encodedGoods += product.getCN();
                // add the separator
                encodedGoods += ",";
                // add the right value of the pair, the amount
                encodedGoods += product.getAmount();
                // add the separator of each two products
                encodedGoods += "\n";
            }
            encodedGoods += ")";
        }
        return encodedGoods;
    }

    private static Map<String, List<Product>> decodeDeliveryGoods(String deliveryGoods) {
        Product product;
        List<Product> products;
        HashMap<String, List<Product>> decodedGoods = new HashMap<>();
        // separate the encoded string by parentheses
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(deliveryGoods);
        String[] destinationGoods;
        String[] productInfo;

        while (m.find()) {
            destinationGoods = m.group(1).split("\n");
            products = new LinkedList<>();

            for (int i = 1; i < destinationGoods.length; i++) {
                productInfo = destinationGoods[i].split(",");

                product = getProduct(productInfo[0]).getResult();
                product.setAmount(Integer.parseInt(productInfo[1]));

                products.add(product);
            }

            decodedGoods.put(destinationGoods[0], products);
        }

        return decodedGoods;
    }


 */
}
