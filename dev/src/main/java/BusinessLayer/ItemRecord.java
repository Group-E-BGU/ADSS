package BusinessLayer;

import java.util.LinkedList;

public class ItemRecord {

    private String name;
    private int id;
    private int minAmount;
    private int storageAmount;
    private int shelfAmount;
    private int totalAmount;
    private int shelfNumber;
    private String manufacture;
    private LinkedList<Item> items;
    private LinkedList<Price> prices; //first is current

    public ItemRecord(String name,int id, int minAmount, int storageAmount, int shelfAmount, int totalAmount, int shelfNumber, String manufacture) {
        this.name = name;
        this.id = id;
        this.minAmount = minAmount;
        this.storageAmount = storageAmount;
        this.shelfAmount = shelfAmount;
        this.totalAmount = totalAmount;
        this.shelfNumber = shelfNumber;
        this.manufacture = manufacture;
        items = new LinkedList<>();
        prices = new LinkedList<>();
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void addPrice(Price price){
        prices.addFirst(price);
    }

    public String getAmounts() {
        return "storage amount : "+ storageAmount + "\nshelf amount : "+ shelfAmount +"\ntotal amount : "+totalAmount;
    }

    public void setStorageAmount(int newAmount) {
        storageAmount = newAmount;
    }

    public void setShelfAmount(int newAmount) {
        shelfAmount = newAmount;
    }

    public void setTotalAmount(int newAmount) {
        while(totalAmount > newAmount){
            items.removeLast();
            totalAmount--;
        }
        while(totalAmount < newAmount){
            items.addFirst(new Item());
            totalAmount++;
        }
        if(totalAmount < minAmount){
            Store store = Store.getInstance();
            if (store != null) {
                store.sendWarning(this);
                store.createAutomaticOrder(id,minAmount);
            }
        }
    }

    public String moveToShelf(int parseInt) {
        if(storageAmount < parseInt) {
            return "Not enough in storage";
        }
        storageAmount -= parseInt;
        shelfAmount += parseInt;
        return "New storage amount : "+ storageAmount + "\nNew shelf amount : "+ shelfAmount;
    }

    public String subtractFromShelf(int amount) {
        if(amount > shelfAmount) {
            return "Not enough on shelf";
        }
        shelfAmount -= amount;
        totalAmount -= amount;
        while (amount > 0){
            items.removeLast();
            amount--;
        }
        if(totalAmount < minAmount){
            Store store = Store.getInstance();
            if (store != null) {
                store.sendWarning(this);
                store.createAutomaticOrder(id,minAmount);
            }
        }
        return getAmounts();
    }

    public String getName() {
        return name;
    }

    public int getShelfAmount() {
        return shelfAmount;
    }

    public int getStorageAmount() {
        return storageAmount;
    }

    public String getPrices() {
        if(prices.isEmpty()) {
            return "";
        }
        return prices.getFirst().toString();
    }

    public Price getCurrPrice(){
        return prices.getFirst();
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public LinkedList<Item> getItems() {
        return items;
    }

    public int getShelfNumber(){return shelfNumber;}

    public int getId() {
        return id;
    }
}
