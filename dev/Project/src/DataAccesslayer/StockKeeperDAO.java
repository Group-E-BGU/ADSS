package DataAccesslayer;

import BusinessLayer.Shift;
import BusinessLayer.StockKeeper;
import BusinessLayer.WorkerDeal;
import javafx.util.Pair;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StockKeeperDAO {

    public StockKeeper get(int stockKeeperId) {

        StockKeeper stockKeeper = null;

        String sql = "SELECT * FROM StockKeepers WHERE id = ?";

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
                stockKeeper.setWorkerShifts(getShiftsIds(rs.getString("shifts")));

            } else
                return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return stockKeeper;
    }

    private List<Integer> getShiftsIds(String shifts) {
        //return Arrays.stream(shifts.split("\n")).map(Integer::parseInt).collect(Collectors.toCollection(LinkedList::new));

        List<Integer> shiftsId = new LinkedList<>();

        if(shifts.compareTo("") == 0)
            return shiftsId;

        String[] separatedIds = shifts.split("\n");

        for (String separatedId : separatedIds) shiftsId.add(Integer.parseInt(separatedId));

        return shiftsId;
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

                tmpStockKeeper = new StockKeeper(id, name, schedule, contract);
                tmpStockKeeper.setWorkerShifts(getShiftsIds(rs.getString("shifts")));

                stockKeepers.add(tmpStockKeeper);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // upon failure return an empty list
        }

        return stockKeepers;
    }

    public void save(StockKeeper stockKeeper) {

        String sql = "INSERT INTO StockKeepers(id, name, schedule, shifts) VALUES(?, ?, ?, ?)";

        int id = stockKeeper.getId();
        String name = stockKeeper.getName();
        String schedule = encodeSchedule(stockKeeper.getSchedule());
        String shifts = encodeShifts(stockKeeper.getWorker_shifts());

        // add the contract to the contracts table with the id of the driver
        new WorkerDealDAO().save(stockKeeper.getContract());

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, schedule);
            pstmt.setString(4, shifts);

            pstmt.executeUpdate();

            System.out.println("The worker has been added successfully");
        } catch (SQLException e)
        {

            System.out.println(e.getMessage());
        }

    }

    private static String encodeShifts(List<Integer> worker_shifts) {
        // when encoding a list of shifts, the string value returned is the id's of the shifts
        // separated by '\n'
        StringBuilder shiftsIds = new StringBuilder();

        for (Integer shift : worker_shifts)
            shiftsIds.append(shift).append("\n");

        return shiftsIds.toString();
    }

    public void update(StockKeeper stockKeeper) {
        String sql = "UPDATE StockKeepers SET name = ? , " +
                "schedule = ? " +
                "WHERE id = ? ";

        int id = stockKeeper.getId();
        String name = stockKeeper.getName();
        String schedule = encodeSchedule(stockKeeper.getSchedule());

        // add the contract to the contracts table with the id of the driver
        new WorkerDealDAO().update(stockKeeper.getContract());

        try (Connection conn = DAL.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, schedule);
            pstmt.setInt(3, id);

            pstmt.executeUpdate();

            System.out.println("The worker has been updated successfully");
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

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

    private static Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> decodeSchedule(String schedule){
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> decodedSchedule = new HashMap<>();
        String[] separatedDays = schedule.split("\n");

        for (String separatedDay : separatedDays) {
            decodedSchedule.put(new Pair<>(DayOfWeek.of(separatedDay.charAt(0) - 48), separatedDay.charAt(1) == 48 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening), separatedDay.charAt(2) == 49);
        }

        return decodedSchedule;
    }

    private static String encodeSchedule(Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule){
        // example : 210 - 2 : Monday, 1 : Evening, 0 : not working => <<Monday, Morning>, false>
        StringBuilder encodedSchedule = new StringBuilder();

        for(Map.Entry<Pair<DayOfWeek, Shift.ShiftTime>,Boolean> entry : schedule.entrySet()){
            encodedSchedule.append(entry.getKey().getKey().getValue());
            encodedSchedule.append(entry.getKey().getValue() == Shift.ShiftTime.Morning ? 0 : 1);
            encodedSchedule.append(entry.getValue() ? 1 : 0);

            encodedSchedule.append("\n");
        }

        return encodedSchedule.toString();
    }


}
