package BusinessLayer.BLObjects;

import java.util.LinkedList;

public class Document{
    private int TruckWeight;
    private LinkedList<String> logs;

    public Document(){
    }

    public void setLogs(LinkedList<String> logs) {
        this.logs = logs;
    }

    public LinkedList<String> getLogs() {
        return logs;
    }

    public int getTruckWeight() {
        return TruckWeight;
    }

    public void setTruckWeight(int truckWeight) {
        TruckWeight = truckWeight;
    }

    public void log(String log){
        this.logs.add(log);
    }
}