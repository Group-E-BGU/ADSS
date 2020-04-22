package PL;


import BL.*;
import BL.Shift.ShiftTime;
import BL.WorkPolicy.WorkingType;
import javafx.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;

public class Main {

    static Scanner keyboard = new Scanner(System.in);
    static InitializeData init_data = new InitializeData();

    public static void main(String[] argv) {

        boolean terminate = false;

        init_data.createWorkers();
        init_data.createShifts();


        while (!terminate) {
            Printer.printMainMenu();
            int choice = getChoice(1, 3);

            switch (choice) {
                case 1:
                    workersView();
                    break;
                case 2:
                    shiftsView();
                    break;
                case 3:
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
                    int id = keyboard.nextInt();
                    Printer.border();
                    if (!Workers.getInstance().getAllWorkers().containsKey(id)) {
                        System.out.println(ConsoleColors.RED_BOLD + "Error : invalid id" + ConsoleColors.RESET);
                    } else workerView(id);
                    break;

                case 3:
                    go_back = true;
                    break;


            }

        }


    }

    private static void workerView(int worker_id) {
        Worker w = Workers.getInstance().getWorker(worker_id);

        boolean go_back = false;
        while (!go_back) {

            Printer.PrintWorkerView(worker_id);

            int choice = getChoice(1, 5);

            switch (choice) {
                case 1:
                    Printer.printSchedule(worker_id);
                    break;
                case 2:
                    System.out.println(ConsoleColors.CYAN_BOLD + w.getContract().toString() + ConsoleColors.RESET);
                    break;
                case 3:
                    Printer.printWorkingShifts(w);
                    break;
                case 4:
                    new CreateActions().editWorker(worker_id);
                    break;
                case 5:
                    go_back = true;
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
                    int id = keyboard.nextInt();
                    Printer.border();
                    if (!History.getInstance().isAvailable(id)) {
                        System.out.println(ConsoleColors.RED_BOLD + "Error : invalid shift_id!" + ConsoleColors.RESET);
                    } else shiftView(id);
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
        Shift shift = History.getInstance().getShifts().get(shift_id);

        boolean go_back = false;
        while (!go_back) {
            Printer.printShiftView(shift_id);

            int first_choice = getChoice(1, 2);
            switch (first_choice) {
                case 1:
                    List<Worker> available_workers = Workers.getInstance().getAvailableWorkers(shift.getShiftDate(), shift.getShiftTime());
                    System.out.println(ConsoleColors.BLUE_BOLD + available_workers.toString() + ConsoleColors.RESET);
                    System.out.println("1) add a worker to this shift");
                    System.out.println("2) return");
                    int second_choice = getChoice(1, 2);
                    if (second_choice == 2)
                        break;
                    else if (available_workers.isEmpty()) {
                        System.out.println(ConsoleColors.RED_BOLD + "Error : there are no workers to choose from!" + ConsoleColors.RESET);

                    } else {
                        System.out.println("enter the id of the worker you want to add");
                        int worker_id = keyboard.nextInt();
                        Worker w = Workers.getInstance().getWorker(worker_id);
                        System.out.println("which job will "+ w.getName()+" do?");
                        int index =1;
                        for(WorkingType type : w.getType())
                        {
                            System.out.println(ConsoleColors.YELLOW_BOLD+index+") "+type+ConsoleColors.RESET);
                            index++;
                        }
                        int type_index = getChoice(1,w.getType().size())-1;
                        shift.addToWorkingTeam(w, w.getType().get(type_index));
                    }
                    break;
                case 2:
                    go_back = true;
                    break;

            }
            Printer.border();
        }

    }

    private static int getChoice(int lower_bound, int upper_bound) {
        for (; ; ) {
            int keyboard_input = keyboard.nextInt();
            if (keyboard_input < lower_bound || keyboard_input > upper_bound) {
                System.out.println(ConsoleColors.RED_BOLD + "Error : number out of bounds!" + ConsoleColors.RESET);
            } else return keyboard_input;
        }
    }
}

