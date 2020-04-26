package BusinessLayer.BLObjects;



public class Product{
    String name;
    String CN;
    int weight;
    private int amount;

    public Product(String name, String CN, int weight, int amount){
        this.name = name;
        this.CN = CN;
        this.weight = weight;
        this.amount = amount;
    }

    public int getWeight() {
        return weight;
    }

    public String getCN() {
        return CN;
    }

    public String getName() {
        return name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}