package PL;

import BL.*;
import javafx.util.Pair;


import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

public class CreateActions {

    static Scanner keyboard = new Scanner(System.in);
    static BLService blService = new BLService();


    public void createShift() {

        for (; ; ) {

            System.out.println("Choose an address for this shift by typing its location :");
            Address address = null;
            Printer.printAllAddresses();
            boolean address_chosen = false;
            while (!address_chosen) {
                String location = keyboard.nextLine();
                if (blService.getAddress(location) == null) {
                    System.out.println("Error : no address found with " + location + " as its location! , Do you want to try again? y/n");
                    if (!getConfirmation()) {
                        System.out.println("Create shift canceled");
                        return;
                    }
                } else {
                    address_chosen = true;
                    address = blService.getAddress(location);
                }
            }


            System.out.println("enter the date using this format dd/mm/yyyy or type EXIT to cancel ...");
            String date_string = keyboard.nextLine();
            if (date_string.equals("EXIT") || date_string.equals("exit"))
                return;

            Date date = null;
            SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");

            try {
                date = date_format.parse(date_string);
                System.out.println("Your shift date will be : " + new SimpleDateFormat("EEEE").format(date) + " " + date_string);
                System.out.println("Type M for Morning , Type E for Evening");
                Shift.ShiftTime shiftTime = null;

                boolean chosen_shift = false;

                while (!chosen_shift) {
                    String choice = keyboard.nextLine();

                    if (choice.equals("M") || choice.equals("m")) {
                        shiftTime = Shift.ShiftTime.Morning;
                        chosen_shift = true;
                    } else if (choice.equals("E") || choice.equals("e")) {
                        shiftTime = Shift.ShiftTime.Evening;
                        chosen_shift = true;
                    } else
                        System.out.println("Error : invalid input!");

                }

                List<Integer> available_boss = blService.getAvailableWorkers(date, shiftTime);

                if (available_boss.isEmpty()) {
                    System.out.println("Error : no available boss for this shift! returning to shifts view screen...");
                    return;
                }

                Printer.printWorkers(available_boss);
                System.out.println("enter the id of the worker who you wish to appoint as a boss");
                int boss_id = getChoice(Main.id_lower_bound, Main.id_upper_bound);
                Worker boss = blService.getWorker(boss_id);
                if (boss == null) {
                    System.out.println("Error : no worker with such id");
                    return;
                }
                if (!blService.isAvailable(boss.getId(), date, shiftTime)) {
                    System.out.println("Error : this worker is not available to work in this shift!");
                    return;
                }

                Map<WorkPolicy.WorkingType, List<Integer>> working_team = new HashMap<>();

                Shift shift = new Shift(address, date, shiftTime, boss, working_team);
                boolean success = blService.addShift(shift);
                if (success)
                    System.out.println("The shift has been created successfully");
                else
                    System.out.println("Error : the shift already exists");
                return;

            } catch (ParseException pe) {
                System.out.println("Error : invalid date input");
            }

        }


    }

    public void registerWorker() {

        System.out.println("Worker id: ");
        int worker_id = getChoice(Main.id_lower_bound, Main.id_upper_bound);
        if (blService.getWorker(worker_id) != null) {
            System.out.println("Error : There is a user with the same id in the data base!");
            return;
        }
        System.out.println("Worker name: ");
        String worker_name = keyboard.nextLine();
        List<WorkPolicy.WorkingType> jobs = Arrays.asList(WorkPolicy.WorkingType.values());
        System.out.println("Worker Job : ");
        Printer.printAllWorkingTypes();
        System.out.println("Choose one of these jobs :");
        int workingType_id = getChoice(1, jobs.size());
        WorkPolicy.WorkingType worker_job = jobs.get(workingType_id - 1);
        System.out.println("Enter constraints : ");
        keyboard.nextLine();
        System.out.println("Worker salary :");
        double salary = getDoubleChoice(0, Double.MAX_VALUE);
        System.out.println("Worker bank address :");
        String address = keyboard.nextLine();
        SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
        Date current_date = new Date();
        WorkerDeal deal = null;
        try {
            deal = new WorkerDeal(worker_id, (date_format.parse(date_format.format(current_date))), salary, address, new LinkedList<String>());
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }

        Worker worker = null;
        switch (worker_job) {
            case Driver:
                System.out.println("Enter the driver's license:");
                String license = keyboard.nextLine();
                worker = new Driver(worker_id, worker_name, new InitializeData().createSchedule(), deal, license);
                break;
            case StockKeeper:
                worker = new StockKeeper(worker_id, worker_name, new InitializeData().createSchedule(), deal);
                break;
        }

        blService.addWorker(worker);

    }

