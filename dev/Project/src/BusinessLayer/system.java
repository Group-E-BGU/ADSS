package BusinessLayer;

import DataAccesslayer.Mapper;
import DataAccesslayer.MapperStore;
import DataAccesslayer.MapperUser;

public class system {

    MapperUser MapUser;
    MapperStore MapStore;

    public system() {
        MapUser = new MapperUser();
        MapStore= new MapperStore();
    }

    public String CheckEmailExist(String email) {
        String s= MapUser.CheckEmailExist(email);
        return s;
    }

    public String Register(String address, String name, String phoneNumber, String password) {
    String s=CheckEmailExist(address);
    if(s.equals("Not Exist")){ //todo- add to the database information
        MapUser.WriteUser(address,password);
        MapStore.WriteStore(address, 0, 0, 0 );
        return "Done";
    }
    return s;
    }

    public String Login(String email, String password) {
        String s=CheckEmailExist(email);
        if(s.equals("Exist")){
            s= MapUser.CheckCorrectPassword(email,password);
            if(s.equals("correct")){
                return "Done";
            }
        }
        return s;
    }



}
