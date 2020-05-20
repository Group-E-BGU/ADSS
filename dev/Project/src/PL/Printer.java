package PL;

import BL.*;
import javafx.util.Pair;

import java.time.DayOfWeek;
import java.util.*;

public class Printer {

    static BLService blService = new BLService();

    public static void printAllWorkingTypes()
    {
        int number =1;
        for(WorkPolicy.WorkingType workingType : WorkPolicy.WorkingType.values())
        {
            System.out.println(number+") "+workingType.toString());
            number++;
        }
    }

    public static void printWorkingShifts(Worker w) {

        if (w.getWorker_shifts().isEmpty())
            System.out.println(w.getName() + " has no working shifts.");


        else for (Integer shift_id : w.getWorker_shifts()) {
            Shift shift = blService.getShift(shift_id);
            System.out.println(shift.toString());
        }
    }

    public static void printSchedule(int worker_id) {
        Map<Pair<DayOfWeek, Shift.ShiftTime>, Boolean> worker_schedule = Workers.getInstance().getAllWorkers().get(worker_id).getSchedule();
        List<Pair<DayOfWeek, Shift.ShiftTime>> schedules_date = new LinkedList<>(worker_schedule.keySet());
        Collections.sort(schedules_date, new Comparator<Pair<DayOfWeek, Shift.ShiftTime>>() {
            @Override
            public int compare(Pair<DayOfWeek, Shift.ShiftTime> o1, Pair<DayOfWeek, Shift.ShiftTime> o2) {

                if ((o1.getKey().getValue()+1)%8 < (o2.getKey().getValue()+1)%8)
                    return -1;

                if (o1.getKey().equals(o2.getKey())) {
                    if (o1.getValue() == Shift.ShiftTime.Morning)
                        return -1;
                    return 1;
                }
                return 0;
            }
        });
        for (Pair<DayOfWeek, Shift.ShiftTime> p : schedules_date) {
            System.out.println(p.getKey() + " , " + p.getValue().toString() + " : " + worker_schedule.get(p));
        }
    }

    public static void printMainMenu() {

        System.out.println("1) View Workers");
        System.out.println("2) View Shifts");
        System.out.println("3) View Deliveries");
        System.out.println("4) View Addresses");
        System.out.println("5) View Trucks");
        System.out.println("6) View Products");
        System.out.println("7) exit");
    }

    public static void printWorkersView()
    {

        String workers_string="";
        for (Worker worker : blService.getAllWorkers().values()) {
            workers_string = workers_string+worker.toString()+'\n';
        }
        if(workers_string.equals(""))
        {
            System.out.println(workers_string);

        }
        else
        {
            workers_string = workers_string.substring(0,workers_string.length()-1);
            System.out.println(workers_string+'\n');
        }


        System.out.println("1) Register a worker");
        System.out.println("2) select a worker");
        System.out.println("3) return\n");
    }

    public static void printShiftsView()
    {
        for (Shift shift : blService.getAllShifts().values()) {
            System.out.println(shift.toString());
            border();
        }

        System.out.println("1) select a shift");
        System.out.println("2) create a shift");
        System.out.println("3) return");
    }

    public static void PrintWorkerView(int worker_id)
    {
        Worker w = blService.getWorker(worker_id);
        if(w==null)
        {
            System.out.println("Error : no worker with such id");
            return;
        }
        System.out.println("Worker name : " + w.getName());
        System.out.println("Worker id : " + w.getId());
        System.out.println("jobs : " + w.getType().toString()+ "\n");

        System.out.println("1) Print Schedule");
        System.out.println("2) Print contract");
        System.out.println("3) Print working shifts");
        System.out.println("4) Edit worker info");
        System.out.println("5) Return");
    }

    public static void printEditWorkerMenu() {
        System.out.println("1) Edit worker name");
        System.out.println("2) Edit worker id");
        System.out.println("3) Edit worker bank address");
        System.out.println("4) Edit worker salary");
        System.out.println("5) Return");
    }
//------------------------------------ Shifts ---------------------------------//

    public static void printAvailableWorkers(Shift shift)
    {
        String available_workers = blService.AvilableWorkerstoString(shift.getShiftDate(), shift.getShiftTime());
        System.out.println(available_workers);
    }

    public static void printShiftView(int shift_id)
    {
        Shift shift = blService.getShift(shift_id);
        System.out.println(shift.toString());
        System.out.println("1) print available workers for this shift");
        System.out.println("2) return");
    }
//------------------------------------ Addresses ---------------------------------//

    public static void printAddressesView()
    {

        printAllAddresses();
        System.out.println("1) Add an address");
        System.out.println("2) Return");

    }

    public static void printAllAddresses()
    {
        Map<String,Address> addresses_map = blService.getAllAddresses();

        String addresses = "";

        for (Address address : addresses_map.values()) {
            addresses += "Location : " + address.getLocation() + "\n" +
                    "Contact Name : " + address.getContactName() + "\n" +
                    "Phone Number : " + address.getPhoneNumber() + "\n\n";
        }

        System.out.println(addresses);
    }

//------------------------------------ Trucks ---------------------------------//

    public static void printTrucksView()
    {

        printAllTrucks();
        System.out.println("1) Add a truck");
        System.out.println("2) Return");

    }

    public static void printAllTrucks()
    {
        Map<String,Truck> trucks_map = blService.getAllTrucks();

        String trucks = "";

        for (Truck t : trucks_map.values()) {
            trucks += "Truck serial number : " + t.getSerialNumber() + "\n" +
                    "Model : " + t.getModel() + "\n" +
                    "Weight : " + t.getWeight() + "\n" +
                    "Max allowed weight : " + t.getMaxAllowedWeight() + "\n\n";
        }

        System.out.println(trucks);
    }
//------------------------------------ Products ---------------------------------//

    public static void printProductsView()
    {

        printAllProducts();
        System.out.println("1) Add a product");
        System.out.println("2) Return");

    }

    public static void printAllProducts()
    {
        Map<String,Product> products_map = blService.getAllProducts();

        String products = "";

        for (Product product : products_map.values()) {
            products += "Product Name : " + product.getName() + "\n" +
                    "CN : " + product.getCN() + "\n" +
                    "Weight : " + product.getWeight() + "\n\n";
        }

        System.out.println(products);
    }

    public static void printDeliveriesView()
    {

        String deliveries_string="";
        for (Delivery delivery : blService.getAllDeliveries().values()) {
            deliveries_string = deliveries_string+delivery.toString()+'\n';
        }
        if(deliveries_string.equals(""))
        {
            System.out.println(deliveries_string);

        }
        else
        {
            deliveries_string = deliveries_string.substring(0,deliveries_string.length()-1);
            System.out.println(deliveries_string+'\n');
        }


        System.out.println("1) Arrange a delivery");
        System.out.println("2) select a delivery");
        System.out.println("3) Return\n");

    }

    public static void PrintDeliveryView(int delivery_id)
    {
        Delivery delivery = blService.getDelivery(delivery_id);
        if(delivery==null)
        {
            System.out.println("Error : no delivery with such id");
            return;
        }

        System.out.println("Delivery id : " + delivery.getDeliveryID());
        System.out.println("Date : " + delivery.getDate().toString());
        System.out.println("Source location name : " + delivery.getSource());
        System.out.println("Destinations : " + delivery.getDocuments().keySet().toString());
        System.out.println("Truck weight : " + delivery.getTruckWeight());
        System.out.println("Delivery logs : " + delivery.getLogs().toString()+ "\n");


        System.out.println("1) reArrange");
        System.out.println("2) Return");

    }

    public static void border() {
        System.out.println("--------------------------------");
    }


}
