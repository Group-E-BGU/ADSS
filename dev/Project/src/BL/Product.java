package BL;

public class Product {

    private String name;
    private String CN;
    private int weight;

    public Product(String name, String CN, int weight){
        this.name = name;
        this.CN = CN;
        this.weight = weight;
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


}
