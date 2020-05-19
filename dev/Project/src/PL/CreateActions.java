package PL;

import BL.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            while(!address_chosen)
            {
                String location = keyboard.nextLine();
                if(blService.getAddress(location)==null)
                {
                    System.out.println("Error : no address found with "+location+" as its location! , Do you want to try again? y/n");
                    if(!getConfirmation())
                    {
                        System.out.println("Create shift canceled");
                        return;
                    }
                }
                else
                {
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

                List<Worker> available_boss = blService.getAvailableWorkers(date, shiftTime);

                if (available_boss.isEmpty()) {
                    System.out.println("Error : no available boss for this shift! returning to shifts view screen...");
                    return;
                }

                System.out.println(available_boss.toString());
                System.out.println("enter the id of the worker who you wish to appoint as a boss");
                int boss_id = getChoice(Main.id_lower_bound , Main.id_upper_bound);
                Worker boss = blService.getWorker(boss_id);
                if(boss == null)
                {
                    System.out.println("Error : no worker with such id");
                    return;
                }
                if (!blService.isAvailable(boss, date, shiftTime)) {
                    System.out.println("Error : this worker is not available to work in this shift!");
                    return;
                }

                Map<WorkPolicy.WorkingType, List<Integer>> working_team = new HashMap<>();

                Shift shift = new Shift(address,date, shiftTime, boss, working_team);
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

            int choice = getChoice(1, 5);
            switch (choice) {
                case 1:
                    System.out.println("Type the new name :");
                    String edited_name = keyboard.nextLine();
                    worker.setName(edited_name);
                    blService.updateWorker(worker);
                    break;
                case 2:
                    System.out.println("Type the new id :");
                    int edited_id = getChoice(Main.id_lower_bound,Main.id_upper_bound);
                    if(blService.getWorker(edited_id)!= null)
                    {
                        System.out.println("Error : There is already a worker with such id!");
                        break;
                    }

                    if(blService.updateWorkerID(worker_id , edited_id))
                        worker_id = edited_id;
                    break;
                case 3:
                    System.out.println("Type the new bank address :");
                    String edited_address = keyboard.nextLine();
                    worker.getContract().setBankAddress(edited_address);
                    blService.updateContract(worker_id , worker.getContract());
                    break;
                case 4:
                    System.out.println("Type the new salary :");
                    double edited_salary = getDoubleChoice(0,Double.MAX_VALUE);
                    worker.getContract().setSalary(edited_salary);
                    blService.updateContract(worker_id , worker.getContract());
                    break;
                case 5:
                    go_back = true;
                    break;


            }
        }

        return worker_id;
    }

    public static void AddAddress()
    {

        System.out.println("Enter the location:");
        String location = keyboard.nextLine();
        if(blService.getAddress(location) != null)
        {
            System.out.println("Error : an address with this location already exists");
            return;
        }
        System.out.println("Enter the contact name:");
        String contact_name = keyboard.nextLine();
        System.out.println("Enter the contact's phone number:");
        String phone_number = keyboard.nextLine();

        if(blService.addAddress(new Address(location,contact_name, phone_number)))
        {
            System.out.println("The address has been added successfully!");
        }
        else
        {
            System.out.println("Error : Couldn't add address");
        }

    }

    public static void AddTruck()
    {
        System.out.println("Enter the truck's serial number:");
        String serialNumber = keyboard.nextLine();
        if(blService.getTruck(serialNumber) != null)
        {
            System.out.println("Error : a truck with this serial number already exists");
            return;
        }
        System.out.println("Enter the truck's model:");
        String model = keyboard.nextLine();
        System.out.println("Enter the truck's weight:");
        int weight = getChoice(0, Integer.MAX_VALUE);
        System.out.println("Enter the truck's max allowed weight:");
        int maxAllowedWeight = getChoice(0, Integer.MAX_VALUE);

        if(blService.addTruck(new Truck(serialNumber,model, weight,maxAllowedWeight)))
        {
            System.out.println("The truck has been added successfully!");
        }
        else
        {
            System.out.println("Error : Couldn't add truck");
        }


    }


    public static void AddProduct()
    {

        System.out.println("Enter the product's CN:");
        String product_cn = keyboard.nextLine();
        if(blService.getProduct(product_cn) != null)
        {
            System.out.println("Error : a product with this cn already exists");
            return;
        }
        System.out.println("Enter the product's name:");
        String product_name = keyboard.nextLine();
        System.out.println("Enter the product's weight:");
        int weight = getChoice(0, Integer.MAX_VALUE);


        if(blService.addProduct(new Product(product_name,product_cn, weight)))
        {
            System.out.println("The product has been added successfully!");
        }
        else
        {
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

    private static boolean getConfirmation()
    {
        for(;;)
        {
            String keyboard_input = keyboard.nextLine();
            if(keyboard_input.equals("y")|| keyboard_input.equals("Y"))
                return true;
            else if(keyboard_input.equals("n")|| keyboard_input.equals("N"))
                return false;
            else
                System.out.println("Error : Invalid input ! type n to cancel or y to confirm");
        }

    }
}
