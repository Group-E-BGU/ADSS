package PresentationLayer;

import BusinessLayer.*;
import javafx.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

public class InitializeData {


    public InitializeData()
    {

    }

    public void createWorkers() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            WorkerDeal john_contract = new WorkerDeal(111111111, dateFormat.parse("30/03/2017"), 28, "a", new LinkedList<>());
            Worker john = new Driver(111111111,"John", createSchedule(), john_contract,"a");

            WorkerDeal steve_contract = new WorkerDeal(222222222, dateFormat.parse("05/11/2016"), 30, "b", new LinkedList<>());
            Worker steve = new Driver(222222222,"Steve", createSchedule(), steve_contract , "b");


            WorkerDeal james_contract = new WorkerDeal(333333333, dateFormat.parse("12/06/2018"), 1000, "c", new LinkedList<>());
            Worker james = new StockKeeper(333333333,"James",  createSchedule(), james_contract);

            WorkerDeal moshe_contract = new WorkerDeal(444444444, dateFormat.parse("22/11/2015"), 1000, "c", new LinkedList<>());
            Worker moshe = new Driver(444444444,"Moshe", createSchedule(), moshe_contract,"c");


            WorkerDeal asd_contract = new WorkerDeal(555555555, dateFormat.parse("01/01/2012"), 1000, "c", new LinkedList<>());
            Worker asd = new StockKeeper(555555555,"Asd", createSchedule(), asd_contract);

            WorkerDeal iris_contract = new WorkerDeal(666666666, dateFormat.parse("17/02/2010"), 1000, "c", new LinkedList<>());
            Worker iris = new StockKeeper(666666666,"Iris", createSchedule(), iris_contract);


            Workers workers = Workers.getInstance();
            workers.addWorker(john);
            workers.addWorker(steve);
            workers.addWorker(james);
            workers.addWorker(moshe);
            workers.addWorker(asd);
            workers.addWorker(iris);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

    }

    public void createShifts() {
        History history = History.getInstance();
        SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Address a = new Address("Haifa","Shadi","0507483947");
            Address b = new Address("Tel Aviv","Eran","0547322165");
            Data.getInstance().getAddresses().put(a.getLocation(),a);
            Data.getInstance().getAddresses().put(b.getLocation(),b);
            Shift sundayMorning_shift = new Shift(a,date_format.parse("13/04/2020"), Shift.ShiftTime.Morning, BLService.getInstance().getWorker(111111111), new HashMap<>());
            Shift mondayEvening_shift = new Shift(b,date_format.parse("14/04/2020"), Shift.ShiftTime.Evening, BLService.getInstance().getWorker(222222222), new HashMap<>());
            history.getShifts().put(sundayMorning_shift.getShiftId(),sundayMorning_shift);
            history.getShifts().put(mondayEvening_shift.getShiftId(),mondayEvening_shift);
            Workers.getInstance().getAllWorkers().get(111111111).getWorker_shifts().add(sundayMorning_shift.getShiftId());
            Workers.getInstance().getAllWorkers().get(222222222).getWorker_shifts().add(mondayEvening_shift.getShiftId());
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

    }

    public Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> createSchedule() {

        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> schedule = new HashMap<>();


        List<Pair<DayOfWeek, Shift.ShiftTime>> shifts = new LinkedList<>();
        shifts.add(new Pair<>(DayOfWeek.SUNDAY, Shift.ShiftTime.Morning));
        shifts.add(new Pair<>(DayOfWeek.SUNDAY, Shift.ShiftTime.Evening));
        shifts.add(new Pair<>(DayOfWeek.MONDAY, Shift.ShiftTime.Morning));
        shifts.add(new Pair<>(DayOfWeek.MONDAY, Shift.ShiftTime.Evening));
        shifts.add(new Pair<>(DayOfWeek.TUESDAY, Shift.ShiftTime.Morning));
        shifts.add(new Pair<>(DayOfWeek.TUESDAY, Shift.ShiftTime.Evening));
        shifts.add(new Pair<>(DayOfWeek.WEDNESDAY, Shift.ShiftTime.Morning));
        shifts.add(new Pair<>(DayOfWeek.WEDNESDAY, Shift.ShiftTime.Evening));
        shifts.add(new Pair<>(DayOfWeek.THURSDAY, Shift.ShiftTime.Morning));
        shifts.add(new Pair<>(DayOfWeek.THURSDAY, Shift.ShiftTime.Evening));
        shifts.add(new Pair<>(DayOfWeek.FRIDAY, Shift.ShiftTime.Morning));
        shifts.add(new Pair<>(DayOfWeek.FRIDAY, Shift.ShiftTime.Evening));
        shifts.add(new Pair<>(DayOfWeek.SATURDAY, Shift.ShiftTime.Morning));
        shifts.add(new Pair<>(DayOfWeek.SATURDAY, Shift.ShiftTime.Evening));

        for (Pair<DayOfWeek, Shift.ShiftTime> pair : shifts) {
            schedule.put(pair, true);
        }
  /*      for(int i = 0; i < 4; i++) {
            int randome = (int) (Math.random()*shifts.size());
            Pair rand = shifts.get(randome);
            schedule.replace(rand, false);
        }

   */
        return schedule;
    }

}
