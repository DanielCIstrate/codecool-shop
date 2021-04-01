package com.codecool.shop.dao;

import com.codecool.shop.model.User;

import java.util.List;
import java.util.Set;

public interface UserDao {

    void add(User user, String password);
    User find(int id);
    void remove(int id);

    List<User> getAll();
    Set<String> getUsernameSet();
    String getPasswordFor(String username);
    void setPasswordFor(String username, String newPassword);
    void updateUser(User user);
}
