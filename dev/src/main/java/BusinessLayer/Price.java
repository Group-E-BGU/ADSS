package BusinessLayer;

import DataAccesslayer.DALPrice;

public class Price {

    private int retailPrice;
    private int storePrice;
    private static DALPrice dalPrice;


    public int getId() {
        return id;
    }

    private int id;//highest id is current

    public Price(int retail, int store) {
        dalPrice = new DALPrice();
        retailPrice = retail;
        storePrice = store;
        id = dalPrice.getMaxPriceId()+1;
    }

    public void savePrice(int IRID){
        dalPrice.InsertPrice(id,IRID,storePrice,retailPrice);
    }

    public static int getCurrId(int IRID) {
        return DALPrice.getCurrId(IRID);
    }

    public int getStorePrice() {
        return storePrice;
    }
    public int getRetailPrice(){
        return retailPrice;
    }

    public String toString(){
        return "retail price "+ retailPrice + " store price "+ storePrice;
    }
}