    public int editWorker(int worker_id) {

        // since this comes from workerView the worker is always not null

        Worker worker = blService.getWorker(worker_id);

        boolean go_back = false;
        while (!go_back) {

            Printer.printEditWorkerMenu();

            int choice = getChoice(1, 6);
            switch (choice) {
                case 1:
                    System.out.println("Type the new name :");
                    String edited_name = keyboard.nextLine();
                    worker.setName(edited_name);
                    blService.updateWorker(worker);
                    break;
                case 2:
                    System.out.println("Type the new id :");
                    int edited_id = getChoice(Main.id_lower_bound, Main.id_upper_bound);
                    if (blService.getWorker(edited_id) != null) {
                        System.out.println("Error : There is already a worker with such id!");
                        break;
                    }

                    if (blService.updateWorkerID(worker_id, edited_id))
                        worker_id = edited_id;
                    break;
                case 3:
                    System.out.println("Type the new bank address :");
                    String edited_address = keyboard.nextLine();
                    worker.getContract().setBankAddress(edited_address);
                    blService.updateContract(worker_id, worker.getContract());
                    break;
                case 4:
                    System.out.println("Type the new salary :");
                    double edited_salary = getDoubleChoice(0, Double.MAX_VALUE);
                    worker.getContract().setSalary(edited_salary);
                    blService.updateContract(worker_id, worker.getContract());
                    break;

                case 5:
                    Printer.printSchedule(worker_id);
                    System.out.println("type the day you want to edit : Sun,Mon,Tue,Wed,Thu,Fri,Sat...");
                    DayOfWeek chosen_day = null;
                    boolean chosen = false;
                    while (chosen_day == null) {
                        String day = keyboard.nextLine();
                        if (day.equals("Sun") || day.equals("sun")) {
                            chosen_day = DayOfWeek.SUNDAY;
                        } else if (day.equals("Mon") || day.equals("mon")) {
                            chosen_day = DayOfWeek.MONDAY;
                        } else if (day.equals("Tue") || day.equals("tue")) {
                            chosen_day = DayOfWeek.TUESDAY;
                        } else if (day.equals("Wed") || day.equals("wed")) {
                            chosen_day = DayOfWeek.WEDNESDAY;
                        } else if (day.equals("Thu") || day.equals("thu")) {
                            chosen_day = DayOfWeek.THURSDAY;
                        } else if (day.equals("Fri") || day.equals("fri")) {
                            chosen_day = DayOfWeek.FRIDAY;
                        } else if (day.equals("Sat") || day.equals("sat")) {
                            chosen_day = DayOfWeek.SATURDAY;
                        } else {
                            System.out.println("Error : wrong input please type one of those : Sun,Mon,Tue,Wed,Thu,Fri,Sat , try again ? y/n ");
                            {
                                if (!getConfirmation()) {
                                    return worker_id;
                                }
                            }
                        }

                        System.out.println("Type M for Morning , Type E for Evening");
                        boolean chosen_shift = false;

                        Shift.ShiftTime shiftTime = null;

                        while (!chosen_shift) {
                            String shiftTime_choice = keyboard.nextLine();

                            if (shiftTime_choice.equals("M") || shiftTime_choice.equals("m")) {
                                shiftTime = Shift.ShiftTime.Morning;
                                chosen_shift = true;
                            } else if (shiftTime_choice.equals("E") || shiftTime_choice.equals("e")) {
                                shiftTime = Shift.ShiftTime.Evening;
                                chosen_shift = true;
                            } else {
                                System.out.println("Error : invalid input! try again? y/n");
                                if (!getConfirmation())
                                    return worker_id;
                            }

                        }

                        System.out.println("Type 0 to set to false , type 1 to set to true");
                        {
                            int set = getChoice(0, 1);
                            Pair<DayOfWeek, Shift.ShiftTime> p = new Pair<>(chosen_day, shiftTime);
                            switch (set) {
                                case 0:
                                    List<Integer> working_shifts = worker.getWorker_shifts();
                                    for (Integer shift_id : working_shifts) {
                                        Shift shift = blService.getShift(shift_id);
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(shift.getShiftDate());
                                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                        if (DayOfWeek.values()[dayOfWeek] == p.getKey() && shift.getShiftTime() == p.getValue()) {
                                            System.out.println("Error : This worker has a shift at this time");
                                            return worker_id;
                                        }

                                    }
                                    worker.getSchedule().put(p, false);
                                    break;
                                case 1:
                                    worker.getSchedule().put(p, true);
                                    break;
                            }
                        }

                    }

                    blService.updateWorker(worker);

                    break;
                case 6:
                    go_back = true;
                    break;


            }
        }

        return worker_id;
    }

