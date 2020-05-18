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
                String choice = keyboard.next();

                if (choice.equals("M") || choice.equals("m")) {
                    shiftTime = Shift.ShiftTime.Morning;
                } else if (choice.equals("E") || choice.equals("e")) {
                    shiftTime = Shift.ShiftTime.Evening;
                }

                System.out.println(Workers.getInstance().AvilableWorkerstoString(date,shiftTime));
                System.out.println("enter the id of the worker who you wish to appoint as a boss");
                int boss_id = keyboard.nextInt();
                Worker boss = Workers.getInstance().getWorker(boss_id);
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
        int worker_id = keyboard.nextInt();
        if(Workers.getInstance().getWorker(worker_id)!=null)
        {
            System.out.println("Error : There is a user with the same id in the data base!");
            return;
        }
        System.out.println("Worker name: ");
        String worker_name = keyboard.next();
        List<WorkPolicy.WorkingType> jobs = Arrays.asList(WorkPolicy.WorkingType.values());
        boolean stop = false;
        System.out.println("Working types : ");
        Printer.printAllWorkingTypes();
        List<WorkPolicy.WorkingType> worker_jobs = new LinkedList<>();
        while (!stop) {
            System.out.println("Choose one of the working types :");
            int workingType_id = keyboard.nextInt();
            worker_jobs.add(jobs.get(workingType_id - 1));
            System.out.println("choose another ? y/n");
            if (keyboard.next().equals("n"))
                stop = true;

        }

        System.out.println("Enter constraints : ");
        keyboard.next();
        System.out.println("Worker salary :");
        double salary = keyboard.nextDouble();
        System.out.println("Worker bank address :");
        String address = keyboard.next();
        SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
        Date current_date = new Date();
        WorkerDeal deal = null;
        try {
            deal = new WorkerDeal(worker_id, (date_format.parse(date_format.format(current_date))), salary, address, new LinkedList<String>());
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        Worker worker = new Worker(worker_id, worker_name, worker_jobs, new InitializeData().createSchedule(), deal);
        Workers.getInstance().addWorker(worker);

    }

    public void editWorker(int worker_id) {
        Worker worker = Workers.getInstance().getWorker(worker_id);
        WorkPolicy.WorkingType[] current_types = WorkPolicy.WorkingType.values();
        boolean go_back = false;
        while (!go_back) {
            System.out.println("1) Edit worker name");
            System.out.println("2) Edit worker id");
            System.out.println("3) Edit worker jobs");
            System.out.println("4) Edit worker bank address");
            System.out.println("5) Edit worker salary");
            System.out.println("6) Return");

            int choice = getChoice(1, 6);
            switch (choice) {
                case 1:
                    System.out.println("Type the new name :");
                    String edited_name = keyboard.next();
                    Workers.getInstance().getWorker(worker_id).setName(edited_name);
                    break;
                case 2:
                    System.out.println("Type the new id :");
                    int edited_id = keyboard.nextInt();
                    Workers.getInstance().getWorker(worker_id).setID(edited_id);
                    break;
                case 3:
                    System.out.println("choose the type you want to add/remove :");
                    System.out.println("to add type the number to remove type -number...");
                    Printer.printAllWorkingTypes();
                    int type_choice = getChoice(-1 * WorkPolicy.WorkingType.values().length, WorkPolicy.WorkingType.values().length);
                    if (type_choice > 0) {
                        WorkPolicy.WorkingType workingType = WorkPolicy.WorkingType.values()[Math.abs(type_choice) - 1];
                        if (worker.getType().contains(workingType))
                            System.out.println("the worker is already signed for this job!");
                        else
                            worker.getType().add(workingType);

                    } else if (type_choice < 0) {
                        WorkPolicy.WorkingType workingType = WorkPolicy.WorkingType.values()[Math.abs(type_choice) - 1];
                        if (!worker.getType().contains(workingType))

                            System.out.println("the worker is not signed for this job");

                        else
                            worker.getType().remove(workingType);
                    }

                    else
                        System.out.println("Error : number out of bounds!");

                    break;
                case 4:
                    System.out.println("Type the new bank address :");
                    String edited_address = keyboard.next();
                    Workers.getInstance().getWorker(worker_id).getContract().setBankAddress(edited_address);
                    break;
                case 5:
                    System.out.println("Type the new salary :");
                    double edited_salary = keyboard.nextDouble();
                    Workers.getInstance().getWorker(worker_id).getContract().setSalary(edited_salary);
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
}
