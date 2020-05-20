package BusinessLayer;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;

public class Order {

    private int ID_Invitation;
    private int ID_Vendor;
    private boolean auto;
    private LinkedList<Integer> Day;
    private LocalDate OrderDate;
    private LocalDate ArrivalTime;
    private Map<Integer, Integer> ItemsID_ItemsIDVendor;
    private Map<Integer, Integer> ItemsID_NumberOfItems;
    private double TotalPrice;
    private String Status;

    public Order(int ID_Vendor, int Id, boolean auto, LinkedList<Integer> day, Map<Integer, Integer> itemsID_ItemsIDVendor, Map<Integer, Integer> itemsID_NumberOfItems, Double totalPrice){//List<DALContact> vendorContacts, List<DALContact> leadersContacts) {

        this.ID_Vendor = ID_Vendor;
        this.ID_Invitation=Id;
        auto=auto;
        Day=day;
        OrderDate = LocalDate.now();
        ArrivalTime =null;
        ItemsID_ItemsIDVendor = itemsID_ItemsIDVendor;
        ItemsID_NumberOfItems = itemsID_NumberOfItems;
        TotalPrice = totalPrice;
        Status = "Waiting";
    }

    public boolean isAuto() {
        return auto;
    }

    public LinkedList<Integer> getDay() {
        return Day;
    }

    public int getID_Invitation() {
        return ID_Invitation;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getID_Vendor() {
        return ID_Vendor;
    }

    public LocalDate getOrderDate() {
        return OrderDate;
    }

    public LocalDate getArrivalTime() {
        return ArrivalTime;
    }

    public Map<Integer, Integer> getItemsID_ItemsIDVendor() {
        return ItemsID_ItemsIDVendor;
    }

    public Map<Integer, Integer> getItemsID_NumberOfItems() {
        return ItemsID_NumberOfItems;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public String getStatus() {
        return Status;
    }

    public void setArrivedatime(LocalDate now) {
        ArrivalTime=now;
    }

    public String CheckAbleToChangeOrder() {
         boolean able=true;
         int today=LocalDate.now().getDayOfWeek().getValue();
        for (int d:Day
             ) {
            if(d==today){
                return "CantChange";
            }
        }
          return "Able";
    }

    public void RemoveProduct(int product_id) {
        ItemsID_ItemsIDVendor.forEach((Id,Id_Sup)->{
            if(Id_Sup==product_id){
                ItemsID_ItemsIDVendor.remove(Id);
            }
        });
        ItemsID_NumberOfItems.forEach((Id,num)->{
            if(Id==product_id){
                ItemsID_NumberOfItems.remove(Id);
            }
        });
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public void ChangeOrder(int id_order, int id_suplaier, LinkedList<Integer> day, Map<Integer, Integer> productID_IDSupplier, Map<Integer, Integer> itemsIDVendor_numberOfItems) {
    this.Day=day;
    this.ID_Vendor=id_suplaier;
        productID_IDSupplier.forEach((Id,Id_sup)->{
            productID_IDSupplier.put(Id,Id_sup);
    });
    itemsIDVendor_numberOfItems.forEach((Id,num)->{
        ItemsID_NumberOfItems.put(Id,num);
    });

    }
}
