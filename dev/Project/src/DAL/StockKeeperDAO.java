package DAL;

import BL.Shift;
import BL.StockKeeper;
import BL.WorkPolicy;
import BL.WorkerDeal;
import javafx.util.Pair;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StockKeeperDAO {

    public StockKeeper get(int stockKeeperId) {

        StockKeeper stockKeeper = null;

        String sql = "SELECT FROM StockKeepers WHERE id = ?";

        int id;
        String name;
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule;
        WorkerDeal contract;

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setInt(1, stockKeeperId);
            //
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
                name = rs.getString("name");
                schedule = decodeSchedule(rs.getString("schedule"));
                contract = new WorkerDealDAO().get(id);

                stockKeeper = new StockKeeper(id, name, schedule, contract);

            } else
                System.out.println("No stock keeper with id :" + stockKeeperId + " is found.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return stockKeeper;
    }

    public List<StockKeeper> getAll() {
        String sql = "SELECT * FROM StockKeepers";
        List<StockKeeper> stockKeepers = new LinkedList<>();
        StockKeeper tmpStockKeeper;

        int id;
        String name;
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule;
        WorkerDeal contract;

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                id = rs.getInt("id");
                name = rs.getString("name");
                schedule = decodeSchedule(rs.getString("schedule"));
                contract = new WorkerDealDAO().get(id);

                tmpStockKeeper = new StockKeeper(id, name, schedule, contract);//TODO - ask mohammad about the shifts field

                stockKeepers.add(tmpStockKeeper);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // upon failure return an empty list
        }

        return stockKeepers;
    }

    public void save(StockKeeper stockKeeper) {

        String sql = "INSERT INTO StockKeepers(id, name, schedule) VALUES(?, ?, ?)";

        int id = stockKeeper.getId();
        String name = stockKeeper.getName();
        String schedule = encodeSchedule(stockKeeper.getSchedule());

        // add the contract to the contracts table with the id of the driver
        new WorkerDealDAO().save(stockKeeper.getContract());

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, schedule);

            pstmt.executeUpdate();

            System.out.println("The worker has been added successfully");
        } catch (SQLException e)
        {

            System.out.println(e.getMessage());
        }

    }

    public void update(StockKeeper stockKeeper, String[] params) {

    }

    public void delete(StockKeeper stockKeeper) {
        int stockKeeperId = stockKeeper.getId();
        String sql = "DELETE FROM StockKeepers WHERE id = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockKeeperId);
            pstmt.executeUpdate();

            System.out.println("Stock keeper with id: " + stockKeeperId + " has been removed.");
        } catch (SQLException e) {
            System.out.println("No stock keeper with id: " + stockKeeperId + " is found.");
        }

    }

    }
