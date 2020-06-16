package BusinessLayer;

import DataAccesslayer.*;
import PresentationLayer.Menu;
import javafx.util.Pair;

import java.time.DayOfWeek;
import java.util.*;

public class BLService {

    private History history;
    private Workers workers;
    private Data data;
    private DAL dal;
    private system systemcontroler;
    private User logged_user;
    private Store current_Store;

    public BLService() {
        history = History.getInstance();
        workers = Workers.getInstance();
        data = Data.getInstance();
        dal = new DAL();
        logged_user=null;
        current_Store=null;
        systemcontroler=new system();

    }
//------------------------------------ Workers --------------------------------//


    public Map<Integer, Worker> getAllWorkers() {
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

    public List<Integer> getDeliveryDriver(Date shiftDate, Shift.ShiftTime shiftTime, String license) {
        List<Integer> delivery_drivers = new LinkedList<>();
        List<Integer> potential_drivers = getAvailableWorkers(shiftDate, shiftTime, WorkPolicy.WorkingType.Driver);
        for (Integer driver_id : potential_drivers) {
            Driver driver = ((Driver) getWorker(driver_id));
            if (driver.getLicense().equals(license)) {
                boolean av = true;
                for(Integer shift_id : driver.getWorker_shifts())
                {
                    Shift shift = getShift(shift_id);
                    if(shift.getShiftDate().equals(shiftDate) && shift.getShiftTime().equals(shiftTime))
                    {
                        av = false;
                        break;
                    }
                }
                if(av)
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

        switch (working_type) {
            case StockKeeper:
                new StockKeeperDAO().save((StockKeeper) worker);
                break;
            case Driver:
                new DriverDAO().save((Driver) worker);
                break;
        }


        return true;
    }

    public boolean updateWorker(Worker worker) {
        if (worker.getType() == WorkPolicy.WorkingType.StockKeeper)
            new StockKeeperDAO().update((StockKeeper) worker);
        else
            new DriverDAO().update((Driver) worker);

        return true;
    }

    public boolean updateWorkerID(int old_id, int new_id) {

        getAllWorkers().put(new_id, getWorker(old_id));
        getAllWorkers().remove(old_id);
        Worker worker = getWorker(new_id);
        worker.setID(new_id);
        for (Integer shift_id : worker.getWorker_shifts()) {
            Shift shift = getShift(shift_id);
            if (shift.getBoss().getId() != new_id) {
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



        Map<Integer,Shift> shMap =  history.getShifts();
        for (Map.Entry<Integer,Shift>entry : shMap.entrySet())
        {
            if(entry.getValue().getShiftTime().equals(shiftTime) && entry.getValue().getShiftDate().equals(date) && (entry.getValue().getBoss().getId() == worker_id || entry.getValue().getWorkingTeam().values().contains(worker_id)))
                return false;
        }

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
            updateWorker(getWorker(worker_id));
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
            if (shift.getAddress().getLocation().equals(address.getLocation())) {
                if (shift.getShiftDate().equals(date)) {
                    if (shift.getShiftTime().equals(shiftTime)) {
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

        Shift sh = getShift(shift_id);

        if (!isAvailable(worker_id, getShift(shift_id).getShiftDate(), getShift(shift_id).getShiftTime()) || getWorker(worker_id).getType() != workingType)
            return false;

        for (Map.Entry<WorkPolicy.WorkingType, List<Integer>> entry : sh.getWorkingTeam().entrySet())
        {
            if(entry.getValue().contains(worker_id) )
                return false;
        }

        work(worker_id, shift_id);
        if (!getShift(shift_id).getWorkingTeam().containsKey(workingType))
            getShift(shift_id).getWorkingTeam().put(workingType, new LinkedList<>());

        getShift(shift_id).getWorkingTeam().get(workingType).add(worker_id);

        if (getWorker(worker_id).getType() == WorkPolicy.WorkingType.Driver) {
            new DriverDAO().update((Driver) getWorker(worker_id));
        } else if (getWorker(worker_id).getType() == WorkPolicy.WorkingType.StockKeeper) {
            new StockKeeperDAO().update((StockKeeper) getWorker(worker_id));
        }

        updateShift(getShift(shift_id));

        return true;
    }

    public boolean stockKeeperAvailable(Shift shift) {
        return (shift.getWorkingTeam().get(WorkPolicy.WorkingType.StockKeeper) != null && (!shift.getWorkingTeam().get(WorkPolicy.WorkingType.StockKeeper).isEmpty()) || shift.getBoss().getType() == WorkPolicy.WorkingType.StockKeeper);
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

    public List<String> getAvailableAddressesRegister()
    {
        return null;
    }

//------------------------------ Truck --------------------------//


    public Map<String, Truck> getAllTrucks() {
        return data.getTrucks();
    }

    public List<String> getAvailableTrucks(Date date, Shift.ShiftTime delivery_time) {

        List<String> available_trucks = new LinkedList<>();
        for (Truck truck : getAllTrucks().values()) {
            if (truckIsAvailable(truck.getSerialNumber(), date, delivery_time)) {
                available_trucks.add(truck.getSerialNumber());
            }
        }

        return available_trucks;
    }

    public boolean truckIsAvailable(String truck_serial, Date date, Shift.ShiftTime shift_time) {
        for (Delivery delivery : getAllDeliveries().values()) {
            if (delivery.getTruckSerialNumber().equals(truck_serial)) {
                return false;
            }
        }

        return true;
    }

    public Truck getTruck(String serial_number) {
        return data.getTrucks().get(serial_number);
    }

    public boolean addTruck(Truck truck) {
        data.getTrucks().put(truck.getSerialNumber(), truck);
        new TruckDAO().save(truck);
        return true;
    }

//------------------------------ Product --------------------------//


    public Map<String, Product> getAllProducts() {
        return data.getProducts();
    }

    public Product getProduct(String cn) {
        return data.getProducts().get(cn);
    }

    public boolean addProduct(Product product) {
        data.getProducts().put(product.getCN(), product);
        new ProductDAO().save(product);
        return true;
    }


    public boolean loadFromDataBase() {

        List<StockKeeper> stockKeepers = new StockKeeperDAO().getAll();
        List<Driver> drivers = new DriverDAO().getAll();
        Map<Integer, Worker> workers_map = new HashMap<>();

        for (StockKeeper sk : stockKeepers) {
            workers_map.put(sk.getId(), sk);
        }

        for (Driver d : drivers) {
            workers_map.put(d.getId(), d);
        }

        workers.setWorkers(workers_map);

        List<Shift> shifts = new ShiftDAO().getAll();

        Map<Integer, Shift> shifts_map = new HashMap<>();

        for (Shift shift : shifts) {
            shifts_map.put(shift.getShiftId(), shift);
        }

        history.setShifts(shifts_map);

        List<Truck> trucks = new TruckDAO().getAll();
        Map<String, Truck> trucks_map = new HashMap<>();
        for (Truck truck : trucks) {
            trucks_map.put(truck.getSerialNumber(), truck);
        }

        data.setTrucks(trucks_map);

        List<Product> products = new ProductDAO().getAll();
        Map<String, Product> products_map = new HashMap<>();
        for (Product product : products) {
            products_map.put(product.getCN(), product);
        }

        data.setProducts(products_map);


        List<Address> addresses = new AddressDAO().getAll();
        Map<String, Address> addresses_map = new HashMap<>();
        for (Address address : addresses) {
            addresses_map.put(address.getLocation(), address);
        }

        data.setAddresses(addresses_map);

        List<Delivery> deliveries = new DeliveryDAO().getAll();
        Map<Integer, Delivery> deliveryMap = new HashMap<>();

        for (Delivery delivery : deliveries) {
            deliveryMap.put(delivery.getDeliveryID(), delivery);
        }

        data.setDeliveries(deliveryMap);

        for(Shift shift : getAllShifts().values())
        {
            for(List<Integer> working_type : shift.getWorkingTeam().values())
            {
                for(Integer worker_id : working_type)
                {
                    getWorker(worker_id).getWorker_shifts().add(shift.getShiftId());
                }
            }
        }

        return true;
    }

    public Delivery arrangeDelivery(String source, Map<String, Document> documents) {
        Delivery delivery = new Delivery();
        int totalWeight = getTotalWeight(documents);
        Truck truck = Data.getInstance().getProperTruck(totalWeight);
        Driver driver = Data.getInstance().getProperDriver(totalWeight);
        Date date = null;

        delivery.setTruckSerialNumber(truck.getSerialNumber());
        delivery.setDriverID(driver.getId());
        delivery.setDocuments(documents);
        delivery.setSource(source);

        if (totalWeight <= truck.getMaxAllowedWeight())
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

        if (totalWeight <= Data.getInstance().getTrucks().get(delivery.getTruckSerialNumber()).getMaxAllowedWeight())
            delivery.setDate(getDeliveryDate((new DriverDAO()).get(delivery.getDriverID())));
    }


    public Map<Integer, Delivery> getAllDeliveries() {
        return data.getDeliveries();
    }

    public Delivery getDelivery(int id) {
        return data.getDeliveries().get(id);
    }

    public boolean addDelivery(Delivery delivery) {
        data.getDeliveries().put(delivery.getDeliveryID(), delivery);
        new DeliveryDAO().save(delivery);
        return true;

    }

    public boolean updateShift(Shift shift) {
        new ShiftDAO().update(shift);
        return true;
    }

    public List<String> getAvailableAddresses(Date date, Shift.ShiftTime delivery_time) {

        List<String> aa = new LinkedList<>();


        for (Shift shift : getAllShifts().values()) {
            if (shift.getShiftDate().equals(date) && shift.getShiftTime().equals(delivery_time)) {
                aa.add(shift.getAddress().getLocation());
            }
        }

        return aa;

    }

    public List<String> getBigTrucks(int weight , Shift shift)
    {

        List<String> big = new LinkedList<>();

        for(Truck truck : getAllTrucks().values())
        {

            if(truck.getMaxAllowedWeight()-truck.getWeight() >= weight)
            {
                boolean av = true;

                for(Delivery delivery : getAllDeliveries().values())
                {

                    if(delivery.getTruckSerialNumber().equals(truck.getSerialNumber()) && delivery.getDate().equals(shift.getShiftDate()))
                    {
                       av = false;
                       break;
                    }
                }

                if(av)
                {
                    big.add(truck.getSerialNumber());
                }

            }

        }


        return big;
    }

//----------------------------- Other --------------------

    public void initializeDB() {
        Mapper.InitializeDB();
    }

    public String AddSupplier(String name, int ID,String Address, String bank, String branch, int bankNumber,
                              String payments, Map<Integer, String> Contacts_ID,
                              Map<Integer, Integer> Contacts_number) {//  List<DALItem> Items)
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done = current_Store.AddSuplier(name, ID,Address,bank, branch,bankNumber, payments, Contacts_ID,Contacts_number);

        return Done;
    }

    public String AddContract(int suplaier_id, boolean fixeDays, LinkedList<Integer> days,
                              boolean leading,  Map<Integer,Integer>  ItemsID_ItemsIDSupplier, Map<Integer, String> productIDVendor_name,
                              Map<Integer, Double> producttemsIDVendor_price) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.AddContract(suplaier_id,fixeDays, days,leading,ItemsID_ItemsIDSupplier, productIDVendor_name,producttemsIDVendor_price);
        return Done;
    }

    public String AddWrite(int suplaier_id, Map<Integer, Integer> itemsID_amount, Map<Integer, Double> itemsID_assumption) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.AddWrite(suplaier_id, itemsID_amount,itemsID_assumption);
        return Done;
    }

    public int MakeOrder(int id_suplaier, LinkedList<Integer> day, Map<Integer, Integer> itemsIDVendor_numberOfItems) {
        int Done=-1;
        if(current_Store!=null)
            Done= current_Store.MakeOrder(id_suplaier,day,itemsIDVendor_numberOfItems);
        return Done;
    }

    public String EditSupplier(String name, int id, String address, String bank, String branch, int bankNumber, String payments, Map<Integer, String> contacts_id, Map<Integer, Integer> contacts_number) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done = current_Store.EditSuplier(name, id,address ,bank, branch,bankNumber, payments,contacts_id,contacts_number);
        return Done;
    }

    public String DeleteSupplier(int id) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null) {
            Done = current_Store.Delete(id);
            //     Done = logged_user.DeleteSupplier(id);
        }
        return Done;
    }

    public String EditContract(int suplaier_id, boolean fixeDays, LinkedList<Integer> days, boolean leading,Map<Integer,Integer>  ItemsID_ItemsIDSupplier,
                               Map<Integer, String> productIDVendor_name, Map<Integer, Double> producttemsIDVendor_price) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null) {
            Done = current_Store.EditContract(suplaier_id, fixeDays, days, leading,ItemsID_ItemsIDSupplier, productIDVendor_name, producttemsIDVendor_price);
        }
        return Done;
    }

    public String EditWrite(int suplaier_id, Map<Integer, Integer> itemsID_amount, Map<Integer, Double> itemsID_assumption) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.EditWrite(suplaier_id, itemsID_amount,itemsID_assumption);
        return Done;
    }

    public String CheckEmailExist(String email) {
        String Done=systemcontroler.CheckEmailExist(email);
        return Done;
    }

    public String Register(String email, String password) {
        String Done=systemcontroler.Register(email,password);
        return Done;
    }

    public String Login(String email, String password) {
        String Done=systemcontroler.Login(email,password);
        if(Done.equals("Done")){
            logged_user=new User(email,password);
            current_Store=Store.createInstance(email);
        }
        //initialize();
        return Done;
    }

    public LinkedList<InterfaceContract> GetContract() {
        LinkedList<InterfaceContract> list=new LinkedList<InterfaceContract>();
        if(current_Store!=null)
            list=current_Store.GetContract();
        return list;
    }

    public LinkedList<InterfaceSupplier> GetSupliers() {
        LinkedList<InterfaceSupplier> list=new LinkedList<InterfaceSupplier>();
        if(current_Store!=null)
            list=current_Store.GetSupliers();
        return list;
    }

    public String ChangOrderStatus (int id_order) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.ChangOrderStatus(id_order);
        return Done;
    }

