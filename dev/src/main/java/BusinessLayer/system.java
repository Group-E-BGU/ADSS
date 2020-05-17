package BusinessLayer;

import DataAccesslayer.Mapper;

public class system {

    Mapper Map;

    public system() {
        Map = new Mapper();
    }

    public String CheckEmailExist(String email) {
        String s=Map.CheckEmailExist(email);
        return s;
    }

    public String Register(String email, String password) {
    String s=CheckEmailExist(email);
    if(s.equals("Not Exist")){
        Map.WriteUser(email,password);
        Map.WriteStore(email, 0, 0, 0 );
        return "Done";
    }
    return s;
    }

    public String Login(String email, String password) {
        String s=CheckEmailExist(email);
        if(s.equals("Exist")){
            s=Map.CheckCorrectPassword(email,password);
            if(s.equals("correct")){
                return "Done";
            }
        }
        return s;
    }

}
