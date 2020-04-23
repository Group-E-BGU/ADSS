package BusinessLogic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Contract {

    private int Suplaier_ID;
    private boolean FixeDays;
    private List<String> Dayes;
    private boolean leading;
    private Map<Integer, Integer> ItemsID_ItemsIDSupplier;
    private Map<Integer, String> ProductIDVendor_Name;
    private Map<Integer, Double> productIDVendor_Price;

    public Contract(int suplaier_ID, boolean fixeDays,List<String> dayes, boolean leading,
                    Map<Integer, String> productIDSupplier_name, Map<Integer, Integer> ItemsID_ItemsIDsupplier,
                    Map<Integer, Double> producttemsIDSupplier_price) {
        Suplaier_ID = suplaier_ID;
        FixeDays = fixeDays;
        Dayes=dayes;
        this.leading = leading;
        ProductIDVendor_Name=productIDSupplier_name;
        ItemsID_ItemsIDSupplier=ItemsID_ItemsIDsupplier;
        productIDVendor_Price=producttemsIDSupplier_price;
    }

    public double GetPrice(int Id){
       AtomicReference<Double> p= new AtomicReference<>((double) 1);
        productIDVendor_Price.forEach((id,Price)->{
            if(id==Id){
                p.set(Price);
            }
        });
        return p.get();
    }


    public int GetIdSup(Integer id) {
        AtomicInteger Id= new AtomicInteger(-1);
        ItemsID_ItemsIDSupplier.forEach((I,P)->{
            if(P== id){
            Id.set(I);
            }
        });
        return Id.get();
    }

    public void setSuplaier_ID(int suplaier_ID) {
        Suplaier_ID = suplaier_ID;
    }

    public void setFixeDays(boolean fixeDays) {
        FixeDays = fixeDays;
    }

    public void setDayes(List<String> dayes) {
        Dayes = dayes;
    }

    public void setLeading(boolean leading) {
        this.leading = leading;
    }

    public void setItemsID_ItemsIDSupplier(Map<Integer, Integer> itemsID_ItemsIDSupplier) {
        ItemsID_ItemsIDSupplier = itemsID_ItemsIDSupplier;
    }

    public void setProductIDVendor_Name(Map<Integer, String> productIDVendor_Name) {
        ProductIDVendor_Name = productIDVendor_Name;
    }

    public void setProductIDVendor_Price(Map<Integer, Double> producttemsIDVendor_Price) {
        this.productIDVendor_Price = producttemsIDVendor_Price;
    }

    public Map<Integer, Integer> getItemsID_ItemsIDSupplier() {
        return ItemsID_ItemsIDSupplier;
    }

    public int getSuplaier_ID() {
        return Suplaier_ID;
    }

    public boolean isFixeDays() {
        return FixeDays;
    }

    public List<String> getDayes() {
        return Dayes;
    }

    public boolean isLeading() {
        return leading;
    }

    public Map<Integer, String> getProductIDVendor_Name() {
        return ProductIDVendor_Name;
    }

    public Map<Integer, Double> getProductIDVendor_Price() {
        return productIDVendor_Price;
    }
}
