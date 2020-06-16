package PresentationLayer;

import BusinessLayer.*;
import InterfaceLayer.SystemManager;

import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

public class SystemAccess
{

    static Scanner keyboard = new Scanner(System.in);
//    private static SystemManager Sys = new SystemManager();
    static BLService blService = BLService.getInstance();


    public static void register(SystemManager Sys)
    {
        List<String> available_locations = blService.getAvailableAddressesRegister();
        if(available_locations.isEmpty())
        {
            System.out.println("no addresses available!");
            return;
        }
        boolean address_chosen = false;

        while (!address_chosen)
        {
            System.out.println("please choose one of the available addresses : ");
            Printer.printAddresses(available_locations);
            String location = keyboard.nextLine();
            if(blService.getAddress(location)==null || !available_locations.contains(location))
            {
                System.out.println("Error : please type the location of the desired address!");
                System.out.println("do you want to try again ? y/n");
                if(!getConfirmation())
                {
                    System.out.println("Your registration has been canceled");
                    return;
                }
            }
            else
            {
                address_chosen = true;
            }
        }

        String email;
        String password;
        System.out.println("Please enter your email");
        email = keyboard.nextLine();
        String Ex=Sys.CheckEmailExist(email);
        if(!Ex.equals("Not Exist")){
            System.out.println(Ex);
        }
        else {
            System.out.println("Please enter password");
            password = keyboard.nextLine();
            String Done = Sys.Register(email, password);
            if (Done.equals("Done")) {
                System.out.println("The registration was successful");
            } else
                System.out.println(Done);
        }
    }


    public static boolean login(SystemManager Sys)
    {
        String email;
        String password;
        System.out.println("Please enter your email");
        email = keyboard.nextLine();
        String Ex = Sys.CheckEmailExist(email);
        boolean done = true;
        if(!Ex.equals("Exist")) {
            System.out.println(Ex);
            done=false;
        }
        if (done) {
            System.out.println("Please enter password");
            password = keyboard.nextLine();
            String Done = Sys.Login(email, password);
            if (!Done.equals("Done")) {
                System.out.println("wrong password");
                done=false;
            }
        }
        if(done) {
            System.out.println(email + " welcome to your super!");
            //       GetOrderDetails();
            //       Action();
            return true;

        }
        return false;
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
}
