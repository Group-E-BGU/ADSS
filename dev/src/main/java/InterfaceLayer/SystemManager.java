package InterfaceLayer;

import BusinessLayer.Store;
import BusinessLayer.system;
import BusinessLayer.User;
import PresentationLayer.Menu;

import java.util.LinkedList;
import java.util.Map;

public class SystemManager {
    private system systemcontroler;
    private User logged_user;
    public Store current_Store; //this field is public only for the unitests

    public SystemManager() {
        logged_user=null;
        current_Store=null;
        systemcontroler=new system();
    }

    public String AddSupplier(String name, int ID,String Address, String bank, String branch, int bankNumber,
                              String payments, Map<Integer, String> Contacts_ID,
                              Map<Integer, Integer> Contacts_number) {//  List<DALItem> Items)
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
         Done = current_Store.AddSuplier(name, ID,Address,bank, branch,bankNumber, payments, Contacts_ID,Contacts_number);

        return Done;
    }

    public String addItemRecord(String name, String minAmount, String shelfNumber, String manufacture) {
        try {
            int minA = Integer.parseInt(minAmount);
            if (minA < 0)
                return "minimum amount cannot be below 0";
            int shelfNum = Integer.parseInt(shelfNumber);
            return current_Store.addItemRecord(name, minA, shelfNum, manufacture);
        } catch (Exception e) {
            return "please enter valid numbers";
        }
    }

