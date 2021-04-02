package com.codecool.shop.model;

import com.codecool.shop.dao.UserDao;

public class User extends BaseModel {
    private String email;


    public User(String name, String email) {
        super(name);
        this.email = email;
    }

    public boolean authenticateWithPassword(String password, UserDao userData) {
        try {
            String databasePwd = userData.getPasswordFor(this.name);
            if (databasePwd.equals(password))
                return true;
            else
                return false;
        } catch (IllegalArgumentException err) {
            err.printStackTrace();
            return false;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
