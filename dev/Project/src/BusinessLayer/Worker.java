package BusinessLayer;

import javafx.util.Pair;

import java.time.DayOfWeek;
import java.util.*;

import BusinessLayer.Shift.ShiftTime;
import BusinessLayer.WorkPolicy.WorkingType;

public abstract class Worker {

    private int id;
    private String name;
    private WorkingType type;
    private Map<Pair<DayOfWeek, ShiftTime>, Boolean> schedule;
    private WorkerDeal contract;
    private List<Integer> worker_shifts;   // Shift_id

    public Worker(int id, String name, WorkingType type, Map<Pair<DayOfWeek, ShiftTime>, Boolean> schedule, WorkerDeal contract) {

        this.name = name;
        this.id = id;
        this.type = type;
        this.schedule = schedule;
        this.contract = contract;
//        System.out.println(contract.toString());
        this.worker_shifts = new LinkedList<>();
    }

    public List<Integer> getWorker_shifts() {
        return worker_shifts;
    }

/*    public List<Pair<Day , ShiftTime>> availableHours()
    {
        List<Pair<Day,ShiftTime>> available_hours = new LinkedList<>();
        for(Pair<Day , ShiftTime> p : schedule.keySet())
        {
            if(schedule.get(p))
                available_hours.add(p);
        }
        return available_hours;
    }

 */

    @Override
    public String toString() {
        return new String(id + " , " + name + " : " + type.toString());
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public WorkingType getType() {
        return type;
    }

    public void setType(WorkingType type) {
        this.type = type;
    }

    public Map<Pair<DayOfWeek, ShiftTime>, Boolean> getSchedule() {
        return schedule;
    }

    public WorkerDeal getContract() {
        return contract;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setWorkerShifts(List<Integer> worker_shifts)
    {
        this.worker_shifts = worker_shifts;
    }

}
