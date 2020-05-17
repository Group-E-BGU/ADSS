package BusinessLayer;

import java.nio.IntBuffer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Wrotequantities {
    private int Suplaier_ID;
    private Map<Integer, Integer> ItemsID_Amount;
    private Map<Integer, Double> ItemsID_Assumption;

    public Wrotequantities(int suplaier_ID, Map<Integer, Integer> itemsID_Amount, Map<Integer, Double> itemsID_Assumption) {
        Suplaier_ID = suplaier_ID;
        ItemsID_Amount = itemsID_Amount;
        ItemsID_Assumption = itemsID_Assumption;
    }


    public int GetSale(Integer id, Integer num) {
        AtomicInteger Sale= new AtomicInteger();
        ItemsID_Amount.forEach((ID,Amount)->{
            if(ID==id&&num>=Amount){
               double tmp=ItemsID_Assumption.get(id);
               int tmm=(int)tmp;
                Sale.set(tmm);
            }
        });
        return Sale.get();
    }

    public void setItemsID_Amount(Map<Integer, Integer> itemsID_Amount) {
        ItemsID_Amount = itemsID_Amount;
    }

    public void setItemsID_Assumption(Map<Integer, Double> itemsID_Assumption) {
        ItemsID_Assumption = itemsID_Assumption;
}

    public int getSuplaier_ID() {
        return Suplaier_ID;
    }

    public Map<Integer, Integer> getItemsID_Amount() {
        return ItemsID_Amount;
    }

    public Map<Integer, Double> getItemsID_Assumption() {
        return ItemsID_Assumption;
    }
}
