package InterfaceLayer;

import BusinessLayer.Contract;
import BusinessLayer.Wrotequantities;
import java.util.Map;

public class InterfaceSupplier {
    public String Name;
    public int ID;
    public String Bank;
    public String Branch;
    public int BankNumber;
    public String Payments;
    public Map<Integer, String> ContactsID_Name;
    public Map<Integer, Integer> ContactsID_number;
    public Contract Contract;
    public Wrotequantities Worte;

    public InterfaceSupplier(String name, int ID, String bank,String branch, int bankNumber,
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
}

