package BL;

import java.text.SimpleDateFormat;
import java.util.*;

public class Delivery{
    public static int counter=0;
    private int delivery_id;
    private Date date;
    private String source;
    private String truckSerialNumber;
    private int driverID;
    private Map<String, Document> documents;
    private List<String> logs;
    private int truckWeight;

    public Delivery(Date date, String source, String truckSerialNumber, int driverID , int truckWeight){
        this.delivery_id = counter++;
        this.date = date;
        this.source = source;
        this.truckSerialNumber = truckSerialNumber;
        this.driverID = driverID;
        this.truckWeight = truckWeight;
        this.documents = new HashMap<>();
        this.logs = new LinkedList<>();
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

    public void setDeliveryId(int id) {
        this.delivery_id = id;
    }

    public int getDelivery_id() {
        return delivery_id;
    }

    @Override
    public String toString()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String delivery_string = "Delivery id : " + delivery_id + '\n';
        delivery_string = delivery_string + "Delivery date : " + dateFormat.format(date) + '\n';
        dateFormat = new SimpleDateFormat("EEEE");
        delivery_string = delivery_string + "Truck id : " + truckSerialNumber+ '\n';
        delivery_string = delivery_string +"Source Address : "+source +'\n';
        delivery_string = delivery_string + "Driver id : " + driverID + '\n';
        delivery_string = delivery_string + "Destinations : ";
        for(String location : documents.keySet())
        {
            delivery_string = delivery_string + location + "     ";
        }



        delivery_string = delivery_string + "\n";

        return delivery_string;

    }
}