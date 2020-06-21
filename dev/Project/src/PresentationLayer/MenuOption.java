package PresentationLayer;

import BusinessLayer.User.UserType;

public class MenuOption {

    private String option_text;
    private UserType[] allowed_users;

    public MenuOption(String option_text , UserType[] allowed_users)
    {

        this.option_text = option_text;
        this.allowed_users = allowed_users;

    }

    public String getOption_text()
    {
        return option_text;
    }

    public UserType[] getAllowed_users()
    {
        return allowed_users;
    }
}
