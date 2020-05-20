package BL;

import java.util.*;

public class Delivery{
    private int delivery_id;
    private Date date;
    private String source;
    private String truckSerialNumber;
    private int driverID;
    private Map<String, Document> documents;
    private List<String> logs;
    private int truckWeight;

    public Delivery(int delivery_id , Date date, String source, String truckSerialNumber, int driverID){
        this.delivery_id = delivery_id;
        this.date = date;
        this.source = source;
        this.truckSerialNumber = truckSerialNumber;
        this.driverID = driverID;
        this.documents = new HashMap<>();
    }

    public Delivery(){}

    public int getDeliveryID()
    {
        return delivery_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

//    public void setDestinations(List<String> destinations) {
//        this.destinations = destinations;
//    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTruckSerialNumber(String truckSerialNumber) {
        this.truckSerialNumber = truckSerialNumber;
    }

    public String getSource() {
        return source;
    }

    public Date getDate() {
        return date;
    }

    public int getDriverID() {
        return driverID;
    }

    public String getTruckSerialNumber() {
        return truckSerialNumber;
    }

//    public List<String> getDestinations() {
//        return destinations;
//    }


    public void setDocuments(Map<String, Document> documents) {
        this.documents = documents;
    }

    public Map<String, Document> getDocuments() {
        return documents;
    }

    public void log(String s) {
        this.logs.add(s);
    }

    public void setTruckWeight(int truckWeight) {
        this.truckWeight = truckWeight;
    }

    public int getTruckWeight(){
        return this.truckWeight;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public List<String> getLogs() {
        return logs;
    }
}