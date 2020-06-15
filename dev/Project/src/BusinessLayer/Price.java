package BusinessLayer;

import DataAccesslayer.MapperPrice;

public class Price {

    private int retailPrice;
    private int storePrice;
    private static MapperPrice mapperPrice;


    public int getId() {
        return id;
    }

    private int id;//highest id is current

    public Price(int retail, int store) {
        mapperPrice = new MapperPrice();
        retailPrice = retail;
        storePrice = store;
        id = mapperPrice.getMaxPriceId()+1;
    }

    public Price(int id,int retail, int store) {
        mapperPrice = new MapperPrice();
        retailPrice = retail;
        storePrice = store;
        this.id = id;
    }

    public void savePrice(int IRID){
        mapperPrice.InsertPrice(id,IRID,storePrice,retailPrice);
    }

    public static Price getCurrId(int IRID) {
        return MapperPrice.getCurrId(IRID);
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
