package BusinessLayer.BLObjects;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    private LinkedList<Adress> adresses;
    private LinkedList<Driver> drivers;
    private LinkedList<Truck> trucks;
    private ConcurrentHashMap<String, LinkedList<Product>> adressesAndProducts;
    private LinkedList<Delivery> deliveries;

    public Data() {
        this.adresses = new LinkedList<Adress>();
        this.drivers = new LinkedList<Driver>();
        this.trucks = new LinkedList<Truck>();
        this.adressesAndProducts = new ConcurrentHashMap<String, LinkedList<Product>>();
        this.deliveries = new LinkedList<>();
    }

    public LinkedList<Adress> getAdresses() {
        return adresses;
    }

    public LinkedList<Driver> getDrivers() {
        return drivers;
    }

    public LinkedList<Truck> getTrucks() {
        return trucks;
    }

    public void setAdresses(LinkedList<Adress> adresses) {
        this.adresses = adresses;
    }

    public void setDrivers(LinkedList<Driver> drivers) {
        this.drivers = drivers;
    }

    public void setTrucks(LinkedList<Truck> trucks) {
        this.trucks = trucks;
    }

    public boolean addAdress(Adress adress){
        this.adresses.add(adress);
        return true;
    }

    ////////////////////
    public boolean addDriver(Driver driver){
        this.drivers.add(driver);
        return true;
    }

    public boolean addTruck(Truck truck){
        this.trucks.add(truck);
        return true;
    }

    public boolean addProoduct(Product product, String adress){
        for(Adress a : this.adresses){
            if((a.getLocation().getLocation().compareTo(adress) == 0)){
                boolean flag = false;

                for(Product p : this.adressesAndProducts.get(adress))
                    if(p.getName().compareTo(product.getName()) == 0){
                        p.setAmount(p.getAmount() + product.getAmount());
                        flag = true;
                    }

                if(!flag)
                    this.adressesAndProducts.get(adress).add(product);
            }
        }

        return false;
    }

    public boolean addProduct(Product product, String adress) {
        boolean flag = false;

        for(Adress a : this.adresses){
            if(a.getLocation().getLocation().compareTo(adress) == 0)
                flag = true;
        }

        if(!flag)
            return false;

        if(this.adressesAndProducts.get(adress) == null){
            LinkedList<Product> products = new LinkedList<>();
            this.adressesAndProducts.put(adress, products);
        }

        flag = false;

        for(Product p : this.adressesAndProducts.get(adress)){
            if(p.getCN().compareTo(product.getCN()) == 0) {
                flag = true;
                p.setAmount(p.getAmount() + product.getAmount());
            }
        }

        if(!flag)
            this.adressesAndProducts.get(adress).add(new Product(product.getName(), product.getCN(), product.getWeight(), product.getAmount()));

        return true;
    }

    public Adress getAdress(String adress) {
        for(Adress a : this.adresses){
            if(a.getLocation().getLocation().compareTo(adress) == 0){
                return a;
            }
        }

        return null;
    }

    public Driver getDriverByLicense(int totalWeight) {
        for(Driver driver : this.drivers){
            if(isAllowed(driver.getLicense(), totalWeight) & driver.isAvailable())
                return driver;
        }

        return null;
    }

    private boolean isAllowed(String license, int weight){
        return weight < 12 |
                (weight < 15 & license.compareTo("C") != 0) |
                license.compareTo("C15") == 0;
    }

    public void addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
    }
    
}