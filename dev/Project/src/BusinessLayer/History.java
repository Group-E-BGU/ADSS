package BusinessLayer;

import java.util.HashMap;
import java.util.Map;

public class History {

    private static History history_instance = null;

    private Map<Integer,Shift> shifts;


    public static History getInstance(){
        if(history_instance == null){
            history_instance = new History();
        }
        return history_instance;
    }

    private History()
    {
        shifts = new HashMap<>();
    }

    public Map<Integer,Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Map<Integer,Shift> shifts)
    {
        this.shifts = shifts;
    }

}
