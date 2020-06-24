package PresentationLayer;


import java.math.BigInteger;

import BusinessLayer.*;
import BusinessLayer.InterfaceContract;
import BusinessLayer.InterfaceOrder;
import BusinessLayer.InterfaceSupplier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static int id_lower_bound = 100000000;
    public static int id_upper_bound = 999999999;
    static Scanner keyboard = new Scanner(System.in);
    static BLService blService = BLService.getInstance();
    static InitializeData init_data = new InitializeData();

    public static void main(String[] argv) {

    //    blService.initializeDB();
        init_data.createWorkers();
    //    init_data.createShifts();
        init_data.createMenus();
        blService.loadFromDataBase();
        //init_data.testArrange();


        boolean terminate = false;
        System.out.println("Welcome to 'super Le'!\n");
        while (!terminate) {
            System.out.println("What action would you like to take now?\n" +
                    "please enter the correct number\n" +
                    "1. Register\n" +
                    "2. Login\n" +
                    "3. Exit");
            int choice = getChoice(1, 4);
            switch (choice) {
                case 1:
                    SystemAccess.register();
                    break;
                case 2:
                    if (SystemAccess.login())
                        blService.DoDelivery(); //todo check if here
                        actionList();
                    break;
                case 3:
                    System.out.println("GoodBye!");
                    terminate = true;
                    break;
                default:
                    System.out.println("Please enter a valid number from the menu");
                    break;

            }
        }

    }

    private static void actionList() {
        boolean terminate = false;
        Map<Integer,MenuOption> allowed_options = Printer.initMenu(blService.getLogged_user().getUserType());

        while (!terminate) {
            Printer.printMainMenu(allowed_options);
            int choice = getChoice(1, allowed_options.size());

            choice = new LinkedList<>(allowed_options.keySet()).get(choice-1);

            switch (choice) {
                case 1:
                    workersView();
                    break;
                case 2:
                    shiftsView();
                    break;
                case 3:
                    deliveriesView();
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
                    Add_Edit_Supplier(1);
                    break;
                case 8:
                    Add_Edit_Agreement(1);
                    break;
                case 9:
                    Add_Edit_Write(1);
                    break;
                case 10:
                    MakeOrder();
                    break;
                case 11:
                    DisplayItems();
                    break;
                case 12:
                    DisplaySupplierDetails();
                    break;
                case 13:
                    Add_Edit_Supplier(2);
                    break;
                case 14:
                    Add_Edit_Agreement(2);
                    break;
                case 15:
                    Add_Edit_Write(2);
                    break;
                case 16:
                    DeleteSupplier();
                    break;
                case 17: {
                    System.out.println("Please enter item name");
                    String name = keyboard.nextLine();
                    String amount = blService.getItemAmountsByName(name);
                    System.out.println(amount);
                    if (!amount.equals("No such item in inventory")) {
                        System.out.println("Choose to add or to remove:\n1.Add\n2.Remove");
                        int add_choice = getChoice(1,2);
                        if (add_choice == 1) {
                            System.out.println("Please enter new storage and shelf amounts and expiration date in the following format(dd/MM/yyyy)");
                            String amounts = keyboard.nextLine();
                            System.out.println(blService.addAmounts(name, amounts));
                        } else if (add_choice == 2) {
                            System.out.println(blService.getItemIdsByName(name));
                            System.out.println("Please enter item id or -1 if done");
                            int id = getChoice(-1,Integer.MAX_VALUE);
                            while (id != -1) {
                                System.out.println(blService.removeItem(name, id));
                                System.out.println("Please enter item id or -1 if done");
                                id = getChoice(-1,Integer.MAX_VALUE);
                            }

                        }
                    }
                    break;
                }
                case 18: {
                    System.out.println("Please enter item name");
                    String name = keyboard.nextLine();
                    String amount = blService.getItemAmountsByName(name);
                    System.out.println(amount);
                    if (!amount.equals("No such item in inventory")) {
                        System.out.println("Please enter amount to move");
                        String amounts = keyboard.nextLine();
                        System.out.println(blService.moveToShelf(name, amounts));
                    }
                    break;
                }
                case 19: {
                    System.out.println("Please enter item name");
                    String name = keyboard.nextLine();
                    String amount = blService.getItemAmountsByName(name);
                    System.out.println(amount);
                    if (!amount.equals("No such item in inventory")) {
                        System.out.println(blService.getItemIdsByName(name));
                        System.out.println("Please enter item id or -1 if done");
                        int id = getChoice(-1,Integer.MAX_VALUE);
                        while (id != -1) {
                            System.out.println(blService.removeItemFromShelf(name, id));
                            System.out.println("Please enter item id or -1 if done");
                            id = getChoice(-1,Integer.MAX_VALUE);
                        }
                    }
                    break;

                }
                case 20: {
                    System.out.println("Please enter categories or 'all'");
                    String names = keyboard.nextLine();
                    System.out.println(blService.getInventoryReport(names));
                    break;
                }
                case 21: {
                    System.out.println("Please enter defected item's name");
                    String name = keyboard.nextLine();
                    String amount = blService.getItemAmountsByName(name);
                    System.out.println(amount);
                    if (!amount.equals("No such item in inventory")) {
                        System.out.println(blService.getItemIdsByName(name));
                        System.out.println("Please enter defected item's ID");
                        String id = keyboard.nextLine();
                        System.out.println(blService.setDefectedItem(name, id));
                    }
                    break;
                }
                case 22: {
                    System.out.println("Enter report's beginning date in the following format(dd/MM/yyyy)");
                    String begDate = keyboard.nextLine();
                    System.out.println("Enter report's end date in the following format(dd/MM/yyyy)");
                    String endDate = keyboard.nextLine();
                    System.out.println(blService.printDefectedReport(begDate, endDate));

                    break;
                }
                case 23: {
                    System.out.println("1. Item discount \n" + "2. Category discount\n");
                    String discountType = keyboard.nextLine();
                    if (discountType.equals("1")) {                   //case item discount
                        System.out.println("Enter item name:");
                        String itemName = keyboard.nextLine();
                        System.out.println("Enter discount percentage:");
                        String percentage = keyboard.nextLine();
                        System.out.println("Enter beginning date in the following format(dd/MM/yyyy)");
                        String begDate = keyboard.nextLine();
                        System.out.println("Enter end date in the following format(dd/MM/yyyy)");
                        String endDate = keyboard.nextLine();

                        System.out.println(blService.addNewItemDiscount(itemName, percentage, begDate, endDate));
                    } else if (discountType.equals("2")) {                      //case category discount
                        System.out.println("Enter category name:");
                        String categoryName = keyboard.nextLine();
                        System.out.println("Enter discount percentage:");
                        String percentage = keyboard.nextLine();
                        System.out.println("Enter beginning date in the following format(dd/MM/yyyy)");
                        String begDate = keyboard.nextLine();
                        System.out.println("Enter end date in the following format(dd/MM/yyyy)");
                        String endDate = keyboard.nextLine();

                        System.out.println(blService.addNewCategoryDiscount(categoryName, percentage, begDate, endDate));
                    } else {
                        System.out.println("Please enter valid discount type");
                    }
                    break;
                }
                case 24: {
                    System.out.println("Please enter item name:");
                    String name = keyboard.nextLine();
                    System.out.println("Enter new store price:");
                    String price = keyboard.nextLine();
                    System.out.println("Enter new retail price:");
                    String rPrice = keyboard.nextLine();
                    System.out.println(blService.setNewPrice(name, price, rPrice));
                }
                break;
                case 25:
                    UpdateDetailsOrder();
                    break;
                case 26:
                    CheckcheepSupplier();
                    break;
                case 27:{
                    System.out.println("Do you sure you want to restart the DeliveryId? y/n ");
                    String ans = keyboard.nextLine();
                    if (ans.equals("y")) {
                        blService.RestartDeliveryIdAttheendoftheDay();
                        System.out.println("Successfully initialized");
                    }
                    }
                    break;
                case 28:
                    DeleteOrder();
                    break;
                case 29:
                    Logout();
                    terminate = true;
                    break;
                default:
                    System.out.println("Please enter a valid number from the menu");
                    break;

            }
            Printer.border();
        }

    }

    private static void DeleteOrder() {
        boolean conect = blService.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            Scanner myScanner = new Scanner(System.in);
            LinkedList<Integer> Days = new LinkedList<Integer>();
            System.out.println("Please enter the Order ID you want to Delete");
            int ID_Order = getChoice(0, Integer.MAX_VALUE);
            String Able = blService.CheckAbleToChangeOrder(ID_Order);
            if (Able.equals("Able")) {
                blService.CancelOrder(ID_Order);
                System.out.println("Order canceled");
            }
            else{
                System.out.println("Too late to cancel the order");
            }
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
                    int id = getChoice(id_lower_bound, id_upper_bound);
                    Printer.border();
                    if (blService.getWorker(id) == null) {
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
                    int id = getChoice(0, Integer.MAX_VALUE);
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
                    Printer.printAvailableWorkers(shift_id);
                    System.out.println("1) add a worker to this shift");
                    System.out.println("2) return");
                    int second_choice = getChoice(1, 2);
                    if (second_choice == 2)
                        break;
                    else {
                        System.out.println("enter the id of the worker you want to add");
                        int worker_id = getChoice(id_lower_bound, id_upper_bound);
                        Worker w = blService.getWorker(worker_id);
                        if (w == null) {
                            System.out.println("Error : no worker with such id");
                        } else if (!blService.work(worker_id,shift_id)) {
                            System.out.println("Error : " + w.getName() + " is not available to work in this shift!");
                        } else {
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

// -------------------------------------- Deliveries ----------------------------------------  //

    private static void deliveriesView() {

        boolean go_back = false;

        while (!go_back) {

            Printer.printDeliveriesView();
            int choice = getChoice(1, 3);

            switch (choice) {
                case 1:
                //    CreateActions.arrangeDelivery();
                    break;
                case 2:
                    if (Data.getInstance().getDeliveries().isEmpty()) {
                        System.out.println("Error : there are no deliveries!");
                        break;
                    }
                    System.out.println("enter the delivery id :");
                    int id = getChoice(0, Integer.MAX_VALUE);
                    Printer.border();
                    if (blService.getDelivery(id) == null) {
                        System.out.println("Error : no delivery with such id");
                    } else DeliveryView(id);
                    break;

                case 3:
                    go_back = true;
                    break;


            }

        }


    }

    private static void DeliveryView(int delivery_id) {
        Delivery delivery = blService.getDelivery(delivery_id);

        if (delivery == null) {
            System.out.println("Error : no delivery with such id");
            return;
        }

        boolean go_back = false;
        while (!go_back) {

            Printer.PrintDeliveryView(delivery_id);

            int choice = getChoice(1, 2);

            switch (choice) {

                case 1:
                    Printer.printDocuments(delivery_id);
                    break;
                case 2:
                    go_back = true;
                    break;
            }
            Printer.border();
        }


    }

// -------------------------------------- Addresses ----------------------------------------  //

    private static void addressesView() {

        boolean terminate = false;

        while (!terminate) {

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

    private static void trucksView() {

        boolean terminate = false;

        while (!terminate) {

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

    private static void productsView() {

        boolean terminate = false;

        while (!terminate) {

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
//----------------------------------- Other --------------------------------

    private static void CheckcheepSupplier() {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Please enter the product ID");
        int ProdudtId = getChoice(0,Integer.MAX_VALUE);
        System.out.println("Please enter the amount of the product you would like to invite");
        int Amount = getChoice(0,Integer.MAX_VALUE);;
        InterfaceSupplier Supplier=blService.GetTheCyeeperSuplier(ProdudtId,Amount);
        if(Supplier==null){
            System.out.println("there is no supplier that supply this product");
        }
        else{
            System.out.println("The most profitable supplier to order "+ ProdudtId +" in "+"units, is:\n "+Supplier.Name+"\nwith ID:"+ Supplier.ID);
            System.out.println("The days that the supplier comes to the store are: ");
            if (Supplier.Contract.Days==null|Supplier.Contract.Days.isEmpty()){
                System.out.println("No day,\n" +
                        "The supplier does not arrive on regular days");
            }
            else {
                for (int d:Supplier.Contract.Days
                ) {
                    System.out.print(d+"\n");
                }
                System.out.print("");
            }
        }
    }
/*
    private static void AddArguments() {
        blService.Register("A@gmail.com","123","","");
        blService.Register("Store2@gmail.com","S2_superLi","","");

        blService.Login("A@gmail.com","123");
        Map<Integer,Integer> contactAli1=new ConcurrentHashMap<Integer, Integer>();
        contactAli1.put(2087564,524536272);
        contactAli1.put(2453214,523756223);
        Map<Integer,String> contactAli2=new ConcurrentHashMap<Integer, String>();
        contactAli2.put(2087564,"yoni");
        contactAli2.put(2453214,"roi");
        blService.AddSupplier("Ron",51345,"haprahim, 5, Tel Aviv","Mizrahi","007",873645,"EFT",contactAli2,contactAli1);
        Map<Integer,Integer> contactIKEA1=new ConcurrentHashMap<Integer, Integer>();
        contactIKEA1.put(208231,522136272);
        contactIKEA1.put(4283214,523546253);
        Map<Integer,String> contactIKEA2=new ConcurrentHashMap<Integer, String>();
        contactIKEA2.put(208231,"Dov");
        contactIKEA2.put(4283214,"Leni");
        blService.AddSupplier("Tom",51321,"shalom, 17, Hulon","Ben-Leumi","027",432679,"EFT",contactIKEA2,contactIKEA1);
        Map<Integer,Integer> contacttXiaomi1=new ConcurrentHashMap<Integer, Integer>();
        contacttXiaomi1.put(45337561,522133272);
        Map<Integer,String> contactXiaomi2=new ConcurrentHashMap<Integer, String>();
        contactXiaomi2.put(45337561,"Or");
        blService.AddSupplier("Eli",51328,"shibolet, 11, yafo","Leumi","3456",435678,"EFT",contactXiaomi2,contacttXiaomi1);

        Map<Integer,String> ProductAli1 =new ConcurrentHashMap<Integer, String>();
        ProductAli1.put(12313,"milk");
        int Id_Store = blService.FindId_P_Store("milk", "Dairy products", "3 percent", "liter", "yotvata",100, 5 );
        ProductAli1.put(2314567,"cheese");
        int Id_Store1 = blService.FindId_P_Store("cheese", "Dairy products", "5 percent", "250 ml", "yotvata",100, 5 );
        Map<Integer,Double> ProductAli2 =new ConcurrentHashMap<Integer,Double>();
        ProductAli2.put(12313,4.9);
        ProductAli2.put(2314567,2.9);
        Map<Integer,Integer>  ProductAli3=new ConcurrentHashMap<Integer, Integer>();
        ProductAli3.put(Id_Store,12313);
        ProductAli3.put(Id_Store1,2314567);
        LinkedList<Integer> Days=new LinkedList<>();
        Days.add(2);
        Days.add(3);
        blService.AddContract(51345,false,Days,true,ProductAli3,ProductAli1,ProductAli2);

        Map<Integer,String> ProductIKEA1 =new ConcurrentHashMap<Integer, String>();
        ProductIKEA1.put(143,"milk");
        int Id_Store2 = blService.FindId_P_Store("milk", "Dairy products", "3 percent", "liter", "yotvata",100, 5 );
        ProductIKEA1.put(5432,"cheese");
        int Id_Store3 = blService.FindId_P_Store("cheese", "Dairy products", "5 percent", "250 ml", "yotvata",100, 5  );
        ProductIKEA1.put(22,"cottage");
        int Id_Store4 = blService.FindId_P_Store("cottage", "Dairy products", "5 percent", "250 ml", "yotvata",100, 5  );
        Map<Integer,Double> ProductIKEA2 =new ConcurrentHashMap<Integer,Double>();
        ProductIKEA2.put(143,4.9);
        ProductIKEA2.put(5432,1.9);
        ProductIKEA2.put(22,2.9);
        Map<Integer,Integer>  ProductIKEA3=new ConcurrentHashMap<Integer, Integer>();
        ProductIKEA3.put(Id_Store2,143);
        ProductIKEA3.put(Id_Store3,5432);
        ProductIKEA3.put(Id_Store4,22);
        LinkedList<Integer> Days1=new LinkedList<>();
        Days1.add(5);
        blService.AddContract(51321,false,Days1,true,ProductIKEA3,ProductIKEA1,ProductIKEA2);

        Map<Integer,String> ProductXiaomi1 =new ConcurrentHashMap<Integer, String>();
        ProductXiaomi1.put(142356,"Potatoes");
        int Id_Store5 = blService.FindId_P_Store("Potatoes", "vegetables", "whites", "bag", "Harez",100, 13 );
        ProductXiaomi1.put(46288,"Carrots");
        int Id_Store6 = blService.FindId_P_Store("Carrots", "vegetables", "orange", "bag", "Harez",100, 14 );
        ProductXiaomi1.put(4328,"Potatoes");
        int Id_Store7= blService.FindId_P_Store("RedPotatoes", "vegetables", "red", "bag", "Harez",100, 14 );
        Map<Integer,Double> ProductXiaomi2 =new ConcurrentHashMap<Integer,Double>();
        ProductXiaomi2.put(142356,13.9);
        ProductXiaomi2.put(46288,8.9);
        ProductXiaomi2.put(4328,13.9);
        Map<Integer,Integer>  ProductXiaomi3=new ConcurrentHashMap<Integer, Integer>();
        ProductXiaomi3.put(Id_Store5,142356);
        ProductXiaomi3.put(Id_Store6,46288);
        ProductXiaomi3.put(Id_Store7,4328);
        LinkedList<Integer> Days2=new LinkedList<>();
        Days2.add(4);
        blService.AddContract(51328,false,Days2,true,ProductXiaomi3,ProductXiaomi1,ProductXiaomi2);

        Map<Integer,Integer> WriteAli1=new ConcurrentHashMap<Integer, Integer>();
        WriteAli1.put(12313,100);
        Map<Integer,Double> WriteAli2=new ConcurrentHashMap<Integer, Double>();
        WriteAli2.put(12313,10.0);
        blService.AddWrite(51345,WriteAli1,WriteAli2);
        Map<Integer,Integer> WriteXiaomi1=new ConcurrentHashMap<Integer, Integer>();
        WriteXiaomi1.put(142356,50);
        WriteXiaomi1.put(4328,70);
        Map<Integer,Double> WriteXiaomi2=new ConcurrentHashMap<Integer, Double>();
        WriteXiaomi2.put(142356,10.0);
        WriteXiaomi2.put(4328,10.0);
        blService.AddWrite(51328,WriteXiaomi1,WriteXiaomi2);

        Map<Integer,Integer> o1=new HashMap<Integer, Integer>();
        o1.put(12313,150);
        blService.MakeOrder(51345,Days,o1);
        Map<Integer,Integer> o2=new HashMap<Integer, Integer>();
        o2.put(5432,150);
        o2.put(22,150);
        blService.MakeOrder(51321,Days1,o2);
        Map<Integer,Integer> o3=new HashMap<Integer, Integer>();
        o3.put(142356,150);
        o3.put(46288,150);
        o3.put(4328,150);
        blService.MakeOrder(51328,Days2,o3);

        blService.addNewItemDiscount("milk","20","20/04/2020","20/06/2020");
        blService.addNewItemDiscount("Carrots","30","20/03/2020","20/06/2021");
        blService.addNewItemDiscount("Potatoes","20","20/04/2020","21/04/2020");

        blService.addNewCategoryDiscount("vegetables" ,"30","20/04/2020","20/06/2020");
        blService.addNewCategoryDiscount("3 percent" ,"25","20/04/2020","20/06/2020");

        blService.setNewPrice("milk","15","5");
        blService.setNewPrice("milk","10","5");
        blService.setNewPrice("cheese","15","10");
        blService.setNewPrice("Carrots","8","3");
        blService.setNewPrice("Potatoes","22","15");
        blService.setNewPrice("Potatoes","20","13");

        blService.addAmounts("milk","50 50 30/05/2020");
        blService.addAmounts("cheese","80 50 30/05/2020");
        blService.addAmounts("Carrots","70 50 30/08/2020");
        blService.addAmounts("Potatoes","65 50 30/08/2020");
        blService.addAmounts("RedPotatoes","40 80 30/08/2020");
        blService.addAmounts("cottage","50 110 30/05/2020");


        blService.Logout();

        blService.Login("Store2@gmail.com","S2_superLi");
        Map<Integer,String> contactAli22=new ConcurrentHashMap<Integer, String>();
        contactAli22.put(2087564,"yoni");
        contactAli22.put(2453214,"roi");
        Map<Integer,Integer> contactAli11=new ConcurrentHashMap<Integer, Integer>();
        contactAli11.put(2087564,0524536272);
        contactAli11.put(2453214,0523756223);
        blService.AddSupplier("Ali",51345,"hprahim, 5, Tel Aviv","Mizrahi","007",873645,"EFT",contactAli22,contactAli11);
        Map<Integer,String> contactIKEA22=new ConcurrentHashMap<Integer, String>();
        contactIKEA22.put(208231,"Dov");
        contactIKEA22.put(4283214,"Leni");
        Map<Integer,Integer> contactIKEA11=new ConcurrentHashMap<Integer, Integer>();
        contactIKEA11.put(208231,05221336272);
        contactIKEA11.put(4283214,0523546253);
        blService.AddSupplier("IKEA",51321,"shalom, 17, Hulon","Ben-Leumi","027",432679,"EFT",contactIKEA22,contactIKEA11);

        Map<Integer,String> ProductAli11 =new ConcurrentHashMap<Integer, String>();
        ProductAli11.put(1213,"blanket");
        ProductAli11.put(43567,"Pillow");
        Map<Integer,Double> ProductAli22 =new ConcurrentHashMap<Integer,Double>();
        ProductAli22.put(1213,89.9);
        ProductAli22.put(43567,1399.9);
        Map<Integer,Integer>  ProductAli33=new ConcurrentHashMap<Integer, Integer>();
        blService.AddContract(51345,false,Days,true,ProductAli33,ProductAli11,ProductAli22);
        Map<Integer,String> ProductIKEA11 =new ConcurrentHashMap<Integer, String>();
        ProductIKEA11.put(223,"Armchair");
        ProductIKEA11.put(345,"Desk");
        ProductIKEA11.put(1687,"Chair");
        Map<Integer,Double> ProductIKEA22 =new ConcurrentHashMap<Integer,Double>();
        ProductIKEA22.put(223,499.9);
        ProductIKEA22.put(345,1399.9);
        ProductIKEA22.put(1687,139.9);
        Map<Integer,Integer>  ProductIKEA33=new ConcurrentHashMap<Integer, Integer>();
        blService.AddContract(51321,false,Days,true,ProductIKEA33,ProductIKEA11,ProductIKEA22);

        Map<Integer,Double> WriteAli22=new ConcurrentHashMap<Integer, Double>();
        WriteAli22.put(1213,13.0);
        Map<Integer,Integer> WriteAli11=new ConcurrentHashMap<Integer, Integer>();
        WriteAli11.put(1213,100);
        blService.AddWrite(51328,WriteAli11,WriteAli22);
        Map<Integer,Integer> WriteIKEA11=new ConcurrentHashMap<Integer, Integer>();
        WriteIKEA11.put(223,60);
        WriteIKEA11.put(345,90);
        Map<Integer,Double> WriteIKEA22=new ConcurrentHashMap<Integer, Double>();
        WriteIKEA22.put(223,12.0);
        WriteIKEA22.put(345,12.0);
        blService.AddWrite(51321, WriteIKEA11,WriteIKEA22);

        blService.addNewItemDiscount("blanket","20","20/04/2020","20/06/2020");
        blService.addNewItemDiscount("Desk","30","20/03/2020","20/06/2021");

        blService.setNewPrice("blanket","15","5");
        blService.setNewPrice("blanket","10","5");
        blService.setNewPrice("Desk","15","10");
        blService.setNewPrice("Chair","22","15");

        blService.Logout();
    }

 */


    private static void Add_Edit_Supplier(int status) {
        boolean conect = blService.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            Scanner myScanner = new Scanner(System.in);
            String name;
            int ID;
            String Address;
            int bankNumber;
            String Bank;
            String Branch;
            String payments;
            Map<Integer, String> Contacts_ID = new ConcurrentHashMap<Integer, String>();
            Map<Integer, Integer> Contacts_number = new ConcurrentHashMap<Integer, Integer>();

            System.out.println("Please enter the Supplier's ID");
            ID = getChoice(0,Integer.MAX_VALUE);

            boolean contiue = true;
            boolean MoreContact = true;

            if (status == 2) {
                String exist = blService.CheckSuplierExist(ID);
                while (!exist.equals("Exist")) {
                    System.out.println(exist);
                    System.out.println("Do you want to continue and enter the supplier Id again? y/n ");
                    String ans = myScanner.nextLine();
                    if (ans.equals("y")) {
                        System.out.println("Please enter the Supplier's ID");
                        ID = getChoice(0,Integer.MAX_VALUE);;
                        exist = blService.CheckSuplierExist(ID);
                    } else {
                        {
                            contiue = false;
                            exist = "Done";
                        }
                    }
                }
            }
            if (contiue) {
                System.out.println("Please enter the Supplier's name");
                name = myScanner.nextLine();
                System.out.println("Pleas enter the Supplier's address");
                Address = myScanner.nextLine();
                System.out.println("Please enter the Supplier's Bank");
                Bank = myScanner.nextLine();
                System.out.println("Please enter the Supplier's Branch's Bank");
                Branch = myScanner.nextLine();
                System.out.println("Please enter the Supplier's bankNumber");
                bankNumber = getChoice(0,Integer.MAX_VALUE);;
                System.out.println("Please enter the Supplier's payments");
                payments = myScanner.nextLine();
                while (MoreContact) {
                    String ContactName;
                    int ContactId;
                    System.out.println("Please enter the Contact's name");
                    ContactName = myScanner.nextLine();
                    System.out.println("Please enter the Contact's Id");
                    ContactId = getChoice(id_lower_bound,id_upper_bound);
                    Contacts_ID.put(ContactId, ContactName);
                    int PhoneNumber;
                    System.out.println("Please enter the Contact's Phone number (Without the first digit!)");
                    PhoneNumber = getChoice(0,Integer.MAX_VALUE);;
                    Contacts_number.put(ContactId, PhoneNumber);
                    String ans;
                    System.out.println("Do you have more contact? y/n");
                    ans = myScanner.nextLine();
                    if (ans.equals("n")) {
                        MoreContact = false;
                    }
                }
                String Done="";
                if (status == 1) {
                    Done=blService.AddSupplier(name, ID,Address, Bank, Branch, bankNumber, payments, Contacts_ID, Contacts_number);
                } else {
                    Done=blService.EditSupplier(name, ID,Address, Bank, Branch, bankNumber, payments, Contacts_ID, Contacts_number);
                }
                System.out.println(Done);
            }
        }
    }

    private static void Add_Edit_Agreement(int status) {
        boolean conect = blService.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            Scanner myScanner = new Scanner(System.in);
            int suplaier_ID;
            boolean fixeDays = false;
            LinkedList<Integer> Days = new LinkedList<Integer>();
            boolean leading = true;
            Map<Integer, Integer> ItemsID_ItemsIDSupplier = new ConcurrentHashMap<Integer, Integer>();
            Map<Integer, String> ProductIDVendor_Name = new ConcurrentHashMap<Integer, String>();
            Map<Integer, Double> ProducttemsIDVendor_Price = new ConcurrentHashMap<Integer, Double>();
            boolean contiue = true;

            System.out.println("Please enter the Supplier's ID");
            suplaier_ID = getChoice(0,Integer.MAX_VALUE);;
            if (status == 2) {
                String exist = blService.CheckSAgreementExist(suplaier_ID);
                if (!exist.equals("Done")) {
                    System.out.println(exist);
                    contiue = false;
                }
            }
            if (contiue) {

                System.out.println("Does the supplier bring the supply on regular days? y/n");
                String ans = myScanner.nextLine();
                if (ans.equals("y")) {
                    fixeDays = true;
                    System.out.println("Please enter one day that the supply are expected to arrive. in number");
                    int day = getChoice(0,Integer.MAX_VALUE);;
                    Days.add(day);
                    boolean MoreDay = true;
                    while (MoreDay) {
                        System.out.println("Is the supply expected to arrive in more days? y/n");
                        ans = myScanner.nextLine();
                        if (ans.equals("n"))
                            MoreDay = false;
                        else {
                            System.out.println("Please enter the extra day. in number");
                            day = getChoice(1,7);;
                            if (day < 8 && day > 0) {
                                Days.add(day);
                            } else {
                                System.out.println("the day need to be between 0-7");
                            }
                        }
                    }
                }
                System.out.println("Does the supplier bring the supplier by himself (or it required for transport Syss)? y/n");
                ans = myScanner.nextLine();
                if (ans.equals("n")) {
                    leading = false;
                }
                boolean MoreProduct = true;
                while (MoreProduct) {
                    String Product_Name;
                    String category;
                    String subcategory;
                    String sub_subcategory;
                    String manufacturer;
                    int minAmount;
                    int shelfNumber;
                    System.out.println("Which product the supplier will supply to the store?\n" + "Please enter its name");
                    Product_Name = myScanner.nextLine();
                    System.out.println("Please enter its category");
                    category = myScanner.nextLine();
                    System.out.println("Please enter its subcategory");
                    subcategory = myScanner.nextLine();
                    System.out.println("Please enter its sub_subcategory");
                    sub_subcategory = myScanner.nextLine();
                    System.out.println("Please enter its manufacturer");
                    manufacturer = myScanner.nextLine();
                    System.out.println("Please enter its min amount");
                    minAmount = getChoice(0,Integer.MAX_VALUE);;
                    System.out.println("Please enter its shelf number");
                    shelfNumber = getChoice(0,Integer.MAX_VALUE);;
                    int Id_Store = blService.FindId_P_Store(Product_Name, category, subcategory, sub_subcategory, manufacturer,minAmount, shelfNumber );
                    System.out.println("Please enter his Catalog Number");
                    int product_Id = getChoice(0,Integer.MAX_VALUE);;
                    System.out.println("Please enter the price");
                    double Product_Price = getDoubleChoice(0,Double.MAX_VALUE);
                    ItemsID_ItemsIDSupplier.put(Id_Store, product_Id);
                    ProductIDVendor_Name.put(product_Id, Product_Name);
                    ProducttemsIDVendor_Price.put(product_Id, Product_Price);
                    System.out.println("Does the supplier provide another product? y/n");
                    ans = myScanner.nextLine();
                    if (ans.equals("n")) {
                        MoreProduct = false;
                    }
                }
                switch (status) {
                    case 1:
                        blService.AddContract(suplaier_ID, fixeDays, Days, leading, ItemsID_ItemsIDSupplier, ProductIDVendor_Name, ProducttemsIDVendor_Price);
                        break;
                    case 2:
                        blService.EditContract(suplaier_ID, fixeDays, Days, leading, ItemsID_ItemsIDSupplier, ProductIDVendor_Name, ProducttemsIDVendor_Price);
                        break;
                }
            }
        }
    }

    private static void Add_Edit_Write(int status) {
        boolean conect = blService.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            int Suplaier_ID;
            Map<Integer, Integer> ItemsID_Amount=new ConcurrentHashMap<Integer, Integer>();
            Map<Integer, Double> ItemsID_Assumption =new ConcurrentHashMap<Integer, Double>();
            Scanner myScanner = new Scanner(System.in);
            System.out.println("Please enter the Supplier's ID");
            Suplaier_ID = getChoice(0,Integer.MAX_VALUE);

            boolean contiue=true;
            if (status == 2) {
                String exist = blService.CheckSWortExist(Suplaier_ID);
                while (!exist.equals("Done")) {
                    System.out.println(exist);
                    System.out.println("Do you want to continue and enter the supplier Id again? y/n ");
                    String ans = myScanner.nextLine();
                    if (ans.equals("y")) {
                        System.out.println("Please enter the Supplier's ID");
                        Suplaier_ID = getChoice(0,Integer.MAX_VALUE);;
                        exist = blService.CheckSWortExist(Suplaier_ID);
                    } else {
                        {
                            contiue = false;
                            exist = "Done";
                        }
                    }
                }
            }
            if (contiue) {
                boolean MoreProduct = true;
                System.out.println("Which product would you like to add to the quantities?");
                while ((MoreProduct)) {
                    int Product_ID;
                    int Product_Amount;
                    double Product_Assumption;
                    System.out.println("Please enter the Product's ID");
                    Product_ID = getChoice(0,Integer.MAX_VALUE);;
                    System.out.println("How many units of this product should be purchased to get the discount?");
                    Product_Amount = getChoice(0,Integer.MAX_VALUE);;
                    System.out.println("What percentage of discount will the product receive?");
                    Product_Assumption = getDoubleChoice(0,100);
                    ItemsID_Amount.put(Product_ID, Product_Amount);
                    ItemsID_Assumption.put(Product_ID, Product_Assumption);
                    System.out.println("Would you like to add another product? y/n");
                    String ans = myScanner.nextLine();
                    if (ans.equals("n")) {
                        MoreProduct = false;
                    }
                }
                String Done;
                switch (status) {
                    case 1:
                        Done = blService.AddWrite(Suplaier_ID, ItemsID_Amount, ItemsID_Assumption);
                        break;
                    case 2:
                        Done = blService.EditWrite(Suplaier_ID, ItemsID_Amount, ItemsID_Assumption);
                        break;
                }
            }
        }
    }

    private static void MakeOrder() {
        boolean conect = blService.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            int ID_Suplaier;
            Map<Integer, Integer> ItemsIDVendor_NumberOfItems = new ConcurrentHashMap<Integer, Integer>();
            Scanner myScanner = new Scanner(System.in);
            int day;
            LinkedList<Integer> Days=new LinkedList<Integer>();
            System.out.println("Please enter the Supplier's ID you want to order from");
            ID_Suplaier = getChoice(0,Integer.MAX_VALUE);;
            String exist = blService.CheckSuplierExist(ID_Suplaier);
            if (!exist.equals("Exist")) {
                conect = false;
                System.out.println("the supplier is not exist in the system.");
            }
            if (conect) {
                boolean moreDay=true;
                while(moreDay) {
                    System.out.println("Please enter the day that the order is expected to arrive the store. in number!");
                    day = getChoice(1,7);
                    conect = blService.CheckTheDay(ID_Suplaier, day);
                    if(conect) {
                        Days.add(day);
                        System.out.println("Would you like to add another day? y/n");
                        String ans = myScanner.nextLine();
                        if (ans.equals("n")) {
                            moreDay = false;
                        }
                    }
                    else{
                        System.out.println("the supplier not supply in that day");
                    }
                }
                if (conect) {
                    boolean MoreProduct = true;
                    System.out.println("Which product would you like to add to the Order?");
                    while ((MoreProduct)) {
                        int Product_ID;
                        int Product_Amount;
                        System.out.println("Please enter the Product's ID (According to the supplier)");
                        Product_ID = getChoice(0,Integer.MAX_VALUE);;
                        conect=blService.CheckProductexist(ID_Suplaier,Product_ID);
                        if(conect) {
                            System.out.println("How many units of the product would you like to order??");
                            Product_Amount = getChoice(0,Integer.MAX_VALUE);;
                            ItemsIDVendor_NumberOfItems.put(Product_ID, Product_Amount);
                        }
                        else{
                            System.out.println("This product is not included in the agreement with the supplier.");
                        }
                        System.out.println("Would you like to add another product? y/n");
                        String ans = myScanner.nextLine();
                        if (ans.equals("n")) {
                            MoreProduct = false;
                        }
                    }
                    int Done = blService.MakeOrder(ID_Suplaier, Days, ItemsIDVendor_NumberOfItems);
                    if (Done>=0){
                        InterfaceOrder o = blService.getOrderDetails(Done);
                        if(o!=null){
                            PrintOrder(o);
                        }
                    }
                }
            }
        }

    }

    private static void UpdateDetailsOrder() {
        boolean conect = blService.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            int ID_Suplaier;
            int ID_Order;
            int Product_ID;
            int Product_Amount;
            Map<Integer, Integer> ItemsIDVendor_NumberOfItems = new ConcurrentHashMap<Integer, Integer>();
            Scanner myScanner = new Scanner(System.in);
            LinkedList<Integer> Days = new LinkedList<Integer>();
            System.out.println("Please enter the Order ID you want to change");
            ID_Order = getChoice(0,Integer.MAX_VALUE);;
            String Able = blService.CheckAbleToChangeOrder(ID_Order);
            if (!Able.equals("Able")) {
                conect = false;
                System.out.println("Too late to change order");
            }
            if (conect) {
                ID_Suplaier = blService.GetSupplierID_PerOrder(ID_Order);
                boolean moreDay = true;
                while (moreDay) {
                    int day;
                    System.out.println("Please enter the day that the order is expected to arrive the store. in number!");
                    day = getChoice(1,7);;
                    conect = blService.CheckTheDay(ID_Suplaier, day);
                    if (conect) {
                        Days.add(day);
                        System.out.println("Would you like to add another day? y/n");
                        String ans = myScanner.nextLine();
                        if (ans.equals("n")) {
                            moreDay = false;
                        }
                    }
                }
                if (conect) {
                    boolean LessProduct = false;
                    System.out.println("Would you like to remove product from the Order?  y/n");
                    String ans = myScanner.nextLine();
                    if (ans.equals("y")) {
                        LessProduct = true;
                    }
                    while (LessProduct) {
                        System.out.println("Please enter the Product's ID (According to the supplier)");
                        Product_ID = getChoice(0,Integer.MAX_VALUE);;
                        blService.RemoveProduct(ID_Order, Product_ID);
                        System.out.println("Would you like to remove another product? y/n");
                        ans = myScanner.nextLine();
                        if (ans.equals("n")) {
                            LessProduct = false;
                        }
                    }

                    boolean MoreProduct = false;
                    System.out.println("Would you like to add Product to the Order? n/y");
                    ans = myScanner.nextLine();
                    if (ans.equals("y")) {
                        MoreProduct = true;
                    }
                    while ((MoreProduct)) {
                        System.out.println("Please enter the Product's ID (According to the supplier)");
                        Product_ID = getChoice(0,Integer.MAX_VALUE);;
                        conect = blService.CheckProductexist(ID_Suplaier, Product_ID);
                        if (conect) {
                            System.out.println("How many units of the product would you like to order?");
                            Product_Amount = getChoice(0,Integer.MAX_VALUE);;
                            ItemsIDVendor_NumberOfItems.put(Product_ID, Product_Amount);
                        } else {
                            System.out.println("This product is not included in the agreement with the supplier.");
                        }
                        System.out.println("Would you like to add another product? y/n");
                        ans = myScanner.nextLine();
                        if (ans.equals("n")) {
                            MoreProduct = false;
                        }
                    }
                    InterfaceOrder o = blService.ChangeOrder(ID_Order, ID_Suplaier, Days, ItemsIDVendor_NumberOfItems);
                    if(o!=null){
                        PrintOrder(o);
                    }
                }
                else
                    System.out.println("the supplier arrived to the store in another days.");
            }
        }

    }

    private static void DisplayItems() {
        boolean conect = blService.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            Printer.printSuperItems();
        }
    }

    private static void DisplaySupplierDetails() {
        boolean conect=blService.CheckConected();
        if(!conect){
            System.out.println("You should login");
        }
        if(conect) {
            Printer.printAllSuppliers();
        }
    }
/*
    private static void UpdateOrderStatus() {

        boolean conect = blService.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            int ID_Order;
            Scanner myScanner = new Scanner(System.in);
            System.out.println("Pleas enter the Order ID that arrived to the store");
            ID_Order = getChoice(0,Integer.MAX_VALUE);;
            InterfaceOrder order = blService.getOrderDetails(ID_Order);
            if(order!=null) {
                Map ProductID_Amount=new HashMap<Integer, Integer>();
                Map ProductID_Date=new HashMap<Integer, Integer>();
                for (Map.Entry p:order.ItemsID_ItemsIDVendor.entrySet()
                ) {
                    System.out.println("Did the item with ID "+ p.getKey() +"arrive at the store? y/n");
                    String ans=myScanner.nextLine();
                    if(ans.equals("y")|ans.equals("N")){
                        System.out.println("Please enter the number of units that came from this product");
                        int amount=getChoice(0,Integer.MAX_VALUE);;
                        ProductID_Amount.put(p.getKey(),amount);
                        System.out.println("Please enter the expiry date for the product");
                        int date=myScanner.nextInt();
                        ProductID_Date.put(p.getKey(),date);
                    }
                }
                blService.AddToStore(ProductID_Amount,ProductID_Date);
            }
            if (order==null)
                System.out.println("the Order is mot exist in the system");
        }
    }

 */

    private static void DeleteSupplier() {
        boolean conect = blService.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            Scanner myScanner = new Scanner(System.in);
            int ID;
            System.out.println("Pleas enter the Supplier's ID");
            ID = getChoice(0,Integer.MAX_VALUE);
            blService.DeleteSupplier(ID);
        }
    }

    private static void Logout() {
        String ans=blService.Logout();
        System.out.println(ans);
    }

    public static void printWarning(String warning) {
        System.out.println(warning);
    }

    private static void PrintOrder(InterfaceOrder o) {
        System.out.println("\n Receipt For The Order:"+o.ID_Inventation);
        System.out.println("The Order ID is: "+o.ID_Inventation);
        System.out.println("The Supplier ID is: "+o.ID_Vendor);
        System.out.println("The Days that the order is coming is:"  );
        for (int d: o.Days
        ) {
            System.out.println(d);
        }
        System.out.println("The product that include in the Order is: ");
        System.out.println("Product/Amount");
        for (Map.Entry<Integer,Integer> I:o.ItemsID_ItemsIDVendor.entrySet()
        ) {
            System.out.print(I.getValue()+" ");
            System.out.println(o.ItemsID_NumberOfItems.get(I.getKey()));
        }
        System.out.println("The Total Price of the order is: "+o.TotalPrice);
    }

    private static void GetOrderDetails() {
        System.out.println("Orders that are scheduled to arrive today:");
        LinkedList<InterfaceOrder> Orders=blService.GetOrderDetails();
        for (InterfaceOrder o:Orders
        ) {
            System.out.println("Order ID is: " + o.ID_Inventation);
            System.out.println("Supplier ID is: " + o.ID_Vendor);
            System.out.println("The product that include in the Order is: ");
            for (Map.Entry<Integer, Integer> I : o.ItemsID_ItemsIDVendor.entrySet()
            ) {
                System.out.print(I.getValue());
                System.out.println(o.ItemsID_NumberOfItems.get(I.getKey()));
            }
        }
        System.out.println("\nIf an order arrives at the store,\n" +
                " select the \"Change item amount\" in the menu and fill the Product that arraived to the store\n");

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

    public static double getDoubleChoice(double lower_bound, double upper_bound) {
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


}

