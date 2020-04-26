package BusinessLayer.BLObjects;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class Driver extends Employee{
    private String license;
    private ConcurrentHashMap<Adress, LinkedList<Product>> destinationsAndProducts;
    private boolean available;

    public Driver(String name, String id, String license){
        super(name, id);
        this.destinationsAndProducts = new ConcurrentHashMap<Adress, LinkedList<Product>>();
        this.license = license;
        this.available = true;
    }

    public String getLicense() {
        return license;
    }

    public ConcurrentHashMap<Adress, LinkedList<Product>> getDestinationsAndProducts() {
        return destinationsAndProducts;
    }

    public void setDestinationsAndProducts(ConcurrentHashMap<Adress, LinkedList<Product>> destinationsAndProducts) {
        this.destinationsAndProducts = destinationsAndProducts;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public boolean isAvailable(){return this.available;}
}