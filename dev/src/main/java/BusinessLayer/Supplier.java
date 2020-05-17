package BusinessLayer;

import java.util.Map;

public class Supplier {

    private String Name;
    private int ID;
    private String Bank;
    private String Branch;
    private int BankNumber;
    private String Payments;
    private Map<Integer, String> ContactsID_Name;
    private Map<Integer, Integer> ContactsID_number;
    private Contract Contract;
    private Wrotequantities Worte;

    public Supplier(String name, int ID, String address, String bank, String branch, int bankNumber,
                    String payments, Map<Integer, String> Contacts_ID,
                    Map<Integer, Integer> Contacts_number) {
        Name = name;
        this.ID = ID;
        Bank=bank;
        Branch=branch;
        BankNumber = bankNumber;
        Payments = payments;
        Contract = null;
        Worte = null;
        ContactsID_Name=Contacts_ID;
        ContactsID_number=Contacts_number;
    }

    public String getName() {
        return Name;
    }

    public int getID() {
        return ID;
    }

    public BusinessLayer.Contract getContract() {
        return Contract;
    }

    public Wrotequantities getWorte() {
        return Worte;
    }

    public void setContract(BusinessLayer.Contract contract) {
        Contract = contract;
    }

    public void setWorte(Wrotequantities worte) {
        Worte = worte;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public void setBankNumber(int bankNumber) {
        BankNumber = bankNumber;
    }

    public void setPayments(String payments) {
        Payments = payments;
    }

    public void setContactsID_Name(Map<Integer, String> contactsID_Name) {
        ContactsID_Name = contactsID_Name;
    }

    public void setContactsID_number(Map<Integer, Integer> contactsID_number) {
        ContactsID_number = contactsID_number;
    }

    public String getBank() {
        return Bank;
    }

    public String getBranch() {
        return Branch;
    }

    public int getBankNumber() {
        return BankNumber;
    }

    public String getPayments() {
        return Payments;
    }

    public Map<Integer, String> getContactsID_Name() {
        return ContactsID_Name;
    }

    public Map<Integer, Integer> getContactsID_number() {
        return ContactsID_number;
    }

    public int GetIdProduct(Integer id) {
        int ID_product=-1;
        if(Contract!=null){
            ID_product=Contract.GetIdSup(id);
        }
        return ID_product;
    }

    public double getPric(int idProduct,int amount) {
        double price=1;
        if(Contract!=null){
            price=Contract.GetPrice(idProduct);
        }
        double Sale=GetSaleProduct(idProduct,amount);
        Sale = (100 - Sale) / 100;
        price= amount * price * Sale;
        return price;
    }

    public int GetIdProduct(int id) {
        int ID_product=-1;
        if(Contract!=null){
            ID_product=Contract.GetIdSup(id);
        }
        return ID_product;
    }

    public int GetSaleProduct(int id, int num) {
        int sale=0;
        if(this.Worte!=null){
            sale=Worte.GetSale(id,num);
        }
        return sale;
    }

    public boolean CheckTheDay(int day) {
        if(Contract!=null){
            return Contract.CheckTheDay(day);
        }
        return false;
    }

    public boolean CheckProductexist(int product_id) {
        if(Contract!=null){
            return Contract.CheckProductexist(product_id);
        }
        return false;
    }
}
