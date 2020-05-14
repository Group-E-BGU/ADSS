package DataAccesslayer;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Mapper {

    private Repo Repositpry;
    int ID_Invetation;

    public Mapper(){
        Repositpry=Repo.getInstance();
        ID_Invetation=0;
        }

    public void WriteSupplier(String Name, int ID, String address, String bank, String branch, int BankNumber,
                              String Payments, Map<Integer, String> Contacts_ID,
                              Map<Integer, Integer> Contacts_number) {

        DALSupplier Dalush = new DALSupplier(Name, ID, address, bank,branch,BankNumber, Payments, Contacts_ID,
                Contacts_number);
        Repositpry.Suppliers.add(Dalush);
    }

    public String EditSupplier(String Name, int ID, String address, String bank, String branch, int BankNumber,
                               String Payments, Map<Integer, String> Contacts_ID,
                               Map<Integer, Integer> Contacts_number) {

        DALSupplier Dalush = new DALSupplier(Name, ID,address, bank,branch,BankNumber, Payments, Contacts_ID,
                Contacts_number);

        for (DALSupplier Ven: Repositpry.Suppliers
             ) {
        if(Ven.ID==Dalush.ID)
            Repositpry.Suppliers.remove(Ven);
    }
        Repositpry.Suppliers.add(Dalush);
        return "Done";
}

    public String DeleteSupplier(int ID){
        for (DALSupplier Ven: Repositpry.Suppliers
        ) {
            if(Ven.ID==ID)
                Repositpry.Suppliers.remove(Ven);
                return "Done";
        }
        return "the supplier is not exist in the system";
    }

    public String WriteContract(int suplaier_ID, boolean fixeDays, List<Integer> dayes, boolean leading,
                              Map<Integer, String> productIDSupplier_name, Map<Integer, Integer> ItemsID_ItemsIDsupplier,
                              Map<Integer, Double> producttemsIDSupplier_price){

        DALContract Dalush=new DALContract(suplaier_ID, fixeDays,dayes, leading, productIDSupplier_name,
                ItemsID_ItemsIDsupplier, producttemsIDSupplier_price);
        for (DALSupplier Sup:Repositpry.Suppliers
             ) {
            if(Sup.ID==suplaier_ID){
                Sup.Contract=Dalush;
                return "Done";
            }
        }
        return "the supplier is not exist in the system";
    }

    public void DeleteContract(int suplaier_ID){
        for (DALSupplier Sup:Repositpry.Suppliers
        ) {
            if(Sup.ID==suplaier_ID){
                Sup.Contract=null;
            }
        }
    }

    public void WriteWrote(int suplaier_ID, java.util.Map<Integer, Integer> itemsID_amount, Map<Integer,Double> itemsID_assumption){
        DALWrotequantities Dalush=new DALWrotequantities(suplaier_ID,  itemsID_amount, itemsID_assumption);
        for (DALSupplier Sup:Repositpry.Suppliers
        ) {
            if(Sup.ID==suplaier_ID){
                Sup.Worte=Dalush;
            }
        }
    }

    public void DeleteWrote(int suplaier_ID){
        for (DALSupplier Sup:Repositpry.Suppliers
        ) {
            if(Sup.ID==suplaier_ID){
             Sup.Worte=null;
            }
        }
    }

    public LinkedList<DALOrder> ReadAllInvetation() {
        LinkedList<DALOrder> Invetation =Repositpry.Invetations;
        return Invetation;
    }

    public LinkedList<DALSupplier> ReadAllSupplier() {
        LinkedList<DALSupplier> Vendors =Repositpry.Suppliers;
        return Vendors;
    }


    public void WriteOrder(int id_suplaier, int numOfOrder,boolean auto,LinkedList<Integer> day, LocalDate d, LocalDate e , Map<Integer, Integer> itemsID_itemsIDSupplier, Map<Integer, Integer> productIDVendor_numberOfItems, Double TotalPrice, String status) {
        DALOrder Dalush=new DALOrder(id_suplaier,numOfOrder,auto,day,d,e,
                itemsID_itemsIDSupplier,productIDVendor_numberOfItems,TotalPrice,status);
        Repositpry.Invetations.add(Dalush);
    }


    public DALContract ReadContract(int id) {
        for (DALSupplier sup:Repositpry.Suppliers
             ) {
            if(sup.ID==id){
               return sup.Contract;
            }
        }
        return null;
    }

    public DALWrotequantities ReadWorte(int id) {
        for (DALSupplier sup:Repositpry.Suppliers
        ) {
            if(sup.ID==id){
                return sup.Worte;
            }
        }
        return null;
    }

    public String CheckEmailExist(String email) {
        for (DALUser du:Repositpry.Users){
            if (du.email.equals(email)){
                return "Exist";
            }
        }
        return "Not Exist";
    }

    public String CheckCorrectPasword(String email, String password) {
        for (DALUser du:Repositpry.Users
             ) {
            if(du.email.equals(email)){
                if (du.password.equals(password)){
                    return "correct";
                }
                return "un correct password";
            }
        }
        return "Not Exist";
    }

    public String Register(String email, String password) {
        DALUser du=new DALUser(email,password);
        Repositpry.Users.add(du);
        return "Done";
    }

    public int getProductId(String product_name, String category, String subcategory, String sub_subcategory, String manufacturer) {
        //todo
        return -1;
    }

    public void AddProdudt(int id, String product_name, String category, String subcategory, String sub_subcategory, String manufacturer) {
    Repositpry.AddProdudt(id,product_name,category,subcategory,sub_subcategory,manufacturer);

    }
}
