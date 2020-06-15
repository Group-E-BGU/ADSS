package BusinessLayer;

import DataAccesslayer.Mapper;

import java.util.List;

public class User {
    private String email;
    private String password;
    private static Mapper Map;

    public User(String email, String password)
    {
        Map = new Mapper();
        this.email = email;
        this.password = password;
    }
}
