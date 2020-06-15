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

    public String Register(String email, String password) {
    String s=CheckEmailExist(email);
    if(s.equals("Not Exist")){
        MapUser.WriteUser(email,password);
        MapStore.WriteStore(email, 0, 0, 0 );
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
