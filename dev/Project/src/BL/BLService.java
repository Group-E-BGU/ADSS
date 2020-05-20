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

    public List<Integer> getAvailableWorkers(Date date, Shift.ShiftTime time) {
        List<Integer> available_workers = new LinkedList<>();
        for (Worker w : workers.getAllWorkers().values()) {
            if (isAvailable(w.getId(), date, time)) {
                available_workers.add(w.getId());
            }
        }

        return available_workers;
    }

    public List<Integer> getAvailableWorkers(Date date, Shift.ShiftTime time, WorkPolicy.WorkingType job) {
        List<Integer> available_workers = new LinkedList<>();
        for (Worker w : workers.getAllWorkers().values()) {
            if (w.getType() == job && isAvailable(w.getId(), date, time)) {
                available_workers.add(w.getId());
            }
        }

        return available_workers;
    }

    public Worker getWorker(int worker_id) {
        if (!workers.getAllWorkers().containsKey(worker_id))
            return null;
        return workers.getAllWorkers().get(worker_id);
    }

    public List<Integer> getDeliveryDriver(Date shiftDate, Shift.ShiftTime shiftTime, String license)
    {
        List<Integer> delivery_drivers = new LinkedList<>();
        List<Integer> potential_drivers = getAvailableWorkers(shiftDate,shiftTime, WorkPolicy.WorkingType.Driver);
        for(Integer driver_id : potential_drivers)
        {
            if(((Driver)getWorker(driver_id)).getLicense().equals(license))
            {
                delivery_drivers.add(driver_id);
            }
        }

        return delivery_drivers;
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
        if(worker.getType() == WorkPolicy.WorkingType.StockKeeper)
            new StockKeeperDAO().update((StockKeeper) worker);
        else
            new DriverDAO().update((Driver) worker);

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


    public boolean isAvailable(int worker_id, Date date, Shift.ShiftTime shiftTime) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }//to make sure that the day index is correct

        Pair<DayOfWeek, Shift.ShiftTime> pair = new Pair<>(DayOfWeek.of(dayOfWeek), shiftTime);
        if (getWorker(worker_id).getSchedule().get(pair)) {
            for (Integer shift_id : getWorker(worker_id).getWorker_shifts()) {
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
            if (isAvailable(worker.getId(), date, shiftTime)) {
                workers_string = workers_string + worker.toString() + '\n';
            }
        }
        if (workers_string.equals("")) {
            return workers_string;
        }
        workers_string = workers_string.substring(0, workers_string.length() - 1);
        return workers_string;
    }

    public boolean work(Integer worker_id, Integer shift_id) {
        if (isAvailable(worker_id, getShift(shift_id).getShiftDate(), getShift(shift_id).getShiftTime())) {
            getWorker(worker_id).getWorker_shifts().add(shift_id);
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
            if (shift.getAddress().getLocation().equals(address.getLocation()))
            {
                if(shift.getShiftDate().equals(date))
                {
                    if(shift.getShiftTime().equals(shiftTime))
                    {
                        return shift;
                    }
                }
            }
           //     return shift;
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

    public boolean addToWorkingTeam(Integer shift_id, Integer worker_id, WorkPolicy.WorkingType workingType) {

        if (!isAvailable(worker_id, getShift(shift_id).getShiftDate(), getShift(shift_id).getShiftTime()) || getWorker(worker_id).getType() != workingType)
            return false;

        work(worker_id, shift_id);
        if (!getShift(shift_id).getWorkingTeam().containsKey(workingType))
            getShift(shift_id).getWorkingTeam().put(workingType, new LinkedList<>());

        getShift(shift_id).getWorkingTeam().get(workingType).add(worker_id);

        // new ShiftDAO().update();
        if(getWorker(worker_id).getType()== WorkPolicy.WorkingType.Driver)
        {
            new DriverDAO().update((Driver)getWorker(worker_id));
        }
        else if(getWorker(worker_id).getType() == WorkPolicy.WorkingType.StockKeeper)
        {
            new StockKeeperDAO().update((StockKeeper)getWorker(worker_id));
        }
        return true;
    }

    public boolean stockKeeperAvailable(Shift shift) {
        return (shift.getWorkingTeam().get(WorkPolicy.WorkingType.StockKeeper) != null && !shift.getWorkingTeam().get(WorkPolicy.WorkingType.StockKeeper).isEmpty());
    }

    //------------------------------ WorkerDeal --------------------------//
    public boolean updateContract(int worker_id, WorkerDeal contract) {
        contract.setWorker_id(worker_id);

        new WorkerDealDAO().update(contract);

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

    public List<String> getAvailableTrucks(Date date, Shift.ShiftTime delivery_time)
    {

        List<String> available_trucks = new LinkedList<>();
        for(Truck truck : getAllTrucks().values())
        {
            if(truckIsAvailable(truck.getSerialNumber(),date,delivery_time))
            {
                available_trucks.add(truck.getSerialNumber());
            }
        }

        return available_trucks;
    }

    public boolean truckIsAvailable(String truck_serial , Date date , Shift.ShiftTime shift_time)
    {
        for(Delivery delivery : getAllDeliveries().values())
        {
            if(delivery.getTruckSerialNumber().equals(truck_serial))
            {
                return false;
            }
        }

        return true;
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

        List<StockKeeper> stockKeepers = new StockKeeperDAO().getAll();
        List<Driver> drivers = new DriverDAO().getAll();
        Map<Integer,Worker> workers_map = new HashMap<>();

        for(StockKeeper sk : stockKeepers)
        {
            workers_map.put(sk.getId(),sk);
        }
        
        for(Driver d : drivers)
        {
            workers_map.put(d.getId(),d);
        }

        workers.setWorkers(workers_map);

        List<Shift> shifts = new ShiftDAO().getAll();

        Map<Integer,Shift> shifts_map = new HashMap<>();

        for(Shift shift : shifts)

        {
            shifts_map.put(shift.getShiftId(),shift);
        }

        history.setShifts(shifts_map);

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

        List<Delivery> deliveries = new DeliveryDAO().getAll();
        Map<Integer,Delivery> deliveryMap = new HashMap<>();

        for(Delivery delivery : deliveries)
        {
            deliveryMap.put(delivery.getDeliveryID(),delivery);
        }

        data.setDeliveries(deliveryMap);


        return true;
    }

    public Delivery arrangeDelivery(String source, Map<String, Document> documents) {
        Delivery delivery = new Delivery();
        int totalWeight = getTotalWeight(documents);
        Truck truck = Data.getInstance().getProperTruck(totalWeight);
        Driver driver = Data.getInstance().getProperDriver(totalWeight);
        Date date= null;

        delivery.setTruckSerialNumber(truck.getSerialNumber());
        delivery.setDriverID(driver.getId());
        delivery.setDocuments(documents);
        delivery.setSource(source);

        if(totalWeight <= truck.getMaxAllowedWeight())
            date = getDeliveryDate(driver);

        delivery.setDate(date);
        delivery.setDeliveryId(new DeliveryDAO().save(delivery));

        return delivery;
    }

    private Date getDeliveryDate(Driver driver) {
        // todo
        // returns the first date that the driver could deliver the delivery in
        return null;
    }

    private int getTotalWeight(Map<String, Document> deliveryGoods) {
        return -1;
        // todo
    }

    public void rearrangeDelivery(Delivery delivery, Map<String, Document> documents) {
        int totalWeight = getTotalWeight(documents);

        if(totalWeight <= Data.getInstance().getTrucks().get(delivery.getTruckSerialNumber()).getMaxAllowedWeight())
            delivery.setDate(getDeliveryDate((new DriverDAO()).get(delivery.getDriverID())));
    }


    public Map<Integer,Delivery> getAllDeliveries()
    {
        return data.getDeliveries();
    }

    public Delivery getDelivery(int id)
    {
        return data.getDeliveries().get(id);
    }

    public boolean addDelivery(Delivery delivery)
    {
        data.getDeliveries().put(delivery.getDeliveryID(),delivery);
        new DeliveryDAO().save(delivery);
        return true;

    }
    public boolean updateShift(Shift shift) {
        new ShiftDAO().update(shift);
        return true;
    }
}