    public String CheckSuplierExist(int id) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.CheckSuplierExit(id);
        return Done;
    }

    public String CheckSAgreementExist(int suplaier_id) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.CheckSAgreementExit(suplaier_id);
        return Done;
    }

    public String CheckSWortExist(int suplaier_id) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store. CheckSWortExit(suplaier_id);
        return Done;
    }

    public String Logout() {
        //todo changed
        if(logged_user==null| current_Store==null){
            return "you need to Login before you logout";
        }
        current_Store.Logout();
        current_Store=null;
        logged_user=null;
        return "logout successfully";
    }

    public boolean CheckConected() {
        return current_Store!=null;
    }

    public static void sendWarning(String warning) {
        Menu.printWarning(warning);
    }

    public String getItemAmountsByName(String name) {
        return current_Store.getItemAmountsByName(name);
    }

    public String addAmounts(String name,String amounts){ return  current_Store.addAmounts(name,amounts);}

    public String setNewAmounts(String name, String amounts) {
        return current_Store.setNewAmounts(name,amounts);
    }

    public String getItemIdsByName(String name) {
        return current_Store.getItemIdsByName(name);
    }

    public String removeItem(String name,int id) {
        return current_Store.removeItem(name,id);
    }

    public String moveToShelf(String name, String amount) {
        return current_Store.moveToShelf(name,amount);
    }

    public String removeItemFromShelf(String name, int id) {
        return current_Store.removeItemFromShelf(name, id);
    }

    public String setDefectedItem(String  name, String id){
        try {
            int ID = Integer.parseInt(id);
            return current_Store.setDefectedItem(name, ID);
        }
        catch (Exception e){
            return "Item's ID is invalid";
        }
    }
    @SuppressWarnings("depercation")
    public String addNewItemDiscount(String itemName, String percentage, String begDate, String enDate) {
        java.sql.Date beginDate, endDate;
        int perc;
        if(begDate.length()!=10 || enDate.length() != 10)
            return "Please enter valid arguments";
        try{
            beginDate = new java.sql.Date(Integer.parseInt(begDate.substring(6))-1900,Integer.parseInt(begDate.substring(3,5))-1,Integer.parseInt(begDate.substring(0,2)));
            endDate = new java.sql.Date(Integer.parseInt(enDate.substring(6))-1900,Integer.parseInt(enDate.substring(3,5))-1,Integer.parseInt(enDate.substring(0,2)));
            perc = Integer.parseInt(percentage);
            if(perc < 1 || perc > 100)
                return "Please enter valid arguments";
        }
        catch (Exception e){
            return "Please enter valid arguments";
        }
        return current_Store.addItemDiscount(itemName, perc, beginDate, endDate);
    }

    public String addNewCategoryDiscount(String categoryName, String percentage, String begDate, String enDate) {
        java.sql.Date beginDate, endDate;
        int perc;
        if(begDate.length()!=10 || enDate.length() != 10)
            return "Please enter valid arguments";
        try{
            beginDate = new java.sql.Date(Integer.parseInt(begDate.substring(6))-1900,Integer.parseInt(begDate.substring(3,5))-1,Integer.parseInt(begDate.substring(0,2)));
            endDate = new java.sql.Date(Integer.parseInt(enDate.substring(6))-1900,Integer.parseInt(enDate.substring(3,5))-1,Integer.parseInt(enDate.substring(0,2)));
            perc = Integer.parseInt(percentage);
            if(perc < 1 || perc > 100)
                return "Please enter valid arguments";
        }
        catch (Exception e){
            return "Please enter valid arguments";
        }
        return current_Store.addNewCategoryDiscount(categoryName, perc, beginDate, endDate);
    }

    public String setNewPrice(String name, String price,String rPrice){
        try {
            int newPrice = Integer.parseInt(price);
            if(newPrice<=0){
                return "Price must greater than 0";
            }
            int retailPrice = Integer.parseInt(rPrice);
            if(retailPrice<=0){
                return "Price must greater than 0";
            }
            return current_Store.setNewPrice(name, newPrice,retailPrice);
        }
        catch (Exception e){
            return "Item's price must be a number";
        }
    }

    public String printDefectedReport(String reportBegin, String reportEnd){
        java.sql.Date beginDate, endDate;
        if(reportBegin.length()!=10 || reportEnd.length() != 10)
            return "Please enter valid arguments";
        try{
            beginDate = new java.sql.Date(Integer.parseInt(reportBegin.substring(6))-1900,Integer.parseInt(reportBegin.substring(3,5))-1,Integer.parseInt(reportBegin.substring(0,2)));
            endDate = new java.sql.Date(Integer.parseInt(reportEnd.substring(6))-1900,Integer.parseInt(reportEnd.substring(3,5))-1,Integer.parseInt(reportEnd.substring(0,2)));
        }
        catch (Exception e){
            return "Please enter valid dates";
        }
        return current_Store.printDefectedReport(beginDate, endDate);
    }

    public String getInventoryReport(String names) {
        String[] categories = names.split("\\s+");
        String report = "";
        if (categories[0].equals("all")) {
            report = current_Store.getAllInventoryReport();
        }
        else {
            for (int i = 0; i < categories.length; i++) {
                report = report + current_Store.getInventoryReport(categories[i]) + "\n";
            }
        }
        return report;
    }

    public boolean CheckTheDay(int id_suplaier, int day) {
        return current_Store.CheckTheDay(id_suplaier,day);
    }

    public boolean CheckProductexist(int id_suplaier, int product_id) {
        return current_Store.CheckProductexist(id_suplaier,product_id);
    }

    public int FindId_P_Store(String product_name, String category, String subcategory, String sub_subcategory, String manufacturer, int minAmount, int shelfNumber) {
        return current_Store.FindId_P_Store(product_name,category,subcategory,sub_subcategory,manufacturer,minAmount,shelfNumber);
    }

    public String CheckAbleToChangeOrder(int id_order) {
        return current_Store.CheckAbleToChangeOrder(id_order);
    }

    public void RemoveProduct(int id_order, int product_id) {
        current_Store.RemoveProduct(id_order,product_id);
    }

    public InterfaceOrder ChangeOrder(int Id_Order, int id_suplaier, LinkedList<Integer> day, Map<Integer, Integer> itemsIDVendor_numberOfItems) {
        return current_Store.ChangeOrder(Id_Order,id_suplaier, day ,itemsIDVendor_numberOfItems);

    }

    public InterfaceOrder getOrderDetails(int done) {
        InterfaceOrder o = current_Store.getOrderDetails(done);
        return o;
    }

    public InterfaceSupplier GetTheCyeeperSuplier(int produdtId, int amount) {
        return current_Store.GetTheCyeeperSuplier(produdtId, amount);
    }

    public int GetSupplierID_PerOrder(int id_order) {
        return current_Store.GetSupplierID_PerOrder(id_order);
    }

    public void AddToStore(Map productID_amount, Map productID_date) {
        current_Store.AddToStore(productID_amount,productID_date);
    }

    public LinkedList<InterfaceOrder> GetOrderDetails() {
        return current_Store.GetOrderDetails();
    }
}
