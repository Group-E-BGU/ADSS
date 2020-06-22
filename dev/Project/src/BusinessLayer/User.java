package BusinessLayer;

import DataAccesslayer.Mapper;

import java.util.List;

public class User {

    public enum UserType{
        Master,Logistic,
        Stock,StoreManager,WorkersManager

    }

    private String email;
    private String password;
    private UserType userType;
    private static Mapper Map;

    public User(String email, String password ,UserType userType )
    {
        Map = new Mapper();
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public UserType getUserType()
    {
        return userType;
    }

    public void setUserType(UserType userType)
    {
        this.userType = userType;
    }
}
