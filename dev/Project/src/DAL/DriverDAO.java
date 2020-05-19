package DAL;

import BL.*;
import BL.Driver;
import javafx.util.Pair;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DriverDAO implements DAO<Driver>{
    @Override
    public Driver get(int driverId)
    {
        Driver driver = null;

        String sql = "SELECT FROM Drivers WHERE id = ?";

        int id;
        String name;
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule;
        String license;
        List<Integer> shifts;
        WorkerDeal contract;

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setInt(1, driverId);
            //
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
                name = rs.getString("name");
                schedule = decodeSchedule(rs.getString("schedule"));
                license = rs.getString("license");
                shifts = decodeShifts(rs.getString("shifts"));
                contract = new WorkerDealDAO().get(id);

                driver = new Driver(id, name, schedule, contract, license);

            } else
                System.out.println("No driver with id :" + driverId + " is found.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return driver;
    }

    @Override
    public List<Driver> getAll()
    {
        String sql = "SELECT * FROM Drivers";
        List<Driver> drivers = new LinkedList<>();
        Driver tmpDriver;

        int id;
        String name;
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule;
        String license;
        List<Integer> shifts;
        WorkerDeal contract;

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                id = rs.getInt("id");
                name = rs.getString("name");
                schedule = decodeSchedule(rs.getString("schedule"));
                license = rs.getString("license");
                shifts = decodeShifts(rs.getString("shifts"));
                contract = new WorkerDealDAO().get(id);

                tmpDriver = new Driver(id, name, schedule, contract, license);//TODO - ask mohammad about the shifts field

                drivers.add(tmpDriver);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return drivers;
    }

    @Override
    public void save(Driver driver)
    {
        String sql = "INSERT INTO Drivers(id, name, schedule, license, shifts) VALUES(?, ?, ?, ?, ?)";

        int id = driver.getId();
        String name = driver.getName();
        String schedule = encodeSchedule(driver.getSchedule());
        String license = driver.getLicense();
        String shifts = encodeShifts(driver.getWorker_shifts());

        // add the contract to the contracts table with the id of the driver
        new WorkerDealDAO().save(driver.getContract());

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, schedule);
            pstmt.setString(4, license);
            pstmt.setString(5, shifts);

            pstmt.executeUpdate();
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Driver driver, String[] params) {

    }

    @Override
    public void delete(Driver driver)
    {

        String sql = "DELETE FROM Drivers WHERE id = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, driver.getId());
            pstmt.executeUpdate();

            System.out.println("Driver with id: " + driver.getId() + " has been removed successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private static String encodeSchedule(Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule) {
        // example : 210 - 2 : Monday, 1 : Evening, 0 : not working => <<Monday, Morning>, false>
        StringBuilder encodedSchedule = new StringBuilder();

        for (Map.Entry<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> entry : schedule.entrySet()) {
            encodedSchedule.append(entry.getKey().getKey().getValue());
            encodedSchedule.append(entry.getKey().getValue() == Shift.ShiftTime.Morning ? 0 : 1);
            encodedSchedule.append(entry.getValue() ? 1 : 0);

            encodedSchedule.append("\n");
        }

        return encodedSchedule.toString();
    }

    private static String encodeShifts(List<Shift> worker_shifts) {
        // when encoding a list of shifts, the string value returned is the id's of the shifts
        // separated by '\n'
        StringBuilder shiftsIds = new StringBuilder();

        for (Shift shift : worker_shifts)
            shiftsIds.append(shift.getShiftId()).append("\n");

        return shiftsIds.toString();
    }

    private static Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> decodeSchedule(String schedule) {
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> decodedSchedule = new HashMap<>();
        String[] separatedDays = schedule.split("\n");

        for (String separatedDay : separatedDays) {
            decodedSchedule.put(new Pair<DayOfWeek, Shift.ShiftTime>(DayOfWeek.of(separatedDay.charAt(0)), separatedDay.charAt(1) == 0 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening), separatedDay.charAt(2) == 1);
        }

        return decodedSchedule;
    }

    private static List<Shift> decodeShifts(String shifts) {
        List<Shift> decodedShifts = new LinkedList<>();
        String[] shiftsIds = shifts.split("\n");

        Shift tmpShift;

        int shift_id;
        Date shift_date;
        Worker boss;
        Shift.ShiftTime shift_time;
        Map<WorkPolicy.WorkingType, List<Worker>> work_team;

        StringBuilder sql = new StringBuilder("SELECT * FROM Shifts WHERE id IN (");

        for (int i = 0; i < shiftsIds.length - 1; i++) {
            sql.append(shiftsIds[i]).append(",");
        }

        sql.append(shiftsIds[shiftsIds.length - 1]).append(")");

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {

            // loop through the result set
            while (rs.next()) {
                shift_id = rs.getInt("id");
                shift_date = rs.getDate("date");
                boss = new StockKeeperDAO().get(rs.getInt("boss"));
                shift_time = rs.getString("time").compareTo("Morning") == 0 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening;
                work_team = decodeWorkTeam(rs.getString("workTeam"));

                tmpShift = new Shift(shift_date, shift_time, boss, work_team);
                tmpShift.setShiftID(shift_id);

                decodedShifts.add(tmpShift);
            }

        } catch (SQLException e) {
            return null;
        }
        return decodedShifts;
    }
}
