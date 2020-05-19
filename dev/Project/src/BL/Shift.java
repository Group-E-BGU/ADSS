package BL;

import BL.WorkPolicy.WorkingType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Shift {

    public enum ShiftTime{
        Morning,
        Evening

    }

    private static int count = 0;
    private int shift_id = 0;
    private Address address;
    private Date shift_date;
    private Worker boss;
    private ShiftTime shift_time;
    private Map<WorkingType, List<Integer>> work_team;   // list of ids

    public Map<WorkingType, List<Integer>> getWorkingTeam() {
        return work_team;
    }

    public Shift(Address address , Date shift_date, ShiftTime shift_time, Worker boss, Map<WorkingType, List<Integer>> work_team) {
        this.address = address;
        this.shift_date = shift_date;
        this.boss = boss;
        this.shift_time = shift_time;
        this.work_team = work_team;
        this.shift_id = count++;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String shift_string = "shift id : " + shift_id + '\n';
        shift_string = shift_string +"Address : "+address.getLocation() +'\n';
        shift_string = shift_string + "shift date : " + dateFormat.format(shift_date) + '\n';
        dateFormat = new SimpleDateFormat("EEEE");
        shift_string = shift_string + "shift type : " + dateFormat.format(shift_date) + " , " + shift_time + '\n';
        shift_string = shift_string + "shift boss : " + boss.getName() + '\n';
        shift_string = shift_string + "Working team :- " + '\n' + "{\n";
        for (WorkingType workingType : work_team.keySet()) {
            shift_string = shift_string + workingType + " staff : " + '\n';
            for (Integer worker_id : work_team.get(workingType)) {
                Worker worker = Workers.getInstance().getAllWorkers().get(worker_id);
                shift_string = shift_string + worker.toString() + '\n';
            }
        }

        shift_string = shift_string + "}\n";

        return shift_string;
    }

    public Date getShiftDate() {
        return shift_date;
    }

    public int getShiftId() {
        return shift_id;
    }

    public ShiftTime getShiftTime() {
        return shift_time;
    }

    public Worker getBoss() {
        return boss;
    }

    public Address getAddress()
    {
        return address;
    }


    // in case if the boss can be changed
    public void setBoss(Worker boss) {
        this.boss = boss;
    }

    public void setShiftID(int shift_id)
    {
        this.shift_id = shift_id;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public void setShift_id(int shift_id) {
        this.shift_id = shift_id;
    }
}
