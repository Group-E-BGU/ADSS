package DataAccesslayer;

import BusinessLayer.Product;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ProductDAO{


    public Product get(String id) {
        return null;
    }


    public List<Product> getAll() {
        List<Product> products = new LinkedList<>();

        Product tmpProduct;
        String CN;
        int weight;
        String name;

        String sql = "SELECT * FROM Products";

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                CN = rs.getString("CN");
                weight = rs.getInt("weight");
                name = rs.getString("name");

                tmpProduct = new Product(name, CN, weight);

                products.add(tmpProduct);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return products;
    }


    public void save(Product product) {

        String sql = "INSERT INTO Products(CN, weight, name) VALUES(?,?,?)";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getCN());
            pstmt.setInt(2, product.getWeight());
            pstmt.setString(3, product.getName());
            pstmt.executeUpdate();
            System.out.println("Product added successfully");
        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

    }


    public void update(Product product, String[] params) {

    }


    public void delete(Product product) {

    }
}
