package InterfaceLayer;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

public class InterfaceOrder {
    public int ID_Inventation;
    public int ID_Vendor;
    public LocalDate OrderDate;
    public LocalDate ArrivalTime;
    public LinkedList<Integer> Days;
    public Map<Integer, Integer> ItemsID_ItemsIDVendor;
    public Map<Integer, Integer> ItemsID_NumberOfItems;
    public Double TotalPrice;
    public String Status;
   // public List<DALContact> VendorContacts;
  //  public List<DALContact> LeadersContacts;

    public InterfaceOrder(int ID_Vendor, int Id,LinkedList<Integer> days, LocalDate orderDate, LocalDate arrivalTime, Map<Integer, Integer> itemsID_ItemsIDVendor, Map<Integer, Integer> itemsID_NumberOfItems, double totalPrice, String status){//List<DALContact> vendorContacts, List<DALContact> leadersContacts) {
        this.ID_Vendor = ID_Vendor;
        this.ID_Inventation=Id;
        Days=days;
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
