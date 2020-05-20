package DAL;

import BL.Driver;
import BL.*;
import javafx.util.Pair;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;


public class ShiftDAO {


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
                String stringDate = rs.getString("date");
                shift_date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
                StockKeeper s = (new StockKeeperDAO()).get(rs.getInt("boss"));
                boss = s != null ? s : (new DriverDAO()).get(rs.getInt("boss"));
                shift_time = rs.getString("time").compareTo("Morning") == 0 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening;
                work_team = decodeWorkingTeam(rs.getString("workTeam"));
                Address address = (new AddressDAO()).get(rs.getString("address"));

                shift = new Shift(address, shift_date, shift_time, boss, work_team);
                shift.setShiftID(id);

                return shift;
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return null;
    }


    public List<Shift> getAll() {

        List<Shift> shifts = new LinkedList<>();
        Shift tmpShift;

        int shift_id = 0;
        Date shift_date;
        Worker boss;
        Shift.ShiftTime shift_time;
        Map<WorkPolicy.WorkingType, List<Integer>> work_team;

        String sql = "SELECT * FROM Shifts";

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                shift_id = rs.getInt("id");
                String stringDate = rs.getString("date");
                shift_date = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);

                boss = (new StockKeeperDAO()).get(rs.getInt("boss"));
                boss = boss != null ? boss : (new DriverDAO()).get(rs.getInt("boss"));
                shift_time = rs.getString("time").compareTo("Morning") == 0 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening;
                work_team = decodeWorkingTeam(rs.getString("workTeam"));
                Address address = (new AddressDAO()).get(rs.getString("address"));

                tmpShift = new Shift(address, shift_date, shift_time, boss, work_team);
                tmpShift.setShiftID(shift_id);

                shifts.add(tmpShift);
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return shifts;
    }


    public void save(Shift shift) {

        String sql = "INSERT INTO Shifts(id, date, boss, time, workTeam,address) VALUES(?, ?, ?, ?, ?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

        int id = shift.getShiftId();
        Date date; // your date
        // Choose time zone in which you want to interpret your Date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(shift.getShiftDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH) + 1;

        int bossId = shift.getBoss().getId();
        String shift_time = shift.getShiftTime() == Shift.ShiftTime.Morning ? "Morning" : "Evening";
        String work_team = encodeWorkTeam(shift.getWorkingTeam());
        String shift_date = day + "/" + month + "/" + year;
        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, shift_date);
            pstmt.setInt(3, bossId);
            pstmt.setString(4, shift_time);
            pstmt.setString(5, work_team);
            pstmt.setString(6,shift.getAddress().getLocation());

            pstmt.executeUpdate();
        } catch (SQLException ignored)
        {
        }
    }


    public void update(Shift shift) {
        String sql = "UPDATE Shifts SET date = ? , boss = ? , time = ? , workTeam = ? , address = ? WHERE id = ?";

        int id = shift.getShiftId();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(shift.getShiftDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH) + 1;

        int bossId = shift.getBoss().getId();
        String shift_time = shift.getShiftTime() == Shift.ShiftTime.Morning ? "Morning" : "Evening";
        String work_team = encodeWorkTeam(shift.getWorkingTeam());
        String shift_date = day + "/" + month + "/" + year;
        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, shift_date);
            pstmt.setInt(2, bossId);
            pstmt.setString(3, shift_time);
            pstmt.setString(4, work_team);
            pstmt.setString(5,shift.getAddress().getLocation());
            pstmt.setInt(6,id);
            pstmt.executeUpdate();
        } catch (SQLException ignored)
        {
        }
    }


    public void delete(Shift shift) {

        String sql = "DELETE FROM Shifts WHERE id = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, shift.getShiftId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
        }


    }


    private String encodeWorkTeam(Map<WorkPolicy.WorkingType, List<Integer>> workingTeam) {
        String encodedWorkingTeam = "";

        for (Map.Entry<WorkPolicy.WorkingType, List<Integer>> entry : workingTeam.entrySet()) {
            encodedWorkingTeam += entry.getKey() == WorkPolicy.WorkingType.StockKeeper ? 0 : 1;
            encodedWorkingTeam += ",";

            for (Integer workerId : entry.getValue())
                encodedWorkingTeam += workerId + ",";


            encodedWorkingTeam += "\n";
        }

        if(encodedWorkingTeam.compareTo("")!=0)
        {
            encodedWorkingTeam = encodedWorkingTeam.substring(0,encodedWorkingTeam.length()-1);
        }
        if(encodedWorkingTeam.charAt(encodedWorkingTeam.length()-1) == ',')
            encodedWorkingTeam = encodedWorkingTeam.substring(0, encodedWorkingTeam.length() - 1);
        return encodedWorkingTeam;
    }

    private static Map<WorkPolicy.WorkingType, List<Worker>> decodeWorkTeam(String workTeam) {
        Map<WorkPolicy.WorkingType, List<Worker>> decodedWorkTeam = new HashMap<>();
        String[] separatedWorkTeams = workTeam.split("\n");
        String[] tmpTeam;
        List<Worker> workers;

        for (String team : separatedWorkTeams) {
            tmpTeam = team.split(",");
            if(tmpTeam.length > 1)
                tmpTeam = Arrays.copyOfRange(tmpTeam, 1, tmpTeam.length);

            workers = new LinkedList<>();

            if(!team.isEmpty())
            {
                if (team.charAt(0) == '0') {
                    // the team are stock keepers
                    int id;
                    String name;
                    Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule;
                    WorkerDeal contract;
                    StockKeeper stockKeeper;

                    StringBuilder sql = new StringBuilder("SELECT * FROM StockKeepers WHERE id IN (");

                    for (int i = 0; i < tmpTeam.length ; i++) {
                        sql.append(tmpTeam[i]).append(",");
                        if(i == tmpTeam.length-1)
                            sql.append(tmpTeam[tmpTeam.length - 1 ]).append(")");
                    }

                    try (Connection conn = DAL.connect();
                         Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(sql.toString())) {

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

                    StringBuilder sql = new StringBuilder("SELECT * FROM Drivers WHERE id IN (");

                    for (int i = 0; i < tmpTeam.length; i++) {
                        sql.append(tmpTeam[i]).append(",");
                        if(i == tmpTeam.length-1)
                            sql.append(tmpTeam[tmpTeam.length - 1 ]).append(")");
                    }

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

    private static Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> decodeSchedule(String schedule){
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> decodedSchedule = new HashMap<>();
        String[] separatedDays = schedule.split("\n");

        for (String separatedDay : separatedDays) {
            decodedSchedule.put(new Pair<DayOfWeek, Shift.ShiftTime>(DayOfWeek.of(separatedDay.charAt(0) - 48), separatedDay.charAt(1) == 0 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening), separatedDay.charAt(2) == 1);
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

        StringBuilder sql = new StringBuilder("SELECT * FROM Shifts WHERE id IN (");

        for (int i = 0; i < shiftsIds.length - 1; i++) {
            sql.append(shiftsIds[i]).append(",");
        }

        sql.substring(0, sql.length() - 1);

        sql.append(shiftsIds[shiftsIds.length - 1]).append(")");

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {

            // loop through the result set
            while (rs.next()) {
                shift_id = rs.getInt("id");
                shift_date = rs.getDate("date");
                boss = (new StockKeeperDAO()).get(rs.getInt("boss"));
                boss = boss != null ? boss : (new DriverDAO()).get(rs.getInt("boss"));
                shift_time = rs.getString("time").compareTo("Morning") == 0 ? Shift.ShiftTime.Morning : Shift.ShiftTime.Evening;
                work_team = decodeWorkingTeam(rs.getString("workTeam"));
                Address address = (new AddressDAO()).get(rs.getString("address"));

                tmpShift = new Shift(address, shift_date, shift_time, boss, work_team);
                tmpShift.setShift_id(shift_id);

                decodedShifts.add(tmpShift);
            }

        } catch (SQLException e) {
            return null;
        }
        return decodedShifts;
    }


}