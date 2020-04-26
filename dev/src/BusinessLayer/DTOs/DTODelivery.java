package BusinessLayer.DTOs;

import BusinessLayer.BLObjects.Delivery;

import java.util.Date;
import java.util.LinkedList;

public class DTODelivery {
    private Date date;
    private DTOAdress source;
    private LinkedList<DTOAdress> destinations;
    private String launchTime;
    private String truckSerialNumber;
    private String driverName;

    public DTODelivery(Date date, DTOAdress source, LinkedList<DTOAdress> destinations, String launchTime, String truckSerialNumber, String driverName){
        this.date = date;
        this.source = source;
        this.destinations = destinations;
        this.launchTime = launchTime;
        this.truckSerialNumber = truckSerialNumber;
        this.driverName = driverName;
    }

    public DTODelivery(Delivery delivery){
        this.date = delivery.getDate();
        this.source = new DTOAdress(delivery.getSource());
        this.launchTime = delivery.getLaunchTime();
        this.truckSerialNumber = delivery.getTruckSerialNumber();
        this.driverName = delivery.getDriverName();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDestinations(LinkedList<DTOAdress> destinations) {
        this.destinations = destinations;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }

    public void setSource(DTOAdress source) {
        this.source = source;
    }

    public void setTruckSerialNumber(String truckSerialNumber) {
        this.truckSerialNumber = truckSerialNumber;
    }

    public DTOAdress getSource() {
        return source;
    }

    public Date getDate() {
        return date;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getLaunchTime() {
        return launchTime;
    }

    public String getTruckSerialNumber() {
        return truckSerialNumber;
    }

    public LinkedList<DTOAdress> getDestinations() {
        return destinations;
    }
}