    public String AddContract(int suplaier_id, boolean fixeDays, LinkedList<Integer> days,
                              boolean leading,  Map<Integer,Integer>  ItemsID_ItemsIDSupplier, Map<Integer, String> productIDVendor_name,
                              Map<Integer, Double> producttemsIDVendor_price) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.AddContract(suplaier_id,fixeDays, days,leading,ItemsID_ItemsIDSupplier, productIDVendor_name,producttemsIDVendor_price);
        return Done;
    }

    public String AddWrite(int suplaier_id, Map<Integer, Integer> itemsID_amount, Map<Integer, Double> itemsID_assumption) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.AddWrite(suplaier_id, itemsID_amount,itemsID_assumption);
        return Done;
    }

    public int MakeOrder(int id_suplaier, LinkedList<Integer> day, Map<Integer, Integer> itemsIDVendor_numberOfItems) {
        int Done=-1;
        if(current_Store!=null)
            Done= current_Store.MakeOrder(id_suplaier,day,itemsIDVendor_numberOfItems);
        return Done;
    }

    public String EditSupplier(String name, int id, String address, String bank, String branch, int bankNumber, String payments, Map<Integer, String> contacts_id, Map<Integer, Integer> contacts_number) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
             Done = current_Store.EditSuplier(name, id,address ,bank, branch,bankNumber, payments,contacts_id,contacts_number);
        return Done;
    }

    public String DeleteSupplier(int id) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null) {
            Done = current_Store.Delete(id);
       //     Done = logged_user.DeleteSupplier(id);
        }
        return Done;
    }

    public String EditContract(int suplaier_id, boolean fixeDays, LinkedList<Integer> days, boolean leading,Map<Integer,Integer>  ItemsID_ItemsIDSupplier,
                               Map<Integer, String> productIDVendor_name, Map<Integer, Double> producttemsIDVendor_price) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null) {
            Done = current_Store.EditContract(suplaier_id, fixeDays, days, leading,ItemsID_ItemsIDSupplier, productIDVendor_name, producttemsIDVendor_price);
        }
        return Done;
    }

    public String EditWrite(int suplaier_id, Map<Integer, Integer> itemsID_amount, Map<Integer, Double> itemsID_assumption) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
           Done=current_Store.EditWrite(suplaier_id, itemsID_amount,itemsID_assumption);
        return Done;
    }

    public String CheckEmailExist(String email) {
        String Done=systemcontroler.CheckEmailExist(email);
        return Done;
    }

    public String Register(String email, String password) {
        String Done=systemcontroler.Register(email,password);
        return Done;
    }

    public String Login(String email, String password) {
        String Done=systemcontroler.Login(email,password);
        if(Done.equals("Done")){
            logged_user=new User(email,password);
            current_Store=Store.createInstance(email);
        }
        return Done;
    }

    public LinkedList<InterfaceContract> GetContract() {
        LinkedList<InterfaceContract> list=new LinkedList<InterfaceContract>();
        if(current_Store!=null)
            list=current_Store.GetContract();
        return list;
    }

    public LinkedList<InterfaceSupplier> GetSupliers() {
        LinkedList<InterfaceSupplier> list=new LinkedList<InterfaceSupplier>();
        if(current_Store!=null)
           list=current_Store.GetSupliers();
        return list;
    }

    public String ChangOrderStatus (int id_order) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
              Done=current_Store.ChangOrderStatus(id_order);
        return Done;
    }

    public String CheckSuplierExist(int id) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.CheckSuplierExit(id);
        return Done;
    }

    public String CheckSAgreementExist(int suplaier_id) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store.CheckSAgreementExit(suplaier_id);
        return Done;
    }

    public String CheckSWortExist(int suplaier_id) {
        String Done="you must be logged in before doing any actions";
        if(current_Store!=null)
            Done=current_Store. CheckSWortExit(suplaier_id);
        return Done;
    }

    public String Logout() {
        if(logged_user==null| current_Store==null){
            return "you need to Login before you logout";
        }
        current_Store=null;
        logged_user=null;
        return "login successfully";
    }

    public boolean CheckConected() {
        return current_Store!=null;
    }

    public static void sendWarning(String warning) {
        Menu.printWarning(warning);
        /*
    public static void sendWarning(String name, int totalAmount, int minAmount) {
        Menu.printWarning(name,totalAmount,minAmount);
*/
    }


    public void initialize() {
        current_Store.initializeItems();
        current_Store.initializeCategories();
        current_Store.initializeDiscounts();
    }

    public String getItemAmountsByName(String name) {
        return current_Store.getItemAmountsByName(name);
    }

    public String setNewAmounts(String name, String amounts) {
        return current_Store.setNewAmounts(name,amounts);
    }

    public String moveToShelf(String name, String amount) {
        return current_Store.moveToShelf(name,amount);
    }

    public String subtract(String name, String amount) {
        return current_Store.subtract(name,amount);
    }

    public String setDefectedItem(String  name, String id){
        try {
            int ID = Integer.parseInt(id);
            return current_Store.setDefectedItem(name, ID);
        }
        catch (Exception e){
            return "Item's ID must be a number";
        }
    }

    public String addNewItemDiscount(String itemName, String percentage, String begDate, String enDate) {
        java.sql.Date beginDate, endDate;
        int perc;
        if(begDate.length()!=10 || enDate.length() != 10)
            return "Please enter valid arguments";
        try{
            beginDate = new java.sql.Date(Integer.parseInt(begDate.substring(6))-1900,Integer.parseInt(begDate.substring(3,5))-1,Integer.parseInt(begDate.substring(0,2)));
            endDate = new java.sql.Date(Integer.parseInt(enDate.substring(6))-1900,Integer.parseInt(enDate.substring(3,5))-1,Integer.parseInt(enDate.substring(0,2)));
            perc = Integer.parseInt(percentage);
            if(perc < 1 || perc > 100)
                return "Please enter valid arguments";
        }
        catch (Exception e){
            return "Please enter valid arguments";
        }
        return current_Store.addItemDiscount(itemName, perc, beginDate, endDate);
    }


    public String addNewCategoryDiscount(String categoryName, String percentage, String begDate, String enDate) {
        java.sql.Date beginDate, endDate;
        int perc;
        if(begDate.length()!=10 || enDate.length() != 10)
            return "Please enter valid arguments";
        try{
            beginDate = new java.sql.Date(Integer.parseInt(begDate.substring(6))-1900,Integer.parseInt(begDate.substring(3,5))-1,Integer.parseInt(begDate.substring(0,2)));
            endDate = new java.sql.Date(Integer.parseInt(enDate.substring(6))-1900,Integer.parseInt(enDate.substring(3,5))-1,Integer.parseInt(enDate.substring(0,2)));
            perc = Integer.parseInt(percentage);
            if(perc < 1 || perc > 100)
                return "Please enter valid arguments";
        }
        catch (Exception e){
            return "Please enter valid arguments";
        }
        return current_Store.addNewCategoryDiscount(categoryName, perc, beginDate, endDate);
    }

    public String setNewPrice(String name, String price,String rPrice){
        try {
            int newPrice = Integer.parseInt(price);
            if(newPrice<=0){
                return "Price must greater than 0";
            }
            int retailPrice = Integer.parseInt(rPrice);
            if(retailPrice<=0){
                return "Price must greater than 0";
            }
            return current_Store.setNewPrice(name, newPrice,retailPrice);
        }
        catch (Exception e){
            return "Item's price must be a number";
        }
    }

    public String printDefectedReport(String reportBegin, String reportEnd){
        java.sql.Date beginDate, endDate;
        if(reportBegin.length()!=10 || reportEnd.length() != 10)
            return "Please enter valid arguments";
        try{
            beginDate = new java.sql.Date(Integer.parseInt(reportBegin.substring(6))-1900,Integer.parseInt(reportBegin.substring(3,5))-1,Integer.parseInt(reportBegin.substring(0,2)));
            endDate = new java.sql.Date(Integer.parseInt(reportEnd.substring(6))-1900,Integer.parseInt(reportEnd.substring(3,5))-1,Integer.parseInt(reportEnd.substring(0,2)));
        }
        catch (Exception e){
            return "Please enter valid dates";
        }
        return current_Store.printDefectedReport(beginDate, endDate);
    }


    public String getInventoryReport(String names) {
        String[] categories = names.split("\\s+");
        String report = "";
        if (categories[0].equals("all")) {
            report = current_Store.getAllInventoryReport();
        }
        else {
            for (int i = 0; i < categories.length; i++) {
                report = report + current_Store.getInventoryReport(categories[i]) + "\n";
            }
        }
        return report;
    }

    public boolean CheckTheDay(int id_suplaier, int day) {
        return current_Store.CheckTheDay(id_suplaier,day);
    }

    public boolean CheckProductexist(int id_suplaier, int product_id) {
        return current_Store.CheckProductexist(id_suplaier,product_id);
    }

    public int FindId_P_Store(String product_name, String category, String subcategory, String sub_subcategory, String manufacturer) {
    return current_Store.FindId_P_Store(product_name,category,subcategory,sub_subcategory,manufacturer);
        }

    public String CheckAbleToChangeOrder(int id_order) {
        return current_Store.CheckAbleToChangeOrder(id_order);
    }

    public void RemoveProduct(int id_order, int product_id) {
        current_Store.RemoveProduct(id_order,product_id);
    }

    public String ChangeOrder(int Id_Order,int id_suplaier, int day, Map<Integer, Integer> itemsIDVendor_numberOfItems) {
   // current_Store.ChangeOrder(Id_Order,id_suplaier, day ,itemsIDVendor_numberOfItems);
   return "done";//todo check
    }

    public InterfaceOrder getOrderDetails(int done) {
        InterfaceOrder o = current_Store.getOrderDetails(done);
        return o;
    }
}