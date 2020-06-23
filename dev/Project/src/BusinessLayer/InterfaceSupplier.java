package BusinessLayer;

import java.util.Map;

public class InterfaceSupplier {
    public String Name;
    public int ID;
    public String Bank;
    public String Branch;
    public int BankNumber;
    public String Payments;
    public String Address;
    public Map<Integer, String> ContactsID_Name;
    public Map<Integer, Integer> ContactsID_number;
    public InterfaceContract Contract;
    public InterfaceWrotequantities Worte;

    public InterfaceSupplier(String name, int ID, String Address,String bank,String branch, int bankNumber,
                             String payments, Map<Integer, String> Contacts_ID,
                             Map<Integer, Integer> Contacts_number,InterfaceContract contract,
                                     InterfaceWrotequantities worte) {
        Name = name;
        this.ID = ID;
        Bank=bank;
        Branch=branch;
        BankNumber = bankNumber;
        Payments = payments;
        Address = Address;
        Contract = null;
        Worte = null;
        ContactsID_Name=Contacts_ID;
        ContactsID_number=Contacts_number;
        Contract=contract;
        Worte=worte;
    }
}

