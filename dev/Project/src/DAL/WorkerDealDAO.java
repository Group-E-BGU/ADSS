package DAL;

import BL.WorkerDeal;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class WorkerDealDAO {


    public WorkerDeal get(int id)
    {

        WorkerDeal deal = null;

        int worker_id;
        Date start_date;
        String bank_address;
        double salary;
        List<String> work_conditions;

        String sql = "SELECT FROM Work_Deals WHERE workerId = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery(sql);
            // loop through the result set
            if (rs.next()) {
                worker_id = rs.getInt("workerId");
                start_date = rs.getDate("startDate");
                bank_address = rs.getString("bankAddress");
                salary = rs.getDouble("salary");
                work_conditions = decodeWorkConditions(rs.getString("workConditions"));

                deal = new WorkerDeal(worker_id, start_date, salary, bank_address, work_conditions);

            } else
                System.out.println("No deal with worker id of: " + id + " is found");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return deal;
    }

    public List<WorkerDeal> getAll() {


        List<WorkerDeal> deals = new LinkedList<>();
        WorkerDeal tmpDeal;

        String sql = "SELECT * FROM Work_Deals";

        int worker_id;
        Date start_date;
        String bank_address;
        double salary;
        List<String> work_conditions;

        try (Connection conn = DAL.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                worker_id = rs.getInt("workerId");
                start_date = rs.getDate("startDate");
                bank_address = rs.getString("bankAddress");
                salary = rs.getDouble("salary");
                work_conditions = decodeWorkConditions(rs.getString("workConditions"));

                tmpDeal = new WorkerDeal(worker_id, start_date, salary, bank_address, work_conditions);

                deals.add(tmpDeal);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return deals;
    }

    public void save(WorkerDeal workerDeal)
    {

        String sql = "INSERT INTO Work_Deals(workerId, startDate, bankAddress, salary, workConditions) VALUES(?, ?, ?, ?, ?)";

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

        int workerId = workerDeal.getWorker_id();
        String startDate = sdf.format(workerDeal.getStart_date());
        String bankAddress = workerDeal.getBankAddress();
        double salary = workerDeal.getSalary();
        String workConditions = encodeWorkConditions(workerDeal.getWork_conditions());

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, workerId);
            pstmt.setString(2, startDate);
            pstmt.setString(3, bankAddress);
            pstmt.setDouble(4, salary);
            pstmt.setString(5, workConditions);
            pstmt.executeUpdate();

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void update(WorkerDeal workerDeal, String[] params)
    {


    }

    public void delete(WorkerDeal workerDeal)
    {

        String sql = "DELETE FROM Work_Deals WHERE workerId = ?";

        try (Connection conn = DAL.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, workerDeal.getWorker_id());
            pstmt.executeUpdate();

            System.out.println("Deal with worker id: " + workerDeal.getWorker_id() + " has been removed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public static List<String> decodeWorkConditions(String workConditions) {
        List<String> decodedWorkConditions = new LinkedList<>();
        String[] splicedLogs = workConditions.split("\n");

        Collections.addAll(decodedWorkConditions, splicedLogs);

        return decodedWorkConditions;
    }

    private static String encodeWorkConditions(List<String> work_conditions) {
        StringBuilder output = new StringBuilder();

        for (String log : work_conditions)
            output.append(log).append('\n');

        return output.toString();
    }
}