    public static void AddAddress() {

        System.out.println("Enter the location:");
        String location = keyboard.nextLine();
        if (blService.getAddress(location) != null) {
            System.out.println("Error : an address with this location already exists");
            return;
        }
        System.out.println("Enter the contact name:");
        String contact_name = keyboard.nextLine();
        System.out.println("Enter the contact's phone number:");
        String phone_number = keyboard.nextLine();

        if (blService.addAddress(new Address(location, contact_name, phone_number))) {
            System.out.println("The address has been added successfully!");
        } else {
            System.out.println("Error : Couldn't add address");
        }

    }

    public static void AddTruck() {
        System.out.println("Enter the truck's serial number:");
        String serialNumber = keyboard.nextLine();
        if (blService.getTruck(serialNumber) != null) {
            System.out.println("Error : a truck with this serial number already exists");
            return;
        }
        System.out.println("Enter the truck's model:");
        String model = keyboard.nextLine();
        System.out.println("Enter the truck's weight:");
        int weight = getChoice(0, Integer.MAX_VALUE);
        System.out.println("Enter the truck's max allowed weight:");
        int maxAllowedWeight = getChoice(0, Integer.MAX_VALUE);

        if (blService.addTruck(new Truck(serialNumber, model, weight, maxAllowedWeight))) {
            System.out.println("The truck has been added successfully!");
        } else {
            System.out.println("Error : Couldn't add truck");
        }


    }


    public static void AddProduct() {

        System.out.println("Enter the product's CN:");
        String product_cn = keyboard.nextLine();
        if (blService.getProduct(product_cn) != null) {
            System.out.println("Error : a product with this cn already exists");
            return;
        }
        System.out.println("Enter the product's name:");
        String product_name = keyboard.nextLine();
        System.out.println("Enter the product's weight:");
        int weight = getChoice(0, Integer.MAX_VALUE);


        if (blService.addProduct(new Product(product_name, product_cn, weight))) {
            System.out.println("The product has been added successfully!");
        } else {
            System.out.println("Error : Couldn't add product");
        }

    }

    private static int getChoice(int lower_bound, int upper_bound) {
        for (; ; ) {
            String keyboard_input = keyboard.nextLine();
            int choice_number = -1;
            boolean tooBig = false;
            try {
                BigInteger big_int = new BigInteger(keyboard_input);
                if (big_int.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                    //    System.out.println("Error : value is too large");
                    tooBig = true;
                }
                choice_number = big_int.intValue();

                if (tooBig || choice_number < lower_bound || choice_number > upper_bound) {
                    System.out.println("Error : number out of bounds!");
                } else return choice_number;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Error : Enter a numeric input!");
            }

        }
    }

    private static double getDoubleChoice(double lower_bound, double upper_bound) {
        for (; ; ) {

            String keyboard_input = keyboard.nextLine();
            try {
                double choice_number = Double.parseDouble(keyboard_input);
                if (choice_number < lower_bound || choice_number > upper_bound) {
                    System.out.println("Error : number out of bounds!");
                } else return choice_number;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Error : Enter a numeric input!");
            }
        }
    }

    private static boolean getConfirmation() {
        for (; ; ) {
            String keyboard_input = keyboard.nextLine();
            if (keyboard_input.equals("y") || keyboard_input.equals("Y"))
                return true;
            else if (keyboard_input.equals("n") || keyboard_input.equals("N"))
                return false;
            else
                System.out.println("Error : Invalid input ! type n to cancel or y to confirm");
        }

    }

