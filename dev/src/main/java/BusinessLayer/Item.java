package BusinessLayer;

public class Item {

    private int id;
    private java.sql.Date expirationDate;
    private boolean defective;

    public Item(int id, java.sql.Date expirationDate){
        this.id = id;
        this.expirationDate = expirationDate;
        defective = false;
    }

    public int getId() {
        return id;
    }

    public void setDefective(boolean isDefective) {
        defective = isDefective;
    }

    public String toString(){
        String desc = "Item ID: " + id
                + " Expiration date: " + expirationDate;
        if (defective){
            desc = desc + " The item is defected\n";
        }
        else {
            desc = desc + " The item is not defected\n";
        }
        return desc;
    }

    public java.sql.Date getExpirationDate() {return expirationDate;}

    public boolean isDefective(){return defective;}
}
