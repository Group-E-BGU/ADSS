package BusinessLayer.DTOs;

public class DTOProduct{
    String name;
    String CN;
    int weight;
    int amount;
    String adress;

    public DTOProduct(String name, String CN, int weight, int amount, String adress){
        this.name = name;
        this.CN = CN;
        this.weight = weight;
        this.amount = amount;
        this.adress = adress;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}