    public static void arrangeDelivery() {


        System.out.println("enter the date for this delivery using this format dd/mm/yyyy or type EXIT to cancel ...");
        String date_string = keyboard.nextLine();
        if (date_string.equals("EXIT") || date_string.equals("exit"))
            return;

        Date date = null;
        SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
        Shift.ShiftTime delivery_time = null;

        try {
            date = date_format.parse(date_string);
            System.out.println("Your delivery date will be : " + new SimpleDateFormat("EEEE").format(date) + " " + date_string);
            System.out.println("Type M for Morning , Type E for Evening");

            boolean chosen_time = false;

            while (!chosen_time) {
                String choice = keyboard.nextLine();

                if (choice.equals("M") || choice.equals("m")) {
                    delivery_time = Shift.ShiftTime.Morning;
                    chosen_time = true;
                } else if (choice.equals("E") || choice.equals("e")) {
                    delivery_time = Shift.ShiftTime.Evening;
                    chosen_time = true;
                } else
                    System.out.println("Error : invalid input!");

            }

        } catch (ParseException pe) {
            System.out.println("Error : invalid date input");
        }

        List<String> available_trucks = blService.getAvailableTrucks(date, delivery_time);

        if (available_trucks.isEmpty()) {
            System.out.println("There are no available trucks for this delivery! aborting the arrangement...");
            return;
        }

        Printer.printTrucks(available_trucks);

        System.out.println("Choose a truck by its serial number : ");

        boolean truck_chosen = false;
        String truck_serial_number = null;

        while (!truck_chosen) {
            truck_serial_number = keyboard.nextLine();
            if (!available_trucks.contains(truck_serial_number)) {
                System.out.println("Error : the truck with the serial number " + truck_serial_number + " is not a valid option! try again ? y/n");
                if (!getConfirmation()) {
                    System.out.println("Delivery arrangement canceled");
                    return;
                }
            } else
                truck_chosen = true;
        }

        System.out.println("Choose a source for this delivery by typing the location :");
        Address address = null;
        List<String> available_addresses = blService.getAvailableAddresses(date, delivery_time);
        boolean address_chosen = false;
        Printer.Addresses(available_addresses);
        while (!address_chosen) {
            String location = keyboard.nextLine();
            if (blService.getAddress(location) == null) {
                System.out.println("Error : no address found with " + location + " as its location! , Do you want to try again? y/n");
                if (!getConfirmation()) {
                    System.out.println("Delivery arrangement canceled");
                    return;
                }
            } else if (blService.getShift(blService.getAddress(location), date, delivery_time) == null) {
                System.out.println("Error : no shift in this address is available at the chosen date!");
            } else {
                address_chosen = true;
                address = blService.getAddress(location);
            }
        }

        Truck delivery_truck = blService.getTruck(truck_serial_number);
        Shift source_shift = blService.getShift(address, date, delivery_time);

        String source = address.getLocation();

        Printer.border();
        Printer.printAllAddresses();

        System.out.println("Choose destinations : ");
        boolean destinations_chosen = false;
        Map<String, Document> documents = new HashMap<>();

        while (!destinations_chosen) {
            String des = keyboard.nextLine();
            if (blService.getAddress(des) == null) {
                System.out.println("Error : no address found with " + des + " as its location! , Do you want to try again? y/n");
                if (!getConfirmation()) {
                    System.out.println("Delivery arrangement canceled");
                    return;
                }
            } else if (des.equals(source)) {
                System.out.println("Error : The source can't be a destination! , Do you want to try again? y/n");
                if (!getConfirmation()) {
                    System.out.println("Delivery arrangement canceled");
                    return;
                }
            } else if (blService.getShift(blService.getAddress(des), date, delivery_time) == null) {
                System.out.println("Error : no available shift at this destination at the chosen time");
            } else if (!blService.stockKeeperAvailable(blService.getShift(blService.getAddress(des), date, delivery_time))) {
                System.out.println("Error : no working stock keeper at the destination at the chosen time");
            } else {

                boolean products_chosen = false;
                Map<String, Integer> delivery_products = new HashMap<>();
                while (!products_chosen) {
                    System.out.println("Choose the product you want to deliver by typing the CN");
                    Printer.printAllProducts();
                    String product_cn = keyboard.nextLine();
                    if (blService.getProduct(product_cn) == null) {
                        System.out.println("Error : no product with such cn found! try again ? y/n");
                        if (!getConfirmation()) {
                            System.out.println("Delivery arrangement canceled");
                            return;
                        }
                    } else {
                        if (delivery_products.containsKey(product_cn)) {
                            System.out.println("Warning : you already added this type of product to your delivery. the amount you choose now will be the new one");
                        } else
                            System.out.println("Type the amount you want to deliver from this product , it must be bigger than zero : ");
                        int amount = getChoice(1, Integer.MAX_VALUE);
                        delivery_products.put(product_cn, amount);
                        System.out.println("add another product delivery to this destination ? y/n");
                        if (!getConfirmation()) {
                            products_chosen = true;
                        }

                    }
                }

                Document document = new Document();
                document.setDeliveryGoods(delivery_products);
                documents.put(des, document);

                System.out.println("Choose another destination ? y/n");
                if (!getConfirmation()) {
                    destinations_chosen = true;
                }
            }
        }


        int total_weight = delivery_truck.getWeight();
        List<String> logs = new LinkedList<>();
        for (Document doc : documents.values()) {
            for (Map.Entry<String, Integer> entry : doc.getDeliveryGoods().entrySet()) {
                total_weight = total_weight + (blService.getProduct(entry.getKey()).getWeight() * entry.getValue());
            }
        }


        String license = null;
        if (total_weight > 1000) {
            license = "B";
        } else {
            license = "A";
        }
        List<Integer> drivers_ids = blService.getDeliveryDriver(source_shift.getShiftDate(), source_shift.getShiftTime(), license);
        if (drivers_ids.isEmpty()) {
            System.out.println("Error : no available drivers for this delivery ... abort");
        } else {
            Printer.printWorkers(drivers_ids);
            System.out.println("Select one of these drivers by typing his id to assign to this delivery");
            int driver_id = -1;
            boolean driver_chosen = false;
            while (!driver_chosen) {
                driver_id = getChoice(Main.id_lower_bound, Main.id_upper_bound);
                if (!drivers_ids.contains(driver_id)) {
                    System.out.println("Error : driver id not valid!");
                } else {
                    driver_chosen = true;
                    Delivery delivery = new Delivery(date, source, truck_serial_number, driver_id, total_weight);
                    delivery.setDocuments(documents);
                    delivery.setLogs(logs);
                    blService.work(driver_id, source_shift.getShiftId());
                    if (total_weight > delivery_truck.getMaxAllowedWeight()) {
                        System.out.println("Error : the truck's weight exceeds its allowed weight");
                        if (!rearrangeDelivery(delivery, source_shift)) ;
                        {
                            return;
                        }
                    }
                    total_weight = delivery_truck.getWeight();
                    for (Document doc : documents.values()) {
                        for (Map.Entry<String, Integer> entry : doc.getDeliveryGoods().entrySet()) {
                            total_weight = total_weight + (blService.getProduct(entry.getKey()).getWeight() * entry.getValue());
                        }
                    }
                    blService.work(driver_id, source_shift.getShiftId());
                    blService.addDelivery(delivery);
                }
            }

        }


    }

