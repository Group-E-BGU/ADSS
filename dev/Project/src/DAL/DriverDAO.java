package DAL;

import BL.*;
import BL.Driver;
import javafx.util.Pair;

import java.sql.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class DriverDAO
{
    public Driver get(int driverId) {
        Driver driver = null;

        String sql = "SELECT * FROM Drivers WHERE id = ?";

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
                shifts = getShiftsIds(rs.getString("shifts"));
                contract = new WorkerDealDAO().get(id);

                driver = new Driver(id, name, schedule, contract, license);
                driver.setWorkerShifts(shifts);

            } else
                System.out.println("No driver with id :" + driverId + " is found.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return driver;
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

    public List<Driver> getAll() {
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
                shifts = getShiftsIds(rs.getString("shifts"));
                contract = new WorkerDealDAO().get(id);

                tmpDriver = new Driver(id, name, schedule, contract, license);

                drivers.add(tmpDriver);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return drivers;
    }

    public void save(Driver driver) {
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

    public void update(Driver driver) {
        String sql = "UPDATE Drivers SET name = ? , " +
                "schedule = ? , " +
                "license = ? , " +
                "shifts = ? " +
                "WHERE id = ?";

        int id = driver.getId();
        String name = driver.getName();
        String schedule = encodeSchedule(driver.getSchedule());
        String license = driver.getLicense();
        String shifts = encodeShifts(driver.getWorker_shifts());

        // add the contract to the contracts table with the id of the driver
        new WorkerDealDAO().update(driver.getContract());

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, schedule);
            pstmt.setString(3, license);
            pstmt.setString(4, shifts);
            pstmt.setInt(5, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void delete(Driver driver) {

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

    private static String encodeShifts(List<Integer> worker_shifts) {
        // when encoding a list of shifts, the string value returned is the id's of the shifts
        // separated by '\n'
        StringBuilder shiftsIds = new StringBuilder();

        for (Integer shift : worker_shifts)
            shiftsIds.append(shift).append("\n");

        return shiftsIds.toString();
    }

    private static Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> decodeSchedule(String schedule) {
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> decodedSchedule = new HashMap<>();
        String[] separatedDays = schedule.split("\n");

        for (String separatedDay : separatedDays) {
            decodedSchedule.put(new Pair<>(DayOfWeek.of(separatedDay.charAt(0) - 48), separatedDay.charAt(1) == 48 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening), separatedDay.charAt(2) == 49);
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
        Map<WorkPolicy.WorkingType, List<Integer>> work_team;
        Address address;

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
                work_team = decodeWorkingTeam(rs.getString("workTeam"));
                address = (new AddressDAO()).get(rs.getString("address"));

                tmpShift = new Shift(address, shift_date, shift_time, boss, work_team);
                tmpShift.setShiftID(shift_id);

                decodedShifts.add(tmpShift);
            }

        } catch (SQLException e) {
            return null;
        }
        return decodedShifts;
    }

    private static Map<WorkPolicy.WorkingType, List<Worker>> decodeWorkTeam(String workTeam) {
        Map<WorkPolicy.WorkingType, List<Worker>> decodedWorkTeam = new HashMap<>();
        String[] separatedWorkTeams = workTeam.split("\n");
        String[] tmpTeam;
        List<Worker> workers;

        for (String team : separatedWorkTeams) {
            tmpTeam = team.split(",");
            tmpTeam = Arrays.copyOfRange(tmpTeam, 1, tmpTeam.length - 1);
            workers = new LinkedList<>();

            if (team.charAt(0) == 0) {
                // the team are stock keepers
                int id;
                String name;
                Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule;
                WorkerDeal contract;
                StockKeeper stockKeeper;

                StringBuilder sql = new StringBuilder("SELECT * FROM StockKeepers WHERE id IN (");

                for (int i = 0; i < tmpTeam.length - 1; i++) {
                    sql.append(tmpTeam[i]).append(",");
                }

                sql.append(tmpTeam[tmpTeam.length - 1]).append(")");

                try (Connection conn = DAL.connect();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql.toString())) {

                    // loop through the result set
                    while (rs.next()) {
                        id = rs.getInt("id");
                        name = rs.getString("name");
                        schedule = decodeSchedule(rs.getString("schedule"));
                        contract = (new WorkerDealDAO()).get(id);

                        stockKeeper = new StockKeeper(id, name, schedule, contract);

                        workers.add(stockKeeper);
                    }
                } catch (SQLException ignored) {
                }

                decodedWorkTeam.put(WorkPolicy.WorkingType.StockKeeper, workers);
            } else {
                // the team are drivers
                Driver driver;

                int id;
                String name;
                Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule;
                String license;
                List<Shift> shifts;
                WorkerDeal contract;

                StringBuilder sql = new StringBuilder("SELECT * FROM StockKeepers WHERE id IN (");

                for (int i = 0; i < tmpTeam.length - 1; i++) {
                    sql.append(tmpTeam[i]).append(",");
                }

                sql.append(tmpTeam[tmpTeam.length - 1]).append(")");

                try (Connection conn = DAL.connect();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql.toString())) {

                    // loop through the result set
                    while (rs.next()) {
                        id = rs.getInt("id");
                        name = rs.getString("name");
                        schedule = decodeSchedule(rs.getString("schedule"));
                        license = rs.getString("license");
                        shifts = decodeShifts(rs.getString("shifts"));
                        contract = (new WorkerDealDAO()).get(id);

                        driver = new Driver(id, name, schedule, contract, license);

                        workers.add(driver);
                    }
                } catch (SQLException ignored) {
                }

                decodedWorkTeam.put(WorkPolicy.WorkingType.Driver, workers);
            }
        }
        return decodedWorkTeam;
    }

    private static Map<WorkPolicy.WorkingType, List<Integer>> decodeWorkingTeam(String workTeam){
        // get the work team
        Map<WorkPolicy.WorkingType, List<Worker>> team = decodeWorkTeam(workTeam);
        Map<WorkPolicy.WorkingType, List<Integer>> output = new HashMap<>();

        // loop through the actual working team and get the id's
        for(Map.Entry<WorkPolicy.WorkingType, List<Worker>> entry : team.entrySet())
            output.put(entry.getKey(), entry.getValue().stream().map(Worker::getId).collect(Collectors.toList()));

        return output;
    }


}
