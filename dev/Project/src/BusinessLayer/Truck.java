package BusinessLayer;

public class Truck {


    private String serialNumber;
    private String model;
    private int weight;
    private int maxAllowedWeight;


    public Truck(String serialNumber, String model, int weight, int maxAllowedWeight){
        this.serialNumber=serialNumber;
        this.model = model;
        this.weight = weight;
        this.maxAllowedWeight = maxAllowedWeight;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public int getMaxAllowedWeight() {
        return maxAllowedWeight;
    }

    public int getWeight() {
        return weight;
    }

    public String getModel() {
        return model;
    }

    public void setMaxAllowedWeight(int maxAllowedWeight) {
        this.maxAllowedWeight = maxAllowedWeight;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
