package BL;

import javafx.util.Pair;

import java.time.DayOfWeek;
import java.util.*;

public class BLService {

    private History history;
    private Workers workers;

    public BLService() {
        history = History.getInstance();
        workers = Workers.getInstance();

    }
//------------------------------------ Workers --------------------------------//

    public List<Worker> getAvailableWorkers(Date date, Shift.ShiftTime time) {
        List<Worker> available_workers = new LinkedList<>();
        for (Worker w : workers.getAllWorkers().values()) {
            if (isAvailable(w, date, time)) {
                available_workers.add(w);
            }
        }

        return available_workers;
    }

    public List<Worker> getAvailableWorkers(Date date, Shift.ShiftTime time, WorkPolicy.WorkingType job) {
        List<Worker> available_workers = new LinkedList<>();
        for (Worker w : workers.getAllWorkers().values()) {
            if (w.getType() == job && isAvailable(w, date, time)) {
                available_workers.add(w);
            }
        }

        return available_workers;
    }

    public Worker getWorker(int worker_id) {
        if (!workers.getAllWorkers().containsKey(worker_id))
            return null;
        return workers.getAllWorkers().get(worker_id);
    }

    public boolean addWorker(Worker worker) {
        if (workers.getAllWorkers().containsKey(worker.getId())) {
            return false;
        }

        workers.getAllWorkers().put(worker.getId(), worker);

        // add DAL
        return true;
    }

    public boolean updateWorker(Worker worker) {

        // DAL

        return true;
    }

    public boolean isAvailable(Worker worker, Date date, Shift.ShiftTime shiftTime) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }//to make sure that the day index is correct

        Pair<DayOfWeek, Shift.ShiftTime> pair = new Pair<>(DayOfWeek.of(dayOfWeek), shiftTime);
        if (worker.getSchedule().get(pair)) {
            for (Shift shift : worker.getWorker_shifts()) {
                if (shift.getShiftDate() == date && shift.getShiftTime() == shiftTime) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public String AvilableWorkerstoString(Date date, Shift.ShiftTime shiftTime) {
        String workers_string = "";
        for (Worker worker : Workers.getInstance().getAllWorkers().values()) {
            if (isAvailable(worker, date, shiftTime)) {
                workers_string = workers_string + worker.toString() + '\n';
            }
        }
        if (workers_string.equals("")) {
            return workers_string;
        }
        workers_string = workers_string.substring(0, workers_string.length() - 1);
        return workers_string;
    }

    public boolean work(Worker worker, Shift shift) {
        if (isAvailable(worker, shift.getShiftDate(), shift.getShiftTime())) {
            worker.getWorker_shifts().add(shift);
            return true;
        }

        return false;
    }

//------------------------------------ Shifts --------------------------------//

    public Map<Integer, Shift> getAllShifts() {
        return history.getShifts();
    }

    public Shift getShift(int shift_id) {
        return history.getShifts().get(shift_id);
    }

    public boolean addShift(Shift shift) {
        for (Shift s : history.getShifts().values()) {
            if (s.getShiftDate().equals(shift.getShiftDate()) && s.getShiftTime() == shift.getShiftTime()) {
                return false;
            }
        }

        history.getShifts().put(shift.getShiftId(), shift);

        // add DAL

        return true;
    }

    public boolean addToWorkingTeam(Shift shift, Worker worker, WorkPolicy.WorkingType workingType) {

        if (!isAvailable(worker, shift.getShiftDate(), shift.getShiftTime()) || worker.getType() != workingType)
            return false;

        work(worker, shift);
        if (!shift.getWorkingTeam().containsKey(workingType))
            shift.getWorkingTeam().put(workingType, new LinkedList<>());

        shift.getWorkingTeam().get(workingType).add(worker);
        return true;
    }
//------------------------------ WorkerDeal --------------------------//
    public boolean updateContract(int worker_id, WorkerDeal contract) {

        // DAL

        return true;

    }
}
