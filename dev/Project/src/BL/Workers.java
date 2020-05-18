package BL;

import BL.Shift.ShiftTime;
import BL.WorkPolicy.WorkingType;

import java.util.*;


// not-thread safe singleton design pattern
public class Workers {

    private static Workers workers_instance = null;
    private Map<Integer, Worker> workers_map;

    private Workers() {
        workers_map = new HashMap<>();
    }

    public static Workers getInstance() {
        if (workers_instance == null) {
            workers_instance = new Workers();
        }
        return workers_instance;
    }

    public boolean addWorker(Worker worker) {
        if (workers_map.containsKey(worker.getId()))
            return false;

        workers_map.put(worker.getId(),worker);
        return true;
    }

    public List<Worker> getAvailableWorkers(Date date, ShiftTime time, WorkingType job) {
        List<Worker> available_workers = new LinkedList<>();
        for (Worker w : workers_map.values()) {
            if (w.getType() == job && w.isAvailable(date, time)) {
                available_workers.add(w);
            }
        }

        return available_workers;
    }

    public List<Worker> getAvailableWorkers(Date date, ShiftTime time)
    {
        List<Worker> available_workers = new LinkedList<>();
        for (Worker w : workers_map.values()) {
            if (w.isAvailable(date, time)) {
                available_workers.add(w);
            }
        }

        return available_workers;
    }

    public Worker getWorker(int worker_id)
    {
        if(!workers_map.containsKey(worker_id))
            return null;
        return workers_map.get(worker_id);
    }

    public Map<Integer,Worker> getAllWorkers() {
        return workers_map;
    }

    @Override
    public String toString()
    {
        String workers_string="";
        for (Worker worker : Workers.getInstance().getAllWorkers().values()) {
            workers_string = workers_string+worker.toString()+'\n';
        }
        workers_string = workers_string.substring(0,workers_string.length()-1);
        return workers_string;
    }
    public String AvilableWorkerstoString(Date date, ShiftTime shiftTime)
    {
        String workers_string="";
        for (Worker worker : Workers.getInstance().getAllWorkers().values()) {
            if(worker.isAvailable(date,shiftTime)){
                workers_string = workers_string+worker.toString()+'\n';
            }
        }
        workers_string = workers_string.substring(0,workers_string.length()-1);
        return workers_string;
    }
}
