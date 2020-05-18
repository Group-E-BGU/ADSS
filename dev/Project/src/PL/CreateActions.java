package PL;

import BL.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateActions {

    static Scanner keyboard = new Scanner(System.in);
    static BLService blService = new BLService();

    public void createShift() {

        for (; ; ) {
            System.out.println("enter the date using this format dd/mm/yyyy or type EXIT to cancel ...");
            String date_string = keyboard.next();
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

                String available_bosses = blService.AvilableWorkerstoString(date, shiftTime);
                if (available_bosses.equals("")) {
                    System.out.println("Error : no available boss for this shift! returning to shifts view screen...");
                    return;
                }

                System.out.println(available_bosses);
                System.out.println("enter the id of the worker who you wish to appoint as a boss");
                int boss_id = keyboard.nextInt();
                Worker boss = blService.getWorker(boss_id);
                if (!blService.isAvailable(boss, date, shiftTime)) {
                    System.out.println("Error : this worker is not available to work in this shift!");
                    return;
                }

                Map<WorkPolicy.WorkingType, List<Worker>> working_team = new HashMap<>();

                Shift shift = new Shift(date, shiftTime, boss, working_team);
                boolean success = blService.addShift(shift);
                if (success)
                    System.out.println("The shift has been created successfully");
                else
                    System.out.println("Error : the shift already exists");
                return;

            } catch (ParseException pe) {
                System.out.println("Error : invalid input");
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

    public void editWorker(int worker_id) {

        Worker worker = blService.getWorker(worker_id);
        if(worker == null)
        {
            System.out.println("Error : no worker with such id!");
            return;
        }

        WorkPolicy.WorkingType[] current_types = WorkPolicy.WorkingType.values();
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
                    int edited_id = getChoice(Main.id_lower_bound,Main.id_upper_bound);
                    worker.setID(edited_id);
                    blService.updateWorker(worker);
                    break;
                case 3:
                    System.out.println("choose the worker's new job by typing the number near it :");
                    Printer.printAllWorkingTypes();
                    int type_choice = getChoice(1, WorkPolicy.WorkingType.values().length);

                    WorkPolicy.WorkingType workingType = WorkPolicy.WorkingType.values()[Math.abs(type_choice) - 1];

                    blService.updateWorker(worker);



                    break;
                case 4:
                    System.out.println("Type the new bank address :");
                    String edited_address = keyboard.nextLine();
                    worker.getContract().setBankAddress(edited_address);
                    blService.updateContract(worker_id , worker.getContract());
                    break;
                case 5:
                    System.out.println("Type the new salary :");
                    double edited_salary = getDoubleChoice(0,Double.MAX_VALUE);
                    worker.getContract().setSalary(edited_salary);
                    blService.updateContract(worker_id , worker.getContract());
                    break;
                case 6:
                    go_back = true;
                    break;


            }
        }

    }

    private static int getChoice(int lower_bound, int upper_bound) {
        for (; ; ) {
            int keyboard_input = keyboard.nextInt();

            if (keyboard_input < lower_bound || keyboard_input > upper_bound) {
                System.out.println("Error : number out of bounds!");
            } else return keyboard_input;
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
}
