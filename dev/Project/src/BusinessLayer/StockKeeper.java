package BusinessLayer;

import javafx.util.Pair;

import java.time.DayOfWeek;
import java.util.Map;

public class StockKeeper extends Worker {

    public StockKeeper(int id, String name, Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule, WorkerDeal contract)
    {
        super(id , name , WorkPolicy.WorkingType.StockKeeper ,schedule , contract);
    }
}
