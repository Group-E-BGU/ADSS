package BL;

import BL.WorkPolicy.WorkingType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Shift {

    public enum ShiftTime {
        Morning,
        Evening
    }

    private static int count = 0;
    private int shift_id = 0;
    private Date shift_date;
    private Worker boss;
    private ShiftTime shift_time;
    private Map<WorkingType, List<Worker>> work_team;   // should be a multimap

    public Map<WorkingType, List<Worker>> getWorkingTeam() {
        return work_team;
    }

    // should we remove the argument work_team from the constructor ?
    public Shift(Date shift_date, ShiftTime shift_time, Worker boss, Map<WorkingType, List<Worker>> work_team) {
        this.shift_date = shift_date;
        this.boss = boss;
        this.shift_time = shift_time;
        this.work_team = work_team;
        this.shift_id = count++;
    }


    public boolean addToWorkingTeam(Worker worker, WorkingType workingType) {

        if (!worker.isAvailable(shift_date, shift_time))
            return false;

        worker.work(this);
        if (!work_team.containsKey(workingType))
            work_team.put(workingType, new LinkedList<>());

        work_team.get(workingType).add(worker);
        return true;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String shift_string = "shift id : " + shift_id + '\n';
        shift_string = shift_string + "shift date : " + dateFormat.format(shift_date) + '\n';
        dateFormat = new SimpleDateFormat("EEEE");
        shift_string = shift_string + "shift type : " + dateFormat.format(shift_date) + " , " + shift_time + '\n';
        shift_string = shift_string + "shift boss : " + boss.getName() + '\n';
        shift_string = shift_string + "Working team :- " + '\n' + "{\n";
        for (WorkingType workingType : work_team.keySet()) {
            shift_string = shift_string + workingType + " staff : " + '\n';
            for (Worker worker : work_team.get(workingType)) {
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

    // in case if the boss can be changed
    public void setBoss(Worker boss) {
        this.boss = boss;
    }
}
