
package BusinessLayer.BLObjects;

import java.util.Date;
import java.util.LinkedList;

public class Delivery{
    private Date date;
    private Adress source;
    private LinkedList<Adress> destinations;
    private String launchTime;
    private String truckSerialNumber;
    private String driverName;

    public Delivery(Date date, Adress source, LinkedList<Adress> destinations, String launchTime, String truckSerialNumber, String driverName){
        this.date = date;
        this.source = source;
        this.destinations = destinations;
        this.launchTime = launchTime;
        this.truckSerialNumber = truckSerialNumber;
        this.driverName = driverName;
    }

    public Delivery(){}

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDestinations(LinkedList<Adress> destinations) {
        this.destinations = destinations;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setLaunchTime(String launchTime) {
        this.launchTime = launchTime;
    }

    public void setSource(Adress source) {
        this.source = source;
    }

    public void setTruckSerialNumber(String truckSerialNumber) {
        this.truckSerialNumber = truckSerialNumber;
    }

    public Adress getSource() {
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

    public LinkedList<Adress> getDestinations() {
        return destinations;
    }

    public void setDriver(Driver driver) {
        this.driverName = driver.getName();
    }
}