package BusinessLayer;

import java.util.HashMap;
import java.util.Map;

public class Data
{
    private static Data data_instance = null;
    private Map<String,Address> addresses;    // the key is the Location string
    private Map<String,Truck> trucks;         // the key is the truck's serial number
    private Map<String,Product> products;
    private Map<Integer,Delivery> deliveries;

    private Data()
    {
        addresses = new HashMap<>();
        trucks = new HashMap<>();
        products = new HashMap<>();
        deliveries = new HashMap<>();
    }

    public static Data getInstance() {
        if (data_instance == null) {
            data_instance = new Data();
        }
        return data_instance;
    }

    public Map<String,Address> getAddresses()
    {
        return addresses;
    }

    public Map<String,Truck> getTrucks()
    {
        return trucks;
    }

    public Map<String,Product> getProducts()
    {
        return products;
    }

    public void setAddresses(Map<String,Address> addresses)
    {
        this.addresses = addresses;
    }

    public void setTrucks(Map<String,Truck> trucks)
    {
        this.trucks = trucks;
    }

    public void setProducts(Map<String,Product> products)
    {
        this.products = products;
    }

    public void setDeliveries(Map<Integer,Delivery> deliveries)
    {
        this.deliveries = deliveries;
    }


    public Map<Integer,Delivery> getDeliveries()
    {
        return deliveries;
    }
    public Truck getProperTruck(int totalWeight) {

        return null;
    }

    public Driver getProperDriver(int totalWeight) {
        return null;
        // todo
    }
}
