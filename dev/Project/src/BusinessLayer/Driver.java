package BusinessLayer;

import javafx.util.Pair;

import java.time.DayOfWeek;
import java.util.Map;

public class Driver extends Worker {

    private String license;

    public Driver(int id, String name, Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule, WorkerDeal contract , String license)
    {
        super(id , name , WorkPolicy.WorkingType.Driver ,schedule , contract);
        this.license = license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }
    public String getLicense()
    {
        return license;
    }

}
