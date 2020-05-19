package DAL;

import BL.*;
import BL.Driver;
import javafx.util.Pair;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.Date;




public class ShiftDAO implements DAO<Shift> {

    @Override
    public Shift get(int id) {

        Shift shift;

        Date shift_date;
        Worker boss;
        Shift.ShiftTime shift_time;
        Map<WorkPolicy.WorkingType, List<Integer>> work_team;

        String sql = "SELECT * FROM Shifts WHERE id = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery(sql);

            // loop through the result set
            if (rs.next()) {
                shift_date = rs.getDate("date");
                StockKeeper s = getStockKeeper(rs.getInt("boss")).getResult();
                boss = s != null ? s : getDriver(rs.getInt("boss")).getResult();
                shift_time = rs.getString("time").compareTo("Morning") == 0 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening;
                work_team = decodeWorkTeam(rs.getString("workTeam"));
                Address address = (new AddressDAO()).get(rs.getString("address"));

                shift = new Shift(address, shift_date, shift_time, boss, work_team);
                shift.setShiftID(id);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return shift;
    }

    @Override
    public List<Shift> getAll() {

        List<Shift> shifts = new LinkedList<>();
        Shift tmpShift;

        int shift_id = 0;
        Date shift_date;
        Worker boss;
        Shift.ShiftTime shift_time;
        Map<WorkPolicy.WorkingType, List<Worker>> work_team;

        String sql = "SELECT * FROM Shifts";

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                shift_id = rs.getInt("id");
                shift_date = rs.getDate("date");
                boss = getStockKeeper(rs.getInt("boss")).getResult();
                shift_time = rs.getString("time").compareTo("Morning") == 0 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening;
                work_team = decodeWorkTeam(rs.getString("workTeam"));

                tmpShift = new Shift(shift_date, shift_time, boss, work_team);
                tmpShift.setShiftID(shift_id);

                shifts.add(tmpShift);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return shifts;
    }

    @Override
    public void save(Shift shift) {

        String sql = "INSERT INTO StockKeepers(id, date, boss, time, workTeam) VALUES(?, ?, ?, ?, ?)";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

        int id = shift.getShiftId();
        String shift_date = sdf.format(shift.getShiftDate());
        int bossId = shift.getBoss().getId();
        String shift_time = shift.getShiftTime() == Shift.ShiftTime.Morning ? "Morning" : "Evening";
        String work_team = encodeWorkTeam(shift.getWorkingTeam());

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, shift_date);
            pstmt.setInt(3, bossId);
            pstmt.setString(4, shift_time);
            pstmt.setString(5, work_team);

            pstmt.executeUpdate();
        } catch (SQLException ignored)
        {
        }
    }

    @Override
    public void update(Shift shift, String[] params) {

    }

    @Override
    public void delete(Shift shift) {

        String sql = "DELETE FROM Shifts WHERE id = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, shift.getShiftId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }


    private String encodeWorkTeam(Map<WorkPolicy.WorkingType, List<Worker>> workingTeam) {
        String encodedWorkingTeam = "";

        for (Map.Entry<WorkPolicy.WorkingType, List<Worker>> entry : workingTeam.entrySet()) {
            encodedWorkingTeam += entry.getKey() == WorkPolicy.WorkingType.StockKeeper ? 0 : 1;
            encodedWorkingTeam += ",";

            for (Worker worker : entry.getValue())
                encodedWorkingTeam += worker.getId() + ",";

            encodedWorkingTeam += "\n";
        }

        return encodedWorkingTeam;
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
                        contract = getDeal(id).getResult();

                        stockKeeper = new StockKeeper(id, name, WorkPolicy.WorkingType.StockKeeper, schedule, contract);

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
                        contract = getDeal(id).getResult();

                        driver = new Driver(id, name, WorkPolicy.WorkingType.Driver, schedule, contract, license);

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
        // TODO


        return null;
    }



}