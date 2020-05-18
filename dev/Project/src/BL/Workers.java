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

}
