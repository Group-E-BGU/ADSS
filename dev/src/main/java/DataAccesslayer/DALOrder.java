package DataAccesslayer;

import java.awt.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class DALOrder {

    public int ID_Inventation;
    public int ID_Vendor;
    public boolean Auto;
    public LinkedList<Integer> Day;
    public LocalDate OrderDate;
    public LocalDate ArrivalTime;
    public Map<Integer, Integer> ItemsID_ItemsIDVendor;
    public Map<Integer, Integer> ItemsID_NumberOfItems;
    public double TotalPrice;
    public String Status;

    public DALOrder(int ID_Vendor, int Id, boolean auto, LinkedList<Integer> day, LocalDate orderDate, LocalDate arrivalTime, Map<Integer, Integer> itemsID_ItemsIDVendor, Map<Integer, Integer> itemsID_NumberOfItems, double totalPrice, String status){//List<DALContact> vendorContacts, List<DALContact> leadersContacts) {
        this.ID_Vendor = ID_Vendor;
        this.ID_Inventation=Id;
        Auto=auto;
        Day=day;
        OrderDate = orderDate;
        ArrivalTime = arrivalTime;
        ItemsID_ItemsIDVendor = itemsID_ItemsIDVendor;
        ItemsID_NumberOfItems = itemsID_NumberOfItems;
        TotalPrice = totalPrice;
        Status = status;
        //VendorContacts = vendorContacts;
       // LeadersContacts = leadersContacts;
    }
}
