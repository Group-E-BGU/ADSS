package PresentationLayer;

import BusinessLayer.Order;
import InterfaceLayer.InterfaceContract;
import InterfaceLayer.InterfaceOrder;
import InterfaceLayer.InterfaceSupplier;
import InterfaceLayer.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Menu {

    private static SystemManager Sys = new SystemManager();

    public static void main(String[] args) {
            Sys.initializeDB();
            AddArguments();
           // Sys.initialize();
        MainMenu();
    }

    private static void  MainMenu(){
        boolean con=true;
        System.out.println("Welcome to 'super Le'!\n");
        while (con) {
            System.out.println("What action would you like to take now?\n" +
                    "please enter the correct number\n" +
                    "1. Register\n" +
                    "2. Login\n" +
                    "3. Exit");
            Scanner myScanner = new Scanner(System.in);
            int Ask = myScanner.nextInt();
            myScanner.nextLine();
            switch (Ask) {
                case 1:
                    Register();
                    break;
                case 2:
                    Login();
                    break;
                case 3:
                    System.out.println("GoodBye!");
                    con = false;
                    break;
                default:
                    System.out.println("Please enter a valid number from the menu");
                    break;

            }
        }
    }

    private static void Action(){
        boolean con = true;
        while (con) {
            System.out.println(  "What action would you like to take now?\n" +
                    "please enter the correct number\n"+
                    "1. Add a new supplier\n" +
                    "2. Add an agreement to supplier\n" +
                    "3. Adding \"Quantity Write\" to supplier\n" +
                    "4. Make an Fix order\n" +
                    "5. Display the items in the super\n" +
                    "6. Display sll the supplier's details\n"+
                    "7. Update Order Status\n"+
                    "8. Edit supplier details\n"+
                    "9. Edit supplier's arrangement\n" +
                    "10. Edit \"Write Quantities\" of supplier\n" +
                    "11. Delete supplier\n" +
                    "12. Change item amount \n" +
                    "13. Move from storage to shelf \n" +
                    "14. Subtract from shelf \n" +
                    "15. Print inventory report\n" +
                    "16. Enter defected item\n" +//im here
                    "17. Print defective report\n" +
                    "18. Enter new discount\n" +
                    "19. Enter new price\n"+
                    "20. Enter new product\n"+//this should be deleted before submission
                    "21. Update DetailsOrder\n"+
                    "22. Check the Cheaper Supplier for specific product\n"+
                    "23. Logout\n");
            Scanner myScanner = new Scanner(System.in);
            int Ask = myScanner.nextInt();
            myScanner.nextLine();
            switch (Ask) {
                case 1:
                    Add_Edit_Supplier(1);
                    break;
                case 2:
                    Add_Edit_Agreement(1);
                    break;
                case 3:
                    Add_Edit_Write(1);
                    break;
                case 4:
                    MakeOrder();
                    break;
                case 5:
                    DisplayItems();
                    break;
                case 6:
                    DisplaySupplierDetails();
                    break;
                case 7:
                    UpdateOrderStatus();
                    break;
                case 8:
                    Add_Edit_Supplier(2);
                    break;
                case 9:
                    Add_Edit_Agreement(2);
                    break;
                case 10:
                    Add_Edit_Write(2);
                    break;
                case 11:
                    DeleteSupplier();
                    break;
                case 12: {
                    System.out.println("Please enter item name");
                    String name = myScanner.nextLine();
                    String amount = Sys.getItemAmountsByName(name);
                    System.out.println(amount);
                    if (!amount.equals("No such item in inventory")) {
                        System.out.println("Choose to add or to remove:\n1.Add\n2.Remove");
                        int choice = myScanner.nextInt();
                        myScanner.nextLine();
                        if(choice == 1){
                            System.out.println("Please enter new storage and shelf amounts and expiration date in the following format(dd/MM/yyyy)");
                            String amounts = myScanner.nextLine();
                            System.out.println(Sys.addAmounts(name, amounts));
                        } else if (choice == 2) {
                            System.out.println(Sys.getItemIdsByName(name));
                            System.out.println("Please enter item id or -1 if done");
                            int id = myScanner.nextInt();
                            while (id != -1) {
                                System.out.println(Sys.removeItem(name, id));
                                System.out.println("Please enter item id or -1 if done");
                                id = myScanner.nextInt();
                            }

                        } else {
                            System.out.println("number out of range");
                            break;
                        }
                    }
                    break;
                }
                case 13: {
                    System.out.println("Please enter item name");
                    String name = myScanner.nextLine();
                    String amount = Sys.getItemAmountsByName(name);
                    System.out.println(amount);
                    if (!amount.equals("No such item in inventory")) {
                        System.out.println("Please enter amount to move");
                        String amounts = myScanner.nextLine();
                        System.out.println(Sys.moveToShelf(name, amounts));
                    }
                    break;
                }
                case 14: {
                    System.out.println("Please enter item name");
                    String name = myScanner.nextLine();
                    String amount = Sys.getItemAmountsByName(name);
                    System.out.println(amount);
                    if (!amount.equals("No such item in inventory")) {
                        System.out.println("Please enter item id or -1 if done");
                        int id = myScanner.nextInt();
                        while (id != -1) {
                            System.out.println(Sys.removeItemFromShelf(name, id));
                            System.out.println("Please enter item id or -1 if done");
                            id = myScanner.nextInt();
                        }
                    }
                    break;

                }
                case 15: {
                    System.out.println("Please enter categories or 'all'");
                    String names = myScanner.nextLine();
                    System.out.println(Sys.getInventoryReport(names));
                    break;
                }
                case 16: {
                    System.out.println("Please enter defected item's name");
                    String name = myScanner.nextLine();
                    System.out.println("Please enter defected item's ID");
                    String id = myScanner.nextLine();
                    System.out.println(Sys.setDefectedItem(name, id));
                    break;
                }
                case 17: {
                    System.out.println("Enter report's beginning date in the following format(dd/MM/yyyy)");
                    String begDate = myScanner.nextLine();
                    System.out.println("Enter report's end date in the following format(dd/MM/yyyy)");
                    String endDate = myScanner.nextLine();
                    System.out.println(Sys.printDefectedReport(begDate, endDate));

                    break;
                }
                case 18: {
                    System.out.println("1. Item discount \n" + "2. Category discount\n");
                    String discountType = myScanner.nextLine();
                    if (discountType.equals("1")) {                   //case item discount
                        System.out.println("Enter item name:");
                        String itemName = myScanner.nextLine();
                        System.out.println("Enter discount percentage:");
                        String percentage = myScanner.nextLine();
                        System.out.println("Enter beginning date in the following format(dd/MM/yyyy)");
                        String begDate = myScanner.nextLine();
                        System.out.println("Enter end date in the following format(dd/MM/yyyy)");
                        String endDate = myScanner.nextLine();

                        System.out.println(Sys.addNewItemDiscount(itemName, percentage, begDate, endDate));
                    } else if (discountType.equals("2")) {                      //case category discount
                        System.out.println("Enter category name:");
                        String categoryName = myScanner.nextLine();
                        System.out.println("Enter discount percentage:");
                        String percentage = myScanner.nextLine();
                        System.out.println("Enter beginning date in the following format(dd/MM/yyyy)");
                        String begDate = myScanner.nextLine();
                        System.out.println("Enter end date in the following format(dd/MM/yyyy)");
                        String endDate = myScanner.nextLine();

                        System.out.println(Sys.addNewCategoryDiscount(categoryName, percentage, begDate, endDate));
                    }
                    else {
                        System.out.println("Please enter valid discount type");
                    }
                    break;
                }
                case 19: {
                    System.out.println("Please enter item name:");
                    String name = myScanner.nextLine();
                    System.out.println("Enter new store price:");
                    String price = myScanner.nextLine();
                    System.out.println("Enter new retail price:");
                    String rPrice = myScanner.nextLine();
                    System.out.println(Sys.setNewPrice(name, price, rPrice));
                }
                    break;
                case 20: {
                    System.out.println("Please enter new item name:");
                    String name = myScanner.nextLine();
                    System.out.println("Enter the minimum amount required:");
                    String minAmount = myScanner.nextLine();
                    System.out.println("Enter the shelf number of the item:");
                    String shelfNumber = myScanner.nextLine();
                    System.out.println("Enter the manufacturer of the item:");
                    String manufacture = myScanner.nextLine();
                    System.out.println(Sys.addItemRecord(name, minAmount, shelfNumber, manufacture));
                }
                    break;
                case 21:
                    UpdateDetailsOrder();
                    break;
                case 22:
                    CheckcheepSupplier();
                    break;
                case 23:
                    Logout();
                    con=false;
                    break;
                default:
                    System.out.println("Please enter a valid number from the menu");
                    break;
            }

        }
    }

    private static void CheckcheepSupplier() {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Please enter the product ID");
        int ProdudtId = myScanner.nextInt();
        System.out.println("Please enter the amount of the product you would like to invite");
        int Amount = myScanner.nextInt();
        InterfaceSupplier Supplier=Sys.GetTheCyeeperSuplier(ProdudtId,Amount);
        if(Supplier==null){
            System.out.println("there is no supplier that supply this product");
        }
        else{
            System.out.println("The most profitable supplier to order "+ ProdudtId +" in "+" units, is: "+Supplier.Name+"\n with ID:"+ Supplier.ID );
            System.out.println("The days that the supplier comes to the store are: ");
            if (Supplier.Contract.Days==null|Supplier.Contract.Days.isEmpty()){
                System.out.println("No day,\n" +
                        "The supplier does not arrive on regular days ");
            }
            else {
                for (int d:Supplier.Contract.Days
                ) {
                    System.out.print(d);
                }
                System.out.print("");
            }
        }
    }

    private static void AddArguments() {
        Sys.Register("Store1@gmail.com","S1_superLi");
        Sys.Register("Store2@gmail.com","S2_superLi");

        Sys.Login("Store1@gmail.com","S1_superLi");
        Map<Integer,Integer> contactAli1=new ConcurrentHashMap<Integer, Integer>();
        contactAli1.put(2087564,0524536272);
        contactAli1.put(2453214,0523756223);
        Map<Integer,String> contactAli2=new ConcurrentHashMap<Integer, String>();
        contactAli2.put(2087564,"yoni");
        contactAli2.put(2453214,"roi");
        Sys.AddSupplier("Ali",51345,"hprahim, 5, Tel Aviv","Mizrahi","007",873645,"EFT",contactAli2,contactAli1);
        Map<Integer,Integer> contactIKEA1=new ConcurrentHashMap<Integer, Integer>();
        contactIKEA1.put(208231,0522136272);
        contactIKEA1.put(4283214,0523546253);
        Map<Integer,String> contactIKEA2=new ConcurrentHashMap<Integer, String>();
        contactIKEA2.put(208231,"Dov");
        contactIKEA2.put(4283214,"Leni");
        Sys.AddSupplier("IKEA",51321,"shalom, 17, Hulon","Ben-Leumi","027",432679,"EFT",contactIKEA2,contactIKEA1);
        Map<Integer,Integer> contacttXiaomi1=new ConcurrentHashMap<Integer, Integer>();
        contacttXiaomi1.put(45337561,05221336272);
        Map<Integer,String> contactXiaomi2=new ConcurrentHashMap<Integer, String>();
        contactXiaomi2.put(45337561,"Or");
        Sys.AddSupplier("Xiaomi",51328,"shibolet, 11, yafo","Leumi","3456",435678,"EFT",contactXiaomi2,contacttXiaomi1);

        Map<Integer,String> ProductAli1 =new ConcurrentHashMap<Integer, String>();
        ProductAli1.put(12313,"pajamas");
        ProductAli1.put(2314567,"slippers");
        Map<Integer,Double> ProductAli2 =new ConcurrentHashMap<Integer,Double>();
        ProductAli2.put(12313,499.9);
        ProductAli2.put(2314567,69.9);
        Map<Integer,Integer>  ProductAli3=new ConcurrentHashMap<Integer, Integer>();
        LinkedList<Integer> Days=new LinkedList<>();
        Days.add(1);
        Days.add(4);
        Sys.AddContract(51345,false,Days,true,ProductAli3,ProductAli1,ProductAli2);
        Map<Integer,String> ProductIKEA1 =new ConcurrentHashMap<Integer, String>();
        ProductIKEA1.put(143,"table");
        ProductIKEA1.put(5432,"bed");
        ProductIKEA1.put(22,"lamp");
        Map<Integer,Double> ProductIKEA2 =new ConcurrentHashMap<Integer,Double>();
        ProductIKEA2.put(143,499.9);
        ProductIKEA2.put(5432,1399.9);
        ProductIKEA2.put(22,139.9);
        Map<Integer,Integer>  ProductIKEA3=new ConcurrentHashMap<Integer, Integer>();
        Sys.AddContract(51321,false,Days,true,ProductIKEA3,ProductIKEA1,ProductIKEA2);
        Map<Integer,String> ProductXiaomi1 =new ConcurrentHashMap<Integer, String>();
        ProductAli1.put(142356,"smartPhone");
        ProductAli1.put(46288,"headphones");
        ProductAli1.put(4328,"Screen Protector");
        Map<Integer,Double> ProductXiaomi2 =new ConcurrentHashMap<Integer,Double>();
        ProductAli2.put(142356,2499.9);
        ProductAli2.put(46288,29.9);
        ProductAli2.put(4328,79.9);
        Map<Integer,Integer>  ProductXiaomi3=new ConcurrentHashMap<Integer, Integer>();
        Sys.AddContract(51328,false,Days,true,ProductXiaomi3,ProductXiaomi1,ProductXiaomi2);

        Map<Integer,Integer> WriteAli1=new ConcurrentHashMap<Integer, Integer>();
        WriteAli1.put(12313,200);
        Map<Integer,Double> WriteAli2=new ConcurrentHashMap<Integer, Double>();
        WriteAli2.put(12313,10.0);
        Sys.AddWrite(51345,WriteAli1,WriteAli2);
        Map<Integer,Integer> WriteIKEA1=new ConcurrentHashMap<Integer, Integer>();
        WriteIKEA1.put(5432,150);
        WriteIKEA1.put(22,200);
        Map<Integer,Double> WriteIKEA2=new ConcurrentHashMap<Integer, Double>();
        WriteIKEA2.put(5432,10.0);
        WriteIKEA2.put(22,10.0);
        Sys.AddWrite(51321, WriteIKEA1,WriteIKEA2);
        Map<Integer,Integer> WriteXiaomi1=new ConcurrentHashMap<Integer, Integer>();
        WriteXiaomi1.put(142356,50);
        WriteXiaomi1.put(4328,70);
        Map<Integer,Double> WriteXiaomi2=new ConcurrentHashMap<Integer, Double>();
        WriteXiaomi2.put(142356,10.0);
        WriteXiaomi2.put(4328,10.0);
        Sys.AddWrite(51328,WriteAli1,WriteAli2);

        Sys.Logout();
   /*     Sys.Login("Store2@gmail.com","S2_superLi");
        Map<Integer,String> contactAli22=new ConcurrentHashMap<Integer, String>();
        contactAli22.put(2087564,"yoni");
        contactAli22.put(2453214,"roi");
        Map<Integer,Integer> contactAli11=new ConcurrentHashMap<Integer, Integer>();
        contactAli11.put(2087564,0524536272);
        contactAli11.put(2453214,0523756223);
        Sys.AddSupplier("Ali",51345,"hprahim, 5, Tel Aviv","Mizrahi","007",873645,"EFT",contactAli22,contactAli11);
        Map<Integer,String> contactIKEA22=new ConcurrentHashMap<Integer, String>();
        contactIKEA22.put(208231,"Dov");
        contactIKEA22.put(4283214,"Leni");
        Map<Integer,Integer> contactIKEA11=new ConcurrentHashMap<Integer, Integer>();
        contactIKEA11.put(208231,05221336272);
        contactIKEA11.put(4283214,0523546253);
        Sys.AddSupplier("IKEA",51321,"shalom, 17, Hulon","Ben-Leumi","027",432679,"EFT",contactIKEA22,contactIKEA11);

        Map<Integer,String> ProductAli11 =new ConcurrentHashMap<Integer, String>();
        ProductAli11.put(1213,"blanket");
        ProductAli11.put(43567,"Pillow");
        Map<Integer,Double> ProductAli22 =new ConcurrentHashMap<Integer,Double>();
        ProductAli22.put(1213,89.9);
        ProductAli22.put(43567,1399.9);
        Map<Integer,Integer>  ProductAli33=new ConcurrentHashMap<Integer, Integer>();
        Sys.AddContract(51345,false,Days,true,ProductAli33,ProductAli11,ProductAli22);
        Map<Integer,String> ProductIKEA11 =new ConcurrentHashMap<Integer, String>();
        ProductIKEA11.put(223,"Armchair");
        ProductIKEA11.put(345,"Desk");
        ProductIKEA11.put(1687,"Chair");
        Map<Integer,Double> ProductIKEA22 =new ConcurrentHashMap<Integer,Double>();
        ProductIKEA22.put(223,499.9);
        ProductIKEA22.put(345,1399.9);
        ProductIKEA22.put(1687,139.9);
        Map<Integer,Integer>  ProductIKEA33=new ConcurrentHashMap<Integer, Integer>();
        Sys.AddContract(51321,false,Days,true,ProductIKEA33,ProductIKEA11,ProductIKEA22);

        Map<Integer,Double> WriteAli22=new ConcurrentHashMap<Integer, Double>();
        WriteAli22.put(1213,13.0);
        Map<Integer,Integer> WriteAli11=new ConcurrentHashMap<Integer, Integer>();
        WriteAli11.put(1213,200);
        Sys.AddWrite(51328,WriteAli11,WriteAli22);
        Map<Integer,Integer> WriteIKEA11=new ConcurrentHashMap<Integer, Integer>();
        WriteIKEA11.put(223,60);
        WriteIKEA11.put(345,90);
        Map<Integer,Double> WriteIKEA22=new ConcurrentHashMap<Integer, Double>();
        WriteIKEA22.put(223,12.0);
        WriteIKEA22.put(345,12.0);
        Sys.AddWrite(51321, WriteIKEA11,WriteIKEA22);


        //todo add details!
        Sys.Logout();*/
    }

    private static void Register() {
        Scanner myScanner = new Scanner(System.in);
        String email;
        String password;
        System.out.println("Please enter your email");
        email = myScanner.next();
        String Ex=Sys.CheckEmailExist(email);
        if(!Ex.equals("Not Exist")){
            System.out.println(Ex);
        }
        else {
            System.out.println("Please enter password");
            password = myScanner.next();
            String Done = Sys.Register(email, password);
            if (Done.equals("Done")) {
                System.out.println("The registration was successful");
            } else
                System.out.println(Done);
        }
    }

    private static void Login() {
        Scanner myScanner = new Scanner(System.in);
        String email;
        String password;
        System.out.println("Please enter your email");
        email = myScanner.next();
        String Ex = Sys.CheckEmailExist(email);
        boolean done = true;
        if(!Ex.equals("Exist")) {
            System.out.println(Ex);
            done=false;
           /* System.out.println("Do you want to return to the menu? y/n");
            String ans=myScanner.next();
            if(ans.equals("y")){
                continu=false;
                break;
            }*/
           /* System.out.println("enter your email:");
            email = myScanner.next();
            Ex = Sys.CheckEmailExist(email);*/
        }
        if (done) {
            System.out.println("Please enter password");
            password = myScanner.next();
            String Done = Sys.Login(email, password);
            if (!Done.equals("Done")) {
                System.out.println(Ex);
                done=false;
                //System.out.println("enter the password again");
                //password = myScanner.next();
                //Done = Sys.Login(email, password);
            }
        }
        if(done) {
            System.out.println(email + " welcome to your super!");
            System.out.println("");
            GetOrderDetails();
            System.out.println("");
            Action();
        }
    }

    private static void Add_Edit_Supplier(int status) {
        boolean conect = Sys.CheckConected();
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
            ID = myScanner.nextInt();

            boolean contiue = true;
            boolean MoreContact = true;

            if (status == 2) {
                String exist = Sys.CheckSuplierExist(ID);
                while (!exist.equals("Done")) {
                    System.out.println(exist);
                    System.out.println("Do you want to continue and enter the supplier Id again? y/n ");
                    String ans = myScanner.next();
                    if (ans.equals("y")) {
                        System.out.println("Please enter the Supplier's ID");
                        ID = myScanner.nextInt();
                        exist = Sys.CheckSuplierExist(ID);
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
                name = myScanner.next();
                System.out.println("Pleas enter the Supplier's address");
                Address = myScanner.next();
                System.out.println("Please enter the Supplier's Bank");
                Bank = myScanner.next();
                System.out.println("Please enter the Supplier's Branch's Bank");
                Branch = myScanner.next();
                System.out.println("Please enter the Supplier's bankNumber");
                bankNumber = myScanner.nextInt();
                System.out.println("Please enter the Supplier's payments");
                payments = myScanner.next();

                while (MoreContact) {
                    String ContactName;
                    int ContactId;
                    System.out.println("Please enter the Contact's name");
                    ContactName = myScanner.next();
                    System.out.println("Please enter the Contact's Id");
                    ContactId = myScanner.nextInt();
                    Contacts_ID.put(ContactId, ContactName);
                    int PhoneNumber;
                    System.out.println("Please enter the Contact's Phone number");
                    PhoneNumber = myScanner.nextInt();
                    Contacts_number.put(ContactId, PhoneNumber);
                    String ans;
                    System.out.println("Do you have more contact? y/n");
                    ans = myScanner.next();
                    if (ans.equals("n")) {
                        MoreContact = false;
                    }
                }
                String Done;
                if (status == 1) {
                    Done = Sys.AddSupplier(name, ID,Address, Bank, Branch, bankNumber, payments, Contacts_ID, Contacts_number);
                } else {
                    Done = Sys.EditSupplier(name, ID,Address, Bank, Branch, bankNumber, payments, Contacts_ID, Contacts_number);
                }
            }
        }
    }

    private static void Add_Edit_Agreement(int status) {
        boolean conect = Sys.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            Scanner myScanner = new Scanner(System.in);
            int suplaier_ID;
            boolean fixeDays = false;
            LinkedList<Integer> Days = new LinkedList<Integer>();
            boolean leading = true;
            Map<Integer,Integer>  ItemsID_ItemsIDSupplier=new ConcurrentHashMap<Integer, Integer>();
            Map<Integer, String> ProductIDVendor_Name = new ConcurrentHashMap<Integer, String>();
            Map<Integer, Double> ProducttemsIDVendor_Price = new ConcurrentHashMap<Integer, Double>();
            boolean contiue=true;

            System.out.println("Please enter the Supplier's ID");
            suplaier_ID = myScanner.nextInt();
            if(status==2){
                String exist=Sys.CheckSAgreementExist(suplaier_ID);
                while (!exist.equals("Done")){
                    System.out.println(exist);
                    System.out.println("Do you want to continue and enter the supplier Id again? y/n ");
                    String ans=myScanner.next();
                    if(ans.equals("y")){
                        System.out.println("Please enter the Supplier's ID");
                        suplaier_ID =myScanner.nextInt();
                        exist=Sys.CheckSAgreementExist(suplaier_ID);
                    }
                    else{
                        {
                            contiue=false;
                            exist="Done";
                        }
                    }
                }
            }
            if (contiue) {

                System.out.println("Does the supplier bring the supply on regular days? y/n");
                String ans = myScanner.next();
                if (ans.equals("y")) {
                    fixeDays = true;
                    System.out.println("Please enter one day that the supply are expected to arrive. in number");
                    int day = myScanner.nextInt();
                    Days.add(day);
                    boolean MoreDay = true;
                    while (MoreDay) {
                        System.out.println("Is the supply expected to arrive in more days? y/n");
                        ans = myScanner.next();
                        if (ans.equals("n"))
                            MoreDay = false;
                        else {
                            System.out.println("Please enter the extra day. in number");
                            day = myScanner.nextInt();
                            if(day<8&&day>0) {
                                Days.add(day);
                            }
                            else{
                                System.out.println("the day need to be between 0-7");
                            }
                        }
                    }
                }
                System.out.println("Does the supplier bring the supplier by himself (or it required for transport Syss)? y/n");
                ans = myScanner.next();
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
                    System.out.println("Which product the supplier will supply to the store?\n" + "Please enter its name");
                    Product_Name = myScanner.next();
                    System.out.println("Please enter its category");
                    category = myScanner.next();
                    System.out.println("Please enter its subcategory");
                    subcategory = myScanner.next();
                    System.out.println("Please enter its sub_subcategory");
                    sub_subcategory = myScanner.next();
                    System.out.println("Please enter its manufacturer");
                    manufacturer = myScanner.next();
                    int Id_Store=Sys.FindId_P_Store(Product_Name,category,subcategory,sub_subcategory,manufacturer);
                    System.out.println("Please enter his Catalog Number");
                    int product_Id = myScanner.nextInt();
                    System.out.println("Please enter the price");
                    double Product_Price = myScanner.nextInt();
                    ItemsID_ItemsIDSupplier.put(Id_Store,product_Id);
                    ProductIDVendor_Name.put(product_Id, Product_Name);
                    ProducttemsIDVendor_Price.put(product_Id, Product_Price);
                    System.out.println("Does the supplier provide another product? y/n");
                    ans = myScanner.next();
                    if (ans.equals("n")) {
                        MoreProduct = false;
                    }
                }
                String Done;
                switch (status) {
                    case 1:
                        Done = Sys.AddContract(suplaier_ID, fixeDays, Days, leading,ItemsID_ItemsIDSupplier, ProductIDVendor_Name, ProducttemsIDVendor_Price);
                        break;
                    case 2:
                        Done = Sys.EditContract(suplaier_ID, fixeDays, Days, leading,ItemsID_ItemsIDSupplier, ProductIDVendor_Name, ProducttemsIDVendor_Price);
                        break;
                }
            }
        }
    }

    private static void Add_Edit_Write(int status) {
        boolean conect = Sys.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            int Suplaier_ID;
            Map<Integer, Integer> ItemsID_Amount=new ConcurrentHashMap<Integer, Integer>();
            Map<Integer, Double> ItemsID_Assumption =new ConcurrentHashMap<Integer, Double>();
            Scanner myScanner = new Scanner(System.in);
            System.out.println("Please enter the Supplier's ID");
            Suplaier_ID = myScanner.nextInt();

            boolean contiue=true;
            if (status == 2) {
                String exist = Sys.CheckSWortExist(Suplaier_ID);
                while (!exist.equals("Done")) {
                    System.out.println(exist);
                    System.out.println("Do you want to continue and enter the supplier Id again? y/n ");
                    String ans = myScanner.next();
                    if (ans.equals("y")) {
                        System.out.println("Please enter the Supplier's ID");
                        Suplaier_ID = myScanner.nextInt();
                        exist = Sys.CheckSWortExist(Suplaier_ID);
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
                    Product_ID = myScanner.nextInt();
                    System.out.println("How many units of this product should be purchased to get the discount?");
                    Product_Amount = myScanner.nextInt();
                    System.out.println("What percentage of discount will the product receive?");
                    Product_Assumption = myScanner.nextInt();
                    ItemsID_Amount.put(Product_ID, Product_Amount);
                    ItemsID_Assumption.put(Product_ID, Product_Assumption);
                    System.out.println("Would you like to add another product? y/n");
                    String ans = myScanner.next();
                    if (ans.equals("n")) {
                        MoreProduct = false;
                    }
                }
                String Done;
                switch (status) {
                    case 1:
                        Done = Sys.AddWrite(Suplaier_ID, ItemsID_Amount, ItemsID_Assumption);
                        break;
                    case 2:
                        Done = Sys.EditWrite(Suplaier_ID, ItemsID_Amount, ItemsID_Assumption);
                        break;
                }
            }
        }
    }

    private static void MakeOrder() {
        boolean conect = Sys.CheckConected();
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
            ID_Suplaier = myScanner.nextInt();
            String exist = Sys.CheckSuplierExist(ID_Suplaier);
            if (exist.equals("Exit")) {
                conect = false;
                System.out.println("the supplier is not exist in the system.");
            }
            if (conect) {
                boolean moreDay=true;
                while(moreDay) {
                    System.out.println("Please enter the day that the order is expected to arrive the store. in number!");
                    day = myScanner.nextInt();
                    conect = Sys.CheckTheDay(ID_Suplaier, day);
                    if(conect) {
                        Days.add(day);
                        System.out.println("Would you like to add another day? y/n");
                        String ans = myScanner.next();
                        if (ans.equals("n")) {
                            moreDay = false;
                        }
                    }
                }
                if (conect) {
                    boolean MoreProduct = true;
                    System.out.println("Which product would you like to add to the Order?");
                    while ((MoreProduct)) {
                        int Product_ID;
                        int Product_Amount;
                        System.out.println("Please enter the Product's ID (According to the supplier)");
                        Product_ID = myScanner.nextInt();
                        conect=Sys.CheckProductexist(ID_Suplaier,Product_ID);
                        if(conect) {
                            System.out.println("How many units of the product would you like to order??");
                            Product_Amount = myScanner.nextInt();
                            ItemsIDVendor_NumberOfItems.put(Product_ID, Product_Amount);
                        }
                        else{
                            System.out.println("This product is not included in the agreement with the supplier.");
                        }
                        System.out.println("Would you like to add another product? y/n");
                        String ans = myScanner.next();
                        if (ans.equals("n")) {
                            MoreProduct = false;
                        }
                    }
                    int Done = Sys.MakeOrder(ID_Suplaier, Days,ItemsIDVendor_NumberOfItems);
                    System.out.println(Done);
                    if (Done>-1){
                        InterfaceOrder o = Sys.getOrderDetails(Done);
                        PrintOrder(o);
                    }
                }
                else
                    System.out.println("the supplier does not supply in that days.");
            }
        }

    }

    private static void UpdateDetailsOrder() {
        boolean conect = Sys.CheckConected();
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
            ID_Order = myScanner.nextInt();
            String Able = Sys.CheckAbleToChangeOrder(ID_Order);
            if (!Able.equals("Able")) {
                conect = false;
                System.out.println(Able);
            }
            if (conect) {
                ID_Suplaier = Sys.GetSupplierID_PerOrder(ID_Order);
                boolean moreDay = true;
                while (moreDay) {
                    int day;
                    System.out.println("Please enter the day that the order is expected to arrive the store. in number!");
                    day = myScanner.nextInt();
                    conect = Sys.CheckTheDay(ID_Suplaier, day);
                    if (conect) {
                        Days.add(day);
                        System.out.println("Would you like to add another day? y/n");
                        String ans = myScanner.next();
                        if (ans.equals("n")) {
                            moreDay = false;
                        }
                    }
                }
                if (conect) {
                    boolean LessProduct = false;
                    System.out.println("Would you like to remove product from the Order?  y/n");
                    String ans = myScanner.next();
                    if (ans.equals("y")) {
                        LessProduct = true;
                    }
                    while (LessProduct) {
                        System.out.println("Please enter the Product's ID (According to the supplier)");
                        Product_ID = myScanner.nextInt();
                        Sys.RemoveProduct(ID_Order, Product_ID);
                        System.out.println("Would you like to remove another product? y/n");
                        ans = myScanner.next();
                        if (ans.equals("n")) {
                            LessProduct = false;
                        }
                    }

                    boolean MoreProduct = false;
                    System.out.println("Would you like to add Product to the Order?");
                    ans = myScanner.next();
                    if (ans.equals("y")) {
                        MoreProduct = true;
                    }
                    while ((MoreProduct)) {
                        System.out.println("Please enter the Product's ID (According to the supplier)");
                        Product_ID = myScanner.nextInt();
                        conect = Sys.CheckProductexist(ID_Suplaier, Product_ID);
                        if (conect) {
                            System.out.println("How many units of the product would you like to order?");
                            Product_Amount = myScanner.nextInt();
                            ItemsIDVendor_NumberOfItems.put(Product_ID, Product_Amount);
                        } else {
                            System.out.println("This product is not included in the agreement with the supplier.");
                        }
                        System.out.println("Would you like to add another product? y/n");
                        ans = myScanner.next();
                        if (ans.equals("n")) {
                            MoreProduct = false;
                        }
                    }
                    String Done = Sys.ChangeOrder(ID_Order, ID_Suplaier, Days, ItemsIDVendor_NumberOfItems);
                    System.out.println(Done);
                }
                else
                    System.out.println("the supplier arrived to the store in another days.");
            }
        }

    }

    private static void DisplayItems() {
        boolean conect = Sys.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            LinkedList<InterfaceContract> Contract = Sys.GetContract();
            for (InterfaceContract Con : Contract
            ) {
                for (Map.Entry<Integer, String> e : Con.ProductIDVendor_Name.entrySet()) {
                    int P = e.getKey();
                    String N = e.getValue();
                    for (Map.Entry<Integer, Double> entry : Con.productIDVendor_Price.entrySet()) {
                        int p = entry.getKey();
                        Double price = entry.getValue();
                        if (P == p) {
                            System.out.println("supplier: " + Con.Suplaier_ID);
                            System.out.println("product name: " + N);
                            System.out.println("Price: " + price + "\n");
                        }
                    }
                }
            }
        }
    }

    private static void DisplaySupplierDetails() {
        boolean conect=Sys.CheckConected();
        if(!conect){
            System.out.println("You should login");
        }
        if(conect) {
            LinkedList<InterfaceSupplier> suppliers =Sys.GetSupliers();
            for (InterfaceSupplier Sup : suppliers
            ) {
                System.out.print("name: " + Sup.Name + "\n" +
                        "Id: " + Sup.ID + "\n" +
                        "Payment with: " + Sup.Payments + "\n" +
                        "Bank: " + Sup.Bank + "\n" +
                        "Branch: " + Sup.Branch + "\n" +
                        "Bank number: " + Sup.BankNumber + "\n");
                System.out.println("Contacts:");
                Sup.ContactsID_Name.forEach((ID, name) -> {
                    System.out.println("name: " + name);
                    System.out.println("ID: " + ID);
                    Sup.ContactsID_number.forEach((Id, number) -> {
                        if (ID == Id) {
                            System.out.println("number: " + number + "\n");
                        }
                    });
                });
            }
        }

    }

    private static void UpdateOrderStatus() {

        boolean conect = Sys.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            int ID_Order;
            Scanner myScanner = new Scanner(System.in);
            System.out.println("Pleas enter the Order ID that arrived to the store");
            ID_Order = myScanner.nextInt();
            InterfaceOrder order = Sys.getOrderDetails(ID_Order);
            if(order!=null) {
                Map ProductID_Amount=new HashMap<Integer, Integer>();
                Map ProductID_Date=new HashMap<Integer, Integer>();
                for (Map.Entry p:order.ItemsID_ItemsIDVendor.entrySet()
                ) {
                    System.out.println("Did the item with ID "+ p.getKey() +"arrive at the store? y/n");
                    String ans=myScanner.next();
                    if(ans.equals("y")|ans.equals("N")){
                        System.out.println("Please enter the number of units that came from this product");
                        int amount=myScanner.nextInt();
                        ProductID_Amount.put(p.getKey(),amount);
                        System.out.println("Please enter the expiry date for the product");
                        int date=myScanner.nextInt();
                        ProductID_Date.put(p.getKey(),date);
                    }
                }
                Sys.AddToStore(ProductID_Amount,ProductID_Date);
            }
            if (order==null)
                System.out.println("the Order is mot exist in the system");
        }
    }

    private static void DeleteSupplier() {
        boolean conect = Sys.CheckConected();
        if (!conect) {
            System.out.println("You need to connect before you take any action");
        }
        if (conect) {
            Scanner myScanner = new Scanner(System.in);
            int ID;
            System.out.println("Pleas enter the Supplier's ID");
            ID = myScanner.nextInt();
            String Done = Sys.DeleteSupplier(ID);

        }
    }

    private static void Logout() {
        String ans=Sys.Logout();
        System.out.println(ans);
    }

    public static void printWarning(String warning) {
        System.out.println(warning);
    }

    private static void PrintOrder(InterfaceOrder o) {
        System.out.println("The Order ID is: "+o.ID_Inventation);
        System.out.println("The Supplier ID is: "+o.ID_Vendor);
        System.out.println("The Days that the order is coming is:"  );
        for (int d: o.Days
        ) {
            System.out.println(d);
        }
        System.out.println("The product that include in the Order is: ");
        System.out.println("Product  | Amount");
        for (Map.Entry<Integer,Integer> I:o.ItemsID_ItemsIDVendor.entrySet()
        ) {
            System.out.print(I.getValue());
            System.out.println(o.ItemsID_NumberOfItems.get(I.getKey()));  //todo check if work
        }
        System.out.println("The Total Price of the order is: "+o.TotalPrice);
    }

    private static void GetOrderDetails() {
        System.out.println("Orders that are scheduled to arrive today:");
        LinkedList<InterfaceOrder> Orders=Sys.GetOrderDetails();
        for (InterfaceOrder o:Orders
        ) {
            System.out.println("Order ID is: "+o.ID_Inventation);
            System.out.println("Supplier ID is: "+o.ID_Vendor);
            System.out.println("The product that include in the Order is: ");
            for (Map.Entry<Integer,Integer> I:o.ItemsID_ItemsIDVendor.entrySet()
            ) {
                System.out.print(I.getValue());
                System.out.println(o.ItemsID_NumberOfItems.get(I.getKey()));  //todo check if work
            }
            System.out.println("If an order arrives at the store,\n" +
                    " select the \"Update Order Status\" in the menu and fill in the order details");
        }
    }

}
