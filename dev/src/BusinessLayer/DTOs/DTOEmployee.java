package BusinessLayer.DTOs;

public abstract class DTOEmployee {
    private String name;
    private String id;

    public DTOEmployee(String name, String id){
        this.name = name;
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){return this.name;}
}