    private static boolean rearrangeDelivery(Delivery delivery, Shift shift) {
        System.out.println("1) Change truck and driver");
        System.out.println("2) Cancel");

        int choice = getChoice(1, 2);
        if (choice == 2) {
            Delivery.counter--;
            return false;
        }

        if (choice == 1) {
            List<String> big_trucks = blService.getBigTrucks(delivery.getTruckWeight() - blService.getTruck(delivery.getTruckSerialNumber()).getWeight(), shift);

            if (big_trucks.isEmpty()) {
                System.out.println("Error : no trucks are available ! Aborting delivery....");
                return false;
            }

            List<Integer> drivers = blService.getDeliveryDriver(shift.getShiftDate(), shift.getShiftTime(), "B");
            if (drivers.isEmpty()) {
                System.out.println("Error : no drivers are available ! Aborting delivery....");
                return false;
            }

            Printer.printTrucks(big_trucks);


            boolean truck_chosen = false;
            String truck_serial_number = null;

            while (!truck_chosen) {
                truck_serial_number = keyboard.nextLine();
                if (!big_trucks.contains(truck_serial_number)) {
                    System.out.println("Error : the truck with the serial number " + truck_serial_number + " is not a valid option! try again ? y/n");
                    if (!getConfirmation()) {
                        System.out.println("Delivery arrangement canceled");
                        return false;
                    }
                } else
                    truck_chosen = true;
                delivery.setTruckSerialNumber(truck_serial_number);
            }


            Printer.printWorkers(drivers);

            System.out.println("Select one of these drivers by typing his id to assign to this delivery");
            int driver_id = -1;
            boolean driver_chosen = false;
            while (!driver_chosen) {
                driver_id = getChoice(Main.id_lower_bound, Main.id_upper_bound);
                if (!drivers.contains(driver_id)) {
                    System.out.println("Error : driver id not valid!");
                } else {
                    driver_chosen = true;
                }
            }

            delivery.setDriverID(driver_id);

            return true;
        }
        return true;
    }
}