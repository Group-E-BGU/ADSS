package BusinessLayer.BLObjects;

public class Adress{
    private Location location;
    private String contactName;
    private String phoneNumber;

    public Adress(Location location, String contactName, String phoneNumber){
        this.location = location;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Location getLocation() {
        return location;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}