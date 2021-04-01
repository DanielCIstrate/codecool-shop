package com.codecool.shop.dao;

import com.codecool.shop.model.User;

import java.util.List;

public interface UserDao {

    void add(User user, String password);
    User find(int id);
    void remove(int id);

    List<User> getAll();
    String getPasswordFor(String username);
    String setPasswordFor(String username);
    void updateUser(User user);
}
