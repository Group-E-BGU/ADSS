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

    public String UserAddressExist(String address) {
        String s= MapUser.UserAddressExist(address);
        return s;
    }

    public String Register(String address,String password) {
    String s=UserAddressExist(address);
    if(s.equals("Not Exist")){
        MapUser.WriteUser(address,password);
        MapStore.WriteStore(address, 0, 0, 0 );
        return "Done";
    }
    return s;
    }

    public String Login(String address, String password) {
        String s=UserAddressExist(address);
        if(s.equals("Exist")){
            s= MapUser.CheckCorrectPassword(address,password);
            if(s.equals("correct")){
                return "Done";
            }
        }
        return s;
    }



}
