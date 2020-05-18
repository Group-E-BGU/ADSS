package BL;

import java.util.Map;

public class BLService {

private History history;
private Workers workers;

public BLService()
{
    history = History.getInstance();
    workers = Workers.getInstance();

}

//------------------------------------ Shifts --------------------------------//

    public Map<Integer,Shift> getAllShifts()
    {
        return history.getShifts();
    }

    public Shift getShift(int shift_id)
    {
        return history.getShifts().get(shift_id);
    }

    public boolean addShift(Shift shift)
    {
        for (Shift s : history.getShifts().values()) {
            if (s.getShiftDate().equals(shift.getShiftDate()) && s.getShiftTime() == shift.getShiftTime()) {
                return false;
            }
        }

        history.getShifts().put(shift.getShiftId() , shift);

        // add DAL

        return true;
    }
}
