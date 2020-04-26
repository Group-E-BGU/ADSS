package BusinessLayer.DTOs;

import BusinessLayer.BLObjects.Adress;

public class DTOAdress {
    private String location;
    private String contactName;
    private String phoneNumber;

    public DTOAdress(String location, String contactName, String phoneNumber){
        this.location = location;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
    }

    public DTOAdress(Adress adress){
        this.location = adress.getLocation().getLocation();
        this.contactName = adress.getContactName();
        this.phoneNumber = adress.getPhoneNumber();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}