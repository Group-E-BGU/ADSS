package PL;


import BL.*;
import BL.Shift.ShiftTime;
import BL.WorkPolicy.WorkingType;
import javafx.util.Pair;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

public class Main {

    public static int id_lower_bound = 100000000;
    public static int id_upper_bound = 999999999;
    static Scanner keyboard = new Scanner(System.in);
    static BLService blService = new BLService();
    static InitializeData init_data = new InitializeData();

    public static void main(String[] argv) {

        boolean terminate = false;

        init_data.createWorkers();
        init_data.createShifts();
        blService.loadFromDataBase();


        while (!terminate) {
            Printer.printMainMenu();
            int choice = getChoice(1, 7);

            switch (choice) {
                case 1:
                    workersView();
                    break;
                case 2:
                    shiftsView();
                    break;
                case 3:
                    break;
                case 4:
                    addressesView();
                    break;
                case 5:
                    trucksView();
                    break;
                case 6:
                    productsView();
                    break;
                case 7:
                    terminate = true;
                    break;
            }

            Printer.border();
        }

    }

    private static void workersView() {

        boolean go_back = false;

        while (!go_back) {

            Printer.printWorkersView();
            int choice = getChoice(1, 3);

            switch (choice) {
                case 1:
                    new CreateActions().registerWorker();
                    break;
                case 2:
                    if (Workers.getInstance().getAllWorkers().isEmpty()) {
                        System.out.println("Error : there are no workers!");
                        break;
                    }
                    System.out.println("enter the worker id :");
                    int id = getChoice(id_lower_bound , id_upper_bound);
                    Printer.border();
                    if (blService.getWorker(id)==null) {
                        System.out.println("Error : no worker with such id");
                    } else workerView(id);
                    break;

                case 3:
                    go_back = true;
                    break;


            }

        }


    }

    private static void workerView(int worker_id) {
        Worker w = blService.getWorker(worker_id);

        if (w == null) {
            System.out.println("Error : no worker with such id");
            return;
        }

        boolean go_back = false;
        while (!go_back) {

            Printer.PrintWorkerView(worker_id);

            int choice = getChoice(1, 5);

            switch (choice) {
                case 1:
                    Printer.printSchedule(worker_id);
                    break;
                case 2:
                    System.out.println(w.getContract().toString());
                    break;
                case 3:
                    Printer.printWorkingShifts(w);
                    break;
                case 4:
                    worker_id = new CreateActions().editWorker(worker_id);
                    break;

                case 5:
                    go_back= true;
                    break;
            }
            Printer.border();
        }
    }


    private static void shiftsView() {

        boolean go_back = false;
        while (!go_back) {

            Printer.printShiftsView();
            int choice = getChoice(1, 3);
            switch (choice) {
                case 1:
                    if (History.getInstance().getShifts().isEmpty()) {
                        System.out.println("There are no shifts at the moment.");
                        break;
                    }
                    System.out.println("enter the shift id :");
                    int id = getChoice(0,Integer.MAX_VALUE);
                    shiftView(id);
                    Printer.border();
                    break;
                case 2:
                    new CreateActions().createShift();
                    break;
                case 3:
                    go_back = true;
                    break;
            }
        }

    }

    private static void shiftView(int shift_id) {
        Shift shift = blService.getShift(shift_id);

        if (shift == null) {
            System.out.println("Error : invalid shift id!");
            return;
        }

        boolean go_back = false;
        while (!go_back) {
            Printer.printShiftView(shift_id);

            int first_choice = getChoice(1, 2);
            switch (first_choice) {
                case 1:
                    Printer.printAvailableWorkers(shift);
                    System.out.println("1) add a worker to this shift");
                    System.out.println("2) return");
                    int second_choice = getChoice(1, 2);
                    if (second_choice == 2)
                        break;
                    else {
                        System.out.println("enter the id of the worker you want to add");
                        int worker_id = getChoice(id_lower_bound,id_upper_bound);
                        Worker w = blService.getWorker(worker_id);
                        if (w == null) {
                            System.out.println("Error : no worker with such id");
                        }
                        else if (!blService.addToWorkingTeam(shift, w, w.getType())) {
                            System.out.println("Error : " + w.getName() + " is not available to work in this shift!");
                        }
                        else
                        {
                            System.out.println("worker added successfully to shift");
                        }
                    }
                    break;
                case 2:
                    go_back = true;
                    break;

            }
            Printer.border();
        }

    }
// -------------------------------------- Addresses ----------------------------------------  //

    private static void addressesView()
    {

        boolean terminate = false;

        while(!terminate) {

            Printer.printAddressesView();

            int choice = getChoice(1, 2);
            switch (choice) {
                case 1:
                    CreateActions.AddAddress();
                    break;
                case 2:
                    terminate = true;
                    break;

            }
        }

    }

// -------------------------------------- Trucks ----------------------------------------  //

    private static void trucksView()
    {

        boolean terminate = false;

        while(!terminate) {

            Printer.printTrucksView();

            int choice = getChoice(1, 2);
            switch (choice) {
                case 1:
                    CreateActions.AddTruck();
                    break;
                case 2:
                    terminate = true;
                    break;

            }
        }

    }

// -------------------------------------- Products ----------------------------------------  //

    private static void productsView()
    {

        boolean terminate = false;

        while(!terminate) {

            Printer.printProductsView();

            int choice = getChoice(1, 2);
            switch (choice) {
                case 1:
                    CreateActions.AddProduct();
                    break;
                case 2:
                    terminate = true;
                    break;

            }
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

