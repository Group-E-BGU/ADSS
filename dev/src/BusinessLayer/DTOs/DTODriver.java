package BusinessLayer.DTOs;

public class DTODriver extends DTOEmployee{
    private String license;

    public DTODriver(String name, String id, String license){
        super(name, id);

        this.license = license;
    }

    //addition
	public String getName() {
		return this.getName();
	}

	public String getLicense() {
		return this.license;
	}


}