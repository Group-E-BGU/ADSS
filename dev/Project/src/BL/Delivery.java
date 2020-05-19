package BL;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Delivery{
    private int delivery_id;
    private Date date;
    private Address source;
    private List<Address> destinations;
    private String launchTime;
    private String truckSerialNumber;
    private int driverID;
    private List<Document> documents;
    private List<String> logs;

    public Delivery(int delivery_id , Date date, Address source, List<Address> destinations, String launchTime, String truckSerialNumber, int driverID){
        this.delivery_id = delivery_id;
        this.date = date;
        this.source = source;
        this.destinations = destinations;
        this.launchTime = launchTime;
        this.truckSerialNumber = truckSerialNumber;
        this.driverID = driverID;
        this.documents = new LinkedList<>();
    }

    public Delivery(){}

    public int getDeliveryID()
    {
        return delivery_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDestinations(List<Address> destinations) {
        this.destinations = destinations;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }

    public void setSource(Address source) {
        this.source = source;
    }

    public void setTruckSerialNumber(String truckSerialNumber) {
        this.truckSerialNumber = truckSerialNumber;
    }

    public Address getSource() {
        return source;
    }

    public Date getDate() {
        return date;
    }

    public int getDriverID() {
        return driverID;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public String getTruckSerialNumber() {
        return truckSerialNumber;
    }

    public List<Address> getDestinations() {
        return destinations;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}