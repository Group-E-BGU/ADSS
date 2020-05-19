package BL;

import DAL.*;
import DAL.ShiftDAO;
import javafx.util.Pair;

import java.time.DayOfWeek;
import java.util.*;

public class BLService {

    private History history;
    private Workers workers;
    private Data data;
    private DAL dal;

    public BLService() {
        history = History.getInstance();
        workers = Workers.getInstance();
        data = Data.getInstance();
        dal = new DAL();

    }
//------------------------------------ Workers --------------------------------//


    public Map<Integer,Worker> getAllWorkers()
    {
        return workers.getAllWorkers();
    }

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

        WorkPolicy.WorkingType working_type = worker.getType();

        switch (working_type)
        {
            case StockKeeper:
                new StockKeeperDAO().save((StockKeeper)worker);
                break;
            case Driver:
                new DriverDAO().save((Driver)worker);
                break;
        }


        return true;
    }

    public boolean updateWorker(Worker worker) {

        // DAL

        return true;
    }

    public boolean updateWorkerID(int old_id, int new_id) {

        getAllWorkers().put(new_id,getWorker(old_id));
        getAllWorkers().remove(old_id);
        Worker worker =getWorker(new_id);
        worker.setID(new_id);
        for(Integer shift_id : worker.getWorker_shifts())
        {
            Shift shift = getShift(shift_id);
            if(shift.getBoss().getId() != new_id)

            {
                shift.getWorkingTeam().get(worker.getType()).remove(old_id);
                shift.getWorkingTeam().get(worker.getType()).add(new_id);
            }
        }

        // check if he is a driver who has a delivery

        return true;
    }

    public boolean removeWorker(int worker_id) {


        return false;
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
            for (Integer shift_id : worker.getWorker_shifts()) {
                Shift shift = getShift(shift_id);
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
            worker.getWorker_shifts().add(shift.getShiftId());
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

    public Shift getShift(Address address, Date date, Shift.ShiftTime shiftTime) {
        for (Shift shift : getAllShifts().values()) {
            if (shift.getAddress().equals(address) && shift.getShiftDate().equals(date) && shift.getShiftTime() == shiftTime)
                return shift;
        }

        return null;
    }

    public boolean addShift(Shift shift) {
        for (Shift s : history.getShifts().values()) {
            if (s.getAddress().equals(shift.getAddress()) && s.getShiftDate().equals(shift.getShiftDate()) && s.getShiftTime() == shift.getShiftTime()) {
                return false;
            }
        }

        history.getShifts().put(shift.getShiftId(), shift);
        getWorker(shift.getBoss().getId()).getWorker_shifts().add(shift.getShiftId());


        ShiftDAO s = new ShiftDAO();
        s.save(shift);

        return true;
    }

    public boolean addToWorkingTeam(Shift shift, Worker worker, WorkPolicy.WorkingType workingType) {

        if (!isAvailable(worker, shift.getShiftDate(), shift.getShiftTime()) || worker.getType() != workingType)
            return false;

        work(worker, shift);
        if (!shift.getWorkingTeam().containsKey(workingType))
            shift.getWorkingTeam().put(workingType, new LinkedList<>());

        shift.getWorkingTeam().get(workingType).add(worker.getId());
        return true;
    }

    public boolean stockKeeperAvailable(Shift shift) {
        return (shift.getWorkingTeam().get(WorkPolicy.WorkingType.StockKeeper) != null && !shift.getWorkingTeam().get(WorkPolicy.WorkingType.StockKeeper).isEmpty());
    }

    //------------------------------ WorkerDeal --------------------------//
    public boolean updateContract(int worker_id, WorkerDeal contract) {

        // DAL

        return true;

    }

//------------------------------ Address --------------------------//

    public Map<String, Address> getAllAddresses() {
        return data.getAddresses();
    }

    public Address getAddress(String location) {
        return data.getAddresses().get(location);
    }

    public boolean addAddress(Address address) {

        data.getAddresses().put(address.getLocation(), address);
        new AddressDAO().save(address);

        return true;
    }

//------------------------------ Truck --------------------------//


    public Map<String,Truck> getAllTrucks()
    {
        return data.getTrucks();
    }

    public Truck getTruck(String serial_number)
    {
        return data.getTrucks().get(serial_number);
    }

    public boolean addTruck(Truck truck)
    {
        data.getTrucks().put(truck.getSerialNumber(),truck);
        new TruckDAO().save(truck);
        return true;
    }

//------------------------------ Product --------------------------//


    public Map<String,Product> getAllProducts()
    {
        return data.getProducts();
    }

    public Product getProduct(String cn)
    {
        return data.getProducts().get(cn);
    }

    public boolean addProduct(Product product)
    {
        data.getProducts().put(product.getCN(),product);
        new ProductDAO().save(product);
        return true;
    }


    public boolean loadFromDataBase()
    {
        List<Truck> trucks = new TruckDAO().getAll();
        Map<String,Truck> trucks_map = new HashMap<>();
        for(Truck truck : trucks)
        {
            trucks_map.put(truck.getSerialNumber(),truck);
        }

        data.setTrucks(trucks_map);

        List<Product> products = new ProductDAO().getAll();
        Map<String,Product> products_map = new HashMap<>();
        for(Product product : products)
        {
            products_map.put(product.getCN(),product);
        }

        data.setProducts(products_map);


        List<Address> addresses = new AddressDAO().getAll();
        Map<String,Address> addresses_map = new HashMap<>();
        for(Address address : addresses)
        {
            addresses_map.put(address.getLocation(),address);
        }

        data.setAddresses(addresses_map);

        return true;
    }
}
