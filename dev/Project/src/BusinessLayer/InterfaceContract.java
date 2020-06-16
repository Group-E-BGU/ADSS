package BusinessLayer;

import java.util.List;
import java.util.Map;

public class InterfaceContract {

   public int Suplaier_ID;
    public boolean FixeDays;
    public List<Integer> Days;
    public boolean leading;
    public Map<Integer, Integer> ItemsID_ItemsIDSupplier;
    public Map<Integer, String> ProductIDVendor_Name;
    public Map<Integer, Double> productIDVendor_Price;

    public InterfaceContract(int suplaier_ID, boolean fixeDays, List<Integer> dayes, boolean leading,
                             Map<Integer, String> productIDSupplier_name, Map<Integer, Integer> ItemsID_ItemsIDsupplier,
                             Map<Integer, Double> producttemsIDSupplier_price) {
        Suplaier_ID = suplaier_ID;
        FixeDays = fixeDays;
        Days=dayes;
        this.leading = leading;
        ProductIDVendor_Name=productIDSupplier_name;
        ItemsID_ItemsIDSupplier=ItemsID_ItemsIDsupplier;
        productIDVendor_Price =producttemsIDSupplier_price;


    }
}
